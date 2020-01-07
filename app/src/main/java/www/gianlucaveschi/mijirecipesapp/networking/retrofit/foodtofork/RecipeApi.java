package www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.responses.RecipeGetResponse;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.responses.RecipeSearchResponse;

public interface RecipeApi {

    // SEARCH
    @GET("api/search")
    Call<RecipeSearchResponse> searchRecipe(
            @Query("key") String key,
            @Query("q") String query,
            @Query("page") String page
    );

    // GET SPECIFIC RECIPE
    @GET("api/get")
    Call<RecipeGetResponse> getRecipe(
            @Query("key") String key,
            @Query("rId") String recipe_id
    );

}