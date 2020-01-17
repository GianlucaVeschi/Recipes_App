package www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.runnables;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Response;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.ServiceGenerator;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.responses.RecipeGetResponse;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;

public class GetRecipeRunnable implements Runnable{

    private boolean cancelRequest;
    private MutableLiveData<Recipe> mRecipe;
    private String mRecipeId;
    private static final String TAG = "RetrieveRecipesRunnable";

    public GetRecipeRunnable(String recipeId, MutableLiveData<Recipe> recipe) {
        this.mRecipeId = recipeId;
        this.mRecipe = recipe;
        cancelRequest = false;
    }

    @Override
    public void run() {

        try {
            Response response = getRecipe(mRecipeId).execute();
            if(cancelRequest){
                return;
            }
            if(response.code() == 200){
                Recipe recipe = ((RecipeGetResponse)response.body()).getRecipe();
                mRecipe.postValue(recipe);
            }
            else{
                String error = response.errorBody().string();
                Log.e(TAG, "run: error: " + error);
                mRecipe.postValue(null);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mRecipe.postValue(null);
        }
    }

    private Call<RecipeGetResponse> getRecipe(String recipeId){
        return ServiceGenerator.getRecipeApi().getRecipe(
                Constants.API_KEY,
                recipeId
        );
    }

    public void cancelRequest(){
        Log.d(TAG, "cancelRequest: canceling the retrieval query");
        cancelRequest = true;
    }

}
