package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.zip.Inflater;

/**
 * Created by ${chen} on 2017/5/2.
 */
public class SeleteAdapter extends BaseAdapter {
    private Object data;
    private int length;
    private int MAX_NUMBER = 12;
    private LayoutInflater mInflater;
    private Context context;
    public ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>)data;
    public SeleteAdapter(Context context,Object data,int length) {
        this.data = data;
        this.length = length;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return length;
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
            holder= new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_pop_pro, null);
            holder.name = (TextView) convertView.findViewById(R.id.pop_place);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ArrayList<HashMap<String, String>> list = (ArrayList<HashMap<String, String>>)data;
        holder.name.setText(list.get(position).get("name"));
        return convertView;
    }
    /**
     * 存放控件
     */
    public final class ViewHolder {
        public TextView name;
        public TextView utils;
        public ImageView utils_bg;
    }
}
