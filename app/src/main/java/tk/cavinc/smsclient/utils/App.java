package tk.cavinc.smsclient.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by cav on 24.07.20.
 */

public class App extends Application {
    private static Context sContext;
    private static SharedPreferences sSharedPreferences;


    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sSharedPreferences =  PreferenceManager.getDefaultSharedPreferences(sContext);
    }

    public static Context getContext() {
        return sContext;
    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }
}
