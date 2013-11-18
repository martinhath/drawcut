package net.hath.drawcut.data;

import java.util.List;

public interface LaunchItemProvider {
    public List<LaunchItem> getLaunchItemList();

    public void addGesture(LaunchItem g);

    public void addSubscriber(LaunchItemSubscriber sub);
    public void removeSubscriber(LaunchItemSubscriber sub);
}
