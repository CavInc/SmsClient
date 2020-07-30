package tk.cavinc.smsclient.data.models;

/**
 * Created by cav on 26.07.20.
 */

public class PhoneListModel {
    private int mId;
    private String mPhone;

    public PhoneListModel(int id, String phone) {
        mId = id;
        mPhone = phone;
    }

    public int getId() {
        return mId;
    }

    public String getPhone() {
        return mPhone;
    }
}
