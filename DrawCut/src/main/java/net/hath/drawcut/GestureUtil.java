package net.hath.drawcut;
/*
 * Copyright (C) 2008-2009 The Android Open Source Project
 * Copyright (C) 2012 Régis Décamps
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import android.gesture.Gesture;
import android.gesture.GestureStroke;
import android.graphics.*;

import java.util.ArrayList;

public class GestureUtil {
    private static final boolean BITMAP_RENDERING_ANTIALIAS = true;
    private static final boolean BITMAP_RENDERING_DITHER = true;
    private static final float BITMAP_RENDERING_WIDTH = 10;
    private static final int NUM_SAMPLES = 20;

    public static Bitmap toBitmap(Gesture gesture, int color, float strokeWidth) {
        RectF bounds = gesture.getBoundingBox();
        int width = (int) bounds.width();
        int height = (int) bounds.height();
        final Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);

        int edge = 0;
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