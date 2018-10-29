package com.yksj.consultation.son.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.HospitalFindTeamAdapter;
import com.yksj.consultation.adapter.WorksationTeamMainAdapter;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.HospitaFindTeamEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;

import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;
//名医名院和按科室找团队往本页面跳转
public class DoctorWorkstationActivity extends Activity implements PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener, AdapterView.OnItemClickListener {

    private PullToRefreshListView pullListView;
    private View mEmptyView;
    private List<HospitaFindTeamEntity.ResultBean.ListBean> mList = new ArrayList<>();
    private ListView mListView;
    private HospitalFindTeamAdapter mAdapter;
    private int pageSize = 1;
    private int office_id=0;
    private String area_code="";
    private int doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_workstation);
        initView();
        loadDataWss();
    }

    private void initView() {
        doctorId = getIntent().getIntExtra("doctor_id", -1);
        area_code = getIntent().getStringExtra("area_code");
        pullListView = (PullToRefreshListView) findViewById(R.id.doctor_workstation);
        mEmptyView = findViewById(R.id.load_faile_layout);
        mListView = pullListView.getRefreshableView();
        pullListView.setOnRefreshListener(this);
        mListView = pullListView.getRefreshableView();
        mListView.setSelector(new ColorDrawable(Color.parseColor("#00000000")));
        mAdapter = new HospitalFindTeamAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        TextView title= (TextView) findViewById(R.id.title_lable);
        if (doctorId!=-1){
            title.setText("工作站");
        }else {
            title.setText(getIntent()!=null?getIntent().getStringExtra("title"):"六一健康");
        }
        office_id=getIntent().getIntExtra("office_id",0);
        ImageView imBack= (ImageView) findViewById(R.id.title_back);
        imBack.setOnClickListener(this);
        mListView.setOnItemClickListener(this);
    }

    private void loadDataWss(){
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("Type", "findWorkSiteByHospital"));
        pairs.add(new BasicNameValuePair("PageSize", "10"));
        pairs.add(new BasicNameValuePair("Page", pageSize+""));
        if (doctorId==-1){
            pairs.add(new BasicNameValuePair("Search_Str", ""));
            if (area_code==null){
                pairs.add(new BasicNameValuePair("Area_Code", ""));
                pairs.add(new BasicNameValuePair("Office_Id", office_id+""));
            }else {
                pairs.add(new BasicNameValuePair("Area_Code", area_code));
                pairs.add(new BasicNameValuePair("Office_Id", ""));
            }
        }else {
            pairs.add(new BasicNameValuePair("Doctor_Id", doctorId+""));
        }

        HttpRestClient.doGetWss(pairs, new OkHttpClientManager.ResultCallback<String>() {
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
                Gson gson = new Gson();
                HospitaFindTeamEntity hospitaFindTeamEntity = gson.fromJson(response, HospitaFindTeamEntity.class);
                List<HospitaFindTeamEntity.ResultBean.ListBean> list = hospitaFindTeamEntity.getResult().getList();
                mList.addAll(list);
                mAdapter.onBoundData(mList);
                if (mList.size()==0){
                    mEmptyView.setVisibility(View.VISIBLE);
                    pullListView.setVisibility(View.GONE);
                }else {
                    mEmptyView.setVisibility(View.GONE);
                    pullListView.setVisibility(View.VISIBLE);
                }
            }
        },this);

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        pageSize=1;
        mList.clear();
        loadDataWss();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        pageSize++;
        loadDataWss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:finish();break;
        }
    }
    //listView点击事件
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DoctorWorkstationMainActivity.class);
        intent.putExtra(DoctorWorkstationMainActivity.SITE_ID,mList.get(position-1).getSITE_ID()+"");
        startActivity(intent);
        Log.i("asdf", "onItemClick: "+mList.size()+"     "+position);
    }
}
