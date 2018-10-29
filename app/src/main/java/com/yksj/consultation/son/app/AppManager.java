package com.yksj.consultation.son.app;

import android.support.v4.app.FragmentActivity;

import com.yksj.consultation.son.login.UserLoginActivity;

import java.util.Stack;

/**
 * 
 * activity 堆栈管理
 * @author origin
 *
 */
public class AppManager {
	
	private Stack<FragmentActivity> activityStack = new Stack<FragmentActivity>();
	private static AppManager activityManager;
	
	private AppManager(){
	}
	
	public static AppManager getInstance(){
		if(activityManager == null){
			activityManager = new AppManager();
		}
		return activityManager;
	}
	
	
	public void addStack(FragmentActivity activity){
		if(activity != null){
			activityStack.add(activity);
		}
	}
	
	public FragmentActivity getCurrentActivity(){
		return activityStack.lastElement();
	}
	public FragmentActivity getFirstActivity(){
		return activityStack.get(0);
	}
	
	
	public void finishActivity(FragmentActivity activity){
		if(activity != null&& !activity.isFinishing()){
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}
	
	public void finishAllActivity(){
		FragmentActivity aty;
		for (int i = 0; i < activityStack.size(); i++) {
			aty = activityStack.get(i);
			if(aty != null && !aty.isFinishing()){
				if(!(aty instanceof UserLoginActivity)){
					activityStack.remove(aty);
					aty.finish();
				}
			}
		}
//		activityStack.clear();
	}
	
};
