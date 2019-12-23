package www.gianlucaveschi.mijirecipesapp.models.meals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

import androidx.annotation.NonNull;

public class MealContainer {

    private int id;

    @SerializedName("meals")
    @Expose
    private ArrayList<MealSimple> mealSimples;

    /**
     * Constructor
     * */
    public MealContainer(ArrayList<MealSimple> mealSimples) {
        this.mealSimples = mealSimples;
    }

    public MealSimple getContainedMeal(){
        return mealSimples.get(0);
    }

    public ArrayList<MealSimple> getMealSimples() {
        return mealSimples;
    }

    public int getId() {
        return id;
    }

    //Room will use this method
    public void setId(int id) {
        this.id = id;
    }

    public void setOrientation(int orientation){
        for(MealSimple mealSimples: mealSimples){
            mealSimples.setOrientationType(orientation);
        }
    }
    @NonNull
    @Override
    public String toString() {
        String result = "\n";
        for (MealSimple mealSimple : mealSimples){
            result += mealSimple.toString() + "\n";
        }
        return result;
    }
}
