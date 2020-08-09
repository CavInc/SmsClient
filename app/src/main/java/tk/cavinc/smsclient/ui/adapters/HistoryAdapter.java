package tk.cavinc.smsclient.ui.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cursoradapter.widget.CursorAdapter;
import tk.cavinc.smsclient.R;

/**
 * Created by cav on 25.07.20.
 */

//http://www.nerdgrl.org/ru/programming/contentprovider-cursorloader-3/
//https://startandroid.ru/ru/uroki/vse-uroki-spiskom/278-urok-136-cursorloader.html
// http://developer.alexanderklimov.ru/android/theory/loader.php


public class HistoryAdapter extends CursorAdapter {
    private LayoutInflater mInflater; //нужен для создания объектов класса View

    public HistoryAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View root = mInflater.inflate(R.layout.history_item, parent, false);
        ViewHolder holder = new ViewHolder();

        holder.mHistoryDate = root.findViewById(R.id.history_data);
        holder.mHistoryPhone = root.findViewById(R.id.history_phone);
        holder.mHistoryMsg = root.findViewById(R.id.history_msg);
        holder.mHistoryStatus = root.findViewById(R.id.history_status);

        root.setTag(holder);
        return root;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        long id = cursor.getLong(cursor.getColumnIndex("_id"));

        String phone = cursor.getString(cursor.getColumnIndex("phone"));
        String msg = cursor.getString(cursor.getColumnIndex("msg"));
        String data = cursor.getString(cursor.getColumnIndex("send_date"));

        ViewHolder holder = (ViewHolder) view.getTag();
        if(holder != null) {
            holder.mHistoryPhone.setText(phone);
            holder.mHistoryMsg.setText(msg);
            holder.mHistoryDate.setText(data);
            holder.classID = id;
        }

    }

    private class ViewHolder {
        public long classID;
        public TextView mHistoryDate;
        public TextView mHistoryPhone;
        public TextView mHistoryMsg;
        public TextView mHistoryStatus;
    }
}
