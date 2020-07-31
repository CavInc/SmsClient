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

import tk.cavinc.smsclient.R;
import tk.cavinc.smsclient.data.managers.DataManager;
import tk.cavinc.smsclient.services.SenserSmsService;

/**
 * Created by cav on 25.07.20.
 */

public class MainFragment extends Fragment implements View.OnClickListener {
    private DataManager mDataManager;

    private Button mStart;
    private Button mStop;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
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

    // запуск сервиса
    public void onStartService(View v){
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
        }
    }
}
