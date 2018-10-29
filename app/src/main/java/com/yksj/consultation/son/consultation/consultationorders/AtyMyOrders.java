package com.yksj.consultation.son.consultation.consultationorders;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yksj.consultation.adapter.BaseListPagerAdpater;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.main.AtyConsultMain;
import com.yksj.consultation.son.home.PatientHomeActivity;

import java.util.ArrayList;

/**
 * Created by HEKL on 2015/9/15.
 * Used for 会诊订单_
 */
public class AtyMyOrders extends BaseFragmentActivity implements View.OnClickListener, ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener {
    private ViewPager mViewPager;//滑页
    private RadioGroup mRadioGroup;//页签

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_myorders);
        initView();

    }

    private void initView() {
        initTitle();
        titleTextV.setText("在线会诊");
        mRadioGroup = (RadioGroup) findViewById(R.id.radio_group);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        BaseListPagerAdpater mAdpater = new BaseListPagerAdpater(getSupportFragmentManager());
        mViewPager.setAdapter(mAdpater);
        mViewPager.setOffscreenPageLimit(1);
        ArrayList<Fragment> mList = new ArrayList<Fragment>();
        titleLeftBtn.setOnClickListener(this);
        mViewPager.setOnPageChangeListener(this);
        mRadioGroup.setOnCheckedChangeListener(this);

        // 待处理
        Fragment waitFragment = new FgtMyOrders();
        Bundle handle = new Bundle();
        handle.putInt("typeList", 0);
        waitFragment.setArguments(handle);
        mList.add(waitFragment);
        // 已完成
        Fragment doneFragment = new FgtMyOrders();
        Bundle done = new Bundle();
        done.putInt("typeList", 1);
        doneFragment.setArguments(done);
        mList.add(doneFragment);
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
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        RadioButton mButton = (RadioButton) mRadioGroup.getChildAt(position);
        mButton.setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton childAt = (RadioButton) group.getChildAt(i);
            if (childAt.isChecked()) {
                mViewPager.setCurrentItem(i, false);
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("BACK")) {
            int backMain = getIntent().getIntExtra("BACK", 0);
            if (backMain == 2) {
                Intent intent = new Intent(AtyMyOrders.this, PatientHomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            } else {
                super.onBackPressed();
            }
        } else {
            super.onBackPressed();
        }
    }
}
