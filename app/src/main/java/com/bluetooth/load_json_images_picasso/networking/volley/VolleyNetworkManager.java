package com.bluetooth.load_json_images_picasso.networking.volley;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VolleyNetworkManager {
    private static final String TAG = "VolleyNetworkManager";
    private static VolleyNetworkManager instance = null;

    public static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";
    public static final String LOOKUP_ID = "lookup.php?i=";
    public static final String FILTER_COUNTRY = "filter.php?a=";

    //for Volley API
    public RequestQueue requestQueue;

    private VolleyNetworkManager(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }

    /** Singleton Class */
    public static synchronized VolleyNetworkManager getInstance(Context context) {
        if (instance == null)
            instance = new VolleyNetworkManager(context);
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized VolleyNetworkManager getInstance() {
        if (null == instance) {
            throw new IllegalStateException(VolleyNetworkManager.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    /**GET REQUEST
     * Used to retrieve filter the Recipe by country*/
    public void filterRecipeByCountry(String country, final VolleyRequestListener<JSONObject> listener) {

        String url = BASE_URL + FILTER_COUNTRY + country;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()                {
                    @Override
                    public void onResponse(JSONObject response) {
                        //Log.d(TAG, "onResponse: " + response.toString()); //Prints whole JSON
                        try {
                            JSONArray jsonArray = response.getJSONArray("meals");
                            for(int i = 0; i < jsonArray.length(); i++){
                                //Object containing the meal is then retrieved in the MainActivity
                                listener.getResult(jsonArray.getJSONObject(i));
                            }
                        }
                        catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.d(TAG + ": ", "Error MealMap code: " + error.networkResponse.statusCode);
                        }
                    }
                });
        requestQueue.add(request);
    }

    /**GET REQUEST
     * Used to retrieve filter the Recipe by ID*/
    public void filterRecipeByID(String recipeID, final VolleyRequestListener<JSONObject> listener){

        String url = BASE_URL + LOOKUP_ID + recipeID;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray jsonArray = response.getJSONArray("meals");
                            for(int i = 0; i < jsonArray.length(); i++){
                                //Object containing the details of the Recipe is retrieved in the MealDetailsActivity
                                listener.getResult(jsonArray.getJSONObject(i));
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (null != error.networkResponse) {
                            Log.d(TAG + ": ", "Error MealMap code: " + error.networkResponse.statusCode);
                        }
                    }
                });
        requestQueue.add(request);
    }
}
