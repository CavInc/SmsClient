package tk.cavinc.smsclient.data.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by cav on 25.07.20.
 */

public class DBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1 ;
    public static String DATABASE_NAME = "smsclient.db3";

    public static String SHORTCUT_MSG = "shortcut_msg";
    public static String MSG = "msg";
    public static String SEND_PHONE = "send_phone";
    public static String HISTORY_SEND = "history_send";
    public static String QUERY_PHONE = "query_phone";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        updatedDB(db,0,DATABASE_VERSION);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updatedDB(db,oldVersion,newVersion);
    }

    private void updatedDB(SQLiteDatabase db,int oldVersion,int newVersion){
        db.execSQL("create table if not exists "+MSG+"(" +
                "id integer PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "msg text)");

        db.execSQL("create table if not exists "+SHORTCUT_MSG+"(" +
                "id integer PRIMARY KEY NOT NULL," +
                "msg text)");

        db.execSQL("create table if not exists "+SEND_PHONE+" (" +
                "id integer PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "phone text)");

        db.execSQL("create table if not exists "+HISTORY_SEND+"(" +
                "_id integer PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "send_date text," +
                "phone text," +
                "msg text," +
                "status integer)");

        db.execSQL("create table if not exists "+QUERY_PHONE+"(" +
                "id integer primary key not null)");

    }
}
