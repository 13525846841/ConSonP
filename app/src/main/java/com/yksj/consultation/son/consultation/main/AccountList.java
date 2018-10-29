package com.yksj.consultation.son.consultation.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.BillAdapper;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.Model;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 账单明细
 */
public class AccountList extends BaseFragmentActivity  implements View.OnClickListener {

    private ListView mListView;
    public BillAdapper adapter;
    private List<JSONObject> mList = new ArrayList<>();
    private RelativeLayout mEmptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_list);
        initView();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("账单明细");
        titleLeftBtn.setOnClickListener(this);
        mListView = (ListView) findViewById(R.id.account_lv);
        adapter = new BillAdapper(mList,this);
        mListView.setAdapter(adapter);
        mEmptyView = (RelativeLayout) findViewById(R.id.load_faile_layout);
        initData();
    }

    private String customer_id = LoginServiceManeger.instance().getLoginUserId();
    private void initData() {
        Map<String,String> map=new HashMap<>();
        map.put("CUSTOMERID", customer_id);//customer_id／／116305
        map.put("PAGENUM", "1");
        map.put("PAGECOUNT", "5");
        HttpRestClient.OKHttpACCOUNTCHANGE(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                ToastUtil.showShort("添加失败");
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if ("1".equals(obj.optString("code"))) {
                        JSONArray array = obj.getJSONArray("result");
                        mList = new ArrayList<JSONObject>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonobject = array.getJSONObject(i);
                            mList.add(jsonobject);
                        }
                    }
                    adapter.onBoundData(mList);
                    if (mList.size()==0){
                        mEmptyView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    }else {
                        mEmptyView.setVisibility(View.GONE);
                        mListView.setVisibility(View.VISIBLE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
        }
    }
}
