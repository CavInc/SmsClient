package tk.cavinc.smsclient.data.managers;

import android.content.SharedPreferences;

import tk.cavinc.smsclient.utils.App;

/**
 * Created by cav on 25.07.20.
 */

public class PrefManager {
    private static final String LAST_SEND_MSG = "LAST_SEND_MSG";
    private static final String COUNT_PHONE = "COUNT_PHONE";
    private static final String COUNT_QUERY = "COUNT_QUERY";
    private static final String PERIOD_FROM = "PF";
    private static final String PERIOD_TO = "PT";
    private static final String LAST_SEND_PHONE = "LAST_SEND_PHONE";
    private static final String SERVER_PAUSE = "SERVER_PAUSE";
    private SharedPreferences mSharedPreferences;

    public PrefManager(){
        mSharedPreferences = App.getSharedPreferences();
    }

    // статус сервиса
    public boolean getServicePause(){
        return mSharedPreferences.getBoolean(SERVER_PAUSE,false);
    }

    public void setServicePause(boolean status){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putBoolean(SERVER_PAUSE,status);
        editor.apply();
    }

    public boolean getMsgRnd(){
        return mSharedPreferences.getBoolean("msg_rand",false);
    }

    public boolean getPhoneRnd(){
        return mSharedPreferences.getBoolean("phone_rand",false);
    }

    // последняя оправленная запись
    public int getLastSendMessage(){
        return mSharedPreferences.getInt(LAST_SEND_MSG,1);
    }

    public void setLastSendMsg(int id){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(LAST_SEND_MSG,id);
        editor.apply();
    }

    // количество номеров телефона
    public int getCountPhone(){
        return mSharedPreferences.getInt(COUNT_PHONE,0);
    }

    public void setCountPhone(int countPhone){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(COUNT_PHONE,countPhone);
        editor.apply();
    }

    // длинная очереди
    public int getCountQuery(){
        return mSharedPreferences.getInt(COUNT_QUERY,0);
    }

    public void setCountQuery(int countQuery) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(COUNT_QUERY,countQuery);
        editor.apply();
    }

    // периоды
    public int getPeriodFrom(){
        return mSharedPreferences.getInt(PERIOD_FROM,5);
    }

    public void setPeriodFrom(int periodFrom) {
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(PERIOD_FROM,periodFrom);
        editor.apply();
    }

    public int getPeriodTo(){
        return mSharedPreferences.getInt(PERIOD_TO,30);
    }

    public void setPeriodTo(int periodTo){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(PERIOD_TO,periodTo);
        editor.apply();
    }

    // последний использованный индекс телефона
    public int getLastPhone(){
        return mSharedPreferences.getInt(LAST_SEND_PHONE,0);
    }

    public void setLastPhone(int id){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(LAST_SEND_PHONE,id);
        editor.apply();
    }

}
