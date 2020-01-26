package www.gianlucaveschi.mijirecipesapp.repositories;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import www.gianlucaveschi.mijirecipesapp.database.mealsDB.MealDAO;
import www.gianlucaveschi.mijirecipesapp.database.mealsDB.MealDatabase;
import www.gianlucaveschi.mijirecipesapp.models.Meal;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.executors.AppExecutors;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.resources.NetworkBoundResource;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.resources.Resource;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.responses.ApiResponse;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.MealRetrofitManager;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.responses.MealResponse;


/**
 * The Repository is a HUB for retrieving the data from either the RestApi or the DB Cache.
 * It respects the Single Source of Truth Principle because it's the only place where data
 * come from without upper layers of the architecture having to know about it.
 * */
public class MealRepository {

    /*--------------------------------- INNER VARIABLES ------------------------------------------*/
    private static final String TAG = "MealRepository";

    private static MealRepository instance;
    private MealDAO mealDAO;

    /*--------------------------------- SINGLETON CONSTRUCTOR ------------------------------------*/
    public static MealRepository getInstance(Context context){
        if(instance == null){
            instance = new MealRepository(context);
        }
        return instance;
    }

    /*--------------------------------- INTERNAL CONSTRUCTOR -------------------------------------*/
    private MealRepository(Context context) {
        //context needed to instantiate the DAO.
        mealDAO = MealDatabase.getInstance(context).getMealDAO();
    }

    /*--------------------------------- EXECUTION ------------------------------------------------*/

    public LiveData<Resource<Meal>> getMealApi(final String recipeID) {
        return new NetworkBoundResource<Meal, MealResponse>(AppExecutors.getInstance()){

            @Override
            protected void saveCallResponseIntoDB(@NonNull MealResponse item) {
                Log.d(TAG, "saveCallResponseIntoDB: OK");
                // Recipe will be NULL if API key is expired
                if(item.getSingleMeal() != null){
                    item.getSingleMeal().setTimestamp((int)(System.currentTimeMillis() / 1000)); // save time in seconds
                    mealDAO.insertMeal(item.getSingleMeal());
                }
                else{
                    Log.d(TAG, "saveCallResponseIntoDB: returned NULL");
                }
            }

            @Override
            protected boolean shouldFetchDataFromNetwork(@Nullable Meal data) {
                Log.d(TAG, "shouldFetchDataFromNetwork: OK");
                return true;
            }

            @NonNull
            @Override
            protected LiveData<Meal> loadFromDb() {
                Log.d(TAG, "loadFromDb: Ok");
                return mealDAO.getMeal(recipeID);
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MealResponse>> createCall() {
                Log.d(TAG, "createCall: OK");
                return MealRetrofitManager.getMealAPI().getMealByIdAsLiveData(recipeID);
            }
        }.getAsLiveData();
    }

}
