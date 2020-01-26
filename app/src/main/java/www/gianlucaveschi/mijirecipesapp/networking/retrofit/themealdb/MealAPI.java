package www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb;

import androidx.lifecycle.LiveData;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.responses.ApiResponse;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.responses.MealResponse;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealAPI {

    /*-------------------------------- GET -------------------------------------------------------*/

    @GET("filter.php")
    Call<MealResponse> getMealsByCountry(@Query("a") String country);

    @GET("filter.php")
    Call<MealResponse> getMealsByCategory(@Query("c") String category);

    @GET("filter.php")
    Call<MealResponse> getMealsByIngredient(@Query("i") String ingredient);

    @GET("lookup.php")
    Call<MealResponse> getMealById(@Query("i") String mealID); // TODO: 26/01/2020: test this

    @GET("random.php")
    Call<MealResponse> getRandomMeal();

    /*--------------------------------- GET AS LIVE DATA -----------------------------------------*/

    @GET("filter.php")
    LiveData<ApiResponse<MealResponse>> getMealsByCountryAsLiveData(@Query("a") String country);

    @GET("filter.php")
    LiveData<ApiResponse<MealResponse>> getMealsByCategoryAsLiveData(@Query("c") String category);

    @GET("filter.php")
    LiveData<ApiResponse<MealResponse>> getMealsByIngredientAsLiveData(@Query("i") String ingredient);

    @GET("lookup.php")
    LiveData<ApiResponse<MealResponse>> getMealByIdAsLiveData(@Query("i") String mealID);


}
