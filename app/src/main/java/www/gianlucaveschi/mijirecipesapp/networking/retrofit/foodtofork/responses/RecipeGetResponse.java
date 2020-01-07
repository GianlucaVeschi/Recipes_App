package www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import www.gianlucaveschi.mijirecipesapp.models.recipes.Recipe;

public class RecipeGetResponse {

    @SerializedName("recipe")
    @Expose
    private Recipe recipe;

    public Recipe getRecipe() {
        return recipe;
    }

    @Override
    public String toString() {
        return "RecipeGetResponse{" +
                "recipe=" + recipe +
                '}';
    }
}
