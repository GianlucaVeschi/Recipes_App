package com.bluetooth.load_json_images_picasso.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

import androidx.annotation.NonNull;

public class Meal implements Parcelable {

    @SerializedName("idMeal")
    private String idMeal;

    @SerializedName("strMeal")
    private String mealName;

    @SerializedName("strMealThumb")
    private String imgUrl;

    //private identifier
    private int orientationType ;

    private Map<String,String> ingredients;

    public Meal(String mealName, String imgUrl, String idMeal , int orientationType) {
        this.mealName = mealName;
        this.imgUrl = imgUrl;
        this.idMeal = idMeal;
        this.orientationType = orientationType;
    }

    public String getMealName() {
        return mealName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getIdMeal() {
        return idMeal;
    }

    public int getorientationType() {
        return orientationType;
    }

    public void setorientationType(int orientationType) {
        this.orientationType = orientationType;
    }

    public Map<String,String> getIngredients() {
        return ingredients;
    }

    @NonNull
    @Override
    public String toString() {
        String result =
                "Name: " + this.mealName + "\n" +
                "ID: "   + this.idMeal + "\n" +
                "URL: "  + this.imgUrl + "\n";
        return result;
    }

    /**
     * Implement Parcelable Interface
     *
     * ReadString() and WriteString() have to be in the same order.
     * */

    protected Meal(Parcel in) {
        mealName = in.readString();
        imgUrl = in.readString();
        idMeal = in.readString();
        orientationType = in.readInt();
    }

    public static final Creator<Meal> CREATOR = new Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel in) {
            return new Meal(in);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mealName);
        dest.writeString(imgUrl);
        dest.writeString(idMeal);
        dest.writeInt(orientationType);
    }
}
