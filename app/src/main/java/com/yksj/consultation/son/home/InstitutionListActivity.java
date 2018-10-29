package com.yksj.consultation.son.home;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.fragment.InstitutionInfoActFragment;
import com.yksj.consultation.son.fragment.InstitutionListFragment;
import com.yksj.consultation.son.pay.PayResult;
import com.yksj.healthtalk.utils.ToastUtil;

import java.util.ArrayList;
import java.util.List;

public class InstitutionListActivity extends FragmentActivity implements View.OnClickListener {
    public static final String insF = "insF";
    public static final String SORT = "SORT";
    public static final String ADDRESS = "address";
    public static final String SELF = "self";
    private int sort=-1;//-1 我的机构  0体检中心 1拓展中心 2康复中心 3 兴趣中心
    private String addressCode="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution_list);
        initView();
    }

    private void initView() {
        int sort = getIntent().getIntExtra(SORT, -1);
        addressCode = getIntent().getStringExtra("addressCode");
        TabLayout insHomeTabLayout= (TabLayout) findViewById(R.id.insListTabLayout);
        ViewPager insHomePager= (ViewPager) findViewById(R.id.insListPager);
        findViewById(R.id.title_back).setOnClickListener(this);
        TextView title= (TextView) findViewById(R.id.title_lable);
        if (sort==0)title.setText("体检中心");else if (sort==1)title.setText("拓展中心");else if (sort==2)title.setText("康复中心");else if (sort==3)title.setText("兴趣中心");else title.setText("我的机构");
        final List<Fragment> fragmentList=new ArrayList<>();
        if (sort!=-1){
            for (int i = 0; i < 3; i++) {
                InstitutionListFragment fragment = new InstitutionListFragment();
                Bundle bundle = new Bundle();
                bundle.putInt(insF,i);
                bundle.putString(ADDRESS,addressCode);
                bundle.putInt(SORT,sort);
                fragment.setArguments(bundle);
                fragmentList.add(fragment);
            }
        }else {
            insHomeTabLayout.setVisibility(View.GONE);
            InstitutionListFragment fragment = new InstitutionListFragment();
            Bundle bundle = new Bundle();
            bundle.putInt(insF,-1);
            bundle.putInt(SORT,sort);
            bundle.putInt(SELF, InstitutionInfoActFragment.IS_SELF);
            fragment.setArguments(bundle);
            fragmentList.add(fragment);
        }
        insHomePager.setOffscreenPageLimit(3);
        insHomePager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragmentList.get(position);
            }

            @Override
            public int getCount() {
                return fragmentList.size();
            }
        });
        if (sort!=-1){
            insHomeTabLayout.setupWithViewPager(insHomePager);
            insHomeTabLayout.getTabAt(0).setText("热门");
            insHomeTabLayout.getTabAt(1).setText("新入");
            insHomeTabLayout.getTabAt(2).setText("附近");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back:finish();break;
        }
    }
}
