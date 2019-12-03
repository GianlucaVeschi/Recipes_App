package com.bluetooth.load_json_images_picasso.networking.retrofit;

import android.util.Log;

import com.bluetooth.load_json_images_picasso.models.Meal;
import com.bluetooth.load_json_images_picasso.models.MealsContainer;
import com.bluetooth.load_json_images_picasso.networking.volley.VolleyRequestListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitNetworkManager {

    public static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static final String TAG = "RetrofitNetworkManager";
    //Gson object will be passed to the GsonConverterFactory so Gson will not drop the nulls and put them instead in the Json Object
    private static Gson gson = new GsonBuilder().create();

    //HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
    //logging.level(HttpLoggingInterceptor.Level.BODY);

    private static HttpLoggingInterceptor logging = new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC);

    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            //.addInterceptor(logging)
            .build();

    /**RETROFIT BUILDER
     * Contains the base URL which is going to be used for every service call and a converter factory
     * GsonConverterFactory is going to map our JSON data to the Meal json_model we defined.
     */
    private static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL) // must always end with a backslash / otherwise IllegalStateException
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(httpClient)
            .build();

    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }

    /**
     * API Methods implementation
     * */
    public void getMealsByCountry(String country, final RetrofitRequestListener<List<Meal>> listener){
          MealAPI mealAPI = createService(MealAPI.class);
          Call<MealsContainer> call = mealAPI.getMealsByCountry(country);
          call.enqueue(new Callback<MealsContainer>() {
              @Override
              public void onResponse(Call<MealsContainer> call, Response<MealsContainer> response) {
                  MealsContainer mealsContainer = response.body();
                  listener.getResult(mealsContainer.getMeals());
              }

              @Override
              public void onFailure(Call<MealsContainer> call, Throwable t) {
                  Log.d(TAG, "onFailure: " + t.getMessage());
              }
          });
    }

    public void getMealByIdAsMealsContainer(int idMeal, final RetrofitRequestListener<Meal> listener){
        MealAPI mealAPI = createService(MealAPI.class);
        Call<MealsContainer> call = mealAPI.getMealDetailsAsMealContainer(idMeal);
        call.enqueue(new Callback<MealsContainer>() {
            @Override
            public void onResponse(Call<MealsContainer> call, Response<MealsContainer> response) {
                MealsContainer mealsContainer = response.body();
                Log.d(TAG, "onResponse: Meals Container body" + response.body().toString());
                Log.d(TAG, "onResponse: Meals Container object" + mealsContainer.toString());
                //Log.d(TAG, "onResponse: Meals Container contains" + mealsContainer.getContainedMeal().toString());

                listener.getResult(mealsContainer.getContainedMeal());
            }

            @Override
            public void onFailure(Call<MealsContainer> call, Throwable t) {
                Log.d(TAG, "onFailure: getMealByIdAsMealsContainer");
            }
        });
    }

}
