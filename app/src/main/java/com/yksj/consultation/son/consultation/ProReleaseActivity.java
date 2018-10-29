package com.yksj.consultation.son.consultation;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.ItemPublicAdappter;
import com.yksj.consultation.adapter.ProReleasPopWdAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.bean.NewsClass;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.utils.ToastUtil;

import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 项目发布
 * by chen
 */
public class ProReleaseActivity extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private CheckBox selector_text;
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mLv;
    public ItemPublicAdappter adapter;
    private LinearLayout popupWLayout;
    private ListView mListview;//popup
    private ProReleasPopWdAdapter popAdapter;
    private View view;
    private List<JSONObject> mList = new ArrayList<>();
    private List<JSONObject> mmList =new ArrayList<>();
    private String class_id = "101";
    private View mEmptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pro_release);
        initView();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("项目发布");
        titleLeftBtn.setOnClickListener(this);
        selector_text = (CheckBox) findViewById(R.id.navigationbar_placee);
        popupWLayout = (LinearLayout) findViewById(R.id.popwindow_layout1);
        mListview = (ListView) findViewById(R.id.pop_list);
        popAdapter = new ProReleasPopWdAdapter(this,mList);
        mEmptyView = findViewById(R.id.empty_view_pro_release);
        view = findViewById(R.id.pop_grey_view);
        view.getBackground().setAlpha(80);
        view.setOnClickListener(this);
        selector_text.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!isChecked) {
                    popupWLayout.setVisibility(View.GONE);

                } else {
                    popupWLayout.setVisibility(View.VISIBLE);
                    initDataTitle();
                }
            }
        });
        mListview.setAdapter(popAdapter);
        mListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selector_text.setText(popAdapter.datas.get(position).optString("CLASS_NAME"));
                class_id = popAdapter.datas.get(position).optString("CLASS_ID");
                initData();
                outPopup();
            }
        });
        mPullToRefreshListView = (PullToRefreshListView) findViewById(R.id.item_publish);
        mLv = mPullToRefreshListView.getRefreshableView();
        adapter = new ItemPublicAdappter(mmList,this);
        mLv.setAdapter(adapter);
        mLv.setOnItemClickListener(this);
        initData();
    }

    //关闭popupwindow
    private void outPopup() {
        if (selector_text.isChecked()){
            selector_text.setChecked(false);
        }else{
            selector_text.setChecked(true);
        }
        popupWLayout.setVisibility(View.GONE);
    }

    /**
     * 加载popupwindow列表的数据
     */
    private void initDataTitle() {
        HttpRestClient.doHttpItemType(new AsyncHttpResponseHandler(this){
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject obj = new JSONObject(content);
                    JSONObject jsonobject  = obj.optJSONObject("projectclazz");

                    JSONArray array = jsonobject.getJSONArray("clazz");
                    mList = new ArrayList<JSONObject>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonobject1 = array.getJSONObject(i);
                        mList.add(jsonobject1);
                    }
                    popAdapter.onBoundData(mList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }
        });

    }
    /**
     * 下面详情数据
     */
    private void initData() {
        Map<String,String> map=new HashMap<>();
        map.put("class_id", class_id);
        HttpRestClient.OKHttpItemDetail(map, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        mmList = new ArrayList<>();
                        JSONArray array = obj.optJSONArray("arts");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonobject = array.getJSONObject(i);
                            mmList.add(jsonobject);
                        }
                        adapter.onBoundData(mmList);
                        if (mmList.size()==0){
                            mEmptyView.setVisibility(View.VISIBLE);
                            mPullToRefreshListView.setVisibility(View.GONE);
                        }else {
                            mEmptyView.setVisibility(View.GONE);
                            mPullToRefreshListView.setVisibility(View.VISIBLE);
                        }
                    }else{
                        ToastUtil.showShort(obj.optString("message"));
                        mEmptyView.setVisibility(View.VISIBLE);
                        mPullToRefreshListView.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;

            case R.id.pop_grey_view:
                outPopup();
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
      Intent intent = new Intent(this,ItemDetailActivity.class);
      intent.putExtra("PROJECT_ID",mmList.get(position-1).optString("PROJECT_ID"));
      startActivity(intent);
    }
}
