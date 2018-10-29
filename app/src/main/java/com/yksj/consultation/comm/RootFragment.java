package com.yksj.consultation.comm;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.LogUtil;

import org.json.JSONArray;

/**
 * 主页tab切换基类
 * @author origin
 */
public class RootFragment extends Fragment{
	private static final String TAG = RootFragment.class.getName();
	public ImageView titleLeftBtn;
	public Button titleRightBtn;
	public Button titleRightBtn2;
	public TextView titleTextV;
	public OnBackPressedClickListener mBackPressedClickListener;
	public FragmentActivity mActivity;
	
	
	public final void setBlackColor(TextView textView){
		if(textView != null){
			textView.setTextColor(getResources().getColor(R.color.black));
		}
	}

	public final void setTitleTxtView(String text){
		titleTextV.setText(text);
	}
	
	
	/**
	 * 返回按键监听事件
	 * @author origin
	 */
	public interface OnBackPressedClickListener{
		void onBackClick(View view);
		//区域选择点击
		void onAreaItemClick(JSONArray array,String titlePicPath);
	}
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		try{
			mActivity=(FragmentActivity) activity;
			mBackPressedClickListener = (OnBackPressedClickListener)activity;
		}catch(ClassCastException e){
		}
	}
	
	/**
	 * 退出堆栈
	 * @return 是否还有堆栈 true ，false
	 */
	public boolean popBackStack(){
		boolean isPopBackStack = getBackStackEntryCount() > 0?true:false;
		if(isPopBackStack)getChildFragmentManager().popBackStackImmediate();
		Log.d(TAG, "-----------popBackStack");
		return isPopBackStack;
	}
	
	/**
	 * 获得堆栈数量
	 * @return
	 */
	public int getBackStackEntryCount(){
		return getChildFragmentManager().getBackStackEntryCount();
	} 
	
	/**
	 * 清除堆栈
	 */
	public void clearBackStack(){
		getChildFragmentManager().popBackStack(null,FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}
	
	/**
	 * title 返回按钮
	 * @param view
	 */
	public void onBackPressed(View view){
		if(mBackPressedClickListener != null){
			mBackPressedClickListener.onBackClick(view);
		}else{
			getActivity().onBackPressed();
		}
	}
	
	public void initTitleView(View view){
		titleLeftBtn = (ImageView)view.findViewById(R.id.title_back);
		titleRightBtn = (Button)view.findViewById(R.id.title_right);
		titleTextV = (TextView)view.findViewById(R.id.title_lable);
		titleRightBtn2 = (Button)view.findViewById(R.id.title_right2);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		LogUtil.d("CLASS_NAME",getClass().toString());
//		Log.v("TAG", getClass().toString());
	}
}
