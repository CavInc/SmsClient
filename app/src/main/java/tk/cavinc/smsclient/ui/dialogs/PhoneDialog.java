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
 * Created by cav on 31.07.20.
 */

public class PhoneDialog extends DialogFragment {

    private static final String PHONE = "PHONE";
    private PhoneDialogListener mPhoneDialogListener;
    private EditText mPhone;
    private String phone;

    public static PhoneDialog newInstance(String phone){
        Bundle args = new Bundle();
        args.putString(PHONE,phone);
        PhoneDialog dialog = new PhoneDialog();
        dialog.setArguments(args);
        return dialog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null){
            phone = getArguments().getString(PHONE);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.phone_dialog, null);

        mPhone = v.findViewById(R.id.phone_num);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Телефон")
                .setView(v)
                .setNegativeButton(R.string.dialog_close,null)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mPhoneDialogListener.onChangePhone(mPhone.getText().toString());
                    }
                });

        return builder.create();
    }

    public void setPhoneDialogListener(PhoneDialogListener listener){
        mPhoneDialogListener = listener;
    }

    public interface PhoneDialogListener {
        void onChangePhone(String phone);
    }
}
