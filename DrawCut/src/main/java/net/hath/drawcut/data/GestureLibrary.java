package net.hath.drawcut.data;

import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureStore;
import android.gesture.Prediction;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class GestureLibrary extends GestureStore {
    private static GestureLibrary instance;
    private static Context context;
    private static String filename = "gestures";

    public static GestureLibrary getInstance(Context context) {
        if (instance == null) {
            instance = new GestureLibrary();
            GestureLibrary.context = context;
        }
        return instance;
    }

    public String getBestPredictionName(Gesture g) {
        ArrayList<Prediction> predictions = super.recognize(g);
        if (predictions != null && predictions.size() > 0) {
            return predictions.get(0).name;
        }
        return null;
    }

    public void load() {
        try {
            InputStream in = context.openFileInput(filename);
            super.load(in);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        try {
            OutputStream out = context.openFileOutput(filename, Context.MODE_PRIVATE);
            super.save(out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
