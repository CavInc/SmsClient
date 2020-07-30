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
import tk.cavinc.smsclient.data.managers.DataManager;

/**
 * Created by cav on 29.07.20.
 */

public class SetDiapasoneDialog extends DialogFragment {
    private DataManager mDataManager;

    private EditText mPeriodFrom;
    private EditText mPeriodTo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = LayoutInflater.from(getActivity()).inflate(R.layout.diapasone_dialog,null);

        mPeriodFrom = v.findViewById(R.id.period_from);
        mPeriodTo = v.findViewById(R.id.period_to);

        mPeriodFrom.setText(String.valueOf(mDataManager.getPrefManager().getPeriodFrom()));
        mPeriodTo.setText(String.valueOf(mDataManager.getPrefManager().getPeriodTo()));

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setView(v)
                .setTitle("Диапазон периода задержки")
                .setNegativeButton(R.string.dialog_close,null)
                .setPositiveButton(R.string.dialog_ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mDataManager.getPrefManager().setPeriodFrom(Integer.parseInt(mPeriodFrom.getText().toString()));
                        mDataManager.getPrefManager().setPeriodTo(Integer.parseInt(mPeriodTo.getText().toString()));
                    }
                });

        return builder.create();
    }

}
