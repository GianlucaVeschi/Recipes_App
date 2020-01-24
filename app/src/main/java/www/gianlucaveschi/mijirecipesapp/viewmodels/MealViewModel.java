package www.gianlucaveschi.mijirecipesapp.viewmodels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import www.gianlucaveschi.mijirecipesapp.models.Meal;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.optimized.Resource;
import www.gianlucaveschi.mijirecipesapp.repositories.MealRepository;
import www.gianlucaveschi.mijirecipesapp.repositories.RecipeRepository;

public class MealViewModel extends AndroidViewModel {

    /*--------------------------------- INNER VARIABLES ------------------------------------------*/
    private MealRepository mMealRepository;

    /*--------------------------------- CONSTRUCTOR ----------------------------------------------*/

    public MealViewModel(@NonNull Application application, MealRepository mMealRepository) {
        super(application);
        this.mMealRepository = mMealRepository;
    }

    /*--------------------------------- EXECUTION ------------------------------------------------*/
    public LiveData<Resource<Meal>> getMeal(String recipeID){
        return mMealRepository.getMealApi(recipeID);
    }
}
