package www.gianlucaveschi.mijirecipesapp.repositories;

import android.content.Context;

import androidx.lifecycle.LiveData;
import www.gianlucaveschi.mijirecipesapp.database.mealsDB.MealDAO;
import www.gianlucaveschi.mijirecipesapp.database.mealsDB.MealDatabase;
import www.gianlucaveschi.mijirecipesapp.models.Meal;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.executors.AppExecutors;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.optimized.NetworkBoundResource;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.optimized.Resource;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.responses.RecipeGetResponse;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.responses.MealResponse;

/**
 * The Repository is a HUB for retrieving the data from either the RestApi or the DB Cache.
 * It respects the Single Source of Truth Principle because it's the only place where data
 * come from without upper layers of the architecture having to know about it.
 * */
public class MealRepository {

    /*--------------------------------- INNER VARIABLES ------------------------------------------*/
    private static final String TAG = "MealRepository";

    private MealRepository instance;
    private MealDAO mealDAO;

    /*--------------------------------- SINGLETON CONSTRUCTOR ------------------------------------*/
    public MealRepository getInstance(Context context){
        if(instance == null){
            instance = new MealRepository(context);
        }
        return instance;
    }

    /*--------------------------------- INTERNAL CONSTRUCTOR -------------------------------------*/
    private MealRepository(Context context) {
        //context needed to instantiate the DAO.
        //mealDAO = MealDatabase.getInstance(context).getMealDAO();
    }


    /*--------------------------------- EXECUTION ------------------------------------------------*/

    public LiveData<Resource<Meal>> getMealApi(final String recipeID) {
//        return new NetworkBoundResource<Meal, MealResponse>(AppExecutors.getInstance()){
//
//        }
        return null;
    }

}
