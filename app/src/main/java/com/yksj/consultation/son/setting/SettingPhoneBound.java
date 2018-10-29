package com.yksj.consultation.son.setting;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog.OnDilaogClickListener;
import com.yksj.consultation.comm.SingleBtnFragmentDialog.OnClickSureBtnListener;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.LogUtil;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.ValidatorUtil;

import org.json.JSONObject;

/**
 * 账号安全
 *
 * @author Administrator
 *
 */
public class SettingPhoneBound extends BaseFragmentActivity implements
		OnClickListener {
	private EditText mInputNumber;// 手机号码
	private EditText mYanzhengma;// 验证码
	// private boolean flag=true;//作为标记,默认情况是绑定手机
	private TextView mMiaoShuText;
	private boolean SUCCESS_PHONE_FLAG = false;// true表示绑定手机成功
	private Button mNext;
	private CustomerInfoEntity mCus;
	private PopupWindow mPopUnBund;// 是否解除绑定的pop
	private boolean Sendcode = false;// 验证码是否发送 true为发送
	private Button mSetCode;
	// 定义Handler
	Handler handler = new Handler();
	private String customerid;
	private Runnable runnable;
	private TextView phone;
	private String phoneNum;
	private EditText mPhoneNumber,codeEdit;
	private Button codeBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.setting_emailmobile_bound);
		customerid = SmartFoxClient.getLoginUserId();
		getWindow().setSoftInputMode(
				WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		initTitle();
		initView();
	}

	private void initView() {
		titleLeftBtn.setOnClickListener(this);
		titleTextV.setText("绑定手机");
		mSetCode = (Button) findViewById(R.id.setting_safe_huoqu_yanzhengma);
		mSetCode.setOnClickListener(this);
		mNext = (Button) findViewById(R.id.next);
		mNext.setOnClickListener(this);
		mMiaoShuText = (TextView) findViewById(R.id.setting_bound_miaoshu);
		mYanzhengma = (EditText) findViewById(R.id.setting_safe_yanzhengma);
		mInputNumber = (EditText) findViewById(R.id.setting_input_number);
		//=======
		mPhoneNumber = (EditText) findViewById(R.id.setting_new_phone_agin);
		codeEdit = (EditText) findViewById(R.id.code1);
		phone=(TextView) findViewById(R.id.setting_bound_miaoshu);
//		phoneNum= LoginServiceManeger.instance().getLoginEntity().getPoneNumber();
//		phone.setText(phoneNum);//code_btn
		codeBtn=(Button)findViewById(R.id.code_btn);//codeBtncode1
		findViewById(R.id.next1).setOnClickListener(this);
		codeBtn.setOnClickListener(this);
		//=======
		mCus = SmartFoxClient.getLoginUserInfo();
		if (mCus.getPoneNumber() != null
				&& !mCus.getPoneNumber().trim().equals("")) {
			findViewById(R.id.yanzhengma_layout).setVisibility(View.GONE);
			findViewById(R.id.re).setBackgroundResource(R.drawable.text_background_single);
			mNext.setText("解除绑定");
			SUCCESS_PHONE_FLAG = true;
			mInputNumber.setText(mCus.getPoneNumber());
			mInputNumber.setEnabled(false);
		} else {
			mNext.setText("绑定");
			SUCCESS_PHONE_FLAG = false;
			mInputNumber.setEnabled(true);
			findViewById(R.id.re).setBackgroundResource(R.drawable.text_layout_up_normal);
			findViewById(R.id.yanzhengma_layout).setVisibility(View.VISIBLE);
			mMiaoShuText.setText(getString(R.string.setting_bound_mobile_miaoshu_befor));
		}
	}

	@Override
	public void onClick(View v) {
		SystemUtils.hideSoftBord(getApplicationContext(), mYanzhengma);
		switch (v.getId()) {
			case R.id.title_back:
				onBackPressed();
				break;

			case R.id.setting_safe_huoqu_yanzhengma:// 获取验证码
				if (Sendcode)
					return;
				getAuthCode();
				break;

			case R.id.next:
				SystemUtils.hideSoftBord(getApplicationContext(), mInputNumber);
			/*
			 * if(timer!=null){ mSetCode.setText("获取"); timer.cancel(); }
			 */
				canFindMe(v);
				break;
//		case R.id.queren:// 确认(解除绑定)
//			UnBund();
//			break;
			case R.id.cancle:// 取消
				if (mPopUnBund != null && mPopUnBund.isShowing())
					mPopUnBund.dismiss();
				break;
			case R.id.code_btn:
				getAuthCode();
				break;
			case R.id.next1:
				upDataPhone();
				break;
		}

	}

	@Override
	public void onBackPressed() {
		Intent intent = this.getIntent();
		Bundle mBundle = new Bundle();
		if (SUCCESS_PHONE_FLAG) {
			mBundle.putString("phone_num", mInputNumber.getText().toString().trim());
		}
		intent.putExtras(mBundle);
		setResult(RESULT_OK, intent);
		SystemUtils.hideSoftBord(this, mInputNumber);
		finish();
		overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
	}

	/**
	 * 解除绑定
	 */
	private void UnBund() {
		if (mPopUnBund != null && mPopUnBund.isShowing())
			mPopUnBund.dismiss();

		HttpRestClient.doHttpUnPhoneBind(customerid, new AsyncHttpResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				mInputNumber.setEnabled(true);
				mInputNumber.setText("");
				mNext.setText("绑定");
				mYanzhengma.setText("");
				SUCCESS_PHONE_FLAG = false;
				findViewById(R.id.re).setBackgroundResource(R.drawable.text_layout_up_normal);
				findViewById(R.id.yanzhengma_layout).setVisibility(View.VISIBLE);
				SmartFoxClient.getLoginUserInfo().setPoneNumber("");
				ToastUtil.showBasicShortToast(SettingPhoneBound.this, "已成功解除绑定");
				mMiaoShuText.setText(getString(R.string.setting_bound_mobile_miaoshu_befor));
			}
		});

	}

	/**
	 * 解除绑定pop
	 */
	private void showMenu(View view) {
//		if (mPopUnBund == null) {
//			LayoutInflater mInflater = LayoutInflater.from(this);
//			View mPopDeleteView = mInflater.inflate(R.layout.photo_delete_pop,null);
//			TextView mTextContent = (TextView) mPopDeleteView
//					.findViewById(R.id.mtext);
//			mTextContent.setText("是否解除绑定");
//			mPopDeleteView.findViewById(R.id.queren).setOnClickListener(this);
//			mPopDeleteView.findViewById(R.id.cancle).setOnClickListener(this);
//			mPopUnBund = new PopupWindow(mPopDeleteView,
//					android.view.WindowManager.LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT);
//			mPopUnBund.setAnimationStyle(R.style.AnimationPreview);
//			mPopUnBund.setBackgroundDrawable(new BitmapDrawable());
//			mPopUnBund.setFocusable(true);
//			mPopUnBund.showAtLocation(view, Gravity.BOTTOM, 0, 0);
//		} else if (mPopUnBund.isShowing()) {
//			mPopUnBund.dismiss();
//			return;
//		} else {
//			mPopUnBund.showAtLocation(view, Gravity.BOTTOM, 0, 0);
//		}
		DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "是否解除绑定?", "取消", "确定", new OnDilaogClickListener() {

			@Override
			public void onDismiss(DialogFragment fragment) {
				fragment.dismiss();
			}

			@Override
			public void onClick(DialogFragment fragment, View v) {
				UnBund();
			}
		});
	}

	/**
	 * 通讯录好友能否找到我
	 */
	private void canFindMe(View v) {
		SmartFoxClient.ifCanFindMe("0");
		if (!SystemUtils.isNetWorkValid(this)) {
			ToastUtil.showShort(this, R.string.getway_error_note);
			return;
		}
		if (SUCCESS_PHONE_FLAG) {// 解除绑定手机
			showMenu(v);
			return;
		} else {
			String phone = mInputNumber.getText().toString().trim();
			String code = mYanzhengma.getText().toString().trim();
			if(!TextUtils.isEmpty(phone)){
				if(!TextUtils.isEmpty(code)){
					if (phone != null && phone.length() == 11 && ValidatorUtil.checkMobile(phone)) {
						HttpRestClient.doHttpConsultationSetPhoneBound(phone, code, SmartFoxClient.getSmartFoxClient().getUserPas(), customerid,
								new AsyncHttpResponseHandler(this) {
									@Override
									public void onSuccess(int statusCode, String value) {
										super.onSuccess(statusCode, value);
										if (value != null && value.equals("1")) {
											SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),getString(R.string.phone_bound_success).toString(),new OnClickSureBtnListener() {
												@Override
												public void onClickSureHander() {
													finish();
												}
											});
											SmartFoxClient.getLoginUserInfo().setPoneNumber(mInputNumber.getText().toString());
											mInputNumber.setEnabled(false);
											mNext.setText("解除绑定");
											mMiaoShuText.setText(getString(R.string.setting_bound_mobile_miaoshu));
											findViewById(R.id.yanzhengma_layout).setVisibility(View.GONE);
											findViewById(R.id.re).setBackgroundResource(R.drawable.text_background_single);
											SUCCESS_PHONE_FLAG = true;
											Sendcode = false;
										} else {
											if (value == null) {
												SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),getString(R.string.bound_new_phone_fall).toString());
											} else {
												SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), value.toString());
											}
											Sendcode = false;
										}

									}});
					}
				}else{
					SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "请输入验证码");
				}
			}else{
				SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),getString(R.string.phone_toastSpecialcharter));
			}
		}
	}

	private void timerTaskC() {
		runnable = new Runnable() {
			int i = 60;
			@Override
			public void run() {
				if (i == 0) {
//					mSetCode.setText("获取");
//					mSetCode.setEnabled(true);
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

	@Override
	protected void onDestroy() {
		super.onDestroy();
		handler.removeCallbacks(runnable);
		if(handler!=null)handler=null;
		if(mPopUnBund!=null)mPopUnBund=null;
	}
	/**
	 * 获取验证码
	 */
	private void getAuthCode() {
		if (!SystemUtils.isNetWorkValid(this)) {
			ToastUtil.showShort(this, R.string.getway_error_note);
			return;
		}
//		String phone = mInputNumber.getText().toString();
		String phoneStr = mPhoneNumber.getText().toString().trim();
		if ( ValidatorUtil.checkMobile(phoneStr)) {
			HttpRestClient.addHttpHeader("client_type", HTalkApplication.CLIENT_TYPE);
			HttpRestClient.OKHttpSendBindPhoneNumCode(phoneStr, new MyResultCallback<JSONObject>() {
				@Override
				public void onError(Request request, Exception e) {

				}

				@Override
				public void onResponse(JSONObject response) {
					super.onResponse(response);
					if (!"1".equals(response.optString("code"))) {
						SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), response.optString("message"));
					} else {
						Sendcode = true;
						timerTaskC();
						ToastUtil.showShort(SettingPhoneBound.this, response.optString("message"));
					}
				}
			}, this);
		} else {
			SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), getString(R.string.phone_toastSpecialcharter));
		}
	}
	private void upDataPhone(){
		final String phoneStr=mPhoneNumber.getText().toString().trim();
		String codeStr=codeEdit.getText().toString().trim();
		if(TextUtils.isEmpty(phoneStr)){
			ToastUtil.showToastPanl("请填写手机号码");
			return;
		}
		if(!ValidatorUtil.checkMobile(phoneStr)){
			ToastUtil.showToastPanl("手机号码有误");
			return;
		}
		if(TextUtils.isEmpty(codeStr)){
			ToastUtil.showToastPanl("请填写验证码");
			return;
		}
		HttpRestClient.OKHttpBindPhoneNum(LoginServiceManeger.instance().getLoginEntity().getId(), phoneStr, codeStr, new MyResultCallback<JSONObject>() {
			@Override
			public void onError(Request request, Exception e) {

			}

			@Override
			public void onResponse(JSONObject response) {
				super.onResponse(response);
				if ("1".equals(response.optString("code"))) {
					LogUtil.d("TAG", response.toString());
					ToastUtil.showToastPanl(response.optString("result"));
					LoginServiceManeger.instance().getLoginEntity().setPoneNumber(phoneStr);
					finish();
				}else {
					String message=response.optString("message");
					ToastUtil.showToastPanl(message);
				}
			}
		}, this);
	}
}
