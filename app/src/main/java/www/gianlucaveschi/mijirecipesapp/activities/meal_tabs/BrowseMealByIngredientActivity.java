package www.gianlucaveschi.mijirecipesapp.activities.meal_tabs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.gianlucaveschi.mijirecipesapp.activities.details.MealDetailsActivity;
import www.gianlucaveschi.mijirecipesapp.adapters.meals.MealAdapter;
import www.gianlucaveschi.mijirecipesapp.adapters.meals.OnMealClickListener;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.responses.MealResponse;
import www.gianlucaveschi.mijirecipesapp.models.Meal;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.MealAPI;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.RetrofitNetworkManager;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;

import static www.gianlucaveschi.mijirecipesapp.utils.Constants.EXTRA_MEAL;


public class BrowseMealByIngredientActivity extends AppCompatActivity implements OnMealClickListener {

    //Bind UI
    @BindView(R.id.toolbar)                     Toolbar toolbar;
    @BindView(R.id.browse_meals_recycler_view)  RecyclerView mealsRecyclerView;

    MealAPI mealAPI;
    MealAdapter mealAdapter;

    //Logger
    private static final String TAG = "BrowseMainIngredientAct";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_recipes_by_country);
        ButterKnife.bind(this);

        //Get Intent
        Intent intent = getIntent();
        String imgUrl   = intent.getStringExtra("image_url");
        String ingredientName  = intent.getStringExtra("ingredient_name");

        //Set UI
        initRecyclerView();
        String toolbar_str = ingredientName + " Meals";
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(toolbar_str);
        displayRecipesByIngredientWithRetrofit(ingredientName);

        //Slide back to the Previous Activity
        Slidr.attach(this);
    }

    private void displayRecipesByIngredientWithRetrofit(String ingredient){
        mealAPI = RetrofitNetworkManager.getClient().create(MealAPI.class);
        mealAPI.getMealsByIngredient(ingredient).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                //Get Retrofit Response
                MealResponse mealResponse = response.body();

                //Set orientation for the rv
                mealResponse.setOrientation(Constants.VERTICAL_VIEW_TYPE);

                //Update UI
                updateRecyclerView(mealResponse);
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void initRecyclerView(){
        mealsRecyclerView.setHasFixedSize(true);
        mealsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        mealsRecyclerView.setAdapter(mealAdapter);//set empty adapter
    }

    private void updateRecyclerView(MealResponse mealResponse) {
        ArrayList<Meal> mealsList = mealResponse.getMeals();
        mealAdapter = new MealAdapter(BrowseMealByIngredientActivity.this, mealsList);
        mealsRecyclerView.setAdapter(mealAdapter);
        mealAdapter.notifyDataSetChanged();
        mealAdapter.setOnMealClickListener(BrowseMealByIngredientActivity.this);
    }

    /**
     * OnItemClick
     * */
    @Override
    public void onItemClick(int position, ArrayList<Meal> mealsList) {
        Intent detailIntent = new Intent(this, MealDetailsActivity.class);
        Meal clickedItem = mealsList.get(position);
        //Send Parcel to the Details Activity
        detailIntent.putExtra(EXTRA_MEAL,clickedItem);
        startActivity(detailIntent);
    }
}
