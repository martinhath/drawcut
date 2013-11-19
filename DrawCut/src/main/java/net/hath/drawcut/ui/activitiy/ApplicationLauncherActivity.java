package net.hath.drawcut.ui.activitiy;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.Window;
import net.hath.drawcut.R;
import net.hath.drawcut.data.GestureLibrary;
import net.hath.drawcut.data.LaunchItemDatabaseManager;
import net.hath.drawcut.view.DrawingView;


public class ApplicationLauncherActivity extends Activity implements DrawingView.GestureCallback{

    GestureLibrary gestureLibrary;
    DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);


        setContentView(R.layout.activity_launcher);


        gestureLibrary = GestureLibrary.getInstance(this);

        drawingView = (DrawingView) findViewById(R.id.launcher);

        LaunchItemDatabaseManager db = new LaunchItemDatabaseManager(this);

    }

    @Override
    public void onGestureDrawn() {

        Gesture g = drawingView.makeGesture();
        if (g.getStrokesCount() == 0) {
            return;
        }
        String name = gestureLibrary.getBestPredictionName(g);

        Intent intent = getPackageManager().getLaunchIntentForPackage(name);
        startActivity(intent);
        finish();
    }
}
