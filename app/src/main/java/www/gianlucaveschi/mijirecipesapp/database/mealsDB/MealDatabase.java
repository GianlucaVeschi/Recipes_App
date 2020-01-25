package www.gianlucaveschi.mijirecipesapp.database.mealsDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import www.gianlucaveschi.mijirecipesapp.database.ArrayConverter;
import www.gianlucaveschi.mijirecipesapp.models.Meal;

@Database(entities = {Meal.class}, version = 1)
@TypeConverters({ArrayConverter.class})
public abstract class MealDatabase extends RoomDatabase {

    public static final String DATABASE_NAME = "meals_db";

    private static MealDatabase instance;

    public static MealDatabase getInstance(final Context context){
        if(instance == null){
            instance = Room.databaseBuilder(
                    context.getApplicationContext(),
                    MealDatabase.class,
                    DATABASE_NAME
            ).build();
        }
        return instance;
    }

    public abstract MealDAO getMealDAO();

}
