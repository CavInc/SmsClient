package tk.cavinc.smsclient.ui.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import tk.cavinc.smsclient.R;
import tk.cavinc.smsclient.data.managers.DataManager;
import tk.cavinc.smsclient.data.models.SmsMessageModel;
import tk.cavinc.smsclient.ui.adapters.MessageAdapter;
import tk.cavinc.smsclient.ui.dialogs.AddEditMessageDialog;

/**
 * Created by cav on 25.07.20.
 */

public class MessageTxtFragment extends Fragment implements View.OnClickListener,
        AddEditMessageDialog.AddEditMessageDialogListener,AdapterView.OnItemLongClickListener {
    private static final int NEW_MSG = 0;
    private static final int EDIT_MSG = 1;
    private DataManager mDataManager;

    private MessageAdapter mAdapter;
    private ListView mListView;
    private int mode;
    private SmsMessageModel selectRecord;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
        ActionBar actionBar = getActivity().getActionBar();
        if (actionBar!= null) {
            actionBar.setTitle("Сообщения");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_message_txt, container, false);

        mListView = rootView.findViewById(R.id.msg_lv);
        mListView.setOnItemLongClickListener(this);

        rootView.findViewById(R.id.fab).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        ArrayList<SmsMessageModel> data = mDataManager.getDB().getMessages();
        if (mAdapter == null) {
            mAdapter = new MessageAdapter(getActivity(),R.layout.message_item,data);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(data);
        }

    }

    @Override
    public void onClick(View v) {
        mode = NEW_MSG;
        AddEditMessageDialog dialog = new AddEditMessageDialog();
        dialog.setListener(this);
        dialog.show(getFragmentManager(),"AEM");
    }

    @Override
    public void onChange(String msg) {
        if (mode == NEW_MSG) {
            mDataManager.getDB().addMessage(msg);
        } else {
            selectRecord.setMsg(msg);
            mDataManager.getDB().updateMessage(selectRecord);
        }
        updateUI();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
        selectRecord = (SmsMessageModel) adapterView.getItemAtPosition(position);

        mode = EDIT_MSG;
        AddEditMessageDialog dialog = AddEditMessageDialog.newInstance(selectRecord.getMsg());
        dialog.setListener(this);
        dialog.show(getFragmentManager(),"UEM");

        return true;
    }
}
