package com.yksj.consultation.son.home;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.BlocDShiJAdapter;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.BlocDaShiJEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

public class BlocDShiJActivity extends Activity implements View.OnClickListener {
    private List<BlocDaShiJEntity.ResultBean.ListBean> mList=new ArrayList<>();
    private int union_id;
    private int page=1;
    private BlocDShiJAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bloc_dshi_j);
        initView();
        loadDataWssDaShiji();
    }

    private void initView() {
        union_id = getIntent().getIntExtra("union_id", -1);
        findViewById(R.id.title_back).setOnClickListener(this);
        TextView tvTitle = (TextView) findViewById(R.id.title_lable);
        tvTitle.setText("大事记");
        RecyclerView daShiJRecycler= (RecyclerView) findViewById(R.id.daShiJRecycler);
        daShiJRecycler.setLayoutManager(new LinearLayoutManager(this));
        adapter = new BlocDShiJAdapter(mList, this);
        daShiJRecycler.setAdapter(adapter);
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
                mList.addAll(list);
                adapter.notifyDataSetChanged();
            }
        },this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:finish();break;
        }
    }
}
