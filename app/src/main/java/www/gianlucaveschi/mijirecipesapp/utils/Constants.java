package www.gianlucaveschi.mijirecipesapp.utils;

import java.util.HashMap;
import java.util.Map;

public class Constants {
    public static final int NETWORK_TIMEOUT = 10000;
    public static final String RECIPES_BASE_URL = "https://recipesapi.herokuapp.com/";
    public static final String RECIPES_API_KEY  = "";
    public static final String MEALS_BASE_URL   = "https://www.themealdb.com/api/json/v1/1/";
    public static final String EXTRA_MEAL = "MealParcel";
    public static final String EXTRA_RECIPE = "RecipeParcel";
    public static final String EXTRA_RECIPE_CAT = "RecipeCatParcel";
    public static final String HEADER_CACHE_CONTROL = "Cache-Control";
    public static final String HEADER_PRAGMA = "Pragma";


    public static final String LOADING = "LOADING...";
    public static final String QUERY_EXHAUSTED = "Query is exhausted.";

    //Distinguish between TypeViews in the MealAdapter
    public final static int HORIZONTAL_VIEW_TYPE = 1;
    public final static int VERTICAL_VIEW_TYPE = 2;

    /*------------------------------------- TIMEOUTS ---------------------------------------------*/
    public static final int CONNECTION_TIMEOUT = 10; // 10 seconds
    public static final int READ_TIMEOUT = 2; // 2 seconds
    public static final int WRITE_TIMEOUT = 2; // 2 seconds
    public static final int RECIPE_REFRESH_TIME = 60 * 60 * 24 * 30; //30 days in seconds

    /*---------------------------------------- RECIPES -------------------------------------------*/

    public static final String[] DEFAULT_SEARCH_CATEGORY_RECIPE = {
            "barbeque", "breakfast",    "chicken",
            "beef",     "brunch",       "dinner",
            "wine",     "italian",      "vegetarian",
            "vegan"
    };


    /*---------------------------------------- MEALS ---------------------------------------------*/

    public static final String[] DEFAULT_SEARCH_COUNTRIES = {
            "Italian",  "Spanish",  "French",
            "Canadian", "Jamaican", "Chinese",
            "Indian",   "Japanese", "Mexican",
            "Moroccan", "Russian",  "Thai",
            "British",  "American", "Dutch",
            "Tunisian"
    };

    public static final String[] DEFAULT_SEARCH_COUNTRIES_CODES = {
            "IT","ES","FR",
            "CA","JM","CN",
            "IN","JP","MX",
            "MA","RU","TH",
            "GB","US","NL",
            "TN"
    };

    //This could be done for Country and CountryFlagCode....
    public static final Map<String, String> myMapOfCountries = new HashMap<String, String>() {{
        put("Italian", "IT");
        put("Spanish", "ES");
    }};
}
