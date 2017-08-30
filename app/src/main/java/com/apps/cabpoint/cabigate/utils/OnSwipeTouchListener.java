package com.apps.cabpoint.cabigate.utils;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import com.apps.cabpoint.cabigate.R;

/**
 * Created by Abdul Ghani on 8/17/2017.
 */

public class OnSwipeTouchListener implements View.OnTouchListener {

    private static final String TAG = "ActivitySwipeDetector";
    private int MIN_DISTANCE = 100; // TODO change this runtime based on screen resolution. for 1920x1080 is to small the 100 distance
    private float downX, downY, upX, upY;
    private boolean isDoubleTap = false;

    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            isDoubleTap = false;
        }
    };

    public OnSwipeTouchListener(Context context) {
        MIN_DISTANCE = context.getResources().getDimensionPixelOffset(R.dimen._25sdp);
    }

    public void onRightToLeftSwipe() {
    }

    public void onLeftToRightSwipe() {
    }

    public void onDoubleTap() {
    }

    public void onSingleTap() {
    }

    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                onActionDown();
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;
                float deltaY = downY - upY;

                // swipe horizontal?
                if (Math.abs(deltaX) > MIN_DISTANCE) {
                    // left or right
                    if (deltaX < 0) {

                        this.onLeftToRightSwipe();
                        return true;
                    }
                    if (deltaX > 0) {
                        this.onRightToLeftSwipe();
                        return true;
                    }
                } else {
                    Log.i(TAG, "Swipe was only " + Math.abs(deltaX) + " long horizontally, need at least " + MIN_DISTANCE);
                    // return false; // We don't consume the event
                }

                // swipe vertical?
                if (Math.abs(deltaY) > MIN_DISTANCE) {
                    // top or down
                    if (deltaY < 0) {
                        return true;
                    }
                    if (deltaY > 0) {
                        return true;
                    }
                } else {
                    Log.i(TAG, "Swipe was only " + Math.abs(deltaX) + " long vertically, need at least " + MIN_DISTANCE);
                    // return false; // We don't consume the event
                }

                return false; // no swipe horizontally and no swipe vertically
            }// case MotionEvent.ACTION_UP:
        }
        return false;
    }

    private void onActionDown(){
        if(!isDoubleTap){
            isDoubleTap = true;
            handler.postDelayed(runnable,300);
        }else {
            this.onDoubleTap();
            isDoubleTap = false;
        }
    }
}
