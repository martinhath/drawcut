package net.hath.drawcut;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.LinkedList;
import java.util.List;


public class DrawingView extends View implements View.OnTouchListener {
    private static final String TAG = "DrawingView";
    public static final int RADIUS = 10;

    private Context context;

    private Paint paint = new Paint();

    List<Point> points = new LinkedList<Point>();

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

    private void init(){
        setOnTouchListener(this);
        setFocusable(true);
        setFocusableInTouchMode(true);

    }

    public void clear(){
        points.clear();
        invalidate();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        paint.setColor(context.getResources().getColor(R.color.drawing_color));
        Log.d(TAG, "Color is "+paint.getColor());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.d(TAG, "Drawing!");
        super.onDraw(canvas);
        for(Point p: points){
            canvas.drawCircle(p.x, p.y, RADIUS, paint);
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        Log.d(TAG, String.format("%d, %d", (int) motionEvent.getX(), (int) motionEvent.getY()));
        Point p = new Point();
        p.x = (int) motionEvent.getX();
        p.y = (int) motionEvent.getY();
        points.add(p);
        invalidate();
        return true;
    }
}
