package www.gianlucaveschi.mijirecipesapp.viewmodels;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import www.gianlucaveschi.mijirecipesapp.models.Meal;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.resources.Resource;
import www.gianlucaveschi.mijirecipesapp.repositories.MealRepository;

public class MealViewModel extends AndroidViewModel {

    /*--------------------------------- INNER VARIABLES ------------------------------------------*/
    private MealRepository mMealRepository;

    /*--------------------------------- CONSTRUCTOR ----------------------------------------------*/

    public MealViewModel(@NonNull Application application) {
        super(application);
        this.mMealRepository = mMealRepository.getInstance(application);
    }

    /*--------------------------------- EXECUTION ------------------------------------------------*/
    public LiveData<Resource<Meal>> getMeal(String recipeID){
        return mMealRepository.getMealApi(recipeID);
    }

    public LiveData<Resource<List<Meal>>> getMealsByCountry(String countryName){
        return mMealRepository.getMealsByCountryApi(countryName);
    }
}
