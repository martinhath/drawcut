package net.hath.drawcut.data;

import android.content.Context;
import android.gesture.Gesture;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class LaunchItemProvider {

    private static final String TAG = "LaunchItemProvider";
    private static LaunchItemProvider instance;

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
    }

    public void addLaunchItem(LaunchItem li){
        String packageName = li.getApplicationPackage();
        Gesture gesture = li.getGesture();

        gestureLibrary.addGesture(packageName, gesture);
        databaseManager.putLaunchItem(li);
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

}
