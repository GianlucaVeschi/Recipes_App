package www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import www.gianlucaveschi.mijirecipesapp.models.Meal;

public class MealResponse {

    private int id;

    @SerializedName("meals")
    @Expose
    private ArrayList<Meal> meals;

    /**
     * Constructor
     * */
    public MealResponse(ArrayList<Meal> meals) {
        this.meals = meals;
    }

    public ArrayList<Meal> getMeals() {
        return meals;
    }

    public int getId() {
        return id;
    }

    //Room will use this method
    public void setId(int id) {
        this.id = id;
    }

    public void setOrientation(int orientation){
        for(Meal mealSimples: meals){
            mealSimples.setOrientationType(orientation);
        }
    }
    @NonNull
    @Override
    public String toString() {
        String result = "\n";
        for (Meal meal : meals){
            result += meal.toString() + "\n";
        }
        return result;
    }
}
