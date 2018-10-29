package com.yksj.healthtalk.utils;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.yksj.consultation.son.R;

import java.util.ArrayList;

/**
* @ClassName: PopWindowUtil 
* @Description：沙龙上面的弹出框
* @author wangtao wt0710910108_gmail_com  
* @date 2013-1-5 上午9:43:11
 */
public class PopWindowUtil {
	  PopupWindow mPopupWindow;
	 Context context;
	private String[][] arrs;
	View mSalonMoreView;
	ArrayList<String> mRightlist;
	EditText mSearchEditView;
	View v;
	
	public PopWindowUtil(Context context1) {
		this.context = context1;
	}
	/**
	 * 显示快捷菜单
	 * @param asDropDownView
	 */
	public  void showPopMenu(View asDropDownView,String name , String buyTime,String endTime,int type){
		if(mPopupWindow == null){
			LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = inflater.inflate(R.layout.common_dialog, null);
			mPopupWindow = new PopupWindow(v,LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			mPopupWindow.setFocusable(true);
			mPopupWindow.setTouchable(true);
			mPopupWindow.setOutsideTouchable(true);
			mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
		}
		TextView buyTimeTextView = (TextView) v.findViewById(R.id.buy_time);
		TextView endTimeTextView = (TextView) v.findViewById(R.id.end_time);
		TextView name1 = (TextView)v.findViewById(R.id.name);
		TextView ticketTypeTextView  =  (TextView) v.findViewById(R.id.ticket_type);
		buyTimeTextView.setText(buyTime);
		endTimeTextView.setText(endTime);
		name1.setText(name);
		if (type == 1) {
			ticketTypeTextView.setText("日票");
		}else if(type == 2){
			ticketTypeTextView.setText("月票");
		}else {
			ToastUtil.showShort(context, "类型传值失败");
		}
		if(mPopupWindow.isShowing()){
			mPopupWindow.dismiss();
			return;
		}
		mPopupWindow.showAtLocation((View) asDropDownView.getParent(), Gravity.CENTER, 0, 0);
	}
	
	public static void showWindow(View view,Context context,LayoutInflater inflater,int id,final OnClickListener listener){
		ViewGroup viewGroup = (ViewGroup)inflater.inflate(id, null);
		final PopupWindow window = new PopupWindow(viewGroup, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		int size = viewGroup.getChildCount();
		for (int i = 0; i < size; i++) {
			View childView  =  viewGroup.getChildAt(i);
			childView.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					window.dismiss();
					if(listener != null)listener.onClick(v);
				}
			});
			if(childView.getId() == R.id.quxiao){
				childView.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						window.dismiss();
					}
				});
			}
		}
		setWindow(window);
		window.showAtLocation(view,Gravity.BOTTOM, 0, 0);
	}
	
//	/**
//	 * 新版的展示poppupWindow
//	 * @param view
//	 * @param context
//	 * @param inflater
//	 * @param id
//	 * @param listener
//	 */
//	public static PopupWindow newShowWindow(View view,final FragmentActivity activity,LayoutInflater inflater,int id,final OnClickListener listener,boolean bo){
//		View poplayout = (ViewGroup)inflater.inflate(id, null);
//		final WindowManager.LayoutParams windowLp=activity.getWindow().getAttributes();
//		windowLp.alpha=0.6f;
//		activity.getWindow().setAttributes(windowLp);
//		final PopupWindow window = new PopupWindow(poplayout, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
//		ViewFinder viewFinder =new ViewFinder(poplayout);
//		viewFinder.onClick(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				windowLp.alpha=1.0f;
//				activity.getWindow().setAttributes(windowLp);
//				window.dismiss();
//				if(listener != null)listener.onClick(v);
//			}
//		}, new int[]{R.id.delete_repeat,R.id.delete_current});
//
//		poplayout.findViewById(R.id.delete_cancel).setOnClickListener(new OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				window.dismiss();
//			}
//		});
//
//		if(bo){
//			poplayout.findViewById(R.id.delete_repeat).setVisibility(View.GONE);
//		}
//		window.setOnDismissListener(new OnDismissListener() {
//
//			@Override
//			public void onDismiss() {
//				windowLp.alpha=1.0f;
//				activity.getWindow().setAttributes(windowLp);
////				window.dismiss();
//			}
//		});
//		setWindow(window);
//		window.showAtLocation(view,Gravity.BOTTOM, 0, 0);
//		return window;
//	}
	
	public static void showSelectPhoto(View view,Context context,LayoutInflater inflater,final OnClickListener listener){
		View checklist = inflater.inflate(R.layout.personal_photo_check, null);
		final PopupWindow window = new PopupWindow(checklist, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
		final OnClickListener listener2 = new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listener != null){
					listener.onClick(v);
				}
				window.dismiss();
			}
		};
		checklist.findViewById(R.id.bendifenjian).setOnClickListener(listener2);
		checklist.findViewById(R.id.paizhao).setOnClickListener(listener2);
		checklist.findViewById(R.id.quxiao).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				window.dismiss();
			}
		});
		setWindow(window);
		window.showAtLocation(view,Gravity.BOTTOM, 0, 0);
	}
	
	public static void setWindow(PopupWindow window){
		window.setAnimationStyle(R.style.AnimationPreview);
		window.setFocusable(true);
		window.setTouchable(true);
		window.setOutsideTouchable(true);
		window.setBackgroundDrawable(new BitmapDrawable());
	}

}
