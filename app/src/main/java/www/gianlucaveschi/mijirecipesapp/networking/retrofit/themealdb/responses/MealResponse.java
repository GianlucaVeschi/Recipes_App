package www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.responses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import www.gianlucaveschi.mijirecipesapp.models.Meal;

public class MealResponse {

    /*--------------------------------- INTERNAL VARIABLES ---------------------------------------*/
    @SerializedName("meals")
    @Expose
    private ArrayList<Meal> meals;

    /*--------------------------------- CONSTRUCTOR ----------------------------------------------*/
    public MealResponse(ArrayList<Meal> meals) {
        this.meals = meals;
    }

    /*--------------------------------- GETTERS --------------------------------------------------*/
    public ArrayList<Meal> getMeals() {
        return meals;
    }

    /*--------------------------------- SETTERS --------------------------------------------------*/
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
