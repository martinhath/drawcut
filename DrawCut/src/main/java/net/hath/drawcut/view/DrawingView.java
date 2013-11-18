package net.hath.drawcut.view;

import android.content.Context;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.gesture.GesturePoint;
import android.gesture.GestureStroke;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
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

    private ArrayList<GesturePoint> points = new ArrayList<GesturePoint>();
    private ArrayList<GesturePoint> points_old = new ArrayList<GesturePoint>();

    private List<GestureStroke> gestureStrokes = new LinkedList<GestureStroke>();

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
        gestureStrokes.clear();
        invalidate();
    }

    /**
     * Creates a GestureStroke from the GesturePoints in points.
     * Adds this to StrokeList.
     */
    public void commit() {
        if (points.size() == 0) return;
        GestureStroke gs = new GestureStroke(points);
        gestureStrokes.add(gs);
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

        for (GestureStroke stroke : gestureStrokes) {
            RectF bounds = stroke.boundingBox;
            Path p = stroke.toPath(bounds.width(), bounds.height(), stroke.points.length);
            p.offset(bounds.left, bounds.top);
            canvas.drawPath(p, color);
        }


        if (points.size() < 1) return;
        GestureStroke current = new GestureStroke(points);
        RectF bounds = current.boundingBox;
        Path p = current.toPath(bounds.width(), bounds.height(), current.points.length);
        p.offset(bounds.left, bounds.top);
        canvas.drawPath(p, color_fresh);
    }

    public List<GestureStroke> getGestureStrokes() {
        return gestureStrokes;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_UP:
                commit();
                break;
            default:
                GesturePoint p = new GesturePoint(motionEvent.getX(), motionEvent.getY(), 0);
                points.add(p);
                invalidate();
        }
        return true;
    }

}








