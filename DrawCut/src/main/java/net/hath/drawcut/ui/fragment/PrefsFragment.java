package net.hath.drawcut.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import net.hath.drawcut.R;

/**
 * Created by Martin on 13.01.14.
 */
public class PrefsFragment extends PreferenceFragment {
    private static final String TAG = "SettingsActivity";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName("gesturesettings");

        addPreferencesFromResource(R.xml.preferences);
    }
}
