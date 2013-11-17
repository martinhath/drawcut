package net.hath.drawcut;

import android.app.Activity;
import android.app.DialogFragment;
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
import android.view.View;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends Activity implements GestureProvider{
    private static final String TAG = "StartActivity";
    private List<GestureSubscriber> subscribers;

    private Fragment content;
    private List<GestureItem> gestures;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        if (savedInstanceState == null) {
            content = new GestureListFragment();
            content.setHasOptionsMenu(true);
            getFragmentManager().beginTransaction().add(R.id.container, content).commit();
        }

        subscribers = new ArrayList<GestureSubscriber>();
        // Skal laste fra DB.
        gestures = new ArrayList<GestureItem>();

        SharedPreferences.Editor preferences = getSharedPreferences("gesturesettings", MODE_PRIVATE).edit();
        preferences.putInt("gestureColor", getResources().getColor(R.color.drawing_color));
        preferences.putInt("gestureColorFresh", getResources().getColor(R.color.drawing_color_fresh));
        preferences.putFloat("gestureStrokeWidth", 10f);
        preferences.apply();

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
        if(resultCode == RESULT_OK){
            Gesture g = data.getParcelableExtra("gesture");
            String name = data.getStringExtra("name");
            Intent i = data.getParcelableExtra("intent");
            ApplicationItem ai = new ApplicationItem(this, (ApplicationInfo) data.getParcelableExtra("applicationinfo"));

            GestureItem gi = new GestureItem(g, name);
            gi.setIntent(i);
            gi.setApplicationItem(ai);

            SharedPreferences prefs = getSharedPreferences("gesturesettings", MODE_PRIVATE);

            Bitmap b = GestureUtil.toBitmap(g, prefs.getInt("gestureColor", 0), prefs.getFloat("gestureStrokeWidth", 1));

            //Utils.saveBitmapToFile(""+gestures.size(), b);

            gi.setImage(b);

            addGesture(gi);

            Log.d(TAG, "Added Gesture. ");

        }else{
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
    public List<GestureItem> getGestures() {
        return gestures;
    }

    @Override
    public void addGesture(GestureItem g) {
        gestures.add(g);
        for(GestureSubscriber s:subscribers){
            s.update();
        }
    }

    @Override
    public void addSubscriber(GestureSubscriber sub) {
        subscribers.add(sub);
    }

    @Override
    public void removeSubscriber(GestureSubscriber sub) {
        subscribers.remove(sub);
    }
}
