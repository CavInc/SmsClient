package tk.cavinc.smsclient.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import tk.cavinc.smsclient.data.managers.ChangeHistoryManager;
import tk.cavinc.smsclient.data.managers.StopServiceObserver;

/**
 * Created by cav on 24.07.20.
 */

public class App extends Application {
    private static Context sContext;
    private static SharedPreferences sSharedPreferences;

    private static ChangeHistoryManager mChangeHistoryManager;
    private static StopServiceObserver sStopServiceObserver;


    @Override
    public void onCreate() {
        super.onCreate();
        sContext = getApplicationContext();
        sSharedPreferences =  PreferenceManager.getDefaultSharedPreferences(sContext);
        mChangeHistoryManager = new ChangeHistoryManager();
        sStopServiceObserver = new StopServiceObserver();
    }

    public static Context getContext() {
        return sContext;
    }

    public static SharedPreferences getSharedPreferences() {
        return sSharedPreferences;
    }

    public static ChangeHistoryManager getChangeHistoryManager() {
        return mChangeHistoryManager;
    }

    public static StopServiceObserver getStopServiceObserver() {
        return sStopServiceObserver;
    }
}
