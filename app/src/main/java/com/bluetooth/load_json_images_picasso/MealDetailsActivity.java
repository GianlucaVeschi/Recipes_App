package com.bluetooth.load_json_images_picasso;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bluetooth.load_json_images_picasso.helpers.JsonHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.bluetooth.load_json_images_picasso.MainActivity.EXTRA_MEAL;

public class MealDetailsActivity extends AppCompatActivity {

    private final String RECIPE_BASE_URL = "https://www.themealdb.com/api/json/v1/1/lookup.php?i=";
    private static final String TAG = "MealDetailsActivity";
    private RequestQueue mRequestQueue;

    ImageView imageView;
    TextView textViewName;
    TextView textViewInstructions;
    JsonHelper jsonHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_details);

        imageView = findViewById(R.id.image_view_meal_detail);
        textViewName = findViewById(R.id.name_meal_detail);
        textViewInstructions = findViewById(R.id.instructions_recipe);
        jsonHelper = new JsonHelper();

        Intent intent = getIntent();
        Meal meal = intent.getParcelableExtra(EXTRA_MEAL);
        String imageURL = meal.getImgUrl();
        String recipeName = meal.getMealName();
        String idRecipe = meal.getIdMeal();
        Log.d(TAG, "manage Intent receive name " + recipeName);
        Log.d(TAG, "manage Intent receive ID: " + idRecipe);

        Picasso.get()
                .load(imageURL)
                .fit()
                .centerInside()
                .into(imageView);
        textViewName.setText(recipeName);

        mRequestQueue = Volley.newRequestQueue(this);
        mRequestQueue.add(jsonHelper.filterRecipeByID(idRecipe,textViewInstructions));
        //getRecipeDetails(idRecipe);
    }

    private void getRecipeDetails(String idRecipe) {
        String recipe_details_url = RECIPE_BASE_URL + idRecipe;
        Log.d(TAG, "getRecipeDetails URL:" + recipe_details_url );
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                recipe_details_url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray jsonArray = response.getJSONArray("meals");
                            Log.d(TAG, "onResponse array length: " + jsonArray.length());

                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject meal_details = jsonArray.getJSONObject(i);
                                Log.d(TAG, "onResponse instructions: " );
                                String instructions = meal_details.getString("strInstructions");
                                Log.d(TAG, "onResponse instructions: " + instructions.length());
                                textViewInstructions.setText(instructions);
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, "onResponse: catched error " + e.getMessage());
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: ");
                        error.printStackTrace();
                    }
                });
        mRequestQueue.add(request);
    }
}
