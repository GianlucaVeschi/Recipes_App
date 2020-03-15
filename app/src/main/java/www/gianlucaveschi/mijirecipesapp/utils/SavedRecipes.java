package www.gianlucaveschi.mijirecipesapp.utils;

import java.util.ArrayList;

import www.gianlucaveschi.mijirecipesapp.models.Recipe;

public class SavedRecipes {

    public static ArrayList<Recipe> favRecipesList = new ArrayList<>();

    public static void addRecipe(Recipe recipe){
        favRecipesList.add(recipe);
    }

    public static ArrayList<Recipe> getRecipes(){
        return favRecipesList;
    }

    public static boolean isEmpty(){
        return favRecipesList.isEmpty();
    }

    public static boolean containsRecipe(Recipe recipe){
        return favRecipesList.contains(recipe);
    }
}
