package com.yksj.consultation.son.consultation.news;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yksj.consultation.adapter.BaseListPagerAdpater;
import com.yksj.consultation.adapter.SixOnePopupAdapter;
import com.yksj.consultation.adapter.SixOneSecPopupAdapter;
import com.yksj.consultation.adapter.SixOneThirdPopupAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 六一健康
 */
public class SixOneActivity extends BaseFragmentActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {

    private ViewPager mViewPager;
    private RadioGroup mRadioGroup;//页签
    private RadioButton common,famous,hospital,classroom;
    private List<JSONObject> mmList = null;
    private List<JSONObject> mmList2 = null;
    private List<JSONObject> mmList3 = null;

    private PopupWindow pw;
    private ListView mlv_news,mlv_news2,mlv_news3;
    private SixOnePopupAdapter adapter;
    private SixOneSecPopupAdapter Secadapter;
    private SixOneThirdPopupAdapter Thirdadapter;
    private String name,name2,name3;
    public String class_id="101"
                 ,class_id2="101"
                 ,class_id3="101";
    public Commondis waitFragment;
    public Famousdochos doneFragment1;
    public DocTeach doneFragment2;

//    private CheckBox commonCheck;
//    private CheckBox famousCheck,lastCheckBox;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_six_one);
        initView();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("六一百科");
        titleLeftBtn.setVisibility(View.VISIBLE);
        mRadioGroup = (RadioGroup) findViewById(R.id.six_one);
        titleLeftBtn.setOnClickListener(this);

        mViewPager = (ViewPager) findViewById(R.id.sixone_viewpager);
        BaseListPagerAdpater mAdpater = new BaseListPagerAdpater(getSupportFragmentManager());
        mViewPager.setAdapter(mAdpater);
        mViewPager.setOffscreenPageLimit(3);
        ArrayList<Fragment> mList = new ArrayList<Fragment>();

        mViewPager.setOnPageChangeListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);

        common = (RadioButton) findViewById(R.id.my_planing);
        hospital=(RadioButton)findViewById(R.id.fro_medicine) ;
        famous = (RadioButton) findViewById(R.id.famous_doctor_hospital);
        classroom = (RadioButton) findViewById(R.id.doctor_teach);
        common.setTextColor(getResources().getColor(R.color.color_blue));
        common.setOnClickListener(this);
        famous.setOnClickListener(this);
        classroom.setOnClickListener(this);
        hospital.setOnClickListener(this);
//        commonCheck = (CheckBox) findViewById(R.id.iv_planing);
//        famousCheck = (CheckBox) findViewById(R.id.iv_famous_doctor_hospital);
//        findViewById(R.id.iv_planing).setOnClickListener(this);
//        findViewById(R.id.iv_famous_doctor_hospital).setOnClickListener(this);


//        common.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (buttonView.isPressed()){
//                  if (isChecked){
//                      common.setButtonDrawable(R.drawable.pop_arrows_index_down2);
//                  }else {
//                      common.setButtonDrawable(R.drawable.pop_arrows_index_down2);
//                  }
//                }
//            }
//        });

        mlv_news= (ListView) View.inflate(this,R.layout.list_news_popup,null);
        mlv_news2= (ListView) View.inflate(this,R.layout.list_news_popup,null);
        mlv_news3= (ListView) View.inflate(this,R.layout.list_news_popup,null);

        adapter = new SixOnePopupAdapter(this);
        Secadapter = new SixOneSecPopupAdapter(this);
        Thirdadapter = new SixOneThirdPopupAdapter(this);

        mlv_news.setAdapter(adapter);
        mlv_news2.setAdapter(Secadapter);
        mlv_news3.setAdapter(Thirdadapter);

        // 常见疾病
        waitFragment = new Commondis();
        mList.add(waitFragment);
        mAdpater.onBoundFragment(mList);
        //医学前沿
        Fragment doneFragment0 = new FroMedicine();
        mList.add(doneFragment0);
        mAdpater.onBoundFragment(mList);
        // 名医名院
        doneFragment1 = new Famousdochos();
        mList.add(doneFragment1);
        mAdpater.onBoundFragment(mList);
        // 名医讲堂
        doneFragment2 = new DocTeach();
        mList.add(doneFragment2);
        mAdpater.onBoundFragment(mList);
        //显示第一页
        mViewPager.setCurrentItem(0, false);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right:
                Intent intent  = new Intent(this,HealthTreeAty.class);
                startActivity(intent);
                break;

            case R.id.my_planing:
                initCommonData();
                showPopup();
                break;
            case R.id.famous_doctor_hospital:
                initFamousData();
                showPopup2();
                break;

            case R.id.doctor_teach:
                initClassRoom();
                showPopup3();

                break;
        }
    }

    /**
     * 常见疾病列表
     */
    private void showPopup() {
        if (pw == null) {
            pw = new PopupWindow(this);
            pw.setWidth(ViewPager.LayoutParams.MATCH_PARENT);
            pw.setHeight(ViewPager.LayoutParams.WRAP_CONTENT);
            pw.setFocusable(true);
            pw.setOutsideTouchable(true);
            pw.setBackgroundDrawable(new BitmapDrawable());
        }
        WindowManager.LayoutParams params=this.getWindow().getAttributes();
        params.alpha=0.8f;
        this.getWindow().setAttributes(params);

        pw.setContentView(mlv_news);
//        pw.setContentView(mlv_news2);
//        pw.setContentView(mlv_news3);

        if(!pw.isShowing());
        pw.showAsDropDown(common,0,0);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });

        mlv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (pw.isShowing()){
                    pw.dismiss();
                }
                name = adapter.getName(position);
                class_id = adapter.getClass_id(position);
                common.setText(name);
                waitFragment.setClassId(class_id);

            }
        });

    }
    //名医名媛
    private void showPopup2() {
        if (pw == null) {
            pw = new PopupWindow(this);
            pw.setWidth(ViewPager.LayoutParams.MATCH_PARENT);
            pw.setHeight(ViewPager.LayoutParams.WRAP_CONTENT);
            pw.setFocusable(true);
            pw.setOutsideTouchable(true);
            pw.setBackgroundDrawable(new BitmapDrawable());
        }
        WindowManager.LayoutParams params=this.getWindow().getAttributes();
        params.alpha=0.8f;
        this.getWindow().setAttributes(params);

//      pw.setContentView(mlv_news);
        pw.setContentView(mlv_news2);
//      pw.setContentView(mlv_news3);

        if(!pw.isShowing());
        pw.showAsDropDown(common,0,0);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        mlv_news2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (pw.isShowing()){
                    pw.dismiss();
                }
                name2 = Secadapter.getName2(position);
                class_id2 = Secadapter.getClass_id2(position);
                famous.setText(name2);
                doneFragment1.setClassId(class_id2);
            }
        });
    }
    //名医讲台
    private void showPopup3() {
        if (pw == null) {
            pw = new PopupWindow(this);
            pw.setWidth(ViewPager.LayoutParams.MATCH_PARENT);
            pw.setHeight(ViewPager.LayoutParams.WRAP_CONTENT);
            pw.setFocusable(true);
            pw.setOutsideTouchable(true);
            pw.setBackgroundDrawable(new BitmapDrawable());
        }
        WindowManager.LayoutParams params=this.getWindow().getAttributes();
        params.alpha=0.8f;
        this.getWindow().setAttributes(params);

//        pw.setContentView(mlv_news);
//        pw.setContentView(mlv_news2);
        pw.setContentView(mlv_news3);

        if(!pw.isShowing());
        pw.showAsDropDown(common,0,0);
        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
        mlv_news3.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (pw.isShowing()){
                    pw.dismiss();

                }
                name3 = Thirdadapter.getName3(position);
                class_id3 = Thirdadapter.getClass_id3(position);
                classroom.setText(name3);
                doneFragment2.setClassId(class_id3);

            }
        });
    }
    /**
     * 名医讲堂 类型
     */
    private void initClassRoom() {
        HttpRestClient.doHttpFamDocClassRoom(new AsyncHttpResponseHandler(this){
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject obj = new JSONObject(content);
                    JSONObject jsonobject  = obj.optJSONObject("forum");

                    JSONArray array = jsonobject.getJSONArray("clazz");
                    mmList3 = new ArrayList<JSONObject>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonobject1 = array.getJSONObject(i);
                        mmList3.add(jsonobject1);
                    }
                    Thirdadapter.onBoundData(mmList3);

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
     * 加载名医名院分类
     */
    private void initFamousData() {
        HttpRestClient.doHttpFamousHospital(new AsyncHttpResponseHandler(this){
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject obj = new JSONObject(content);
                    JSONObject jsonobject  = obj.optJSONObject("clazz");

                    JSONArray array = jsonobject.getJSONArray("clazz");
                    mmList2 = new ArrayList<JSONObject>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonobject1 = array.getJSONObject(i);
                        mmList2.add(jsonobject1);
                    }
                    Secadapter.onBoundData(mmList2);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                ToastUtil.showShort("添加失败");
            }
        });
    }

    /**
     * 加载常见疾病的分类
     */
    private void initCommonData() {
        HttpRestClient.doHttpCommonIll(new AsyncHttpResponseHandler(this){
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject obj = new JSONObject(content);
                    JSONObject jsonobject  = obj.optJSONObject("clazz");
                    JSONArray array = jsonobject.getJSONArray("clazz");
                    mmList = new ArrayList<JSONObject>();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonobject1 = array.getJSONObject(i);
                        mmList.add(jsonobject1);
                    }
                    adapter.onBoundData(mmList);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                ToastUtil.showShort("添加失败");
            }
        });
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton childAt = (RadioButton) group.getChildAt(i);
            if (childAt.isChecked()) {
                mViewPager.setCurrentItem(i, false);
                childAt.setSelected(true);
            }else{
                childAt.setSelected(false);
            }
    }

//        for(int i=0;i<group.getChildCount();i++){
//            if(group.getChildAt(i).getId()==checkedId){
//                if(mViewPager.getCurrentItem()!=i){
//                    mViewPager.setCurrentItem(i,false);
////                    group.check(checkedId);
//                }
//                break;
//            }
//        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
      //  mRadioGroup.check(mRadioGroup.getChildAt(position).getId());

        setBackGroud(position);

//        RadioButton mButton = (RadioButton) mRadioGroup.getChildAt(position);
//      //  mButton.setChecked(true);
//        boolean ischeck;
//        if (mButton.isChecked()){
//            ischeck=true;
//            mButton.setTextColor(getResources().getColor(R.color.color_blue));
//        }else {
//            ischeck = false;
//            mButton.setTextColor(getResources().getColor(R.color.gray_text));
//        }
//            mButton.setChecked(ischeck);

    }

    private void setBackGroud(int position) {
//        if (0==position){
//            common.setTextColor(getResources().getColor(R.color.color_blue));
//            famous.setTextColor(getResources().getColor(R.color.gray_text));
//        }else if (1==position){
//            famous.setTextColor(getResources().getColor(R.color.color_blue));
//            common.setTextColor(getResources().getColor(R.color.gray_text));
//        }
        common.setTextColor(position==0?getResources().getColor(R.color.color_blue):getResources().getColor(R.color.gray_text));
        hospital.setTextColor(position==1?getResources().getColor(R.color.color_blue):getResources().getColor(R.color.gray_text));
        famous.setTextColor(position==2?getResources().getColor(R.color.color_blue):getResources().getColor(R.color.gray_text));
        classroom.setTextColor(position==3?getResources().getColor(R.color.color_blue):getResources().getColor(R.color.gray_text));
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }


//    /**
//     * 初始化Fragment
//     */
//
//    /**
//     * 选择的Column里面的Tab
//     */
//    private void selectTab(int tab_postion) {
//        columnSelectIndex = tab_postion;
//        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
//            View checkView = mRadioGroup_content.getChildAt(tab_postion);
//            int k = checkView.getMeasuredWidth();
//            int l = checkView.getLeft();
//            int i2 = l + k / 2 - mScreenWidth / 2;
//            // rg_nav_content.getParent()).smoothScrollTo(i2, 0);
//
//            // mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
//            // mItemWidth , 0);
//        }
//
//        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
//            RadioButton checkView = (RadioButton) mRadioGroup_content.getChildAt(j);
//            checkView.setSelected(true);
//        }
//
//        //判断是否选中
//        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
////          RadioButton checkView = (RadioButton) mRadioGroup_content.getChildAt(j).findViewById(R.id.rb_text);
//            RadioButton checkView = (RadioButton) mRadioGroup_content.getChildAt(j);
//            boolean ischeck;
//            if (j == tab_postion) {
//                ischeck = true;
//            } else {
//                ischeck = false;
//            }
////          checkView.setSelected(ischeck);
//            checkView.setChecked(ischeck);
//        }
//    }
}
