package www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import www.gianlucaveschi.mijirecipesapp.executors.AppExecutors;
import www.gianlucaveschi.mijirecipesapp.models.recipes.Recipe;

import static www.gianlucaveschi.mijirecipesapp.utils.Constants.NETWORK_TIMEOUT;

import retrofit2.Response;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.responses.RecipeSearchResponse;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.runnables.GetRecipeRunnable;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.runnables.RetrieveRecipesRunnable;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;

public class RecipeApiClient {

    private static final String TAG = "RecipeApiClient";

    private static RecipeApiClient instance;

    //Observed LiveData
    private MutableLiveData<List<Recipe>> mRecipes;
    private MutableLiveData<Recipe> mRecipe;

    //Runnables
    private RetrieveRecipesRunnable mRetrieveRecipesRunnable;
    private GetRecipeRunnable mGetRecipeRunnable;


    public static RecipeApiClient getInstance(){
        if(instance == null){
            instance = new RecipeApiClient();
        }
        return instance;
    }

    private RecipeApiClient() {
        mRecipes = new MutableLiveData<>();
        mRecipe = new MutableLiveData<>();
    }

    public LiveData<List<Recipe>> getRecipes(){
        return mRecipes;
    }

    public LiveData<Recipe> getRecipe(){
        return mRecipe;
    }

    public void searchRecipesApi(String query, int pageNumber){
        if(mRetrieveRecipesRunnable != null){
            mRetrieveRecipesRunnable = null;
        }
        mRetrieveRecipesRunnable = new RetrieveRecipesRunnable(query, pageNumber, mRecipes);
        final Future handler = AppExecutors.get().networkIO().submit(mRetrieveRecipesRunnable);

        // Set a timeout for the data refresh
        AppExecutors.get().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // let the user know it timed out
                handler.cancel(true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public void searchRecipeById(String recipeId){
        if(mGetRecipeRunnable != null){
            mGetRecipeRunnable = null;
        }

        Log.d(TAG, "searchRecipeById: " + recipeId); //arriva qui

        mGetRecipeRunnable = new GetRecipeRunnable(recipeId,mRecipe);

        final Future handler = AppExecutors.get().networkIO().submit(mGetRecipeRunnable);

        AppExecutors.get().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // let the user know it timed out
                handler.cancel( true);
            }
        }, NETWORK_TIMEOUT, TimeUnit.MILLISECONDS);
    }

    public void cancelRequest(){
        if(mRetrieveRecipesRunnable != null){
            mRetrieveRecipesRunnable.cancelRequest();
        }
        if(mGetRecipeRunnable != null){
            mGetRecipeRunnable.cancelRequest();
        }
    }
    
    
}


