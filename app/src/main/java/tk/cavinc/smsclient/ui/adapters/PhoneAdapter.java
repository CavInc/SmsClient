package tk.cavinc.smsclient.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import tk.cavinc.smsclient.R;
import tk.cavinc.smsclient.data.models.PhoneListModel;

/**
 * Created by cav on 27.07.20.
 */

public class PhoneAdapter extends ArrayAdapter<PhoneListModel> {

    private LayoutInflater mInflater;
    private int resLayout;

    public PhoneAdapter(@NonNull Context context, int resource, @NonNull List<PhoneListModel> objects) {
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

            holder.mStatus = row.findViewById(R.id.phone_num_status);
            holder.mPhone = row.findViewById(R.id.phone_num_item);

            row.setTag(holder);
        } else {
            holder = (ViewHolder)row.getTag();
        }

        PhoneListModel record = getItem(position);

        holder.mPhone.setText(record.getPhone());
        if (record.isStatusSend()){
            holder.mStatus.setImageResource(R.drawable.ic_textsms_green_24dp);
        } else {
            holder.mStatus.setImageResource(R.drawable.ic_textsms_red_24dp);
        }

        return row;
    }

    public void setData(ArrayList<PhoneListModel> data){
        this.clear();
        this.addAll(data);
    }

    private class ViewHolder{
        public ImageView mStatus;
        public TextView mPhone;
    }
}
