package net.hath.drawcut.util;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import java.io.FileOutputStream;

public class Utils{

    private static final String TAG = "Utils";

    public static void saveBitmapToFile(String name, Bitmap b){
        try {
            String path = Environment.getExternalStorageDirectory().toString();
            String fullPath = path+String.format("/%s.png", name);
            FileOutputStream out = new FileOutputStream(fullPath);
            b.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            Log.d(TAG, "Saved to " + fullPath);
        } catch (Exception e) {
            Log.w(TAG, "Not saved. ");
            e.printStackTrace();
        }
    }
}
