package net.hath.drawcut;

import android.app.Activity;
import android.gesture.Gesture;
import android.gesture.GestureStroke;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class NewGestureActivity extends Activity {

    TextView header;
    DrawingView drawingView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_gesture);


        drawingView = (DrawingView) findViewById(R.id.drawsurface);


        header = (TextView) findViewById(R.id.header);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto-li.ttf");
        assert font != null;
        header.setTypeface(font);


        //noinspection ConstantConditions
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();

        Gesture g = new Gesture();
        for(GestureStroke gs: drawingView.strokes){
            g.addStroke(gs);
        }

    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.new_gesture, menu);

        menu.findItem(R.id.action_clear).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                drawingView.clear();
                return false;
            }
        });
        return true;
    }
}