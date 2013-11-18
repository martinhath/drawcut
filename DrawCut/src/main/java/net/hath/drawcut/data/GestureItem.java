package net.hath.drawcut.data;

import android.gesture.Gesture;
import android.graphics.Bitmap;

public class GestureItem {
    private String name;
    private Gesture gesture;
    private Bitmap image;
    private ApplicationItem app;

    public GestureItem(Gesture g, String n) {
        gesture = g;
        name = n;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Gesture getGesture() {
        return gesture;
    }

    public ApplicationItem getApp() {
        return app;
    }

    public void setApplicationItem(ApplicationItem ai) {
        app = ai;
    }
}
