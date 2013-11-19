package net.hath.drawcut.service;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import net.hath.drawcut.R;
import net.hath.drawcut.ui.activitiy.ApplicationLauncherActivity;

public class HUD extends Service {
    private static final String TAG = "HUD";
    private View view;
    private WindowManager windowManager;

    private Service launcher;

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

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
                    initialTouchX = x;
                    initialTouchY = y;

                    initialX = params.x;
                    initialY = params.y;
                }
                if(motionEvent.getAction() == MotionEvent.ACTION_MOVE){
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
                Intent intent = new Intent(HUD.this, LauncherService.class);
                startService(intent);
            }
        });

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        windowManager.removeView(view);
    }

}
