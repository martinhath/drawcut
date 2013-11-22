package net.hath.drawcut.data;

import android.content.Context;
import android.gesture.Gesture;
import android.gesture.GestureStore;
import android.gesture.Prediction;
import android.util.Log;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class GestureLibrary extends GestureStore {
    private static final String TAG = "GestureLibrary";
    private static String filename = "gestures";
    private static final float GESTURE_THRESHOLD = 3f;

    private Context context;
    private boolean isLoaded = false;

    public GestureLibrary(Context context) {
        this.context = context;
    }

    public String getBestPredictionName(Gesture g) {
        return getBestPredictionName(g, GESTURE_THRESHOLD);
    }

    public String getBestPredictionName(Gesture g, float threshold) {
        ArrayList<Prediction> predictions = super.recognize(g);
        if (predictions != null && predictions.size() > 0) {
            for (Prediction p : predictions) {
                Log.d(TAG, p.name + " " + p.score);
            }
            Prediction p = predictions.get(0);
            double score = p.score;
            if (Double.isNaN(score)) score = 0;
            Log.w(TAG, String.format("Score: %f(%f)", score, p.score));
            if (score < threshold) {
                return null;
            }
            return predictions.get(0).name;
        }
        Log.w(TAG, "No match found.");
        return null;
    }

    @Override
    public void addGesture(String entryName, Gesture gesture) {
        super.addGesture(entryName, gesture);
        isLoaded = false;
    }

    public void load() {
        try {
            InputStream in = context.openFileInput(filename);
            super.load(in);
            isLoaded = true;
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

    public boolean isLoaded() {
        return isLoaded;
    }

}
