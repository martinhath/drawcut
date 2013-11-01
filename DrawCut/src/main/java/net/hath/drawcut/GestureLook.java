package net.hath.drawcut;

import android.graphics.Paint;

public class GestureLook {
    private Paint color;

    public Paint getColor() {
        return color;
    }

    public void setColor(Paint color) {
        this.color = color;
    }

    public float getWidth() {
        return color.getStrokeWidth();
    }

    public void setWidth(float width) {
        this.color.setStrokeWidth(width);
    }
}
