package www.gianlucaveschi.mijirecipesapp.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;
import www.gianlucaveschi.mijirecipesapp.repositories.RecipeRepository;

public class RecipeDetailsViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private String mRecipeId;

    public RecipeDetailsViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<Recipe> getRecipe(){
        return mRecipeRepository.getRecipe();
    }

    //Are these two methods used to make sure that the Recipe received in the Intent
    // and the one retrieved through the IdSearch are the same?
    public void searchRecipeById(String recipeId){
        mRecipeId = recipeId;
        mRecipeRepository.searchRecipeById(recipeId);
    }

    public String getRecipeId() {
        return mRecipeId;
    }
}
