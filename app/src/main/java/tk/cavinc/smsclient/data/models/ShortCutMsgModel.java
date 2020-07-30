package tk.cavinc.smsclient.data.models;

/**
 * Created by cav on 25.07.20.
 */

public class ShortCutMsgModel {
    private int mId;
    private String mMsg;

    public ShortCutMsgModel(int id, String msg) {
        mId = id;
        mMsg = msg;
    }

    public int getId() {
        return mId;
    }

    public String getMsg() {
        return mMsg;
    }
}
