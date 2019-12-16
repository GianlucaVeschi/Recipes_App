package www.gianlucaveschi.load_json_images_picasso.utils;

import www.gianlucaveschi.load_json_images_picasso.models.meals.MealContainer;

import retrofit2.Response;

public class MyLogger {

    public static String logResponse(Response<MealContainer> response){
        String result = "Response \n\t" + response + "\n"
                + "Response Body \n\t" + response.body() + "\n";
        return result;
    }



}
