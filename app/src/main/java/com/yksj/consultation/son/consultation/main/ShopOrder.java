package com.yksj.consultation.son.consultation.main;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.ShopOrderAdapter;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.Actor;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
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

/**
 * Created by ${chen} on 2017/8/16.
 */
public class ShopOrder implements PullToRefreshBase.OnRefreshListener2<ListView>{

    public View rootView;
    public ShopOrderActivity shopOrderActivity;
    public TextView textview;
    private ListView mLv;
    private ShopOrderAdapter adapter;
    private List<Actor> actors = new ArrayList<>();
    private List<JSONObject> mList = new ArrayList<>();
    private int type;
    public String status;
    private View mEmptyView;
    private String customerId = "";
    private int conPageSize = 1;//当前的页数
    private PullToRefreshListView mPullRefreshListView;

    public ShopOrder(ShopOrderActivity shopOrderActivity,int type) {
        this.shopOrderActivity = shopOrderActivity;
        this.type = type;
        rootView = initView();
        initData();
    }
    private View initView() {
        View view =View.inflate(shopOrderActivity, R.layout.shop_order, null);
        mPullRefreshListView = (PullToRefreshListView)view.findViewById(R.id.shop_order_list);
        mLv = mPullRefreshListView.getRefreshableView();
        mPullRefreshListView.setOnRefreshListener(this);
        mEmptyView = view.findViewById(R.id.empty_view_famous);
        adapter = new ShopOrderAdapter(shopOrderActivity);
        mLv.setAdapter(adapter);
        return view;
    }
    private void initData() {

        customerId = LoginServiceManeger.instance().getLoginEntity().getId();
        Map<String,String> map=new HashMap<>();
        map.put("Type", "findOrders");
        map.put("customer_id",customerId);
        if (type==2){
            status="10";
            map.put("order_status", "10");
        }else if (type==3){
            status = "20";
            map.put("order_status", "20");
        }else if (type ==4){
            status = "30";
            map.put("order_status", "30");
        }
        map.put("pageNum",conPageSize + "" );
        map.put("pageSize", "10");

        HttpRestClient.OKHttGoodsServlet(map, new HResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    mList = new ArrayList<>();
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        if (!HStringUtil.isEmpty(obj.optString("server_params"))){
                            JSONObject object = obj.optJSONObject("server_params");
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


    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        conPageSize = 1;
        initData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        initData();
    }
}
