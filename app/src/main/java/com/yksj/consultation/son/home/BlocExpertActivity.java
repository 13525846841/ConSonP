package com.yksj.consultation.son.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.BlocExpertAdapter;
import com.yksj.consultation.adapter.DoctorBlocAdapter;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.BlocExpertEntity;
import com.yksj.healthtalk.entity.DoctorBlocEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;

import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

public class BlocExpertActivity extends Activity implements PullToRefreshBase.OnRefreshListener2<ListView>, AdapterView.OnItemClickListener, View.OnClickListener {

    private List<BlocExpertEntity.ResultBean.ListBean> mList=new ArrayList<>();
    private View mEmptyView;
    private TextView tvBlocSearch;
    private EditText edtBlocSearch;
    private ListView mListView;
    private PullToRefreshListView pullToList;
    private BlocExpertAdapter mAdapter;
    private int page=1;
    private int union_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_bloc);
        initView();
        loadDataWss();
    }

    private void initView() {
        union_id = getIntent().getIntExtra("union_id", -1);
        findViewById(R.id.title_back).setOnClickListener(this);
        TextView title= (TextView) findViewById(R.id.title_lable);
        title.setText("专家团");
        edtBlocSearch = (EditText) findViewById(R.id.edtBlocSearch);
        tvBlocSearch = (TextView) findViewById(R.id.tvBlocSearch);
        tvBlocSearch.setOnClickListener(this);
        edtOpater();
        pullToList = (PullToRefreshListView) findViewById(R.id.pullToList);
        mEmptyView = findViewById(R.id.load_faile_layout);
        pullToList.setOnRefreshListener(this);
        mListView = pullToList.getRefreshableView();
        mListView.setSelector(new ColorDrawable(Color.parseColor("#00000000")));
        mListView.setOnItemClickListener(this);
        mListView.setDividerHeight(0);
        mAdapter = new BlocExpertAdapter(this, mList);
        mListView.setAdapter(mAdapter);

    }
    private void edtOpater() {
        edtBlocSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String s1 = s.toString();
                if (!s1.equals("")) {
                    tvBlocSearch.setVisibility(View.VISIBLE);
                    edtBlocSearch.setGravity(Gravity.CENTER_VERTICAL);
                }else {
                    tvBlocSearch.setVisibility(View.GONE);
                    edtBlocSearch.setGravity(Gravity.CENTER);
                }
            }
        });
    }

    private void loadDataWss(){
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("Type", "findWorkSiteByHospital"));
        pairs.add(new BasicNameValuePair("Search_Str", edtBlocSearch.getText().toString()));
        pairs.add(new BasicNameValuePair("Page", page+""));
        pairs.add(new BasicNameValuePair("Union_Id", union_id+""));
        pairs.add(new BasicNameValuePair("PageSize", "10"));

        HttpRestClient.doGetWss(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                pullToList.setRefreshing();
            }

            @Override
            public void onAfter() {
                pullToList.onRefreshComplete();
                super.onAfter();
            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                BlocExpertEntity blocExpertEntity = gson.fromJson(response, BlocExpertEntity.class);
                List<BlocExpertEntity.ResultBean.ListBean> list = blocExpertEntity.getResult().getList();
                mList.addAll(list);
                mAdapter.onBoundData(mList);
                if (mList.size()==0){
                    mEmptyView.setVisibility(View.VISIBLE);
                    pullToList.setVisibility(View.GONE);
                }else {
                    mEmptyView.setVisibility(View.GONE);
                    pullToList.setVisibility(View.VISIBLE);
                }
            }
        },this);

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
//        page=1;
        mList.clear();
        loadDataWss();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
//        page++;
        loadDataWss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(BlocExpertActivity.this, DoctorWorkstationMainActivity.class);
        intent.putExtra(DoctorWorkstationMainActivity.SITE_ID,mList.get(position-1).getSITE_ID()+"");
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
      switch (v.getId()){
          case R.id.title_back:finish();break;
          case R.id.tvBlocSearch:
              mList.clear();
              loadDataWss();
              break;
      }
    }
}
