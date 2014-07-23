
package com.brightr.listeners;

import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

public abstract class OnFlingGestureListener implements OnTouchListener {

    private final GestureDetector gdt = new GestureDetector(new GestureListener());
    private int                   view_position;

    public boolean onTouch(final View v, final MotionEvent event) {
        gdt.onTouchEvent(event);
       // v.onTouchEvent(event);
        return false;
    }

    private final class GestureListener extends SimpleOnGestureListener implements OnItemClickListener {

        private static final int SWIPE_MIN_DISTANCE       = 60;
        private static final int SWIPE_THRESHOLD_VELOCITY = 100;

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                onRightToLeft();
                return true;
            } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
                onLeftToRight();
                return true;
            }
            if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                onBottomToTop();
                return true;
            } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
                onTopToBottom();
                return true;
            }

            return false;
        }

        
       /* public boolean onDown(MotionEvent e) {

            view_position = e.getSource();
            // onItemSelected();

            return false;
        }*/
        
        

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            
           // view_position = e.getSource();
            
            onItemSelected(view_position);
           
            return true;
        }


        public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
            // TODO Auto-generated method stub
            
        }

       

    }

    public abstract void onItemSelected(int pos);

    public abstract void onRightToLeft();

    public abstract void onLeftToRight();

    public abstract void onBottomToTop();

    public abstract void onTopToBottom();

}
