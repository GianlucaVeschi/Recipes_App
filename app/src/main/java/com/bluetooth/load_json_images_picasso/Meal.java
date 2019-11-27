package com.bluetooth.load_json_images_picasso;

public class Meal {
    private String mealName;
    private String imgUrl;
    private String idMeal;
    private String instructions;

    public Meal(String mealName, String imgUrl, String idMeal) {
        this.mealName = mealName;
        this.imgUrl = imgUrl;
        this.idMeal = idMeal;
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
}
