package net.hath.drawcut;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class DrawingView extends View implements View.OnTouchListener {
    @SuppressWarnings("UnusedDeclaration")
    private static final String TAG = "DrawingView";
    private static final float STROKE_WIDTH = 10;
    public static final int RADIUS = (int) STROKE_WIDTH / 2;

    private Context context;

    private Paint paint;
    private Paint paint_circle;

    List<Point> points = new ArrayList<Point>();

    @SuppressWarnings("UnusedDeclaration")
    public DrawingView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    @SuppressWarnings("UnusedDeclaration")
    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    private void init() {
        if (paint != null) return;
        setOnTouchListener(this);
        setFocusable(true);
        setFocusableInTouchMode(true);

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(context.getResources().getColor(R.color.drawing_color));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);

        paint_circle = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_circle.setColor(context.getResources().getColor(R.color.drawing_color));
    }

    public void clear() {
        points.clear();
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (points.size() <= 1) return;
        Point p0;
        Point p1 = points.get(0);
        for (int i = 1; i < points.size(); i++) {
            p0 = p1;
            p1 = points.get(i);
            // p1 == null means the user has released, and is drawing again.
            // which means that we shouldn't draw a line
            if(p0 == null || p1==null){
                continue;
            }
            canvas.drawLine(p0.x, p0.y, p1.x, p1.y, paint);
            canvas.drawCircle(p0.x, p0.y, RADIUS, paint_circle);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN){
            points.add(null);
        }
        Point p = new Point(motionEvent.getX(), motionEvent.getY());
        points.add(p);
        invalidate();
        return true;
    }

    private class Point {
        public float x;
        public float y;

        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
    }
}








