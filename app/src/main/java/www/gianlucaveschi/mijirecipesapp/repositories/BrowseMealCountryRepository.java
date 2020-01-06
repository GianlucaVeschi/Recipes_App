package www.gianlucaveschi.mijirecipesapp.repositories;

import java.util.List;

import androidx.lifecycle.LiveData;
import www.gianlucaveschi.mijirecipesapp.models.recipes.Recipe;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.RecipeApiClient;

public class BrowseMealCountryRepository {

    private static BrowseMealCountryRepository instance;
    private RecipeApiClient mRecipeApiClient;

    //Singleton Constructor
    public static BrowseMealCountryRepository getInstance(){
        if(instance == null){
            instance = new BrowseMealCountryRepository();
        }
        return instance;
    }

    private BrowseMealCountryRepository() {
        mRecipeApiClient = RecipeApiClient.getInstance();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipeApiClient.getRecipes();
    }

    //Retrieves the recipes on the web server by requesting them to the RecipeApiClient
    public void searchRecipesApi(String query, int pageNumber){
        if(pageNumber == 0){
            pageNumber = 1;
        }
        mRecipeApiClient.searchRecipesApi(query, pageNumber);
    }
}
