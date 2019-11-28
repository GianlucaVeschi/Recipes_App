package com.bluetooth.load_json_images_picasso;

import android.os.Parcel;
import android.os.Parcelable;

public class Meal implements Parcelable {
    private String mealName;
    private String imgUrl;
    private String idMeal;
    private int type;


    public Meal(String mealName, String imgUrl, String idMeal , int type) {
        this.mealName = mealName;
        this.imgUrl = imgUrl;
        this.idMeal = idMeal;
        this.type = type;
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
        type = in.readInt();
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
        dest.writeInt(type);
    }
}
