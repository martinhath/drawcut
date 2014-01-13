package net.hath.drawcut.ui.activitiy;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import net.hath.drawcut.R;

public class SettingsActivity extends PreferenceFragment {
    private static final String TAG = "SettingsActivity";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getPreferenceManager().setSharedPreferencesName("gesturesettings");

        addPreferencesFromResource(R.xml.preferences);
    }

}
