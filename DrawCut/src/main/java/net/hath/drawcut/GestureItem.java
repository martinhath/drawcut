package net.hath.drawcut;

import android.gesture.Gesture;
import android.graphics.Bitmap;

public class GestureItem {
    private String name;
    private Gesture gesture;
    private Bitmap image;

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public GestureItem(Gesture g, String n) {
        gesture = g;
        name = n;
    }

    public String getName() {
        return name;
    }

    public Gesture getGesture() {
        return gesture;
    }

}
