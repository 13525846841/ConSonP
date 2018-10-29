package com.yksj.consultation.son.consultation;

import java.util.ArrayList;
import java.util.List;

import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.yksj.consultation.adapter.AdtConsultationAssistant;
import com.yksj.consultation.adapter.ConsultationExpertAdapter;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.friend.DoctorClinicMainActivity;
import com.yksj.consultation.son.listener.OnClickChildItemListener;
import com.yksj.healthtalk.entity.BaseInfoEntity;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.utils.FriendHttpUtil;

/**
 * 创建资深 基层 动态 3个view
 * @author zheng
 */
public class PConsultCreateView implements  OnClickChildItemListener, OnRefreshListener2<ListView>{
	private Context context;
	private LayoutInflater inflater;
	private View view,view1;
	private PullToRefreshListView pullToRefreshListView,pullToRefreshListView1;
	private ListView mListView;
	private View nullView,nullView1;
	private ConsultationExpertAdapter ceaAdapter;
	private AdtConsultationAssistant acaAdapter;
	private List<View> viewList;
	private CustomerInfoEntity mCustomerInfoEntity;
	private ArrayList<CustomerInfoEntity> mEntities;
	private ArrayList<CustomerInfoEntity> seeList;
	private int pagesize1=1,pagesize2=1;
	
	public PConsultCreateView(Context context) {
		super();
		this.context = context;
		inflater=LayoutInflater.from(context);
		viewList=new ArrayList<View>();
	}
	/*
	 * 专家列表
	 */
	public View getPagerView() {
		view=inflater.inflate(R.layout.consultant_center_viewpager_listview1, null);
		nullView=view.findViewById(R.id.service_null);
		pullToRefreshListView=(PullToRefreshListView) view.findViewById(R.id.consultation_expert_listView);
		mListView=pullToRefreshListView.getRefreshableView();
		ceaAdapter=new ConsultationExpertAdapter(context);
		mListView.setAdapter(ceaAdapter);
		ceaAdapter.setFollowListener(new OnClickChildItemListener() {
			
			@Override
			public void onHeadIconClick(BaseInfoEntity topicName, int position) {
				onClickFriendHead(ceaAdapter.datas.get(position),position+1);
			}
			
			@Override
			public void onFollowClick(BaseInfoEntity topicName, int position) {
				
			}
		});
		initData1();
		pullToRefreshListView.setOnRefreshListener(this);
		return view;
	}
	/*
	 * 会诊医生列表
	 */
	public View getPagerView1(){
		view1=inflater.inflate(R.layout.consultant_center_viewpager_listview2, null);
//		nullView1=view.findViewById(R.id.service1);
		pullToRefreshListView1=(PullToRefreshListView) view1.findViewById(R.id.consultation_Assistant_listView);
		mListView=pullToRefreshListView1.getRefreshableView();
		acaAdapter=new AdtConsultationAssistant(context);
		acaAdapter.setFollowListener(this);
		mListView.setAdapter(acaAdapter);
		initData2();
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(mEntities.get(position)!=null)
					FriendHttpUtil.chatFromPerson((FragmentActivity) context, mEntities.get(position-1));
			}
		});
		pullToRefreshListView1.setOnRefreshListener(this);
		return view1;
	}
	/*
	 * 获得布局方法
	 */
	public List<View> getViewList(){
		viewList.add(getPagerView());
		viewList.add(getPagerView1());
		return viewList;
	}
	/*
	 * 专家列表数据
	 */
	private void initData1(){
		HttpRestClient.doHttpConsultationCenterDoctorList( pagesize1 , new ObjectHttpResponseHandler((FragmentActivity) context){

			@Override
			public void onStart() {
				pullToRefreshListView.setRefreshing();
				super.onStart();
			}

			@Override
			public void onFinish() {
				pullToRefreshListView.onRefreshComplete();
				super.onFinish();
			}

			@Override
			public Object onParseResponse(String content) {
				seeList=new ArrayList<CustomerInfoEntity>();
				try {
					JSONObject obj=new JSONObject(content);
					JSONArray array=obj.getJSONArray("consultationCenterDoctorList");
					for(int i=0;i<array.length();i++){
						mCustomerInfoEntity=new CustomerInfoEntity();
						JSONObject object=array.getJSONObject(i);
						mCustomerInfoEntity.setId(object.optString("CUSTOMER_ID"));//客户Id
						mCustomerInfoEntity.setRealname(object.optString("CUSTOMER_NICKNAME"));//客户昵称
						mCustomerInfoEntity.setUsername(object.optString("CUSTOMER_ACCOUNTS"));//多美好
						mCustomerInfoEntity.setDoctorTitleName(object.optString("ACADEMIC_JOB"));//职务
						mCustomerInfoEntity.setServicePrice(object.optString("SERVICE_PRICE"));//价格
						mCustomerInfoEntity.setDoctorClientPicture(object.optString("CLIENT_ICON_BACKGROUND"));//小头像
						mCustomerInfoEntity.setDoctorBigPicture(object.optString("BIG_ICON_BACKGROUND"));//大头像
						mCustomerInfoEntity.setSpecial(object.optString("RESUME_CONTENT"));//专长
						mCustomerInfoEntity.setCustomerlocus(object.optString("JOIN_TIME"));//时间
						seeList.add(mCustomerInfoEntity);
					}
					return seeList;
				} catch (JSONException e) {
					e.printStackTrace();
					return null;
				}
			}
			@Override
			public void onSuccess(Object response) {
				super.onSuccess(response);
				if(response!= null){
					ceaAdapter.addAll((List<CustomerInfoEntity>) response);
					if(ceaAdapter.datas.size()==0){
						nullView.setVisibility(View.VISIBLE);
						pullToRefreshListView.setVisibility(View.GONE);
					}
				}
			}
			
		});
	}
	/*
	 * 助理列表数据
	 */
	private void initData2(){
		HttpRestClient.doHttpExpertAssistant(pagesize2, new ObjectHttpResponseHandler(){

			@Override
			public void onStart() {
				pullToRefreshListView1.setRefreshing();
				super.onStart();
			}

			@Override
			public void onFinish() {
				pullToRefreshListView1.onRefreshComplete();
				super.onFinish();
			}
			@Override
			public Object onParseResponse(String content) {
				mEntities = new ArrayList<CustomerInfoEntity>();
				try {
					JSONObject obj = new JSONObject(content);
					JSONArray array = obj.getJSONArray("consultationCenterDoctorAssiList");
					if(array==null){
						pullToRefreshListView1.setVisibility(View.GONE);
						nullView1.setVisibility(View.VISIBLE);
						return null;
					}
					JSONObject item;
					for (int i = 0; i < array.length(); i++) {
						item = array.getJSONObject(i);
						mCustomerInfoEntity = new CustomerInfoEntity();
						mCustomerInfoEntity.setName(item.optString("CUSTOMER_NICKNAME"));
						mCustomerInfoEntity.setDoctorTitle(item.optString("TITLE_NAME"));
						mCustomerInfoEntity.setOfficeName1(item.optString("OFFICE_NAME"));
						mCustomerInfoEntity.setHospital(item.optString("DOCTOR_HOSPITAL"));
						mCustomerInfoEntity.setSpecial(item.optString("DOCTOR_SPECIALLY"));
						mCustomerInfoEntity.setDoctorBigPicture(item.optString("BIG_ICON_BACKGROUND"));
						mCustomerInfoEntity.setDoctorClientPicture(item.optString("CLIENT_ICON_BACKGROUND"));
						mCustomerInfoEntity.setId(item.optString("CUSTOMER_ID"));
						mEntities.add(mCustomerInfoEntity);
					}
					return mEntities;
				} catch (JSONException e) {
					return null;
				}
			}
			@Override
			public void onSuccess(Object response) {
				super.onSuccess(response);
				if(response!= null){
					acaAdapter.addAll((List<CustomerInfoEntity>) response);
					if(acaAdapter.datas.size()==0){
						nullView1.setVisibility(View.VISIBLE);
						pullToRefreshListView1.setVisibility(View.GONE);
					}
				}
			}
		});
	}
	/*
	 * 点头像回调
	 * @see com.yksj.consultation.ui.listener.OnClickChildItemListener#onHeadIconClick(com.yksj.healthtalk.entity.BaseInfoEntity, int)
	 */
	@Override
	public void onHeadIconClick(BaseInfoEntity topicName, int position) {
		onClickFriendHead(acaAdapter.datas.get(position),position+1);
	}

	@Override
	public void onFollowClick(BaseInfoEntity topicName, int position) {
		if(topicName==null)return;
		if(topicName instanceof CustomerInfoEntity){
			FriendHttpUtil.requestAttentionTofriends(context, null, (CustomerInfoEntity)topicName);
		}
	}
	/*
	 * 点头像回调action
	 */
	public void onClickFriendHead(CustomerInfoEntity entity,int position) {
		Intent intent = new Intent(context, DoctorClinicMainActivity.class);
		intent.putExtra("id", entity.getId());
		intent.putExtra("position", position-1);
		context.startActivity(intent);
	}

	/*
	 * 下啦
	 */
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		if(refreshView==pullToRefreshListView){
			pagesize1=1;
			ceaAdapter.removeAll();
			initData1();
			return;
		}
		if(refreshView==pullToRefreshListView1){
			pagesize2=1;
			acaAdapter.removeAll();
			initData2();
			return;
		}
	}
	/*
	 * 上拉
	 */
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		if(refreshView==pullToRefreshListView){
			++pagesize1;
			initData1();
			return;
		}
		if(refreshView==pullToRefreshListView1){
			++pagesize2;
			initData2();
			return;
		}
	}
}
