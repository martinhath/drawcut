package net.hath.drawcut.service;

import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import net.hath.drawcut.R;
import net.hath.drawcut.data.GestureLibrary;
import net.hath.drawcut.data.LaunchItemProvider;
import net.hath.drawcut.view.DrawingView;

public class HUD extends Service implements DrawingView.GestureCallback {
    private static final String TAG = "HUD";
    private WindowManager.LayoutParams drawingParams;
    private View floater;
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
        drawingView = new DrawingView(this, null);

        Resources res = getResources();
        ((ImageView) floater).setImageDrawable(res.getDrawable(R.drawable.hud));

        Drawable d = res.getDrawable(R.drawable.white_box);

        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            drawingView.setBackgroundDrawable(d);
        } else {
            drawingView.setBackground(d);
        }

        launchItemProvider = LaunchItemProvider.getInstance(this);

        drawingView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                drawingView.onTouch(view, motionEvent);
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

        drawingParams = new WindowManager.LayoutParams(
                (int) res.getDimension(R.dimen.launcher_size),
                (int) res.getDimension(R.dimen.launcher_size),
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                PixelFormat.TRANSLUCENT);
        drawingParams.dimAmount = 0.2F;

        floaterParams.windowAnimations = android.R.style.Animation_Dialog;
        drawingParams.windowAnimations = android.R.style.Animation_Dialog;

        windowManager.addView(floater, floaterParams);

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
                windowManager.addView(drawingView, drawingParams);
                drawingView.requestFocus();
            }
        });

        drawingView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                Log.d(TAG, i + "");
                int key = keyEvent.getKeyCode();
                if (key == KeyEvent.KEYCODE_BACK || key == KeyEvent.KEYCODE_HOME) {
                    windowManager.removeView(drawingView);
                }
                return false;
            }
        });
    }

    @Override
    public void onGestureDrawn() {
        Gesture g = drawingView.makeGesture();
        if (g.getStrokesCount() == 0) {
            return;
        }
        drawingView.clear();

        windowManager.removeView(drawingView);

        GestureLibrary glib = launchItemProvider.getGestureLibrary();

        String name = glib.getBestPredictionName(g);
        if (name == null || name.equals("")) return;

        Intent intent = getPackageManager().getLaunchIntentForPackage(name);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(floater);
        windowManager.removeView(drawingView);
    }

}
