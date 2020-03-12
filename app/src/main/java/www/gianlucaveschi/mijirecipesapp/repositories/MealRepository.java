package www.gianlucaveschi.mijirecipesapp.repositories;

import android.content.Context;
import android.util.Log;

import java.util.List;

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

    public LiveData<Resource<List<Meal>>> getMealsByCountryApi(final String countryName){
        Log.d(TAG, "getMealsByCountryApi: OK");
        return new NetworkBoundResource<List<Meal>,MealResponse>(AppExecutors.getInstance()){
            @Override
            protected void saveCallResponseIntoDB(@NonNull MealResponse response) {
                Log.d(TAG, "save Call Response Into DB: OK");
                // Recipe will be NULL if API key is expired
                if(response.getMeals() != null){
                    response.getMeals(); // save time in seconds

                    //The DAO makes use of varargs... so the Recipes contained in the response
                    // will be stored in an Array
                    Meal[] mealsInTheDB = new Meal[response.getMeals().size()];
                    Meal[] mealsInTheResponse = response.getMeals().toArray(mealsInTheDB);

                    int index = 0;
                    for(long rowId: mealDAO.insertMeals(mealsInTheResponse)){ //Array containing the results of the INSERT operation.
                        if(rowId == -1){ // conflict detected is saved by the INSERT operation as -1
                            Log.d(TAG, "saveCallResult: CONFLICT... This recipe is already in cache.");
                            // if already exists, I don't want to set the ingredients or timestamp b/c they will be erased
                            mealDAO.updateMeal(
                                    mealsInTheDB[index].getIdMeal(),
                                    mealsInTheDB[index].getMealName(),
                                    mealsInTheDB[index].getImgUrl()

                            );
                        }
                        index++;
                    }
                }
                else{
                    Log.d(TAG, "saveCallResponseIntoDB: returned NULL");
                }
            }

            @Override
            protected boolean shouldFetchDataFromNetwork(@Nullable List<Meal> data) {
                // TODO: 29/01/2020 : Some time stamp
                Log.d(TAG, "shouldFetchDataFromNetwork: OK");
                return true;
            }

            @NonNull
            @Override
            protected LiveData<List<Meal>> loadFromDb() {
                Log.d(TAG, "loadFromDb: OK");
                LiveData<List<Meal>> mealsInTheDb = mealDAO.searchMeals(countryName);
                //Log.d(TAG, "loadFromDb: " + mealsInTheDb.getValue());
                return mealsInTheDb;
            }

            @NonNull
            @Override
            protected LiveData<ApiResponse<MealResponse>> createCall() {
                Log.d(TAG, "createCall: OK");
                return MealRetrofitManager.getMealAPI().getMealsByCountryAsLiveData(countryName);
            }
        }.getAsLiveData();
    }
}
