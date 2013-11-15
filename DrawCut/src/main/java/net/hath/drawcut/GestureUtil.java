package net.hath.drawcut;

import android.gesture.Gesture;
import android.gesture.GestureStroke;
import android.graphics.*;

import java.util.ArrayList;

public class GestureUtil {
    private static final boolean BITMAP_RENDERING_ANTIALIAS = true;
    private static final boolean BITMAP_RENDERING_DITHER = true;
    private static final float BITMAP_RENDERING_WIDTH = 10;
    private static final int NUM_SAMPLES = 20;
    private static final String TAG = "GestureUtil";

    public static Bitmap toBitmap(Gesture gesture, int color, float strokeWidth) {
        RectF bounds = gesture.getBoundingBox();
        int width = (int) bounds.width();
        int height = (int) bounds.height();
        final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);

        int edge = (int) strokeWidth;
        //canvas.translate(edge, edge);

        final Paint paint = new Paint();
        paint.setAntiAlias(BITMAP_RENDERING_ANTIALIAS);
        paint.setDither(BITMAP_RENDERING_DITHER);
        paint.setColor(color);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeWidth(BITMAP_RENDERING_WIDTH);

        final ArrayList<GestureStroke> strokes = gesture.getStrokes();
        final int count = strokes.size();

        for (int i = 0; i < count; i++) {
            Path path = strokes.get(i).toPath(width - 2 * edge, height - 2 * edge, NUM_SAMPLES);
            canvas.drawPath(path, paint);
        }

        return bitmap;
    }

}