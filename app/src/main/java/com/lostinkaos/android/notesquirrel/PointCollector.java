package com.lostinkaos.android.notesquirrel;

import android.graphics.Point;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by keya on 24/7/15.
 */
public class PointCollector implements View.OnTouchListener {

    private List<Point> points = new ArrayList<Point>();
    private PointCollectorListener listener;

    public void setListener(PointCollectorListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        int x = Math.round(motionEvent.getX());
        int y = Math.round(motionEvent.getY());

        String message = String.format("Coordinates: (%d, %d)", x, y);

        Log.d(MainActivity.DEBUGTAG, message);

        points.add(new Point(x, y));

        if( points.size() == 4 ) {
            if( listener != null ) {
                listener.pointsCollected(points);
                points.clear();
            } else {
                Log.d(MainActivity.DEBUGTAG, "No Listener set!");
            }
        }

        return false;
    }
}
