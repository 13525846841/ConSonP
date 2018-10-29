package com.yksj.consultation.son.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.DoctorTeamGridAdapter;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.DepartmentFindTeamEntity;
import com.yksj.healthtalk.entity.DoctorworksTeamEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;

import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshGridView;

import java.util.ArrayList;
import java.util.List;

public class DoctorTeamMemberActivity extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private List<DoctorworksTeamEntity.ResultBean> teamList=new ArrayList<>();
    private DoctorTeamGridAdapter teamGridAdapter;
    private GridView teamGridView;
    private View mEmptyView;
    private int site_id=0;
    private PullToRefreshGridView teamPullToGridView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_team_member);
        initView();
        loadDataWorks();
    }

    private void initView() {
        site_id = getIntent().getIntExtra("site_id", 0);
        TextView title= (TextView) findViewById(R.id.title_lable);
        findViewById(R.id.title_back).setOnClickListener(this);
        title.setText("团队成员");

        mEmptyView = findViewById(R.id.load_faile_layout);

        teamPullToGridView = (PullToRefreshGridView) findViewById(R.id.team_member_grid);
        teamGridView = teamPullToGridView.getRefreshableView();
        teamGridView.setSelector(new ColorDrawable(Color.parseColor("#00000000")));
        teamGridAdapter = new DoctorTeamGridAdapter(DoctorTeamMemberActivity.this, teamList);
        teamGridView.setOnItemClickListener(this);
        teamGridView.setNumColumns(3);
        teamGridView.setAdapter(teamGridAdapter);
    }

    private void loadDataWorks() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("op", "querySitePerson"));
        pairs.add(new BasicNameValuePair("site_id", site_id+""));

        HttpRestClient.doGetWorks(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
            }

            @Override
            public void onAfter() {
                super.onAfter();

            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                DoctorworksTeamEntity doctorworksTeamEntity = gson.fromJson(response, DoctorworksTeamEntity.class);
                List<DoctorworksTeamEntity.ResultBean> beanList = doctorworksTeamEntity.getResult();
                teamList.addAll(beanList);
                teamGridAdapter.onBoundData(teamList);
                if (teamList.size()==0){
                    mEmptyView.setVisibility(View.VISIBLE);
                    teamGridView.setVisibility(View.GONE);
                }else {
                    mEmptyView.setVisibility(View.GONE);
                    teamGridView.setVisibility(View.VISIBLE);
                }
                Log.i("www", "onResponse--------: " + response);
            }
        }, this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:finish();break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DoctorInfoActivity.class);
        intent.putExtra("customer_id",teamList.get(position).getCUSTOMER_ID());
        intent.putExtra(DoctorInfoActivity.SITE_ID,teamList.get(position).getSITE_ID());
        startActivity(intent);
    }

}
