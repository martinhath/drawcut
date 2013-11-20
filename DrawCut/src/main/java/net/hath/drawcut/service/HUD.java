package net.hath.drawcut.service;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.res.Resources;
import android.gesture.Gesture;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
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
    private final WindowManager.LayoutParams dViewParams = new WindowManager.LayoutParams(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.TYPE_PHONE,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
            PixelFormat.TRANSLUCENT);
    private View view;
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

        view = new ImageView(this);
        ((ImageView) view).setImageDrawable(getResources().getDrawable(R.drawable.hud));

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_PHONE,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        params.windowAnimations = android.R.style.Animation_Dialog;
        dViewParams.windowAnimations = android.R.style.Animation_Dialog;

        windowManager.addView(view, params);

        view.setOnTouchListener(new View.OnTouchListener() {
            private WindowManager.LayoutParams lp = params;
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

                    initialX = params.x;
                    initialY = params.y;
                }
                if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                    lp.x = initialX - (initialTouchX - x);
                    lp.y = initialY - (initialTouchY - y);
                    windowManager.updateViewLayout(view, params);
                }
                return false;
            }
        });

        view.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                createDrawingView();
            }
        });

    }

    @SuppressLint("NewApi")
    public void createDrawingView() {
        if(launchItemProvider == null){
            launchItemProvider = LaunchItemProvider.getInstance(this);
            launchItemProvider.init();
        }
        drawingView = new DrawingView(this, null);

        drawingView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                drawingView.onTouch(view, motionEvent);
                return false;
            }
        });

        Resources res = getResources();


        Drawable d = res.getDrawable(R.drawable.white_box);
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            drawingView.setBackgroundDrawable(d);
        } else {
            drawingView.setBackground(d);
        }

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        dViewParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL;
        dViewParams.horizontalMargin = res.getDimension(R.dimen.margin_large);
        dViewParams.verticalMargin = res.getDimension(R.dimen.margin_large);

        drawingView.setLayoutParams(dViewParams);

        windowManager.addView(drawingView, dViewParams);
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
        Log.d(TAG, "PACKAGE NAME: " + name);
        if (name == "" || name == null) {
            return;
        }

        Intent intent = getPackageManager().getLaunchIntentForPackage(name);
        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT |
                Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(view);
        windowManager.removeView(drawingView);
    }

}
