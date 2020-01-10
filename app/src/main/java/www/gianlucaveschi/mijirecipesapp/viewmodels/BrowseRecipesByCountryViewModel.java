package www.gianlucaveschi.mijirecipesapp.viewmodels;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import www.gianlucaveschi.mijirecipesapp.models.recipes.Recipe;
import www.gianlucaveschi.mijirecipesapp.repositories.RecipeRepository;

public class BrowseRecipesByCountryViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;

    public BrowseRecipesByCountryViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipesApi(String query, int pageNumber){
        mRecipeRepository.searchRecipesApi(query, pageNumber);
    }
}
