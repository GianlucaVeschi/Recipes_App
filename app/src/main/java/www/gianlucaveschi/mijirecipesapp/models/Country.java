package www.gianlucaveschi.mijirecipesapp.models;

import java.util.Random;

import static www.gianlucaveschi.mijirecipesapp.utils.Constants.DEFAULT_SEARCH_COUNTRIES;

public class Country {
    private String name;
    private StringBuilder flag_img_url = new StringBuilder("https://www.countryflags.io/");

    public static final String shinyTheme = "/shiny";
    public static final String flatTheme  = "/flat";
    public static final String pixelSize  = "/64.png"; //16 24 32 48 64


    public Country(String name, String flag_id) {
        this.name = name;
        this.flag_img_url.append(flag_id).append(shinyTheme).append(pixelSize);
    }

    public String getName() {
        return name;
    }

    public String getFlag_img_url() {
        return flag_img_url.toString();
    }

    public static String getRandomCountry() {
        int randomCountry = new Random().nextInt(DEFAULT_SEARCH_COUNTRIES.length);
        return DEFAULT_SEARCH_COUNTRIES[randomCountry];
    }
}
