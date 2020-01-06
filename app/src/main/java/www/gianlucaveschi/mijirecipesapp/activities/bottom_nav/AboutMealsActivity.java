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
import www.gianlucaveschi.mijirecipesapp.activities.meal_drawer.MealCountriesActivity;
import www.gianlucaveschi.mijirecipesapp.activities.meal_drawer.MealMainIngredientActivity;
import www.gianlucaveschi.mijirecipesapp.activities.meal_drawer.MealRandomActivity;
import www.gianlucaveschi.mijirecipesapp.adapters.MealAdapter;
import www.gianlucaveschi.mijirecipesapp.models.meals.MealContainer;
import www.gianlucaveschi.mijirecipesapp.models.meals.MealSimple;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.MealAPI;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.themealdb.RetrofitNetworkManager;
import www.gianlucaveschi.mijirecipesapp.utils.BottomNavigationViewHelper;

import com.gianlucaveschi.load_json_images_picasso.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class AboutMealsActivity extends AppCompatActivity implements MealAdapter.OnItemClickListener , NavigationView.OnNavigationItemSelectedListener {

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
    @BindView(R.id.recycler_view)   RecyclerView mRecyclerView;
    @BindView(R.id.recycler_view_2) RecyclerView mRecyclerView_2;
    @BindView(R.id.recycler_view_4) RecyclerView mRecyclerView_4;

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
        initRecyclerViews();

        //Populate RecyclerView with recipes from the MealDatabase
        displayRecipesByCountryWithRetrofit("Italian", mRecyclerView,HORIZONTAL_VIEW_TYPE);
        displayRecipesByCountryWithRetrofit("Chinese", mRecyclerView_2,HORIZONTAL_VIEW_TYPE);
        //displayRecipesByCountryWithRetrofit("American",mRecyclerView_3,VERTICAL_VIEW_TYPE);
        displayRecipesByCategoryWithRetrofit("Seafood",mRecyclerView_4,HORIZONTAL_VIEW_TYPE);

        //Try to Retrieve the data as Map<String,MealSimple>
        //displayRecipesByCountryWithRetrofitAsMealSimple("Japanese");

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
                mealAdapter.getFilter().filter(newText);
                Log.d(TAG, "onQueryTextChange:" );
                return false;
            }
        });
        return true;
    }


    private void initRecyclerViews() {

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

        //Second RecyclerView containing Chinese Recipes
        mRecyclerView_2.setHasFixedSize(true);
        mRecyclerView_2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

        //Fourth RecyclerView containing Seafood Recipes
        mRecyclerView_4.setHasFixedSize(true);
        mRecyclerView_4.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

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
        mealAPI.getMealsByCountry(country).enqueue(new Callback<MealContainer>() {
            @Override
            public void onResponse(Call<MealContainer> call, Response<MealContainer> response) {

                //Get Retrofit Response
                MealContainer mealContainer = response.body();

                //Set orientation for the rv
                mealContainer.setOrientation(orientation);

                //Update UI
                updateUserInterface(mealContainer,recyclerView);
            }

            @Override
            public void onFailure(Call<MealContainer> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    private void displayRecipesByCategoryWithRetrofit(String category, final RecyclerView recyclerView, final int orientation){
        mealAPI = RetrofitNetworkManager.getClient().create(MealAPI.class);
        mealAPI.getMealsByCategory(category).enqueue(new Callback<MealContainer>() {
            @Override
            public void onResponse(Call<MealContainer> call, Response<MealContainer> response) {
                //Get Retrofit Response
                MealContainer mealContainer = response.body();

                //Set orientation for the rv
                mealContainer.setOrientation(orientation);

                //Update UI
                updateUserInterface(mealContainer,recyclerView);
            }
            @Override
            public void onFailure(Call<MealContainer> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getLocalizedMessage());
            }
        });
    }

    /**
     * Update User Interface
     * */
    private void updateUserInterface(MealContainer mealContainer, RecyclerView recyclerView){
        ArrayList<MealSimple> mealsList = mealContainer.getMealSimples();
        mealAdapter = new MealAdapter(AboutMealsActivity.this, mealsList);
        recyclerView.setAdapter(mealAdapter);
        mealAdapter.setOnItemClickListener(AboutMealsActivity.this);
    }

    /**
     * OnItemClick
     * */
    @Override
    public void onItemClick(int position, ArrayList<MealSimple> mealsList) {
        Intent detailIntent = new Intent(this, MealDetailsActivity.class);
        MealSimple clickedItem = mealsList.get(position);

        //Send Parcel to the Details Activity
        detailIntent.putExtra(EXTRA_MEAL,clickedItem);
        startActivity(detailIntent);
    }

    /**
     * Navigation View Item Listener*/
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case(R.id.nav_meal_countries):
                Intent mealCountriesIntent = new Intent(this, MealCountriesActivity.class);
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
