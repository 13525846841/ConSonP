package com.yksj.healthtalk.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.animation.Animation;

import java.io.InputStream;


/**
 * 动画 工具类
 * @author jack_tang
 */
public class AnimaUtils {

    private static  Animation animationGone ;
    private static  Animation animationVisi ;
  public static  AnimationDrawable readBitMap(Context context, AnimationDrawable anim,int[] resId){
     for(int i = 0; i < resId.length; i++){
	       BitmapFactory.Options opt = new BitmapFactory.Options();
	       opt.inPreferredConfig = Bitmap.Config.RGB_565; 
	       opt.inPurgeable = true; 
	       opt.inInputShareable = true; 
	       // 获取资源图片 
	       InputStream is = context.getResources().openRawResource(resId[i]); 
	       anim.addFrame(new BitmapDrawable(BitmapFactory.decodeStream(is,null,opt)), 200);
     }
     anim.setOneShot(true);
     return anim;
  }



    public  static void viewUpGone(View v){
//        animationGone=null;
//        animationGone = android.view.animation.AnimationUtils.loadAnimation(HApplication.getApplication(), R.anim.anim_view_up_gone);
        v.setVisibility(View.GONE);
//        v.setAnimation(animationGone);

    }

    public  static void viewDownVisitly(View v){
//        animationVisi=null;
//        animationVisi = android.view.animation.AnimationUtils.loadAnimation(HApplication.getApplication(), R.anim.anim_view_down_visiblty);
        v.setVisibility(View.VISIBLE);
//        v.setAnimation(animationVisi);
    }

}
