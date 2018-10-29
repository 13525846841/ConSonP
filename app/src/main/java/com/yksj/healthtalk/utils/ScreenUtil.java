package com.yksj.healthtalk.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * 屏幕参数书工具
 * Created by Mickey.Li on 2016-3-21.
 */
public class ScreenUtil {

    private int width;
    private int height;

    public ScreenUtil(Activity context) {
        DisplayMetrics metrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        width = metrics.widthPixels;
        height = metrics.heightPixels;
    }

    /**
     * @return 屏幕宽度 in pixel
     */
    public int getWidth() {
        return width;
    }

    /**
     * @return 屏幕高度 in pixel
     */
    public int getHeight() {
        return height;
    }

    @Override
    public String toString() {
        return "Screen{" +
                "width=" + width +
                ", height=" + height +
                '}';
    }
}