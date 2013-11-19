package net.hath.drawcut.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import net.hath.drawcut.R;
import net.hath.drawcut.data.GestureLibrary;
import net.hath.drawcut.view.DrawingView;

public class LauncherService extends Service implements DrawingView.GestureCallback {
    private static final String TAG = "LauncherService";
    private DrawingView view;
    private WindowManager windowManager;

    GestureLibrary glib;

    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        glib = GestureLibrary.getInstance(this);
        glib.load();

        Resources res = getResources();

        view = new DrawingView(this, null);


        view.setBackgroundColor(res.getColor(R.color.grey_dark));

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;

        params.horizontalMargin = res.getDimension(R.dimen.margin_large);
        params.verticalMargin = res.getDimension(R.dimen.margin_large);

        windowManager.addView(view, params);


        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                int kcode = keyEvent.getKeyCode();
                if(kcode == KeyEvent.KEYCODE_BACK ||
                   kcode == KeyEvent.KEYCODE_HOME){
                    onDestroy();
                }
                return true;
            }
        });

    }

    @Override
    public void onGestureDrawn() {
        Gesture g = view.makeGesture();
        if (g.getStrokesCount() == 0) {
            return;
        }
        view.clear();

        String name = glib.getBestPredictionName(g);

        Log.d(TAG, name);

        Intent intent = getPackageManager().getLaunchIntentForPackage(name);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        windowManager.removeView(view);
    }
}
