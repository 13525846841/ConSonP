package com.yksj.consultation.son.home;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.PlanAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.DocPlanEntity;
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
 * 医教计划
 *
 */
public class MyDoctorPlan extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView mLv;
    private PlanAdapter adapter;
    private List<DocPlanEntity> data;
    private DocPlanEntity dpEntity;
    private View footview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_doctor_plan);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("医教计划");
        mLv = (ListView) findViewById(R.id.list);
        footview = View.inflate(this,R.layout.doc_plan_foot,null);
        mLv.addFooterView(footview);
        adapter = new PlanAdapter(this);
        mLv.setAdapter(adapter);
        mLv.setOnItemClickListener(this);
        titleLeftBtn.setOnClickListener(this);
        footview.findViewById(R.id.tianjia).setOnClickListener(this);
    }
    private String id = "4198";
    private String customer_id = LoginServiceManeger.instance().getLoginUserId();

    /**
     * 医教联盟计划列表
     */
    private void initData() {

        Map<String,String> map=new HashMap<>();
        map.put("customer_id",customer_id);
        HttpRestClient.OKHttpGetPlanList(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                data = new ArrayList<DocPlanEntity>();
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        JSONArray array = obj.getJSONArray("childrens");
                        JSONObject item;
                        for (int i = 0; i < array.length(); i++) {
                            item = array.getJSONObject(i);
                            dpEntity = new DocPlanEntity();
                            dpEntity.setCHILDREN_NAME(item.optString("CHILDREN_NAME"));
                            dpEntity.setCHILDREN_ID(item.optString("CHILDREN_ID"));
                            dpEntity.setCUSTOMER_ID(item.optString("CUSTOMER_ID"));
                            dpEntity.setCHILDREN_BIRTHDAY(item.optString("CHILDREN_BIRTHDAY"));
                            dpEntity.setCHILDREN_REMARK(item.optString("CHILDREN_REMARK"));
                            dpEntity.setCHILDREN_HIGHT(item.optString("CHILDREN_HIGHT"));
                            dpEntity.setCHILDREN_WEIGHT(item.optString("CHILDREN_WEIGHT"));
                            dpEntity.setCHILDREN_SEX(item.optString("CHILDREN_SEX"));
                            dpEntity.setHEAD_PORTRAIT_ICON(item.optString("HEAD_PORTRAIT_ICON"));
                            data.add(dpEntity);
                        }
                        adapter.onBoundData(data);
                    }else {
                        ToastUtil.showShort(obj.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.tianjia://添加宝贝
                intent = new Intent(MyDoctorPlan.this,AddBabyActivity.class);
                intent.putExtra(AddBabyActivity.TYPE,"add");
                startActivity(intent);
                break;
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        if (position==parent.getChildCount()-1){
            return;
        }else{
            Intent intent = new Intent(this, PlanListActivity.class);
            intent.putExtra("CHILDREN_ID",adapter.datas.get(position).getCHILDREN_ID());
            startActivity(intent);
        }

    }
}
