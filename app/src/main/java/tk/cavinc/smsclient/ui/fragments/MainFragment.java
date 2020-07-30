package tk.cavinc.smsclient.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tk.cavinc.smsclient.R;
import tk.cavinc.smsclient.data.managers.DataManager;
import tk.cavinc.smsclient.services.SenserSmsService;

/**
 * Created by cav on 25.07.20.
 */

public class MainFragment extends Fragment {
    private DataManager mDataManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDataManager = DataManager.getInstance();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        if (mDataManager.isMyServiceRunning(SenserSmsService.class)) {
            rootView.findViewById(R.id.main_start).setEnabled(false);
        } else {
            rootView.findViewById(R.id.main_start).setEnabled(true);
        }

        return rootView;
    }
}
