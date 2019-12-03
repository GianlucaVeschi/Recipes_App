package com.bluetooth.load_json_images_picasso.networking.retrofit;

import com.bluetooth.load_json_images_picasso.models.Meal;

import java.util.List;

public interface RetrofitRequestListener<T> {
    void getResult(T object);
    //List<Meal> getResult(T object);
}

