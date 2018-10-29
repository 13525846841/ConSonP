//package com.yksj.healthtalk.utils;
//
//import android.graphics.drawable.BitmapDrawable;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.PopupWindow;
//
//import com.yksj.consultation.son.R;
//
//
///**
// * popwindow工具
// * Created by Mickey.Li on 2016-3-17.
// */
//public class PopUtil {
//
//    public static void showPop(final Window window, PopupWindow pop, View view, int way) {
//        if (way == ChatActivityB.TITLE_SETTINT || way == ChatActivityB.TITLE_CHANGE || way == ChatActivityB.LOGIN_OUT || way == ChatActivityB.TAKE_PHOTO) {
//            pop.setOnDismissListener(new PopupWindow.OnDismissListener() {
//                @Override
//                public void onDismiss() {
//                    WindowManager.LayoutParams lp = window.getAttributes();
//                    lp.alpha = 1f;
//                    window.setAttributes(lp);
//                }
//            });
//            WindowManager.LayoutParams lp = window.getAttributes();
//            lp.alpha = 0.7f;
//            window.setAttributes(lp);
//            if (way == ChatActivityB.TITLE_SETTINT || way == ChatActivityB.TITLE_CHANGE) {
//                pop.setBackgroundDrawable(new BitmapDrawable());
//                pop.setFocusable(true);
//                pop.setTouchable(true);
//                pop.setOutsideTouchable(true);
//                pop.setAnimationStyle(R.style.PopTopShow);
//                pop.showAsDropDown(view);
//            }
//        }
//    }
//}
