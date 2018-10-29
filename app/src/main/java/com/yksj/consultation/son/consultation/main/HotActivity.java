package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yksj.consultation.adapter.HotActivityAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.DAtyConslutDynMes;
import com.yksj.consultation.son.consultation.bean.NewsClass;
import com.yksj.healthtalk.entity.DynamicMessageListEntity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.SharePreUtils;
import com.yksj.healthtalk.utils.TimeUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 热门活动的界面
 * by chen
 */
public class HotActivity extends BaseFragmentActivity implements PullToRefreshBase.OnRefreshListener2<ListView>,View.OnClickListener, AdapterView.OnItemClickListener {
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private HotActivityAdapter adapter;
    private int mPagesize = 1;// 页码

    private List<DynamicMessageListEntity> nfeList;
    public static final String TYPEID="type_id";
    public static final String TYPENAME="type_name";
    private String typeid="1";
    private String typename="";
    private DynamicMessageListEntity dnlEntity;
    private View mNullView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hot);
        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("热门活动");
        mNullView = findViewById(R.id.mnullview);
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.hot_act_lv);
        mListView = mPullToRefreshListView.getRefreshableView();

        adapter = new HotActivityAdapter(HotActivity.this,0, null);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);

        mPullToRefreshListView.setOnRefreshListener(this);
        mPullToRefreshListView.setRefreshing();
        initData();
    }

    private void initData() {
        if (HStringUtil.isEmpty(typeid)){
            return;
        }
        RequestParams params = new RequestParams();
        params.put("consultation_center_id", HTalkApplication.APP_CONSULTATION_CENTERID);
        params.put("info_class_id", "201");
        HttpRestClient.doHttpHotMessageList(params, new ObjectHttpResponseHandler() {
            @Override
            public Object onParseResponse(String content) {
                nfeList = new ArrayList<DynamicMessageListEntity>();
                try {
                    JSONObject obj = new JSONObject(content);
                    JSONObject obj1 = obj.optJSONObject("news");
                    JSONArray array = obj1.optJSONArray("artList");
                    JSONObject item;
                    for (int i = 0; i < array.length(); i++) {
                        item = array.getJSONObject(i);
                            dnlEntity = new DynamicMessageListEntity();
                            dnlEntity.setConsultationCenterId(item.optInt("CONSULTATION_CENTER_ID"));
                            dnlEntity.setCustomerId(item.optInt("CUSTOMER_ID"));
                            dnlEntity.setInfoId(item.optInt("INFO_ID"));
                            dnlEntity.setInfoPicture(item.optString("INFO_PICTURE"));
                            String time = TimeUtil.formatTime(item.optString("PUBLISH_TIME"));
                            dnlEntity.setPublishTime(time);
                            dnlEntity.setStatusTime(item.optString("STATUS_TIME"));
                            dnlEntity.setInfoStaus(item.optString("INFO_STATUS"));
                            dnlEntity.setInfoName(item.optString("INFO_NAME"));
                            dnlEntity.setColorchage(0);
                            nfeList.add(dnlEntity);

                    }
                    SharePreUtils.saveDynamicReadedId(dnlEntity.getInfoId() + "");
                    return nfeList;
                } catch (JSONException e) {
                    return null;
                }
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mPullToRefreshListView.onRefreshComplete();
                if (adapter.getCount() == 0) {
                    mNullView.setVisibility(View.VISIBLE);
                    mPullToRefreshListView.setVisibility(View.GONE);
                } else {
                    mNullView.setVisibility(View.GONE);
                    mPullToRefreshListView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                if (response != null) {
                    if (mPagesize == 1) {
                        adapter.replaceAll((List<DynamicMessageListEntity>) response);
                    } else {
                        adapter.addAll((List<DynamicMessageListEntity>) response);
                    }
                }
//                if (nfeAdapter.getCount() == 0) {
//                    mNullView.setVisibility(View.VISIBLE);
//                    mPullToRefreshListView.setVisibility(View.GONE);
//                } else {
//                    mNullView.setVisibility(View.GONE);
//                    mPullToRefreshListView.setVisibility(View.VISIBLE);
//                }
            }
        });
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
        mPagesize = 1;
        initData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        mPagesize = 1;
        initData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(HotActivity.this, DAtyConslutDynMes.class);
        intent.putExtra("conId", HTalkApplication.APP_CONSULTATION_CENTERID);
        intent.putExtra("infoId", "" + adapter.datas.get(position - 1).getInfoId());
        intent.putExtra("title", typename);
        startActivity(intent);
    }
}
