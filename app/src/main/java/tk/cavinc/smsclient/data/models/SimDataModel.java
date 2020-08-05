package tk.cavinc.smsclient.data.models;

/**
 * Created by cav on 05.08.20.
 */

public class SimDataModel {
    private int mSimSlotIndex;
    private String mName;

    public SimDataModel(int simSlotIndex, String name) {
        mSimSlotIndex = simSlotIndex;
        mName = name;
    }

    public int getSimSlotIndex() {
        return mSimSlotIndex;
    }

    public String getName() {
        return mName;
    }
}
