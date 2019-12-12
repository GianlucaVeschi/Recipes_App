package com.bluetooth.load_json_images_picasso.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bluetooth.load_json_images_picasso.R;
import com.bluetooth.load_json_images_picasso.models.MealSimple;
import com.bluetooth.load_json_images_picasso.networking.retrofit.RetrofitNetworkManager;
import com.bluetooth.load_json_images_picasso.networking.retrofit.RetrofitRequestListener;
import com.bluetooth.load_json_images_picasso.networking.volley.VolleyNetworkManager;
import com.bluetooth.load_json_images_picasso.networking.volley.VolleyRequestListener;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.bluetooth.load_json_images_picasso.activities.MainActivity.EXTRA_MEAL;

public class MealDetailsActivity extends AppCompatActivity {

    private static final String TAG = "MealDetailsActivity";

    ImageView imageView;
    TextView textViewName;
    TextView textViewInstructions;
    TextView textViewIngredients;
    TextView textViewQuantity;
    TextView textViewInstructionsTitle;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.meal_details);

        imageView = findViewById(R.id.image_view_meal_detail);
        textViewName = findViewById(R.id.name_meal_detail);
        textViewIngredients = findViewById(R.id.ingredient_list);
        textViewInstructions = findViewById(R.id.instructions_recipe);
        textViewQuantity = findViewById(R.id.ingredient_quantity);
        textViewInstructionsTitle = findViewById(R.id.ingredients_title);
        textViewIngredients.setText("");
        textViewQuantity.setText("");

        Intent intent = getIntent();
        MealSimple mealSimple = intent.getParcelableExtra(EXTRA_MEAL);
        String imageURL = mealSimple.getImgUrl();
        String recipeName = mealSimple.getMealName();
        String idRecipe = mealSimple.getIdMeal();

        Picasso.get()
                .load(imageURL)
                .fit()
                .centerInside()
                .into(imageView);
        textViewName.setText(recipeName);

        displayRecipeDetails(idRecipe);
        displayRecipeByIDwithRetrofit(idRecipe);
    }

    //Volley
    private void displayRecipeDetails(String recipe_ID) {
        VolleyNetworkManager.getInstance().filterRecipeByID(recipe_ID, new VolleyRequestListener<JSONObject>() {
            @Override
            public void getResult(JSONObject meal_details) {
                try {
                    //Get and Set Instructions to the UI
                    String instructions = meal_details.getString("strInstructions");
                    textViewInstructions.setText(instructions);

                    //Get and set Ingredients and Quantities
                    for (int i = 1; i <= 20; i++) {

                        String currentIngredient = meal_details.getString("strIngredient" + i);
                        String currentMeasure = meal_details.getString("strMeasure" + i);

                        if (currentIngredient.length() > 0) {
                            textViewIngredients.append(currentIngredient + "\n");
                            textViewQuantity.append(currentMeasure + "\n");
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    //Retrofit
    private void displayRecipeByIDwithRetrofit(String idMeal) {
        
        RetrofitNetworkManager retrofitNetworkManager = new RetrofitNetworkManager();
        retrofitNetworkManager.getMealByIdAsMealsContainer(idMeal, new RetrofitRequestListener<MealSimple>() {
            @Override
            public void getResult(MealSimple mealSimple) {
                //Get and Set Instructions to the
                Log.d(TAG, "getResult retrofit : " + mealSimple); //working
                String instructions = mealSimple.getInstructions();
                Log.d(TAG, "getResult: instructions " + instructions); //working
            }
        });

        /*
        Log.d(TAG, "displayRecipeByIDwithRetrofit: dummylog");
        retrofitNetworkManager.getMealDetailsAsJSONObject(idMeal, new RetrofitRequestListener<JSONObject>(){
            @Override
            public void getResult(JSONObject object) {
                try {
                    Log.d(TAG, "getResult: dummylog" + object.getString("strInstructions"));
                    Log.d(TAG, "getResult: dummylog" + object.getString("strIngredient0"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        */
    }

}
