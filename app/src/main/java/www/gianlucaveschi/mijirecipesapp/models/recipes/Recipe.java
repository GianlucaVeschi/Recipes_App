package www.gianlucaveschi.mijirecipesapp.models.recipes;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

public class Recipe implements Parcelable {

    private String title;
    private String publisher;
    private String publisher_url;
    private String[] ingredients;
    private String recipe_id;
    private String image_url;
    private float social_rank;

    public Recipe(String title, String publisher, String publisher_url, String[] ingredients,
                  String recipe_id, String image_url, float social_rank) {
        this.title = title;
        this.publisher = publisher;
        this.publisher_url = publisher_url;
        this.ingredients = ingredients;
        this.recipe_id = recipe_id;
        this.image_url = image_url;
        this.social_rank = social_rank;
    }

    public Recipe() {
    }

    protected Recipe(Parcel in) {
        title = in.readString();
        publisher = in.readString();
        publisher_url = in.readString();
        ingredients = in.createStringArray();
        recipe_id = in.readString();
        image_url = in.readString();
        social_rank = in.readFloat();
    }

    public static final Creator<Recipe> CREATOR = new Creator<Recipe>() {
        @Override
        public Recipe createFromParcel(Parcel in) {
            return new Recipe(in);
        }

        @Override
        public Recipe[] newArray(int size) {
            return new Recipe[size];
        }
    };

    public String getTitle() {
        return title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(publisher);
        parcel.writeString(publisher_url);
        parcel.writeStringArray(ingredients);
        parcel.writeString(recipe_id);
        parcel.writeString(image_url);
        parcel.writeFloat(social_rank);
    }

    @Override
    public String toString() {
        return "Recipe{"                                            + "\n" +
                "title='"           + title                         + "\n" +
                "publisher='"       + publisher                     + "\n" +
                "publisher_url='"   + publisher_url                 + "\n" +
                "ingredients="      + Arrays.toString(ingredients)  + "\n" +
                "recipe_id='"       + recipe_id                     + "\n" +
                "image_url='"       + image_url                     + "\n" +
                "social_rank="      + social_rank                   + "\n" +
                '}'                                                 + "\n";
    }
}