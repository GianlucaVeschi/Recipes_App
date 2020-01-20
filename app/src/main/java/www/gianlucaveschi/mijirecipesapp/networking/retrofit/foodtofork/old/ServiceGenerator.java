package www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.old;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.old.RecipeApi;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.optimized.LiveDataCallAdapterFactory;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;

/**
 * Modeled after @brittbarak's example on Github
 *
 https://github.com/brittBarak/NetworkingDemo
 * https://twitter.com/brittbarak
 */
public class ServiceGenerator {

    private static Retrofit.Builder retrofitBuilder =
            new Retrofit.Builder()
                    .baseUrl(Constants.RECIPES_BASE_URL)
                    .addCallAdapterFactory(new LiveDataCallAdapterFactory())
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = retrofitBuilder.build();

    private static RecipeApi recipeApi = retrofit.create(RecipeApi.class);

    public static RecipeApi getRecipeApi() {
        return recipeApi;
    }
}