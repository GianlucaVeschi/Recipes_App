package www.gianlucaveschi.load_json_images_picasso.models.meals;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class MealMap {

    @SerializedName("meals")
    @Expose
    Map<String, MealSimple> MyObject;

    public Map<String, MealSimple> getMyObject() {
        return MyObject;
    }

    public void setMyObject(Map<String, MealSimple> myObject) {
        MyObject = myObject;
    }

    public String getMealStrings(){
        return getMyObject().values().toString();
    }
}
