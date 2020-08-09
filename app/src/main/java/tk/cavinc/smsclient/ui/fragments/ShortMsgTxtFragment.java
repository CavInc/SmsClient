package tk.cavinc.smsclient.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import tk.cavinc.smsclient.R;
import tk.cavinc.smsclient.data.managers.DataManager;
import tk.cavinc.smsclient.data.models.ShortCutMsgModel;
import tk.cavinc.smsclient.ui.adapters.MessageAdapter;
import tk.cavinc.smsclient.ui.adapters.ShortMsgAdapter;
import tk.cavinc.smsclient.ui.dialogs.AddEditMessageDialog;
import tk.cavinc.smsclient.ui.dialogs.EditDeleteDialog;

/**
 * Created by cav on 25.07.20.
 */

public class ShortMsgTxtFragment extends Fragment implements View.OnClickListener,
        AddEditMessageDialog.AddEditMessageDialogListener,AdapterView.OnItemLongClickListener,
        EditDeleteDialog.SelectEditDeleteListener {

    private static final int NEW_MSG = 0;
    private static final int EDIT_MSG = 1;

    private DataManager mDataManager;
    private ListView mListView;
    private ShortMsgAdapter mAdapter;
    private ShortCutMsgModel selectRecord;
    private int mode;

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
        mListView.setOnItemLongClickListener(this);

        rootView.findViewById(R.id.fab).setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onClick(View view) {
        mode = NEW_MSG;
        AddEditMessageDialog dialog = new AddEditMessageDialog();
        dialog.setListener(this);
        dialog.show(getFragmentManager(),"AEM");
    }

    @Override
    public void onChange(String msg) {
        if (mode == NEW_MSG) {
            mDataManager.getDB().addShortCut(msg);
        } else {
            selectRecord.setMsg(msg);
            mDataManager.getDB().updateShortCut(selectRecord);
        }
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

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        selectRecord = (ShortCutMsgModel) parent.getItemAtPosition(position);

        EditDeleteDialog deleteDialog = new EditDeleteDialog();
        deleteDialog.setSelectEditDeleteListener(this);
        deleteDialog.show(getFragmentManager(),"ED");

        return false;
    }

    @Override
    public void selectItem(int id) {
        switch (id) {
            case R.id.edit_laout:
                mode = EDIT_MSG;
                AddEditMessageDialog dialog = AddEditMessageDialog.newInstance(selectRecord.getMsg());
                dialog.setListener(this);
                dialog.show(getFragmentManager(),"UEM");
                break;
            case R.id.del_laout:
                mDataManager.getDB().deleteShortCupId(selectRecord.getId());
                updateUI();
                break;
        }
    }
}
