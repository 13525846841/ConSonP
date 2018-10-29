package com.yksj.consultation.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.consultationorders.AtyOrderDetails;
import com.yksj.consultation.son.consultation.consultationorders.AtyOrdersDetails;
import com.yksj.healthtalk.bean.ListDetails;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.utils.TimeUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by HEKl on 2015/9/18.
 * Used for
 */
public class AdtConsultationOrders extends SimpleBaseAdapter<ListDetails> {
    private FragmentActivity maActivity;
    private int Type;

    public AdtConsultationOrders(Context context, int type) {
        super(context);
        this.context = context;
        this.Type = type;
        maActivity = (FragmentActivity) context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public int getItemResource() {
        return R.layout.myorder_item_layout;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {
        final int state = datas.get(position).getCONSULTATION_STATUS();
        final int conId = datas.get(position).getCONSULTATION_ID();
        String strTime = TimeUtil.format(datas.get(position).getCREATE_TIME());//会诊单时间
        final TextView textContent = holder.getView(R.id.tv_content);//会诊简介
        final String str = datas.get(position).getSERVICE_OPERATION();//会诊操作
        final TextView textName = holder.getView(R.id.tv_ordername);//会诊内容
        final TextView textRText = holder.getView(R.id.right_text);//会诊时间显示
        final Button btnState = holder.getView(R.id.btn_status);//会诊状态显示
        final Button handle = holder.getView(R.id.btn_handle);//会诊操作
        final ImageView imageDot = holder.getView(R.id.image_dot);//会诊操作
        imageDot.setVisibility(View.INVISIBLE);
        textContent.setText(datas.get(position).getCONSULTATION_DESC());
        textName.setText(datas.get(position).getCONSULTATION_NAME());
        btnState.setText(datas.get(position).getSERVICE_STATUS_NAME());
        textRText.setText(strTime);
        if (1==datas.get(position).getNEW_CHANGE_PATIENT()){
            imageDot.setVisibility(View.VISIBLE);
        }
        //会诊列表区分
        if (Type == 1) {
            btnState.setBackgroundResource(R.drawable.leftstate_gray);
        }
        handle.setVisibility(View.GONE);
        //会诊按钮判断
        if (!"".equals(str)) {
            handle.setVisibility(View.VISIBLE);
            handle.setText(datas.get(position).getSERVICE_OPERATION());
        }
        //会展操作按钮监听
        handle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendRead(conId, state);
            }
        });
        //会诊列表点击
        convertView.findViewById(R.id.rl_entry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendRead(conId, state);
            }
        });
        return convertView;
    }

    /**
     * 初始订单详情
     */
    private void putAcitvity(int conId) {
        Intent intent = new Intent(maActivity, AtyOrdersDetails.class);
        intent.putExtra("CONID", conId);
        maActivity.startActivity(intent);
    }

    /**
     * 订单详情
     */
    private void putAcitvitySec(int conId) {
        Intent intent = new Intent(maActivity, AtyOrderDetails.class);
        intent.putExtra("CONID", conId);
        maActivity.startActivity(intent);
    }

    private void sendRead(final int consultId, final int status) {
        HttpRestClient.OKHttpSendRead(consultId + "", new HResultCallback<String>(maActivity) {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject object = new JSONObject(response);
                        if ("1".equals(object.optString("code"))) {
                            switch (status) {
                                case 10://待服务
                                case 15:
                                    putAcitvity(consultId);
                                    break;
                                case 20://填病历
                                case 30://修改病历
                                case 50://待接诊没有病历补充
                                case 55:
                                case 60://重选专家
                                case 70://去支付
                                case 80://等待给意见
                                case 85:
                                case 88:
                                case 90:
                                case 95://已取消
                                case 99://已完成
                                case 222://待退款，未填账号
                                case 232://待退款，已填账号
                                case 242://退款完成
                                case 252://退款失败
                                    putAcitvitySec(consultId);
                                    break;
                            }
                        } else {
                            ToastUtil.showShort(object.optString("message"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }

            @Override
            public void onError(Request request, Exception e) {
                super.onError(request, e);
            }
        }, this);
    }
}
