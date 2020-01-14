package www.gianlucaveschi.mijirecipesapp.utils;

import android.util.Log;

import java.util.List;
import java.util.TimerTask;

import www.gianlucaveschi.mijirecipesapp.models.meals.MealContainer;

import retrofit2.Response;
import www.gianlucaveschi.mijirecipesapp.models.recipes.Recipe;

public class MyLogger extends TimerTask {

    public static String logMealContainerResponse(Response<MealContainer> response){
        String result = "Response \n\t" + response + "\n"
                + "Response Body \n\t" + response.body() + "\n";
        return result;
    }

    public static void logRecipes(String tag, List<Recipe> list){
        for(Recipe recipe: list){
            Log.d(tag, "LOG RECIPES: " + recipe.getRecipe_id() + ", " + recipe.getTitle());
        }
    }

    @Override
    public void run() {
        Log.d("MyLogger", "Animation is running");
    }
}
