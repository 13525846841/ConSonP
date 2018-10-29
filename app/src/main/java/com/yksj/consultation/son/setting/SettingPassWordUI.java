package com.yksj.consultation.son.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.login.UserLoginActivity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.net.socket.SmartControlClient;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.DialogUtils;
import com.yksj.healthtalk.utils.LogUtil;
import com.yksj.healthtalk.utils.MD5Utils;
import com.yksj.healthtalk.utils.SharePreUtils;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.ValidatorUtil;
import com.yksj.healthtalk.utils.WheelUtils;

public class SettingPassWordUI extends BaseFragmentActivity implements OnClickListener {

	private EditText mNewPsd;//新密码
	private EditText mNewPsdAgain;//重复新密码
	private EditText codeEdit;
	private String pasagain;
	private SmartControlClient mSmartFoxClient;
	private EditText oldPsd;
	private Button codeBtn;
	private TextView phone;
	private Runnable runnable;
	Handler handler = new Handler();
	private boolean Sendcode = false;
	private String phoneNum;
	private LinearLayout codell;
	private String intentCode="",intentPhone="";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_password);
		initView();
	}

	private void initView() {
		initTitle();
		titleLeftBtn.setOnClickListener(this);
		titleTextV.setText("修改密码");
		mSmartFoxClient=SmartControlClient.getControlClient();
		findViewById(R.id.next).setOnClickListener(this);
		mNewPsd = (EditText)findViewById(R.id.setting_new_psd);
		mNewPsdAgain = (EditText)findViewById(R.id.setting_new_psd_agin);
		oldPsd = (EditText)findViewById(R.id.setting_old_psd);
		phone = (TextView)findViewById(R.id.phone);
		codeEdit=(EditText)findViewById(R.id.code1);
		codell=(LinearLayout)findViewById(R.id.title_below1);

		codeBtn=(Button)findViewById(R.id.code_btn);
		codeBtn.setOnClickListener(this);
		if(getIntent().hasExtra("CODE")){
			intentCode=getIntent().getStringExtra("CODE");
			intentPhone=getIntent().getStringExtra("PHONE");
			phone.setText(intentPhone);
			codell.setVisibility(View.GONE);
		}else {
			phoneNum=LoginServiceManeger.instance().getLoginEntity().getPoneNumber();
			phone.setText(phoneNum);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			SystemUtils.hideSoftBord(this,mNewPsd);
			finish();
			break;
			
		case R.id.next:
//			String string = oldPsd.getText().toString().trim();
//			if(string!=null&&mSmartFoxClient.getPassword().equals(MD5Utils.getMD5(string))){
				comfirm();
//			}else{
//				ToastUtil.showBasicShortToast(SettingPassWordUI.this, "当前密码输入错误");
//			}
			break;
		case R.id.code_btn:
			getAuthCode();
			break;
		}
		
	}
	
	
	private void comfirm() {
		if(!SystemUtils.isNetWorkValid(this)){
			ToastUtil.showShort(SettingPassWordUI.this, R.string.getway_error_note);
			return;
		}
		if(!getIntent().hasExtra("CODE")){
			if(TextUtils.isEmpty(codeEdit.getText().toString().trim())){
				ToastUtil.showToastPanl("请填写验证码");
				return;
			}
		}
		String pasnew = mNewPsd.getText().toString().trim();
		pasagain = mNewPsdAgain.getText().toString().trim();
		if ("".equals(pasnew)|| "".equals(pasagain)) {
			DialogUtils.getErrorDialog(SettingPassWordUI.this,
					getString(R.string.jiankangliao),
					getString(R.string.psw_toastNull)).show();
		}else if (pasnew.length() < 6 || pasnew.length()> 12||pasagain.length() < 6) {
			ToastUtil.showShort("您输入的密码应该是6-12位");
		}else if (!pasnew.equals(pasagain)) {
			DialogUtils.getErrorDialog(SettingPassWordUI.this,getString(R.string.jiankangliao),
					getString(R.string.psw_toastDiscord)).show();
		} else if(pasnew.replaceAll("[a-z]*[A-Z]*\\d*", "").length()!=0 || 
				  pasagain.replaceAll("[a-z]*[A-Z]*\\d*", "").length()!=0){
			DialogUtils.getErrorDialog(SettingPassWordUI.this,
					getString(R.string.jiankangliao),
					getString(R.string.psw_toastSpecialcharter)).show();
		}else {
			String password = MD5Utils.getMD5(pasnew);
			if(getIntent().hasExtra("CODE")){
				HttpRestClient.OKHttpUpdateFindPassword(intentPhone, password, intentCode, new MyResultCallback<org.json.JSONObject>(this) {
					@Override
					public void onError(Request request, Exception e) {

					}

					@Override
					public void onResponse(org.json.JSONObject response) {
						super.onResponse(response);
						if("1".equals(response.optString("code"))){
							LogUtil.d("TAG","修改密码请求返回"+response.toString());
							if (pasagain.trim().length() != 32&&!pasagain.trim().equals("")&&pasagain.length()!=0) {
								mSmartFoxClient.updateUserPassword(pasagain);
								SharePreUtils.updateUserLoginPasswd(mSmartFoxClient.getPassword());
								ToastUtil.showBasicShortToast(SettingPassWordUI.this, "修改成功..");
								Intent intent = new Intent(SettingPassWordUI.this,UserLoginActivity.class);
								intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
								startActivity(intent);
								overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
							}else {
								ToastUtil.showBasicShortToast(SettingPassWordUI.this, "修改失败,请稍后重试..");
							}
						}
					}
				},this);
			}else {
				HttpRestClient.OKHttpUpdatePassword(LoginServiceManeger.instance().getLoginEntity().getId(), password, codeEdit.getText().toString().trim(), new MyResultCallback<org.json.JSONObject>(this) {
					@Override
					public void onError(Request request, Exception e) {

					}

					@Override
					public void onResponse(org.json.JSONObject response) {
						super.onResponse(response);
						if("1".equals(response.optString("code"))){
							LogUtil.d("TAG","修改密码请求返回"+response.toString());
							if (pasagain.trim().length() != 32&&!pasagain.trim().equals("")&&pasagain.length()!=0) {
								mSmartFoxClient.updateUserPassword(pasagain);
								SharePreUtils.updateUserLoginPasswd(mSmartFoxClient.getPassword());
								ToastUtil.showBasicShortToast(SettingPassWordUI.this, "修改成功..");
								finish();
								overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
							}else {
								ToastUtil.showBasicShortToast(SettingPassWordUI.this, "修改失败,请稍后重试..");
							}
						}
					}
				},this);
			}
//			HttpRestClient.doHttpChangePassword(SmartFoxClient.getSmartFoxClient().getUserId(), password, new AsyncHttpResponseHandler(this){
//				@Override
//				public void onSuccess(String value) {
//					if(value!=null && value.equals("1")){
//						if (pasagain.trim().length() != 32&&!pasagain.trim().equals("")&&pasagain.length()!=0) {
//						mSmartFoxClient.updateUserPassword(pasagain);
//						SharePreUtils.updateUserLoginPasswd(mSmartFoxClient.getPassword());
//						ToastUtil.showBasicShortToast(SettingPassWordUI.this, "修改成功..");
//						finish();
//						overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
//						}
//					}else{
//						ToastUtil.showBasicShortToast(SettingPassWordUI.this, "修改失败,请稍后重试..");
//					}
//				}
//			});
			WheelUtils.hideInput(this, mNewPsdAgain.getWindowToken());
		}
	}
	/**
	 * 获取验证码
	 */
	private void getAuthCode() {
		if (!SystemUtils.isNetWorkValid(this)) {
			ToastUtil.showShort(this, R.string.getway_error_note);
			return;
		}
		String phone = phoneNum;
		if(TextUtils.isEmpty(phone)){
			ToastUtil.showToastPanl("请填写手机号码");
			return;
		}
		if(!ValidatorUtil.checkMobile(phone)){
			ToastUtil.showToastPanl("手机号码有误");
			return;
		}
		if ( ValidatorUtil.checkMobile(phone)) {
			HttpRestClient.OKHttpSendUpdatePasswordCode(LoginServiceManeger.instance().getLoginEntity().getId(), new MyResultCallback<JSONObject>(this) {
				@Override
				public void onError(Request request, Exception e) {

				}

				@Override
				public void onResponse(com.alibaba.fastjson.JSONObject response) {
					super.onResponse(response);
					if ("0".equals(response.getString("code"))) {
						SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), response.getString("message"));
					} else {
						Sendcode = true;
						timerTaskC();
						ToastUtil.showShort(SettingPassWordUI.this, response.getString("message"));
					}
				}
			}, this);
		} else {
			SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), getString(R.string.phone_toastSpecialcharter));
		}
	}
	/**
	 * 设置六十秒
	 */
	private void timerTaskC() {
		runnable = new Runnable() {
			int i = 60;
			@Override
			public void run() {
				if (i == 0) {
					codeBtn.setText("发送验证码");
					codeBtn.setEnabled(true);
					Sendcode=false;
					return;
				} else {
					--i;
					handler.postDelayed(this, 1000);
					codeBtn.setText(i + "");
					codeBtn.setEnabled(false);
				}
			}
		};
		handler.postDelayed(runnable, 1000);
	}
}
