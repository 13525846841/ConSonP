package com.yksj.healthtalk.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Process;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.login.LoginOutDialogActivity;
import com.yksj.consultation.son.login.UserLoginActivity;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.socket.SmartFoxClient;

/**
 * 系统所有的dialog面板
 * @author Administrator
 *
 */
public class DialogUtils {

	/**
	 * 默认系统加载 
	 * @param context
	 * @param content
	 * @return
	 */
	public static Dialog getLoadingDialog(Context context,String content){
		Dialog dialog = new Dialog(context,R.style.translucent_dialog);
		dialog.setContentView(R.layout.loading_dialog_layout);
		TextView textView = (TextView)dialog.findViewById(R.id.loadingTxt);
		textView.setText(content);
		return dialog;
	}

	/**
	 * 多美术测试
	 * @return
	 */
	public static Dialog showResultDialog(Context context,String value) {
		final Dialog dia = new Dialog(context, R.style.translucent_dialog);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog, null);
		view.findViewById(R.id.dialog_title).setVisibility(View.GONE);
		Button button = (Button) view.findViewById(R.id.dialog_btn);
		((TextView)view.findViewById(R.id.dialog_text)).setText(value);
		button.setText(R.string.read_finish);

		DisplayMetrics display = context.getResources().getDisplayMetrics();
		int width = display.widthPixels - 50;
		int height = LayoutParams.WRAP_CONTENT;
		dia.setCancelable(true);
		dia.setContentView(view, new LayoutParams(width, height));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dia.dismiss();
				//				dia.cancel();
			}
		});
		return dia;
	}

	/**
	 * 系统错误dialog
	 * @param context
	 * @param title 标题
	 * @param str 错误提示内容
	 * @return dialog
	 */
	public static Dialog getErrorDialog(Context context,String title,String content){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setNeutralButton(context.getString(R.string.ok_button), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		return builder.create();
	}

	/**
	 * 系统等待 
	 * @param context
	 * @param content
	 * @return
	 */
	public static Dialog getWaitingDialog(Context context,String content){
		Dialog dialog = new Dialog(context,R.style.translucent_dialog);
		dialog.setContentView(R.layout.loading_dialog_layout);
		TextView textView = (TextView)dialog.findViewById(R.id.loadingTxt);
		textView.setText(content);
		return dialog;
	}

	/**
	 * 修改成功dialog,确定后返回
	 * @param context
	 * @param title 标题
	 * @param content 提示内容
	 * @return dialog
	 */
	public static Dialog changeSuccessDialog(final Activity activity,Context context,String title,String content){
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setNeutralButton(context.getString(R.string.ok_button), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (activity != null) {
					activity.onBackPressed();
				}
			}
		});
		return builder.create();
	}

	/**
	 * 修改成功dialog
	 * @param context
	 * @param title 标题
	 * @param content 提示内容
	 * @return dialog
	 */
	public static Dialog changeSuccessDialog(final Activity activity,Context context,String title,String content,String type){
		final AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setCancelable(false);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle(title);
		builder.setMessage(content);
		builder.setNeutralButton(context.getString(R.string.ok_button), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				if (activity != null) {
					builder.create().dismiss();
				}
			}
		});
		return builder.create();
	}



//	/**
//	 * 操作提示
//	 * @return
//	 */
//	public static Dialog showNotifyDialog(Context context, int textId,OnClickListener listener){
//		final Dialog dia = new Dialog(context, R.style.translucent_dialog);
//		View view = LayoutInflater.from(context).inflate(R.layout.dialog, null);
//		Button button = (Button) view.findViewById(R.id.dialog_btn);
//
//		DisplayMetrics display = context.getResources().getDisplayMetrics();
//		int width = display.widthPixels - 50;
//		int height = LayoutParams.WRAP_CONTENT;
//		((TextView)view.findViewById(R.id.dialog_text)).setText(textId);
//		view.findViewById(R.id.dialog_cancle).setOnClickListener(listener);
//		view.findViewById(R.id.dialog_cancle).setVisibility(View.VISIBLE);
//		button.setText(R.string.sure);
//		button.setOnClickListener(listener);
//		dia.setContentView(view, new LayoutParams(width, height));
//		return dia;
//	}



	/**
	 * 后台返回无数据提醒
	 * 
	 * @return
	 */
	public static Dialog showNotifyDialog(Context context,int stringID) {

		final Dialog dia = new Dialog(context, R.style.translucent_dialog);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog, null);
		Button button = (Button) view.findViewById(R.id.dialog_btn);
		((TextView)view.findViewById(R.id.dialog_text)).setText(stringID);
		button.setText(R.string.sure);
		DisplayMetrics display = context.getResources().getDisplayMetrics();
		int width = display.widthPixels - 50;
		int height = LayoutParams.WRAP_CONTENT;
		dia.setContentView(view, new LayoutParams(width, height));
		button.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dia.dismiss();
			}
		});
		return dia;
	}

	/**
	 * 挤下线对话框
	 * @param context
	 * @return
	 */
	public static void showLoginOutDialog2(final Context context,String content){
		Intent intent = new Intent(context,LoginOutDialogActivity.class);
		intent.putExtra(LoginOutDialogActivity.CONTENT,content);
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		context.startActivity(intent);
	}

	/**
	 * 挤下线对话框
	 * @param context
	 * @return
	 */
	public static Dialog showLoginOutDialog(final Context context){
		final Dialog dialog = new Dialog(context, R.style.translucent_dialog);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog, null);
		Button button = (Button) view.findViewById(R.id.dialog_btn);
		((TextView)view.findViewById(R.id.dialog_text)).setText(R.string.login_agin);
		button.setText(R.string.sure);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		DisplayMetrics display = context.getResources().getDisplayMetrics();
		int width = display.widthPixels - 50;
		int height = LayoutParams.WRAP_CONTENT;
		dialog.setContentView(view,new LayoutParams(width, height));
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.setOnDismissListener(new android.content.DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				Intent intent = new Intent(context,UserLoginActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				context.startActivity(intent);
			}
		});
		return dialog;
	}

	/**
	 * 强制重新登录
	 * @param context
	 * @return
	 */
	public static Dialog showForceLoginDialog(final Context context){
		final Dialog dialog = new Dialog(context, R.style.translucent_dialog);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog, null);
		Button button = (Button) view.findViewById(R.id.dialog_btn);
		((TextView)view.findViewById(R.id.dialog_text)).setText(R.string.login_force);
		button.setText(R.string.sure);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		DisplayMetrics display = context.getResources().getDisplayMetrics();
		int width = display.widthPixels - 50;
		int height = LayoutParams.WRAP_CONTENT;
		dialog.setContentView(view,new LayoutParams(width, height));
		dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		dialog.setOnDismissListener(new android.content.DialogInterface.OnDismissListener() {
			@Override
			public void onDismiss(DialogInterface dialog) {
				/*context.stopService(new Intent(context,MessagePushService.class));
				Intent intent = new Intent(context,AccountAndPswActivity.class);
				intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);*/
				//				context.startActivity(intent);
				android.os.Process.killProcess(Process.myPid());
			}
		});
		return dialog;
	}
	/**
	 * titleName 名字
	 * message 内容
	 * @return
	 */
	public static AlertDialog.Builder showBasicDialog(Context mContext,String titleName,String message){
		AlertDialog.Builder builder=new Builder(mContext);
		builder.setTitle(titleName+"");
		builder.setMessage(message);
		return builder;
	}

	/**
	 * 关注的时候提示信息
	 * @param context
	 */
	public static void  PromptDialogBox(final Context context,String message,final CustomerInfoEntity entity ){
		AlertDialog.Builder mBuilder = DialogUtils.showBasicDialog(
				context, context.getString(R.string.jiankangliao),
				message);
		mBuilder.setNegativeButton(context.getString(R.string.cancel), null);
		mBuilder.setPositiveButton(context.getString(R.string.sure),
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,
					int which) {//0 没有关系 1是关注的 2是黑名单 3是客户 4医生 5 我的合作者
				int id=Integer.valueOf(SmartFoxClient.getLoginUserId());
				if (entity.getIsAttentionFriend() == 2) {//从黑名单  到 加关注
					FriendHttpUtil.requestHttpAboutFriend(id, entity, 1);
					//							FriendHttpUtil.requestHttpAboutFriend(Integer.valueOf(SmartFoxClient.getLoginUserId()),entity,5);
					//						FriendHttpUtil.showuploadPopWindow(context, entity);
				}else if(entity.getIsAttentionFriend() == 3){//现在是客户 要做取消关注操作
					FriendHttpUtil.requestHttpAboutFriend(id,entity,5);
				}else if(entity.getIsAttentionFriend() == 4){
					FriendHttpUtil.requestHttpAboutFriend(id,entity,7);
				}
			}
		});
		mBuilder.create().show();
	}

	//	/**
	//	 * 提示沙龙资料已修改
	//	 * @param context
	//	 */
	//	public static void infoChangeDialog(final Activity activity){
	//		final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	//		builder.setCancelable(false);
	//		builder.setIcon(android.R.drawable.ic_dialog_info);
	//		builder.setTitle(R.string.tishi);
	//		builder.setMessage(R.string.save_yes_no);
	//		builder.setPositiveButton(activity.getString(R.string.Ensure),new DialogInterface.OnClickListener() {
	//			public void onClick(DialogInterface dialog, int which) {
	//				if(activity != null){
	//					builder.create().dismiss();
	//					((SalonReadSelf) activity).createGroup();
	//				}
	//			}
	//		});
	//		builder.setNegativeButton(activity.getString(R.string.Cancel), new DialogInterface.OnClickListener() {
	//			
	//			@Override
	//			public void onClick(DialogInterface dialog, int which) {
	//				activity.finish();
	//			}
	//		});
	//		builder.create().show();
	//	}

	/**
	 * 从关注--黑名单
	 * @param context
	 * @param id
	 */
	public static void attToBlacklistDialog(Context context ,final CustomerInfoEntity entity){
		AlertDialog.Builder mBuilder = DialogUtils.showBasicDialog(
				context, context.getString(R.string.jiankangliao),
				context.getString(R.string.jiaru_black_note));
		mBuilder.setNegativeButton("取消", null);
		mBuilder.setPositiveButton("加入黑名单",
				new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog,
					int which) {
				FriendHttpUtil.requestHttpAboutFriend(Integer.valueOf(SmartFoxClient.getLoginUserId()),entity,2);
			}
		});
		mBuilder.create().show();
	}
	/**
	 * 
	 * @param context
	 * @param hintText
	 * @param btnStr
	 * @param btnStr1
	 * @param listener
	 * @return
	 */
	public static Dialog getHintDialog(Context context, String hintText,String btnStr,String btnStr1,OnClickListener listener){
		final Dialog dia=new Dialog(context, R.style.translucent_dialog);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_hint, null);
		Button btn=(Button) view.findViewById(R.id.hint_kbtn);
		Button btn1=(Button) view.findViewById(R.id.positive1);
		Button btn2=(Button) view.findViewById(R.id.negative1);
		
		TextView tv=(TextView) view.findViewById(R.id.hint_tv);
		tv.setText("提示");
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				dia.dismiss();	
			}
		});
		btn1.setVisibility(View.VISIBLE);
		btn1.setText(btnStr);
		btn2.setVisibility(View.VISIBLE);
		btn2.setText(btnStr1);
		
		TextView tv2=(TextView) view.findViewById(R.id.hint_tv2);
		tv2.setText(hintText);
		btn1.setOnClickListener(listener);
		DisplayMetrics display = context.getResources().getDisplayMetrics();
		int width = display.widthPixels - 50;
		int height = LayoutParams.WRAP_CONTENT;
		dia.setContentView(view,new LayoutParams(width, height));
		dia.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		return dia;

	}
	/**
	 * 
	 * @param context
	 * @param hintText
	 * @param btnStr
	 * @param listener
	 * @return
	 */
	public static Dialog getHintDialog(Context context, String hintText,String btnStr,OnClickListener listener){
		final Dialog dia=new Dialog(context, R.style.translucent_dialog);
		View view = LayoutInflater.from(context).inflate(R.layout.dialog_hint, null);
		Button btn=(Button) view.findViewById(R.id.hint_kbtn);
		Button btn1=(Button) view.findViewById(R.id.ensure_hint);
		TextView tv=(TextView) view.findViewById(R.id.hint_tv);
		tv.setText("提示");
		btn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dia.dismiss();	
			}
		});
		btn1.setVisibility(View.VISIBLE);
		btn1.setText(btnStr);
		TextView tv2=(TextView) view.findViewById(R.id.hint_tv2);
		tv2.setText(hintText);
		btn1.setOnClickListener(listener);
		DisplayMetrics display = context.getResources().getDisplayMetrics();
		int width = display.widthPixels - 50;
		int height = LayoutParams.WRAP_CONTENT;
		dia.setContentView(view,new LayoutParams(width, height));
		dia.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
		return dia;
		
	}

}
