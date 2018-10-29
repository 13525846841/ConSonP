package com.yksj.consultation.son.doctor;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.NavigationListAdapter;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.db.DictionaryHelper;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.utils.ToastUtil;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NavigateFragment extends Fragment implements OnClickListener{
	private LayoutInflater inflater;
	LinearLayout navLayout;//导航布局
	private PopupWindow popupWindow;
	private LinearLayout contentLayout;//PopupWindow内容布局
	private LinearLayout popupWLayout,listLayout,otherLayout;//列表布局,输入布局
	private int lastCheckedId=0;//最后一次被点击的RadioButton
	private ListView firstList,secondList,otherList,assesslist;//一级,二级菜单
	private View view;
	private CheckBox tvRegin,tvHospital,lastCheckBox,tvOffice;
	private List<Map<String, String>> proList;//省级集合（第一级）,大科室列表（第一级）
	private HashMap<String, List<Map<String, String>>> citysMap;//省辖市集合（第二级）,小科室(第二级)
	private List<Map<String, String>> titles;
	private SelectorResultListener selectorListener;//用于回调得接口
	private NavigationListAdapter firstAdapter;//左右ListView的适配器
	private NavigationListAdapter secondAdapter,otherAdapter;
	private String area="全部";//全部
	private String qProvince="0",qCity="0",qHospital="",qBigDepartment="0",qLitleDepartment="0",qTitle="0";//回调查询的条件
	//记录下每个item所处的位置
	private int proPosition=0,cityPosition=0;
	WindowManager.LayoutParams windowLp;
	View greyView;
	private String areaCode="";
	private String officeName="";
	DisplayMetrics dm= new DisplayMetrics();
	private RelativeLayout listLayout2;
	private String Star_level = "";
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		view =inflater.inflate(R.layout.navigation_layout, null);
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
		initView();
		initPopupLayout();
		if(selectorListener!=null){
			selectorListener.goNotifyLoadData("","","");
		}
		return view;
	}

	private void initView() {
		windowLp=getActivity().getWindow().getAttributes();
		navLayout=(LinearLayout) view.findViewById(R.id.navigationbar_radiogroup);
		tvRegin=(CheckBox) view.findViewById(R.id.navigationbar_region);
		tvHospital=(CheckBox) view.findViewById(R.id.navigationbar_hospital);
		tvOffice = (CheckBox) view.findViewById(R.id.navigationbar_office);

		tvRegin.setOnClickListener(this);
		tvHospital.setOnClickListener(this);
		tvOffice.setOnClickListener(this);

		firstAdapter = new NavigationListAdapter(getActivity());
		secondAdapter = new NavigationListAdapter(getActivity());
		otherAdapter = new NavigationListAdapter(getActivity());
		secondAdapter.setLeft(true);//是左边的适配器
	}

	/**
	 * 用于选择器选择后回调的接口,后续操作
	 */
	public interface SelectorResultListener{
		//加载数据传的参数,res查询传递服务器的参数,which为按条件查询true还是按服务false
		public void goNotifyLoadData(String areaCode, String unitCode,String Star_level);
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
		case R.id.navigationbar_hospital://医院
			if(qCity==null||qCity.length()<2){
				ToastUtil.showShort(getActivity(),"您必须先选择地区");
				if(lastCheckBox!=null){
					lastCheckBox.setChecked(false);
					lastCheckBox=null;
				}
				return;
			}
			popupWLayout.setVisibility(View.VISIBLE);
			lastCheckBox=tvHospital;
			listLayout.setVisibility(View.GONE);
			otherLayout.setVisibility(View.VISIBLE);
			otherLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
					dm.heightPixels / 2));
			otherList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					Map<String,String> map=otherAdapter.datas.get(position);
					if (!"全部".equals(map.get("name")))
						//tvHospital.setText("医院");
					    tvHospital.setText(otherAdapter.datas.get(position).get("name"));
					else
					    tvHospital.setText("医院");
						//tvHospital.setText(otherAdapter.datas.get(position).get("name"));
					selectorListener.goNotifyLoadData(areaCode, otherAdapter.datas.get(position).get("code"),Star_level);
					shanhou();
				}
			});
			if(!areaCode.equals(qCity)){
				areaCode=qCity;
				queryHospital(areaCode);
			}

			break;
		case R.id.navigationbar_region://地区
			popupWLayout.setVisibility(View.VISIBLE);
			lastCheckBox=tvRegin;
			listLayout.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.MATCH_PARENT, dm.heightPixels/2));
			listLayout.setVisibility(View.VISIBLE);
			otherLayout.setVisibility(View.GONE);
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
					areaCode="";
					otherAdapter.datas.clear();
					otherAdapter.notifyDataSetChanged();
					tvHospital.setText("医院");
					selectorListener.goNotifyLoadData(qCity, areaCode,Star_level);
					shanhou();
				}
			});

			break;
			case R.id.navigationbar_office://评价
//              firstList2.setAdapter(firstAdapter2);
//              secondList2.setAdapter(secondAdapter2);
				popupWLayout.setVisibility(View.VISIBLE);
				lastCheckBox = tvOffice;
				listLayout2.setLayoutParams(new LinearLayout.LayoutParams(
						LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT ));
				listLayout2.setVisibility(View.VISIBLE);
				listLayout.setVisibility(View.GONE);
				otherLayout.setVisibility(View.GONE);
				listLayout2.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
						tvOffice.setText("由高到低");
						Star_level = "1";
						selectorListener.goNotifyLoadData(qCity, areaCode,Star_level);
						shanhou();
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
		listLayout2 = (RelativeLayout) view.findViewById(R.id.pop_list_layout2);//列表布局  评价
		otherLayout=(LinearLayout) view.findViewById(R.id.pop_other_layout);//选择医院
		firstList=(ListView) view.findViewById(R.id.pop_first_list);
		secondList=(ListView) view.findViewById(R.id.pop_second_list);
		otherList=(ListView) view.findViewById(R.id.pop_other_list);
		firstList.setAdapter(firstAdapter);
		secondList.setAdapter(secondAdapter);
		otherList.setAdapter(otherAdapter);
		greyView=view.findViewById(R.id.pop_grey_view);
		greyView.getBackground().setAlpha(80);
		greyView.setOnClickListener(this);
	
	}

	//根据地区查询医院
	private void queryHospital(String areaCode) {
		//192.168.16.45:8899/DuoMeiHealth/ConsultationInfoSet?TYPE=findUnitByAreaCode&AREACODE=
		List<BasicNameValuePair> valuePairs = new ArrayList<>();
		valuePairs.add(new BasicNameValuePair("TYPE", "findUnitByAreaCode"));
		valuePairs.add(new BasicNameValuePair("AREACODE", areaCode));
		HttpRestClient.doGetConsultationInfoSet(valuePairs, new OkHttpClientManager.ResultCallback<JSONObject>() {

			@Override
			public void onError(Request request, Exception e) {

			}

			@Override
			public void onResponse(JSONObject response) {
				if ("1".equals(response.optString("code"))) {
					ArrayList<Map<String,String>> datas=new ArrayList<Map<String, String>>();
					try {
						JSONArray array = response.getJSONArray("result");
						HashMap<String,String> map2=new HashMap<String, String>();
						map2.put("name","全部");
						map2.put("code", "");
						datas.add(map2);
						for (int i = 0; i < array.length(); i++) {
							HashMap<String,String> map=new HashMap<String, String>();
							JSONObject obj=array.getJSONObject(i);
							map.put("name", obj.optString("UNIT_NAME"));
							map.put("code", ""+obj.optInt("UNIT_CODE"));
							datas.add(map);
						}
						otherAdapter.setDatas(datas);
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					ToastUtil.showShort(getActivity(), response.optString("message"));
				}
			}
		}, this);
	}


	// 查询地区

	private void queryCity() {
		citysMap = new HashMap<String, List<Map<String, String>>>();
		proList = DictionaryHelper.getInstance(getActivity()).setCityData(getActivity(),
				citysMap);
		// 每项加个全部
//		for (Map<String, String> map : proList) {
//			HashMap<String, String> hashMap = new HashMap<String, String>();
//			hashMap.put("name", getActivity().getString(R.string.limit_no));
//			hashMap.put("code", map.get("code"));
//			String name = map.get("name");
//			citysMap.get(name).add(0, hashMap);
//		}
//		HashMap<String, String> hashMap = new HashMap<String, String>();
//		hashMap.put("name", getActivity().getString(R.string.limit_no));//全部
//		hashMap.put("code", "0");
//		proList.add(0, hashMap);
//
//		List<Map<String, String>> arr = new ArrayList<Map<String, String>>();
//		arr.add(hashMap);
//		citysMap.put(getActivity().getString(R.string.limit_no), arr);
		
	}

	public String getOfficeName() {
		return officeName;
	}

	public void setOfficeName(String officeName) {
		this.officeName = officeName;
	}
}
