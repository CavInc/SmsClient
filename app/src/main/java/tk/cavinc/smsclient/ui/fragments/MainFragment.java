package tk.cavinc.smsclient.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import tk.cavinc.smsclient.R;
import tk.cavinc.smsclient.data.managers.DataManager;
import tk.cavinc.smsclient.data.models.ShortCutMsgModel;
import tk.cavinc.smsclient.services.SenserSmsService;
import tk.cavinc.smsclient.utils.App;
import tk.cavinc.smsclient.utils.Utils;

import static android.content.ContentValues.TAG;

/**
 * Created by cav on 25.07.20.
 */

public class MainFragment extends Fragment implements View.OnClickListener,Observer {
    private DataManager mDataManager;

    private Button mStart;
    private Button mStop;
    private LinearLayout mSimSelect;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();

        //mDataManager.getDB().deleteAllQuery();
        //mDataManager.getPrefManager().setCountQuery(0);
        if (mDataManager.getSimDataModel().size() > 1) {
            //TODO включаем кнопки для симок
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mStart = rootView.findViewById(R.id.main_start);
        mStop = rootView.findViewById(R.id.main_stop);

        mStart.setOnClickListener(this);
        mStop.setOnClickListener(this);

        if (mDataManager.isMyServiceRunning(SenserSmsService.class)) {
            mStart.setEnabled(false);
        } else {
            mStart.setEnabled(true);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getStopServiceObserver().addObserver(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        App.getStopServiceObserver().deleteObserver(this);
    }

    // пауза
    public void onPauseService(View v) {

    }

    // запуск сервиса
    public void onStartService(View v){
        int workCount = mDataManager.getDB().getNoWorkCountPhone();
        if (workCount == 0) {
            Toast.makeText(getActivity(),"Нет не обработанных телефонов",Toast.LENGTH_LONG).show();
            return;
        }
        //mDataManager.getDB().deleteAllQuery();
        Intent intent = new Intent(getActivity(), SenserSmsService.class);
        // тодо тут надо добавить прверку на версию
        //if (Build.VERSION)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getActivity().startForegroundService(intent);
        } else {
            getActivity().startService(intent);
        }
    }

    // остановка сервиса
    public void onStopService(View v){
        Intent intent = new Intent(getActivity(),SenserSmsService.class);
        getActivity().stopService(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.main_start:
                onStartService(view);
                mStart.setEnabled(false);
                break;
            case R.id.main_stop:
                onStopService(view);
                mStart.setEnabled(true);
                break;
            case R.id.main_pause:
                onPauseService(view);
                break;
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        if (mDataManager.isMyServiceRunning(SenserSmsService.class)) {
            mStart.setEnabled(false);
        } else {
            mStart.setEnabled(true);
        }
    }
}
