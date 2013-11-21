package net.hath.drawcut.data;

import android.content.Context;
import android.gesture.Gesture;
import android.util.Log;
import net.hath.drawcut.Observer;
import net.hath.drawcut.Subject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class LaunchItemProvider implements Subject {

    private static final String TAG = "LaunchItemProvider";
    private static LaunchItemProvider instance;

    private List<Observer> observers;

    private Context context;
    private GestureLibrary gestureLibrary;
    private LaunchItemDatabaseManager databaseManager;

    private LaunchItemProvider(){}
    public static LaunchItemProvider getInstance(Context context){
        if(instance == null){
            instance = new LaunchItemProvider();
            instance.context = context;
            instance.gestureLibrary = new GestureLibrary(context);
            instance.databaseManager = new LaunchItemDatabaseManager(context);

            instance.init();
        }
        return instance;
    }

    public void init(){
        gestureLibrary.load();
        observers = new LinkedList<Observer>();
    }

    public void addLaunchItem(LaunchItem li){
        String packageName = li.getApplicationPackage();
        Gesture gesture = li.getGesture();

        gestureLibrary.addGesture(packageName, gesture);
        databaseManager.putLaunchItem(li);

        notifyObservers();
    }

    public GestureLibrary getGestureLibrary(){
        return gestureLibrary;
    }

    public List<LaunchItem> getLaunchItems(){
        List<LaunchItem> list = databaseManager.getLaunchItems();

        for (LaunchItem li : list) {
            List<Gesture> glist = gestureLibrary.getGestures(li.getApplicationPackage());
            if(glist == null){
                continue;
            }
            Gesture g = glist.get(0);
            if (g != null) {
                li.setGesture(g);
            }
        }

        return list;
    }

    public void save(){
        gestureLibrary.save();
    }

    @Override
    public void register(Observer o) {
        observers.add(o);
    }

    @Override
    public void unregister(Observer o) {
        observers.remove(o);
    }

    private void notifyObservers() {
        for(Observer o:observers){
            o.update();
        }
    }

}
