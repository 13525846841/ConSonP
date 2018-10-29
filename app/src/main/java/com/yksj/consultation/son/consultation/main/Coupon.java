package com.yksj.consultation.son.consultation.main;

import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import com.yksj.consultation.adapter.CouponCardAdapter;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.Actor;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.ToastUtil;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2016/12/1.
 * 优惠卷
 */
public class Coupon{

    public View rootView;
    public CouponActivity couponActivity;
    public TextView textview;
  //private RecyclerView mRecyclerView;
    private ListView mLv;
    private CouponCardAdapter adapter;
    private List<Actor> actors = new ArrayList<>();
    private List<JSONObject> mList = new ArrayList<>();
    private int type;
    public String status;
    private View mEmptyView;

    public Coupon(CouponActivity couponActivity,int type) {
        this.couponActivity = couponActivity;
        this.type = type;
        rootView = initView();
        initData();
    }

    private View initView() {
        View view =View.inflate(couponActivity, R.layout.coupon, null);
        mLv = (ListView) view.findViewById(R.id.coupon_list);
        mEmptyView = view.findViewById(R.id.empty_view_famous);
        adapter = new CouponCardAdapter(couponActivity);
        mLv.setAdapter(adapter);
        return view;
    }
    private String customer_id = LoginServiceManeger.instance().getLoginUserId();

    private void initData() {
        if (type==1) {
             status = "1";
        }else if (type==2){
            status="2";
        }else if (type==3){
            status = "3";
        }
        HttpRestClient.OKHttpCoupon(customer_id,status,new AsyncHttpResponseHandler(couponActivity){//"124031"
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject obj = new JSONObject(content);
                    JSONObject item;
                    if (obj.has("coupons")) {
                        JSONArray array = obj.getJSONArray("coupons");
                        mList = new ArrayList<JSONObject>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonobject = array.getJSONObject(i);
                            mList.add(jsonobject);
                        }
                        adapter.onBoundData(mList);
                        if (mList.size()==0){
                            mEmptyView.setVisibility(View.VISIBLE);
                            mLv.setVisibility(View.GONE);
                        }else {
                            mEmptyView.setVisibility(View.GONE);
                            mLv.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                ToastUtil.showShort("添加失败");
            }
        });
    }

}
