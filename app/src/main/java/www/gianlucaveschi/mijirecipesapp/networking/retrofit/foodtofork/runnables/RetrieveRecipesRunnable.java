package www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.runnables;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Response;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.ServiceGenerator;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.responses.RecipeSearchResponse;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;

public class RetrieveRecipesRunnable implements Runnable{

    private static final String TAG = "RetrieveRecipesRunnable";

    private String query;
    private int pageNumber;
    private boolean cancelRequest;
    private MutableLiveData<List<Recipe>> mRecipes;
    private GetRecipeRunnable mGetRecipeRunnable;


    public RetrieveRecipesRunnable(String query, int pageNumber, MutableLiveData<List<Recipe>> recipes) {
        this.query = query;
        this.pageNumber = pageNumber;
        this.mRecipes = recipes;
        cancelRequest = false;
    }

    @Override
    public void run() {
        try {
            Response response = getRecipes(query, pageNumber).execute();
            if(cancelRequest){
                return;
            }
            if(response.code() == 200){
                List<Recipe> list = new ArrayList<>(((RecipeSearchResponse)response.body()).getRecipes());
                if(pageNumber == 1){
                    mRecipes.postValue(list);
                }
                else{
                    Log.d(TAG, "run: pageNumber " + pageNumber);
                    List<Recipe> currentRecipes = mRecipes.getValue();
                    currentRecipes.addAll(list);
                    mRecipes.postValue(currentRecipes);
                }
            }
            else{
                String error = response.errorBody().string();
                Log.e(TAG, "run: error: " + error);
                mRecipes.postValue(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mRecipes.postValue(null);
        }
    }

    private Call<RecipeSearchResponse> getRecipes(String query, int pageNumber){
        return ServiceGenerator.getRecipeApi().searchRecipe(
                Constants.API_KEY,
                query,
                String.valueOf(pageNumber));
    }

    public void cancelRequest(){
        Log.d(TAG, "cancelRequest: canceling the retrieval query");
        cancelRequest = true;
    }
}
