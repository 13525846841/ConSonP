package com.yksj.consultation.son.consultation;

import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.ValidatorUtil;
import com.yksj.healthtalk.utils.ViewFinder;
/**
 * 患者退款 填写信息
 * @author jack_tang
 *
 */
public class ConsultationBackPayActivity extends BaseFragmentActivity implements OnClickListener {

	private int type;//1 表示支付宝支付  2表示银行卡
	private JSONObject mJson;
	private ViewFinder mFfinder;
	private int consultationId;//会诊id

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.order_patient_refund_activity_layout);
		initView();
	}

	private void initView() {
		initTitle();
		titleLeftBtn.setOnClickListener(this);
		titleTextV.setText("退款");
		findViewById(R.id.action).setOnClickListener(this);
		type = getIntent().getIntExtra("type",1);
		if(type == 1){
			findViewById(R.id.zhifupay_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.yinhang_layout).setVisibility(View.GONE);
		}else if(type == 2){
			findViewById(R.id.zhifupay_layout).setVisibility(View.GONE);
			findViewById(R.id.yinhang_layout).setVisibility(View.VISIBLE);
		}
		consultationId = getIntent().getIntExtra("conId",-1);
		mFfinder = new ViewFinder(this);
		initData();
	}

	private void initData() {
		RequestParams params=new RequestParams();
		params.put("OPTION", "12");
		params.put("CONSULTATIONID", ""+consultationId);
		params.put("TYPE", ""+type);
		HttpRestClient.doHttpSubmitCaseTemplate(params,new AsyncHttpResponseHandler(this){
			@Override
			public void onSuccess(int statusCode, String content) {
				super.onSuccess(statusCode, content);
				try {
					JSONObject object=new JSONObject(content);
					if(object.has("CODE")){
						if("0".equals(object.optString("CODE"))){
							if(type == 1){//支付宝
								mFfinder.setText(R.id.pay_number, object.optString("ACCOUNT"));
								mFfinder.setText(R.id.pay_number_to, object.optString("ACCOUNT"));
								mFfinder.setText(R.id.pay_phone, object.optString("PHONE"));
							}else{//银行卡
								mFfinder.setText(R.id.bank_number,  object.optString("ACCOUNT"));
								mFfinder.setText(R.id.bank_name,  object.optString("NAME"));
								mFfinder.setText(R.id.bank_phone,  object.optString("PHONE"));
								mFfinder.setText(R.id.bank_type,  object.optString("ADDR"));
							}
						}
					}
					
				} catch (Exception e) {
				}
			}
		});
	}

	@Override
	public void onClick(View arg0) {
		SystemUtils.hideSoftBord(this,(EditText)findViewById(R.id.pay_number));
		switch (arg0.getId()) {
		case R.id.title_back:
			onBackPressed();
			break;
		case R.id.action:
			actionSumit();
			break;
		}
	}

	private void actionSumit() {
		RequestParams params = new RequestParams();
		params.put("CONSULTATIONID", ""+consultationId);
		if(type == 1){
			if(HStringUtil.isEmpty(mFfinder.editView(R.id.pay_number).getText().toString())){
				ToastUtil.showShort("请输入支付宝账号");
				return ;
			}	
			if(HStringUtil.isEmpty(mFfinder.editView(R.id.pay_number_to).getText().toString())){
				ToastUtil.showShort("请输入确认支付宝账号");
				return ;
			}
			
			if(!mFfinder.editView(R.id.pay_number).getText().toString().equals(mFfinder.editView(R.id.pay_number_to).getText().toString())){
				ToastUtil.showShort("两次支付宝账号输入不一致");
				return ;
			}
			
			if(!ValidatorUtil.checkMobile(mFfinder.editView(R.id.pay_phone).getText().toString())){
				ToastUtil.showShort("请输入正确的手机号码");
				return ;
			}
			params.put("PHONE", mFfinder.editView(R.id.pay_phone).getText().toString());
			params.put("ACCOUNT", mFfinder.editView(R.id.pay_number).getText().toString());
			params.put("OPTION", "2");
			
		}else{
			
			if(HStringUtil.isEmpty(mFfinder.editView(R.id.bank_name).getText().toString())){
				ToastUtil.showShort("请输入收款人姓名");
				return ;
			}	
			if(HStringUtil.isEmpty(mFfinder.editView(R.id.bank_number).getText().toString())){
				ToastUtil.showShort("请输入银行卡账号");
				return ;
			}
			
			if(HStringUtil.isEmpty(mFfinder.editView(R.id.bank_type).getText().toString())){
				ToastUtil.showShort("请输入银行卡开户行");
				return ;
			}
			
			if(!ValidatorUtil.checkMobile(mFfinder.editView(R.id.bank_phone).getText().toString())){
				ToastUtil.showShort("请输入正确的手机号码");
				return ;
			}//OPENADDR
			params.put("ACCOUNT", mFfinder.editView(R.id.bank_number).getText().toString());
			params.put("OPENADDR", mFfinder.editView(R.id.bank_type).getText().toString());
			params.put("NAME", mFfinder.editView(R.id.bank_name).getText().toString());
			params.put("PHONE", mFfinder.editView(R.id.bank_phone).getText().toString());
			params.put("OPTION", "3");
		}
		
		HttpRestClient.doHttpConsultationBackPay(params, new AsyncHttpResponseHandler(this){
			@Override
			public void onSuccess(String content) {
				super.onSuccess(content);
			try {
				JSONObject response =new JSONObject(content);
				if (response.has("errormessage")) {
					SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(),  response.getString("errormessage"));
				}else {
					if (response.optInt("CODE")==0) {
						ToastUtil.showToastPanl(response.optString("INFO"));
						setResult(RESULT_OK, getIntent());
						ConsultationBackPayActivity.this.finish();
					}
				}
			} catch (Exception e) {
			}
			}
		});
		
	}
}
