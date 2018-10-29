package com.yksj.consultation.son.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.SelectExpertListAdapter;
import com.yksj.consultation.adapter.SelectExpertListAdapterr;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.consultation.member.FlowMassageActivity;
import com.yksj.consultation.son.home.DoctorStudioActivity;
import com.yksj.healthtalk.bean.BaseBean;
import com.yksj.healthtalk.bean.DoctorSimpleBean;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import org.json.JSONException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

/**
 * 根据地区医院二级菜单选择会诊专家
 * <p>
 * Created by lmk on 2015/9/14.
 */
public class SelectExpertMainUI extends BaseFragmentActivity implements View.OnClickListener, SelectExpertListAdapter.OnClickSelectListener,
        NavigateFragment.SelectorResultListener, PullToRefreshBase.OnRefreshListener2<ListView>, AdapterView.OnItemClickListener, SelectExpertListAdapterr.OnClickSelectListener {

    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private SelectExpertListAdapter mAdapter;
    private SelectExpertListAdapterr mmAdapter;
    private LinearLayout navLayout;
    private FragmentManager manager;
    private NavigateFragment navFragment;
    private int conPageSize = 1;//当前的页数
    public static final String TITLE = "TITLE";
    public static final String TYPE = "TYPE";
    private Bundle bundle;
    private String areaCode = "", unitCode = "", officeCode = "11", consultId;
    private String Star_level = "";
    private int goalType = 0;//0为默认 1找医生 2为患者重选专家
    private String officeName;
    private String diseaseName;//热门疾病名称

    private List<DoctorSimpleBean> mList = null;
    private List<DoctorSimpleBean> mList1 = null;
    public DoctorSimpleBean doctorSimple;
    //下面是临时的
    ImageView icon;
    public static String type1 = "";
    private View mEmptyView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.select_expert_main_ui);
        initView();
        goalType = getIntent().getIntExtra("goalType", 0);
        consultId = getIntent().getStringExtra("consultId");

        diseaseName = getIntent().getStringExtra("diseaseName");
        officeCode = getIntent().getStringExtra("OFFICECODE");
        officeName = getIntent().getStringExtra("OFFICENAME");
        type1 = getIntent().getStringExtra(TYPE);

        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction = manager.beginTransaction();
        navFragment = new NavigateFragment();
        navFragment.setSelectorListener(this);
        navFragment.setOfficeName(officeCode);
        transaction.add(R.id.navigationbar_layout, navFragment);
        transaction.commit();
    }

    private void initView() {
        initTitle();
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setBackgroundResource(R.drawable.ig_seach);
        titleTextV.setText("会诊专家");
        titleLeftBtn.setOnClickListener(this);
        titleRightBtn2.setOnClickListener(this);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.select_expert_list);
        mListView = mPullRefreshListView.getRefreshableView();
        mListView.setOnItemClickListener(this);
        mPullRefreshListView.setOnRefreshListener(this);
        mAdapter = new SelectExpertListAdapter(SelectExpertMainUI.this, 0);
        mAdapter.setSelectListener(this);
////	mListView.addFooterView(getLayoutInflater().inflate(R.layout.list_line_layout, null),null,false);
        mListView.setAdapter(mAdapter);
        mEmptyView = findViewById(R.id.load_data_is_null);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
//            case R.id.select_expert_list_item_headicon:
//                intent=new Intent(this, AtyDoctorMassage.class);
//                intent.putExtra("id","20");
//                startActivity(intent);
//                break;
            case R.id.title_right2://搜索
                intent = new Intent(SelectExpertMainUI.this, SearchExpertActivity.class);
                intent.putExtra("OFFICECODE", officeCode);
                intent.putExtra("OFFICENAME", officeName);
                intent.putExtra("consultId", consultId);
                intent.putExtra("type", goalType);
                startActivity(intent);
                break;
            case R.id.select_expert_list_item_select://搜索
                intent = new Intent(SelectExpertMainUI.this, FlowMassageActivity.class);
                intent.putExtra("OFFICENAME", officeName);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void goNotifyLoadData(String areaCode, String unitCode, String Star_level) {
        conPageSize = 1;
        this.areaCode = areaCode;
        this.unitCode = unitCode;
        this.Star_level = Star_level;

        if (type1.equals("diseaseName")) {
            loadData1(areaCode, unitCode, Star_level, 1);
        } else if (type1.equals("selectedOffice")) {
            loadData(areaCode, unitCode, Star_level, 1);
        }
    }

    private void loadData(String areaCode, String unitCode, String Star_level, final int page) {
        Map<String, String> map1 = new HashMap<>();
        map1.put("areaCode", areaCode);
        map1.put("office_id", officeCode);
        map1.put("unitCode", unitCode);
        map1.put("Star_level", Star_level);
        map1.put("pageNum", "" + page);
        HttpRestClient.OKHttpFindDocByRoom(map1, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))) {
                        mList = new ArrayList<DoctorSimpleBean>();
                        JSONArray array = obj.optJSONArray("doctors");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            doctorSimple = new DoctorSimpleBean();
                            doctorSimple.setDOCTOR_REAL_NAME(jsonObject.optString("DOCTOR_REAL_NAME"));
                            doctorSimple.setDOCTOR_HOSPITAL(jsonObject.optString("DOCTOR_HOSPITAL"));
                            doctorSimple.setCUSTOMER_ID(jsonObject.optString("CUSTOMER_ID"));
                            doctorSimple.setOFFICE_NAME(jsonObject.optString("OFFICE_NAME"));
                            doctorSimple.setTITLE_NAME(jsonObject.optString("TITLE_NAME"));
                            doctorSimple.setICON_DOCTOR_PICTURE(jsonObject.optString("ICON_DOCTOR_PICTURE"));
                            doctorSimple.setNUMS(jsonObject.optString("DOCTOR_SERVICE_NUMBER"));
                            doctorSimple.setSERVICE_PRICE(jsonObject.optString("SERVICE_PRICE"));
                            doctorSimple.setDOCTOR_SPECIALLY(jsonObject.optString("DOCTOR_SPECIALLY"));
                            doctorSimple.setISRECOMMNED(jsonObject.optString("ISRECOMMND"));
                            mList.add(doctorSimple);
                        }

                        if (conPageSize == 1) {//第一次加载
                            mAdapter.removeAll();
                            if (mList.size() == 0) {
                                mEmptyView.setVisibility(View.VISIBLE);
                                mPullRefreshListView.setVisibility(View.GONE);
                            } else {
                                mEmptyView.setVisibility(View.GONE);
                                mAdapter.addAll(mList);
                            }
                        } else {
                            if (mList.size() != 0) {//加载出了数据
                                mAdapter.addAll(mList);
                            } else {
                                ToastUtil.showShort("没有更多了");
                            }
                        }
                        conPageSize++;
//                        mAdapter.onBoundData(mList);
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

//                BaseBean bb = com.alibaba.fastjson.JSONObject.parseObject(content, BaseBean.class);
//                if (HttpResult.SUCCESS.equals(bb.code)) {
//                    List<DoctorSimpleBean> list1 = JSON.parseArray(bb.doctors, DoctorSimpleBean.class);
//                    if (list1 != null) {
//                        if (page == 1)//第一次加载
//                            mAdapter.removeAll();
//                        if (list1.size() != 0) {//加载出了数据
//                            mAdapter.addAll(list1);
//                        }
//                    }
//                } else {
//                    ToastUtil.showShort(bb.message);
//                }
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                mPullRefreshListView.setRefreshing();
            }

            @Override
            public void onAfter() {
                mPullRefreshListView.onRefreshComplete();
                super.onAfter();
            }
        }, this);
    }

    private void loadData1(String areaCode, String unitCode, String Star_level, final int page) {
        Map<String, String> map2 = new HashMap<>();
        map2.put("areaCode", areaCode);
        map2.put("disease", diseaseName);
        map2.put("unitCode", unitCode);
        map2.put("Star_level", Star_level);
        map2.put("pageNum", "" + page);
        HttpRestClient.OKHttpFindDocByDis(map2, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))) {
                        mList1 = new ArrayList<DoctorSimpleBean>();
                        JSONArray array = obj.optJSONArray("doctors");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            doctorSimple = new DoctorSimpleBean();
                            doctorSimple.setDOCTOR_REAL_NAME(jsonObject.optString("DOCTOR_REAL_NAME"));
                            doctorSimple.setDOCTOR_HOSPITAL(jsonObject.optString("DOCTOR_HOSPITAL"));
                            doctorSimple.setCUSTOMER_ID(jsonObject.optString("CUSTOMER_ID"));
                            doctorSimple.setOFFICE_NAME(jsonObject.optString("OFFICE_NAME"));
                            doctorSimple.setTITLE_NAME(jsonObject.optString("TITLE_NAME"));
                            doctorSimple.setICON_DOCTOR_PICTURE(jsonObject.optString("ICON_DOCTOR_PICTURE"));
                            doctorSimple.setNUMS(jsonObject.optString("DOCTOR_SERVICE_NUMBER"));
                            doctorSimple.setSERVICE_PRICE(jsonObject.optString("SERVICE_PRICE"));
                            doctorSimple.setDOCTOR_SPECIALLY(jsonObject.optString("DOCTOR_SPECIALLY"));
                            doctorSimple.setISRECOMMNED(jsonObject.optString("ISRECOMMND"));
                            mList1.add(doctorSimple);
                        }

                        if (conPageSize == 1) {//第一次加载
                            if (mList1.size() == 0) {
                                mEmptyView.setVisibility(View.VISIBLE);
                            } else {
                                mEmptyView.setVisibility(View.GONE);
                                mAdapter.removeAll();
                                mAdapter.addAll(mList1);
                            }
                        } else {
                            if (mList1.size() != 0) {//加载出了数据
                                mAdapter.addAll(mList1);
                            } else {
                                ToastUtil.showShort("没有更多了");
                            }
                        }
                        conPageSize++;

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                BaseBean bb = com.alibaba.fastjson.JSONObject.parseObject(content, BaseBean.class);
//                if (HttpResult.SUCCESS.equals(bb.code)) {
//                    List<DoctorSimpleBean> list2 = JSON.parseArray(bb.doctors, DoctorSimpleBean.class);
//                    if (list2 != null) {
//                        if (page == 1)//第一次加载
//                            mAdapter.removeAll();
//                        if (list2.size() != 0) {//加载出了数据
//                            mAdapter.addAll(list2);
//                        }
//                    }
//                } else {
//                    ToastUtil.showShort(bb.message);
//                }
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                mPullRefreshListView.setRefreshing();
            }

            @Override
            public void onAfter() {
                mPullRefreshListView.onRefreshComplete();
                super.onAfter();
            }
        }, this);
    }

//    private void loadData(String areaCode,String unitCode,final int page){
//
//        List<BasicNameValuePair> valuePairs=new ArrayList<>();
//        valuePairs.add(new BasicNameValuePair("TYPE","findExpertByOfficeAndUnit"));
//        valuePairs.add(new BasicNameValuePair("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID));
//        valuePairs.add(new BasicNameValuePair("PAGENUM","20"));
//        valuePairs.add(new BasicNameValuePair("PAGESIZE",""+page));
//        valuePairs.add(new BasicNameValuePair("UPPER_OFFICE_ID",officeCode));
//        valuePairs.add(new BasicNameValuePair("UNITCODE",unitCode));
//        valuePairs.add(new BasicNameValuePair("AREACODE",areaCode));
//
//        HttpRestClient.doGetConsultationInfoSet(valuePairs, new OkHttpClientManager.ResultCallback<String>() {
//            @Override
//            public void onError(Request request, Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(String response) {
//                BaseBean bb = com.alibaba.fastjson.JSONObject.parseObject(response, BaseBean.class);
//                if ("1".equals(bb.code)) {
//                    List<DoctorSimpleBean> list = JSON.parseArray(bb.result, DoctorSimpleBean.class);
////                ArrayList<DoctorSimpleBean> list= dld.result;
//                    if (list != null) {
//                        if (page == 1)//第一次加载
//                            mAdapter.removeAll();
//                        if (list.size() != 0) {//加载出了数据
//                            mAdapter.addAll(list);
//                        }
//                    }
//                } else {
//                    ToastUtil.showShort(bb.message);
//
//                }
//            }
//
//            @Override
//            public void onBefore(Request request) {
//                super.onBefore(request);
//                mPullRefreshListView.setRefreshing();
//            }
//
//            @Override
//            public void onAfter() {
//                mPullRefreshListView.onRefreshComplete();
//                super.onAfter();
//            }
//        }, this);
//    }

    //下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        conPageSize = 1;
        if (type1.equals("diseaseName")) {
            loadData1(areaCode, unitCode, Star_level, conPageSize);
        } else if (type1.equals("selectedOffice")) {
            loadData(areaCode, unitCode, Star_level, conPageSize);
        }


    }

    //上拉加载
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

        if (type1.equals("diseaseName")) {
            loadData1(areaCode, unitCode, Star_level, conPageSize);
        } else if (type1.equals("selectedOffice")) {
            loadData(areaCode, unitCode, Star_level, conPageSize);
        }
    }

    @Override
    public void onClickSelect(DoctorSimpleBean dsb) {
        if (goalType == 0) {
            Intent intent = new Intent(SelectExpertMainUI.this, FlowMassageActivity.class);
            intent.putExtra("data", dsb);
            intent.putExtra("PROMTER", "10");
            intent.putExtra("OFFICECODE", officeCode);
            intent.putExtra("OFFICENAME", officeName);
            startActivity(intent);

        } else if (goalType == 2) {
            selectHim(dsb);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //Intent intent = new Intent(SelectExpertMainUI.this, AtyDoctorMassage.class);
        Intent intent = new Intent(SelectExpertMainUI.this, DoctorStudioActivity.class);
        intent.putExtra("DOCTOR_ID", mAdapter.datas.get(position - 1).getCUSTOMER_ID());
        intent.putExtra("data", mAdapter.datas.get(position - 1));
//        intent.putExtra("id", mAdapter.datas.get(position-1).CUSTOMER_ID+"");
//        intent.putExtra("type", 0);
//        if(goalType==2){
//            intent.putExtra("consultId", consultId);
//        }
//        intent.putExtra("OFFICECODE", officeCode);
//        intent.putExtra("OFFICENAME", officeName);
        startActivity(intent);
    }

    private void selectHim(DoctorSimpleBean dsb) {
        ///DuoMeiHealth/ConsultationInfoSet?TYPE=reSelectedExpert&CUSTOMERID=&CONSULTATIONID=
        List<BasicNameValuePair> valuePairs = new ArrayList<>();
        valuePairs.add(new BasicNameValuePair("TYPE", "reSelectedExpert"));
        valuePairs.add(new BasicNameValuePair("CUSTOMERID", dsb.CUSTOMER_ID + ""));
        valuePairs.add(new BasicNameValuePair("CONSULTATIONID", consultId));
        valuePairs.add(new BasicNameValuePair("SERVICE_PRICE", "" + dsb.SERVICE_PRICE));
        HttpRestClient.doGetConsultationInfoSet(valuePairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                BaseBean bb = com.alibaba.fastjson.JSONObject.parseObject(response, BaseBean.class);
                if ("1".equals(bb.code)) {
                    EventBus.getDefault().post(new MyEvent("refresh", 2));
                    SelectExpertMainUI.this.finish();
                } else {
                    ToastUtil.showShort(SelectExpertMainUI.this, bb.message);

                }
            }
        }, this);

    }

}
