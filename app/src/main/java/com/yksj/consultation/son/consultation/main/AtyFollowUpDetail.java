package com.yksj.consultation.son.consultation.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import com.squareup.okhttp.Request;
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
 * 随访计划详情
 */
public class AtyFollowUpDetail extends BaseFragmentActivity implements View.OnClickListener ,PullToRefreshBase.OnRefreshListener2<ListView>{
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private FollowUpDetailAdapter adapter;
    private List<JSONObject> mList;
    private String customer_id = LoginServiceManeger.instance().getLoginUserId();
    private String follow_id = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aty_follow_up_detail);
        initView();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("随访计划");
        titleLeftBtn.setOnClickListener(this);
        follow_id = getIntent().getStringExtra("follow_id");


        mPullRefreshListView = ((PullToRefreshListView) findViewById(R.id.my_follow_up__pulllist));
        mListView = mPullRefreshListView.getRefreshableView();
        adapter = new FollowUpDetailAdapter(this);
        mListView.setAdapter(adapter);
        mPullRefreshListView.setOnRefreshListener(this);
        initData();
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("op","findFollowSubListById");
        map.put("follow_id", follow_id);
        HttpRestClient.OKHttpFindFollowSubListById(map, new HResultCallback<String>(this) {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))) {
                        mList = new ArrayList<>();
                        JSONArray array = obj.getJSONArray("sbus");
                        JSONObject item;
                        for (int i = 0; i < array.length(); i++) {
                            item = array.getJSONObject(i);
                            mList.add(item);
                        }
                        adapter.onBoundData(mList);
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
}
