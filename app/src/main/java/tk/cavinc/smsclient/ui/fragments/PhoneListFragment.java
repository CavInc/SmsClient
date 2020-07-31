package tk.cavinc.smsclient.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.provider.DocumentFile;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.channels.FileChannel;
import java.util.ArrayList;

import tk.cavinc.smsclient.R;
import tk.cavinc.smsclient.data.managers.DataManager;
import tk.cavinc.smsclient.data.models.PhoneListModel;
import tk.cavinc.smsclient.ui.adapters.PhoneAdapter;
import tk.cavinc.smsclient.ui.dialogs.PhoneDialog;

import static android.app.Activity.RESULT_OK;

/**
 * Created by cav on 26.07.20.
 */

public class PhoneListFragment extends Fragment implements View.OnClickListener {

    private static final int REQUEST_OPEN_DOCUMENT = 345;
    private static final String TAG = "PLF";
    private DataManager mDataManager;

    private PhoneAdapter mAdapter;
    private ListView mListView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_phone_list, container, false);

        rootView.findViewById(R.id.fab).setOnClickListener(this);

        mListView = rootView.findViewById(R.id.phone_lv);

        return rootView;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.phone_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.phone_load:
                loadPhoneList();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateUI();
    }

    private void updateUI(){
        ArrayList<PhoneListModel> data = mDataManager.getDB().getPhone();
        if (mAdapter == null) {
            mAdapter = new PhoneAdapter(getActivity(),R.layout.phone_item,data);
            mListView.setAdapter(mAdapter);
        } else {
            mAdapter.setData(data);
        }
    }

    private void loadPhoneList(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        startActivityForResult(Intent.createChooser(intent,"Выбор каталога"),REQUEST_OPEN_DOCUMENT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_OPEN_DOCUMENT && resultCode == RESULT_OK){
            if (data != null) {
                System.out.println(data);
                Uri uri = data.getData();
                System.out.println(uri);

                final File fname = copyUriToLocal(uri);
                loadPhonefile(fname);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private File copyUriToLocal(Uri uri){
        DocumentFile file = DocumentFile.fromSingleUri(getActivity(),uri);
        Log.d(TAG,file.getName());
        try {
            File fOut = new File(mDataManager.getStorageAppPath(),file.getName());

            FileInputStream input = (FileInputStream) getActivity().getContentResolver().openInputStream(file.getUri());
            FileOutputStream output = new FileOutputStream(fOut);

            FileChannel fileChannelIn = input.getChannel();
            FileChannel fileChannelOut = output.getChannel();
            fileChannelIn.transferTo(0, fileChannelIn.size(), fileChannelOut);

            output.flush();
            output.close();
            input.close();

            return fOut;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    // загрузка файлов
    private void loadPhonefile(File file){
        if (!mDataManager.isExternalStorageWritable()) {
            // TODO показать что картыа или память ек
        }
        // удалили старые данные о телефонах
        mDataManager.getDB().deletePhoneAll();

        try {
            InputStreamReader inputStreamReader = new InputStreamReader(
                    new FileInputStream(file));

            BufferedReader br = new BufferedReader(inputStreamReader);
            String str = "";
            mDataManager.getDB().open();
            while ((str = br.readLine()) != null) {
                str = str.trim();
                if (str.length() != 0) {
                    Log.d(TAG,str);
                    mDataManager.getDB().addPhone(str);
                }
            }
            br.close();
            mDataManager.getDB().close();

            int count = mDataManager.getDB().getCountPhone();
            mDataManager.getPrefManager().setCountPhone(count);

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getActivity(),e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
        }

        updateUI();

    }

    @Override
    public void onClick(View view) {
        PhoneDialog dialog = new PhoneDialog();
        dialog.setPhoneDialogListener(mPhoneDialogListener);
        dialog.show(getFragmentManager(),"PD");
    }

    PhoneDialog.PhoneDialogListener mPhoneDialogListener = new PhoneDialog.PhoneDialogListener() {
        @Override
        public void onChangePhone(String phone) {
            mDataManager.getDB().open();
            mDataManager.getDB().addPhone(phone);
            mDataManager.getDB().close();
            updateUI();
        }
    };
}
