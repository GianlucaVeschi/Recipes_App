package www.gianlucaveschi.mijirecipesapp.activities.meal_tabs;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.gianlucaveschi.mijirecipesapp.activities.details.RecipeDetailsActivity;
import www.gianlucaveschi.mijirecipesapp.adapters.recipes.OnRecipeListener;
import www.gianlucaveschi.mijirecipesapp.adapters.recipes.RecipeAdapter;
import www.gianlucaveschi.mijirecipesapp.models.meals.MealSimple;
import www.gianlucaveschi.mijirecipesapp.models.recipes.Recipe;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.RecipeApi;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.ServiceGenerator;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.responses.RecipeGetResponse;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;
import www.gianlucaveschi.mijirecipesapp.utils.MyLogger;
import www.gianlucaveschi.mijirecipesapp.viewmodels.BrowseMealCountriesViewModel;


public class BrowseMealCountryActivity extends AppCompatActivity implements OnRecipeListener {

    private static final String TAG = "BrowseMealCountryAct";

    //Bind UI
    @BindView(R.id.toolbar)                         Toolbar toolbar;
    @BindView(R.id.browse_meals_recycler_view)      RecyclerView mealsRecyclerView;

    //View Model
    private static BrowseMealCountriesViewModel mBrowseMealCountriesViewModel;

    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_country_meal);
        ButterKnife.bind(this);
        mBrowseMealCountriesViewModel = ViewModelProviders.of(this).get(BrowseMealCountriesViewModel.class);

        //Get Intent
        Intent intent = getIntent();
        String flagImgUrl   = intent.getStringExtra("flag_url");
        String countryName  = intent.getStringExtra("country_name");

        //Set UI
        String toolbar_str = countryName + " Meals";
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(toolbar_str);
        initRecyclerView();

        //Observe the LiveData and Update the UI
        subscribeObservers();
        displayRecipesByCountry(countryName);

        //Slide back to the Previous Activity
        Slidr.attach(this);
    }

    //Retrieve Live Data from the Repository
    public LiveData<List<Recipe>> getRecipes() {
        return mBrowseMealCountriesViewModel.getRecipes();
    }

    //Observe Live Data
    private void subscribeObservers() {
        mBrowseMealCountriesViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> receivedRecipes) {
                if (receivedRecipes != null) {
                    MyLogger.logRecipes("onChanged", receivedRecipes);
                    recipes = new ArrayList<>(receivedRecipes);
                    updateRecyclerView(receivedRecipes);
                }
            }
        });
    }

    private void initRecyclerView() {
        recipeAdapter = new RecipeAdapter(this);
        mealsRecyclerView.setAdapter(recipeAdapter);
        mealsRecyclerView.setHasFixedSize(true);
        mealsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void updateRecyclerView(List<Recipe> recipes) {
        mealsRecyclerView.setAdapter(recipeAdapter);
        recipeAdapter.setRecipes(recipes);
    }

    private void displayRecipesByCountry(String nationality) {
        searchRecipesApi(nationality, 1);
    }

    //Ask data to the ViewHolder
    private void searchRecipesApi(String query, int pageNumber) {
        if (pageNumber == 0) {
            pageNumber = 1;
        }
        mBrowseMealCountriesViewModel.searchRecipesApi(query, pageNumber);
    }

    @Override
    public void onRecipeClick(int position) {
        Toast.makeText(this, "onRecipeClick", Toast.LENGTH_SHORT).show();
        if(recipes != null){
            Intent detailIntent = new Intent(this, RecipeDetailsActivity.class);
            Recipe clickedItem = recipes.get(position);

            //Send Parcel to the Details Activity
            detailIntent.putExtra(Constants.EXTRA_RECIPE,clickedItem);
            startActivity(detailIntent);
        }
    }

    @Override
    public void onCategoryClick(String category) {
        Toast.makeText(this, "OnCategoryClick", Toast.LENGTH_SHORT).show();
    }

    private void testRetrofitRequestSimple() {
        RecipeApi recipeApi = ServiceGenerator.getRecipeApi();
        // do get using retrofit
        Call<RecipeGetResponse> responseCall = recipeApi
                .getRecipe(
                        Constants.API_KEY,
                        "35382"
                );

        responseCall.enqueue(new Callback<RecipeGetResponse>() {
            @Override
            public void onResponse(Call<RecipeGetResponse> call, Response<RecipeGetResponse> response) {
                Log.d(TAG, "onResponse: URL: " + response.raw().request().url());
                //https://recipesapi.herokuapp.com/api/get?key=&rId=35382
                Log.d(TAG, "onResponse: Server Response: " + response.toString());
                if (response.code() == 200) {
                    Recipe recipe = response.body().getRecipe();
                    Log.d(TAG, "onResponse: " + recipe.toString());
                } else {
                    try {
                        Log.d(TAG, "onResponse: " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Call<RecipeGetResponse> call, Throwable t) {
                Log.d(TAG, "onResponse: ERROR: " + t.getMessage());
            }
        });
    }
}
