package tk.cavinc.smsclient.data.managers;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.IBinder;

import android.telephony.SmsManager;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.util.Log;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.RequiresApi;
import tk.cavinc.smsclient.data.database.DBConnect;
import tk.cavinc.smsclient.data.models.SimDataModel;
import tk.cavinc.smsclient.utils.App;

/**
 * Created by cav on 25.07.20.
 */

public class DataManager {
    private static DataManager INSTANCE = null;

    private Context mContext;
    private PrefManager mPrefManager;
    private DBConnect mDB;
    private ArrayList<SimDataModel> mSimDataModel;

    public static DataManager getInstance() {
        if (INSTANCE==null){
            INSTANCE = new DataManager();
        }
        return INSTANCE;
    }

    public DataManager() {
        mContext = App.getContext();
        mPrefManager = new PrefManager();
        mDB = new DBConnect(mContext);
        mSimDataModel = new ArrayList<>();
    }

    public Context getContext() {
        return mContext;
    }

    public PrefManager getPrefManager() {
        return mPrefManager;
    }

    public DBConnect getDB() {
        return mDB;
    }

    public ArrayList<SimDataModel> getSimDataModel() {
        return mSimDataModel;
    }

    // возвращает путь к локальной папки приложения
    public String getStorageAppPath(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return null;
        File path = new File (Environment.getExternalStorageDirectory(), "SMSClient");
        if (! path.exists()) {
            if (!path.mkdirs()){
                return null;
            }
        }
        return path.getPath();
    }

    // локальные файлы приложения на SD
    public String getPrivateFilesApp(){
        if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
            return null;
        File path = new File(mContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS),"SMSClient");
        if (! path.exists()) {
            if (!path.mkdirs()){
                return null;
            }
        }
        return path.getPath();
    }

    // возвращаем путь в хранилищу приложения
    public String getAppStorage(){
       File path = mContext.getFilesDir();
       return path.getPath();
    }


    /* Checks if external storage is available for read and write */
    public boolean isExternalStorageWritable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {
            return true;
        }
        return false;
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    // получили дефолтную симку
    // https://stackoverrun.com/ru/q/11409212
    public int  getDefaultSimmm(Context context) {

        Object tm = context.getSystemService(Context.TELEPHONY_SERVICE);
        Method method_getDefaultSim;
        int defaultSimm = -1;
        try {
            method_getDefaultSim = tm.getClass().getDeclaredMethod("getDefaultSim");
            method_getDefaultSim.setAccessible(true);
            defaultSimm = (Integer) method_getDefaultSim.invoke(tm);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        if (defaultSimm != -1) return defaultSimm;

        Method method_getSmsDefaultSim;
        int smsDefaultSim = -1;
        try {
            method_getSmsDefaultSim = tm.getClass().getDeclaredMethod("getSmsDefaultSim");
            smsDefaultSim = (Integer) method_getSmsDefaultSim.invoke(tm);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return smsDefaultSim;
    }

    public boolean sendSMS(Context ctx, int simID, String toNum, String centerNum, String smsText, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        String name;

        try {
            if (simID == 0) {
                name = "isms";
                // for model : "Philips T939" name = "isms0"
            } else if (simID == 1) {
                name = "isms2";
            } else {
                throw new Exception("can not get service which for sim '" + simID + "', only 0,1 accepted as values");
            }
            Method method = Class.forName("android.os.ServiceManager").getDeclaredMethod("getService", String.class);
            method.setAccessible(true);
            Object param = method.invoke(null, name);

            method = Class.forName("com.android.internal.telephony.ISms$Stub").getDeclaredMethod("asInterface", IBinder.class);
            method.setAccessible(true);
            Object stubObj = method.invoke(null, param);
            if (Build.VERSION.SDK_INT < 18) {
                method = stubObj.getClass().getMethod("sendText", String.class, String.class, String.class, PendingIntent.class, PendingIntent.class);
                method.invoke(stubObj, toNum, centerNum, smsText, sentIntent, deliveryIntent);
            } else {
                method = stubObj.getClass().getMethod("sendText", String.class, String.class, String.class, String.class, PendingIntent.class, PendingIntent.class);
                method.invoke(stubObj, ctx.getPackageName(), toNum, centerNum, smsText, sentIntent, deliveryIntent);
            }

            return true;
        } catch (ClassNotFoundException e) {
            Log.e("apipas", "ClassNotFoundException:" + e.getMessage());
        } catch (NoSuchMethodException e) {
            Log.e("apipas", "NoSuchMethodException:" + e.getMessage());
        } catch (InvocationTargetException e) {
            Log.e("apipas", "InvocationTargetException:" + e.getMessage());
        } catch (IllegalAccessException e) {
            Log.e("apipas", "IllegalAccessException:" + e.getMessage());
        } catch (Exception e) {
            Log.e("apipas", "Exception:" + e.getMessage());
        }
        return false;
    }

    //https://coderoad.ru/23990001/%D0%9A%D0%B0%D0%BA-%D0%BE%D1%82%D0%BF%D1%80%D0%B0%D0%B2%D0%B8%D1%82%D1%8C-sms-%D0%B2-android-%D0%B2-%D0%B4%D0%B2%D0%BE%D0%B9%D0%BD%D0%BE%D0%B9-SIM-%D0%BA%D0%B0%D1%80%D1%82%D0%B5
    @SuppressLint("MissingPermission")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public  boolean sendSMS2(Context context, int simID, String toNum, String centerNum,
                             String smsText, PendingIntent sentIntent, PendingIntent deliveryIntent) {
        SmsManager smsMan = SmsManager.getDefault();
        int[] param;
        Method method = null;
        try {
            SubscriptionManager localSubscriptionManager = SubscriptionManager.from(context);
            if (localSubscriptionManager.getActiveSubscriptionInfoCount() > 1) {
                List localList = localSubscriptionManager.getActiveSubscriptionInfoList();
                SubscriptionInfo simInfo = (SubscriptionInfo) localList.get(simID);
                SmsManager.getSmsManagerForSubscriptionId(simInfo.getSubscriptionId())
                        .sendTextMessage(toNum, null, smsText, null, null);
            }
            return true;
        } catch (Exception e) {
            Log.e("SmsSimUtil.sendSMS", "Exception:" + e.getMessage());
        }
        return false;
    }
}
