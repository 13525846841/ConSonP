package com.yksj.consultation.son.home;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.DoctorBlocHomeAdapter;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.BlocDaShiJEntity;
import com.yksj.healthtalk.entity.DoctorBlocHomeEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class DoctorBlocHomeActivity extends Activity implements View.OnClickListener {
    private List<Object> mlist=new ArrayList<Object>();
    private int union_id;
    private int page=1;
    private DoctorBlocHomeAdapter doctorBlocHomeAdapter;
    private TextView tvTitle;
    private ImageView imgCollcetion;
    private int FOLLOW_FLAG; //0未关注  1关注
    private TextView tipTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_bloc_home);
        initView();
        loadDataWss();
    }

    private void initView() {
        union_id = getIntent().getIntExtra("union_id", -1);
        findViewById(R.id.title_back).setOnClickListener(this);
        tipTv = (TextView) findViewById(R.id.tipTv);
        tipTv.setOnClickListener(this);
        tipTv.setEnabled(false);
        tvTitle = (TextView) findViewById(R.id.title_lable);
        imgCollcetion = (ImageView) findViewById(R.id.collection);
        imgCollcetion.setOnClickListener(this);
        RecyclerView docBlocRecycler= (RecyclerView) findViewById(R.id.docBlocRecycler);
        docBlocRecycler.setLayoutManager(new LinearLayoutManager(this));
        doctorBlocHomeAdapter = new DoctorBlocHomeAdapter(mlist, this);
        docBlocRecycler.setAdapter(doctorBlocHomeAdapter);
    }

    private void loadDataWss(){
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("Type", "doctorUnionHome"));
        pairs.add(new BasicNameValuePair("customer_id", LoginServiceManeger.instance().getLoginUserId()));
        pairs.add(new BasicNameValuePair("union_id", union_id+""));

        HttpRestClient.doGetWss(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                tipTv.setText("加载失败，点击重试");
                tipTv.setEnabled(true);
            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                DoctorBlocHomeEntity doctorBlocHomeEntity = gson.fromJson(response, DoctorBlocHomeEntity.class);
                DoctorBlocHomeEntity.ResultBean result = doctorBlocHomeEntity.getResult();
                mlist.add(result);
                tvTitle.setText(result.getUNION_NAME());
                if (result.getFOLLOW_FLAG()==0) {
                    imgCollcetion.setImageResource(R.drawable.works_shoucang);
                   FOLLOW_FLAG=0;
                }else {
                    imgCollcetion.setImageResource(R.drawable.quxiaoshoucang);
                    FOLLOW_FLAG=1;
                }
                loadDataWssDaShiji();
            }
        },this);

    }

    private void loadDataWssDaShiji(){
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("Type", "queryUnionEvent"));
        pairs.add(new BasicNameValuePair("union_id", union_id+""));
        pairs.add(new BasicNameValuePair("Page", page+""));
        pairs.add(new BasicNameValuePair("PageSize", "50"));

        HttpRestClient.doGetWss(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                BlocDaShiJEntity blocDaShiJEntity = gson.fromJson(response, BlocDaShiJEntity.class);
                List<BlocDaShiJEntity.ResultBean.ListBean> list = blocDaShiJEntity.getResult().getList();
                mlist.addAll(list);
                doctorBlocHomeAdapter.notifyDataSetChanged();
                tipTv.setVisibility(View.GONE);
            }
        },this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:finish();break;
            case R.id.tipTv:
                mlist.clear();
                tipTv.setEnabled(false);
                tipTv.setText("加载中...");
                loadDataWss();
                break;
            case R.id.collection:
             loadCollection();
            break;
        }
    }
    //添加关注
    private void loadCollection() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("Type", "followUnion"));
        pairs.add(new BasicNameValuePair("customer_id", LoginServiceManeger.instance().getLoginUserId()));
        pairs.add(new BasicNameValuePair("union_id", union_id+""));

        HttpRestClient.doGetWss(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                if (FOLLOW_FLAG==0) {
                    imgCollcetion.setImageResource(R.drawable.quxiaoshoucang);
                    ToastUtil.onShow(DoctorBlocHomeActivity.this,"关注成功",1000);
                    FOLLOW_FLAG=1;
                }else {
                    imgCollcetion.setImageResource(R.drawable.works_shoucang);
                    ToastUtil.onShow(DoctorBlocHomeActivity.this,"关注取消",1000);
                    FOLLOW_FLAG=0;
                }
            }
        },this);

    }
}
