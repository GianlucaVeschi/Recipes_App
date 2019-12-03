package com.bluetooth.load_json_images_picasso.models;

import android.os.Parcel;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import androidx.annotation.NonNull;

public class MealsContainer {

    @SerializedName("meals")
    private List<Meal> meals;

    public MealsContainer(List<Meal> meals) {
        this.meals = meals;
    }

    public Meal getContainedMeal(){
        return meals.get(0);
    }
    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {
        this.meals = meals;
    }

    @NonNull
    @Override
    public String toString() {
        String result = "";
        for (Meal meal : meals){
            result += meal.toString() + "\n";
        }
        return result;
    }
}
