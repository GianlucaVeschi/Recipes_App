package www.gianlucaveschi.mijirecipesapp;

import android.app.Application;

import www.gianlucaveschi.mijirecipesapp.networking.retrofit.RetrofitNetworkManager;
import www.gianlucaveschi.mijirecipesapp.networking.volley.VolleyNetworkManager;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        VolleyNetworkManager.getInstance(this);
        RetrofitNetworkManager.getClient(this);
    }
}
