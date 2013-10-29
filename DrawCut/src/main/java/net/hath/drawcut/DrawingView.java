package net.hath.drawcut;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;

import java.util.LinkedList;
import java.util.List;


public class DrawingView extends SurfaceView implements View.OnTouchListener {
    public static final int RADIUS = 5;
    private static final String TAG = "DrawingView";
    private Paint paint = new Paint();

    List<Point> points = new LinkedList<Point>();

    public DrawingView(Context context) {
        super(context);
    }

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        paint.setColor(0xff9966);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for(Point p: points){
            canvas.drawCircle(p.x, p.y, RADIUS, paint);
        }
        super.onDraw(canvas);
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d(TAG, String.format("%d, %d", motionEvent.getX(), motionEvent.getY()));
        Point p = new Point();
        p.x = (int) motionEvent.getX();
        p.y = (int) motionEvent.getY();
        points.add(p);
        return true;
    }
}
