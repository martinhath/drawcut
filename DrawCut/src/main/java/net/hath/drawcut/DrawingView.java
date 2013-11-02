package net.hath.drawcut;

import android.content.Context;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GesturePoint;
import android.gesture.GestureStroke;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


public class DrawingView extends SquareView implements View.OnTouchListener {
    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "DrawingView";

    private Context context;

    ArrayList<GesturePoint> points = new ArrayList<GesturePoint>();
    ArrayList<GesturePoint> points_old = new ArrayList<GesturePoint>();
    List<GestureStroke> strokes = new LinkedList<GestureStroke>();

    private Paint color;
    private Paint color_fresh;
    private float strokeWidth;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        setOnTouchListener(this);
        setFocusable(true);
        setFocusableInTouchMode(true);

        SharedPreferences spref = context.getSharedPreferences("gesturesettings", Context.MODE_PRIVATE);
        strokeWidth = spref.getFloat("gestureStrokeWidth", 1);


        color = new Paint(Paint.ANTI_ALIAS_FLAG);
        color.setColor(spref.getInt("gestureColor", 0));
        color.setStyle(Paint.Style.STROKE);
        color.setDither(true);
        color.setStrokeJoin(Paint.Join.ROUND);
        color.setStrokeCap(Paint.Cap.ROUND);
        color.setStrokeMiter(0.5f);
        color.setStrokeWidth(strokeWidth);

        color_fresh = new Paint(Paint.ANTI_ALIAS_FLAG);
        color_fresh.setStyle(Paint.Style.STROKE);
        color_fresh.setStrokeJoin(Paint.Join.ROUND);
        color_fresh.setColor(spref.getInt("gestureColorFresh", 0));
        color_fresh.setStrokeWidth(strokeWidth);
    }

    public void clear() {
        points.clear();
        points_old.clear();
        strokes.clear();
        invalidate();
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
            canvas.drawCircle(p1.x, p1.y, strokeWidth / 2, color);
            for (int i = 1; i < points_old.size(); i++) {
                p0 = p1;
                p1 = points_old.get(i);
                // p1 == null means the user has released, and is drawing again.
                // which means that we shouldn't draw a line
                if (p0 == null) {
                } else {
                    if (p1 == null) {
                        canvas.drawCircle(p0.x, p0.y, strokeWidth / 2, color);
                    } else {
                        // Ingen er nulls
                        canvas.drawLine(p0.x, p0.y, p1.x, p1.y, color);
                    }
                }
            }
        }
        // Draw current stroke
        if (points.size() < 1) return;
        GesturePoint p0;
        GesturePoint p1 = points.get(0);
        canvas.drawCircle(p1.x, p1.y, strokeWidth / 2, color_fresh);
        for (int i = 1; i < points.size(); i++) {
            p0 = p1;
            p1 = points.get(i);
            // p1 == null means the user has released, and is drawing again.
            // which means that we shouldn't draw a line
            if (p0 == null || p1 == null) {
                continue;
            }
            canvas.drawLine(p0.x, p0.y, p1.x, p1.y, color_fresh);
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








