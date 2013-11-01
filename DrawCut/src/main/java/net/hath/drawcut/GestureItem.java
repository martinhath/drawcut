package net.hath.drawcut;

import android.gesture.Gesture;

public class GestureItem {
    private String name;
    private Gesture gesture;

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
