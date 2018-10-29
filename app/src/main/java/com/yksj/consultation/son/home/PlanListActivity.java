package com.yksj.consultation.son.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.squareup.picasso.Picasso;
import com.yksj.consultation.adapter.BaseListPagerAdpater;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.entity.DocPlanMemberEntity;
import com.yksj.healthtalk.entity.TextEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * xx的医教计划
 * by chen
 */
public class PlanListActivity extends BaseFragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private TextView add_new_plan;
    private ViewPager viewpager;
    private RadioGroup radiogroup;
    private BaseListPagerAdpater mPagerAdapter;
    private TextView tv_plan_name;
    private TextView tv_plan_sex;
    private TextView tv_plan_age;
    private ImageView image;
    private String children_id;//宝贝ID
    private JSONObject contentObject;//内容JSONObject
    private List<TextEntity> data;
    public TextEntity textEntity;
    private String name="";
    private  String sex="";
    private  String age="";
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_list);
        initView();
    }
    @Override
    protected void onStart() {
        super.onStart();
//        initData();
    }
    private void initView() {
        initTitle();
        image = (ImageView) findViewById(R.id.image);
        data = new ArrayList<TextEntity>();
        Intent intent = getIntent();
        children_id = intent.getStringExtra("CHILDREN_ID");
        initData();
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setText("成员");
        add_new_plan = (TextView) findViewById(R.id.add_new_plan);

        tv_plan_name = (TextView) findViewById(R.id.tv_plan_name);
        tv_plan_sex = (TextView) findViewById(R.id.tv_plan_sex);
        tv_plan_age = (TextView) findViewById(R.id.tv_plan_age);
        image = (ImageView) findViewById(R.id.image);
        add_new_plan.setOnClickListener(this);
        titleLeftBtn.setOnClickListener(this);
        titleRightBtn2.setOnClickListener(this);
        viewpager = (ViewPager) findViewById(R.id.my_plan_frag);
        viewpager.setOffscreenPageLimit(2);
        radiogroup = (RadioGroup) findViewById(R.id.my_plan);
        radiogroup.setOnCheckedChangeListener(this);

        mPagerAdapter = new BaseListPagerAdpater(getSupportFragmentManager());
        viewpager.setAdapter(mPagerAdapter);
        viewpager.setOnPageChangeListener(this);
        radiogroup.setOnCheckedChangeListener(this);

    }

    private void initFragment() {
        ArrayList<Fragment> dpList = new ArrayList<Fragment>();
        // 进行中的计划
        Fragment waitFragment = new DocPlanFragment();
        Bundle handle = new Bundle();
        handle.putString("typeList", "0");
        handle.putString("children_id",children_id);
        handle.putString("name",name);
        handle.putString("sex",sex);
        handle.putString("age",age);
        handle.putString("url",url);
        waitFragment.setArguments(handle);
        dpList.add(waitFragment);
        // 已完成的计划
        Fragment doneFragment1 = new DocPlanFragment();
        Bundle bundle = new Bundle();
        bundle.putString("typeList", "1");
        bundle.putString("children_id",children_id);
        bundle.putString("name",name);
        bundle.putString("sex",sex);
        bundle.putString("age",age);
        bundle.putString("url",url);
        doneFragment1.setArguments(bundle);
        dpList.add(doneFragment1);
        mPagerAdapter.onBoundFragment(dpList);
        //显示第一页
        viewpager.setCurrentItem(0, false);
    }

    /**
     * 加载数据
     */
    private void initData() {

        Map<String,String> map=new HashMap<>();
        map.put("children_id",children_id);
        HttpRestClient.OKHttpGetBabyInfo(map, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject object = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(object.optString("code"))){
                        JSONObject massage = object.optJSONObject("children");
                        name = massage.optString("CHILDREN_NAME");
                        sex = massage.optString("CHILDREN_SEX");
                        age = massage.optString("CHILDREN_YEAR");

                        if ("null".equals(name)){
                            tv_plan_name.setVisibility(View.GONE);
                        }else{
                            tv_plan_name.setText(name);
                        }

                        if ("null".equals(sex)){
                            tv_plan_sex.setVisibility(View.GONE);
                        }else{
                            if (sex.equals("1")){
                                tv_plan_sex.setText("男");
                            }else if (sex.equals("0")){
                                tv_plan_sex.setText("女");
                            }
                        }

                        if ("null".equals(age)){
                            tv_plan_age.setVisibility(View.GONE);
                        }else{
                            tv_plan_age.setText(age);
                        }

                        titleTextV.setText(name +"的医教计划");
                        //图片展示
                        url= HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW+massage.optString("HEAD_PORTRAIT_ICON");
                        Picasso.with(PlanListActivity.this).load(url).placeholder(R.drawable.default_head_patient).into(image);
                        initFragment();
                    }else {
                        ToastUtil.showShort(object.optString("message"));
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
            case R.id.title_right2:
                intent = new Intent(this,MemberActivity.class);
                intent.putExtra("CHILDREN_ID",children_id);
                startActivity(intent);
                break;
            case R.id.add_new_plan://添加新计划
                intent = new Intent(this,AddPlayActivity.class);
                intent.putExtra("name",name);
                intent.putExtra("age",age);
                intent.putExtra("sex",sex);
                intent.putExtra("url",url);
                intent.putExtra("CHILDREN_ID",children_id);
                startActivity(intent);
                break;
        }
    }
    /**
     * 切换radioButton时的事件,是ViewPager也随之切换
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i=0;i<radiogroup.getChildCount();i++){
            RadioButton childAt = (RadioButton) group.getChildAt(i);
            if (childAt.isChecked()){
                viewpager.setCurrentItem(i,false);
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        RadioButton mButton = (RadioButton) radiogroup.getChildAt(position);
        mButton.setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
