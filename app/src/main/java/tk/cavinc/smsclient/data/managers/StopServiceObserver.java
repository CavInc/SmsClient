package tk.cavinc.smsclient.data.managers;

import java.util.Observable;

/**
 * Created by cav on 05.08.20.
 */

public class StopServiceObserver extends Observable {
    private boolean mStopService = true;

    public boolean isStopService() {
        return mStopService;
    }

    public void setStopService(boolean stopService) {
        mStopService = stopService;
        setChanged();
        notifyObservers();
    }
}
