package com.yksj.consultation.son.app;

import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

/**
 * @author zhao
 *
 */
public class AppTools {

	/**
	 * 获得屏幕的尺寸
	 * @param context
	 * @return
	 */
	public static DisplayMetrics getWindowSize(Activity context){
		DisplayMetrics dm = new DisplayMetrics();
		context.getWindowManager().getDefaultDisplay().getMetrics(dm);
		return dm ;
	}

	public static String getDeviceId(Context context){
		TelephonyManager telephonemanage = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		return telephonemanage.getDeviceId();
	}
	
}
