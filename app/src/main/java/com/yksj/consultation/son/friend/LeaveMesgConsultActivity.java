package com.yksj.consultation.son.friend;

import java.util.ArrayList;

import org.handmark.pulltorefresh.library.PullToRefreshScrollView;
import org.json.JSONException;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.TickMesg;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.JsonsfHttpResponseHandler;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.FriendHttpUtil;
import com.yksj.healthtalk.utils.JsonParseUtils;

/**
 * 留言咨询Activity
 * @author lmk
 *
 */
public class LeaveMesgConsultActivity extends BaseFragmentActivity implements OnClickListener{

	private LinearLayout contentLayout;//内容布局
	private PullToRefreshScrollView mPullScrollView;
	private LayoutInflater inflater;
	private JSONObject totalObject;
	private CustomerInfoEntity doctorInfoEntity;
	private ArrayList<TickMesg> mesgs;//门票列表
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.leave_msg_main_ui);
		initView();
		Intent intent=getIntent();
		if(intent!=null&&intent.hasExtra("doctorInfoEntity")){
			doctorInfoEntity=intent.getParcelableExtra("doctorInfoEntity");
			initData();
		}else{
			LeaveMesgConsultActivity.this.finish();
		}
	}
	
	/**
	 * 初始化View
	 */
	private void initView() {
		initTitle();
		titleLeftBtn.setOnClickListener(this);
		titleTextV.setText(R.string.leave_mesage_consult);
		contentLayout=(LinearLayout) findViewById(R.id.consult_service_contentLayout);
		mPullScrollView=(PullToRefreshScrollView) findViewById(R.id.pull_refresh_scrollview);
		mPullScrollView.setLayoutInvisible();//设置正在加载数据不可见
		inflater=LayoutInflater.from(LeaveMesgConsultActivity.this);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if(doctorInfoEntity != null)
			initData();
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		HttpRestClient.doHttpEngageTheDialogue(SmartFoxClient.getLoginUserId(),doctorInfoEntity.getId(), "1", 
				new JsonsfHttpResponseHandler(){

					@Override
					public void onSuccess(JSONObject response) {
						if (JsonParseUtils.filterErrorMessage(response) == null) {
							totalObject=response;
							onBoundData();
						}
						super.onSuccess(response);
					}

					@Override
					public void onStart() {
						mPullScrollView.setRefreshing();
						super.onStart();
					}

					@Override
					public void onFinish() {
						mPullScrollView.onRefreshComplete();
						super.onFinish();
					}
		});
	}
	/**
	 * 绑定数据
	 */
	private void onBoundData() {
		JSONArray array=(JSONArray) totalObject.get("TickMesg");
		mesgs = (ArrayList<TickMesg>) JSON.parseArray(totalObject.getJSONArray("TickMesg").toString(),TickMesg.class);
		contentLayout.removeAllViews();
		for (int i = 0; i < array.size(); i++) {
			final int x=i;
			View view=inflater.inflate(R.layout.consult_service_item, null);
			TextView tvName=(TextView) view.findViewById(R.id.consult_service_item_name);
			TextView tvPrice=(TextView) view.findViewById(R.id.consult_service_item_price);
			Button btnBuy=(Button) view.findViewById(R.id.consult_service_item_payBtn);
			final JSONObject object = array.getJSONObject(i);
			tvName.setText(object.getString("SERVICE_TYPE_SUB"));
			tvPrice.setText(object.getString("SERVICE_PRICE"));
			if(object.getInteger("ISBUY")==0){
				if("".equals(object.getString("ORDER_ID")))
					btnBuy.setText(R.string.go_buy);//去购买
				else
					btnBuy.setText(R.string.go_pay);//订单以生成,去支付
			}else{
				btnBuy.setText(R.string.go_leave_message);//支付已完成,去留言
			}
			btnBuy.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(object.getInteger("ISBUY")!=0){//支付已完成,去留言
						FriendHttpUtil.chatFromPerson(LeaveMesgConsultActivity.this, doctorInfoEntity);
					}else{
						actionPayLeaveMsg(mesgs.get(x));
					}
				}
			});
			contentLayout.addView(view);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			finish();
			break;
		}
	}
	
	private void actionPayLeaveMsg(final TickMesg mesg){
		//获取钱包余额,及订单信息
		HttpRestClient.doHttpWalletBalanceServlet(null,getParams(mesg), new AsyncHttpResponseHandler() {
			@Override
			public void onSuccess(String content) {
				try {
//					JSONObject object=new JSONObject(content);
					org.json.JSONObject object =new org.json.JSONObject(content);
					if (object.has("error_message")) {//出错
						SingleBtnFragmentDialog.showDefault(getSupportFragmentManager(), object.getString("error_message"));
						return;
					}else if (object.has("PAY_ID")) {//订单信息
					Intent intent=new Intent(LeaveMesgConsultActivity.this,ServicePayMainUi.class);
					intent.putExtra("doctorInfoEntity", doctorInfoEntity);
					intent.putExtra("mesg",mesg);
					intent.putExtra("SERVICE_TYPE", 1);
					intent.putExtra("json", object.toString());
					startActivity(intent);
					}else if(object.has("LING")){//{"LING":"1","BindPhone":"0","Balance":"0","isSetPsw":"0"}
						FriendHttpUtil.chatFromPerson(LeaveMesgConsultActivity.this, doctorInfoEntity);
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
				super.onSuccess(content);
			}
		});
	}
	
	
	/**
	 * 获取到查询钱包余额的字符串参数
	 * @return  字符串
	 */
	public String getParams(TickMesg mesg){
		RequestParams params = new RequestParams();
		params.put("Type", "MedicallyRegistered");
		params.put("DOCTORID", doctorInfoEntity.getId());
//		params.put("SELECTDATE", null);
		params.put("CUSTOMER_ID",SmartFoxClient.getLoginUserId());
		params.put("SERVICE_ITEM_ID", mesg.getSERVICE_ITEM_ID());
		params.put("SERVICE_TYPE_SUB_ID", mesg.getSERVICE_TYPE_SUB_ID());
		params.put("SERVICE_TYPE_ID", mesg.getSERVICE_TYPE_ID());
		return params.toString();
	}
}
