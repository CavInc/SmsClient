package tk.cavinc.smsclient.ui.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import tk.cavinc.smsclient.R;
import tk.cavinc.smsclient.data.models.SmsMessageModel;

/**
 * Created by cav on 25.07.20.
 */

public class MessageAdapter extends ArrayAdapter<SmsMessageModel> {
    private LayoutInflater mInflater;
    private int resLayout;

    public MessageAdapter(@NonNull Context context, int resource, @NonNull List<SmsMessageModel> objects) {
        super(context, resource, objects);
        resLayout = resource;
        mInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        View row = convertView;
        if (row == null) {
            row = mInflater.inflate(resLayout, parent, false);
            holder = new ViewHolder();

            holder.mId = row.findViewById(R.id.msg_id_item);
            holder.mMsg = row.findViewById(R.id.msg_txt_item);

            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }

        SmsMessageModel record = getItem(position);

        holder.mId.setText("Сообщение №: "+record.getId());
        holder.mMsg.setText(record.getMsg());

        return row;
    }

    public void setData(ArrayList<SmsMessageModel> data) {
        this.clear();
        this.addAll(data);
        notifyDataSetChanged();
    }

    public void updateItem(int position,SmsMessageModel record){
        this.insert(record,position);
        notifyDataSetChanged();
    }

    private class ViewHolder {
        private TextView mId;
        private TextView mMsg;
    }
}
