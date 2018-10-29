package com.yksj.consultation.son.consultation.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.DiseaseContentAdapter;
import com.yksj.consultation.adapter.DiseaseDetailAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.home.DoctorStudioActivity;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 疾病详情界面
 */
public class DiseaseDetailActivity extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    public String name;
    private List<JSONObject> mList = null;
    private List<JSONObject> mList1 = null;
    private View header;
    private ListView mLv;
    private ListView content;
    private DiseaseDetailAdapter adapter;//推荐医生
    private DiseaseContentAdapter mAdapter;//详情
    private String disease_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_disease_detail);
        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        name = getIntent().getStringExtra("name");
        disease_id = getIntent().getStringExtra("disease_id");

        titleTextV.setText(name);
        mLv = (ListView) findViewById(R.id.disease_detail_lv);
        content= (ListView) findViewById(R.id.disease_detail_lv1);
        adapter = new DiseaseDetailAdapter(this);
        mAdapter = new DiseaseContentAdapter(this);
        mLv.setAdapter(adapter);
        content.setAdapter(mAdapter);
        mLv.setOnItemClickListener(this);
        initData();
        initDocData();
    }

    /**
     * 常见病详情数据
     */
    private void initData() {
        Map<String,String> map=new HashMap<>();
        map.put("disease_id", disease_id);//1
        HttpRestClient.OKHttpCommonIllDetail(map,  new HResultCallback<String>(this){

            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        mList1 = new ArrayList<>();
                        JSONArray array = obj.optJSONArray("disease");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonobject = array.getJSONObject(i);
                            mList1.add(jsonobject);
                        }
                        mAdapter.onBoundData(mList1);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }

    /**
     * 推荐医生
     */
    private void initDocData() {
        Map<String,String> map=new HashMap<>();
        map.put("disease_id", disease_id);//1
        HttpRestClient.OKHttpDoctorRecommend(map,new HResultCallback<String>(this){

            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        mList = new ArrayList<>();
                        if (obj.optJSONArray("doctors")!=null){

                            JSONArray array = obj.optJSONArray("doctors");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonobject = array.getJSONObject(i);
                                mList.add(jsonobject);
                            }
                            adapter.onBoundData(mList);

                        }
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
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(this, DoctorStudioActivity.class);
        intent.putExtra("DOCTOR_ID",adapter.datas.get(position).optString("CUSTOMER_ID"));
        startActivity(intent);
    }
}
