package tk.cavinc.smsclient.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import tk.cavinc.smsclient.R;
import tk.cavinc.smsclient.data.managers.DataManager;
import tk.cavinc.smsclient.data.models.ShortCutMsgModel;
import tk.cavinc.smsclient.ui.adapters.MessageAdapter;
import tk.cavinc.smsclient.ui.adapters.ShortMsgAdapter;
import tk.cavinc.smsclient.ui.dialogs.AddEditMessageDialog;

/**
 * Created by cav on 25.07.20.
 */

public class ShortMsgTxtFragment extends Fragment implements View.OnClickListener,
        AddEditMessageDialog.AddEditMessageDialogListener {
    private DataManager mDataManager;
    private ListView mListView;
    private ShortMsgAdapter mAdapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message_txt, container, false);

        mListView = rootView.findViewById(R.id.msg_lv);

        rootView.findViewById(R.id.fab).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        AddEditMessageDialog dialog = new AddEditMessageDialog();
        dialog.setListener(this);
        dialog.show(getFragmentManager(),"AEM");
    }

    @Override
    public void onChange(String msg) {
        mDataManager.getDB().addShortCut(msg);
        updateUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    public void updateUI(){
        ArrayList<ShortCutMsgModel> data = mDataManager.getDB().getShortCut();
        if (mAdapter == null){
            mAdapter = new ShortMsgAdapter(getActivity(),R.layout.message_item,data);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(data);
        }
    }
}
