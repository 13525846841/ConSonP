package com.yksj.healthtalk.utils;

import android.content.Context;

/**
 * 单位转换工具
 * Created by Mickey.Li on 2016-3-18.
 */
public class UnitUtil {
    /**
     * dp转px
     *
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
