package www.gianlucaveschi.mijirecipesapp.adapters.meals;

import java.util.ArrayList;

import www.gianlucaveschi.mijirecipesapp.models.meals.MealSimple;

//Create internal Interface
public interface OnMealClickListener{
    void onItemClick(int position, ArrayList<MealSimple> mealsList);
}
