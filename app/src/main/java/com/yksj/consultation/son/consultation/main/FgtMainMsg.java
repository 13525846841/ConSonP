package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.Spanned;
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
import com.yksj.consultation.son.login.UserLoginActivity;
import com.yksj.consultation.son.message.MessageNotifyActivity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * Created by HEKL on 2015/9/18.
 * Used for 首页消息__
 */
public class FgtMainMsg extends RootFragment implements View.OnClickListener {
    private TextView mMsgFrom;//消息来源
    private TextView mMsgContent;//消息内容
    private TextView mNoMsg;//没有消息
    private ImageView imageDot;//提示红点
    JSONObject object;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fgt_mainmsg, null, false);
        EventBus.getDefault().register(FgtMainMsg.this);
        view.setOnClickListener(this);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (LoginServiceManeger.instance().isVisitor) {//不是游客
            mMsgFrom.setVisibility(View.GONE);
            mMsgContent.setVisibility(View.GONE);
            mNoMsg.setVisibility(View.VISIBLE);
            mNoMsg.setText("未登录, 无法查看消息");
            imageDot.setImageDrawable(getResources().getDrawable(R.drawable.gray_dot));
        }
    }

    private void initView(View view) {
        mMsgFrom = (TextView) view.findViewById(R.id.msg_from);
        mMsgContent = (TextView) view.findViewById(R.id.msg_content);
        mNoMsg = (TextView) view.findViewById(R.id.no_msg);
        imageDot = (ImageView) view.findViewById(R.id.dot);
        if (!LoginServiceManeger.instance().isVisitor) {//不是游客
            if (!FgtMainMsg.this.isDetached()) {
                showOrder();
            }
        } else {
            mMsgFrom.setVisibility(View.GONE);
            mMsgContent.setVisibility(View.GONE);
            mNoMsg.setVisibility(View.VISIBLE);
            mNoMsg.setText("未登录, 无法查看消息");
            imageDot.setImageDrawable(getResources().getDrawable(R.drawable.gray_dot));
        }
    }


    @Override
    public void onClick(View v) {
        if (!LoginServiceManeger.instance().isVisitor) {
            startActivity(new Intent(getActivity(), MessageNotifyActivity.class));
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

            @SuppressWarnings({"EqualsBetweenInconvertibleTypes", "StatementWithEmptyBody"})
            @Override
            public void onResponse(String response) {
                try {
                    object = new JSONObject(response);
                    JSONObject obj = object.getJSONObject("result").getJSONObject("message").getJSONObject("message");
                    if (!TextUtils.isEmpty(response)) {
                        if ("1".equals(object.optString("code"))) {
                            mMsgFrom.setVisibility(View.VISIBLE);
                            mMsgContent.setVisibility(View.VISIBLE);
                            mMsgFrom.setText("");
                            mMsgContent.setText("");
                            mNoMsg.setVisibility(View.GONE);
                            if (!obj.has("MESSAGE_CONTENT")) {
                                mMsgFrom.setVisibility(View.GONE);
                                mMsgContent.setVisibility(View.GONE);
                                mNoMsg.setVisibility(View.VISIBLE);
                                mNoMsg.setText("您暂时没有未读消息");
                                imageDot.setImageDrawable(getResources().getDrawable(R.drawable.gray_dot));
                            } else {
                                if (obj.optInt("SEND_ID") == 100000) {
                                    mMsgFrom.setText("系统通知");
                                } else if (obj.optInt("SEND_ID") != 100000&&!HStringUtil.isEmpty(obj.optString("TARGET_NAME"))) {
                                    mMsgFrom.setText(obj.optString("TARGET_NAME"));
                                }else {
                                    mMsgFrom.setText("匿名");
                                }
                                if (!"null".equals(obj.optString("MESSAGE_CONTENT"))) {
                                    if (obj.optInt("MESSAGE_TYPE") == 1) {
                                        mMsgContent.setText("[语音]");
                                    } else if (obj.optInt("MESSAGE_TYPE") == 2) {
                                        mMsgContent.setText("[图片]");
                                    } else {
                                        mMsgContent.setText(obj.optString("MESSAGE_CONTENT"));
                                    }
                                }
                                int msgNum = Integer.valueOf(object.getJSONObject("result").getJSONObject("message").optString("nums"));
                                if (msgNum > 0) {
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(FgtMainMsg.this);
    }

    public void onEventMainThread(MyEvent event) {
        if ("mainrefresh".equals(event.what)) {
            if (!FgtMainMsg.this.isDetached()) {
                showOrder();
            }
        } else if (!"".equals(event.what) && (event.code == 10)) {
            object = null;
            try {
                object = new JSONObject(event.what);
                mMsgFrom.setVisibility(View.VISIBLE);
                mMsgContent.setVisibility(View.VISIBLE);
                mNoMsg.setVisibility(View.GONE);
                mMsgFrom.setText("");
                mMsgContent.setText("");
                if ("100000".equals(object.optString("customerId"))) {
                    mMsgFrom.setText("系统通知");
                } else {
                    String name = object.optString("customer_nickname");
                    if (!HStringUtil.isEmpty(name)) {
                        mMsgFrom.setText(name);
                    } else {
                        mMsgFrom.setText("匿名");
                    }
                }
                if (!"null".equals(object.optString("sms_req_content"))) {
                    if (object.optInt("type") == 1) {
                        mMsgContent.setText("[语音]");
                    } else {
                        String name = object.optString("sms_req_content");
                        if (!HStringUtil.isEmpty(name)) {
                            Spanned spanned = Html.fromHtml(object.optString("sms_req_content"));
                            mMsgContent.setText(spanned);
                        } else {
                            mMsgContent.setText("匿名");
                        }

                    }
                }
                imageDot.setImageDrawable(getResources().getDrawable(R.drawable.red_dot));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 加载提示
     */
    private void sendTip(String str) {
        mMsgFrom.setVisibility(View.GONE);
        mMsgContent.setVisibility(View.GONE);
        mNoMsg.setVisibility(View.VISIBLE);
        mNoMsg.setText(str);
        imageDot.setImageDrawable(getResources().getDrawable(R.drawable.gray_dot));
    }
}
