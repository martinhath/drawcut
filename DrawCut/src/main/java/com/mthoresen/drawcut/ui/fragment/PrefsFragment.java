package com.mthoresen.drawcut.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import com.mthoresen.drawcut.R;
import com.mthoresen.drawcut.service.HUD;

/**
 * Created by Martin on 13.01.14.
 */
public class PrefsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {
    private static final String TAG = "PrefsFragment";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PreferenceManager prefm = getPreferenceManager();
        prefm.setSharedPreferencesName("settings");
        prefm.setSharedPreferencesMode(getActivity().MODE_PRIVATE);

        prefm.getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals("service_enabled")) {
            boolean shouldStart = sharedPreferences.getBoolean(s, false);
            Intent intent = new Intent(getActivity(), HUD.class);
            if (shouldStart) {  // Turn on
                getActivity().startService(intent);
            } else {            // Turn off
                getActivity().stopService(intent);
            }

        }
    }
}
