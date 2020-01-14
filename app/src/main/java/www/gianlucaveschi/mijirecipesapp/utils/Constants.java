package www.gianlucaveschi.mijirecipesapp.utils;

public class Constants {
    public static final int NETWORK_TIMEOUT = 3000;
    public static final String RECIPES_BASE_URL = "https://recipesapi.herokuapp.com/";
    public static final String API_KEY = "";
    public static final String EXTRA_MEAL = "MealParcel";
    public static final String EXTRA_RECIPE = "RecipeParcel";

    public static final String LOADING = "LOADING...";

    //Distinguish between the two TypeViews in the MealAdapter
    public final static int HORIZONTAL_VIEW_TYPE = 1;
    public final static int VERTICAL_VIEW_TYPE   = 2;
    public static final String[] DEFAULT_SEARCH_CATEGORIES =
            {"Barbeque", "Breakfast", "Chicken", "Beef", "Brunch", "Dinner", "Wine", "Italian"};

    public static final String[] DEFAULT_SEARCH_CATEGORY_IMAGES =
            {
                    "barbeque",
                    "breakfast",
                    "chicken",
                    "beef",
                    "brunch",
                    "dinner",
                    "wine",
                    "italian"
            };

}
