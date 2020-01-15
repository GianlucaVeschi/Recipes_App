package www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.executors;

import android.os.Handler;
import android.os.Looper;


import java.util.concurrent.Executor;


import androidx.annotation.NonNull;

//This class posts things to the Main Thread

public class MainThreadExecutor implements Executor{

    private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

    @Override
    public void execute(@NonNull Runnable command) {
        mainThreadHandler.post(command);
    }
}
