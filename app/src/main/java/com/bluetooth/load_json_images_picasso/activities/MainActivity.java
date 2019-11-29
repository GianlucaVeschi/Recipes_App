package com.bluetooth.load_json_images_picasso.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.bluetooth.load_json_images_picasso.R;
import com.bluetooth.load_json_images_picasso.adapters.MealAdapter;
import com.bluetooth.load_json_images_picasso.models.Meal;
import com.bluetooth.load_json_images_picasso.networking.VolleyNetworkManager;
import com.bluetooth.load_json_images_picasso.networking.VolleyRequestListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MealAdapter.OnItemClickListener {

    public static final String EXTRA_MEAL = "MealParcel";

    //Distinguish between the two TypeViews
    private final static int HORIZONTAL_VIEW_TYPE = 1;
    private final static int VERTICAL_VIEW_TYPE = 2;

    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView_2;
    private RecyclerView mRecyclerView_3;

    private MealAdapter mFirstMealAdapter;
    private MealAdapter mSecondMealAdapter;
    private MealAdapter mThirdMealAdapter;

    private ArrayList<Meal> mFirstMealsList;
    private ArrayList<Meal> mSecondMealsList;
    private ArrayList<Meal> mThirdMealsList;
    private Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //First Recycler View containing Italian Recipes
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        mFirstMealsList = new ArrayList<>();
        mFirstMealAdapter = new MealAdapter(MainActivity.this, mFirstMealsList);

        //Second Recyclerview containing Chinese Recipes
        mRecyclerView_2 = findViewById(R.id.recycler_view_2);
        mRecyclerView_2.setHasFixedSize(true);
        mRecyclerView_2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        mSecondMealsList = new ArrayList<>();
        mSecondMealAdapter = new MealAdapter(MainActivity.this, mSecondMealsList);

        //Third Recyclerview containing German Recipes
        mRecyclerView_3 = findViewById(R.id.recycler_view_3);
        mRecyclerView_3.setHasFixedSize(true);
        mRecyclerView_3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        mThirdMealsList = new ArrayList<>();
        mThirdMealAdapter = new MealAdapter(MainActivity.this, mThirdMealsList);


        //Populate RecyclerView with recipes from the MealDatabase
        displayRecipeByCountry("Italian", mRecyclerView,mFirstMealsList,mFirstMealAdapter,HORIZONTAL_VIEW_TYPE);
        displayRecipeByCountry("Chinese", mRecyclerView_2,mSecondMealsList,mSecondMealAdapter,HORIZONTAL_VIEW_TYPE);
        displayRecipeByCountry("American",mRecyclerView_3,mThirdMealsList,mThirdMealAdapter,VERTICAL_VIEW_TYPE);

    }

    private void displayRecipeByCountry(String country, final RecyclerView recyclerView, final ArrayList<Meal> mealsList, final MealAdapter mealAdapter , final int orientation){
        VolleyNetworkManager.getInstance().filterRecipeByCountry(country, new VolleyRequestListener<JSONObject>() {
            @Override
            public void getResult(JSONObject meal_json) {

                //Deserialize object
                Meal meal = gson.fromJson(meal_json.toString(), Meal.class);
                meal.setorientationType(orientation);
                //Log.d(TAG, "getResult: GSON " + meal.toString());

                mealsList.add(meal);
                recyclerView.setAdapter(mealAdapter);
                mealAdapter.setOnItemClickListener(MainActivity.this);
            }
        });
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
