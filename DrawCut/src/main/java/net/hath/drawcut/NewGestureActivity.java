package net.hath.drawcut;

import android.app.Activity;
import android.content.Intent;
import android.gesture.Gesture;
import android.gesture.GestureStroke;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class NewGestureActivity extends Activity {

    private static final String TAG = "NewGestureActivity";
    TextView header;
    DrawingView drawingView;
    EditText formName;

    Button button;
    Intent intent;

    Gesture gesture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_gesture);


        drawingView = (DrawingView) findViewById(R.id.drawsurface);
        formName = (EditText) findViewById(R.id.nameForm);


        header = (TextView) findViewById(R.id.header);
        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto-li.ttf");
        assert font != null;
        header.setTypeface(font);

        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(Intent.ACTION_MAIN);
            }
        });

                //noinspection ConstantConditions
                getActionBar().setDisplayHomeAsUpEnabled(true);
    }


    public void saveGesture(){
        Gesture g = new Gesture();
        for(GestureStroke gs: drawingView.gestureStrokes){
            g.addStroke(gs);
        }
        gesture = g;
        // Prepares the gesture for returning to prev. activity.
        Intent data = new Intent();

        data.putExtra("gesture", gesture);

        String name = formName.getText().toString();
        //String appName = formAppName.getText().toString();
        // TODO: Remove when launch
        name = name.equals("")?String.valueOf(formName.hashCode()):name;
        // END_TODO

        data.putExtra("name", name);
        data.putExtra("intent", intent);


        setResult(RESULT_OK, data);
        setResult(RESULT_OK, data);
    }

    @Override
    public void finish() {
        saveGesture();
        super.finish();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}