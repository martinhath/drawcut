package net.hath.drawcut.ui.fragment;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;

import net.hath.drawcut.R;

/**
 * Created by Martin on 13.01.14.
 */
public class PrefsFragment extends PreferenceFragment {
    private static final String TAG = "PrefsFragment";

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        PreferenceManager prefm = getPreferenceManager();
        prefm.setSharedPreferencesName("settings");
        prefm.setSharedPreferencesMode(getActivity().MODE_PRIVATE);

        addPreferencesFromResource(R.xml.preferences);
    }
}
