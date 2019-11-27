package com.bluetooth.load_json_images_picasso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bluetooth.load_json_images_picasso.adapters.HorizontalMealAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements HorizontalMealAdapter.OnItemClickListener {

    public static final String EXTRA_RECIPE_URL = "imageUrl";
    public static final String EXTRA_RECIPE_NAME = "recipeName";
    public static final String EXTRA_RECIPE_ID = "recipeID";

    private static final String TAG = "MainActivity";
    private RecyclerView mRecyclerView;
    private RecyclerView mRecyclerView_2;
    private RecyclerView mRecyclerView_3;

    private HorizontalMealAdapter mFirstHorizontalMealAdapter;
    private HorizontalMealAdapter mSecondHorizontalMealAdapter;
    private HorizontalMealAdapter mThirdHorizontalMealAdapter;

    private ArrayList<Meal> mFirstMealsList;
    private ArrayList<Meal> mSecondMealsList;
    private ArrayList<Meal> mThirdMealsList;
    private RequestQueue mRequestQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //First Recycler View containing Italian Recipes
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        mFirstMealsList = new ArrayList<>();
        mFirstHorizontalMealAdapter = new HorizontalMealAdapter(MainActivity.this, mFirstMealsList);

        //Second Recyclerview containing Chinese Recipes
        mRecyclerView_2 = findViewById(R.id.recycler_view_2);
        mRecyclerView_2.setHasFixedSize(true);
        mRecyclerView_2.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL,false));
        mSecondMealsList = new ArrayList<>();
        mSecondHorizontalMealAdapter = new HorizontalMealAdapter(MainActivity.this, mSecondMealsList);

        //Third Recyclerview containing German Recipes
        mRecyclerView_3 = findViewById(R.id.recycler_view_3);
        mRecyclerView_3.setHasFixedSize(true);
        mRecyclerView_3.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false));
        mThirdMealsList = new ArrayList<>();
        mThirdHorizontalMealAdapter = new HorizontalMealAdapter(MainActivity.this, mThirdMealsList);


        //Populate RecyclerView with recipes from the Database
        mRequestQueue = Volley.newRequestQueue(this);
        parseJSONrecipesByCountry("Italian", mRecyclerView, mFirstMealsList, mFirstHorizontalMealAdapter);
        parseJSONrecipesByCountry("Chinese", mRecyclerView_2, mSecondMealsList, mSecondHorizontalMealAdapter);
        parseJSONrecipesByCountry("Spanish", mRecyclerView_3, mThirdMealsList, mThirdHorizontalMealAdapter);


    }

    private void parseJSONrecipesByCountry(String country, final RecyclerView recView, final ArrayList<Meal> mealsList, final HorizontalMealAdapter horizontalMealAdapter){
        String Recipes_mealDB = "https://www.themealdb.com/api/json/v1/1/filter.php?a=" + country;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, Recipes_mealDB, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: about to try to get the JSON ");
                        try {
                            JSONArray jsonArray = response.getJSONArray("meals");
                            Log.d(TAG, "onResponse array length: " + jsonArray.length());
                            for(int i = 0; i < jsonArray.length(); i++){
                                JSONObject meal = jsonArray.getJSONObject(i);

                                String mealName = meal.getString("strMeal");
                                String imageUrl = meal.getString("strMealThumb");
                                String idMeal = meal.getString("idMeal");

                                mealsList.add(new Meal(mealName,imageUrl, idMeal));
                                Log.d(TAG, "onResponse: " + mealName);
                            }


                            recView.setAdapter(horizontalMealAdapter);
                            horizontalMealAdapter.setOnItemClickListener(MainActivity.this);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d(TAG, "onErrorResponse: ");
                        error.printStackTrace();
                    }
                });
        mRequestQueue.add(request);
    }

    @Override
    public void onItemClick(int position, ArrayList<Meal> mealsList) {
        Intent detailIntent = new Intent(this, MealDetailsActivity.class);
        Meal clickedItem = mealsList.get(position);

        detailIntent.putExtra(EXTRA_RECIPE_URL, clickedItem.getImgUrl());
        detailIntent.putExtra(EXTRA_RECIPE_NAME, clickedItem.getMealName());
        detailIntent.putExtra(EXTRA_RECIPE_ID, clickedItem.getIdMeal());
        Log.d(TAG, "manage Intent send ID:" + clickedItem.getIdMeal());

        startActivity(detailIntent);
    }
}
