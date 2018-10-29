package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.AtyFollowUpPlanAdapter;
import com.yksj.consultation.adapter.FollowUpDetailAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
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

/**
 * 随访计划
 */
public class AtyFollowUpPlan extends BaseFragmentActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener, AdapterView.OnItemClickListener {

    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private AtyFollowUpPlanAdapter adapter;
    private List<JSONObject> mList;
    private String customer_id = LoginServiceManeger.instance().getLoginUserId();;
    private View mEmptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_follow_up_plan);
        initView();

    }

    private void initView() {
        initTitle();
        titleTextV.setText("随访计划");
        titleLeftBtn.setOnClickListener(this);
        mPullRefreshListView = ((PullToRefreshListView) findViewById(R.id.my_follow_up__pulllist));
        mListView = mPullRefreshListView.getRefreshableView();
        adapter = new AtyFollowUpPlanAdapter(this);
        mListView.setAdapter(adapter);
        mPullRefreshListView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);
        mEmptyView = findViewById(R.id.empty_view_famous);
        initData();
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", customer_id);//47324
        map.put("flag", "1");

        HttpRestClient.OKHttpFindTemplate(map, new HResultCallback<String>(this) {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))) {
                        mList = new ArrayList<>();
                        JSONArray array = obj.getJSONArray("follows");
                        JSONObject item;
                        for (int i = 0; i < array.length(); i++) {
                            item = array.getJSONObject(i);
                            mList.add(item);
                        }
                        adapter.onBoundData(mList);

                        if (mList.size()==0){
                            mEmptyView.setVisibility(View.VISIBLE);
                            mPullRefreshListView.setVisibility(View.GONE);
                        }else {
                            mEmptyView.setVisibility(View.GONE);
                            mPullRefreshListView.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this,AtyFollowUpDetail.class);
        intent.putExtra("follow_id",mList.get(position-1).optString("FOLLOW_ID"));
        startActivity(intent);
    }
}
