package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.CaseManageAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.consultationorders.AtyOrdersDetails2;
import com.yksj.consultation.son.consultation.member.FlowMassageActivity2;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
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


/**
 * 病历管理
 */
public class CaseManage extends BaseFragmentActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2<ListView>, AdapterView.OnItemClickListener {
    private PullToRefreshListView mRefreshableView;
    public ListView mListView;
    public CaseManageAdapter mAdapter;
    private List<JSONObject> list = null;
    private String customerId = "";
    private int conPageSize = 1;//当前的页数
    private View mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_casemanage);
        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleRightBtn.setVisibility(View.VISIBLE);
        titleRightBtn.setText("添加");
        titleRightBtn.setOnClickListener(this);
        titleTextV.setText("病历管理");

        mRefreshableView = (PullToRefreshListView) findViewById(R.id.pull_refresh_listview);
        mListView = mRefreshableView.getRefreshableView();
        mAdapter = new CaseManageAdapter(this);
        mListView.setAdapter(mAdapter);
        mRefreshableView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);

        mEmptyView = findViewById(R.id.empty_view_famous);
        customerId = LoginServiceManeger.instance().getLoginUserId();

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("op", "queryPatientCaseList");
        map.put("customer_id", customerId);
        HttpRestClient.OKHttpCaseList(map, new HResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {JSONObject obj = new JSONObject(content);
                    list = new ArrayList<>();
                    if ("1".equals(obj.optString("code"))) {

                        if (!HStringUtil.isEmpty(obj.optString("result"))) {
                            JSONArray array = obj.optJSONArray("result");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonobject = array.getJSONObject(i);
                                list.add(jsonobject);
                            }
                            if (conPageSize == 1) {//第一次加载
                                if (list.size() == 0) {
                                    mAdapter.removeAll();
                                    mAdapter.addAll(list);
                                    mRefreshableView.setVisibility(View.GONE);
                                    mEmptyView.setVisibility(View.VISIBLE);
                                } else {
                                    mRefreshableView.setVisibility(View.VISIBLE);
                                    mAdapter.removeAll();
                                    mAdapter.addAll(list);
                                }
                            } else {
                                if (list.size() != 0) {//加载出了数据
                                    mAdapter.addAll(list);
                                } else {
                                    ToastUtil.showShort("没有更多了");
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                mRefreshableView.setRefreshing();
            }

            @Override
            public void onAfter() {
                mRefreshableView.onRefreshComplete();
                super.onAfter();
            }
        }, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right:
                Intent intent = new Intent(CaseManage.this, FlowMassageActivity2.class);
                intent.putExtra("SELECTA", "SELECTA");
                intent.putExtra("OFFICECODE", "1");
                intent.putExtra("OFFICENAME", "2");
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        conPageSize = 1;
        initData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        conPageSize++;
        initData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(this, PersonSeekDetailAty.class);
//        intent.putExtra(PersonSeekDetailAty.PERSON_ID, mAdapter.datas.get(position - 1).optString("PERSON_ID"));
//        intent.putExtra("name", mAdapter.datas.get(position - 1).optString("PERSON_NAME"));
//        intent.putExtra("sex", mAdapter.datas.get(position - 1).optString("PERSON_SEX"));
//        intent.putExtra("age", mAdapter.datas.get(position - 1).optString("PERSON_AGE"));
//        intent.putExtra("phone", mAdapter.datas.get(position - 1).optString("PERSON_PHONE"));
//        intent.putExtra("idCard", mAdapter.datas.get(position - 1).optString("PERSON_IDENTITY"));
//        intent.putExtra("allergy", mAdapter.datas.get(position - 1).optString("case_discussion_id"));
//        intent.putExtra("allergy", mAdapter.datas.get(position - 1).optString("CASE_DISCUSSION_ID"));
//        startActivity(intent);

        Intent intent = new Intent(this, AtyOrdersDetails2.class);
        intent.putExtra("CONID", mAdapter.datas.get(position - 1).optString("CASE_DISCUSSION_ID"));
        intent.putExtra("MAINCASE", mAdapter.datas.get(position - 1).optString("MEDICAL_NAME"));
        startActivity(intent);
    }
}
