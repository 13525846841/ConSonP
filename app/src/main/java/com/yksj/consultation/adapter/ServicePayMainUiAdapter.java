package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.yksj.consultation.son.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2017/5/23.
 */
public class ServicePayMainUiAdapter extends SimpleBaseAdapter<JSONObject> {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Context context;
    private List<JSONObject> list = new ArrayList<JSONObject>();
    public int  type;
    public ServicePayMainUiAdapter(Context context) {
        super(context);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public int getItemResource() {
        return R.layout.item_pay_main_ui;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        TextView textView = holder.getView(R.id.pay_name);

        type = Integer.parseInt(datas.get(position).optString("type"));


        if (1==type){
            textView.setText("余额支付");
        }else if (2==type){
            textView.setText("微信支付");
        }else if (3==type){
            textView.setText("支付宝支付");
        }else if (4==type){
            textView.setText("银联支付");
        }

        return convertView;
    }
    public int PayType(int position){
        return Integer.parseInt(datas.get(position).optString("type"));
    }
}
