package www.gianlucaveschi.mijirecipesapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.resources.Resource;
import www.gianlucaveschi.mijirecipesapp.repositories.RecipeRepository;

public class RecipeDetailsViewModel extends AndroidViewModel {

    /*--------------------------------- INNER VARIABLES ------------------------------------------*/
    private RecipeRepository mRecipeRepository;

    /*--------------------------------- CONSTRUCTOR ----------------------------------------------*/

    public RecipeDetailsViewModel(@NonNull Application application) {
        super(application);
        this.mRecipeRepository = mRecipeRepository.getInstance(application);
    }

    /*--------------------------------- EXECUTION ------------------------------------------------*/
    public LiveData<Resource<Recipe>> getRecipe(String recipeID){
        return mRecipeRepository.getRecipeApi(recipeID);
    }


}
