package net.hath.drawcut.ui.activitiy;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import net.hath.drawcut.R;
import net.hath.drawcut.service.HUD;

public class SettingsActivity extends Activity {
    private static final String TAG = "SettingsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        setCheckedItems();
    }

    public void setCheckedItems() {
        SharedPreferences prefs = getSharedPreferences("gesturesettings", MODE_PRIVATE);
        if (prefs.getInt("serviceEnabled", -1) == 1) {
            ((CheckBox) findViewById(R.id.checkbox_toggle_floater)).setChecked(true);
        }
    }

    // called when a checkbox is clicked
    public void onCheckboxClicked(View view) {
        boolean checked = ((CheckBox) view).isChecked();

        switch (view.getId()) {
            case R.id.checkbox_toggle_floater:
                SharedPreferences.Editor editor = getSharedPreferences("gesturesettings", MODE_PRIVATE).edit();
                if (checked) {
                    // Turn on
                    editor.putInt("serviceEnabled", 1);
                    Intent intent = new Intent(this, HUD.class);
                    startService(intent);
                } else {
                    // turn off
                    editor.putInt("serviceEnabled", 0);
                    Intent intent = new Intent(this, HUD.class);
                    stopService(intent);
                }
                editor.apply();
                break;
            default:
                Log.w(TAG, "View not recognized. Error!!");
        }
    }
}
