package www.gianlucaveschi.mijirecipesapp.repositories;

import android.content.Context;
import android.util.Log;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import www.gianlucaveschi.mijirecipesapp.database.recipesDB.RecipeDAO;
import www.gianlucaveschi.mijirecipesapp.database.recipesDB.RecipeDatabase;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.executors.AppExecutors;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.optimized.ServiceGenerator;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.optimized.NetworkBoundResource;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.optimized.Resource;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.optimized.ApiResponse;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.responses.RecipeGetResponse;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.responses.RecipeSearchResponse;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;

/**
 * The Repository is a HUB for retrieving the data from either the RestApi or the DB Cache.
 * It respects the Single Source of Truth Principle because it's the only place where data
 * come from without over layers of the architecture having to know about it.
 * */
public class RecipeRepository {

    /*--------------------------------- INNER VARIABLES ------------------------------------------*/
    private static final String TAG = "RecipeRepository";

    private static RecipeRepository instance;
    private RecipeDAO recipeDAO;

    /*--------------------------------- SINGLETON CONSTRUCTOR ------------------------------------*/
    public static RecipeRepository getInstance(Context context) {
        if(instance == null){
            instance = new RecipeRepository(context);
        }
        return instance;
    }

    /*--------------------------------- INTERNAL CONSTRUCTOR -------------------------------------*/
    private RecipeRepository(Context context) {
        //context needed to instantiate the DAO.
        recipeDAO = RecipeDatabase.getInstance(context).getRecipeDAO();
    }

    /**
     * This methods condense the searchRecipesApi() call in the RecipeApiClient Object.
     * Its main advantages are
     * 1)   It uses the NetworkBoundResource file, which uses generics
     * 2)   Returns the RETROFIT call as LIVE DATA thanks to the RETROFIT CONVERTERS.
     * 3)   No need for a custom Runnable for every request because LIVE DATA ìs handled
     *      asynchronously by default and there's no need for additional executors.
     * 4)   More scalable and concise.
     */

    /*--------------------------------- GET LIST OF RECIPES --------------------------------------*/
    public LiveData<Resource<List<Recipe>>> searchRecipesApi(final String query, final int pageNumber){
        Log.d(TAG, "searchRecipesApi: OK");

        return new NetworkBoundResource<List<Recipe>, RecipeSearchResponse>(AppExecutors.getInstance()){
            //Save response from RETROFIT into the CACHE
            @Override
            protected void saveCallResponsteIntoDB(@NonNull RecipeSearchResponse searchResponse) {
                if(searchResponse.getRecipes() != null) { // recipe list will be null if api key is expired

                    Log.d(TAG, "saveCallResult: OK");

                    //The DAO makes use of varargs... so the Recipes contained in the response
                    // will be stored in an Array
                    Recipe[] recipesInTheDB = new Recipe[searchResponse.getRecipes().size()];
                    Recipe[] recipesInTheResponse = searchResponse.getRecipes().toArray(recipesInTheDB);

                    int index = 0;
                    for(long rowId: recipeDAO.insertRecipes(recipesInTheResponse)){ //Array containing the results of the INSERT operation.
                        if(rowId == -1){ // conflict detected is saved by the INSERT operation as -1
                            Log.d(TAG, "saveCallResult: CONFLICT... This recipe is already in cache.");
                            // if already exists, I don't want to set the ingredients or timestamp b/c they will be erased
                            recipeDAO.updateRecipe(
                                    recipesInTheDB[index].getRecipe_id(),
                                    recipesInTheDB[index].getTitle(),
                                    recipesInTheDB[index].getPublisher(),
                                    recipesInTheDB[index].getImage_url(),
                                    recipesInTheDB[index].getSocial_rank()
                            );
                        }
                        index++;
                    }
                } else{
                    Log.d(TAG, "saveCallResult: searchResponse.getRecipes() is NULL");
                }
            }

            //Contains logic for deciding whether to refresh the CACHE or not
            @Override
            protected boolean shouldFetchDataFromNetwork(@Nullable List<Recipe> data) {
                //Refresh the CACHE every time by default because theoretically the user could
                //search for anything so writing the logic for updating
                //it according to the request of a list would require looking up any item in
                //the list and this is quite expensive.
                Log.d(TAG, "shouldFetch: OK");
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Recipe>> loadFromDb() {
                Log.d(TAG, "loadFromDb: OK");
                return recipeDAO.searchRecipes(query, pageNumber);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RecipeSearchResponse>> createCall() {
                Log.d(TAG, "createCall: OK");
                return ServiceGenerator.getRecipeApiLiveData().searchRecipeAsLiveData(
                        Constants.RECIPES_API_KEY,
                        query,
                        String.valueOf(pageNumber)
                );
            }
        }.getAsLiveData();
    }

    /*--------------------------------- GET SINGLE RECIPE ----------------------------------------*/
    public LiveData<Resource<Recipe>> getRecipeApi(final String recipeID){
        return new NetworkBoundResource<Recipe, RecipeGetResponse>(AppExecutors.getInstance()){

            @Override
            protected void saveCallResponsteIntoDB(@NonNull RecipeGetResponse item) {
                // Recipe will be NULL if API key is expired
                if(item.getRecipe() != null){
                    item.getRecipe().setTimestamp((int)(System.currentTimeMillis() / 1000)); // save time in seconds
                    recipeDAO.insertRecipe(item.getRecipe());
                }
            }

            //Contains logic for deciding whether to refresh the CACHE according to the timestamp
            @Override
            protected boolean shouldFetchDataFromNetwork(@Nullable Recipe data) {
                Log.d(TAG, "shouldFetch: recipe: " + data.toString());
                int currentTime = (int)(System.currentTimeMillis() / 1000);
                Log.d(TAG, "shouldFetch: current time: " + currentTime);
                int lastRefresh = data.getTimestamp();
                Log.d(TAG, "shouldFetch: last refresh: " + lastRefresh);
                Log.d(TAG, "shouldFetch: it's been " +
                        ((currentTime - lastRefresh) / 60 / 60 / 24)    +
                        " days since this recipe was refreshed. \n"     +
                        "30 days must elapse.");
                if(((System.currentTimeMillis() / 1000) - data.getTimestamp()) >= Constants.RECIPE_REFRESH_TIME){
                    Log.d(TAG, "shouldFetch: SHOULD REFRESH RECIPE? " + true);
                    return true;
                }
                Log.d(TAG, "shouldFetch: SHOULD REFRESH RECIPE? " + false);
                return false;
            }

            @NonNull
            @Override
            protected LiveData<Recipe> loadFromDb() {
                return recipeDAO.getRecipe(recipeID);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<RecipeGetResponse>> createCall() {
                return ServiceGenerator.getRecipeApiLiveData().getRecipeAsLiveData(
                        Constants.RECIPES_API_KEY,
                        recipeID
                );
            }
        }.getAsLiveData();
    }

}
