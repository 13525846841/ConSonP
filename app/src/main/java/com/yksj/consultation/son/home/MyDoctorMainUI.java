package com.yksj.consultation.son.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioGroup;

import com.yksj.consultation.adapter.BaseListPagerAdpater;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;

import java.util.ArrayList;

/**
 * 我的医生
 * Created by lmk on 15/9/16.
 */
public class MyDoctorMainUI extends BaseFragmentActivity implements RadioGroup.OnCheckedChangeListener,
        View.OnClickListener{

    private ViewPager viewpager;
    private RadioGroup radiogroup;
    private BaseListPagerAdpater mPagerAdapter;
    private ArrayList<Fragment> mlList;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.my_doctor_main_ui);
        initView();
        initViewPager();
    }

    private void initView() {

        initTitle();
        titleTextV.setText(R.string.my_doctor);
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

    }

    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        mlList = new ArrayList<Fragment>();
        mlList.add(MyDoctorFragment.newInstance(0));
        mlList.add(MyDoctorFragment.newInstance(1));
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
    }

}
