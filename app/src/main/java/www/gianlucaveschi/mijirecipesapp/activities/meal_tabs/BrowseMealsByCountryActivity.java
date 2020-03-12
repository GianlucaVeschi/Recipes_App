package www.gianlucaveschi.mijirecipesapp.activities.meal_tabs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
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
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.resources.Resource;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.responses.MealResponse;
import www.gianlucaveschi.mijirecipesapp.models.Meal;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.MealAPI;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.MealRetrofitManager;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;
import www.gianlucaveschi.mijirecipesapp.viewmodels.MealViewModel;

import static www.gianlucaveschi.mijirecipesapp.utils.Constants.EXTRA_MEAL;


public class BrowseMealsByCountryActivity extends AppCompatActivity implements OnMealClickListener {

    //Logger
    private static final String TAG = "BrowseMealsByCountry";

    //Bind UI
    @BindView(R.id.browse_meals_recycler_view)      RecyclerView mealsRecyclerView;

    //API instance
    MealAPI mealAPI = MealRetrofitManager.getMealAPI();
    MealViewModel mMealViewModel;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_recipes_by_country);
        ButterKnife.bind(this);
        Slidr.attach(this); //Slide back to the Previous Activity
        mMealViewModel = ViewModelProviders.of(this).get(MealViewModel.class);

        //Get Intent
        Intent intent = getIntent();
        String flagImgUrl   = intent.getStringExtra("flag_url");
        String countryName  = intent.getStringExtra("country_name");

        //Set UI
        displayRecipesByCountryWithRetrofit(countryName);

        subscribeObservers(countryName);

    }

    private void subscribeObservers(String countryName){
        mMealViewModel.getMealsByCountry(countryName).observe(this, new Observer<Resource<List<Meal>>>() {
            @Override
            public void onChanged(Resource<List<Meal>> listResource) {
                if(listResource != null){
                    Log.d(TAG, "onChanged: status " + listResource.status);
                    if(listResource.data != null){
                        switch (listResource.status) {
                            case LOADING: {
                                // TODO: 30/01/2020
                                break;
                            }
                            case SUCCESS: {
                                Log.d(TAG, "onChanged: body " + listResource.data);
                                break;
                            }
                            case ERROR: {
                                // TODO: 30/01/2020
                            }
                        }
                    }
                }
            }
        });
    }

    private void displayRecipesByCountryWithRetrofit(String country){
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
