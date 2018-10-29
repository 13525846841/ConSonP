package com.yksj.healthtalk.net.http;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.dmsj.newask.http.LodingFragmentDialog;
import com.yksj.consultation.son.friend.DoctorClinicMainActivity;
import com.yksj.consultation.son.home.PersonInfoActivity;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.utils.DataParseUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONObject;

public class CustomerInfoHttpResponseHandler extends ObjectHttpResponseHandler {
	
	LodingFragmentDialog mDialog;
	final Activity mActivity;
	final FragmentManager mManager;
	public CustomerInfoHttpResponseHandler(Activity mActivity,FragmentManager manager) {
		this.mActivity = mActivity;
		this.mManager = manager;
	}
	@Override
	public void onStart() {
		mDialog = LodingFragmentDialog.showLodingDialog(mManager,mActivity.getResources());
	}
	
	@Override
	public Object onParseResponse(String content) {
		try {
			JSONObject object = new JSONObject(content);
			CustomerInfoEntity  entity = DataParseUtil.jsonToCustmerInfo(object);
			return entity;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public void onSuccess(int statusCode, Object response) {
		CustomerInfoEntity  entity = (CustomerInfoEntity)response;
		if(TextUtils.isEmpty(entity.getId().trim())){
			ToastUtil.showToastPanl("没有这个人");
			return;
		}
		if(mDialog.isShowing() && entity != null){
			if(entity.isShowDoctorV()){
				Intent intent = new Intent(mActivity,DoctorClinicMainActivity.class);
				intent.putExtra("id", entity.getId());
				mActivity.startActivity(intent);
			}else{
				Intent intent = new Intent(mActivity,PersonInfoActivity.class);
				intent.putExtra("id", entity.getId());
				mActivity.startActivity(intent);
			}
		}
	}
	
	@Override
	public void onFinish() {
		mDialog.dismissAllowingStateLoss();
	}

}
