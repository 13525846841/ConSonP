package com.yksj.healthtalk.utils;

import android.app.Activity;
import android.view.animation.Animation;
  
public class DisplayNextVieUtil implements Animation.AnimationListener {  
  //翻转 暂时没用
    Object obj;  
    Activity ac;  
    int order;  
  
    public DisplayNextVieUtil(Activity ac, int order) {  
        this.ac = ac;  
        this.order = order;  
    }  
  
    public void onAnimationStart(Animation animation) {  
    }  
  
    public void onAnimationEnd(Animation animation) {  
        doSomethingOnEnd(order);  
    }  
  
    public void onAnimationRepeat(Animation animation) {  
    }  
  
    private final class SwapViews implements Runnable {  
        public void run() {   
            switch (order) {  
            case RotationHelper.KEY_FIRST_INVERSE:  
//                ((FriendNearbyActivity) ac).showRotation();  
                break;  
            case RotationHelper.KEY_SECOND_CLOCKWISE:  
//                ((FriendBaiDuMapActivity) ac).showRotation();  
                break;  
            }  
        }  
    }  
  
    public void doSomethingOnEnd(int _order) {  
        switch (_order) {  
        case RotationHelper.KEY_FIRST_INVERSE:  
//            ((FriendNearbyActivity) ac).near_layout.post(new SwapViews());  
            break;  
  
        case RotationHelper.KEY_SECOND_CLOCKWISE:  
//            ((FriendBaiDuMapActivity) ac).map_layout.post(new SwapViews());  
            break;  
        }  
    }  
}  

