package net.hath.drawcut;

import android.app.Fragment;
import android.graphics.Point;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;

import java.util.LinkedList;
import java.util.List;


public class DrawingFragment extends Fragment{

    DrawingView drawingView = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.drawfragment, null);
        drawingView = (DrawingView) view.findViewById(R.id.drawsurface);

        return view;
    }



}
