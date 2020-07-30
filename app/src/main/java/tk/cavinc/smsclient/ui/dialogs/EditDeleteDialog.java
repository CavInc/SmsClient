package tk.cavinc.smsclient.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import tk.cavinc.smsclient.R;

/**
 * Created by cav on 30.07.20.
 */

public class EditDeleteDialog extends DialogFragment implements View.OnClickListener {

    private SelectEditDeleteListener mSelectEditDeleteListener;

    public interface SelectEditDeleteListener {
        void selectItem(int id);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.edit_delete_dialog, null);

        LinearLayout mEditLayout = (LinearLayout) v.findViewById(R.id.edit_laout);
        LinearLayout mDelLayout = (LinearLayout) v.findViewById(R.id.del_laout);

        mEditLayout.setOnClickListener(this);
        mDelLayout.setOnClickListener(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Действия над записью")
                .setView(v);
        return builder.create();
    }


    public void setSelectEditDeleteListener(SelectEditDeleteListener selectEditDeleteListener) {
        mSelectEditDeleteListener = selectEditDeleteListener;
    }

    @Override
    public void onClick(View v) {
        if (mSelectEditDeleteListener != null){
            mSelectEditDeleteListener.selectItem(v.getId());
        }
        dismiss();
    }
}
