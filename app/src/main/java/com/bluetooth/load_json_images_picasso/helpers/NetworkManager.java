package com.bluetooth.load_json_images_picasso.helpers;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bluetooth.load_json_images_picasso.Meal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class NetworkManager {
    private static final String TAG = "NetworkManager";
    private static NetworkManager instance = null;

    public static final String BASE_URL = "https://www.themealdb.com/api/json/v1/1/";

    //for Volley API
    public RequestQueue requestQueue;

    private NetworkManager(Context context) {
        requestQueue = Volley.newRequestQueue(context.getApplicationContext());

    }

    public static synchronized NetworkManager getInstance(Context context)
    {
        if (instance == null)
            instance = new NetworkManager(context);
        return instance;
    }

    //this is so you don't need to pass context each time
    public static synchronized NetworkManager getInstance() {
        if (null == instance) {
            throw new IllegalStateException(NetworkManager.class.getSimpleName() +
                    " is not initialized, call getInstance(...) first");
        }
        return instance;
    }

    public void filterRecipeByCountry(String country, final VolleyRequestListener<JSONObject> listener) {

        String url = BASE_URL+ "filter.php?a=" + country;

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>()                {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: " + response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("meals");
                            for(int i = 0; i < jsonArray.length(); i++){
                                //JSONObject meal = jsonArray.getJSONObject(i);
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
                            Log.d(TAG + ": ", "Error Response code: " + error.networkResponse.statusCode);
                            //listener.getResult(false);
                        }
                    }
                });

        requestQueue.add(request);
    }
}
