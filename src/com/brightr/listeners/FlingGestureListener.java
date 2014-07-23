package com.brightr.listeners;

import android.util.Log;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.widget.Toast;

public class FlingGestureListener extends SimpleOnGestureListener {
	
	// these constants are used for onFling
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
	
	@Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {

        if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
            return false;
        }
        // right to left swipe
        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
           // Toast.makeText(getApplicationContext(), "right_left", Toast.LENGTH_LONG).show();
            Log.i("MyGestureDetector", "R2L!");
           
            // left to right swipe
        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            //Toast.makeText(getApplicationContext(), "left_right", Toast.LENGTH_LONG).show();
            Log.i("MyGestureDetector", "L2R!");
            
        }

        return false;
    }

    // Return true from onDown for the onFling event to register
    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

}
