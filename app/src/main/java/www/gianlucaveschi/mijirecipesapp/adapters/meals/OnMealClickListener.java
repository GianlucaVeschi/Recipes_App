package www.gianlucaveschi.mijirecipesapp.adapters.meals;

import java.util.ArrayList;

import www.gianlucaveschi.mijirecipesapp.models.Meal;

//Create internal Interface
public interface OnMealClickListener{
    void onItemClick(int position, ArrayList<Meal> mealsList);
}
