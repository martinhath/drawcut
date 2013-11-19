package net.hath.drawcut.ui.activitiy;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.gesture.Gesture;
import android.gesture.GestureStore;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import net.hath.drawcut.R;
import net.hath.drawcut.data.*;
import net.hath.drawcut.service.HUD;
import net.hath.drawcut.ui.fragment.LaunchItemListFragment;
import net.hath.drawcut.util.GestureUtil;
import net.hath.drawcut.util.Utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class StartActivity extends Activity implements LaunchItemProvider {
    private static final String TAG = "StartActivity";
    private List<LaunchItemSubscriber> subscribers;
    private Fragment content;
    private List<LaunchItem> launchItemList;
    private LaunchItemDatabaseManager databaseManager;
    private GestureLibrary gestureLibrary;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (savedInstanceState == null) {
            content = new LaunchItemListFragment();
            content.setHasOptionsMenu(true);
            getFragmentManager().beginTransaction().add(R.id.container, content).commit();
        }

        subscribers = new ArrayList<LaunchItemSubscriber>();


        // ASYNC
        gestureLibrary = GestureLibrary.getInstance(this);

        gestureLibrary.load();

        Set<String> strings = gestureLibrary.getGestureEntries();
        for(String s:strings){
            Log.d(TAG, "Gesture: "+s);
        }

        launchItemList = new ArrayList<LaunchItem>();
        databaseManager = new LaunchItemDatabaseManager(getApplicationContext());
        launchItemList = databaseManager.getLaunchItems();


        for (LaunchItem li : launchItemList) {
            List<Gesture> glist = gestureLibrary.getGestures(li.getApplicationPackage());
            if(glist == null){
                Log.d(TAG, "glist is null");
                continue;
            }
            Gesture g = glist.get(0);
            if (g != null) {
                li.setGesture(g);
            }
        }

        SharedPreferences.Editor preferences = getSharedPreferences("gesturesettings", MODE_PRIVATE).edit();
        preferences.putInt("gestureColor", getResources().getColor(R.color.drawing_color));
        preferences.putInt("gestureColorFresh", getResources().getColor(R.color.drawing_color_fresh));
        preferences.putFloat("gestureStrokeWidth", 10f);
        preferences.apply();



        startService(new Intent(this, HUD.class));

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
            ApplicationItem ai = new ApplicationItem(this, (ApplicationInfo) data.getParcelableExtra("applicationinfo"));

            LaunchItem gi = new LaunchItem(name, g, ai);

            SharedPreferences prefs = getSharedPreferences("gesturesettings", MODE_PRIVATE);

            Bitmap b = GestureUtil.toBitmap(g, prefs.getInt("gestureColor", 0), prefs.getFloat("gestureStrokeWidth", 1));

            Utils.saveBitmapToFile(this, "" + gi.getId(), b);

            gi.setGestureImage(b);

            addGesture(gi);

            databaseManager.putLaunchItem(gi);


            gestureLibrary.addGesture(ai.getPackageName(), g);

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
    public List<LaunchItem> getLaunchItemList() {
        return launchItemList;
    }

    @Override
    protected void onStop() {
        super.onStop();
        gestureLibrary.save();
    }

    @Override
    public void addGesture(LaunchItem g) {
        launchItemList.add(g);
        for (LaunchItemSubscriber s : subscribers) {
            s.update();
        }
    }

    @Override
    public void addSubscriber(LaunchItemSubscriber sub) {
        subscribers.add(sub);
    }

    @Override
    public void removeSubscriber(LaunchItemSubscriber sub) {
        subscribers.remove(sub);
    }
}
