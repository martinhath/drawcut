package net.hath.drawcut.ui.activitiy;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.gesture.Gesture;
import android.gesture.GestureStroke;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import net.hath.drawcut.ui.fragment.ApplicationPickerDialog;
import net.hath.drawcut.view.DrawingView;
import net.hath.drawcut.R;

public class NewGestureActivity extends Activity {

    private static final String TAG = "NewGestureActivity";
    TextView header;
    DrawingView drawingView;
    EditText formName;
    ImageView appicon;
    Intent intent;
    Gesture gesture;
    ApplicationInfo application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.create_gesture);
        application = null;


        drawingView = (DrawingView) findViewById(R.id.drawsurface);
        formName = (EditText) findViewById(R.id.nameForm);

        appicon = (ImageView) findViewById(R.id.icon);


        header = (TextView) findViewById(R.id.header);
        TextView subheader = (TextView) findViewById(R.id.subheader);

        Typeface font = Typeface.createFromAsset(getAssets(), "fonts/roboto-li.ttf");
        assert font != null;
        header.setTypeface(font);
        subheader.setTypeface(font);

        appicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogFragment dialogFragment = new ApplicationPickerDialog();
                dialogFragment.show(getFragmentManager(), "123");
            }
        });

        //noinspection ConstantConditions
        getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void saveGesture() {

        Gesture g = new Gesture();
        for (GestureStroke gs : drawingView.getGestureStrokes()) {
            g.addStroke(gs);
        }
        gesture = g;

        if (gesture.getStrokesCount() == 0) {
            setResult(RESULT_CANCELED);
            return;
        }


        Intent data = new Intent();

        data.putExtra("gesture", gesture);

        String name = formName.getText().toString();

        //String appName = formAppName.getText().toString();
        // TODO: Remove when launch
        name = name.equals("") ? String.valueOf(formName.hashCode()) : name;
        // END_TODO

        data.putExtra("name", name);
        data.putExtra("applicationinfo", application);

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

    public void setSelectedActivity(ApplicationInfo applicationInfo) {
        application = applicationInfo;
        PackageManager pm = getPackageManager();
        formName.setText(pm.getApplicationLabel(application));
        Drawable d = pm.getApplicationIcon(applicationInfo);
        appicon.setImageDrawable(d);
    }
}