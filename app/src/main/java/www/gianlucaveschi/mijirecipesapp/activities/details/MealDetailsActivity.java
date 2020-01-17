package www.gianlucaveschi.mijirecipesapp.activities.details;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gianlucaveschi.load_json_images_picasso.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import www.gianlucaveschi.mijirecipesapp.models.Meal;
import www.gianlucaveschi.mijirecipesapp.networking.volley.VolleyNetworkManager;
import www.gianlucaveschi.mijirecipesapp.networking.volley.VolleyRequestListener;

import com.r0adkll.slidr.Slidr;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static www.gianlucaveschi.mijirecipesapp.activities.bottom_nav.AboutMealsActivity.EXTRA_MEAL;

public class MealDetailsActivity extends AppCompatActivity {

    private static final String TAG = "MealDetailsActivity";

    @BindView(R.id.image_view_meal_detail)  ImageView imageView;
    @BindView(R.id.name_meal_detail)        TextView textViewName;
    @BindView(R.id.instructions_recipe)     TextView textViewInstructions;
    @BindView(R.id.ingredient_quantity)     TextView textViewQuantity;
    @BindView(R.id.ingredients_title )      TextView textViewInstructionsTitle;
    @BindView(R.id.ingredient_list)         TextView textViewIngredients;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_details);
        ButterKnife.bind(this);

        textViewIngredients.setText("");
        textViewQuantity.setText("");

        Intent intent = getIntent();
        Meal meal = intent.getParcelableExtra(EXTRA_MEAL);

        String imageURL = meal.getImgUrl();
        String recipeName = meal.getMealName();
        String idRecipe = meal.getIdMeal();


        textViewName.setText(recipeName);
        setImage(imageURL);

        displayRecipeDetails(idRecipe);

        //Slide back to the Previous Activity
        Slidr.attach(this);

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


    private void setImage(String imgUrl){
        Picasso.get()
                .load(imgUrl)
                .fit()
                .centerInside()
                .into(imageView);

        /*
        //ToDo: Find a faster way to load and display images from a remote source
        //Glide Approach is also slow
        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.mipmap.ic_launcher_round);

        Glide.with(this)
                .load(imgUrl)
                .apply(options)
                .into(imageView);
        */
    }

}
