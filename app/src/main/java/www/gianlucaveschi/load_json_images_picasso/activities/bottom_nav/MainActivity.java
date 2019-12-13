package www.gianlucaveschi.load_json_images_picasso.activities.bottom_nav;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import www.gianlucaveschi.load_json_images_picasso.activities.details.MealDetailsActivity;
import www.gianlucaveschi.load_json_images_picasso.activities.meal_drawer.MealMainIngredientActivity;
import www.gianlucaveschi.load_json_images_picasso.activities.meal_drawer.MealRandomActivity;
import www.gianlucaveschi.load_json_images_picasso.models.MealContainer;
import www.gianlucaveschi.load_json_images_picasso.models.MealSimple;
import www.gianlucaveschi.load_json_images_picasso.networking.retrofit.MealAPI;
import www.gianlucaveschi.load_json_images_picasso.networking.retrofit.RetrofitNetworkManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import www.gianlucaveschi.load_json_images_picasso.activities.meal_drawer.MealCategoriesActivity;
import www.gianlucaveschi.load_json_images_picasso.utils.BottomNavigationViewHelper;
import com.gianlucaveschi.load_json_images_picasso.R;
import www.gianlucaveschi.load_json_images_picasso.adapters.MealAdapter;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MealAdapter.OnItemClickListener , NavigationView.OnNavigationItemSelectedListener {

    public static final String EXTRA_MEAL = "MealParcel";

    //Distinguish between the two TypeViews
    private final static int HORIZONTAL_VIEW_TYPE = 1;
    private final static int VERTICAL_VIEW_TYPE = 2;

    private static final String TAG = "MainActivity";

    //Retrofit instance
    MealAPI mealAPI;

    //UI components
    private DrawerLayout drawer;
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView_2;
    private RecyclerView mRecyclerView_3;
    private RecyclerView mRecyclerView_4;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the Drawer Layout
        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //Set the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        //toolbar.setBackgroundColor(todo set a nice color);
        setSupportActionBar(toolbar);

        //Adds the "Hamburger" to the toolbar,which opens the drawer layout
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState(); //Rotates the hamburger Icon

        //Set the Bottom Navigation Bar
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavView_Bar);
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

    private void initRecyclerViews() {

        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

        //Second Recyclerview containing Chinese Recipes
        mRecyclerView_2 = findViewById(R.id.recycler_view_2);
        mRecyclerView_2.setHasFixedSize(true);
        mRecyclerView_2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

        //Third Recyclerview containing German Recipes
        mRecyclerView_3 = findViewById(R.id.recycler_view_3);
        mRecyclerView_3.setHasFixedSize(true);
        mRecyclerView_3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        //Fourth RecyclerView containing Seafood Recipes
        mRecyclerView_4 = findViewById(R.id.recycler_view_4);
        mRecyclerView_4.setHasFixedSize(true);
        mRecyclerView_4.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

    }

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
        MealAdapter mealAdapter = new MealAdapter(MainActivity.this, mealsList);
        recyclerView.setAdapter(mealAdapter);
        mealAdapter.setOnItemClickListener(MainActivity.this);
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
                Intent mealCountriesIntent = new Intent(this, MealCategoriesActivity.class);
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
                    Intent intentAboutMe = new Intent(MainActivity.this, AboutMeActivity.class);
                    startActivity(intentAboutMe);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;

                case R.id.navigation_about_miji:
                    Intent intentAboutMiji = new Intent(MainActivity.this, AboutMiji.class);
                    startActivity(intentAboutMiji);
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    break;

            }
            return false;
        }
    };
}
