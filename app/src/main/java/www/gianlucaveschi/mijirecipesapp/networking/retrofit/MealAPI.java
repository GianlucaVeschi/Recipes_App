package www.gianlucaveschi.mijirecipesapp.networking.retrofit;

import www.gianlucaveschi.mijirecipesapp.models.meals.MealContainer;
import www.gianlucaveschi.mijirecipesapp.models.meals.MealMap;

import org.json.JSONArray;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MealAPI {

    /*---------------------GET---------------------*/

    @GET("filter.php") //id will be replaced in mainActivity
    Call<MealContainer> getMealsByCountry(@Query("a") String country);

    @GET("lookup.php")
    Call<MealContainer> getMealDetailsAsMealContainer(@Query("i") String mealId);

    //Work In Progress
    @GET("lookup.php")
    Call<JSONArray> getMealDetailsAsJSONArray(@Query("i") String mealId);

    @GET("filter.php")
    Call<MealContainer> getMealsByCategory(@Query("c") String category);

    @GET("filter.php") //id will be replaced in mainActivity
    Call<MealMap> getMealsByCountryAsMealSimple(@Query("a") String country);

    @GET("random.php")
    Call<MealContainer> getRandomMeal();

}
