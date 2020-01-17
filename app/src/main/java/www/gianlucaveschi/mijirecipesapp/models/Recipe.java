package www.gianlucaveschi.mijirecipesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "recipes")
public class Recipe implements Parcelable {

    @PrimaryKey
    @NonNull
    private String recipe_id;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "publisher")
    private String publisher;

    @ColumnInfo(name = "image_url")
    private String image_url;

    @ColumnInfo(name = "social_rank")
    private float social_rank;

    @ColumnInfo(name = "ingredients")
    private String[] ingredients;

    /**
     * Saves current timestamp in **SECONDS**
     */
    @ColumnInfo(name = "timestamp")
    private int timestamp;

    public Recipe(@NonNull String recipe_id, String title, String publisher, String image_url, float social_rank, String[] ingredients, int timestamp) {
        this.recipe_id = recipe_id;
        this.title = title;
        this.publisher = publisher;
        this.image_url = image_url;
        this.social_rank = social_rank;
        this.ingredients = ingredients;
        this.timestamp = timestamp;
    }

    @Ignore
    public Recipe() {
    }

    protected Recipe(Parcel in) {
        recipe_id = in.readString();
        title = in.readString();
        publisher = in.readString();
        image_url = in.readString();
        social_rank = in.readFloat();
        ingredients = in.createStringArray();
        timestamp = in.readInt();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel destination, int i) {
        destination.writeString(title);
        destination.writeString(publisher);
        destination.writeStringArray(ingredients);
        destination.writeString(recipe_id);
        destination.writeString(image_url);
        destination.writeFloat(social_rank);
        destination.writeInt(timestamp);
    }

    @Override
    public String toString() {
        return "Recipe{"                                            + "\n" +
                "title='"           + title                         + "\n" +
                "publisher='"       + publisher                     + "\n" +
                "ingredients="      + Arrays.toString(ingredients)  + "\n" +
                "recipe_id='"       + recipe_id                     + "\n" +
                "image_url='"       + image_url                     + "\n" +
                "social_rank="      + social_rank                   + "\n" +
                "time stamp="      + timestamp                     + "\n" +
                '}'                                                 + "\n";
    }

    //SETTERS

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public void setIngredients(String[] ingredients) {
        this.ingredients = ingredients;
    }

    public void setRecipe_id(String recipe_id) {
        this.recipe_id = recipe_id;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public void setSocial_rank(float social_rank) {
        this.social_rank = social_rank;
    }

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    //GETTERS

    public String getTitle() {
        return title;
    }

    public String getRecipe_id() {
        return recipe_id;
    }

    public String getPublisher() {
        return publisher;
    }

    public float getSocial_rank() {
        return social_rank;
    }

    public String[] getIngredients() {
        return ingredients;
    }

    public String getImage_url() {
        return image_url;
    }

    public int getTimestamp() {
        return timestamp;
    }
}