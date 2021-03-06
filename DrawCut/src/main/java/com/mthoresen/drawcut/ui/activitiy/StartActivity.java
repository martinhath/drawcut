package com.mthoresen.drawcut.ui.activitiy;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.gesture.Gesture;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.mthoresen.drawcut.R;
import com.mthoresen.drawcut.data.ApplicationItem;
import com.mthoresen.drawcut.data.LaunchItem;
import com.mthoresen.drawcut.data.LaunchItemProvider;
import com.mthoresen.drawcut.service.HUD;
import com.mthoresen.drawcut.ui.fragment.LaunchItemListFragment;

public class StartActivity extends Activity {
    private static final String TAG = "StartActivity";
    private Fragment content;
    private LaunchItemProvider launchItemProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_container);

        if (savedInstanceState == null) {
            content = new LaunchItemListFragment();
            content.setHasOptionsMenu(true);
            getFragmentManager().beginTransaction().add(R.id.container, content).commit();
        }


        launchItemProvider = LaunchItemProvider.getInstance(this);

        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        buildSharedPreferences();

        //Start service, if enabled
        //
        boolean service = prefs.getBoolean("service_enabled", false);
        if (service)
            startService(new Intent(this, HUD.class));

    }

    public void buildSharedPreferences() {
        SharedPreferences prefs = getSharedPreferences("settings", MODE_PRIVATE);
        SharedPreferences.Editor editor = getSharedPreferences("settings", MODE_PRIVATE).edit();
        if (prefs.getInt("gestureColor", 0) == 0)
            editor.putInt("gestureColor", getResources().getColor(R.color.drawing_color));
        if (prefs.getInt("gestureColorFresh", 0) == 0)
            editor.putInt("gestureColorFresh", getResources().getColor(R.color.drawing_color_fresh));
        if (prefs.getFloat("gestureStrokeWidth", 0) == 0)
            editor.putFloat("gestureStrokeWidth", 10f);
        editor.apply();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
        //noinspection ConstantConditions
        menu.findItem(R.id.action_add).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intent = new Intent(StartActivity.this, NewGestureActivity.class);
                startActivityForResult(intent, 1);
                return true;
            }
        });
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Gesture g = data.getParcelableExtra("gesture");
            String name = data.getStringExtra("name");
            ApplicationInfo appinfo = data.getParcelableExtra("applicationinfo");
            if (appinfo == null) {
                return;
            }
            ApplicationItem ai = new ApplicationItem(this, appinfo);
            LaunchItem gi = new LaunchItem(name, g, ai);
            launchItemProvider.addLaunchItem(gi);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
                startActivity(new Intent(this, SettingsActivity.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStop() {
        super.onStop();
        launchItemProvider.save();
    }
}
