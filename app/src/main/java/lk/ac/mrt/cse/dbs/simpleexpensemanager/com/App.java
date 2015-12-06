package lk.ac.mrt.cse.dbs.simpleexpensemanager.com;

import android.app.Application;
import android.content.Context;

/**
 * Created by Uer on 12/6/2015.
 */
public class App extends Application {
    public static Context context;

    @Override public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getAppContext(){
        return context;
    }
}
