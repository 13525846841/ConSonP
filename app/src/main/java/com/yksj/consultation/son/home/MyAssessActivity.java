package com.yksj.consultation.son.home;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yksj.consultation.adapter.BaseListPagerAdpater;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;

import java.util.ArrayList;

/**
 * 医生工作室评价界面
 */
public class MyAssessActivity extends BaseFragmentActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener, View.OnClickListener {
    private ViewPager mPager;
    private RadioGroup mGroup;
    public static final String ID = "id";
    public static final String SITE = "site";
    private String id = "";
    private String site_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_assess);
        initView();
    }

    private void initView() {
        initTitle();
        if (getIntent().hasExtra(ID))
            id = getIntent().getStringExtra(ID);
        if (getIntent().hasExtra(SITE))
            site_id = getIntent().getStringExtra(SITE);
        titleTextV.setText("评价");
        titleLeftBtn.setOnClickListener(this);
        mGroup = (RadioGroup) findViewById(R.id.radio_group1);
        mGroup.setOnCheckedChangeListener(this);
        mPager = (ViewPager) findViewById(R.id.viewpager1);
        BaseListPagerAdpater mAdpater = new BaseListPagerAdpater(getSupportFragmentManager());
        mPager.setAdapter(mAdpater);
        mPager.setOnPageChangeListener(this);
        ArrayList<Fragment> mlList = new ArrayList<>();

//        //0-未回复
//        Fragment fragment1 = new MyAssessListFragment();
//        mlList.add(fragment1);
//        mAdpater.onBoundFragment(mlList);
        //1-已回复
        Fragment fragment2 = new MyAssessListFragmented();
        Bundle bundle = new Bundle();
        bundle.putString(MyAssessListFragmented.ID, id);
        bundle.putString(MyAssessListFragmented.SITE, site_id);
        fragment2.setArguments(bundle);

        mlList.add(fragment2);
        mAdpater.onBoundFragment(mlList);
        mPager.setCurrentItem(0, false);
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        for (int i = 0; i < group.getChildCount(); i++) {
            RadioButton childAt = (RadioButton) group.getChildAt(i);
            if (childAt.isChecked())
                mPager.setCurrentItem(i, false);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        RadioButton mButton = (RadioButton) mGroup.getChildAt(position);
        mButton.setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
        }
    }
}
