package net.hath.drawcut;

import android.app.Fragment;
import android.gesture.Gesture;
import android.gesture.GestureStore;
import android.gesture.Prediction;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.io.FileOutputStream;
import java.util.ArrayList;


public class DrawingFragment extends Fragment{

    private static final String TAG = "DrawingFragment" ;
    DrawingView drawingView = null;

    GestureStore gl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        gl = new GestureStore();

        View view = inflater.inflate(R.layout.drawfragment, null);

        assert view != null;
        drawingView = (DrawingView) view.findViewById(R.id.drawsurface);


        Button b = (Button) view.findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            int c=0;
            @Override
            public void onClick(View view) {
                drawingView.commit();
                Gesture g = drawingView.makeGesture(drawingView.strokes);
                Bitmap b = g.toBitmap(198, 198, 0, Color.WHITE);

                gl.addGesture(c++ + "", g);

                drawingView.clear();

                try {
                    String path = Environment.getExternalStorageDirectory().toString();
                    FileOutputStream out = new FileOutputStream(path+"/test.png");
                    b.compress(Bitmap.CompressFormat.PNG, 90, out);
                    out.close();
                    Log.d(TAG, "Saved to "+path+"/test.png");
                } catch (Exception e) {
                    Log.w(TAG, "Not saved. ");
                    e.printStackTrace();
                }
            }
        });

        Button b2 = (Button) view.findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawingView.commit();
                Gesture g = drawingView.makeGesture(drawingView.strokes);

                ArrayList<Prediction> preds = gl.recognize(g);
                Log.d(TAG, "Gjettinger:");
                for(Prediction p:preds){
                    Log.d(TAG, p.toString());
                }
                Log.d(TAG, "Gjettinger_END:");

                drawingView.clear();
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        Log.d(TAG, "Menu setup");
        //noinspection ConstantConditions
        menu.findItem(R.id.action_clear).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                drawingView.clear();
                return false;
            }
        });
    }

}
