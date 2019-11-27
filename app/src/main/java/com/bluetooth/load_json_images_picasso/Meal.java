package com.bluetooth.load_json_images_picasso;

public class Meal {
    private String mealName;
    private String imgUrl;
    private String idMeal;
    private int type;//comment


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
}
