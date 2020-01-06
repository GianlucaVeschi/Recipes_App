package www.gianlucaveschi.mijirecipesapp.executors;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * THERE IS NO BENEFIT IN CALLING EXECUTE ON THE CALL OBJECT INSTEAD OF USING CALL.ENQUEUE.
 * IT JUST GIVES MORE CONTROL OVER THE LOGIC
 * */
public class AppExecutors {

    private static AppExecutors instance;

    //Singleton Constructor
    public static AppExecutors get(){
        if(instance == null){
            instance = new AppExecutors();
        }
        return instance;
    }

    private final ScheduledExecutorService mNetworkIO = Executors.newScheduledThreadPool(3);

    public ScheduledExecutorService networkIO() {
        return mNetworkIO;
    }
}