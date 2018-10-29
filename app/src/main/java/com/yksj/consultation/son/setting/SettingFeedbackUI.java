package com.yksj.consultation.son.setting;

import org.json.JSONObject;

import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.comm.SingleBtnFragmentDialog.OnClickSureBtnListener;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.MyResultCallback;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.WheelUtils;
/**
 * 意见反馈
 * @author Administrator
 *
 */
public class SettingFeedbackUI extends BaseFragmentActivity implements OnClickListener {
	private EditText mEditText;
	private int textNumber = 0;
	TextView textNum = null;
	IntentFilter filter_faceback;//广播注册
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

		setContentView(R.layout.setting_layout_feedback);

		initView();
	}

	private void initView() {

		initTitle();

		titleLeftBtn.setOnClickListener(this);
		titleRightBtn2.setOnClickListener(this);
		titleRightBtn2.setVisibility(View.VISIBLE);
		titleRightBtn2.setText("提交");
		titleTextV.setText("意见反馈");


		mEditText = (EditText)findViewById(R.id.setting_feedback_text);
		mEditText.addTextChangedListener(textWatcher);
		textNum = (TextView)findViewById(R.id.textcount);
		textNum.setText("0/500");
		//		textNum.setVisibility(View.GONE);
		filter_faceback = new IntentFilter();    
		filter_faceback.addAction("com.yksj.ui.ACTION_SEND_FACEBACK");
	}

	/**
	 * 文字监听
	 */
	private TextWatcher textWatcher = new TextWatcher() {  
		@Override    
		public void afterTextChanged(Editable s) { 
		}   
		@Override 
		public void beforeTextChanged(CharSequence s, int start, int count,  
				int after) {  
		}  
		@Override    
		public void onTextChanged(CharSequence s, int start, int before,     
				int count) {   
			if((start +count)<=500 && mEditText.getText().toString().length()<=500){
				textNumber = mEditText.getText().toString().length();
				textNum.setText(textNumber+"/500"); 
			}else{
				mEditText.setText(s.subSequence(0, 500));
				ToastUtil.showShort(SettingFeedbackUI.this, R.string.most_five_hundred);
			}
		}                    
	};


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			SystemUtils.hideSoftBord(this,mEditText);
			finish();
			overridePendingTransition(R.anim.back_enter, R.anim.back_exit);
			break;

		case R.id.title_right2:
			submitFeedback();
			break;
		}
	}

	/**
	 * 提交意见内容
	 */
	private void submitFeedback(){
		WheelUtils.hideInput(SettingFeedbackUI.this,mEditText.getWindowToken());
		String value = mEditText.getEditableText().toString().trim();
		if("".equals(value)){
			SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), getString(R.string.opinion_null));
			return;
		}

		if(!SystemUtils.isNetWorkValid(this)){
			ToastUtil.showShort(this, R.string.getway_error_note);
			return;
		}

		HttpRestClient.OKHttpSaveFeedBackHZ(value, new MyResultCallback<JSONObject>(this) {
			@Override
			public void onError(Request request, Exception e) {
				super.onError(request, e);
			}

			@Override
			public void onResponse(JSONObject response) {
				super.onResponse(response);
				if (response!=null){
					if ("1".equals(response.optString("code"))){
						SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),  response.optString("result"),new OnClickSureBtnListener() {
							@Override
							public void onClickSureHander() {
								SettingFeedbackUI.this.finish();
							}
						});
					}else {
						SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),  response.optString("message"));
					}
				}else {
					ToastUtil.showShort(getApplicationContext(), R.string.request_error);
				}
			}
		},this);
//		HttpRestClient.doHttpSaveFeedBackHZ(value ,new AsyncHttpResponseHandler(this){
//
//			@Override
//			public void onSuccess(int statusCode, String content) {
//				super.onSuccess(statusCode, content);
//				org.json.JSONObject response;
//				try {
//					response = new org.json.JSONObject(content);
//					org.json.JSONObject obj=response.getJSONObject("saveFeedBackHZ");
//					if(content.contains("error_code")){
//						SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),  obj.getString("error_message"));
//					}else{
//						SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),  obj.getString("info"),new OnClickSureBtnListener() {
//							@Override
//							public void onClickSureHander() {
//								SettingFeedbackUI.this.finish();
//							}
//						});
//					}
//				} catch (JSONException e) {
//					e.printStackTrace();
//				}
//			}
//		});
	}
}
