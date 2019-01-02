package com.yksj.consultation.son.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.fragment.InstitutionInfoEvaluationFragment;
import com.yksj.consultation.son.fragment.InstitutionInfoActFragment;
import com.yksj.consultation.son.fragment.InstitutionInfoIntroductionFragment;

import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

public class InstitutionInfoMainActivity extends FragmentActivity implements View.OnClickListener {

    public static final String Unit_Code="Unit_Code";
    public static final String SELF="self";
    private ImageView insInfoImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution_info_main);
        EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        int unit_code = getIntent().getIntExtra(Unit_Code, -1);
        int self = getIntent().getIntExtra(SELF, -1);
        findViewById(R.id.title_back).setOnClickListener(this);
        TextView title= (TextView) findViewById(R.id.title_lable);
        title.setText("机构详情");
        insInfoImg = (ImageView) findViewById(R.id.insInfoImg);
        TabLayout insInfoTabLayout= (TabLayout) findViewById(R.id.insInfoTabLayout);
        ViewPager insInfoPager= (ViewPager) findViewById(R.id.insInfoPager);
        insInfoPager.setOffscreenPageLimit(3);
         final List<Fragment> fragmentList=new ArrayList<>();
         fragmentList.add(InstitutionInfoIntroductionFragment.newInstance(unit_code));
         fragmentList.add(InstitutionInfoActFragment.newInstance(unit_code,self));
         fragmentList.add(InstitutionInfoEvaluationFragment.newInstance(unit_code));
        insInfoPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }

        });
        insInfoTabLayout.setupWithViewPager(insInfoPager);
        insInfoTabLayout.getTabAt(0).setText("简介");
        insInfoTabLayout.getTabAt(1).setText("活动");
        insInfoTabLayout.getTabAt(2).setText("评价");
    }
    public void onEvent(MyEvent event) {
        if (event.code== InstitutionInfoIntroductionFragment.EVENT_MSG) {
            Glide.with(this).load(ImageLoader.getInstance().getDownPathUri(event.what)).error(R.drawable.waterfall_default)
                    .placeholder(R.drawable.waterfall_default).dontAnimate().into(insInfoImg);
            Log.i("lll", "onEvent: "+ImageLoader.getInstance().getDownPathUri(event.what));
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:finish();break;
        }
    }
}
