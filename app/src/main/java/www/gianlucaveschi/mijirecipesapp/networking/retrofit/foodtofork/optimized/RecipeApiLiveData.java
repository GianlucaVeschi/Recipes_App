package www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.optimized;

import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.optimized.ApiResponse;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.responses.RecipeGetResponse;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.responses.RecipeSearchResponse;

public interface RecipeApiLiveData {

    // The following two calls are exactly like the ones above but the responses are Wrapped
    // into ApiResponse wrapped into LiveData so that they can be observed.

    // SEARCH AS LIVE DATA
    @GET("api/search")
    LiveData<ApiResponse<RecipeSearchResponse>> searchRecipeAsLiveData(
            @Query("key") String key,
            @Query("q") String query,
            @Query("page") String page
    );

    // GET RECIPE REQUEST AS LIVE DATA
    @GET("api/get")
    LiveData<ApiResponse<RecipeGetResponse>> getRecipeAsLiveData(
            @Query("key") String key,
            @Query("rId") String recipe_id
    );

}