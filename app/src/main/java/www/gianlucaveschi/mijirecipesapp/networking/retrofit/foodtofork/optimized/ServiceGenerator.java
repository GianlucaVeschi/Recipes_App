package www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.optimized;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
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
public class ServiceGenerator {

    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            // establish connection with server
            .connectTimeout(CONNECTION_TIMEOUT, TimeUnit.SECONDS)
            // time between each byte read from server
            .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
            // time between each byte sent to server
            .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)

            .build();

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Constants.RECIPES_BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static RecipeApiLiveData recipeApiLiveData = retrofit.create(RecipeApiLiveData.class);

    public static RecipeApiLiveData getRecipeApiLiveData() {
        return recipeApiLiveData;
    }


}