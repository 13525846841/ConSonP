package com.yksj.consultation.son.consultation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.PCouponsAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.bean.CouponBean;
import com.yksj.healthtalk.bean.CouponListData;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * 患者优惠券Activity
 * Created by lmk on 15/9/16.
 */
public class PConsultCouponActivity extends BaseFragmentActivity implements View.OnClickListener {

    private ListView couponList;
    private PullToRefreshListView mPullListView;
    private PCouponsAdapter mAdapter;
    private String consultId="";
    private int pageSize=1;
    private View empty;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_pconsult_coupon);
        if (getIntent().hasExtra("consultId")){
            consultId=getIntent().getStringExtra("consultId");
        }

        initView();
        initData();
    }
    private String customer_id = LoginServiceManeger.instance().getLoginUserId();
    public CouponListData cld;
    private void initData() {
        List<BasicNameValuePair> valuePairs=new ArrayList<>();
        //valuePairs.add(new BasicNameValuePair("CUSTOMERID", LoginServiceManeger.instance().getLoginEntity().getId()));
        valuePairs.add(new BasicNameValuePair("CUSTOMERID", customer_id));//47324
      //  valuePairs.add(new BasicNameValuePair("CONSULTATIONID", consultId));//consultId
        valuePairs.add(new BasicNameValuePair("PAGENUM", pageSize+""));
        valuePairs.add(new BasicNameValuePair("PAGECOUNT", "20"));
        HttpRestClient.doGetConsultationCouponsList(valuePairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                cld = JSONObject.parseObject(response, CouponListData.class);
                if ("1".equals(cld.code)){
                    if (cld.result!=null&&cld.result.size()>0){
                        mAdapter.addAll(cld.result);
                    }
                    empty.setVisibility(View.GONE);
                    mPullListView.setVisibility(View.VISIBLE);
                }else

                    ToastUtil.showShort(PConsultCouponActivity.this,cld.message);
                    empty.setVisibility(View.VISIBLE);
                    mPullListView.setVisibility(View.GONE);

            }
        }, this);

    }


    private CouponBean cb;
    private void initView() {
        initTitle();
        mPullListView= (PullToRefreshListView) findViewById(R.id.pcousult_coupon_list);
        couponList=mPullListView.getRefreshableView();
        empty = findViewById(R.id.empty_view_famous);

        titleTextV.setText(R.string.discount_coupon);
        titleRightBtn.setVisibility(View.VISIBLE);
        titleRightBtn.setText("确定");
        titleRightBtn.setOnClickListener(this);
        titleLeftBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
//        TextView tvTip= new TextView(PConsultCouponActivity.this);
//        tvTip.setPadding(16, 20, 16, 16);
//        tvTip.setTextSize(16);
//        tvTip.setLineSpacing(1, 1.2f);
       // tvTip.setText("使用规则:\n1.一次会诊中只能使用一张优惠券\n2.优惠券不找零,不兑现\n3.优惠券在有效期内使用\n");
     //   couponList.addFooterView(tvTip);
        mAdapter=new PCouponsAdapter(PConsultCouponActivity.this);
        couponList.setAdapter(mAdapter);

//        couponList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (position == 0 ||position>mAdapter.datas.size()) {
//                    return;
//                }
//                cb=mAdapter.datas.get(position-1);
//                if(cb.STATUS!=0){
//                    return;
//                }
//
//                Intent data=new Intent();
//                data.putExtra("price",cb.VALUE);
//                data.putExtra("id",cb.ID);
//                setResult(RESULT_OK, data);
//                finish();
//            }
//        });

    }

    @Override
    public void onClick(View v) {
        if (cld.result.size()>0){
            if (mAdapter.id()==0){
                ToastUtil.showShort("请选择优惠劵");
            }else {
                Intent data=new Intent();
                data.putExtra("price",mAdapter.price());
                data.putExtra("id",mAdapter.id());
                setResult(RESULT_OK, data);
                finish();
            }
        }else {
            finish();
        }

    }
}
