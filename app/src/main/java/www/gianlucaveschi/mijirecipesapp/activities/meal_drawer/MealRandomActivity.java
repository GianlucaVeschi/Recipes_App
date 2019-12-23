package www.gianlucaveschi.mijirecipesapp.activities.meal_drawer;

import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.google.gson.JsonObject;
import com.r0adkll.slidr.Slidr;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import butterknife.BindView;
import butterknife.ButterKnife;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.MealAPI;
import www.gianlucaveschi.mijirecipesapp.networking.volley.VolleyNetworkManager;
import www.gianlucaveschi.mijirecipesapp.networking.volley.VolleyRequestListener;

public class MealRandomActivity extends AppCompatActivity {


    private static final String TAG = "MealRandomActivity";


    //Retrofit instance
    MealAPI mealAPI;

    @BindView(R.id.refreshLayout)       SwipeRefreshLayout refreshLayout;
    @BindView(R.id.card_view_id)        CardView cardView;
    @BindView(R.id.imageViewMeal)       ImageView imageView;
    @BindView(R.id.name_meal_detail)    TextView textViewName;
    @BindView(R.id.instructions_recipe) TextView textViewInstructions;
    @BindView(R.id.ingredient_quantity) TextView textViewQuantity;
    @BindView(R.id.ingredients_title )  TextView textViewInstructionsTitle;
    @BindView(R.id.ingredient_list)     TextView textViewIngredients;
    @BindView(R.id.progress_bar)        ProgressBar progressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_random);
        ButterKnife.bind(this);

        //Slide back to the Previous Activity
        Slidr.attach(this);

        //Toast
        Toast toast = Toast.makeText(this,"Swipe Up to get another recipe :)",Toast.LENGTH_LONG);
        toast.setGravity(Gravity.CENTER,0,0);
        toast.show();

        //Retrieve a Random Meal from the MealDB and update the UI
        getRandomRecipeWithVolley();

        //Refresh Listener
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRandomRecipeWithVolley();
                refreshLayout.setRefreshing(false); //otherwise animation gets stuck
            }
        });
    }

    private void getRandomRecipeWithVolley(){
        VolleyNetworkManager.getInstance().getRandomRecipe(new VolleyRequestListener<JSONObject>() {
            @Override
            public void getResult(JSONObject meal_details) {
                progressBar.setVisibility(View.INVISIBLE);
                updateUserInterface(meal_details);
            }
        });
    }

    private void updateUserInterface(JSONObject meal_details){

        //Empty TextView Fields
        textViewIngredients.setText("");
        textViewQuantity.setText("");

        //Update UI
        try{
            textViewName.setText(meal_details.getString("strMeal"));
            setImage(meal_details.getString("strMealThumb"));
            textViewInstructions.setText(meal_details.getString("strInstructions"));

            //Get and set Ingredients and Quantities
            for (int i = 1; i <= 20; i++) {
                String currentIngredient = meal_details.getString("strIngredient" + i);
                String currentMeasure = meal_details.getString("strMeasure" + i);

                if (currentIngredient.length() > 0) {
                    textViewIngredients.append(currentIngredient + "\n");
                    textViewQuantity.append(currentMeasure + "\n");
                }
            }
        }
        catch(JSONException e){
            Log.d(TAG, "getResult: " + e.getMessage());
        }
    }

    private void setImage(String imgUrl){
        Picasso.get()
                .load(imgUrl)
                .fit()
                .centerInside()
                .into(imageView);
        cardView.setVisibility(View.VISIBLE);

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
