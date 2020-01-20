package www.gianlucaveschi.mijirecipesapp.activities.bottom_nav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import butterknife.BindView;
import butterknife.ButterKnife;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.gianlucaveschi.mijirecipesapp.activities.details.MealDetailsActivity;
import www.gianlucaveschi.mijirecipesapp.activities.meal_drawer.MealCountriesFlagsActivity;
import www.gianlucaveschi.mijirecipesapp.activities.meal_drawer.MealMainIngredientActivity;
import www.gianlucaveschi.mijirecipesapp.activities.meal_drawer.MealRandomActivity;
import www.gianlucaveschi.mijirecipesapp.activities.meal_drawer.RecipeCategoriesActivity;
import www.gianlucaveschi.mijirecipesapp.adapters.FoodCategoryAdapter;
import www.gianlucaveschi.mijirecipesapp.adapters.meals.MealAdapter;
import www.gianlucaveschi.mijirecipesapp.adapters.meals.OnMealClickListener;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.responses.MealResponse;
import www.gianlucaveschi.mijirecipesapp.models.Meal;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.MealAPI;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.RetrofitNetworkManager;
import www.gianlucaveschi.mijirecipesapp.utils.UI.BottomNavigationViewHelper;
import www.gianlucaveschi.mijirecipesapp.utils.Constants;
import www.gianlucaveschi.mijirecipesapp.utils.UI.HorizontalSpacingItemDecorator;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Arrays;

public class AboutMealsActivity extends AppCompatActivity implements OnMealClickListener, FoodCategoryAdapter.OnFoodCategoryClickListener, NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_MEAL = "MealParcel";

    //Distinguish between the two TypeViews
    private final static int HORIZONTAL_VIEW_TYPE = 1;
    private final static int VERTICAL_VIEW_TYPE = 2;

    private static final String TAG = "AboutMealsActivity";

    //Retrofit instance
    MealAPI mealAPI;
    MealAdapter mealAdapter;

    //UI components
    @BindView(R.id.drawer_layout)   DrawerLayout drawer;
    @BindView(R.id.nav_view)        NavigationView navigationView;
    @BindView(R.id.toolbar)         Toolbar toolbar;
    @BindView(R.id.bottom_nav_view) BottomNavigationView bottomNavigationView;
    @BindView(R.id.categories_rec_view)   RecyclerView mFoodCategoriesRecView;
    @BindView(R.id.top_recycler_view)   RecyclerView topRecView;
    @BindView(R.id.central_recycler_view) RecyclerView centralMealsRecView;
    @BindView(R.id.bottom_recycler_view) RecyclerView bottomRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_meals);
        ButterKnife.bind(this);

        //Set the Drawer Layout
        navigationView.setNavigationItemSelectedListener(this);

        //Set the toolbar
        setSupportActionBar(toolbar);

        //Adds the "Hamburger" to the toolbar,which opens the drawer layout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        toggle.syncState(); //Rotates the hamburger Icon
        drawer.addDrawerListener(toggle);

        //Set the Bottom Navigation Bar
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);

        //Highlight the touched button
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        //Init RecyclerViews
        initFoodCategoriesRecView();
        initMealRecyclerViews();

        //Populate RecyclerView with recipes from the MealDatabase
            displayRecipesByCountryWithRetrofit("Italian", topRecView,VERTICAL_VIEW_TYPE);
        displayRecipesByCountryWithRetrofit("Chinese", centralMealsRecView,HORIZONTAL_VIEW_TYPE);
        displayRecipesByCategoryWithRetrofit("Seafood",bottomRecView,HORIZONTAL_VIEW_TYPE);
    }

    private void initFoodCategoriesRecView() {
        HorizontalSpacingItemDecorator itemDecorator = new HorizontalSpacingItemDecorator(5);
        ArrayList<String> foodCategories = new ArrayList<>(Arrays.asList(Constants.DEFAULT_SEARCH_CATEGORIES));

        FoodCategoryAdapter foodCategoryAdapter = new FoodCategoryAdapter(foodCategories);
        foodCategoryAdapter.setOnFoodCategoryClickListener(AboutMealsActivity.this);

        mFoodCategoriesRecView.addItemDecoration(itemDecorator);
        mFoodCategoriesRecView.setHasFixedSize(true);
        mFoodCategoriesRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        mFoodCategoriesRecView.setAdapter(foodCategoryAdapter);
    }

    //Inflate the Menu of the Drawer Layout
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_miji_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        Log.d(TAG, "onCreateOptionsMenu: ");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                mealAdapter.getFilter().filter(newText); // TODO: 17/01/2020 not working, maybe change this with a recipe search
                Log.d(TAG, "onQueryTextChange:" );
                return false;
            }
        });
        return true;
    }


    private void initMealRecyclerViews() {

        //First Recycler View
        topRecView.setHasFixedSize(true);
        topRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        topRecView.setAdapter(mealAdapter);

        //Second Recycler View
        centralMealsRecView.setHasFixedSize(true);
        centralMealsRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        topRecView.setAdapter(mealAdapter);

        //Third Recycler View
        bottomRecView.setHasFixedSize(true);
        bottomRecView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        topRecView.setAdapter(mealAdapter);
    }

    //If the user presses the Back button while the drawer is open
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    /**
     * Retrofit Calls
     * */
    private void displayRecipesByCountryWithRetrofit(String country, final RecyclerView recyclerView, final int orientation){
        mealAPI = RetrofitNetworkManager.getClient().create(MealAPI.class);
        mealAPI.getMealsByCountry(country).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {

                //Get Retrofit Response
                MealResponse mealResponse = response.body();

                //Set orientation for the rv
                mealResponse.setOrientation(orientation);

                //Update UI
                updateUserInterface(mealResponse,recyclerView);
            }

            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void displayRecipesByCategoryWithRetrofit(String category, final RecyclerView recyclerView, final int orientation){
        mealAPI = RetrofitNetworkManager.getClient().create(MealAPI.class);
        mealAPI.getMealsByCategory(category).enqueue(new Callback<MealResponse>() {
            @Override
            public void onResponse(Call<MealResponse> call, Response<MealResponse> response) {
                //Get Retrofit Response
                MealResponse mealResponse = response.body();

                //Set orientation for the rv
                mealResponse.setOrientation(orientation);

                //Update UI
                updateUserInterface(mealResponse,recyclerView);
            }
            @Override
            public void onFailure(Call<MealResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    /**
     * Update User Interface
     * */
    private void updateUserInterface(MealResponse mealResponse, RecyclerView recyclerView){
        ArrayList<Meal> mealsList = mealResponse.getMeals();
        mealAdapter = new MealAdapter(AboutMealsActivity.this, mealsList);
        mealAdapter.setLIMIT_LIST_ITEMS(10);
        recyclerView.setAdapter(mealAdapter);
        mealAdapter.setOnMealClickListener(AboutMealsActivity.this);
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

    @Override
    public void onFoodCategoryClick(int position, ArrayList<String> foodCategoriesList) {
        String categoryName = foodCategoriesList.get(position);
        Intent displayFoodCategory = new Intent(AboutMealsActivity.this,RecipeCategoriesActivity.class);
        displayFoodCategory.putExtra(Constants.EXTRA_RECIPE_CAT,categoryName);
        startActivity(displayFoodCategory);
    }

    /**
     * Navigation View Item Listener*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case(R.id.nav_meal_countries):
                Intent mealCountriesIntent = new Intent(this, MealCountriesFlagsActivity.class);
                startActivity(mealCountriesIntent);
                break;
            case(R.id.nav_meal_random):
                Intent mealRandomIntent = new Intent(this, MealRandomActivity.class);
                startActivity(mealRandomIntent);
                break;
            case(R.id.nav_meal_main_ingredient):
                Intent mealMainIngredientIntent = new Intent(this, MealMainIngredientActivity.class);
                startActivity(mealMainIngredientIntent);
                break;
            case(R.id.nav_recipes_categories):
                Intent recipeCategoriesIntent = new Intent(this, RecipeCategoriesActivity.class);
                startActivity(recipeCategoriesIntent);
                break;
            case R.id.nav_share:
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_send:
                Toast.makeText(this, "Send", Toast.LENGTH_SHORT).show();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.contact_miji_button){
            Toast.makeText(this,"Soon you will be able to send me an email",Toast.LENGTH_SHORT)
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Bottom Navigation View
     * */
    //Handles the logic of the Bottom Navigation View
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch(item.getItemId()){
                case R.id.navigation_about_me:
                    Intent intentAboutMe = new Intent(AboutMealsActivity.this, AboutMeActivity.class);
                    startActivity(intentAboutMe);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;

                case R.id.navigation_about_miji:
                    Intent intentAboutMiji = new Intent(AboutMealsActivity.this, AboutMijiActivity.class);
                    startActivity(intentAboutMiji);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;
            }
            return false;
        }
    };
}
