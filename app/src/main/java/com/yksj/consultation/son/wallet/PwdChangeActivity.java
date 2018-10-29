package com.yksj.consultation.son.wallet;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.alibaba.fastjson.JSONObject;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.JsonsfHttpResponseHandler;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.MD5Utils;
import com.yksj.healthtalk.utils.ToastUtil;


/**
 * 修改支付密码
 * @author Administrator
 *
 */
public class PwdChangeActivity extends BaseFragmentActivity implements OnClickListener{
	
	private EditText mOldPwd;
	private EditText mReNewPwd;
	private EditText mNewPwd;

	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		setContentView(R.layout.pwd_change);
		initWidget();
		initDate();
	}

	private void initDate() {
		// TODO Auto-generated method stub
		
	}

	private void initWidget() {
		// TODO Auto-generated method stub
		initTitle();
		mOldPwd = (EditText)findViewById(R.id.old_pwd);
		mNewPwd = (EditText)findViewById(R.id.new_pwd);
		mReNewPwd = (EditText)findViewById(R.id.re_new_pwd);
		titleTextV.setText("修改支付密码");
		titleRightBtn2.setVisibility(View.VISIBLE);
		titleRightBtn2.setText("确定");
		titleLeftBtn.setOnClickListener(this);
		titleRightBtn2.setOnClickListener(this);
	}

	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch (arg0.getId()) {
		case R.id.title_back:
			finish();
			break;
		case R.id.title_right2:
			if (mOldPwd.getText().toString().trim().length() == 6 && mReNewPwd.getText().toString().trim().length() == 6 & mNewPwd.getText().toString().trim().length() == 6) {
				if (!mReNewPwd.getText().toString().equals(mNewPwd.getText().toString())) {
					SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),  "两次密码输入不一致");
					return;
				}
				RequestParams params = new RequestParams();
				params.put("CUSTOMERID", SmartFoxClient.getLoginUserId());
				params.put("TYPE", "UPDATEPASS");
				params.put("PAYMENT_PASS", MD5Utils.getMD5(mNewPwd.getText().toString().trim()));
				params.put("RE_PAYMENT_PASS", MD5Utils.getMD5(mReNewPwd.getText().toString().trim())	);
				params.put("OLD_PAYMENT_PASS", MD5Utils.getMD5(mOldPwd.getText().toString().trim())	);
				HttpRestClient.doHttpWalletSetting(params,
						new JsonsfHttpResponseHandler(this){
					@Override
					public void onSuccess(int statusCode, JSONObject response) {
						if (response.containsKey("CODE")) {
							if (response.getIntValue("CODE") == 0) {
								finish();
							}
							ToastUtil.showShort(getApplicationContext(), response.getString("INFO"));
						}
						super.onSuccess(statusCode, response);
					}
				});
			}else {
				SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),  "密码必须为6位");
			}
			break;
		default:
			break;
		}
	}

}
