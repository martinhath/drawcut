package com.mthoresen.drawcut.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

public class SquareView extends View {

    public SquareView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int mheight = getMeasuredHeight();
        int mwidth = getMeasuredWidth();
        if (mheight == 0) {
            setMeasuredDimension(mwidth, mwidth);
        } else if (mwidth == 0) {
            setMeasuredDimension(mheight, mheight);
        } else {
            int minInt = Math.min(mheight, mwidth);
            setMeasuredDimension(minInt, minInt);
        }
    }
}
