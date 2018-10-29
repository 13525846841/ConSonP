package com.yksj.consultation.son.doctor;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.graphics.drawable.BitmapDrawable;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.yksj.healthtalk.db.DictionaryHelper;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.consultation.adapter.NavigationListAdapter;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.ToastUtil;

public class SelectFragment extends Fragment implements OnClickListener{
	private LayoutInflater inflater;
	LinearLayout navLayout;//导航布局
	private PopupWindow popupWindow;
	private LinearLayout contentLayout;//PopupWindow内容布局
	private LinearLayout popupWLayout,listLayout,editLayout;//列表布局,输入布局
	private int lastCheckedId=0;//最后一次被点击的RadioButton
	private ListView firstList,secondList;//一级,二级菜单
	private EditText editText;//搜索医院输入框
	private Button btnSearch;
	private View view;
	private CheckBox tvRegin,tvHospital,tvDepartment,tvDoctorTitle,lastCheckBox;
	private List<Map<String, String>> proList,bigDepartments;//省级集合（第一级）,大科室列表（第一级）
	private HashMap<String, List<Map<String, String>>> citysMap,littleDepartments;//省辖市集合（第二级）,小科室(第二级)
	private List<Map<String, String>> titles;
	private SelectorResultListener selectorListener;//用于回调得接口
	private NavigationListAdapter firstAdapter;//左右ListView的适配器
	private NavigationListAdapter secondAdapter,keshiAdapter,zhicAdapter;
	private String area="全部",office="科室";//全部
	private String qProvince="0",qCity="0",qHospital="",qBigDepartment="0",qLitleDepartment="0",qTitle="0";//回调查询的条件
	//记录下每个item所处的位置
	private int proPosition=0,cityPosition=0,bigDepPosition=0,littleDepPsition=0,docPosition=-1;
	WindowManager.LayoutParams windowLp;
	View greyView;
	private String merchantid;//商户id,只有从医疗机构的医生服务跳转过来才传递商户id,然后根据商户id去查询
	private int isFrom=1;//来自哪儿1-小壹,2-导医护士
	
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view =inflater.inflate(R.layout.selector_layout, null);
		initView();
		Bundle bundle=getArguments();
		if(bundle!=null){
			isFrom=bundle.getInt("isFrom");
			if(isFrom==1){//由小壹跳转而来
				qLitleDepartment=bundle.getString("officeCode");
				tvHospital.setVisibility(View.GONE);
				tvDepartment.setVisibility(View.GONE);
				view.findViewById(R.id.navigationbar_second_line).setVisibility(View.GONE);
				view.findViewById(R.id.navigationbar_third_line).setVisibility(View.GONE);
			}else if(isFrom==2){
				qLitleDepartment=bundle.getString("officeCode");
				tvDepartment.setClickable(false);//科室不可点击
			}
			
//			if(bundle.containsKey("merchantid")){//根据商户id为前提查询
//				merchantid=bundle.getString("merchantid");
//			}
			
		}
		initPopupLayout();
		if(selectorListener!=null){
			selectorListener.goNotifyLoadData(getSearchString(),true);
		}
		return view;
	}
	
	private void initView() {
		windowLp=getActivity().getWindow().getAttributes();
		navLayout=(LinearLayout) view.findViewById(R.id.navigationbar_radiogroup);
		tvRegin=(CheckBox) view.findViewById(R.id.navigationbar_region);
		tvHospital=(CheckBox) view.findViewById(R.id.navigationbar_hospital);
		tvDepartment=(CheckBox) view.findViewById(R.id.navigationbar_department);
		tvDoctorTitle=(CheckBox) view.findViewById(R.id.navigationbar_doctor_title);
		tvRegin.setOnClickListener(this);
		tvHospital.setOnClickListener(this);
		tvDepartment.setOnClickListener(this);
		tvDoctorTitle.setOnClickListener(this);
		firstAdapter = new NavigationListAdapter(getActivity());
		secondAdapter = new NavigationListAdapter(getActivity());
		keshiAdapter = new NavigationListAdapter(getActivity());//科室的右边list适配器
		zhicAdapter = new NavigationListAdapter(getActivity());//科室的右边list适配器
		secondAdapter.setLeft(true);//是左边的适配器
		zhicAdapter.setLeft(true);//是左边的适配器
		keshiAdapter.setLeft(true);//是左边的适配器
		
	}

	private void initPopLayout() {
		inflater=LayoutInflater.from(getActivity());
		contentLayout=(LinearLayout) inflater.inflate(R.layout.popwindow_doctor_search, null);
	}

	/**
	 * 用于选择器选择后回调的接口,后续操作
	 */
	public interface SelectorResultListener{
		//加载数据传的参数,res查询传递服务器的参数,which为按条件查询true还是按服务false
		public void goNotifyLoadData(JSONObject res,boolean isCondition);
	}

	public void setSelectorListener(SelectorResultListener selectorListener) {
		this.selectorListener = selectorListener;
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
		case R.id.pop_input_btn:
			String hospital=editText.getText().toString().trim();
			editText.setText("");
			if(hospital!=null&&hospital.length()!=0){//用户输入了数据
				tvHospital.setText(hospital);
				qHospital=hospital;
			}else{//没有输入数据
				tvHospital.setText(getActivity().getString(R.string.hospital));
				qHospital="";
			}
//			popupWindow.dismiss();
			shanhou();
			selectorListener.goNotifyLoadData(getSearchString(),true);
			break;
		case R.id.navigationbar_hospital://医院
			popupWLayout.setVisibility(View.VISIBLE);
			lastCheckBox=tvHospital;
			listLayout.setVisibility(View.GONE);
			editLayout.setVisibility(View.VISIBLE);
			btnSearch.setOnClickListener(this);
			break;
		case R.id.navigationbar_region://地区
			popupWLayout.setVisibility(View.VISIBLE);
			lastCheckBox=tvRegin;
			listLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 500));
			listLayout.setVisibility(View.VISIBLE);
			editLayout.setVisibility(View.GONE);
			firstList.setVisibility(View.VISIBLE);
			secondList.setBackgroundColor(getActivity().getResources().getColor(R.color.color_index_gray_bg));
			if(citysMap==null)//没有数据则查询
				queryCity();
			secondList.setAdapter(secondAdapter);
			secondAdapter.clearDatas();
			firstAdapter.setDatas(proList);
			firstAdapter.setmSelectedPosition(proPosition, null);
			secondAdapter.setDatas(citysMap.get(proList.get(proPosition).get("name")));
			firstList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						final int position1, long id) {
					proPosition=position1;
					secondAdapter.setCurrentPosition(proPosition);
					firstAdapter.setmSelectedPosition(proPosition, view);
					firstAdapter.notifyDataSetChanged();
//					area=(position1==0?"地区":proList.get(position1).get("name"));
					area=proList.get(position1).get("name");
					secondAdapter.setDatas(citysMap.get(proList.get(position1).get("name")));
				}
			});	
			secondList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					qProvince=proList.get(proPosition).get("code");
					String result=secondAdapter.datas.get(position).get("name");
					if(!"全部".equals(result)){
						tvRegin.setText(result);
						qCity=secondAdapter.datas.get(position).get("code");
					}else{
						tvRegin.setText(area);
						qCity=qProvince;//如果点击某一个省的全部,则编号为省
					}
					cityPosition=position;
					secondAdapter.setmLeftPosition(proPosition);
					secondAdapter.setmRightPosition(cityPosition);
					secondAdapter.notifyDataSetChanged();
					selectorListener.goNotifyLoadData(getSearchString(),true);
					shanhou();
				}
			});
			
			break;
		case R.id.navigationbar_department://科室
			popupWLayout.setVisibility(View.VISIBLE);
			lastCheckBox=tvDepartment;
			listLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 500));
			listLayout.setVisibility(View.VISIBLE);
			editLayout.setVisibility(View.GONE);
			firstList.setVisibility(View.VISIBLE);
			secondList.setBackgroundColor(getActivity().getResources().getColor(R.color.color_index_gray_bg));
			firstAdapter.clearDatas();
			secondAdapter.clearDatas();
			zhicAdapter.clearDatas();
			if(bigDepartments==null||bigDepartments.size()==0){
				HttpRestClient.doHttpFindkeshi(new AsyncDepartmentsHandler());
			}else{
				showDepartment();
			}
			break;
		case R.id.navigationbar_doctor_title://医生职称
			popupWLayout.setVisibility(View.VISIBLE);
			lastCheckBox=tvDoctorTitle;
			listLayout.setLayoutParams(new android.widget.LinearLayout.LayoutParams(
					android.widget.LinearLayout.LayoutParams.MATCH_PARENT, android.widget.LinearLayout.LayoutParams.WRAP_CONTENT));
			listLayout.setVisibility(View.VISIBLE);
			editLayout.setVisibility(View.GONE);
			firstList.setVisibility(View.GONE);
			secondList.setAdapter(zhicAdapter);
			secondList.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
			if(titles==null){
				initDoctorTitle();
			}
			zhicAdapter.setDatas(titles);
			zhicAdapter.setCurrentPosition(0);
			zhicAdapter.setmLeftPosition(0);
			secondList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String result=zhicAdapter.datas.get(position).get("name");
					if(!"全部".equals(result)){
						tvDoctorTitle.setText(result);
						qTitle=zhicAdapter.datas.get(position).get("code");
					}else{
						tvDoctorTitle.setText("职称");
						qTitle="0";
					}
					zhicAdapter.setmRightPosition(position);
					zhicAdapter.notifyDataSetChanged();
					shanhou();
					selectorListener.goNotifyLoadData(getSearchString(),true);
				}
			
			});
			break;
		}
	}
	
	/**
	 * 处理善后工作,如设置上层布局隐藏,
	 */
	private void shanhou(){
		/*CheckBox box=(CheckBox) view.findViewById(lastCheckedId);
		box.setChecked(false);*/
		if(lastCheckBox!=null){
			lastCheckBox.setChecked(false);
			lastCheckBox=null;
		}
		popupWLayout.setVisibility(View.GONE);
	}
	
	@Override
	public void onStop() {
		shanhou();
		super.onStop();
	}
	
	/**
	 * 默认,弹出布局是隐藏的(这里是悬浮在上面的布局)
	 */
	private void initPopupLayout(){
		popupWLayout=(LinearLayout) view.findViewById(R.id.popwindow_layout);
		listLayout=(LinearLayout) view.findViewById(R.id.pop_list_layout);//列表布局
		editLayout=(LinearLayout) view.findViewById(R.id.pop_input_layout);//输入医院
		firstList=(ListView) view.findViewById(R.id.pop_first_list);
		secondList=(ListView) view.findViewById(R.id.pop_second_list);
		firstList.setAdapter(firstAdapter);
		secondList.setAdapter(secondAdapter);
		editText=(EditText) view.findViewById(R.id.pop_input_edit);
		btnSearch=(Button) view.findViewById(R.id.pop_input_btn);
		greyView=view.findViewById(R.id.pop_grey_view);
		greyView.getBackground().setAlpha(80);
		greyView.setOnClickListener(this);
	
	}
	
	private void showPoppupWindow(final int checkedId) {
//		windowLp.alpha=0.7f;
//		getActivity().getWindow().setAttributes(windowLp);
//		contentLayout.getBackground().setAlpha(30);
		//先初始化布局
		if(popupWindow==null){
//			popupWindow=new PopupWindow(contentLayout, LayoutParams.MATCH_PARENT, 495);
			popupWindow=new PopupWindow(contentLayout, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			listLayout=(LinearLayout) contentLayout.findViewById(R.id.pop_list_layout);
			editLayout=(LinearLayout) contentLayout.findViewById(R.id.pop_input_layout);
			firstList=(ListView) contentLayout.findViewById(R.id.pop_first_list);
			secondList=(ListView) contentLayout.findViewById(R.id.pop_second_list);
			firstList.setAdapter(firstAdapter);
			secondList.setAdapter(secondAdapter);
			editText=(EditText) contentLayout.findViewById(R.id.pop_input_edit);
			btnSearch=(Button) contentLayout.findViewById(R.id.pop_input_btn);
			greyView=contentLayout.findViewById(R.id.pop_grey_view);
			greyView.getBackground().setAlpha(80);
			greyView.setOnClickListener(this);
		}
		switch (checkedId) {
		case R.id.navigationbar_region://地区
//			popupWindow.setHeight(495);
			listLayout.setVisibility(View.VISIBLE);
			editLayout.setVisibility(View.GONE);
			firstList.setVisibility(View.VISIBLE);
			secondList.setBackgroundColor(getActivity().getResources().getColor(R.color.color_index_gray_bg));
			if(citysMap==null)//没有数据则查询
				queryCity();
			secondAdapter.clearDatas();
			firstAdapter.setDatas(proList);
			firstAdapter.setmSelectedPosition(proPosition, null);
			secondAdapter.setDatas(citysMap.get(proList.get(proPosition).get("name")));
			firstList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						final int position1, long id) {
					proPosition=position1;
					firstAdapter.setmSelectedPosition(proPosition, view);
					firstAdapter.notifyDataSetChanged();
//					area=(position1==0?"地区":proList.get(position1).get("name"));
					area=proList.get(position1).get("name");
					secondAdapter.setDatas(citysMap.get(proList.get(position1).get("name")));
				}
			});	
			secondList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
					qProvince=proList.get(proPosition).get("code");
					String result=secondAdapter.datas.get(position).get("name");
					if(!"全部".equals(result)){
						tvRegin.setText(result);
						qCity=secondAdapter.datas.get(position).get("code");
					}else{
						tvRegin.setText(area);
						qCity=qProvince;//如果点击某一个省的全部,则编号为省
					}
					selectorListener.goNotifyLoadData(getSearchString(),true);
//					popupWindow.dismiss();
//					popupWLayout.setVisibility(View.GONE);
					shanhou();
				}
			});
			break;
			
		case R.id.navigationbar_hospital://医院
			popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
			listLayout.setVisibility(View.GONE);
			editLayout.setVisibility(View.VISIBLE);
			btnSearch.setOnClickListener(this);
			break;
		case R.id.navigationbar_department://科室
//			popupWindow.setHeight(495);
			listLayout.setVisibility(View.VISIBLE);
			editLayout.setVisibility(View.GONE);
			firstList.setVisibility(View.VISIBLE);
			secondList.setBackgroundColor(getActivity().getResources().getColor(R.color.color_index_gray_bg));
			firstAdapter.clearDatas();
			secondAdapter.clearDatas();
			if(bigDepartments==null||bigDepartments.size()==0){
				HttpRestClient.doHttpFindkeshi(new AsyncDepartmentsHandler());
			}else{
				showDepartment();
			}
			break;
		case R.id.navigationbar_doctor_title://职称
			popupWindow.setHeight(LayoutParams.WRAP_CONTENT);
			listLayout.setVisibility(View.VISIBLE);
			editLayout.setVisibility(View.GONE);
			firstList.setVisibility(View.GONE);
			secondList.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
			if(titles==null){
				initDoctorTitle();
			}
			secondAdapter.setDatas(titles);
			secondList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					String result=secondAdapter.datas.get(position).get("name");
					if(!"全部".equals(result)){
						tvDoctorTitle.setText(result);
						qTitle=secondAdapter.datas.get(position).get("code");
					}else{
						tvDoctorTitle.setText("职称");
						qTitle="0";
					}
					selectorListener.goNotifyLoadData(getSearchString(),true);
					shanhou();			
				}
			
			});
			break;
		}
		popupWindow.setTouchable(true);
		popupWindow.setFocusable(true);
		popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);
//		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		popupWindow.setBackgroundDrawable(new BitmapDrawable()); 
		popupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
		popupWindow.showAsDropDown(navLayout, 0,0);

		popupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				windowLp.alpha=1f;
				getActivity().getWindow().setAttributes(windowLp);
				CheckBox boxview=(CheckBox) view.findViewById(checkedId);
				boxview.setChecked(false);
			}
		});
		lastCheckedId=checkedId;
	}
	
	// 查询地区
	private void queryCity() {
		citysMap = new HashMap<String, List<Map<String, String>>>();
		proList = DictionaryHelper.getInstance(getActivity()).setCityData(getActivity(),
				citysMap);
		// 每项加个全部
		for (Map<String, String> map : proList) {
			HashMap<String, String> hashMap = new HashMap<String, String>();
			hashMap.put("name", getActivity().getString(R.string.limit_no));
			hashMap.put("code", map.get("code"));
			String name = map.get("name");
			citysMap.get(name).add(0, hashMap);
		}
		HashMap<String, String> hashMap = new HashMap<String, String>();
		hashMap.put("name", getActivity().getString(R.string.limit_no));//全部
		hashMap.put("code", "0");
		proList.add(0, hashMap);
		
		List<Map<String, String>> arr = new ArrayList<Map<String, String>>();
		arr.add(hashMap);
		citysMap.put(getActivity().getString(R.string.limit_no), arr);
		
	}
	
	// 查询职称
	private void queryDoctorTitle() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				titles = new ArrayList<Map<String,String>>();
				HashMap<String, String> map=new HashMap<String, String>();
				map.put(getActivity().getString(R.string.limit_no), "0");
				titles.add(map);
				titles.addAll(DictionaryHelper
						.getInstance(getActivity()).querydoctorTitle(getActivity()));
				secondAdapter.setDatas(titles);
			}
		}).start();
	}
	
	/**
	 * 获取大小科室列表数据,并解析成列表
	 */
	class AsyncDepartmentsHandler extends ObjectHttpResponseHandler{

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
				bigDepartments=new ArrayList<Map<String,String>>();
				littleDepartments=new HashMap<String, List<Map<String,String>>>();
				JSONArray response=new JSONArray(content);//总的数据
				for (int i = 0; i < response.length(); i++) {
					List<Map<String, String>> list = new ArrayList<Map<String, String>>();//用于存放小科室
					HashMap<String, String> map = new HashMap<String, String>();//用于存放大科室数据
					JSONObject object = response.getJSONObject(i);
					if (object.has("OFFICE_NAME")) {//科室
						map.put("code", object.optString("OFFICE_CODE"));
						map.put("name", object.optString("OFFICE_NAME"));
						bigDepartments.add(map);//大科室添加一次
						JSONArray array=object.optJSONArray("SUB_OFFICE");//开始解析小科室
						for(int j=0;j<array.length();j++){
							HashMap<String, String> littlemap=new HashMap<String, String>();
							JSONObject obj = array.getJSONObject(j);
							if (obj.has("OFFICE_NAME")) {//科室
								littlemap.put("code", obj.optString("OFFICE_CODE"));
								littlemap.put("name", obj.optString("OFFICE_NAME"));
								list.add(littlemap);
							}
						}
						HashMap<String, String> allLittle=new HashMap<String, String>();
						allLittle.put("code", "0");
						allLittle.put("name",  getActivity().getString(R.string.all));
						list.add(0, allLittle);
						littleDepartments.put(object.optString("OFFICE_NAME"), list);
					}
				}
				List<Map<String, String>> list = new ArrayList<Map<String, String>>();
				HashMap<String, String> map = new HashMap<String, String>();
				map.put("code", "0");
				map.put("name", getActivity().getString(R.string.all));
				bigDepartments.add(0,map);
				list.add(map);
				littleDepartments.put(getActivity().getString(R.string.all), list);
				return bigDepartments;
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		public void onSuccess(int statusCode, Object response) {
		    
			if(response instanceof List){
				showDepartment();
			}else if(response instanceof String){
				ToastUtil.showShort(response.toString());
			}else{
				ToastUtil.showBasicErrorShortToast(getActivity());
			}
			super.onSuccess(statusCode, response);
		}
	}
	//展示科室列表
	private void showDepartment() {
		firstAdapter.setDatas(bigDepartments);
		firstList.setAdapter(firstAdapter);
		firstAdapter.setmSelectedPosition(bigDepPosition, null);
		secondList.setAdapter(keshiAdapter);
		keshiAdapter.setDatas(littleDepartments.get(bigDepartments.get(bigDepPosition).get("name")));
		firstList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position1, long id) {
				bigDepPosition=position1;
				keshiAdapter.setCurrentPosition(bigDepPosition);
				firstAdapter.setmSelectedPosition(bigDepPosition, view);
				firstAdapter.notifyDataSetChanged();
				keshiAdapter.setDatas(littleDepartments.get(bigDepartments.get(position1).
						get("name")));
			}
		});
		secondList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String result1=bigDepartments.get(bigDepPosition).get("name");
				if(!"全部".equals(result1)){
					office=bigDepartments.get(bigDepPosition).get("name");
					qBigDepartment=bigDepartments.get(bigDepPosition).get("code");//大科室id
				}else{
					office="科室";
					qBigDepartment="0";
				}
				
				String result=keshiAdapter.datas.get(position).get("name");
				if(!"全部".equals(result)){
					tvDepartment.setText(result);
					qLitleDepartment=keshiAdapter.datas.get(position).get("code");
				}else{
					tvDepartment.setText(office);
					qLitleDepartment="0";
				}
				littleDepPsition=position;
				keshiAdapter.setmLeftPosition(bigDepPosition);
				keshiAdapter.setmRightPosition(littleDepPsition);
//				popupWindow.dismiss();
//				popupWLayout.setVisibility(View.GONE);
				selectorListener.goNotifyLoadData(getSearchString(),true);
				shanhou();
			}
		});
	}
	
	//展示医生职称列表
	private void initDoctorTitle() {
		String[] names={"全部","主任医师","副主任医师","主治医师","医师"};
		titles=new ArrayList<Map<String,String>>();
		for(int i=0;i<names.length;i++){
			HashMap<String, String> map=new HashMap<String, String>();
			map.put("name", names[i]);
			map.put("code",""+i);
			titles.add(map);
		}
	}
	
	
	//遍历得到一个Json字符串,作为参数返回去进行查询
	private JSONObject getSearchString() {
		JSONObject object=new JSONObject();
		try {
			object.put("AREA", qCity);//城市
			object.put("SERVICETYPEID",0);//服务类型ID
			object.put("ORDERONOFF", 2);//收费不限
			object.put("DOCTORTITLE", qTitle);//医生职称
			object.put("DOCTOROFFICE", qLitleDepartment);//小科室
			if(isFrom!=1){//如果不是来自多美小壹需要传递这些参数
				object.put("OFFICEFLAG",1);//按小科室查
				object.put("MAIN_DOCTOR_OFFICE",qBigDepartment);//大科室
				object.put("DOCTORHOSPITAL",qHospital);//医院
			}
			object.put("PAGENUM", 20);
			object.put("CUSTOMERID", SmartFoxClient.getLoginUserId());
			return object;
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return null;
	}

		
}
