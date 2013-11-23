package net.hath.drawcut.view;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;
import net.hath.drawcut.R;

public class LauncherView extends RelativeLayout {

    private DrawingView drawingView;
    private TextView title;
    private TextView desc;


    public LauncherView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.launcher, this, true);

        drawingView = (DrawingView) findViewById(R.id.drawing);
        title = (TextView) findViewById(R.id.title);
        desc = (TextView) findViewById(R.id.desc);


        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/roboto-li.ttf");
        title.setTypeface(typeface);
        desc.setTypeface(typeface);
    }

    public void setDescription(String text){
        desc.setText(text);
    }

    public DrawingView getDrawingView(){
        return drawingView;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME){

        }
        return super.onKeyDown(keyCode, event);
    }
}
