package www.gianlucaveschi.mijirecipesapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "meals")
public class Meal implements Parcelable {


    /*--------------------------------- INNER VARIABLES ------------------------------------------*/
    @PrimaryKey
    @NonNull
    @SerializedName("idMeal")
    private String idMeal;

    @SerializedName("strMeal")
    @ColumnInfo(name = "mealName")
    private String mealName;

    @SerializedName("strMealThumb")
    @ColumnInfo(name = "imgUrl")
    private String imgUrl;

    @ColumnInfo(name = "orientationType")
    private int orientationType;    //diff between vertical and horizontal item view in the adapter

    @ColumnInfo(name = "timestamp") //Saves current timestamp in **SECONDS**
    private int timestamp;

    /*--------------------------------- CONSTRUCTOR ----------------------------------------------*/

    public Meal(@NonNull String idMeal, String mealName, String imgUrl, int orientationType, int timestamp) {
        this.idMeal = idMeal;
        this.mealName = mealName;
        this.imgUrl = imgUrl;
        this.orientationType = orientationType;
        this.timestamp = timestamp;
    }

    @Ignore
    public Meal() {
    }

    @Ignore
    public Meal(Meal meal){
        this.idMeal     = meal.idMeal;
        this.mealName   = meal.mealName;
        this.imgUrl     = meal.imgUrl;
        this.orientationType = meal.orientationType;
        this.timestamp  = meal.timestamp;
    }

    /*--------------------------------- GETTERS --------------------------------------------------*/

    public String getMealName() {
        return mealName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public String getIdMeal() {
        return idMeal;
    }

    public int getOrientationType() {
        return orientationType;
    }

    public int getTimestamp() {
        return timestamp;
    }

    /*--------------------------------- SETTERS --------------------------------------------------*/

    public void setTimestamp(int timestamp) {
        this.timestamp = timestamp;
    }

    public void setOrientationType(int orientationType) {
        this.orientationType = orientationType;
    }

    @NonNull
    @Override
    public String toString() {
        return "Name: " + this.mealName + "\n" +
                "ID: "   + this.idMeal + "\n" +
                "URL: "  + this.imgUrl + "\n";
    }

    /*--------------------------------- PARCELABLE -----------------------------------------------*/

    //ReadString() and WriteString() have to be in the same order.
    protected Meal(Parcel in) {
        mealName = in.readString();
        imgUrl = in.readString();
        idMeal = in.readString();
        orientationType = in.readInt();
        timestamp = in.readInt();
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
        dest.writeInt(timestamp);
    }
}
