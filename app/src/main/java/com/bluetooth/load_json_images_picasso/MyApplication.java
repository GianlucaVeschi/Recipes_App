package com.bluetooth.load_json_images_picasso;

import android.app.Application;

import com.bluetooth.load_json_images_picasso.networking.volley.VolleyNetworkManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VolleyNetworkManager.getInstance(this);
    }
}
