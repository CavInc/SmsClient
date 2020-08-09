package tk.cavinc.smsclient.ui.fragments;

import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;


import androidx.annotation.Nullable;
import tk.cavinc.smsclient.R;
import tk.cavinc.smsclient.ui.dialogs.SetDiapasoneDialog;

/**
 * Created by cav on 25.07.20.
 */

public class SettingFragment extends PreferenceFragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.pref);

        Preference timePeriod = findPreference("time_period");

        timePeriod.setOnPreferenceClickListener(mTimePeriodListener);
    }

    Preference.OnPreferenceClickListener mTimePeriodListener = new Preference.OnPreferenceClickListener() {
        @Override
        public boolean onPreferenceClick(Preference preference) {

            SetDiapasoneDialog diapasoneDialog = new SetDiapasoneDialog();
            diapasoneDialog.show(getFragmentManager(),"DD");

            return true;
        }
    };
}
