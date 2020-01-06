package www.gianlucaveschi.mijirecipesapp.utils;

import www.gianlucaveschi.mijirecipesapp.models.meals.MealContainer;

import retrofit2.Response;

public class MyLogger {

    public static String logResponse(Response<MealContainer> response){
        String result = "Response \n\t" + response + "\n"
                + "Response Body \n\t" + response.body() + "\n";
        return result;
    }


}
