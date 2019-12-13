package www.gianlucaveschi.load_json_images_picasso.networking.retrofit;

import www.gianlucaveschi.load_json_images_picasso.models.MealContainer;
import www.gianlucaveschi.load_json_images_picasso.models.MealMap;

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
