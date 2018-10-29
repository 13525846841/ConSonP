package com.yksj.consultation.son.views;

import android.content.Context;
import android.view.animation.Interpolator;
import android.widget.Scroller;

/**
 * 为了控制viewpager 切换速度
 */
public class CustomScrollDurationView extends Scroller {
    private int mDuration = 500;

    public CustomScrollDurationView(Context context) {
        super(context);
    }
    public CustomScrollDurationView(Context context, Interpolator interpolator) {
        super(context, interpolator);
    }


    @Override
    public void startScroll(int startX, int startY, int dx, int dy, int duration) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        // Ignore received duration, use fixed one instead
        super.startScroll(startX, startY, dx, dy, mDuration);
    }
    public void setmDuration(int time){
        mDuration = time;
    }
    public int getmDuration(){
        return mDuration;
    }

}
