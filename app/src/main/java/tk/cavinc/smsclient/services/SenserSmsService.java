package tk.cavinc.smsclient.services;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.util.TimeUtils;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import tk.cavinc.smsclient.data.managers.DataManager;
import tk.cavinc.smsclient.data.models.PhoneListModel;
import tk.cavinc.smsclient.data.models.ShortCutMsgModel;
import tk.cavinc.smsclient.data.models.SmsMessageModel;
import tk.cavinc.smsclient.ui.MainActivity;
import tk.cavinc.smsclient.utils.App;
import tk.cavinc.smsclient.utils.ParseAndConstructorSms;
import tk.cavinc.smsclient.utils.Utils;

import static android.content.ContentValues.TAG;

/**
 * Created by cav on 25.07.20.
 */

public class SenserSmsService extends Service {
    private static final int DEFAULT_NOTIFICATION_ID = 453;
    private static final String CHANEL_ID = "tk.cavinc.SMSCLIENT";
    private final SmsManager smsManager;
    private DataManager mDataManager;
    private boolean runing = true;

    private ArrayList<SmsMessageModel> smsMessage;
    private ArrayList<ShortCutMsgModel> shortCutMsg;

    public SenserSmsService(){
        mDataManager = DataManager.getInstance();
        smsManager = SmsManager.getDefault();
        smsMessage = mDataManager.getDB().getMessages();
        int maxIDPhone = mDataManager.getDB().getPhoneMaxID();
        mDataManager.getPrefManager().setCountPhone(maxIDPhone);
        //shortCutMsg = mDataManager.getDB().getShortCut();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        sendNotification("SMSClient","SmsClient","Отправка сообщений");
        work();
        return START_STICKY;
    }

    private void work(){
        Log.d(TAG,"START SERICE");

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (runing) {
                    int minDelay = mDataManager.getPrefManager().getPeriodFrom();
                    int maxDelay = mDataManager.getPrefManager().getPeriodTo();

                    int delay = Utils.getRandItem(minDelay,maxDelay);

                    Log.d(TAG,"DELAY : " + delay);

                    try {
                        TimeUnit.SECONDS.sleep(delay);//было 30
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    if (!runing) break;

                    int count = mDataManager.getDB().getCountMsg();
                    if (count > smsMessage.size()) {
                        smsMessage = mDataManager.getDB().getMessages();
                    }
                    int idMsg = 0;

                    SmsMessageModel msg = null;
                    do {
                        if (!mDataManager.getPrefManager().getMsgRnd()) {
                            idMsg = Utils.getRandItem(count);
                            if (idMsg == 0) idMsg = 1;
                        } else {
                            idMsg = mDataManager.getPrefManager().getLastSendMessage();
                            idMsg += 1;
                            if (idMsg > count) {
                                idMsg = 1;
                            }
                            mDataManager.getPrefManager().setLastSendMsg(idMsg);
                        }

                        Log.d(TAG, "ID " + idMsg);

                        //msg = mDataManager.getDB().getMessagesId(idMsg);
                        msg = smsMessage.get(idMsg-1);
                    } while (msg == null);

                    //Log.d(TAG, msg.getMsg());
                    String msgIn = ParseAndConstructorSms.parse(msg.getMsg(),mDataManager);
                    //Log.d(TAG,msgIn);


                    String phone = "5556";//+15555215556

                    phone = getPhone3();

                    //String phone = getPhone2();
                    Log.d(TAG,"PHONE :"+phone);



                    if (phone != null) {
                        try {
                            smsManager.sendTextMessage(phone, null, msgIn, null, null);
                        } catch (Exception e){
                            Toast.makeText(mDataManager.getContext(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                        }
                        mDataManager.getDB().addHistory(phone,msgIn);
                        App.getChangeHistoryManager().setChange(msgIn);
                    }


                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG,"STOP SERVICE");
        runing = false;
        stopSelf();
    }

    public void sendNotification(String Ticker,String Title,String Text) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setAction(Intent.ACTION_MAIN);
        notificationIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent contentIntent = PendingIntent.getActivity(getApplicationContext(),
                0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        NotificationManager notificationManager = (NotificationManager) mDataManager.getContext()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        //для А8+
        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel(CHANEL_ID,"Reminder", NotificationManager.IMPORTANCE_HIGH);
            channel.setDescription("Reminder");
            channel.enableLights(true);
            channel.enableVibration(true);
            notificationManager.createNotificationChannel(channel);
        }


        Notification.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            builder = new Notification.Builder(this,CHANEL_ID);
        } else {
            builder = new Notification.Builder(this);
        }

        //NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(contentIntent)
                .setOngoing(true)   //Can't be swiped out
                //.setSmallIcon(R.drawable.ic_access_point_network)
                //.setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.large))   // большая картинка
                .setTicker(Ticker)
                .setContentTitle(Title) //Заголовок
                .setContentText(Text) // Текст уведомления
                .setWhen(System.currentTimeMillis());

        Notification notification;
        if (Build.VERSION.SDK_INT<=15) {
            notification = builder.getNotification(); // API-15 and lower
        }else{
            notification = builder.build();
        }
        startForeground(DEFAULT_NOTIFICATION_ID, notification);
    }

    private int getFreeIDQuery(int searchId,int countPhone){
        int cre = 0;
        while (mDataManager.getDB().getIDQuery(searchId) != -1){
            Log.d(TAG,"QUERY PHONE ID : "+searchId);
            searchId = Utils.getRandItem(countPhone);
            cre += 1;
            if (cre > countPhone) break;
        }
        return searchId;
    }


    // получаем телефон
    private String getPhone3(){
        String phone = null;

        int coutPhone = mDataManager.getPrefManager().getCountPhone(); // количество телефонов
        if (coutPhone == 0) return phone;

        int id = 0;

        boolean checkPhone = true;
        do {
            if (!mDataManager.getPrefManager().getPhoneRnd()) {
                id = Utils.getRandItem(coutPhone);
            } else {
                id = mDataManager.getPrefManager().getLastPhone();
                id += 1;
                if (id > coutPhone) {
                    id = 0;
                    mDataManager.getPrefManager().setLastPhone(id);
                    runing = false;
                    stopSelf();
                }
                mDataManager.getPrefManager().setLastPhone(id);
            }
            PhoneListModel record = mDataManager.getDB().getPhoneObject(id);
            if (!record.isStatusSend()) {
                checkPhone = false;
                phone = record.getPhone();
                //TODO установка флага после отправки
                mDataManager.getDB().updatePhoneStatus(record.getId(),true);
                int query = mDataManager.getPrefManager().getCountQuery();
                query += 1;
                if (query>coutPhone) {
                    // останавливаемся
                    runing = false;
                    stopSelf();
                }
                mDataManager.getPrefManager().setCountQuery(query);
            }
        } while (checkPhone);


        return phone;
    }

    // получаем телефон
    private String getPhone2(){
        String phone = null;
        int coutPhone = mDataManager.getPrefManager().getCountPhone(); // количество телефонов
        if (coutPhone == 0) return phone;

        int id = Utils.getRandItem(coutPhone);
        do {
            id = getFreeIDQuery(id, coutPhone); // получили свободный номер в очереди
            phone = mDataManager.getDB().getPhoneId(id);

            mDataManager.getDB().addQuery(id);
            mDataManager.getPrefManager().setCountQuery(mDataManager.getPrefManager().getCountQuery() + 1);
            Log.d(TAG,"QUERY COUNT: "+mDataManager.getPrefManager().getCountQuery());

            if (mDataManager.getPrefManager().getCountQuery() >= coutPhone) {
                // сбрасываем счетчики
                mDataManager.getDB().deleteAllQuery();
                mDataManager.getPrefManager().setCountQuery(0);
                Log.d(TAG,"---------------------------");
                runing = false; // останавливаем отправку
                stopSelf();
            }

        }while (phone == null);

        // нашли телефон
        return phone;
    }

    // получаем телефон
    private String getPhone(){
        String phone = null;
        int coutPhone = mDataManager.getPrefManager().getCountPhone();
        Log.d(TAG,"COUNT PHONE: "+coutPhone);
        Log.d(TAG,"QUERY COUNT: "+mDataManager.getPrefManager().getCountQuery());

        int id = Utils.getRandItem(coutPhone);

        int cre = 0;
        // крутим цикл по очереди пока не найдем пустой номер
        while (mDataManager.getDB().getIDQuery(id) != -1) {
            Log.d(TAG,"GET PHONE ID "+id);
            id = Utils.getRandItem(coutPhone);
            cre += 1;
            if (coutPhone <= cre) break;
        }

        Log.d(TAG,"YES ID: "+id);
        if (mDataManager.getDB().getIDQuery(id) == -1) {
            phone = mDataManager.getDB().getPhoneId(id);
            if (phone != null) {
                Log.d(TAG, phone);
            }
            mDataManager.getDB().addQuery(id);
            mDataManager.getPrefManager().setCountQuery(mDataManager.getPrefManager().getCountQuery() + 1);
            Log.d(TAG,"QUERY COUNT: "+mDataManager.getPrefManager().getCountQuery());
            if (phone == null) {
                phone = getPhone();
            }

            if (mDataManager.getPrefManager().getCountQuery() >= coutPhone) {
                // сбрасываем счетчики
                mDataManager.getDB().deleteAllQuery();
                mDataManager.getPrefManager().setCountQuery(0);
                Log.d(TAG,"---------------------------");
            }

            return phone;
        }
        return phone;
    }
}
