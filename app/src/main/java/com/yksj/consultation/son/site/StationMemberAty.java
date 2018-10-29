package com.yksj.consultation.son.site;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.StationMemberAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.home.DoctorStudioActivity;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class StationMemberAty extends BaseFragmentActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, AdapterView.OnItemClickListener, View.OnClickListener {

    private StationMemberAdapter adapter;
    private PullToRefreshListView mPullRefreshListView;
    public static final String SITE_ID = "site_id";
    private List<JSONObject> list = null;
    private String siteID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_member_aty);
        initView();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("集团成员");
        titleLeftBtn.setOnClickListener(this);
        if (getIntent().hasExtra(SITE_ID)) {
            siteID = getIntent().getStringExtra(SITE_ID);
        }
        mPullRefreshListView = ((PullToRefreshListView) findViewById(R.id.my_station_member__pulllist));
        ListView mListView = mPullRefreshListView.getRefreshableView();
        mPullRefreshListView.setOnRefreshListener(this);
        adapter = new StationMemberAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        initData();
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
        }
    }

    private void initData() {
        if (HStringUtil.isEmptyAndZero(siteID)) {
            ToastUtil.showShort("数据异常");
            return;
        }
        Map<String, String> map = new HashMap<>();
        map.put("site_id", siteID);
        map.put("op", "querySitePerson");//class_id
        HttpRestClient.OKHttpStationCommonUrl(map, new HResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    list = new ArrayList<>();
                    if ("1".equals(obj.optString("code"))) {

                        if (!HStringUtil.isEmpty(obj.optString("result"))) {
                            JSONArray array = obj.optJSONArray("result");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonobject = array.getJSONObject(i);
                                list.add(jsonobject);
                            }
                            adapter.onBoundData(list);
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
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

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        refreshView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        refreshView.onRefreshComplete();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DoctorStudioActivity.class);
        intent.putExtra("DOCTOR_ID", adapter.datas.get(position - 1).optString("CUSTOMER_ID"));
        intent.putExtra(DoctorStudioActivity.SITE_ID, adapter.datas.get(position - 1).optString("SITE_ID"));
        startActivity(intent);
    }
}
