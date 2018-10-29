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
 * Created by HEKL on 2015/7/14.
 * Used for
 */
public class AdtCommonTools extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private List<JSONObject> list = new ArrayList<JSONObject>();
    private Context context;

    public AdtCommonTools(List<JSONObject> list, Context context) {
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
            convertView = mInflater.inflate(R.layout.aty_commontools_list_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_toolsname);
            holder.desc = (TextView) convertView.findViewById(R.id.tv_desc);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if ("null".equals(list.get(position).optString("TOOL_NAME"))) {
            holder.name.setText("");
        } else {
            holder.name.setText(list.get(position).optString("TOOL_NAME"));
        }
        if ("null".equals(list.get(position).optString("TOOL_DESC"))) {
            holder.desc.setText("");
        } else {
            holder.desc.setText(list.get(position).optString("TOOL_DESC"));
        }
        return convertView;
    }

    /**
     * 存放控件
     */
    public final class ViewHolder {
        public TextView name;
        public TextView desc;
    }
}
