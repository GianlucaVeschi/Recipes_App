package www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb;

import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.responses.MealResponse;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealAPI {

    /*---------------------GET---------------------*/

    @GET("filter.php") //id will be replaced in mainActivity
    Call<MealResponse> getMealsByCountry(@Query("a") String country);

    @GET("lookup.php")
    Call<MealResponse> getMealDetailsAsMealContainer(@Query("i") String mealId);

    //Work In Progress
    @GET("lookup.php")
    Call<JSONArray> getMealDetailsAsJSONArray(@Query("i") String mealId);

    @GET("filter.php")
    Call<MealResponse> getMealsByCategory(@Query("c") String category);

    @GET("random.php")
    Call<MealResponse> getRandomMeal();

    @GET("filter.php")
    Call<MealResponse> getMealsByIngredient(@Query("i") String ingredient);
}
