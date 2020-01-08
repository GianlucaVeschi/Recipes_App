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
import www.gianlucaveschi.mijirecipesapp.models.meals.MealContainer;
import www.gianlucaveschi.mijirecipesapp.models.meals.MealSimple;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.MealAPI;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.RetrofitNetworkManager;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;


public class BrowseMealCountryActivity_DEPRECATED extends AppCompatActivity implements OnMealClickListener {

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
        setContentView(R.layout.activity_browse_country_meal);
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
        mealAPI.getMealsByCountry(country).enqueue(new Callback<MealContainer>() {
            @Override
            public void onResponse(Call<MealContainer> call, Response<MealContainer> response) {
                //Get Retrofit Response
                MealContainer mealContainer = response.body();

                //Set orientation for the rv
                mealContainer.setOrientation(Constants.VERTICAL_VIEW_TYPE);

                //Update UI
                updateRecyclerView(mealContainer);
            }

            @Override
            public void onFailure(Call<MealContainer> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void updateRecyclerView(MealContainer mealContainer) {

        //Init RV
        mealsRecyclerView.setHasFixedSize(true);
        mealsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        ArrayList<MealSimple> mealsList = mealContainer.getMealSimples();
        MealAdapter mealAdapter = new MealAdapter(BrowseMealCountryActivity_DEPRECATED.this, mealsList);
        mealsRecyclerView.setAdapter(mealAdapter);
        mealAdapter.setOnMealClickListener(BrowseMealCountryActivity_DEPRECATED.this);
    }

    @Override
    public void onItemClick(int position, ArrayList<MealSimple> mealsList) {
        Intent detailIntent = new Intent(this, MealDetailsActivity.class);
        MealSimple clickedItem = mealsList.get(position);

        //Send Parcel to the Details Activity
        detailIntent.putExtra(EXTRA_MEAL,clickedItem);
        startActivity(detailIntent);
    }
}
