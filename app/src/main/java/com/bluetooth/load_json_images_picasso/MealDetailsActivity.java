package com.bluetooth.load_json_images_picasso;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.bluetooth.load_json_images_picasso.helpers.VolleyNetworkManager;
import com.bluetooth.load_json_images_picasso.helpers.VolleyRequestListener;
import com.squareup.picasso.Picasso;

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
    VolleyNetworkManager volleyNetworkManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_details);

        imageView = findViewById(R.id.image_view_meal_detail);
        textViewName = findViewById(R.id.name_meal_detail);
        textViewInstructions = findViewById(R.id.instructions_recipe);

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

        displayRecipeDetails(idRecipe);
    }

    private void displayRecipeDetails(String recipe_ID){
        VolleyNetworkManager.getInstance().filterRecipeByID(recipe_ID, new VolleyRequestListener<JSONObject>() {
            @Override
            public void getResult(JSONObject meal_details) {
                try {

                    String instructions = meal_details.getString("strInstructions");
                    Log.d(TAG, "onResponse instructions: " + instructions.length());
                    textViewInstructions.setText(instructions);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
