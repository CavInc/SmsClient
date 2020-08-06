package tk.cavinc.smsclient.ui.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Observable;
import java.util.Observer;

import tk.cavinc.smsclient.R;
import tk.cavinc.smsclient.data.managers.DataManager;
import tk.cavinc.smsclient.services.SenserSmsService;
import tk.cavinc.smsclient.utils.App;

/**
 * Created by cav on 25.07.20.
 */

public class MainFragment extends Fragment implements View.OnClickListener,Observer {
    private DataManager mDataManager;

    private Button mStart;
    private Button mStop;
    private Button mPause;
    private LinearLayout mSimSelect;

    private Button mSim1;
    private Button mSim2;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();

        //mDataManager.getDB().deleteAllQuery();
        //mDataManager.getPrefManager().setCountQuery(0);
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        mStart = rootView.findViewById(R.id.main_start);
        mStop = rootView.findViewById(R.id.main_stop);
        mPause = rootView.findViewById(R.id.main_pause);

        mStart.setOnClickListener(this);
        mStop.setOnClickListener(this);
        mPause.setOnClickListener(this);

        if (mDataManager.getPrefManager().getServicePause()) {
            mStop.setEnabled(false);
            mPause.setEnabled(false);
        }

        if (mDataManager.isMyServiceRunning(SenserSmsService.class)) {
            mStart.setEnabled(false);
        } else {
            mStart.setEnabled(true);
        }

        mSimSelect = rootView.findViewById(R.id.select_sim);
        mSim1 = rootView.findViewById(R.id.sim1);
        mSim2 = rootView.findViewById(R.id.sim2);

        mSim1.setOnClickListener(this);
        mSim2.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        App.getStopServiceObserver().addObserver(this);
        if (mDataManager.getSimDataModel().size() > 1) {
            //TODO включаем кнопки для симок
            mSimSelect.setVisibility(View.VISIBLE);
            mSim1.setText(mDataManager.getSimDataModel().get(0).getName());
            mSim2.setText(mDataManager.getSimDataModel().get(1).getName());
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        App.getStopServiceObserver().deleteObserver(this);
    }

    // пауза
    public void onPauseService(View v) {
        mDataManager.getPrefManager().setServicePause(true);
        mPause.setEnabled(false);
        mStop.setEnabled(false);
        mStart.setEnabled(true);
        Intent intent = new Intent(getActivity(),SenserSmsService.class);
        getActivity().stopService(intent);
    }

    // запуск сервиса
    public void onStartService(View v){
        if (mDataManager.getPrefManager().getServicePause()) {
            mPause.setEnabled(true);
            mStop.setEnabled(true);
            mDataManager.getPrefManager().setServicePause(false);
        }

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
        mDataManager.getDB().clearWorkedPhone();
        mDataManager.getPrefManager().setServicePause(false);
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
            case R.id.sim1:
                mSim1.setActivated(true);
                break;
            case R.id.sim2:
                break;
        }
    }

    @Override
    public void update(Observable observable, Object o) {
        /*
        while (mDataManager.isMyServiceRunning(SenserSmsService.class)){

        }
        */
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mStart.setEnabled(true);
            }
        });
    }
}
