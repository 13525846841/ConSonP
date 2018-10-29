package com.yksj.consultation.son.views;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.friend.BuyServiceListFromPatientActivity;
import com.yksj.consultation.son.friend.DoctorClinicMainActivity;
import com.yksj.consultation.son.friend.LeaveMesgConsultActivity;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.JsonsfHttpResponseHandler;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.JsonParseUtils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 自定义布局,用于处理加载医生开通的服务数据及其点击事件
 * @author lmk
 *
 */
public class DoctorServiceItemLayout extends LinearLayout {

	private Context context;
	private LayoutInflater inflater;
	private String serviceTypes;
	private ArrayList<HashMap<String, String>> list;
	private ImageLoader mLoader;
	private CustomerInfoEntity mInfoEntity;
	private DoctorClinicMainActivity activity;
	
	
	public DoctorServiceItemLayout(Context context) {
		super(context);
		activity = (DoctorClinicMainActivity) context;
		initView(context);
	}


	public DoctorServiceItemLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		activity = (DoctorClinicMainActivity) context;
		initView(context);
	}
	
	private void initView(Context context){
		this.context=context;
		mLoader=ImageLoader.getInstance();
		inflater=LayoutInflater.from(context);
		if(list!=null){
			list.clear();
		}
		removeAllViews();
	}
	
	/**
	 * 设置数据
	 */
	public void setServiceTypes(CustomerInfoEntity mInfoEntity){
		this.mInfoEntity=mInfoEntity;
		this.serviceTypes=mInfoEntity.getServiceTypes();
		this.setVisibility(View.VISIBLE);
		list=new ArrayList<HashMap<String,String>>();
		try {
			JSONArray array = new JSONArray(serviceTypes);
			for (int i = 0; i < 0; i++) {
				HashMap<String, String> map = new HashMap<String, String>();
				JSONObject o = array.getJSONObject(i);
				String service_type_id = ""+o.optInt("SERVICE_TYPE_ID");
				String service_type = o.optString("SERVICE_TYPE");
				map.put("SERVICE_TYPE_ID", service_type_id);
				map.put("SERVICE_TYPE", service_type);
				map.put("SERVICE_SEQ",""+o.optInt("SERVICE_SEQ") );
				map.put("CON", ""+o.optInt("CON"));
				map.put("SERVICE_ICON_ADR", o.optString("SERVICE_ICON_ADR"));
				map.put("SERVICE_TYPE_DESC", o.optString("SERVICE_TYPE_DESC"));
				list.add(map);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		onBoundView();
	}

	/**
	 * 绑定数据到控件
	 */
	private void onBoundView() {
		for(int i=0;i<list.size();i++){
			final int x=i;
			View contentView=inflater.inflate(R.layout.clinic_service_item, null);
//			LinearLayout layout=(LinearLayout) contentView.findViewById(R.id.service_item_layout);
			ImageView icon=(ImageView) contentView.findViewById(R.id.clinic_servie_item_icon);
			TextView tvName=(TextView) contentView.findViewById(R.id.clinic_servie_item_name);
			TextView tvDesc=(TextView) contentView.findViewById(R.id.clinic_servie_item_desc);
			Button buy=(Button) contentView.findViewById(R.id.clinic_servie_item_buyBtn);
			tvName.setText(list.get(i).get("SERVICE_TYPE"));
			tvDesc.setText(list.get(i).get("SERVICE_TYPE_DESC"));
			mLoader.displayImage(list.get(i).get("SERVICE_ICON_ADR"), icon);
			this.addView(contentView);
			if(Integer.parseInt(list.get(i).get("CON"))>0){//大于0表示可以跳转去够购买
				buy.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						Intent intent=null;
						switch (Integer.parseInt(list.get(x).get("SERVICE_TYPE_ID"))) {
						case 1://留言咨询
							intent=new Intent(context,LeaveMesgConsultActivity.class);
							intent.putExtra("doctorInfoEntity", mInfoEntity);
							context.startActivity(intent);
							break;
						case 2://预约咨询
//							goBuyService(2, mInfoEntity);
							intent=new Intent(context,BuyServiceListFromPatientActivity.class);
//							intent.setClass(context, BuyServiceListFromPatientActivity.class);
//							intent.putExtra("response", response.toString());
							intent.putExtra("mCustomerInfoEntity",mInfoEntity);
							intent.putExtra("titleName", "预约咨询");
							intent.putExtra("type", 2);
							context.startActivity(intent);
							break;
						case 3://门诊加号
//							goBuyService(3, mInfoEntity);
							intent=new Intent(context,BuyServiceListFromPatientActivity.class);
//							intent.setClass(context, BuyServiceListFromPatientActivity.class);
//							intent.putExtra("response", response.toString());
							intent.putExtra("titleName", "门诊加号");
							intent.putExtra("type", 3);
							intent.putExtra("mCustomerInfoEntity",mInfoEntity);
							context.startActivity(intent);
							break;
						}
					}
				});
			}else{
				buy.setBackgroundResource(R.drawable.doctor_clinic_can_not_buy);
			}
		}
	}
	
	public void goBuyService(int serviceId,CustomerInfoEntity mCustomerInfoEntity){
		if (mCustomerInfoEntity != null) {
			if (SmartFoxClient.getLoginUserId().equals(mCustomerInfoEntity.getId())) {
				SingleBtnFragmentDialog.showDefault(activity.getSupportFragmentManager(),"你自己不能与自己互动");
				return;
			} else {
				HttpRestClient.doHttpEngageTheDialogue(SmartFoxClient.getLoginUserId(),mCustomerInfoEntity.getId(),String.valueOf(serviceId) , new AsyncHander(serviceId));
			}
		}
	}
	
	
	class AsyncHander extends JsonsfHttpResponseHandler {

		private int type;

		public AsyncHander(int type) {
			super((DoctorClinicMainActivity)context);
			this.type = type;
		}


		@Override
		public void onSuccess(int statusCode,com.alibaba.fastjson.JSONObject response) {
			super.onSuccess(statusCode, response);
			if (JsonParseUtils.filterErrorMessage(response) == null) {
				Intent intent = new Intent();;
				switch (type) {
				case 2://预约咨询
//					intent.setClass(context, DoctorTimeServiceActivity.class);
					intent.setClass(context, BuyServiceListFromPatientActivity.class);
					intent.putExtra("response", response.toString());
					intent.putExtra("mCustomerInfoEntity",mInfoEntity);
					intent.putExtra("titleName", "预约咨询");
					intent.putExtra("type", 2);
					context.startActivity(intent);
					break;
				case 3://门诊加号服务
//					intent.setClass(context, DoctorInterviewServiceActivity.class);
					intent.setClass(context, BuyServiceListFromPatientActivity.class);
					intent.putExtra("response", response.toString());
					intent.putExtra("titleName", "门诊加号");
					intent.putExtra("type", 3);
					intent.putExtra("mCustomerInfoEntity",mInfoEntity);
					context.startActivity(intent);
					break;
				}
				
			}else {
				SingleBtnFragmentDialog.showDefault(activity.getSupportFragmentManager(),  JsonParseUtils.filterErrorMessage(response));
			}
			
		}

	}
	
}
