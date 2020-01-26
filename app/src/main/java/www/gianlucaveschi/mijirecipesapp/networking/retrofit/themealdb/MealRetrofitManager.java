package www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb;

import android.content.Context;
import android.util.Log;

import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.logging.HttpLoggingInterceptor;
import www.gianlucaveschi.mijirecipesapp.MyApplication;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.RecipeAPI;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.call_adapters.LiveDataCallAdapterFactory;
import www.gianlucaveschi.mijirecipesapp.networking.volley.VolleyNetworkManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static www.gianlucaveschi.mijirecipesapp.utils.Constants.HEADER_CACHE_CONTROL;
import static www.gianlucaveschi.mijirecipesapp.utils.Constants.HEADER_PRAGMA;
import static www.gianlucaveschi.mijirecipesapp.utils.Constants.MEALS_BASE_URL;

public class MealRetrofitManager {

    private static final String TAG = "MealRetrofitManager";

    /*------------------------------- INTERNAL VARIABLES -----------------------------------------*/

    //Retrofit Instance initialized only once
    private static Retrofit retrofit = null;
    private static Gson gson = new GsonBuilder().create();
    private static MealAPI mealAPI;

    /*------------------------------- RETROFIT BUILDER -------------------------------------------*/

    public static synchronized Retrofit getClient(Context context){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(MEALS_BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(okHttpClient())
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                    .build();
        }
        return retrofit;
    }

    //Context only has to be passed once
    public static synchronized Retrofit getClient() {
        if (null == retrofit) {
            throw new IllegalStateException(MealRetrofitManager.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return retrofit;
    }

    /*------------------------------- OkHTTP BUILDER ---------------------------------------------*/

    private static OkHttpClient okHttpClient(){
        return new OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor())    // used if network off OR on
                //.addNetworkInterceptor(networkInterceptor()) // only used when network is on
                //.addInterceptor(offlineInterceptor())
                .build();
    }

    /*------------------------------- GETTERS ----------------------------------------------------*/

    public static MealAPI getMealAPI() {
        if(mealAPI == null){
            mealAPI = getClient().create(MealAPI.class);
        }
        return mealAPI;
    }

    /*------------------------------- INTERCEPTORS -----------------------------------------------*/

    private static HttpLoggingInterceptor httpLoggingInterceptor () {
        HttpLoggingInterceptor httpLoggingInterceptor =
                new HttpLoggingInterceptor( new HttpLoggingInterceptor.Logger() {
                    @Override
                    public void log (String message) {
                        Log.d(TAG, "log: http log: " + message);
                    }
                } );
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    //This interceptor will be called ONLY if the network is available
    private static Interceptor networkInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Log.d(TAG, "network interceptor: called.");

                Response response = chain.proceed(chain.request());

                //Change some options that have to do with the caching
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxAge(5, TimeUnit.MINUTES)
                        .build();

                return response.newBuilder()
                        .removeHeader(HEADER_PRAGMA)    //This header can potentially avoid using caching
                        .removeHeader(HEADER_CACHE_CONTROL) //Remove Header that comes from the server
                        .header(HEADER_CACHE_CONTROL, cacheControl.toString()) //Apply own Cache Control
                        .build();
            }
        };
    }

    //This interceptor will be called both if the network is available and if it is not
    private static Interceptor offlineInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Log.d(TAG, "offline interceptor: called.");
                Request request = chain.request();

                // prevent caching when network is on. For that we use the "networkInterceptor"
                if (!MyApplication.hasNetwork()) {
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxStale(7, TimeUnit.DAYS)
                            .build();

                    request = request.newBuilder()
                            .removeHeader(HEADER_PRAGMA)
                            .removeHeader(HEADER_CACHE_CONTROL)
                            .cacheControl(cacheControl)
                            .build();
                }

                return chain.proceed(request);
            }
        };
    }
}
