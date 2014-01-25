package net.hath.drawcut.ui.activitiy;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.TextView;

import net.hath.drawcut.R;
import net.hath.drawcut.data.GestureLibrary;
import net.hath.drawcut.data.LaunchItemProvider;
import net.hath.drawcut.view.DrawingView;

public class LauncherActivity extends Activity implements DrawingView.GestureCallback {

    private static final String TAG = "LauncherActivity";
    private DrawingView drawingView;
    private TextView text;
    private LaunchItemProvider launchItemProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "OnCreate");
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        setContentView(R.layout.launcher_overlay);

        drawingView = (DrawingView) findViewById(R.id.draw);
        text = (TextView) findViewById(R.id.text);

        text.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/roboto-li.ttf"));

        launchItemProvider = LaunchItemProvider.getInstance(this);

        setFinishOnTouchOutside(true);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, android.R.anim.slide_out_right);
    }

    @Override
    public void onGestureDrawn() {
        Gesture g = drawingView.makeGesture();
        if (g.getStrokesCount() == 0) {
            return;
        }
        drawingView.clear();

        GestureLibrary glib = launchItemProvider.getGestureLibrary();

        String name = glib.getBestPredictionName(g);
        Log.d(TAG, "PACKAGE NAME: " + name);
        if (name.equals("") || name == null) {
            return;
        }

        Intent intent = getPackageManager().getLaunchIntentForPackage(name);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        finish();
    }

}
