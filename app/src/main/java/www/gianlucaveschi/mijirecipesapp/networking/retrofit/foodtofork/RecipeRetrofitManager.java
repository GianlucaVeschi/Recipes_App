package www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.call_adapters.LiveDataCallAdapterFactory;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;

import static www.gianlucaveschi.mijirecipesapp.utils.Constants.CONNECTION_TIMEOUT;
import static www.gianlucaveschi.mijirecipesapp.utils.Constants.READ_TIMEOUT;
import static www.gianlucaveschi.mijirecipesapp.utils.Constants.WRITE_TIMEOUT;

/**
 * Modeled after @brittbarak's example on Github
 *
 https://github.com/brittBarak/NetworkingDemo
 * https://twitter.com/brittbarak
 */
public class RecipeRetrofitManager {

    /*------------------------------- OkHTTP BUILDER ---------------------------------------------*/

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            // establish connection with server
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            // time between each byte read from server
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            // time between each byte sent to server
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build();

    /*------------------------------- RETROFIT BUILDER -------------------------------------------*/

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Constants.RECIPES_BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    /*------------------------------- GETTERS -------------------------------------------*/

    private static RecipeAPI recipeApiLiveData = retrofit.create(RecipeAPI.class);

    public static RecipeAPI getRecipeApiLiveData() {
        return recipeApiLiveData;
    }


}