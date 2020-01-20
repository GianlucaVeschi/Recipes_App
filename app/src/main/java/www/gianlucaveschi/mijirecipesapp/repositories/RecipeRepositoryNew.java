package www.gianlucaveschi.mijirecipesapp.repositories;

import android.content.Context;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import www.gianlucaveschi.mijirecipesapp.database.RecipeDAO;
import www.gianlucaveschi.mijirecipesapp.database.RecipeDatabase;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.executors.AppExecutors;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.old.ServiceGenerator;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.optimized.NetworkBoundResource;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.optimized.Resource;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.optimized.ApiResponse;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.responses.RecipeSearchResponse;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;

/**
 * The Repository is a HUB for retrieving the data from either the RestApi or the DB Cache.
 * It respects the Single Source of Truth Principle because it's the only place where data
 * come from without over layers of the architecture having to know about it.
 * */
public class RecipeRepositoryNew {

    private static RecipeRepositoryNew instance;
    private RecipeDAO recipeDAO;

    //Singleton constructor
    public static RecipeRepositoryNew getInstance(Context context) {
        if(instance == null){
            instance = new RecipeRepositoryNew(context);
        }
        return instance;
    }

    //Constructor takes context as a parameter to instantiate the DAO.
    private RecipeRepositoryNew(Context context) {
        recipeDAO = RecipeDatabase.getInstance(context).getRecipeDAO();
    }

    /**
     * This method condenses the searchRecipesApi() call in the RecipeApiClient Object.
     * Its main advantages are
     * 1)   It uses the NetworkBoundResource file, which uses generics
     * 2)   Returns the RETROFIT call as LIVEDATA thanks to the RETROFIT CONVERTERS.
     * 3)   No need for a custom Runnable for every request because LIVEDATA ìs handled
     *      asynchronously by default and there's no need for additional executors.
     * 4)   More scalable and concise.
     */
    public LiveData<Resource<List<Recipe>>> searchRecipesApi(final String query, final int pageNumber){
        return new NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(AppExecutors.getInstance()){

            //Save response from RETROFIT into the CACHE
            @Override
            protected void saveCallResult(@NonNull RecipeSearchResponse searchResponse) {
                if(searchResponse.getRecipes() != null) { // recipe list will be null if api key is expired

                    //Empty array which will store the items contained in the
                    //response because the DAO makes use of varargs...
                    Recipe[] recipes = new Recipe[searchResponse.getRecipes().size()];

                    int index = 0;
                    for(long rowId: recipeDAO.insertRecipes((Recipe[])(searchResponse.getRecipes().toArray(recipes)))){
                        if(rowId == -1){ // conflict detected (DB Insert method returns -1 when one Item is not inserted.
                            recipeDAO.updateRecipe(
                                    recipes[index].getRecipe_id(),
                                    recipes[index].getTitle(),
                                    recipes[index].getPublisher(),
                                    recipes[index].getImage_url(),
                                    recipes[index].getSocial_rank()
                            );
                        }
                    }
                }
            }

            //Contains logic for deciding whether to refresh the CACHE according to the timestamp
            @Override
            protected boolean shouldFetch(@Nullable List<Recipe> data) {
                //Refresh the CACHE every time by default because theoretically the user could
                //search for anything so writing the logic for updating
                //it according to the request of a list would require looking up any item in
                //the list and this is quite expensive.
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Recipe>> loadFromDb() {
                return recipeDAO.searchRecipes(query, pageNumber);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RecipeSearchResponse>> createCall() {
                return ServiceGenerator.getRecipeApi().searchRecipeAsLiveData(
                        Constants.API_KEY,
                        query,
                        String.valueOf(pageNumber)
                );
            }
        }.getAsLiveData();
    }
}
