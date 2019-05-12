package com.yksj.consultation.son.site;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.EvelateAdapter;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 医生评论列表界面
 * Created by lmk on 15/10/21.
 */
public class SiteCommentListActivity extends BaseListActivity implements PullToRefreshBase.OnRefreshListener2<ListView> {
    TextView textView;
    private EvelateAdapter mAdapter;
    private int pageSize = 1;

    public static final String SITE_ID = "site_id";
    private String site_id = "";

    @Override
    protected void initData() {
        if (HStringUtil.isEmptyAndZero(site_id)) {
            ToastUtil.showShort("数据异常");
            return;
        }
        //http://220.194.46.204/DuoMeiHealth/ConsultationInfoSet?TYPE=findCommentList&CUSTOMERID=3783&PAGESIZE=1&PAGENUM=20
        Map<String, String> map = new HashMap<>();
        map.put("site_id", site_id);
        map.put("pageNum", "" + pageSize);
        map.put("pageSize", "20");

        HttpRestClient.OKHttpStationUrl(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                mPullListView.setRefreshing();
            }

            @Override
            public void onAfter() {
                mPullListView.onRefreshComplete();
                super.onAfter();
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if ("0".equals(obj.optString("code"))) {
                        if (pageSize == 1)
                            mAdapter.removeAll();
                        JSONObject result = obj.optJSONObject("result");
//                        textView.setText("我的评论(" + result.optInt("commentNum") + ")");
                        ArrayList<Map<String, String>> list = new ArrayList<Map<String, String>>();
                        JSONArray array = result.getJSONArray("comment");
                        Map<String, String> map = null;
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonObject = array.getJSONObject(i);
                            map = new HashMap<String, String>();
                            map.put("DOCTOR_REAL_NAME", jsonObject.optString("DOCTOR_REAL_NAME"));
                            map.put("CUSTOMER_NICKNAME", jsonObject.optString("CUSTOMER_NICKNAME"));
                            map.put("EVALUATE_TIME", jsonObject.optString("EVALUATE_TIME"));
                            map.put("COMMENT_INFO", jsonObject.optString("COMMENT_INFO"));
                            map.put("SERVICE_TYPE_ID", jsonObject.optString("SERVICE_TYPE_ID"));
                            list.add(map);
                        }
                        if (list.size() > 0) {
                            pageSize++;
                            mAdapter.add(list);
                        }
                    } else {
                        ToastUtil.showShort(SiteCommentListActivity.this, obj.optString("message"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, this);
    }

    @Override
    protected void initView() {
        titleTextV.setText("患者评价");
        if (getIntent().hasExtra(SITE_ID))
            site_id = getIntent().getStringExtra(SITE_ID);
//        textView = new TextView(this);
//        textView.setPadding(16, 6, 16, 6);
//        textView.setTextColor(getResources().getColor(R.color.color_text_gray));
//        mListView.addHeaderView(textView);
        mAdapter = new EvelateAdapter(this);
        mPullListView.setAdapter(mAdapter);
        mPullListView.setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageSize = 1;
        initData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        initData();
    }


}
