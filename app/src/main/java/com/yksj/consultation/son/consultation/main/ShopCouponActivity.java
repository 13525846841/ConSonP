package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.ShopCouponAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.Actor;
import com.yksj.consultation.son.views.PagerSlidingTabStrip;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ToastUtil;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShopCouponActivity extends BaseFragmentActivity implements View.OnClickListener {

    private List<String> pageTitle;
    private PagerSlidingTabStrip ts_coupon_tab;
    private ViewPager vp_coupon;
    private List<View> viewData;
    private List<JSONObject> mList = new ArrayList<>();
    public static final int USECOUPON = 1;
    public static final int UNUSECOUPON = 2;
    private String goodId = "";
    private String totalprice = "";//总价
    private String couponId = "";//ID
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_coupon);
        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("优惠券");
        goodId = getIntent().getStringExtra("GOODID");
        totalprice = getIntent().getStringExtra("TOTALPRICE");
        couponId = getIntent().getStringExtra("COUPONID");

        ts_coupon_tab = (PagerSlidingTabStrip) findViewById(R.id.ts_coupon_tab);
        vp_coupon = (ViewPager) findViewById(R.id.vp_coupon);
        pageTitle = new ArrayList<String>();
        viewData = new ArrayList<View>();
        pageTitle.add("有效劵");
        pageTitle.add("无效劵");
        viewData.add(new ShopCoupon(ShopCouponActivity.this,USECOUPON).rootView);
        viewData.add(new ShopCoupon(ShopCouponActivity.this,UNUSECOUPON).rootView);
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

    class ShopCoupon implements PullToRefreshBase.OnRefreshListener2<ListView>, AdapterView.OnItemClickListener {

        public View rootView;
        public ShopCouponActivity shopCouponActivity;
        public TextView textview;
        private ListView mLv;
        private ShopCouponAdapter adapter;
        private List<Actor> actors = new ArrayList<>();
        private List<JSONObject> mList = new ArrayList<>();
        private int type;
        public String status;
        private View mEmptyView;
        private String customerId = "";
        private int conPageSize = 1;//当前的页数
        private PullToRefreshListView mPullRefreshListView;

        public ShopCoupon(ShopCouponActivity shopCouponActivity, int type) {
            this.shopCouponActivity = shopCouponActivity;
            this.type = type;
            rootView = initView();
            initData();
        }

        private void initData() {
            customerId = LoginServiceManeger.instance().getLoginEntity().getId();
            Map<String,String> map=new HashMap<>();
            map.put("CUSTOMERID",customerId);
            map.put("GOODS_ID",goodId);
            map.put("TOTAL_FREE",totalprice);//总价
            if (type==1){
                map.put("USER_FLAG", "1");
            }else if (type==2){
                map.put("USER_FLAG", "2");
            }
            map.put("PAGENUM",conPageSize + "" );
            map.put("PAGECOUNT", "10");

            HttpRestClient.OKHttGoodsCouponServlet(map, new HResultCallback<String>() {
                @Override
                public void onError(Request request, Exception e) {
                }
                @Override
                public void onResponse(String content) {
                    try {
                        JSONObject obj = new JSONObject(content);
                        mList = new ArrayList<>();
                        if ("1".equals(obj.optString("code"))){
                            if (!HStringUtil.isEmpty(obj.optString("result"))){
                                JSONObject object = obj.optJSONObject("result");
                                JSONArray array = object.optJSONArray("result");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsonobject = array.getJSONObject(i);
                                    mList.add(jsonobject);
                                }

                                if (conPageSize == 1) {//第一次加载
                                    if (mList.size() == 0) {
                                        adapter.removeAll();
                                        adapter.addAll(mList);
                                        mEmptyView.setVisibility(View.VISIBLE);
                                        mPullRefreshListView.setVisibility(View.GONE);
                                    } else {
                                        mEmptyView.setVisibility(View.GONE);
                                        mPullRefreshListView.setVisibility(View.VISIBLE);
                                        adapter.removeAll();
                                        adapter.addAll(mList);
                                    }
                                } else {
                                    if (mList.size() != 0) {//加载出了数据
                                        adapter.addAll(mList);
                                    } else {
                                        ToastUtil.showShort("没有更多了");
                                    }
                                }
                                conPageSize++;
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onBefore(Request request) {
                    super.onBefore(request);
                    mPullRefreshListView.setRefreshing();
                }

                @Override
                public void onAfter() {
                    mPullRefreshListView.onRefreshComplete();
                    super.onAfter();
                }
            },this);
        }

        private View initView() {
            View view = View.inflate(shopCouponActivity, R.layout.shop_order, null);
            mPullRefreshListView = (PullToRefreshListView) view.findViewById(R.id.shop_order_list);
            mLv = mPullRefreshListView.getRefreshableView();
            mPullRefreshListView.setOnRefreshListener(this);
            mEmptyView = view.findViewById(R.id.empty_view_famous);
            adapter = new ShopCouponAdapter(shopCouponActivity,type,couponId);
            mLv.setAdapter(adapter);
            mLv.setOnItemClickListener(this);
            return view;
        }

        @Override
        public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
            conPageSize = 1;
            initData();
        }

        @Override
        public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
            initData();
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (type==1){
                Intent data = new Intent();
                data.putExtra("price",  adapter.datas.get(position-1).optString("COUPONS_VALUE"));
                data.putExtra("id", adapter.datas.get(position-1).optString("COUPONS_ID"));
                data.putExtra("COUPONS_TYPE", adapter.datas.get(position-1).optString("COUPONS_TYPE"));
                shopCouponActivity.setResult(RESULT_OK, data);
                shopCouponActivity.finish();
            }else {
                ToastUtil.showShort("此劵已过期");
            }
        }
    }
}
