package net.hath.drawcut;

import java.util.List;

public interface GestureProvider {
    public List<GestureItem> getGestures();

    public void addGesture(GestureItem g);

    public void addSubscriber(GestureSubscriber sub);
    public void removeSubscriber(GestureSubscriber sub);
}
