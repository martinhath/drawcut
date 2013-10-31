package net.hath.drawcut;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.gesture.Gesture;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class StartActivity extends Activity implements GestureProvider{
    private static final String TAG = "StartActivity";

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

        // Skal laste fra DB.
        gestures = new ArrayList<GestureItem>();
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

            addGesture(new GestureItem(g, name));
            Log.d(TAG, "Added Gesture. ");
            Log.d(TAG, "Current number of gestures: " + gestures.size());

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
    }
}
