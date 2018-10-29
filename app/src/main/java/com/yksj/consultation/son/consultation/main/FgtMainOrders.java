package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.consultation.consultationorders.AtyMyOrders;
import com.yksj.consultation.son.login.UserLoginActivity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by HEKL on 2015/9/18.
 * Used for 我的订单_
 */
public class FgtMainOrders extends RootFragment implements View.OnClickListener {
    private TextView mOrderName;//订单名称
    private TextView mNoOrders;//没有订单
    private TextView mOrderState;//订单状态
    private ImageView imageDot;//消息红点
    SpannableStringBuilder ss;
    JSONObject object;
    JSONObject obj;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fgt_mainorders, null, false);
        view.setOnClickListener(this);
        EventBus.getDefault().register(FgtMainOrders.this);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (LoginServiceManeger.instance().isVisitor) {//游客
            sendTip("未登录, 无法查看订单");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(FgtMainOrders.this);
    }

    private void initView(View view) {
        mOrderName = (TextView) view.findViewById(R.id.order_message);
        mOrderState = (TextView) view.findViewById(R.id.order_state);
        mNoOrders = (TextView) view.findViewById(R.id.no_order);
        imageDot = (ImageView) view.findViewById(R.id.dot);
        ss = new SpannableStringBuilder();


//        mOrderState.setVisibility(View.GONE);
//        mOrderName.setVisibility(View.GONE);
//        mNoOrders.setVisibility(View.VISIBLE);
//        mNoOrders.setText("我的订单");

        if (!LoginServiceManeger.instance().isVisitor) {
            if (!FgtMainOrders.this.isDetached()){
                showOrder();
            }
        } else {
            sendTip("未登录, 无法查看订单");
        }
    }

    @Override
    public void onClick(View v) {
        if (!LoginServiceManeger.instance().isVisitor) {
            startActivity(new Intent(getActivity(), AtyMyOrders.class));
//            startActivity(new Intent(getActivity(), OrderActivity.class));

        } else {
            Intent intent = new Intent(getActivity(), UserLoginActivity.class);
            startActivity(intent);
        }
    }

    /**
     * 首页订单显示
     */
    private void showOrder() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("TYPE", "homePageInfoPatient"));
        pairs.add(new BasicNameValuePair("CUSTOMERID", LoginServiceManeger.instance().getLoginUserId()));
        HttpRestClient.OKHttpOrderTip(pairs, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                sendTip("加载中, 请稍后...");
            }

            @SuppressWarnings("EqualsBetweenInconvertibleTypes")
            @Override
            public void onResponse(String response) {
                try {
                    object = new JSONObject(response);
                    obj = object.getJSONObject("result");
                    if (!TextUtils.isEmpty(response)) {
                        if ("1".equals(object.optString("code"))) {
                            mOrderName.setVisibility(View.VISIBLE);
                            mOrderState.setVisibility(View.VISIBLE);
                            mNoOrders.setVisibility(View.GONE);
                            ss.clear();
                            if ("null".equals(obj.getJSONObject("consultation").optString("SERVICE_STATUS_NAME"))) {
                                mOrderName.setVisibility(View.GONE);
                                mOrderState.setVisibility(View.GONE);
                                mNoOrders.setVisibility(View.VISIBLE);
                                mNoOrders.setText("您还没有会诊");
                                imageDot.setImageDrawable(getResources().getDrawable(R.drawable.gray_dot));
                            } else {
                                if (!"".equals(obj.getJSONObject("consultation").optString("SERVICE_STATUS_NAME"))) {
                                    String str = obj.getJSONObject("consultation").optString("SERVICE_STATUS_NAME");
                                    mOrderState.setText(ss.append("(" + str + ")"));
                                }
                                if (!"".equals(obj.getJSONObject("consultation").optString("CONSULTATION_NAME"))) {
                                    mOrderName.setText(obj.getJSONObject("consultation").optString("CONSULTATION_NAME"));
                                }
                                if ("1".equals(obj.getJSONObject("consultation").optString("NEW_CHANGE"))) {
                                    imageDot.setImageDrawable(getResources().getDrawable(R.drawable.red_dot));
                                } else {
                                    imageDot.setImageDrawable(getResources().getDrawable(R.drawable.gray_dot));
                                }
                            }
                        } else {
                            ToastUtil.showShort(object.optString("message"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }

    public void onEventMainThread(MyEvent event) {
        if ("mainrefresh".equals(event.what)||"mainrefreshOrder".equals(event.what)) {
            if (!FgtMainOrders.this.isDetached()){
                showOrder();
            }
        } else if (event.code == 11 && (!"".equals(event.what))) {
            ss.clear();
            try {
                object = new JSONObject(event.what);
                mOrderName.setText(object.optString("CONSULTATION_NAME"));
                mOrderState.setText(ss.append("(" + object.optString("SERVICE_STATUS_NAME") + ")"));
                if ("1".equals(object.optString("NEW_CHANGE"))) {
                    imageDot.setImageDrawable(getResources().getDrawable(R.drawable.red_dot));
                } else {
                    imageDot.setImageDrawable(getResources().getDrawable(R.drawable.gray_dot));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * 加载提示
     */
    private void sendTip(String str) {
        mOrderName.setVisibility(View.GONE);
        mOrderState.setVisibility(View.GONE);
        mNoOrders.setVisibility(View.VISIBLE);
        mNoOrders.setText(str);
        imageDot.setImageDrawable(getResources().getDrawable(R.drawable.gray_dot));
    }
}
