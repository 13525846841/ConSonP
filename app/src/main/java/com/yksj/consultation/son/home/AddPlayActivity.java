package com.yksj.consultation.son.home;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.views.LoopView;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 添加新计划
 */
public class AddPlayActivity extends BaseFragmentActivity implements View.OnClickListener {
    private RelativeLayout rootview;
    private RelativeLayout.LayoutParams layoutParams;
    private TextView tv_addplan_name;
    private TextView tv_addplan_sex;
    private TextView tv_addplan_age;
    private ImageView addplan_image_head;
    private TextView mTime;
    private EditText et_title;//计划标题
    private EditText et_target;//计划目标
    private String CHILDREN_ID;//宝贝ID
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_play);
        initView();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("添加计划");
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setText("完成");


        Intent intent = getIntent();
        CHILDREN_ID = intent.getStringExtra("CHILDREN_ID");

        titleLeftBtn.setOnClickListener(this);
        titleRightBtn2.setOnClickListener(this);
        addplan_image_head = (ImageView) findViewById(R.id.addplan_image_head);
        tv_addplan_name = (TextView) findViewById(R.id.tv_addplan_name);
        tv_addplan_sex = (TextView) findViewById(R.id.tv_addplan_sex);
        tv_addplan_age = (TextView) findViewById(R.id.tv_addplan_age);

        tv_addplan_name.setText(intent.getStringExtra("name"));

        Picasso.with(AddPlayActivity.this).load(intent.getStringExtra("url")).placeholder(R.drawable.default_head_patient).into(addplan_image_head);

        if ("null".equals(intent.getStringExtra("age"))){
            tv_addplan_age.setVisibility(View.GONE);
        }else{
            tv_addplan_age.setText(intent.getStringExtra("age"));
        }

        if ("1".equals(intent.getStringExtra("sex"))){
            tv_addplan_sex.setText("男");
        }else if ("0".equals(intent.getStringExtra("sex"))){
            tv_addplan_sex.setText("女");
        }else {
            tv_addplan_sex.setVisibility(View.GONE);
        }
        et_title = (EditText) findViewById(R.id.et_title);
        et_target = (EditText) findViewById(R.id.et_target);
        mTime = (TextView) findViewById(R.id.plan_time);
        //加载选择时间的轮播图
        mLoopView();
    }
    //滚动图添加
    private void mLoopView() {
        //滚动选项的添加
        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT/3, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        rootview = (RelativeLayout) findViewById(R.id.mloopview);
        LoopView loopView = new LoopView(this);
        ArrayList<String> list = new ArrayList<>();
        for (int i = 1; i < 52; i++) {
            list.add( i+"星期");
        }
        //滚动监听
        loopView.setListener(new LoopView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int index) {
              mTime.setText(index + 1 +"星期");
              //mTime.setText(index + 1 + "");
            }
        });
        //设置原始数据
        loopView.setItems(list);
        //设置初始位置
        loopView.setInitPosition(0);
        //设置字体大小
        loopView.setTextSize(22);
        rootview.addView(loopView, layoutParams);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right2:
                addPlan();
                break;
        }
    }
    public String title;//标题
    public String target;//目标
    public String period;//周期

    private void addPlan() {
        initDate();
    }
    private String customer_id = SmartFoxClient.getLoginUserId();
    /**
     * 加载数据
     */
    private void initDate() {
        title = et_title.getText().toString().trim();
        target= et_target.getText().toString().trim();
        period= mTime.getText().toString().trim().substring(0,mTime.getText().toString().length()-2);

        if (TextUtils.isEmpty(title)) {
            ToastUtil.showToastPanl("请填写计划标题");
            return;
        }
        if (TextUtils.isEmpty(target)) {
            ToastUtil.showToastPanl("请填写计划目标");
            return;
        }

        Map<String,String> map=new HashMap<>();
        map.put("children_id", CHILDREN_ID);
        map.put("customer_id", customer_id);
        map.put("plan_cycle", period);
        map.put("plan_target", target);
        map.put("plan_title", title);
        HttpRestClient.OKHttpAddPlan(map, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
                ToastUtil.showShort("添加失败");
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        finish();
                    }
                    ToastUtil.showShort(obj.optString("message"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }

}
