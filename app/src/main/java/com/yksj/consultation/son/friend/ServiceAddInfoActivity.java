package com.yksj.consultation.son.friend;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.consultation.outpatient.AtyOutPatientDetail;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.SystemUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.ValidatorUtil;

import de.greenrobot.event.EventBus;

/**
 * 服务为免费的  填写需要的资料信息
 * @author jack_tang
 *
 */
public class ServiceAddInfoActivity extends BaseFragmentActivity implements OnClickListener {

	private EditText mName;
	private EditText mPhone;
	private EditText mRemark;
	private org.json.JSONObject json;


	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.service_addinfo_activity_layout);
		initView();
	}

	private void initView() {
		initTitle();
		titleTextV.setText("填写患者资料");
		titleLeftBtn.setOnClickListener(this);
		try {
			json = new org.json.JSONObject(getIntent().getStringExtra("response"));
			findViewById(R.id.chat_action).setOnClickListener(this);
			mName = (EditText) findViewById(R.id.service_pay_basic_info_name2);
			mPhone = (EditText) findViewById(R.id.service_pay_basic_info_phone2);
			mRemark = (EditText) findViewById(R.id.service_pay_basic_info_remark2);
			
			
			if("2".equals(json.getString("SERVICE_TYPE_ID"))){//只显示备注
				findViewById(R.id.phone_view).setVisibility(View.GONE);
				findViewById(R.id.name_view).setVisibility(View.GONE);
			}
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			onBackPressed();
			break;
		case R.id.chat_action:
			actionBuy();
			break;
		}
	}
	
	protected void actionBuy() {
		
		SystemUtils.hideSoftBord(getApplicationContext(), mName);
		final RequestParams params = new RequestParams();
		try {
			
			if ("3".equals(json.getString("SERVICE_TYPE_ID")) && !ValidatorUtil.checkMobile(mPhone.getText().toString())){
				ToastUtil.showShort("请输入正确的手机号码!");
				return ;
			}
			params.put("DOCTORID", json.getString("CUSTOMER_ID"));	
			params.put("Type", "MedicallyRegistered330");
			params.put("SERVICE_ITEM_ID", json.getString("SERVICE_ITEM_ID"));
//			params.put("CONSULTATIONID", getIntent().getStringExtra("consultId"));
			params.put("ORDER_ID", json.optString("ORDER_ID"));
			params.put("SERVICE_TYPE_ID", json.getString("SERVICE_TYPE_ID"));
			params.put("SERVICE_TYPE_SUB_ID", "8");
			params.put("SELECTDATE", json.getString("SERVICE_TIME_BEGIN").substring(0,8));
			if("3".equals(json.getString("SERVICE_TYPE_ID")) ){
				params.put("ADVICE_CONTENT", mRemark.getText().toString());//咨询内容
				params.put("PATIENT_NAME", mName.getText().toString());//名字
				params.put("PATIENT_PHONE", mPhone.getText().toString());//手机
			}
			params.put("CUSTOMER_ID", LoginServiceManeger.instance().getLoginUserId());
		} catch (Exception e) {
			return ;
		}
		HttpRestClient.doHttpSERVICESETSERVLET44(params, new AsyncHttpResponseHandler(this) {
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					Object object = JSON.parse(content);
					if (object instanceof JSONObject) {
						final JSONObject object2 = (JSONObject) object;
						if(object2.getIntValue("code")==1){
							DoubleBtnFragmentDialog.showDoubleBtn(ServiceAddInfoActivity.this, "预约成功", object2.getJSONObject("result").getString("MESSAGE"),
									"查看详细", "关闭", new DoubleBtnFragmentDialog.OnFristClickListener() {
										@Override
										public void onBtn1() {
											ToastUtil.showShort("查看详细");
											Intent intent = new Intent(ServiceAddInfoActivity.this,AtyOutPatientDetail.class);
											intent.putExtra("ORIDERID", object2.getJSONObject("result").getString("ORDERID"));
											intent.putExtra("type",1);
											startActivity(intent);
											EventBus.getDefault().post(new MyEvent("refresh", 2));
											ServiceAddInfoActivity.this.finish();
										}
									}, new DoubleBtnFragmentDialog.OnSecondClickListener() {
										@Override
										public void onBtn2() {
											ToastUtil.showShort("关闭");
											onBackPressed();
											EventBus.getDefault().post(new MyEvent("refresh", 2));
											ServiceAddInfoActivity.this.finish();
										}
									}).show();
						}else {
							String str=object2.getString("message");
							SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), str);
						}

					}
				} catch (Exception e) {
				}
				super.onSuccess(statusCode, content);

			}
		});
	}
	
}
