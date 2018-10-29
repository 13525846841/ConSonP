package com.yksj.healthtalk.utils;

import android.app.Activity;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;

import com.yksj.consultation.son.views.Rotate3dAnimation;
  
public class RotationHelper {  
	
	public final static int KEY_FIRST_INVERSE = 1;  
	  
    public final static int KEY_FIRST_CLOCKWISE = 2;  
  
    public final static int KEY_SECOND_INVERSE = 3;  
  
    public final static int KEY_SECOND_CLOCKWISE = 4;  
      
    DisplayNextVieUtil displayNext;  
      
    public RotationHelper(Activity con,int order){  
        displayNext = new DisplayNextVieUtil(con, order);  
    }  
      
    // ��ʱ����ת90  
    public void applyFirstRotation(ViewGroup layout,float start, float end) {  
        // Find the center of the container  
        final float centerX = layout.getWidth() / 2.0f;  
        final float centerY = layout.getHeight() / 2.0f;  
  
        // Create a new 3D rotation with the supplied parameter  
        // The animation listener is used to trigger the next animation  
        final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,  
                centerX, centerY, 310.0f, true);  
        rotation.setDuration(700);  
        rotation.setFillAfter(true);  
        rotation.setInterpolator(new AccelerateInterpolator());  
        rotation.setAnimationListener(displayNext);  
        layout.startAnimation(rotation);  
    }  
      
    public void applyLastRotation(ViewGroup layout,float start, float end) {  
        // Find the center of the container  
        final float centerX = layout.getWidth() / 2.0f;  
        final float centerY = layout.getHeight() / 2.0f;  
  
        // Create a new 3D rotation with the supplied parameter  
        // The animation listener is used to trigger the next animation  
        final Rotate3dAnimation rotation = new Rotate3dAnimation(start, end,  
                 160, 192, 310.0f, false);  
        rotation.setDuration(700);  
        rotation.setFillAfter(true);  
        rotation.setInterpolator(new AccelerateInterpolator());  
        layout.startAnimation(rotation);  
    }     
}  

