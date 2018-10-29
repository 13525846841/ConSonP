package com.yksj.consultation.son.consultation;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import com.yksj.consultation.adapter.ProReleasPopWdAdapter;
import com.yksj.consultation.adapter.PubFundAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * ，公益基金
 */
public class PubFundActivity extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private CheckBox selector_text;
    private LayoutInflater mInflater;
    private org.handmark.pulltorefresh.library.PullToRefreshListView pullListView;
    private ListView mLv;
    private PubFundAdapter MainAdapter;
    public static final String TYPE = "type";
    public String type;
    private View view;
    private ListView mListview;//popup
    private LinearLayout popupWLayout;
    private ProReleasPopWdAdapter popAdapter;
    private List<JSONObject> mList = new ArrayList<>();
    private String class_id = "101";
    private View mEmptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fam_doc);
        initView();
    }

    private void initView() {
        initTitle();
        initData();
        titleTextV.setText("公益基金");
        view = findViewById(R.id.pop_grey_view);
        view.getBackground().setAlpha(80);
        view.setOnClickListener(this);

        popupWLayout = (LinearLayout) findViewById(R.id.popwindow_layout1);
        titleLeftBtn.setOnClickListener(this);
        selector_text = (CheckBox) findViewById(R.id.navigationbar_placee);
        mInflater = LayoutInflater.from(this);
        mEmptyView = findViewById(R.id.empty_view_pro_fam_doc);
        pullListView = (org.handmark.pulltorefresh.library.PullToRefreshListView) findViewById(R.id.famous_doc_act_lv);
        mLv = pullListView.getRefreshableView();
        mListview = (ListView) findViewById(R.id.pop_list);
        popAdapter = new ProReleasPopWdAdapter(this,mList);
        MainAdapter = new PubFundAdapter(this);
        mLv.setAdapter(MainAdapter);
        
        mLv.setOnItemClickListener(this);
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
    }

    /**
     * 加载类型数据
     */
    private void initDataTitle() {
        HttpRestClient.doHttpProFundType(new AsyncHttpResponseHandler(this){
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        JSONObject jsonobject  = obj.optJSONObject("fundClass");

                        JSONArray array = jsonobject.getJSONArray("clazz");
                        mList = new ArrayList<JSONObject>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonobject1 = array.getJSONObject(i);
                            mList.add(jsonobject1);
                        }
                        popAdapter.onBoundData(mList);

                    }else{
                        ToastUtil.showShort(obj.optString("message"));
                    }
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
     * 加载数据
     */
    private void initData() {
        HttpRestClient.OKHttpFindFund( class_id,new AsyncHttpResponseHandler(this){
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject obj = new JSONObject(content);
                   if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                       JSONArray array = obj.optJSONArray("fund");
                       mList = new ArrayList<JSONObject>();
                       for (int i = 0; i < array.length(); i++) {
                           JSONObject jsonobject = array.getJSONObject(i);
                           mList.add(jsonobject);
                       }
                       MainAdapter.onBoundData(mList);
                       if (mList.size()==0){
                           mEmptyView.setVisibility(View.VISIBLE);
                           pullListView.setVisibility(View.GONE);
                       }else {
                           mEmptyView.setVisibility(View.GONE);
                           pullListView.setVisibility(View.VISIBLE);
                       }
                   }else{
                       ToastUtil.showShort(obj.optString("message"));
                       mEmptyView.setVisibility(View.VISIBLE);
                       pullListView.setVisibility(View.GONE);
                   }
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

    //关闭popupwindow
    private void outPopup() {
        if (selector_text.isChecked()){
            selector_text.setChecked(false);
        }else{
            selector_text.setChecked(true);
        }
        popupWLayout.setVisibility(View.GONE);
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
        intent.putExtra("FUND_ID",mList.get(position-1).optString("FUND_ID"));
        startActivity(intent);
    }
}
