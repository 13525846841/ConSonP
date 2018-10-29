package com.yksj.consultation.son.consultation.main;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.views.PagerSlidingTabStrip;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 商城订单列表
 */
public class ShopOrderActivity extends BaseFragmentActivity implements View.OnClickListener {

    private List<String> pageTitle;
    private PagerSlidingTabStrip ts_coupon_tab;
    private ViewPager vp_coupon;
    private List<View> viewData;
    private List<JSONObject> mList = new ArrayList<>();
    public static final int ALL = 1;
    public static final int WAITTINGPAY = 2;
    public static final int WAITTINGFA = 3;
    public static final int WATTINGSHOU = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_order);
        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("我的订单");

        ts_coupon_tab = (PagerSlidingTabStrip) findViewById(R.id.ts_coupon_tab);
        vp_coupon = (ViewPager) findViewById(R.id.vp_coupon);
        pageTitle = new ArrayList<String>();
        viewData = new ArrayList<View>();
        pageTitle.add("全部");
        pageTitle.add("待支付");
        pageTitle.add("代发货");
        pageTitle.add("待收货");
        viewData.add(new ShopOrder(ShopOrderActivity.this,ALL).rootView);
        viewData.add(new ShopOrder(ShopOrderActivity.this,WAITTINGPAY).rootView);
        viewData.add(new ShopOrder(ShopOrderActivity.this,WAITTINGFA).rootView);
        viewData.add(new ShopOrder(ShopOrderActivity.this,WATTINGSHOU).rootView);
        MyCouponPagerAdapter adapter = new MyCouponPagerAdapter();
        vp_coupon.setAdapter(adapter);
        ts_coupon_tab.setViewPager(vp_coupon);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
        }
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
}
