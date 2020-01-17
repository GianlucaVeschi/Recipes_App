package www.gianlucaveschi.mijirecipesapp.activities.meal_tabs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
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


public class BrowseMealsByCountryActivity extends AppCompatActivity implements OnMealClickListener {

    public static final String EXTRA_MEAL = "MealParcel";

    //Bind UI
    //@BindView(R.id.meals_type_name)                 TextView countryMealsTextView;
    @BindView(R.id.browse_meals_recycler_view)      RecyclerView mealsRecyclerView;

    //Retrofit instance
    MealAPI mealAPI;

    //Logger
    private static final String TAG = "BrowseMealCountryActivi";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_recipes_by_country);
        ButterKnife.bind(this);

        //Get Intent
        Intent intent = getIntent();
        String flagImgUrl   = intent.getStringExtra("flag_url");
        String countryName  = intent.getStringExtra("country_name");

        //Set UI
        //String countryName_str = countryName + " Meals";
        //countryMealsTextView.setText(countryName_str);
        displayRecipesByCountryWithRetrofit(countryName);

        //Slide back to the Previous Activity
        Slidr.attach(this);

    }

    private void displayRecipesByCountryWithRetrofit(String country){
        mealAPI = RetrofitNetworkManager.getClient().create(MealAPI.class);
        mealAPI.getMealsByCountry(country).enqueue(new Callback<MealResponse>() {
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

    private void updateRecyclerView(MealResponse mealResponse) {

        //Init RV
        mealsRecyclerView.setHasFixedSize(true);
        mealsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        ArrayList<Meal> mealsList = mealResponse.getMeals();
        MealAdapter mealAdapter = new MealAdapter(BrowseMealsByCountryActivity.this, mealsList);
        mealsRecyclerView.setAdapter(mealAdapter);
        mealAdapter.setOnMealClickListener(BrowseMealsByCountryActivity.this);
    }

    @Override
    public void onItemClick(int position, ArrayList<Meal> mealsList) {
        Intent detailIntent = new Intent(this, MealDetailsActivity.class);
        Meal clickedItem = mealsList.get(position);

        //Send Parcel to the Details Activity
        detailIntent.putExtra(EXTRA_MEAL,clickedItem);
        startActivity(detailIntent);
    }
}
