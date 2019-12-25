package www.gianlucaveschi.mijirecipesapp.models.others;

public class Ingredient {
    private String name;
    private StringBuilder ingredient_img_url = new StringBuilder("https://www.themealdb.com/images/ingredients/");

    public static final String PNG = ".png";


    public Ingredient(String name) {
        this.name = name;
        this.ingredient_img_url.append(name).append(PNG);
    }

    public String getName() {
        return name;
    }

    public String getIngredientImgUrl() {
        return ingredient_img_url.toString();
    }
}
