package com.yksj.healthtalk.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;

/**
 * 此类用于所有的toast提示
 * @author Administrator
 *
 */


public class ToastUtil {
	
	private static final  int TShortTime = 800;
	
	
	private ToastUtil() {
		
	}
	/**
	 * 最基本的short toast提示
	 * @param context
	 * @param content
	 */
	public static void  showBasicShortToast(Context context,String content){
		Toast toast = Toast.makeText(context, content, TShortTime);
		toast.show();
	}
	// 默认的文字提示 请稍后重试
	public static void  showBasicErrorShortToast(Context context){
		Toast toast = Toast.makeText(context, "网络失败,请稍后重试", TShortTime);
		toast.show();
	}
	
	public static void showGetWayError(Context context){
		showLong(context,R.string.getway_error_note);
	}
	
	public static void showLoginOutError(Context context){
		showLong(context,R.string.login_out_error_note);
	}
	
	public static void onShow(Context context,int strid,int duration){
		Toast toast = Toast.makeText(context,context.getString(strid),duration);
		toast.show();
	}
	
	public static void onShow(Context context,String str,int duration){
		Toast toast = Toast.makeText(context,str,duration);
		toast.show();
	}
	
	public static void showShort(Context context,int strid){
		onShow(context, strid,TShortTime);
	}
	
	public static void showShort(Context context,String str){
		onShow(context, str,TShortTime);
	}
	
	public static void showShort(String str){
		showShort(HTalkApplication.getApplication(), str);
	}
	
	public static void showLong(Context context,int strid){
		onShow(context, strid,Toast.LENGTH_LONG);
	}
	
 	public static void showLong(Context context,String content){
		Toast toast = Toast.makeText(context,content,Toast.LENGTH_LONG);
		toast.show();
	}
	
	public static void showStorageUninToast(Context context){
//		showShort(context,R.string.sd_uninstall);
	}
	
	public static void showSDCardBusy(){
		showShort("内存卡暂无法使用");
	}
	
	public static void showCreateFail(){
		showShort("文件创建失败");
	}
	
	public static void showGetImageFail(){
		showShort("图片获取失败");
	}
	
	public static void showToastPanl(String str){
		Toast toast = new Toast(HTalkApplication.getApplication());
		LayoutInflater inflater = LayoutInflater.from(HTalkApplication.getHTalkApplication());
		View view = inflater.inflate(R.layout.toast_dialog_layout,null);
		TextView textView = (TextView)view.findViewById(R.id.loadingTxt);
		textView.setText(str);
		toast.setGravity(Gravity.CENTER,0,0);
		toast.setView(view);
		toast.setDuration(TShortTime);
		toast.show();
	}
	public static void showToastPanlPro(String str){//为了多行显示
		Toast toast = new Toast(HTalkApplication.getApplication());
		LayoutInflater inflater = LayoutInflater.from(HTalkApplication.getHTalkApplication());
		View view = inflater.inflate(R.layout.toast_dialog_2,null);
		TextView textView = (TextView)view.findViewById(R.id.loadingTxt);
		textView.setText(str);
		toast.setGravity(Gravity.CENTER,0,0);
		toast.setView(view);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.show();
	}
	
}
