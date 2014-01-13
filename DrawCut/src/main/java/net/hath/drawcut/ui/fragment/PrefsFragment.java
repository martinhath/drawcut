package net.hath.drawcut.ui.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.util.Log;

import net.hath.drawcut.R;
import net.hath.drawcut.service.HUD;

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
        Log.d(TAG, "Preferences are changed");
        if (s.equals("service_enabled")) {
            boolean shouldStart = sharedPreferences.getBoolean(s, false);
            if (shouldStart) {
                // Turn on
                Intent intent = new Intent(getActivity(), HUD.class);
                getActivity().startService(intent);
            } else {
                // Turn off
                Intent intent = new Intent(getActivity(), HUD.class);
                getActivity().stopService(intent);
            }

        }
    }
}
