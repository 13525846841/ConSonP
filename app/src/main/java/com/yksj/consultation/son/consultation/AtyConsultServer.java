package com.yksj.consultation.son.consultation;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的会诊
 *
 * Created by zheng on 2015/7/23.
 */
public class AtyConsultServer extends BaseFragmentActivity implements View.OnClickListener {
    private ViewPager mViewPager;
    private PConsultServiceAdapter csa;
    private List<Fragment> fragList;
    private RadioGroup mTopRadioGroup;
//    private AdtConsultServerList frag1, frag2, frag3;
    private ConsultationServiceListFragment frag1, frag2;
    private Bundle bun1, bun2;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_consult_server);
        initView();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("我的会诊");
        titleLeftBtn.setOnClickListener(this);
        mViewPager = (ViewPager) findViewById(R.id.consultation_service_vp);
        mViewPager.setOffscreenPageLimit(0);
        mTopRadioGroup = (RadioGroup) findViewById(R.id.consultation_service_rg1);
        initFrag();
    }

    private void initFrag() {
        fragList = new ArrayList<Fragment>();
        fragList.clear();
//        frag1 = new AdtConsultServerList();
//        bun1 = new Bundle();
//        bun1.putInt("TYPENUM", 1);
//        frag1.setArguments(bun1);
//        fragList.add(frag1);
//
//        frag2 = new AdtConsultServerList();
//        bun2 = new Bundle();
//        bun2.putInt("TYPENUM", 7);
//        frag2.setArguments(bun2);
//        fragList.add(frag2);
//
//        frag3 = new AdtConsultServerList();
//        bun3 = new Bundle();
//        bun3.putInt("TYPENUM", 8);
//        frag3.setArguments(bun3);
//        fragList.add(frag3);

        frag1 = new ConsultationServiceListFragment();
        bun1 = new Bundle();
        bun1.putInt("TYPENUM", 0);
        frag1.setArguments(bun1);
        fragList.add(frag1);

        frag2 = new ConsultationServiceListFragment();
        bun2 = new Bundle();
        bun2.putInt("TYPENUM", 1);
        frag2.setArguments(bun2);
        fragList.add(frag2);

//        frag3 = new ConsultationServiceListFragment();
//        bun3 = new Bundle();
//        bun3.putInt("TYPENUM", 8);
//        frag3.setArguments(bun3);
//        fragList.add(frag3);

        csa = new PConsultServiceAdapter(getSupportFragmentManager(), fragList);
        mViewPager.setAdapter(csa);
        initFunction();
    }

    private void initFunction() {
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int arg0) {
                switch (arg0) {
                    case 0:
                        ((RadioButton) mTopRadioGroup.getChildAt(0)).setChecked(true);
                        break;
                    case 1:
                        ((RadioButton) mTopRadioGroup.getChildAt(1)).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageScrollStateChanged(int arg0) {

            }
        });
        mTopRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                for (int i = 0; i < mTopRadioGroup.getChildCount(); i++) {
                    if (mTopRadioGroup.getChildAt(i).getId() == checkedId) {
                        if (mViewPager.getCurrentItem() != i) {
                            mViewPager.setCurrentItem(i, false);
                        }
                        break;
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
//        if(getIntent().hasExtra("CONFRAG")) {
//            int backMain = getIntent().getIntExtra("CONFRAG",0);
//            if (backMain==2) {
//                Intent intent = new Intent(AtyConsultServer.this, MainTabActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intent);
//            }else {
//                super.onBackPressed();
//            }
//        }else {
//            super.onBackPressed();
//        }

    }
}
