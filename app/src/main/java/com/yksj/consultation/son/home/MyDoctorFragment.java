package com.yksj.consultation.son.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.SelectExpertListAdapter;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.bean.DoctorSimpleBean;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.HStringUtil;

import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 六一健康我的医生列表fragment 分为会诊医生和会诊专家列表
 * Created by lmk on 2015/10/9.
 */
public class MyDoctorFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2<ListView> {
    private View mEmptyView;
    private View view;
    private PullToRefreshListView pullListView;
    private ListView mListView;
//    private HospitalFindTeamAdapter mAdapter;
    private SelectExpertListAdapter mAdapter;
    private int type = 0;//0 专家  1医生
    private int pageSize = 1;

    public DoctorSimpleBean doctorSimple;
    private List<DoctorSimpleBean> mList =  new ArrayList<DoctorSimpleBean>();;
//    private List<HospitaFindTeamEntity.ResultBean.ListBean> mList = null;

    public static MyDoctorFragment newInstance(int sourceType) {
        MyDoctorFragment fragment = new MyDoctorFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("source", sourceType);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("source");
            pullListView.setOnRefreshListener(this);
//            mAdapter = new HospitalFindTeamAdapter(getActivity(), mList);
            mAdapter = new SelectExpertListAdapter(getActivity(), type);
            mAdapter.setFromType(1);
            mListView.setAdapter(mAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    Intent intent = new Intent(getActivity(), AtyDoctorMassage.class);
//                    intent.putExtra("id", mAdapter.datas.get(position - 1).CUSTOMER_ID + "");
//                    intent.putExtra("type", type);
//                    intent.putExtra("CLINIC", "CLINIC");
//                    intent.putExtra("ORDER", "");
//                    startActivity(intent);

//                    Intent intent = new Intent(getActivity(), DoctorStudioActivity.class);
//                    intent.putExtra("DOCTOR_ID", mAdapter.datas.get(position - 1).CUSTOMER_ID + "");
//                    if (!HStringUtil.isEmpty(mAdapter.datas.get(position - 1).DOCTOR_SITE_ID)) {
//                        intent.putExtra(DoctorStudioActivity.SITE_ID, mAdapter.datas.get(position - 1).DOCTOR_SITE_ID);
//                    }
//                    startActivity(intent);
                    Intent intent = new Intent(getActivity(), DoctorInfoActivity.class);
                    intent.putExtra("customer_id",Integer.valueOf(mAdapter.datas.get(position - 1).CUSTOMER_ID));
                    if (mAdapter.datas.get(position - 1).getDOCTOR_SITE_ID()!=null) {
                        intent.putExtra(DoctorInfoActivity.SITE_ID,Integer.valueOf(mAdapter.datas.get(position - 1).getDOCTOR_SITE_ID()));
                    }
                    startActivity(intent);
//
                }
            });
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mList.clear();
        pageSize = 1;
        loadData();
//        loadDataWss();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.mydoc_list_fragment, null);
        initView();
        return view;
    }

    private void initView() {
        pullListView = (PullToRefreshListView) view.findViewById(R.id.find_friend_pulllist);
        mListView = pullListView.getRefreshableView();
        mEmptyView = view.findViewById(R.id.load_faile_layout);
        view.findViewById(R.id.sort).setVisibility(View.GONE);
    }

    private void loadData() {
        ///DuoMeiHealth/ConsultationInfoSet?TYPE=findMyPatient&NAME=&CUSTOMERID=&PAGESIZE=&PAGENUM=
        HttpRestClient.addHttpHeader("client_type","60");
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("CUSTOMERID", LoginServiceManeger.instance().getLoginEntity().getId()));
        pairs.add(new BasicNameValuePair("TYPE", "findMyDoctor"));
        pairs.add(new BasicNameValuePair("NAME", ""));
        pairs.add(new BasicNameValuePair("PAGESIZE", "" + pageSize));
        pairs.add(new BasicNameValuePair("PAGENUM", "20"));
        if (type == 0)
            pairs.add(new BasicNameValuePair("DOCTOR_CLASS", "10"));//专家
        else
            pairs.add(new BasicNameValuePair("DOCTOR_CLASS", "20"));
        pairs.add(new BasicNameValuePair("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID));

        HttpRestClient.doGetConsultationInfoSet(pairs, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                pullListView.setRefreshing();
            }

            @Override
            public void onAfter() {
                pullListView.onRefreshComplete();
                super.onAfter();
            }

            @Override
            public void onResponse(String response) {
                try {
                    Log.i("www", "onResponse"+response);
                    org.json.JSONObject obj = new org.json.JSONObject(response);
                    if ("1".equals(obj.optString("code"))) {

                        JSONArray array = obj.optJSONArray("result");
                        for (int i = 0; i < array.length(); i++) {
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
                            if (jsonObject.has("site_id")) {
                                doctorSimple.setDOCTOR_SITE_ID(jsonObject.optString("site_id"));
                            }

                            mList.add(doctorSimple);
                        }

                        mAdapter.onBoundData(mList);

                        if (mList.size() == 0) {
                            mEmptyView.setVisibility(View.VISIBLE);
                            pullListView.setVisibility(View.GONE);
                        } else {
                            mEmptyView.setVisibility(View.GONE);
                            pullListView.setVisibility(View.VISIBLE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                BaseBean bb = JSONObject.parseObject(response, BaseBean.class);
//                if ("1".equals(bb.code)) {
//                    List<DoctorSimpleBean> list = JSON.parseArray(bb.result, DoctorSimpleBean.class);
//                    if (list != null) {
//                        if (pageSize == 1)//第一次加载
//                            mAdapter.removeAll();
//                        if (list.size() != 0) {//加载出了数据
////                            if (list.size() == 1) {//只有导医护士一个人
////                                ToastUtil.showShort(getResources().getString(R.string.no_search_result));
////                            }
//                            mAdapter.addAll(list);
//                        }
//                    }
//                } else
//                    ToastUtil.showShort(getActivity(), bb.message);
            }
        }, this);

    }

//    private void loadDataWss(){
//        List<BasicNameValuePair> pairs = new ArrayList<>();
//        if (type==0){
//            pairs.add(new BasicNameValuePair("Type", "findWorkSiteByHospital"));
//            pairs.add(new BasicNameValuePair("Search_Str", ""));
//            pairs.add(new BasicNameValuePair("Area_Code", "340100"));
//            pairs.add(new BasicNameValuePair("PageSize", "10"));
//            pairs.add(new BasicNameValuePair("Page", pageSize+""));
//            pairs.add(new BasicNameValuePair("Office_Id", ""));
//        }else {
//            pairs.add(new BasicNameValuePair("Type", "findWorkSiteByOffice"));
//        }
//
//
//        HttpRestClient.doGetWss(pairs, new OkHttpClientManager.ResultCallback<String>() {
//            @Override
//            public void onError(Request request, Exception e) {
//
//            }
//
//            @Override
//            public void onBefore(Request request) {
//                super.onBefore(request);
//            }
//
//            @Override
//            public void onAfter() {
//                super.onAfter();
//            }
//
//            @Override
//            public void onResponse(String response) {
//                Gson gson = new Gson();
//                if(type==0){
//                    HospitaFindTeamEntity hospitaFindTeamEntity = gson.fromJson(response, HospitaFindTeamEntity.class);
//                    mList = hospitaFindTeamEntity.getResult().getList();
//                    Log.i("erer", "onResponse--------: "+mList.size());
//                    mAdapter.notifyDataSetChanged();
//
//                }else {
//                    DepartmentFindTeamEntity departmentFindTeamEntity = gson.fromJson(response, DepartmentFindTeamEntity.class);
//                    List<DepartmentFindTeamEntity.ResultBean> result = departmentFindTeamEntity.getResult();
//                    int size = result.size();
//                    Log.i("erer", "onResponse--------: "+size);
//                }
//                Log.i("www", "onResponse--------: "+response);
//            }
//        },this);
//
//    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageSize = 1;
        mList.clear();
        loadData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageSize++;
        loadData();
    }
}
