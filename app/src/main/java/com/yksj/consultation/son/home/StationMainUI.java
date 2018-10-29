package com.yksj.consultation.son.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yksj.consultation.adapter.BaseListPagerAdpater;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.CommonUtils;
import com.yksj.healthtalk.utils.ToastUtil;

import java.util.ArrayList;

/**
 * 名医联诊
 * Created by lmk on 15/9/16.
 */
public class StationMainUI extends BaseFragmentActivity implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener, TextWatcher {

    private ViewPager viewpager;
    private RadioGroup radiogroup;
    private BaseListPagerAdpater mPagerAdapter;
    private ArrayList<Fragment> mlList;
    private TextView famousInstitute;
    private EdtSearchChanageListener edtSearchChanageListener;
    private TextView tvSearch;
    private EditText edtSearch;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.station_main_ui);
        initView();
        initViewPager();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("名医联诊");
        titleLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        viewpager=(ViewPager) findViewById(R.id.my_doctor_viewpager);
        viewpager.setOffscreenPageLimit(2);
        radiogroup=(RadioGroup) findViewById(R.id.my_doctor_radiogroup);
        radiogroup.setOnCheckedChangeListener(this);
        famousInstitute = (TextView) findViewById(R.id.famous_institute);
        famousInstitute.setOnClickListener(this);
        edtSearch = (EditText) findViewById(R.id.edit_search);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().trim().length()>0){
                    tvSearch.setVisibility(View.VISIBLE);
                }else {
                    tvSearch.setVisibility(View.GONE);
                }
            }
        });
        tvSearch = (TextView) findViewById(R.id.tvSearch);
        tvSearch.setOnClickListener(this);
    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        mlList = new ArrayList<Fragment>();
//        mlList.add(MyDoctorFragment.newInstance(0));
        mlList.add(new HospitalFindTeamFragment());
        mlList.add(new DepartmentFindTeamFragment());
//        mlList.add(MyDoctorFragment.newInstance(1));

        mPagerAdapter = new BaseListPagerAdpater(getSupportFragmentManager());
        mPagerAdapter.onBoundFragment(mlList);
        viewpager.setAdapter(mPagerAdapter);
        viewpager.setCurrentItem(0, false);


        viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                radiogroup.check(radiogroup.getChildAt(arg0).getId());
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     * 切换radioButton时的事件,是ViewPager也随之切换
     */
    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for(int i=0;i<radiogroup.getChildCount();i++){
            if(radiogroup.getChildAt(i).getId()==checkedId){
                if(viewpager.getCurrentItem()!=i){
                    viewpager.setCurrentItem(i,false);
                }
                break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.famous_institute:
                startActivity(new Intent(this,FamousHospitalActivity.class));
                break;
            case R.id.tvSearch:
                if (viewpager.getCurrentItem()!=0) {
                    viewpager.setCurrentItem(0);
                }
                if (CommonUtils.isShowSoftInput(this)){
                    CommonUtils.hideSoftInput(this,tvSearch);
                }
                edtSearchChanageListener.edtSearchListener(edtSearch.getText().toString().trim());
                break;
        }
    }

    public void setEdtSearchChanageListener(EdtSearchChanageListener edtSearchChanageListener) {
        this.edtSearchChanageListener = edtSearchChanageListener;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        Log.i("uio", "beforeTextChanged: ");
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        Log.i("uio", "onTextChanged: ");
    }

    @Override
    public void afterTextChanged(Editable s) {
        Log.i("uio", "afterTextChanged: ");

    }

    interface EdtSearchChanageListener{
        void edtSearchListener(String searchContent);
    }

}
