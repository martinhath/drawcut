package net.hath.drawcut.service;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import net.hath.drawcut.R;
import net.hath.drawcut.data.GestureLibrary;
import net.hath.drawcut.data.LaunchItemProvider;
import net.hath.drawcut.view.DrawingView;
import net.hath.drawcut.view.LauncherView;

public class HUD extends Service implements DrawingView.GestureCallback {
    private static final String TAG = "HUD";
    private WindowManager.LayoutParams launcherParams;
    private View floater;
    private LauncherView launcherView;
    private DrawingView drawingView;
    private WindowManager windowManager;
    private LaunchItemProvider launchItemProvider;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        floater = new ImageView(this);
        launcherView = new LauncherView(this, null);
        drawingView = launcherView.getDrawingView();

        Resources res = getResources();
        ((ImageView) floater).setImageDrawable(res.getDrawable(R.drawable.hud));

        launchItemProvider = LaunchItemProvider.getInstance(this);

        launcherView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                drawingView.onTouch(view, motionEvent);
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    windowManager.removeView(launcherView);
                }
                return false;
            }
        });

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        final WindowManager.LayoutParams floaterParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        floaterParams.gravity = Gravity.LEFT;

        launcherParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                PixelFormat.TRANSLUCENT);
        launcherParams.dimAmount = 0.3F;
        launcherParams.gravity = Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL;

        floaterParams.windowAnimations = android.R.style.Animation_Dialog;
        launcherParams.windowAnimations = android.R.style.Animation_Dialog;

        windowManager.addView(floater, floaterParams);
        SharedPreferences prefs = getSharedPreferences("hud", MODE_PRIVATE);
        float x = prefs.getFloat("x", 0);
        float y = prefs.getFloat("y", 0);
        Log.d(TAG, x + " " + y);
        floater.setX(x);
        floater.setY(y);

        floater.setOnTouchListener(new View.OnTouchListener() {
            private WindowManager.LayoutParams lp = floaterParams;
            private int initialX;
            private int initialY;
            private int initialTouchX;
            private int initialTouchY;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                int x = (int) motionEvent.getRawX();
                int y = (int) motionEvent.getRawY();

                if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    initialTouchX = x;
                    initialTouchY = y;

                    initialX = floaterParams.x;
                    initialY = floaterParams.y;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    lp.x = initialX - (initialTouchX - x);
                    lp.y = initialY - (initialTouchY - y);
                    windowManager.updateViewLayout(view, floaterParams);
                }
                return false;
            }
        });

        floater.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                windowManager.addView(launcherView, launcherParams);
                launcherView.requestFocus();
            }
        });

        launcherView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d(TAG, "Keypress");
                int key = keyEvent.getKeyCode();
                if (key == KeyEvent.KEYCODE_BACK || key == KeyEvent.KEYCODE_HOME) {
                    windowManager.removeView(launcherView);
                    launcherView.setDescription("");
                }
                return false;
            }
        });
    }

    @Override
    public void onGestureDrawn() {
        Gesture g = drawingView.makeGesture();
        if (g.getStrokesCount() == 0) {
            launcherView.setDescription("");
            return;
        }
        drawingView.clear();

        GestureLibrary glib = launchItemProvider.getGestureLibrary();
        Log.d(TAG, glib.toString());

        String name = glib.getBestPredictionName(g);
        if (name == null || name.equals("")) {
            launcherView.setDescription(getResources().getString(R.string.gesture_nf));
            return;
        }

        Intent intent = getPackageManager().getLaunchIntentForPackage(name);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        windowManager.removeView(launcherView);
        launcherView.setDescription("");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        SharedPreferences.Editor prefs = getSharedPreferences("hud", MODE_PRIVATE).edit();
        float x = floater.getX();
        float y = floater.getY();
        Log.d(TAG, "Put: " + x + " " + y);
        prefs.putFloat("x", x);
        prefs.putFloat("y", y);
        prefs.apply();

        windowManager.removeView(floater);
    }

}
