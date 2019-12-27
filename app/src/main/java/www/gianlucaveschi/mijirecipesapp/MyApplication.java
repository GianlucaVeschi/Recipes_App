package www.gianlucaveschi.mijirecipesapp;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import www.gianlucaveschi.mijirecipesapp.networking.retrofit.RetrofitNetworkManager;
import www.gianlucaveschi.mijirecipesapp.networking.volley.VolleyNetworkManager;

public class MyApplication extends Application {

    private static MyApplication applicationInstance;



    @Override
    public void onCreate() {
        super.onCreate();
        if(applicationInstance == null){
            applicationInstance = this;
        }
        VolleyNetworkManager.getInstance(this);
        RetrofitNetworkManager.getClient(this);
    }

    public static MyApplication getInstance(){
        return applicationInstance;
    }

    public static boolean hasNetwork(){
        return applicationInstance.isNetworkConnected();
    }

    //ToDo : Update this deprecated method
    private boolean isNetworkConnected(){
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
