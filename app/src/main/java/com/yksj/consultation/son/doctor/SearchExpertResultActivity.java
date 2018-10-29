package com.yksj.consultation.son.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.SelectExpertListAdapter;
import com.yksj.consultation.comm.RootListActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.consultation.consultationorders.AtyOrderDetails;
import com.yksj.consultation.son.consultation.member.FlowMassageActivity;
import com.yksj.consultation.son.home.DoctorStudioActivity;
import com.yksj.healthtalk.bean.BaseBean;
import com.yksj.healthtalk.bean.DoctorSimpleBean;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 搜索专家的结果列表,一个单独的列表
 * @author lmk
 *
 */
public class SearchExpertResultActivity extends RootListActivity implements OnClickListener,SelectExpertListAdapter.OnClickSelectListener,
OnRefreshListener2<ListView>,AdapterView.OnItemClickListener{

	private PullToRefreshListView mPullRefreshListView;
	private ListView mListView;
	private SelectExpertListAdapter mAdapter;
	private String duomeiNum="",url="",merchantId="";//分别是搜索的名称,URL,医疗机构id
	private int pageSize=1;
	private RelativeLayout mNullLayout;
	private String officeCode,consultId,officeName;
	private int goalType=0;
	Intent intent=null;

	public DoctorSimpleBean doctorSimple;
	private List<DoctorSimpleBean> mList = null;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.search_result_layout);
		intent=getIntent();
		officeCode=intent.getStringExtra("OFFICECODE");
		officeName=intent.getStringExtra("OFFICENAME");
		goalType=intent.getIntExtra("type",0);
		if (goalType==2)
			consultId=getIntent().getStringExtra("consultId");
		initView();
		initData();
	}

	//初始化数据
	private void initData() {
		if (intent.hasExtra("result")) {
			duomeiNum=intent.getStringExtra("result");
		}
		if (getIntent().hasExtra("OFFICENAME")){
			officeName = getIntent().getStringExtra("OFFICENAME");
		}
		loadData();
	}

	/**
	 * 按类型加载数据
	 * 每页加载20条
	 */
	private void loadData() {
		//TYPE=findExpertByOfficeAndName&UPPER_OFFICE_ID=&NAME=&UNITCODE=&CONSULTATION_CENTER_ID=&PAGESIZE=&PAGENUM=
		List<BasicNameValuePair> pairs=new ArrayList<>();
		if (goalType==0||goalType==2){
			pairs.add(new BasicNameValuePair("UNITCODE",""));
			pairs.add(new BasicNameValuePair("TYPE","findExpertByOfficeAndName"));
		} else
			pairs.add(new BasicNameValuePair("TYPE","findAssiByOfficeAndName"));
		pairs.add(new BasicNameValuePair("UPPER_OFFICE_ID",officeCode));
		pairs.add(new BasicNameValuePair("NAME",duomeiNum));
		pairs.add(new BasicNameValuePair("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID));
		pairs.add(new BasicNameValuePair("PAGESIZE", pageSize+""));
		pairs.add(new BasicNameValuePair("PAGENUM", "20"));
		HttpRestClient.doGetConsultationInfoSet(pairs, new OkHttpClientManager.ResultCallback<String>() {
			@Override
			public void onError(Request request, Exception e) {

			}
			@Override
			public void onResponse(String response) {
				try {
					org.json.JSONObject obj = new org.json.JSONObject(response);
					if("1".equals(obj.optString("code"))){
						mList = new ArrayList<DoctorSimpleBean>();
						JSONArray array = obj.optJSONArray("result");
						for (int i=0;i<array.length();i++){
							org.json.JSONObject jsonObject = array.getJSONObject(i);
							doctorSimple = new DoctorSimpleBean();
							doctorSimple.setDOCTOR_REAL_NAME(jsonObject.optString("DOCTOR_REAL_NAME"));
							doctorSimple.setDOCTOR_HOSPITAL(jsonObject.optString("UNIT_NAME"));
							doctorSimple.setCUSTOMER_ID(jsonObject.optString("CUSTOMER_ID"));
							doctorSimple.setOFFICE_NAME(jsonObject.optString("OFFICE_NAME"));
							doctorSimple.setTITLE_NAME(jsonObject.optString("TITLE_NAME"));
							doctorSimple.setICON_DOCTOR_PICTURE(jsonObject.optString("ICON_DOCTOR_PICTURE"));
							doctorSimple.setNUMS(jsonObject.optString("DOCTOR_SERVICE_NUMBER"));
							doctorSimple.setSERVICE_PRICE(jsonObject.optString("SERVICE_PRICE"));
							doctorSimple.setDOCTOR_SPECIALLY(jsonObject.optString("DOCTOR_SPECIALLY"));

							mList.add(doctorSimple);
						}
						mAdapter.onBoundData(mList);


						if (pageSize == 1) {//第一次加载
							mAdapter.removeAll();
							if (mList.size() == 0) {
								mNullLayout.setVisibility(View.VISIBLE);
							} else {
								mNullLayout.setVisibility(View.GONE);
								mAdapter.addAll(mList);
							}
						} else {
							if (mList.size() != 0) {//加载出了数据
								mAdapter.addAll(mList);
							} else {
								ToastUtil.showShort("没有更多了");
							}
						}
						pageSize++;
//                        if (mList.size()==0){
//                            mEmptyView.setVisibility(View.VISIBLE);
//                            mPullRefreshListView.setVisibility(View.GONE);
//                        }else {
//                            mEmptyView.setVisibility(View.GONE);
//                            mPullRefreshListView.setVisibility(View.VISIBLE);
//                        }
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}



//				DoctorListData dld= com.alibaba.fastjson.JSONObject.parseObject(response,DoctorListData.class);
//				ArrayList<DoctorSimpleBean> list= dld.result;
//				if (list != null) {
//					if (pageSize == 1)//第一次加载
//						mAdapter.removeAll();
//					if (list.size() != 0) {//加载出了数据
//
//						mAdapter.addAll(list);
//					}else {
//						ToastUtil.showShort(getResources().getString(R.string.no_search_result));
//					}
//				} else if (response != null && response instanceof String) {
//					ToastUtil.showShort(dld.message);
//				}
			}

			@Override
			public void onBefore(Request request) {

				mPullRefreshListView.setRefreshing();
				super.onBefore(request);
			}

			@Override
			public void onAfter() {
				mPullRefreshListView.onRefreshComplete();
				super.onAfter();
			}
		}, this);
	}

	//初始化视图
	private void initView() {
		initTitle();
		titleLeftBtn.setOnClickListener(this);
		titleTextV.setText(R.string.knoweSearch);
		mPullRefreshListView=(PullToRefreshListView) findViewById(R.id.search_result_pulllist);
		mNullLayout=(RelativeLayout) findViewById(R.id.load_data_is_null);
		mPullRefreshListView.setOnRefreshListener(this);
		mListView=mPullRefreshListView.getRefreshableView();
		mListView.setOnItemClickListener(this);
		if (getIntent().hasExtra("CLINIC")){
			mAdapter=new SelectExpertListAdapter(SearchExpertResultActivity.this,1);
			mAdapter.setFromType(1);
		}else
			mAdapter=new SelectExpertListAdapter(SearchExpertResultActivity.this,goalType);

		mAdapter.setSelectListener(this);
		mListView.setAdapter(mAdapter);
	}


	@Override
	public ListView getListView() {
		return mListView;
	}

	@Override
	public BaseAdapter getAdapter() {
		return mAdapter;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			onBackPressed();
			break;
		}
	}

	//下拉刷新
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		pageSize=1;
		loadData();
	}

	//上拉加载
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		loadData();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode==RESULT_OK){
			if(requestCode==11&&data!=null&&data.hasExtra("isChangeAttention")){
				if(data.getBooleanExtra("isChangeAttention", false)){
//					mAdapter.datas.get(data.getIntExtra("position", 0)).
//					setIsAttentionFriend(data.getIntExtra("attentionFriend", 0));
//					mAdapter.notifyDataSetChanged();
				}
			}
		}
		switch (requestCode){
			case 201://搜索返回
				if (resultCode == RESULT_OK) {
					setResult(RESULT_OK,data);
					finish();
				}
				break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void onClickSelect(DoctorSimpleBean dsb) {
		if(goalType==0){
			Intent intent = new Intent(SearchExpertResultActivity.this, FlowMassageActivity.class);
			intent.putExtra("data", dsb);
			intent.putExtra("OFFICECODE", officeCode);
			intent.putExtra("OFFICENAME", officeName);
			intent.putExtra("PROMTER","10");
			startActivity(intent);
		}else if (goalType==1){
			Intent data=new Intent();
			data.putExtra("data",dsb);
			setResult(RESULT_OK, data);
			finish();
		}else {
			List<BasicNameValuePair> valuePairs=new ArrayList<>();
			valuePairs.add(new BasicNameValuePair("TYPE","reSelectedExpert"));
			valuePairs.add(new BasicNameValuePair("CUSTOMERID", dsb.CUSTOMER_ID+""));
			valuePairs.add(new BasicNameValuePair("CONSULTATIONID", consultId));
			valuePairs.add(new BasicNameValuePair("SERVICE_PRICE", "" + dsb.SERVICE_PRICE));
			HttpRestClient.doGetConsultationInfoSet(valuePairs, new OkHttpClientManager.ResultCallback<String>() {
				@Override
				public void onError(Request request, Exception e) {

				}

				@Override
				public void onResponse(String response) {

					BaseBean bb = JSONObject.parseObject(response, BaseBean.class);
					if ("1".equals(bb.code)) {
						EventBus.getDefault().post(new MyEvent("refresh", 2));
						Intent intent=new Intent(SearchExpertResultActivity.this, AtyOrderDetails.class);
						intent.putExtra("CONID", consultId);
						intent.putExtra("BACK", 2);
						startActivity(intent);
					}else
						ToastUtil.showShort(SearchExpertResultActivity.this, bb.message);
				}
			}, this);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = new Intent(SearchExpertResultActivity.this, DoctorStudioActivity.class);
		intent.putExtra("DOCTOR_ID",mAdapter.datas.get(position-1).CUSTOMER_ID+"");
		startActivity(intent);


//		Intent intent = new Intent(SearchExpertResultActivity.this, AtyDoctorMassage.class);
//		intent.putExtra("id", mAdapter.datas.get(position - 1).CUSTOMER_ID + "");
//		if (getIntent().hasExtra("CLINIC")){
//			intent.putExtra("CLINIC", "CLINIC");//0是专家
//		}
//		if (goalType==1) {
//			intent.putExtra("type", 1);//0是专家
//			startActivityForResult(intent, 201);
//		}else if (goalType==0) {
//			intent.putExtra("type", 0);//0是专家
//			intent.putExtra("OFFICECODE",officeCode);
//			intent.putExtra("OFFICENAME", officeName);
//			startActivity(intent);
//		}else {
//			intent.putExtra("type", 0);//0是专家
//			intent.putExtra("consultId", consultId);
//			startActivity(intent);
//
//		}
	}
}
