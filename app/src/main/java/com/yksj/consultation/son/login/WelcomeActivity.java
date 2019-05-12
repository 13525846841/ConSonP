package com.yksj.consultation.son.login;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.main.AtyConsultMain;
import com.yksj.consultation.son.home.PatientHomeActivity;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.services.CoreService;
import com.yksj.healthtalk.utils.AppCashHandler;
import com.yksj.healthtalk.utils.DataParseUtil;
import com.yksj.healthtalk.utils.SharePreUtils;
import com.yksj.healthtalk.utils.ThreadManager;
import com.yksj.healthtalk.utils.WeakHandler;

import cn.sharesdk.framework.ShareSDK;

public class WelcomeActivity extends BaseFragmentActivity {

	WeakHandler mHandler =new WeakHandler();
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.logo_layout);
		CoreService.actionStart(WelcomeActivity.this);
		ThreadManager.getInstance().createLongPool().execute(new Runnable() {
			@Override
			public void run() {
				AppCashHandler appExceptionCashHandler = AppCashHandler.getInstance();
				appExceptionCashHandler.init(WelcomeActivity.this);
				ShareSDK.initSDK(WelcomeActivity.this);

				//上传异常日志
				if (isWifi()) {
					appExceptionCashHandler.sendLogToServer();
				}
			}
		});
		mHandler.postDelayed(new Runnable() {
			@Override
			public void run() {

				boolean b = SharePreUtils.getLoginState();// 是否已经登录
				if (b) {
					onHandleCrashedLogin();
				} else {
					onHandleLogin();
				}
			}
		}, 4000);
	}
	/**
	 * 是否是wifi网络
	 *
	 * @return
	 */
	public synchronized boolean isWifi() {
		NetworkInfo info = HTalkApplication.getApplication().getConnectivityManager().getActiveNetworkInfo();
		if (info == null) {
			return false;
		}
		return info.getType() == ConnectivityManager.TYPE_WIFI;
	}

	private void onHandleLogin() {
		Intent intent = new Intent(WelcomeActivity.this, PatientHomeActivity.class);
		startActivity(intent);
		this.finish();
	}

	/**
	 * 异常退出登录处理
	 */
	private void onHandleCrashedLogin() {
		try {
			final String[] str = SharePreUtils.fatchUserLoginCache();
//			LoginServiceManeger.instance().login(str[0],str[1]);
//			SmartControlClient controlClient = SmartControlClient.getControlClient();
//			controlClient.setUserPassword(str[0], str[1]);
//			controlClient.setCustomerInfoEntity(entity);
			String parame = SharePreUtils.getLoginUserInfo();
			JSONObject jsonObject = JSON.parseObject(parame);
			CustomerInfoEntity entity = DataParseUtil.jsonToCustomerInfo2(jsonObject.toString());
			LoginServiceManeger.instance().setLoginInfo(entity);
			LoginServiceManeger.instance().login(str[0], str[1],"","","0");
			ThreadManager.getInstance().createShortPool().execute(new Runnable() {
				@Override
				public void run() {

				}
			});
//			CoreService.actionLogin(this);
			Intent intent = new Intent(WelcomeActivity.this, PatientHomeActivity.class);
			startActivity(intent);
			this.finish();
		} catch (Exception e) {
			SharePreUtils.updateLoginState(false);
			onHandleLogin();
		}
	}


	@Override
	protected void onDestroy() {
		super.onDestroy();
		mHandler.removeCallbacksAndMessages(null);
	}



}
