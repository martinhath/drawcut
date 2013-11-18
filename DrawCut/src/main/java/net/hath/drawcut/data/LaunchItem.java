package net.hath.drawcut.data;

import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

public class LaunchItem {
    private String name;
    private Gesture gesture;
    private Bitmap gestureImage;
    private ApplicationItem app;
    private long id;

    public LaunchItem(String name, Gesture gesture, ApplicationItem ai) {
        this.name = name;
        this.gesture = gesture;
        this.app = ai;
        id = ai.getPackageName().hashCode();
    }

    public Bitmap getGestureImage() {
        return gestureImage;
    }

    public void setGestureImage(Bitmap gestureImage) {
        this.gestureImage = gestureImage;
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

    public void setGesture(Gesture gesture) {
        this.gesture = gesture;
    }

    public ApplicationItem getApplicationItem() {
        return app;
    }

    public Drawable getApplicationIcon() {
        return app.getIcon();
    }

    public long getId() {
        return id;
    }

    public String getApplicationPackage() {
        return app.getPackageName();
    }
}
