package net.hath.drawcut.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.FileOutputStream;

public class Utils{

    private static final String TAG = "Utils";

    public static void saveBitmapToFile(Context context, String name, Bitmap b){

        try {
            String dir = context.getFilesDir().toString();
            String fullPath = dir+String.format("/%s.png", name);
            FileOutputStream out = new FileOutputStream(fullPath);
            b.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            Log.d(TAG, "Saved to " + fullPath);
        } catch (Exception e) {
            Log.w(TAG, "Not saved. ");
            e.printStackTrace();
        }
    }

    public static Bitmap loadBitmapFromFile(Context context, String name){
        String dir = context.getFilesDir().toString();
        return BitmapFactory.decodeFile(dir+"/"+name+".png");
    }
}
