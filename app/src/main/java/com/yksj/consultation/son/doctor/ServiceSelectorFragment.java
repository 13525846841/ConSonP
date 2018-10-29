package com.yksj.consultation.son.doctor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.TextView;

import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.consultation.adapter.NavigationListAdapter;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.doctor.SelectFragment.SelectorResultListener;
import com.yksj.healthtalk.utils.ToastUtil;

/**
 * 按服务类型找专家的导航Fragment
 * @author lmk
 *
 */
public class ServiceSelectorFragment extends Fragment implements OnClickListener{
	LinearLayout navLayout;//导航布局
	private CheckBox tvServiceType,tvPrice;
	private View view,greyView;//大布局和下面的灰色区域
	private PopupWindow popupWindow;
	private LinearLayout contentLayout,rigLayout,popupWLayout;//PopupWindow内容布局
	private ListView leftListView;//服务列表
	private TextView tvPriceAll,tvFree,tvNotFree;
	private SelectorResultListener selectorListener;
	private List<Map<String,String>> services;
	private NavigationListAdapter listAdapter;
	private CheckBox lastCheckBox;
	private String qLitleDepartment="0";//小科室id
	
	private String serviceType="0",isFree="2";//默认都是全部
	private int isFrom=1;//来自哪儿1-小壹,2-导医护士
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.selector_layout2, null);
		initView();
		Bundle bundle=getArguments();
		if(bundle!=null){
			isFrom=bundle.getInt("isFrom");
			if(isFrom==1){//由小壹跳转而来
				qLitleDepartment=bundle.getString("officeCode");
			}else if(isFrom==2){//来自导医护士
				qLitleDepartment=bundle.getString("officeCode");
			}
		}
		initPopupLayout();
		if(selectorListener!=null){
			selectorListener.goNotifyLoadData(getJSONObject(),false);
		}
		return view;
	}

	//初始化上层布局
	private void initPopupLayout() {
		popupWLayout=(LinearLayout) view.findViewById(R.id.popwindow_layout);
		leftListView=(ListView) view.findViewById(R.id.pop_by_service_type);
		rigLayout=(LinearLayout) view.findViewById(R.id.pop_by_service_price_layout);
		tvPriceAll=(TextView) view.findViewById(R.id.pop_by_service_price_all);
		tvFree=(TextView) view.findViewById(R.id.pop_by_service_price_free);
		tvNotFree=(TextView) view.findViewById(R.id.pop_by_service_price_notfree);
		tvPriceAll.setOnClickListener(this);
		tvFree.setOnClickListener(this);
		tvNotFree.setOnClickListener(this);
		greyView=view.findViewById(R.id.pop_grey_view);
		greyView.getBackground().setAlpha(80);
		greyView.setOnClickListener(this);
	}

	@Override
	public void onStop() {
		shanhou();
		super.onStop();
	}
	
	/**
	 * 初始化布局
	 */
	private void initView() {
		navLayout=(LinearLayout) view.findViewById(R.id.navigationbar_radiogroup);
		tvServiceType=(CheckBox) view.findViewById(R.id.navigationbar_region);
		tvPrice=(CheckBox) view.findViewById(R.id.navigationbar_hospital);
		tvServiceType.setText(R.string.select_service);
		tvPrice.setText(R.string.is_free);
		view.findViewById(R.id.navigationbar_department).setVisibility(View.GONE);
		view.findViewById(R.id.navigationbar_doctor_title).setVisibility(View.GONE);
		tvServiceType.setOnClickListener(this);
		tvPrice.setOnClickListener(this);
//		contentLayout=(LinearLayout) LayoutInflater.from(getActivity()).inflate(R.layout.popwindow_doctor_search_by_service, null);
	}
	
	/**
	 * 
	 */
	private void showPopupWindow(final int checkedId){
		//先初始化布局
		if(popupWindow==null){
			popupWindow=new PopupWindow(contentLayout, LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT);
			leftListView=(ListView) contentLayout.findViewById(R.id.pop_by_service_type);
			rigLayout=(LinearLayout) contentLayout.findViewById(R.id.pop_by_service_price_layout);
			tvPriceAll=(TextView) contentLayout.findViewById(R.id.pop_by_service_price_all);
			tvFree=(TextView) contentLayout.findViewById(R.id.pop_by_service_price_free);
			tvNotFree=(TextView) contentLayout.findViewById(R.id.pop_by_service_price_notfree);
			tvPriceAll.setOnClickListener(this);
			tvFree.setOnClickListener(this);
			tvNotFree.setOnClickListener(this);
		}
		if(checkedId==R.id.navigationbar_region){
			leftListView.setVisibility(View.VISIBLE);
			rigLayout.setVisibility(View.GONE);
			HttpRestClient.doHttpDoctorSelectService(new AsyncServiceHandler());
		}else{
			leftListView.setVisibility(View.GONE);
			rigLayout.setVisibility(View.VISIBLE);
		}
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		popupWindow.showAsDropDown(navLayout, 0,0);

		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				CheckBox boxview=(CheckBox) view.findViewById(checkedId);
				boxview.setChecked(false);
			}
		});
	}

	@Override
	public void onClick(View v) {
		if(lastCheckBox!=null){
			if(lastCheckBox.getId()==v.getId()){
				if(!lastCheckBox.isChecked()){
					popupWLayout.setVisibility(View.GONE);
					return;
				}
			}else{
				lastCheckBox.setChecked(false);
			}
		}
		switch (v.getId()) {
		case R.id.pop_grey_view:
			shanhou();
			break;
		case R.id.navigationbar_region://点击选择服务
			lastCheckBox=tvServiceType;
			popupWLayout.setVisibility(View.VISIBLE);
			leftListView.setVisibility(View.VISIBLE);
			rigLayout.setVisibility(View.GONE);
			if(services==null){
				HttpRestClient.doHttpDoctorSelectService(new AsyncServiceHandler());
			}
			break;
		case R.id.navigationbar_hospital://点击是否收费
			lastCheckBox=tvPrice;
			popupWLayout.setVisibility(View.VISIBLE);
			leftListView.setVisibility(View.GONE);
			rigLayout.setVisibility(View.VISIBLE);
			break;
		case R.id.pop_by_service_price_all://费用不限
			tvPrice.setText(R.string.is_free);
			tvPriceAll.setTextColor(getActivity().getResources().getColor(R.color.color_green_blue_bg));
			tvFree.setTextColor(Color.BLACK);
			tvNotFree.setTextColor(Color.BLACK);
			isFree="2";
			shanhou();
			selectorListener.goNotifyLoadData(getJSONObject(),false);
			break;
		case R.id.pop_by_service_price_free:
			tvPrice.setText(R.string.free_of_charge);
			tvFree.setTextColor(getActivity().getResources().getColor(R.color.color_green_blue_bg));
			tvPriceAll.setTextColor(Color.BLACK);
			tvNotFree.setTextColor(Color.BLACK);
			isFree="0";//免费
			shanhou();
			selectorListener.goNotifyLoadData(getJSONObject(),false);
			break;
		case R.id.pop_by_service_price_notfree:
			tvPrice.setText(R.string.not_free);
			tvNotFree.setTextColor(getActivity().getResources().getColor(R.color.color_green_blue_bg));
			tvFree.setTextColor(Color.BLACK);
			tvPriceAll.setTextColor(Color.BLACK);
			isFree="1";
			shanhou();
			selectorListener.goNotifyLoadData(getJSONObject(),false);
			break;

		}
	}
	
	/**
	 * 处理善后工作,如设置上层布局隐藏,
	 */
	private void shanhou(){
		if(lastCheckBox!=null){
			lastCheckBox.setChecked(false);
			lastCheckBox=null;
		}
		popupWLayout.setVisibility(View.GONE);
	}
	
	/**
	 * 获得JSONObject,用于返回查询数据
	 * @return
	 */
	private JSONObject getJSONObject(){
		JSONObject object=new JSONObject();
		try {
			object.put("AREA", "0");
			object.put("DOCTORTITLE", 0);
			object.put("PAGENUM", 20);
			object.put("ORDERONOFF",isFree);//收费不限
			object.put("CUSTOMERID", SmartFoxClient.getLoginUserId());
			object.put("DOCTOROFFICE", qLitleDepartment);
			object.put("SERVICETYPEID",serviceType);//服务类型
			if(isFrom!=1){
				object.put("OFFICEFLAG",1);//按小科室查
				object.put("MAIN_DOCTOR_OFFICE",0);
			}
			return object;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void setSelectorListener(SelectorResultListener selectorListener) {
		this.selectorListener = selectorListener;
	}
	
	/**
	 * 获取服务类型列表的数据
	 */
	class AsyncServiceHandler extends ObjectHttpResponseHandler{
		@Override
		public Object onParseResponse(String content) {
			if(content.contains("error_message")){
				try {
					JSONObject object=new JSONObject(content);
					return object.optString("error_message");
				} catch (JSONException e) {
					e.printStackTrace();
				}
				return content;
			}
			try {
				services=new ArrayList<Map<String,String>>();
				JSONArray response=new JSONArray(content);//总的数据
				for (int i = 0; i < response.length(); i++) {
					HashMap<String, String> map = new HashMap<String, String>();//用于存放大科室数据
					JSONObject object = response.getJSONObject(i);
					if (object.has("SERVICE_TYPE")) {//服务
						map.put("code", object.optString("SERVICE_TYPE_ID"));
						map.put("name", object.optString("SERVICE_TYPE"));
						services.add(map);
					}
				}
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("code", "0");
				map.put("name", getActivity().getString(R.string.all));
				services.add(0,map);
				return services;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}
		@Override
		public void onSuccess(int statusCode, Object response) {
		    
			if(response instanceof List){
				showData();
			}else if(response instanceof String){
				ToastUtil.showShort(response.toString());
			}else{
				ToastUtil.showBasicErrorShortToast(getActivity());
			}
			super.onSuccess(statusCode, response);
		}
	}
	
	/**
	 * 显示左边服务列表
	 */
	private void showData() {
		listAdapter=new NavigationListAdapter(getActivity());
		leftListView.setAdapter(listAdapter);
		listAdapter.setDatas(services);
		listAdapter.setCurrentPosition(0);
		listAdapter.setmLeftPosition(0);
		listAdapter.setLeft(true);
		leftListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				
				listAdapter.setmRightPosition(position);
				listAdapter.notifyDataSetChanged();
				String str=listAdapter.datas.get(position).get("name");
				tvServiceType.setText("全部".equals(str)?getString(R.string.select_service):str);
				serviceType=listAdapter.datas.get(position).get("code");
				selectorListener.goNotifyLoadData(getJSONObject(),false);
				shanhou();
//				popupWindow.dismiss();
			}
		});
	}
	
}
