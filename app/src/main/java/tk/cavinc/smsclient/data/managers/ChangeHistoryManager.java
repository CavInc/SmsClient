package tk.cavinc.smsclient.data.managers;

import java.util.Observable;

/**
 * Created by cav on 04.08.20.
 */

public class ChangeHistoryManager extends Observable {
    private String mChange;

    public String getChange() {
        return mChange;
    }

    public void setChange(String change) {
        mChange = change;
        setChanged();
        notifyObservers();
    }
}
