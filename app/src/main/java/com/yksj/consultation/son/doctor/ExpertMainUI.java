package com.yksj.consultation.son.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.ExpertMainUIAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.member.FlowMassageActivity;
import com.yksj.consultation.son.home.DoctorInfoActivity;
import com.yksj.consultation.son.home.DoctorStudioActivity;
import com.yksj.healthtalk.bean.DoctorSimpleBean;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * 由首页点击专家进入
 * 根据地区医院科室二级菜单选择会诊专家
 * <p>
 * Created by lmk on 2015/9/14.
 */
public class ExpertMainUI extends BaseFragmentActivity implements View.OnClickListener,
        ExpertNavigateFragment.SelectorResultListener, PullToRefreshBase.OnRefreshListener2<ListView>, AdapterView.OnItemClickListener {

    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private ExpertMainUIAdapter mAdapter;
    private LinearLayout navLayout;
    private FragmentManager manager;
    private ExpertNavigateFragment navFragment;
    private int conPageSize = 1;//当前的页数
    private Bundle bundle;
    private String areaCode = "", unitCode = "", officeCode = "", consultId;
    private int goalType = 0;//0为默认 1找医生 2为患者重选专家
    //下面是临时的
    ImageView icon;
    public DoctorSimpleBean doctorSimple;
    private List<DoctorSimpleBean> mList = null;
    private View mEmptyView;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.select_expert_main_ui);
        initView();
        goalType = getIntent().getIntExtra("goalType", 0);
        consultId = getIntent().getStringExtra("consultId");
        manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction = manager.beginTransaction();
        navFragment = new ExpertNavigateFragment();
        navFragment.setSelectorListener(this);
        transaction.add(R.id.navigationbar_layout, navFragment);
        transaction.commit();
    }

    private void initView() {
        initTitle();
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setBackgroundResource(R.drawable.ig_seach);
       // titleTextV.setText("会诊专家");
        titleTextV.setText("专家");
        titleLeftBtn.setOnClickListener(this);
        titleRightBtn2.setOnClickListener(this);
        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.select_expert_list);
        mListView = mPullRefreshListView.getRefreshableView();
        mListView.setOnItemClickListener(this);
        mPullRefreshListView.setOnRefreshListener(this);
        mAdapter = new ExpertMainUIAdapter(ExpertMainUI.this, 1);
        mAdapter.setFromType(1);
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
                intent = new Intent(ExpertMainUI.this, SearchExpertActivity.class);
                intent.putExtra("type", goalType);
                intent.putExtra("CLINIC", "CLINIC");
                intent.putExtra("OFFICECODE", "");
                startActivity(intent);
                break;
            case R.id.select_expert_list_item_select://搜索
                intent = new Intent(ExpertMainUI.this, FlowMassageActivity.class);
                startActivity(intent);
                break;

        }
    }

    @Override
    public void goNotifyLoadData(String areaCode, String unitCode, String officeCode) {
        conPageSize = 1;
        this.areaCode = areaCode;
        this.unitCode = unitCode;
        this.officeCode = officeCode;
        loadData(areaCode, unitCode, conPageSize);
    }

    /**
     * 加载数据
     *
     * @param unitCode 医院
     * @param areaCode 地区编码
     * @param page     页码
     */
    private void loadData(String areaCode, String unitCode, final int page) {

        List<BasicNameValuePair> valuePairs = new ArrayList<>();
        valuePairs.add(new BasicNameValuePair("TYPE", "findExpertByOfficeAndUnit"));
        valuePairs.add(new BasicNameValuePair("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID));
        valuePairs.add(new BasicNameValuePair("PAGENUM", "20"));
        valuePairs.add(new BasicNameValuePair("PAGESIZE", conPageSize + ""));
        valuePairs.add(new BasicNameValuePair("UPPER_OFFICE_ID", officeCode));
        valuePairs.add(new BasicNameValuePair("UNITCODE", unitCode));
        valuePairs.add(new BasicNameValuePair("AREACODE", areaCode));

        HttpRestClient.doGetConsultationInfoSet(valuePairs, new HResultCallback<String>(this)  {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                try {
                    org.json.JSONObject obj = new org.json.JSONObject(response);
                    if ("1".equals(obj.optString("code"))) {
                        mList = new ArrayList<DoctorSimpleBean>();
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
                            doctorSimple.setISRECOMMNED(jsonObject.optString("ISRECOMMND"));
                            mList.add(doctorSimple);
                        }

                        if (conPageSize == 1) {//第一次加载
                            if (mList.size() == 0) {
                                mAdapter.removeAll();
                                mAdapter.addAll(mList);
                                mEmptyView.setVisibility(View.VISIBLE);
                                mPullRefreshListView.setVisibility(View.GONE);
                            } else {
                                mEmptyView.setVisibility(View.GONE);
                                mPullRefreshListView.setVisibility(View.VISIBLE);
                                mAdapter.removeAll();
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
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                BaseBean bb = JSONObject.parseObject(response, BaseBean.class);
//                if ("1".equals(bb.code)) {
//                    List<DoctorSimpleBean> list = JSON.parseArray(bb.result, DoctorSimpleBean.class);
////                ArrayList<DoctorSimpleBean> list= dld.result;
//                    org.json.JSONObject obj = null;
//
//                    JSONArray array = obj.optJSONArray("doctors");
//
//                    if (list != null) {
//                        if (page == 1)//第一次加载
//                            mAdapter.removeAll();
//                        if (list.size() != 0) {//加载出了数据
//                            conPageSize++;
//                            mAdapter.addAll(list);
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

    //下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        conPageSize = 1;
        loadData(areaCode, unitCode, conPageSize);
    }

    //上拉加载
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        loadData(areaCode, unitCode, conPageSize);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(ExpertMainUI.this, AtyDoctorMassage.class);
//        intent.putExtra("id", mAdapter.datas.get(position-1).CUSTOMER_ID+"");
//        intent.putExtra("type", 0);
//        intent.putExtra("CLINIC", "CLINIC");
//        if(goalType==2){
//            intent.putExtra("consultId", consultId);
//        }
//        intent.putExtra("OFFICECODE", officeCode);
//        startActivity(intent);

//        Intent intent = new Intent(ExpertMainUI.this, DoctorStudioActivity.class);
//        //intent.putExtra("DOCTOR_ID",mAdapter.datas.get(position-1).getCUSTOMER_ID());
//        intent.putExtra("DOCTOR_ID", mAdapter.datas.get(position - 1).CUSTOMER_ID + "");
//        startActivity(intent);

        Intent intent = new Intent(this, DoctorInfoActivity.class);
        intent.putExtra("customer_id",Integer.valueOf(mAdapter.datas.get(position - 1).CUSTOMER_ID));
        intent.putExtra("good",mAdapter.datas.get(position-1).getDOCTOR_SPECIALLY());
        if (mAdapter.datas.get(position - 1).getDOCTOR_SITE_ID()!=null) {
            intent.putExtra(DoctorInfoActivity.SITE_ID,Integer.valueOf(mAdapter.datas.get(position - 1).getDOCTOR_SITE_ID()));
        }
        startActivity(intent);
    }
}
