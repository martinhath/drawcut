package net.hath.drawcut;

import java.util.List;

public interface GestureProvider {
    public List<GestureItem> getGestures();

    public void addGesture(GestureItem g);
}
