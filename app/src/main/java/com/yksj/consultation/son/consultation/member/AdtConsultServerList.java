package com.yksj.consultation.son.consultation.member;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.yksj.consultation.adapter.AdtConsultationServiceList;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.ConsultationServiceEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.utils.ToastUtil;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class AdtConsultServerList extends Fragment implements PullToRefreshBase.OnRefreshListener2{

    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private AdtConsultationServiceList csla;
    private int mPageSize = 1;
    private int mType;
    private View mNullView;
    private List<ConsultationServiceEntity> datas;
    private ConsultationServiceEntity cse;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Bundle bun=getArguments();
        mType=bun.getInt("TYPENUM");
        View mView = inflater.inflate(R.layout.fragment_adt_consult_server_list, container, false);
        mPullToRefreshListView=(PullToRefreshListView) mView.findViewById(R.id.dossier_list_kill);
        mNullView=mView.findViewById(R.id.dossier_list_null);
        mListView=mPullToRefreshListView.getRefreshableView();
        csla = new AdtConsultationServiceList(getActivity());
        mListView.setEmptyView(mNullView);
        mListView.setAdapter(csla);
        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setRefreshing();
//        initData();
        return mView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mPageSize=1;
        initData();
    }

    private synchronized void initData(){
        HttpRestClient.doHttpFindMyConsuServiceList1(mType, mPageSize, new ObjectHttpResponseHandler() {

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
                    try {
                        JSONObject obj = new JSONObject(content);
                        JSONArray array = obj.getJSONArray("findExpertByPatient");
                        datas = new ArrayList<ConsultationServiceEntity>();
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
                            cse.setIsTalk(item.optString("ISTALK"));
                            cse.setCreateDoctorIdName(item.optString("CREATE_DOCTOR_ID_NAME"));
                            cse.setCreateDoctorId(item.optString("CREATE_DOCTOR_ID"));
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
                mPageSize++;
                if(response==null){

                }else  if (response instanceof String) {
                    JSONObject object;
                    try {
                        object = new JSONObject((String) response);
                        ToastUtil.showShort(object.optString("error_message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (response instanceof List) {
                    if(mPageSize==2){
                        csla.replaceAll((List<ConsultationServiceEntity>) response);
                    }else{
                        csla.addAll((List<ConsultationServiceEntity>) response);
                    }
                 }
//                if (csla.datas.size() == 0) {
//                    mNullView.setVisibility(View.VISIBLE);
//                    mPullToRefreshListView.setVisibility(View.GONE);
//                }
            }
        });
    }
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        initData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        initData();
    }
}
