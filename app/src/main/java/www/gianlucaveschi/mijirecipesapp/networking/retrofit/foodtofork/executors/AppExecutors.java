package www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.executors;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * THERE IS NO BENEFIT IN CALLING EXECUTE ON THE CALL OBJECT INSTEAD OF USING CALL.ENQUEUE.
 * IT JUST GIVES MORE CONTROL OVER THE LOGIC
 * */
public class AppExecutors {

    private static AppExecutors instance;

    //Singleton Constructor
    public static AppExecutors getInstance(){
        if(instance == null){
            instance = new AppExecutors();
        }
        return instance;
    }

    //Old Implementation
    private final ScheduledExecutorService mNetworkIO = Executors.newScheduledThreadPool(3);

    public ScheduledExecutorService networkIO() {
        return mNetworkIO;
    }

    //Used for DB Operation from the Cache
    private final Executor mDiskIO = Executors.newSingleThreadExecutor();

    //Used to post stuff on the Main Thread
    private final Executor mMainThreadExecutor = new MainThreadExecutor();

    public Executor diskIO(){
        return mDiskIO;
    }

    public Executor mainThread(){
        return mMainThreadExecutor;
    }


}