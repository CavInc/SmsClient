package tk.cavinc.smsclient.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.ArrayAdapter;

import java.util.ArrayList;
import java.util.Date;

import tk.cavinc.smsclient.data.models.PhoneListModel;
import tk.cavinc.smsclient.data.models.ShortCutMsgModel;
import tk.cavinc.smsclient.data.models.SmsMessageModel;
import tk.cavinc.smsclient.utils.Utils;

/**
 * Created by cav on 25.07.20.
 */

public class DBConnect {
    private SQLiteDatabase database;
    private Context mContext;
    private DBHelper mDBHelper;


    public DBConnect(Context context){
        mContext = context;
        mDBHelper = new DBHelper(context,DBHelper.DATABASE_NAME,null,DBHelper.DATABASE_VERSION);
    }

    public void open(){
        database = mDBHelper.getWritableDatabase();
    }
    public  void close(){
        if (database != null) {
            database.close();
        }
    }

    // получаем список сообщений
    public ArrayList<SmsMessageModel> getMessages(){
        ArrayList<SmsMessageModel> rec = new ArrayList<>();
        open();
        Cursor cursor = database.query(DBHelper.MSG,
                new String[]{"id","msg"},null,null,null,null,"id");
        while (cursor.moveToNext()){
            rec.add(new SmsMessageModel(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("msg"))));
        }
        close();
        return rec;
    }

    // добавляем текст сообщения
    public void addMessage(String msg){
        open();
        ContentValues values = new ContentValues();
        values.put("msg",msg);
        database.insert(DBHelper.MSG,null,values);
        close();
    }

    // обновить текст сообщения
    public void updateMessage(SmsMessageModel data){
        open();
        ContentValues values = new ContentValues();
        values.put("msg",data.getMsg());
        database.update(DBHelper.MSG,values,"id="+data.getId(),null);
        close();
    }

    // получаем сообщение по номеру
    public SmsMessageModel getMessagesId(int id){
        SmsMessageModel data = null;
        open();
        Cursor cursor = database.query(DBHelper.MSG,new String[]{"id","msg"},
                "id=?",new String[]{String.valueOf(id)},null,null,null);
        while (cursor.moveToNext()){
            data = new SmsMessageModel(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("msg"))
            );
        }
        close();
        return data;
    }

    // получаем количество сообщений
    public int getCountMsg(){
        int count = 0;
        String sql = "select count(id) from "+DBHelper.MSG;
        open();
        Cursor cursor = database.rawQuery(sql,null);
        while (cursor.moveToNext()){
            count = cursor.getInt(0);
        }
        close();
        return count;
    }

    // получаем список шоркатов
    public ArrayList<ShortCutMsgModel> getShortCut(){
        ArrayList<ShortCutMsgModel> rec = new ArrayList<>();
        open();
        Cursor cursor = database.query(DBHelper.SHORTCUT_MSG,
                new String[]{"id","msg"},null,null,null,null,"id");
        while (cursor.moveToNext()){
            rec.add(new ShortCutMsgModel(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("msg"))
            ));
        }
        close();
        return rec;
    }

    // добавляем текст шортката
    public void addShortCut(String msg){
        open();
        ContentValues values = new ContentValues();
        values.put("msg",msg);
        database.insert(DBHelper.SHORTCUT_MSG,null,values);
        close();
    }

    // получаем шрткат по номеру
    public SmsMessageModel getShortCupId(int id){
        SmsMessageModel data = null;
        open();
        Cursor cursor = database.query(DBHelper.SHORTCUT_MSG,new String[]{"id","msg"},
                "id=?",new String[]{String.valueOf(id)},null,null,null);
        while (cursor.moveToNext()){
            data = new SmsMessageModel(
                    cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("msg"))
            );
        }
        close();
        return data;
    }

    // получаем номера телефонов
    public ArrayList<PhoneListModel> getPhone(){
        ArrayList<PhoneListModel> rec = new ArrayList<>();
        open();
        Cursor cursor = database.query(DBHelper.SEND_PHONE,
                new String[]{"id","phone"},null,null,null,
                null,"id");
        while (cursor.moveToNext()){
            rec.add(new PhoneListModel(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("phone"))));
        }
        close();
        return rec;
    }

    // очистить номера телефонов
    public void deletePhoneAll(){
        open();
        database.delete(DBHelper.SEND_PHONE,null,null);
        close();
    }

    // записать номер телефона (мульт)
    public void addPhone(String phone){
        ContentValues values = new ContentValues();
        values.put("phone",phone);
        database.insert(DBHelper.SEND_PHONE,null,values);
    }


    // получить количество номеров
    public int getCountPhone() {
        int res = 0;
        String sql = "select count(id) as ci from "+DBHelper.SEND_PHONE;
        open();
        Cursor cursor = database.rawQuery(sql,null,null);
        while (cursor.moveToNext()){
            res = cursor.getInt(0);
        }
        close();
        return res;
    }

    public String getPhoneId(int id) {
        String phone = null;
        open();
        Cursor cursor = database.query(DBHelper.SEND_PHONE,new String[]{"phone"},"id="+id,null,null,null,null);
        while (cursor.moveToNext()){
            phone = cursor.getString(0);
        }
        close();
        return phone;
    }

    // записать данные о отправки сообщения
    public void addHistory(String phone,String msg){
        open();
        ContentValues values = new ContentValues();
        values.put("phone",phone);
        values.put("msg",msg);
        values.put("send_date", Utils.dateToStr("yyyy-MM-dd HH:mm:ss",new Date()));
        long id = database.insert(DBHelper.HISTORY_SEND,null,values);
        close();
    }

    // получить данные истории
    public Cursor getHistory(){
        Cursor cursor = database.query(DBHelper.HISTORY_SEND,
                new String[]{"_id","send_date","phone","msg","status"},null,null,null,null,"send_date desc");
        return cursor;
    }

    // удаляем историю
    public void deleteHistory(){
        open();
        database.delete(DBHelper.HISTORY_SEND,null,null);
        close();
    }


    // Добавить запись в очередь
    public void addQuery(int idPhone){
        open();
        ContentValues values = new ContentValues();
        values.put("id",idPhone);
        database.insert(DBHelper.QUERY_PHONE,null,values);
        close();
    }

    // найти запись в очереди по id
    public int getIDQuery(int idPhone){
        int rec = -1;
        open();
        Cursor cursor = database.query(DBHelper.QUERY_PHONE,
                new String[]{"id"},"id="+idPhone,null,null,null,null);
        while (cursor.moveToNext()){
            rec = cursor.getInt(0);
        }
        close();
        return rec;
    }

    // очистить все записи в очереди
    public void deleteAllQuery(){
        open();
        database.delete(DBHelper.QUERY_PHONE,null,null);
        close();
    }


}
