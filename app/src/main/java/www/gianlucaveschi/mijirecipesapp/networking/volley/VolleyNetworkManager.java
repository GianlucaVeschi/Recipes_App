package www.gianlucaveschi.mijirecipesapp.networking.volley;

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
    public static final String RANDOM_MEAL = "random.php";

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


    //TODO
    // I don't think it is a good practice to map a JSON object into a POJO model when it has too many
    // parameters that represent the exact same thing (StrIngredients)
    // I tried to let retrofit return the JsonArray and then manually handle the parameters
    // as I do with Volley but this is proving to be a lot of work.

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

    /**GET REQUEST
     * Used to retrieve random recipe*/
    public void getRandomRecipe(final VolleyRequestListener<JSONObject> listener){
        String url = BASE_URL + RANDOM_MEAL;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try{
                            JSONArray jsonArray = response.getJSONArray("meals");
                            for(int i = 0; i < jsonArray.length(); i++){
                                //Object containing the details of the Recipe is retrieved in the MealRandomActivity
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
