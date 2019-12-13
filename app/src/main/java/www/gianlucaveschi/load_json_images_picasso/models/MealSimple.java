package www.gianlucaveschi.load_json_images_picasso.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Map;

import androidx.annotation.NonNull;

public class MealSimple implements Parcelable {



    @SerializedName("idMeal")
    private String idMeal;

    @SerializedName("strMeal")
    private String mealName;

    @SerializedName("strMealThumb")
    private String imgUrl;

    private String strInstructions;

    //private identifier
    private int orientationType ;

    private Map<String,String> ingredients;

    public MealSimple(String mealName, String imgUrl, String idMeal , int orientationType) {
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

    public int getOrientationType() {
        return orientationType;
    }

    public void setOrientationType(int orientationType) {
        this.orientationType = orientationType;
    }

    public String getInstructions(){
        return strInstructions;
    }

    public Map<String,String> getIngredients() {
        return ingredients;
    }

    public void setStrInstructions(String strInstructions) {
        this.strInstructions = strInstructions;
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

    protected MealSimple(Parcel in) {
        mealName = in.readString();
        imgUrl = in.readString();
        idMeal = in.readString();
        orientationType = in.readInt();
    }

    public static final Creator<MealSimple> CREATOR = new Creator<MealSimple>() {
        @Override
        public MealSimple createFromParcel(Parcel in) {
            return new MealSimple(in);
        }

        @Override
        public MealSimple[] newArray(int size) {
            return new MealSimple[size];
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
