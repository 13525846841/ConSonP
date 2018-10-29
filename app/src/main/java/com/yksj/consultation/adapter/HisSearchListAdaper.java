package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yksj.consultation.son.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ${chen} on 2016/11/23. 首页搜索历史的适配器
 */
public class HisSearchListAdaper extends BaseAdapter {
    private ArrayList<HashMap<String,String>> mData;
    private Context context;
    public HisSearchListAdaper(Context context,ArrayList<HashMap<String,String>> data){
        this.context = context;
        this.mData = data;
    }
    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView == null) {
            holder = new Holder();
            convertView = LayoutInflater.from(context).inflate(R.layout.search_main_history, null);
            holder.desc = (TextView) convertView.findViewById(R.id.history_text);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }
        HashMap<String,String> data=mData.get(position);
        holder.desc.setText(data.get("name"));

        return convertView;
    }

    class Holder {
        public TextView desc;
    }
}
