package net.hath.drawcut.ui.activitiy;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import net.hath.drawcut.R;
import net.hath.drawcut.data.ApplicationItem;
import net.hath.drawcut.data.LaunchItem;
import net.hath.drawcut.data.LaunchItemProvider;
import net.hath.drawcut.service.HUD;
import net.hath.drawcut.ui.fragment.LaunchItemListFragment;
import net.hath.drawcut.util.GestureUtil;
import net.hath.drawcut.util.Utils;

public class StartActivity extends Activity {
    private static final String TAG = "StartActivity";
    private Fragment content;
    private LaunchItemProvider launchItemProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (savedInstanceState == null) {
            content = new LaunchItemListFragment();
            content.setHasOptionsMenu(true);
            getFragmentManager().beginTransaction().add(R.id.container, content).commit();
        }


        launchItemProvider = LaunchItemProvider.getInstance(this);

        SharedPreferences.Editor preferences = getSharedPreferences("gesturesettings", MODE_PRIVATE).edit();
        buildSharedPreferences();

        //Start service, if enabled
        //
        int service = preferences.getInt("serviceEnabled", 1);
        if(service > 0)
            startService(new Intent(this, HUD.class));

    }

    public void buildSharedPreferences(){
        sharedPreferences.Editor prefs = getSharedPreferences("gesturesettings", MODE_PRIVATE).edit();
        if (prefs.getInt("gestureColor", 0) == 0)
            prefs.putInt("gestureColor", getResources().getColor(R.color.drawing_color));
        if (prefs.getInt("gestureColorFresh", 0) == 0)
            prefs.putInt("gestureColorFresh", getResources().getColor(R.color.drawing_color_fresh));
        if (prefs.getFloat("gestureStrokeWidth", 0) == 0)
            prefs.putFloat("gestureStrokeWidth", 10f);
        if (prefs.getInt("serviceEnabled", -1) == -1)
            prefs.putInt("serviceEnabled", 1);
        prefs.apply();
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
            if(appinfo == null){
                Log.d(TAG, "Got null");
                return;
            }
            ApplicationItem ai = new ApplicationItem(this, appinfo);

            LaunchItem gi = new LaunchItem(name, g, ai);

            launchItemProvider.addLaunchItem(gi);

            Log.d(TAG, "Added Gesture. ");

        } else {
            Log.w(TAG, "Failed to get result. ");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.action_settings:
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
