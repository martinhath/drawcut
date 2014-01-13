package net.hath.drawcut.ui.activitiy;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;

import net.hath.drawcut.R;
import net.hath.drawcut.ui.fragment.PrefsFragment;


public class SettingsActivity extends Activity {

    private Fragment content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        if (savedInstanceState == null) {
            content = new PrefsFragment();
            getFragmentManager().beginTransaction().add(R.id.container, content).commit();
        }

    }
}
