package com.bluetooth.load_json_images_picasso.activities;

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

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bluetooth.load_json_images_picasso.R;
import com.bluetooth.load_json_images_picasso.adapters.MealAdapter;
import com.bluetooth.load_json_images_picasso.models.MealContainer;
import com.bluetooth.load_json_images_picasso.models.MealSimple;
import com.bluetooth.load_json_images_picasso.networking.retrofit.MealAPI;
import com.bluetooth.load_json_images_picasso.networking.retrofit.RetrofitNetworkManager;
import com.bluetooth.load_json_images_picasso.networking.retrofit.RetrofitRequestListener;
import com.bluetooth.load_json_images_picasso.networking.volley.VolleyNetworkManager;
import com.bluetooth.load_json_images_picasso.networking.volley.VolleyRequestListener;
import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements MealAdapter.OnItemClickListener {

    public static final String EXTRA_MEAL = "MealParcel";

    //Distinguish between the two TypeViews
    private final static int HORIZONTAL_VIEW_TYPE = 1;
    private final static int VERTICAL_VIEW_TYPE = 2;

    private static final String TAG = "MainActivity";
    private MealAdapter mMealAdapter_1;

    private ArrayList<MealSimple> mMealsList_1;
    private Gson gson = new Gson();

    //SecondApproach for accessing retrofit
    MealAPI mealAPI;


    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawer = findViewById(R.id.drawer_layout);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //What is the purpose of this?
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();



        //First Recycler View containing Italian Recipes
        RecyclerView mRecyclerView;
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        mMealsList_1 = new ArrayList<>();
        mMealAdapter_1 = new MealAdapter(MainActivity.this, mMealsList_1);

        //Second Recyclerview containing Chinese Recipes
        RecyclerView mRecyclerView_2;
        mRecyclerView_2 = findViewById(R.id.recycler_view_2);
        mRecyclerView_2.setHasFixedSize(true);
        mRecyclerView_2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

        //Third Recyclerview containing German Recipes
        RecyclerView mRecyclerView_3;
        mRecyclerView_3 = findViewById(R.id.recycler_view_3);
        mRecyclerView_3.setHasFixedSize(true);
        mRecyclerView_3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));

        //Fourth RecyclerView containing Seafood Recipes
        RecyclerView mRecyclerView_4;
        mRecyclerView_4 = findViewById(R.id.recycler_view_4);
        mRecyclerView_4.setHasFixedSize(true);
        mRecyclerView_4.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));

        //Populate RecyclerView with recipes from the MealDatabase
        displayRecipesByCountryWithVolley("Italian", mRecyclerView,mMealsList_1,mMealAdapter_1,HORIZONTAL_VIEW_TYPE);
        displayRecipesByCountryWithRetrofit("Chinese", mRecyclerView_2,HORIZONTAL_VIEW_TYPE);
        displayRecipesByCountryWithRetrofit("American",mRecyclerView_3,VERTICAL_VIEW_TYPE);
        displayRecipesByCategoryWithRetrofit("Seafood",mRecyclerView_4,HORIZONTAL_VIEW_TYPE);

        //Try to Retrieve the data as Map<String,MealSimple>
        //displayRecipesByCountryWithRetrofitAsMealSimple("Japanese");

    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //Volley Implementation
    private void displayRecipesByCountryWithVolley(String country, final RecyclerView recyclerView, final ArrayList<MealSimple> mealsList, final MealAdapter mealAdapter , final int orientation){
        VolleyNetworkManager.getInstance().filterRecipeByCountry(country, new VolleyRequestListener<JSONObject>() {
            @Override
            public void getResult(JSONObject meal_json) {

                //TODO: 04/12/2019  Find a better way to update the UI
                //Deserialize object
                MealSimple mealSimple = gson.fromJson(meal_json.toString(), MealSimple.class);
                mealSimple.setOrientationType(orientation);

                //Update UI
                mealsList.add(mealSimple);
                recyclerView.setAdapter(mealAdapter);
                mealAdapter.setOnItemClickListener(MainActivity.this);
            }
        });
    }

    //Retrofit implementation
    private void displayRecipesByCountryWithRetrofitAsMealSimple(String country) {
        RetrofitNetworkManager retrofitNetworkManager = new RetrofitNetworkManager();
        retrofitNetworkManager.getMealsByCountryAsMealSimple(country);
    }

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
}
