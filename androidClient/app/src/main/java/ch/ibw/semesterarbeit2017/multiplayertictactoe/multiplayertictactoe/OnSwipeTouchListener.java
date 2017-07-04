package ch.ibw.semesterarbeit2017.multiplayertictactoe.multiplayertictactoe;

import android.app.Activity;
import android.app.TabActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

/**
 * Created by rk on 01.07.17.
 * basically copied from stackoverflow and adapted...
 */

public class OnSwipeTouchListener implements View.OnTouchListener {

    public static final String PROG = "____ONSWIPE";
    private MainActivity activity;
    static final int MIN_DISTANCE = 100;//  change this runtime based on screen resolution. for 1920x1080 is to small the 100 distance
    private float downX, downY, upX, upY;

    public OnSwipeTouchListener(MainActivity mainActivity) {
        activity = mainActivity;
    }

    public int getNextTabRight(int currentTab) {
        if (currentTab+1 >2) {
            return 0;
        }
        return currentTab +1;
    }
    public int getNextTabLeft(int currentTab) {
        if (currentTab-1 <0) {
            return 2;
        }
        return currentTab -1;
    }


    public void onRightToLeftSwipe() {
        Log.i(PROG, "RightToLeftSwipe!");
        //Toast.makeText(activity, "RightToLeftSwipe | " +activity.getTabHost().getCurrentTab(), Toast.LENGTH_SHORT).show();
        int nextTab = getNextTabRight(activity.getTabHost().getCurrentTab());
        activity.getTabHost().setCurrentTab(nextTab);
    }

    public void onLeftToRightSwipe() {
        Log.i(PROG, "LeftToRightSwipe!");
        //Toast.makeText(activity, "LeftToRightSwipe", Toast.LENGTH_SHORT).show();
        int nextTab = getNextTabLeft(activity.getTabHost().getCurrentTab());
        activity.getTabHost().setCurrentTab(nextTab);
    }


    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                downX = event.getX();
                downY = event.getY();
                return true;
            }
            case MotionEvent.ACTION_UP: {
                upX = event.getX();
                upY = event.getY();

                float deltaX = downX - upX;

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
                    Log.i(PROG, "Swipe was only " + Math.abs(deltaX) + " long horizontally, need at least " + MIN_DISTANCE);
                    // return false; // We don't consume the event
                }

                return false; // no swipe horizontally and no swipe vertically
            }// case MotionEvent.ACTION_UP:
        }
        return false;
    }

}