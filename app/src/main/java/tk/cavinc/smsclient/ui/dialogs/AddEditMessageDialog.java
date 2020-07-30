package tk.cavinc.smsclient.ui.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import tk.cavinc.smsclient.R;

/**
 * Created by cav on 25.07.20.
 */

public class AddEditMessageDialog extends DialogFragment {

    private static final String MSG = "MSG";
    private AddEditMessageDialogListener mListener;
    private EditText mMsgTxt;

    private String msgTxt;

    public static AddEditMessageDialog newInstance(String msg){
        Bundle args = new Bundle();
        args.putString(MSG,msg);

        AddEditMessageDialog dialog = new AddEditMessageDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            msgTxt = getArguments().getString(MSG);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.addeditmessage_dialog,null);

        mMsgTxt = v.findViewById(R.id.msg_txt_item);
        mMsgTxt.setText(msgTxt);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Сообщение");
        builder.setView(v);
        builder.setNegativeButton(R.string.dialog_close,null)
                .setPositiveButton(R.string.dialog_store,mOnClickListener);
        return builder.create();
    }

    DialogInterface.OnClickListener mOnClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if (mListener != null){
                mListener.onChange(mMsgTxt.getText().toString());
            }
        }
    };

    public void setListener(AddEditMessageDialogListener listener) {
        mListener = listener;
    }

    public interface AddEditMessageDialogListener {
        void onChange(String msg);
    }
}
