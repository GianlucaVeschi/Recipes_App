package www.gianlucaveschi.mijirecipesapp.viewmodels;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import www.gianlucaveschi.mijirecipesapp.models.recipes.Recipe;
import www.gianlucaveschi.mijirecipesapp.repositories.RecipeRepository;

public class BrowseRecipesByCountryViewModel extends ViewModel {

    private RecipeRepository mRecipeRepository;
    private Boolean mIsViewingRecipes;
    private Boolean mIsPerformingQuery; //Used to cancel a RETROFIT request

    public BrowseRecipesByCountryViewModel() {
        mRecipeRepository = RecipeRepository.getInstance();
        mIsPerformingQuery = false;
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeRepository.getRecipes();
    }

    public void searchRecipesApi(String query, int pageNumber){
        mRecipeRepository.searchRecipesApi(query, pageNumber);
        mIsPerformingQuery = true;
    }


    public Boolean isPerformingQuery() {
        return mIsPerformingQuery;
    }

    public void setIsPerformingQuery(Boolean mIsPerformingQuery) {
        this.mIsPerformingQuery = mIsPerformingQuery;
    }

    public boolean onBackPressed(){
        if(mIsPerformingQuery){
            // cancel the query
            mRecipeRepository.cancelRequest();
            mIsPerformingQuery = false;
        }
        if(mIsViewingRecipes){
            mIsViewingRecipes = false;
            return false;
        }
        return true;
    }
}
