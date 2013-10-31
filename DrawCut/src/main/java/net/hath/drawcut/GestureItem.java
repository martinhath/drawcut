package net.hath.drawcut;

import android.gesture.Gesture;

public class GestureItem {
    String name;
    Gesture gesture;

    public GestureItem(Gesture g, String n){
        gesture = g;
        name = n;
    }
}
