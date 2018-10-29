package com.yksj.consultation.son.home;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.EvaluationAdapter;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.EvaluationEntity;
import com.yksj.healthtalk.entity.HospitaFindTeamEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;

import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class EvaluationActivity extends Activity implements PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener {
    private View mEmptyView;
    private List<EvaluationEntity.ResultBean> mList=new ArrayList<>();
    private int page=1;
    private String type;
    private String Doctor_ID,course_ID;
    private String Site_Id;
    private PullToRefreshListView evaluationListView;
    private EvaluationAdapter evaluationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_evaluation);
        initView();
        if (type.equals("heath")){
            loadPersonClassroom();
        }else {
            loadDataWss();
        }

    }

    private void initView() {
        //works 工作站   doctor 医生  heath健康讲堂课程
        type = getIntent().getStringExtra("type");
        Doctor_ID = getIntent().getStringExtra("Doctor_ID");
        course_ID = getIntent().getStringExtra("course_ID");
        Site_Id = getIntent().getStringExtra("Site_Id");
        findViewById(R.id.title_back).setOnClickListener(this);
        TextView title= (TextView) findViewById(R.id.title_lable);
        title.setText("用户评论");
        mEmptyView = findViewById(R.id.load_faile_layout);
        evaluationListView = (PullToRefreshListView) findViewById(R.id.evaluationListView);
        evaluationListView.setOnRefreshListener(this);
        evaluationAdapter = new EvaluationAdapter(this, mList);
        ListView listView = evaluationListView.getRefreshableView();
        listView.setSelector(new ColorDrawable(Color.parseColor("#00000000")));
        listView.setAdapter(evaluationAdapter);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        page=1;
        mList.clear();
        if (type.equals("heath")){
            loadPersonClassroom();
        }else {
            loadDataWss();
        }
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        page++;
        if (type.equals("heath")){
            loadPersonClassroom();
        }else {
            loadDataWss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:finish();break;
        }
    }

    private void loadPersonClassroom(){
        List<BasicNameValuePair> pairs = new ArrayList<>();
            pairs.add(new BasicNameValuePair("op", "queryEvaluation"));
            pairs.add(new BasicNameValuePair("course_ID", course_ID));
            pairs.add(new BasicNameValuePair("pageNum", page +""));
        HttpRestClient.doGetPersonClassroom(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                evaluationListView.setRefreshing();
            }

            @Override
            public void onAfter() {
                evaluationListView.onRefreshComplete();
                super.onAfter();
            }

            @Override
            public void onResponse(String response) {
                Log.i("hhh", "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");
                    for (int i = 0; i < result.length(); i++) {
                        JSONObject resultJson = result.getJSONObject(i);
                        String iconBackground = resultJson.getString("CLIENT_ICON_BACKGROUND");
                        String nickname = resultJson.getString("CUSTOMER_NICKNAME");
                        String evaluateContent = resultJson.getString("EVALUATE_CONTENT");
                        String time = resultJson.getString("EVALUATE_TIME");
                        EvaluationEntity.ResultBean resultBean = new EvaluationEntity.ResultBean();
                        resultBean.setBIG_ICON_BACKGROUND(iconBackground);
                        resultBean.setCUSTOMER_NAME(nickname);
                        resultBean.setEVALUATE_TIME(time);
                        resultBean.setNOTE(evaluateContent);
                        mList.add(resultBean);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                evaluationAdapter.onBoundData(mList);
                if (mList.size()==0){
                    mEmptyView.setVisibility(View.VISIBLE);
                    evaluationListView.setVisibility(View.GONE);
                }else {
                    mEmptyView.setVisibility(View.GONE);
                    evaluationListView.setVisibility(View.VISIBLE);
                }
            }
        },this);
    }

    private void loadDataWss(){
        List<BasicNameValuePair> pairs = new ArrayList<>();
        if (type.equals("doctor")){
            pairs.add(new BasicNameValuePair("Type", "evaluateInDoctor"));
            pairs.add(new BasicNameValuePair("Doctor_ID", Doctor_ID));
        }else {
            pairs.add(new BasicNameValuePair("Type", "evaluateInWorkSite"));
            pairs.add(new BasicNameValuePair("Site_Id", "210"));
        }

        pairs.add(new BasicNameValuePair("PageSize", "10"));
        pairs.add(new BasicNameValuePair("Page", page +""));
        HttpRestClient.doGetWss(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                evaluationListView.setRefreshing();
            }

            @Override
            public void onAfter() {
                evaluationListView.onRefreshComplete();
                super.onAfter();
            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                EvaluationEntity evaluationEntity = gson.fromJson(response, EvaluationEntity.class);
                List<EvaluationEntity.ResultBean> result = evaluationEntity.getResult();
                mList.addAll(result);
                evaluationAdapter.onBoundData(mList);
                if (mList.size()==0){
                    mEmptyView.setVisibility(View.VISIBLE);
                    evaluationListView.setVisibility(View.GONE);
                }else {
                    mEmptyView.setVisibility(View.GONE);
                    evaluationListView.setVisibility(View.VISIBLE);
                }
            }
        },this);

    }

}
