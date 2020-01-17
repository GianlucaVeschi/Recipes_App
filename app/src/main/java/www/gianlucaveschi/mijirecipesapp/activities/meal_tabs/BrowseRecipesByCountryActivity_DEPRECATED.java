package www.gianlucaveschi.mijirecipesapp.activities.meal_tabs;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.r0adkll.slidr.Slidr;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import www.gianlucaveschi.mijirecipesapp.activities.details.RecipeDetailsActivity;
import www.gianlucaveschi.mijirecipesapp.adapters.recipes.OnRecipeListener;
import www.gianlucaveschi.mijirecipesapp.adapters.recipes.RecipeAdapter;
import www.gianlucaveschi.mijirecipesapp.models.Recipe;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;
import www.gianlucaveschi.mijirecipesapp.utils.MyLogger;
import www.gianlucaveschi.mijirecipesapp.viewmodels.RecipesCategoriesViewModel;


public class BrowseRecipesByCountryActivity_DEPRECATED extends AppCompatActivity implements OnRecipeListener {

    private static final String TAG = "BrowseRecByCountryAct";

    //Bind UI
    @BindView(R.id.toolbar)                         Toolbar toolbar;
    @BindView(R.id.browse_meals_recycler_view)      RecyclerView mealsRecyclerView;
    @BindView(R.id.search_view)                     SearchView searchView;

    //View Model
    private static RecipesCategoriesViewModel mBrowseRecipesByCountryViewModel;

    private RecipeAdapter recipeAdapter;
    private List<Recipe> recipes;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse_recipes_by_country);
        ButterKnife.bind(this);
        mBrowseRecipesByCountryViewModel = ViewModelProviders.of(this).get(RecipesCategoriesViewModel.class);

        //Get Intent
        Intent intent = getIntent();
        String flagImgUrl   = intent.getStringExtra("flag_url");
        String countryName  = intent.getStringExtra("country_name");

        //Set UI
        String toolbar_str = countryName + " Meals";
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(toolbar_str);
        initRecyclerView();
        initSearchView();

        //Observe the LiveData and Update the UI
        subscribeObservers();
        displayRecipesByCountry(countryName);       //Fill the recycler view

        //Slide back to the Previous Activity
        Slidr.attach(this);
    }

    //Retrieve Live Data from the Repository
    public LiveData<List<Recipe>> getRecipes() {
        return mBrowseRecipesByCountryViewModel.getRecipes();
    }

    //Observe Live Data
    private void subscribeObservers() {
        mBrowseRecipesByCountryViewModel.getRecipes().observe(this, new Observer<List<Recipe>>() {
            @Override
            public void onChanged(@Nullable List<Recipe> receivedRecipes) {
                if (receivedRecipes != null) {
                    MyLogger.logRecipes("onChanged", receivedRecipes);
                    recipes = new ArrayList<>(receivedRecipes);
                    mBrowseRecipesByCountryViewModel.setIsPerformingQuery(false); // The query is complete
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
        recipeAdapter.displayLoading();
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
        mBrowseRecipesByCountryViewModel.searchRecipesApi(query, pageNumber);
    }

    private void initSearchView(){
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                recipeAdapter.displayLoading();
                mBrowseRecipesByCountryViewModel.searchRecipesApi(query, 1);
                searchView.clearFocus(); //The focus has to be cleared in order to properly cancel a Retrofit request
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if(mBrowseRecipesByCountryViewModel.onBackPressed()){
            super.onBackPressed();
        }
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

    /*
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
     */
}
