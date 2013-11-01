package net.hath.drawcut;

import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GesturePoint;
import android.gesture.GestureStroke;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class DrawingView extends SquareView implements View.OnTouchListener {
    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "DrawingView";
    private static final float STROKE_WIDTH = 10;
    public static final int RADIUS = (int) STROKE_WIDTH / 2;

    private Context context;

    public GestureLook glook_fresh;

    public GestureLook glook;


    ArrayList<GesturePoint> points = new ArrayList<GesturePoint>();
    ArrayList<GesturePoint> points_old = new ArrayList<GesturePoint>();
    List<GestureStroke> strokes = new LinkedList<GestureStroke>();


    @SuppressWarnings("UnusedDeclaration")
    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
        Log.d(TAG, "CTOR, 2");
    }

    private void init() {
        setOnTouchListener(this);
        setFocusable(true);
        setFocusableInTouchMode(true);
        glook = new GestureLook();
        glook_fresh = new GestureLook();

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.drawing_color_fresh));
        paint.setStyle(Paint.Style.STROKE);
        glook_fresh.setColor(paint);
        glook_fresh.setWidth(STROKE_WIDTH);


        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.drawing_color));
        paint.setStyle(Paint.Style.STROKE);
        glook.setColor(paint);
        glook.setWidth(STROKE_WIDTH);

    }

    public void clear() {
        points.clear();
        points_old.clear();
        strokes.clear();
        invalidate();
    }


    public GestureLook getGestureLook() {
        return glook;
    }

    /**
     * Adds the current GesturePoint List to the Strokelist.
     */
    public void commit() {
        if (points.size() == 0) return;
        GestureStroke gs = new GestureStroke(points);
        strokes.add(gs);
        points_old.addAll(points);
        points_old.add(null);
        points.clear();
        invalidate();
    }

    public Gesture makeGesture(List<GestureStroke> list) {
        Gesture g = new Gesture();
        for (GestureStroke gs : list) {
            g.addStroke(gs);
        }
        return g;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Draw old stroke
        if (!(points_old.size() < 1)) {
            GesturePoint p0;
            GesturePoint p1 = points_old.get(0);
            canvas.drawCircle(p1.x, p1.y, glook.getWidth() / 2, glook.getColor());
            for (int i = 1; i < points_old.size(); i++) {
                p0 = p1;
                p1 = points_old.get(i);
                // p1 == null means the user has released, and is drawing again.
                // which means that we shouldn't draw a line
                if (p0 == null) {
                } else {
                    if (p1 == null) {
                        canvas.drawCircle(p0.x, p0.y, glook.getWidth() / 2, glook.getColor());
                    } else {
                        // Ingen er nulls
                        canvas.drawCircle(p0.x, p0.y, glook.getWidth() / 2, glook.getColor());
                        canvas.drawLine(p0.x, p0.y, p1.x, p1.y, glook.getColor());
                    }
                }
            }
        }
        // Draw current stroke
        if (points.size() < 1) return;
        GesturePoint p0;
        GesturePoint p1 = points.get(0);
        canvas.drawCircle(p1.x, p1.y, glook_fresh.getWidth() / 2, glook_fresh.getColor());
        for (int i = 1; i < points.size(); i++) {
            p0 = p1;
            p1 = points.get(i);
            // p1 == null means the user has released, and is drawing again.
            // which means that we shouldn't draw a line
            if (p0 == null || p1 == null) {
                continue;
            }
            canvas.drawLine(p0.x, p0.y, p1.x, p1.y, glook_fresh.getColor());
            canvas.drawCircle(p0.x, p0.y, glook_fresh.getWidth() / 2, glook_fresh.getColor());
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP:
                commit();
                break;
            default:
                GesturePoint p = new GesturePoint(motionEvent.getX(), motionEvent.getY(), 500);
                points.add(p);
                invalidate();
        }
        return true;
    }

}








