package net.hath.drawcut;

import net.hath.drawcut.Observer;

public interface Subject {
    
    public void register(Observer o);
    public void unregister(Observer o);

}
