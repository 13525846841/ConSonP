package com.yksj.healthtalk.utils;

import android.util.Log;

import com.yksj.healthtalk.net.http.RequestParams;
/**
 * 
 * 日志工具类
 * @author zhao
 * @version 4.1
 */
public class LogUtil {
	public static final boolean DEBUG = true;
	
	public static String makeLogTag(Class<Object> cls){
		return "CONSULTATION_"+cls.getSimpleName();
	} 
	

	public static void d(String TAG,String str1,RequestParams params){
		if(DEBUG&&params!=null){
			Log.d(TAG,str1+"?"+params.toString());
		}

	}
	public static void d(String TAG,String msg){
		if(DEBUG){
			Log.d(TAG, msg);
		}
	}
	
	public static void d(String tag,String msg,Throwable tr){
		if(DEBUG){
			Log.d(tag, msg, tr);
		}
	}
	
	public static void e(String tag,String msg){
		if(DEBUG){
			Log.e(tag, msg);
		}
	}
	
	public static void e(String tag,String msg,Throwable tr){
		if(DEBUG){
			Log.e(tag, msg, tr);
		}
	}
	
	public static void i(String tag,String msg){
		if(DEBUG){
			Log.i(tag, msg);
		}
	}
	
	public static void i(String tag,String msg,Throwable tr){
		if(DEBUG){
			Log.i(tag, msg, tr);
		}
	}
	
	public static void v(String tag,String msg){
		if(DEBUG){
			Log.v(tag, msg);
		}
	}
	
	public static void v(String tag,String msg,Throwable tr){
		if(DEBUG){
			Log.v(tag, msg, tr);
		}
	}
	
	
	public static void w(String tag,String msg){
		if(DEBUG){
			Log.w(tag,msg);
		}
	}
	
	
	public static void w(String tag,Throwable tr){
		if(DEBUG){
			Log.w(tag, tr);
		}
	}
	
	public static void w(String tag,String msg,Throwable tr){
		if(DEBUG){
			Log.w(tag, msg, tr);
		}
	}
	
}
