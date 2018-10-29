package com.yksj.consultation.son.wallet;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.JsonsfHttpResponseHandler;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.MD5Utils;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 忘记支付密码
 * @author Administrator
 *
 */
public class PwdForgetActivity extends BaseFragmentActivity implements OnClickListener{
	
	private boolean Sendcode=false;//验证码是否发送 true为发送
	private Button mSetCode;
	private TextView mInputNumber;//手机号码
	private Timer timer;
	private String isBDPhoneNum;
	 // 定义Handler  
    Handler handler = new Handler() { 
        @Override 
        public void handleMessage(Message msg) { 
            super.handleMessage(msg); 
            // Handler处理消息  
            if (msg.what > 0) { 
            	mSetCode.setText(msg.what + ""); 
            } else { 
            	Sendcode=false;
            	mSetCode.setText("获取"); 
                timer.cancel();  // 结束Timer计时器  
            } 
        } 
    };
	private EditText mCode;//验证码
	private EditText mRePwd;
	private EditText mPwd;
    
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.pwd_forget);
		initWidget();
		initDate();
	}

	private void initDate() {
		// TODO Auto-generated method stub
		isBDPhoneNum = getIntent().getExtras().getString("isBDPhoneNum");
		mInputNumber.setText(isBDPhoneNum);
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		initTitle();
		mSetCode = (Button)findViewById(R.id.code);
		mInputNumber = (TextView)findViewById(R.id.phone_num);
		mCode = (EditText) findViewById(R.id.setting_safe_yanzhengma);
		mRePwd = (EditText)findViewById(R.id.re_pwd);
		mPwd = (EditText)findViewById(R.id.pwd);
		titleTextV.setText("忘记支付密码");
		titleRightBtn2.setVisibility(View.VISIBLE);
		titleRightBtn2.setText("确定");
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn2.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.title_right2:
			if (mCode.getText().toString().trim().equals("")) {
				SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "验证码不能为空");
				return;
			}else if(mPwd.getText().toString().trim().equals("") || mRePwd.getText().toString().trim().equals("")){
				SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "密码不能为空");
				return;
			}else if(!mPwd.getText().toString().trim().equals(mRePwd.getText().toString().trim())){
				SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "两次密码输入不一致");
				return;
			}else if(mPwd.getText().toString().trim().length() != 6){
				SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "密码必须为6位");
				return;
			}
			RequestParams params = new RequestParams();
			params.put("TYPE", "FORGETPASS");
			params.put("CUSTOMERID", SmartFoxClient.getLoginUserId());
			params.put("PAYMENT_PASS", MD5Utils.getMD5(mPwd.getText().toString().trim()));
			params.put("RE_PAYMENT_PASS", MD5Utils.getMD5(mRePwd.getText().toString().trim()));
			params.put("CODE", mCode.getText().toString().trim());
			params.put("TELEPHONE", isBDPhoneNum);
			HttpRestClient.doHttpWalletSetting(params,new AsyncHander(HTTP.SUBMIT.value));
			break;
		default:
			break;
		}
	}

	/**
	 * 获取验证码
	 * @param v
	 */
	public void onClickCode(View v){
		if(Sendcode)return;
		getAuthCode();
	}
	
	
	private void timerTaskC(){
    	TimerTask timerTask = new TimerTask() { 
            // 倒数10秒  
            int i = 60; 
            @Override 
            public void run() { 
                // 定义一个消息传过去  
                Message msg = new Message(); 
                msg.what = i--; 
                handler.sendMessage(msg); 
            } 
        };
        if(timer==null)timer=new Timer();
    	timer.schedule(timerTask, 0, 1000);
    }
	
	
	/**
	 * 获取验证码
	 */
	private void getAuthCode() {
		if (!SystemUtils.isNetWorkValid(this)) {
			ToastUtil.showShort(this, R.string.getway_error_note);
			return;
		}

		String phone = mInputNumber.getText().toString().trim();
		if (phone != null && phone.length() == 11 && isDigit(phone)) {
			RequestParams params = new RequestParams();
			params.put("TYPE", "GETCODE");
			params.put("CUSTOMERID", SmartFoxClient.getLoginUserId());
			params.put("TELEPHONE", phone);
			HttpRestClient.doHttpWalletSetting(params,new AsyncHander(HTTP.CODE.value));
		} else {
			SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), getString(R.string.phone_toastSpecialcharter));
		}
	}
	
	
	public boolean isDigit(String str) {
		boolean flag = true;
		char[] c = str.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (!Character.isDigit(c[i])) {
				flag = false;
			}
		}
		return flag;
	}
	
	//http请求
	class AsyncHander extends JsonsfHttpResponseHandler {
		private int value;
		public AsyncHander(int value){
			super(PwdForgetActivity.this);
			this.value = value;
		}
		@Override
		public void onSuccess(JSONObject response) {
			if (response.containsKey("CODE")) {
				if (response.getIntValue("CODE") == 0) {
					switch (value) {
					case 1://获取验证码
							Sendcode=true;
							timerTaskC();
						break;
					case 2://提交密码
						ToastUtil.showShort(getApplicationContext(), response.getString("INFO"));
						finish();
						break;
					default:
						break;
					}
				}else {
					ToastUtil.showShort(getApplicationContext(), response.getString("INFO"));
				}
			}else {
				ToastUtil.showShort(getApplicationContext(), response.toString());
			}
			super.onSuccess(response);
		}
	}
	
	public enum HTTP{
		 CODE(1),SUBMIT(2),REQUEST(0);
		private int value;
		private HTTP(int value){
			this.value = value;
		 }
	}
	
}
