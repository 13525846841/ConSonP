package com.yksj.consultation.son.consultation.main;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.views.PagerSlidingTabStrip;

import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 优惠卷的界面
 */
public class CouponActivity extends BaseFragmentActivity implements View.OnClickListener {

    private PullToRefreshListView pullListView;
    private ListView mListView;

    private List<String> pageTitle;
    private PagerSlidingTabStrip ts_coupon_tab;
    private ViewPager vp_coupon;
    private List<View> viewData;
    private List<JSONObject> mList = new ArrayList<>();
    public static final int using = 1;
    public static final int used = 2;
    public static final int useout = 3;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon);
        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("优惠券");
        ts_coupon_tab = (PagerSlidingTabStrip) findViewById(R.id.ts_coupon_tab);
        vp_coupon = (ViewPager) findViewById(R.id.vp_coupon);
        pageTitle = new ArrayList<String>();
        viewData = new ArrayList<View>();
        pageTitle.add("未使用");
        pageTitle.add("已使用");
        pageTitle.add("已过期");
        viewData.add(new Coupon(CouponActivity.this,using).rootView);
        viewData.add(new Coupon(CouponActivity.this,used).rootView);
        viewData.add(new Coupon(CouponActivity.this,useout).rootView);
        MyCouponPagerAdapter adapter = new MyCouponPagerAdapter();
        vp_coupon.setAdapter(adapter);
        ts_coupon_tab.setViewPager(vp_coupon);

    }

    class MyCouponPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {

            return pageTitle.size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 ==arg1;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitle.get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View view = viewData.get(position);
            container.addView(view);
            return view;
        }

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
        }
    }
}
