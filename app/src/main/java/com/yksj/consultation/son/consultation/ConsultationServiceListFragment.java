/**
 * 
 */
package com.yksj.consultation.son.consultation;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.yksj.consultation.adapter.AdtConsultationServiceList;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.ConsultationServiceEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.utils.ToastUtil;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * 患者端会诊服务
 * @author zheng
 */
public class ConsultationServiceListFragment extends RootFragment implements OnRefreshListener2<ListView>{
	private PullToRefreshListView mPullToRefreshListView;
	private ListView mListView;
	private AdtConsultationServiceList csla;
	private int pagesize1=1,pagesize2=1,pagesize3=1;//,pagesize4=1,pagesize5=1,pagesize6=1,pagesize7=1;
	private int p;
	private List<ConsultationServiceEntity> datas;
	private ConsultationServiceEntity cse;
	private View mNullView;
	private int one=1;
	private View mView;


	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mView=inflater.inflate(R.layout.consultant_center_viewpager_listview1, container, false);
		mNullView=mView.findViewById(R.id.service_null);
		mPullToRefreshListView=(PullToRefreshListView) mView.findViewById(R.id.consultation_expert_listView);
		mListView=mPullToRefreshListView.getRefreshableView();
        mListView.setEmptyView(mNullView);
		csla=new AdtConsultationServiceList(getActivity());
		mListView.setAdapter(csla);
		mPullToRefreshListView.setOnRefreshListener(this);
		Bundle bun=getArguments();
		int type=bun.getInt("TYPENUM");
		if(one==1){
			switch (type) {
			case 1:
				p=1;
				initData(1,1);
				break;
			case 7:
				p=7;
				initData(7,1);
				break;
			case 8:
				p=8;
				initData(8,1);
				break;
			}
			one=2;
		}
		return mView;
	}
	@Override
	public void onStart() {
		Bundle bun=getArguments();
		int type=bun.getInt("TYPENUM");
		if(one==1){
			switch (type) {
			case 1:
                csla.removeAll();
				initData(1,1);
				break;
			case 7:
                csla.removeAll();
				initData(7,1);
				break;
			case 8:
                csla.removeAll();
				initData(8,1);
				break;
			}
			one=2;
		}
		super.onStart();
	}
	@Override
	public void onStop() {
		csla.removeAll();
		if(one==2){
			one=1;
		}
		super.onStop();
	}
	/*
	 * 请求
	 */
	private void initData(int type,int pagesize){
		HttpRestClient.doHttpFindMyConsuServiceList1(type, pagesize, new ObjectHttpResponseHandler(mActivity) {

			@Override
			public void onStart() {
				mPullToRefreshListView.setRefreshing();
				super.onStart();
			}

			@Override
			public void onFinish() {
				mPullToRefreshListView.onRefreshComplete();
				super.onFinish();
			}

			@Override
			public Object onParseResponse(String content) {
                if (content.contains("error_code")) {
                    return content;
                } else {
                    datas = new ArrayList<ConsultationServiceEntity>();
                    datas.clear();
                    try {
                        JSONObject obj = new JSONObject(content);
                        JSONArray array = obj.getJSONArray("findExpertByPatient");
                        JSONObject item;
                        for (int i = 0; i < array.length(); i++) {
                            item = array.getJSONObject(i);
                            cse = new ConsultationServiceEntity();
                            cse.setApplytime(item.getString("CREATE_TIME"));
                            cse.setConsultationId(item.getString("CONSULTATION_ID"));//会诊Id
                            cse.setConsultationCenterId(item.getString("CONSULTATION_CENTER_ID"));//六一健康Id
                            cse.setConsultationName(item.getString("CONSULTATION_NAME"));//疾病名称
                            cse.setConsultationState(item.getString("CONSULTATION_STATUS"));
//						    cse.setExpertName(item.getString("CUSTOMER_NICKNAME"));//专家姓名
                            cse.setSExpertHeader(item.getString("CLIENT_ICON_BACKGROUND"));
                            cse.setBExpertHeader(item.getString("BIG_ICON_BACKGROUND"));
                            cse.setServiceStatusName(item.getString("SERVICE_STATUS_NAME"));
                            cse.setServiceOperation(item.getString("SERVICE_OPERATION"));
                            cse.setExpertId(item.optString("EXPERT_ID"));//专家Id
                            cse.setCustomerNickName(item.optString("CUSTOMER_NICKNAME"));
							cse.setCreateDoctorId(item.optString("CREATE_DOCTOR_ID"));
							cse.setCreateDoctorIdName(item.optString("CREATE_DOCTOR_ID_NAME"));
							cse.setIsTalk(item.optString("ISTALK"));
                            datas.add(cse);
                        }
                        return datas;
                    } catch (JSONException e) {
                        return null;
                    }
                }
			}

			@Override
			public void onSuccess(Object response) {
				super.onSuccess(response);
                csla.removeAll();
                if (response instanceof String) {
                    JSONObject object;
                    try {
                        object = new JSONObject((String) response);
                        ToastUtil.showShort(object.optString("error_message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (response instanceof List) {
                    if (response == null) {
                        mNullView.setVisibility(View.VISIBLE);
					    mPullToRefreshListView.setVisibility(View.GONE);
                    } else if (response != null) {
                        csla.addAll((List<ConsultationServiceEntity>) response);
//                        if (csla.datas.size() == 0) {
//                          mNullView.setVisibility(View.VISIBLE);
//						    mPullToRefreshListView.setVisibility(View.GONE);
//                        }
                    }
                }
			}
		});
	}
	//下拉
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
		switch (p) {
		case 1:
			csla.removeAll();
			initData(1,1);
			break;
		case 7:
			csla.removeAll();
			initData(7,1);
			break;
		case 8:
			csla.removeAll();
			initData(8,1);
			break;
		}
	}
	//上啦
	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
		switch (p) {
		case 1:
			++pagesize1;
			initData(1,pagesize1);
			break;
		case 7:
			++pagesize2;
			initData(7,pagesize2);
			break;
		case 8:
			++pagesize3;
			initData(8,pagesize3);
			break;
		}
	}
}
