package com.mthoresen.drawcut.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.gesture.Gesture;
import android.graphics.Bitmap;

import com.mthoresen.drawcut.Observer;
import com.mthoresen.drawcut.Subject;
import com.mthoresen.drawcut.util.GestureUtil;
import com.mthoresen.drawcut.util.Utils;

import java.util.LinkedList;
import java.util.List;

public class LaunchItemProvider implements Subject {

    private static final String TAG = "LaunchItemProvider";
    private static final float BITMAP_RATIO = 0.5F;
    private static LaunchItemProvider instance;
    private List<Observer> observers;
    private Context context;
    private GestureLibrary gestureLibrary;
    private LaunchItemDatabaseManager databaseManager;

    private LaunchItemProvider() {
    }

    public static LaunchItemProvider getInstance(Context context) {
        if (instance == null) {
            instance = new LaunchItemProvider();
            instance.context = context;
            instance.gestureLibrary = new GestureLibrary(context);
            instance.databaseManager = new LaunchItemDatabaseManager(context);

            instance.init();
        }
        return instance;
    }

    private void init() {
        gestureLibrary.load();
        observers = new LinkedList<Observer>();
    }

    public void addLaunchItem(LaunchItem li) {
        String packageName = li.getApplicationPackage();
        Gesture gesture = li.getGesture();

        gestureLibrary.addGesture(packageName, gesture);
        databaseManager.putLaunchItem(li);

        SharedPreferences prefs = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        Bitmap b = GestureUtil.toBitmap(gesture, prefs.getInt("gestureColor", 0), prefs.getFloat("gestureStrokeWidth", 1), BITMAP_RATIO);
        li.setGestureImage(b);
        Utils.saveBitmapToFile(context, "" + li.getId(), b);

        notifyObservers();
    }

    public void removeLaunchItem(LaunchItem li) {
        String packageName = li.getApplicationPackage();
        gestureLibrary.removeEntry(packageName);
        databaseManager.removeLaunchItem(li);

        Utils.deleteBitmap(context, "" + li.getId());

        notifyObservers();
    }

    public GestureLibrary getGestureLibrary() {
        return gestureLibrary;
    }

    public List<LaunchItem> getLaunchItems() {
        List<LaunchItem> list = databaseManager.getLaunchItems();

        for (LaunchItem li : list) {
            List<Gesture> glist = gestureLibrary.getGestures(li.getApplicationPackage());
            if (glist == null) {
                continue;
            }
            Gesture g = glist.get(0);
            if (g != null) {
                li.setGesture(g);
            }
        }

        return list;
    }

    public void save() {
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
        for (Observer o : observers) {
            o.update();
        }
    }

}
