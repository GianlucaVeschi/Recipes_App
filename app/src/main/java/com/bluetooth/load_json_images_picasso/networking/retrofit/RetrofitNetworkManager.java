package com.bluetooth.load_json_images_picasso.networking.retrofit;

import android.content.Context;
import android.util.Log;
import com.bluetooth.load_json_images_picasso.models.MealMap;
import com.bluetooth.load_json_images_picasso.models.MealSimple;
import com.bluetooth.load_json_images_picasso.models.MealContainer;
import com.bluetooth.load_json_images_picasso.networking.volley.VolleyNetworkManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitNetworkManager{

    public static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    private static final String TAG = "RetrofitNetworkManager";
    private static Gson gson = new GsonBuilder().create();
    private static OkHttpClient httpClient = new OkHttpClient.Builder().build();

    //todo Create Singleton Class
    private static Retrofit retrofit = null;

    //Get the Retrofit instance
    public static synchronized Retrofit getClient(Context context){
        if(retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .client(httpClient)
                    .build();
        }
        return retrofit;
    }

    //Context only has to passed once
    public static synchronized Retrofit getClient() {
        if (null == retrofit) {
            throw new IllegalStateException(VolleyNetworkManager.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return retrofit;
    }

    /*todo: delete this method and use getClient.create() instead.
    public static <S> S createService(Class<S> serviceClass) {
        return retrofit.create(serviceClass);
    }*/

    /**
     * API Methods implementation
     * */

    public void getMealByIdAsMealsContainer(String idMeal, final RetrofitRequestListener<MealSimple> listener){
        MealAPI mealAPI = getClient().create(MealAPI.class);
        Call<MealContainer> call = mealAPI.getMealDetailsAsMealContainer(idMeal);
        call.enqueue(new Callback<MealContainer>() {
            @Override
            public void onResponse(Call<MealContainer> call, Response<MealContainer> response) {
                MealContainer mealContainer = response.body();

                listener.getResult(mealContainer.getContainedMeal());
            }

            @Override
            public void onFailure(Call<MealContainer> call, Throwable t) {
                Log.d(TAG, "onFailure: getMealByIdAsMealsContainer");
            }
        });
    }

    //TODO This method is not working
    // I don't think it is a good practice to map a JSON object into a POJO model when it has too many
    // parameters that represent the exact same thing (StrIngredients)
    // I am trying to let retrofit return the JsonArray and then manually handle the parameters
    // as I do with Volley but this is proving to be a lot of work.
    public void getMealDetailsAsJSONObject(String idMeal, final RetrofitRequestListener<JSONObject> listener){
        MealAPI mealAPI = getClient().create(MealAPI.class);
        Log.d(TAG, "getMealDetailsAsJSONObject: dummylog");
        Call<JSONArray> call = mealAPI.getMealDetailsAsJSONArray(idMeal);

        call.enqueue(new Callback<JSONArray>() {
            @Override
            public void onResponse(Call<JSONArray> call, Response<JSONArray> response) {
                try{
                    JSONArray jsonArray = response.body().getJSONArray(0);
                    Log.d(TAG, "onResponse: jsonArray " + jsonArray);

                }
                catch (Exception e){
                    e.getMessage();
                }
            }
            @Override
            public void onFailure(Call<JSONArray> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }


    /**
     * PROBLEM :
     *  JsonArray meals (MealContainer) internally contains jsonObjects(MealSimple)
     *  Can Retrofit retrieve the list of MealSimples directly?
     * */
    public void getMealsByCountryAsMealSimple(String country){
        MealAPI mealAPI = getClient().create(MealAPI.class);
        Call<MealMap> call = mealAPI.getMealsByCountryAsMealSimple(country);

        call.enqueue(new Callback<MealMap>() {
            @Override
            public void onResponse(Call<MealMap> call, Response<MealMap> response) {
                Log.d(TAG, "onResponse asMealSimple: " + response);
                Log.d(TAG, "onResponse asMealSimple: " + response.body());
                Log.d(TAG, "onResponse asMealSimple: " + response.body().getMealStrings());
            }

            @Override
            public void onFailure(Call<MealMap> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

}
