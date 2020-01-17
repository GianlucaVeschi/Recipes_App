package www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork;

import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.executors.AppExecutors;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;

import static www.gianlucaveschi.mijirecipesapp.utils.Constants.NETWORK_TIMEOUT;

import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.runnables.GetRecipeRunnable;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.runnables.RetrieveRecipesRunnable;

public class RecipeApiClient {

    private static final String TAG = "RecipeApiClient";

    private static RecipeApiClient instance;

    //Observed LiveData
    private MutableLiveData<List<Recipe>> mRecipes;
    private MutableLiveData<Recipe> mRecipe;
    private MutableLiveData<Boolean> mRecipeRequestTimeout = new MutableLiveData<>();

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

        mGetRecipeRunnable = new GetRecipeRunnable(recipeId,mRecipe);

        final Future handler = AppExecutors.get().networkIO().submit(mGetRecipeRunnable);

        mRecipeRequestTimeout.setValue(false);
        AppExecutors.get().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                // let the user know it timed out
                mRecipeRequestTimeout.postValue(true);
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

    public LiveData<Boolean> isRecipeRequestTimedOut(){
        return mRecipeRequestTimeout;
    }
    
    
}


