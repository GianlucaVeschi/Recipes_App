package com.bluetooth.load_json_images_picasso.networking.volley;

public interface VolleyRequestListener<T> {
        void getResult(T object);
}