package www.gianlucaveschi.mijirecipesapp.database.mealsDB;

import java.util.List;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import www.gianlucaveschi.mijirecipesapp.models.Meal;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;

import static androidx.room.OnConflictStrategy.IGNORE;
import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MealDAO {

    @Insert(onConflict = IGNORE) //This means that if a Meal already exists in the DB it doesn't have to be replaced
    long[] insertMeals(Meal... meals); //VarArg annotations can contain 0 or N elements but it's always treated as an array

    @Insert(onConflict = REPLACE)
    void insertMeal(Meal meal);

    // Custom update statement so timestamp doesn't get removed
    @Query("UPDATE meals " +
            "SET mealname = :mealName,"      +
            "imgUrl = :imgUrl,"             +
            "orientationType = :orientationType " +
            "WHERE idMeal = :idMeal")
    void updateMeal(String idMeal, String mealName, String imgUrl, int orientationType);

    // NOTE: The SQL query sometimes won't return EXACTLY what the api does since the API might use a different query
    // or even a different database. But they are very very close.
    @Query("SELECT * FROM meals WHERE mealName LIKE '%' || :query || '%'" +
            " ORDER BY idMeal DESC")
    LiveData<List<Recipe>> searchMeals(String query);

    @Query(" SELECT * FROM meals WHERE idMeal = :idMeal")
    LiveData<Recipe> getMeal(String idMeal);
}
