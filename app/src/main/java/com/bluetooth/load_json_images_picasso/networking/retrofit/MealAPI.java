package com.bluetooth.load_json_images_picasso.networking.retrofit;

import com.bluetooth.load_json_images_picasso.models.Meal;
import com.bluetooth.load_json_images_picasso.models.MealsContainer;
import com.google.gson.JsonObject;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MealAPI {

    /*---------------------GET---------------------*/

    @GET("filter.php") //id will be replaced in mainActivity
    Call<MealsContainer> getMealsByCountry(@Query("a") String country);

    @GET("lookup.php")
    Call<MealsContainer> getMealDetailsAsMealContainer(@Query("i") int mealId);





}
