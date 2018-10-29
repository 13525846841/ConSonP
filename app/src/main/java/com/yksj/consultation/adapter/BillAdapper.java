package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yksj.consultation.son.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2016/12/15.
 */
public class BillAdapper extends BaseAdapter {

    private Context context;
    private List<JSONObject> list = new ArrayList<JSONObject>();
    private LayoutInflater mInflater;

    public BillAdapper(List<JSONObject> list, Context context) {
        this.list = list;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_bill_detail, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.bill_name);
            holder.number = (TextView) convertView.findViewById(R.id.bill_number);
            holder.time = (TextView) convertView.findViewById(R.id.detail_time);
            holder.balance = (TextView) convertView.findViewById(R.id.balance_number);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if ("null".equals(list.get(position).optString("CHANGE_TYPE"))) {
            holder.name.setText("");
        } else {
            holder.name.setText(list.get(position).optString("CHANGE_TYPE"));
        }

        if ("null".equals(list.get(position).optString("CHANGE_VALUE"))) {
            holder.number.setText("0元");
        } else {
            holder.number.setText(list.get(position).optString("CHANGE_VALUE")+"元");
        }

        if ("null".equals(list.get(position).optString("BALANCE_AFTER"))) {
            holder.balance.setText("0元");
        } else {
            holder.balance.setText(list.get(position).optString("BALANCE_AFTER")+"元");
        }

            //holder.time.setText(TimeUtil.format(list.get(position).optString("CHANGE_TIME")));
        holder.time.setText(list.get(position).optString("CHANGE_TIME"));
        return convertView;
    }
    /**
     * 存放控件
     */
    public final class ViewHolder {
        public TextView name;
        public TextView time;
        public TextView number;
        public TextView balance;

    }
    public void onBoundData(List<JSONObject> datas) {
        if (list != null) {
            list.clear();
            list.addAll(datas);
            notifyDataSetChanged();
        }
    }
}
