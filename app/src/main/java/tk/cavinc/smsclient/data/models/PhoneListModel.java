package tk.cavinc.smsclient.data.models;

/**
 * Created by cav on 26.07.20.
 */

public class PhoneListModel {
    private int mId;
    private String mPhone;
    private boolean mStatusSend;

    public PhoneListModel(int id, String phone) {
        mId = id;
        mPhone = phone;
        mStatusSend = false;
    }

    public PhoneListModel(int id, String phone, boolean statusSend) {
        mId = id;
        mPhone = phone;
        mStatusSend = statusSend;
    }

    public int getId() {
        return mId;
    }

    public String getPhone() {
        return mPhone;
    }

    public boolean isStatusSend() {
        return mStatusSend;
    }
}
