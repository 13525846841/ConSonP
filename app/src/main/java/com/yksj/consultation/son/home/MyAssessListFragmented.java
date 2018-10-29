package com.yksj.consultation.son.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yksj.consultation.adapter.AssessedAdapter;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.utils.HStringUtil;

import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2016/11/16.
 */
public class MyAssessListFragmented extends RootFragment {
    private PullToRefreshListView mRefreshableView;
    private View mEmptyView;
    private AssessedAdapter mAdapter;
    private List<JSONObject> list = null;
    public static final String ID = "id";
    public static final String SITE = "site";
    private String id = "";
    private String site_id = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.assess_fragment_layout, null);
        initView(view);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView(View view) {
        if (getActivity().getIntent().hasExtra(ID))
            id = getActivity().getIntent().getStringExtra(ID);
        mRefreshableView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_listview);
        mEmptyView = view.findViewById(R.id.empty_view);
        ListView mListView = mRefreshableView.getRefreshableView();
        //  mRefreshableView.setOnRefreshListener(this);
        //mRefreshableView.setOnRefreshListener(this);
        mAdapter = new AssessedAdapter(mActivity);
        mListView.setAdapter(mAdapter);
        // mRefreshableView.setOnLastItemVisibleListener(this);
        if (getActivity().getIntent().hasExtra(SITE)) {
            site_id = getActivity().getIntent().getStringExtra(SITE);
            getSiteData();
        } else {
            getData();
        }

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    /**
     * 已回复数据
     */
    public void getData() {
//        Map<String, String> map = new HashMap<>();
//        map.put("doctor_id", LoginServiceManager.instance().getLoginUserId());// LoginServiceManager.instance().getLoginUserId()
//        map.put("evaluate_staturs", "1");
//        map.put("op", "queryEvaluateRecord");
        List<BasicNameValuePair> valuePairs = new ArrayList<>();

        valuePairs.add(new BasicNameValuePair("customer_id", id));
        valuePairs.add(new BasicNameValuePair("op", "queryDoctorStudioCommentInfo"));
        valuePairs.add(new BasicNameValuePair("type", "10"));//10全部 20 已回复 30 未回复

        HttpRestClient.doGetConsultationBuyStudioServlet(valuePairs, new HResultCallback<String>() {

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (!HStringUtil.isEmpty(response)) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (HttpResult.SUCCESS.endsWith(obj.optString("code"))) {
                            list = new ArrayList<>();
                            JSONArray array = obj.optJSONArray("result");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                list.add(object);
                            }
                            mAdapter.onBoundData(list);
                            if (list.size() == 0) {
                                mEmptyView.setVisibility(View.VISIBLE);
                                mRefreshableView.setVisibility(View.GONE);
                            } else {
                                mEmptyView.setVisibility(View.GONE);
                                mRefreshableView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            mEmptyView.setVisibility(View.VISIBLE);
                            mRefreshableView.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, this);
    }

    /**
     * 工作站评价
     */
    public void getSiteData() {
//        Map<String, String> map = new HashMap<>();
//        map.put("doctor_id", LoginServiceManager.instance().getLoginUserId());// LoginServiceManager.instance().getLoginUserId()
//        map.put("evaluate_staturs", "1");
//        map.put("op", "queryEvaluateRecord");
        List<BasicNameValuePair> valuePairs = new ArrayList<>();
        valuePairs.add(new BasicNameValuePair("op", "querySiteCommentInfo"));
        valuePairs.add(new BasicNameValuePair("site_id", site_id));//10全部 20 已回复 30 未回复

        HttpRestClient.doGetConsultationBuyStudioServlet(valuePairs, new HResultCallback<String>() {

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (!HStringUtil.isEmpty(response)) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (HttpResult.SUCCESS.endsWith(obj.optString("code"))) {
                            list = new ArrayList<>();
                            JSONArray array = obj.optJSONArray("result");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject object = array.getJSONObject(i);
                                list.add(object);
                            }
                            mAdapter.onBoundData(list);
                            if (list.size() == 0) {
                                mEmptyView.setVisibility(View.VISIBLE);
                                mRefreshableView.setVisibility(View.GONE);
                            } else {
                                mEmptyView.setVisibility(View.GONE);
                                mRefreshableView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            mEmptyView.setVisibility(View.VISIBLE);
                            mRefreshableView.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, this);
    }
}
