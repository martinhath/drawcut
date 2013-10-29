package net.hath.drawcut;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class DrawingFragment extends Fragment{

    private static final String TAG = "DrawingFragment" ;
    DrawingView drawingView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.drawfragment, null);

        assert view != null;
        drawingView = (DrawingView) view.findViewById(R.id.drawsurface);

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
