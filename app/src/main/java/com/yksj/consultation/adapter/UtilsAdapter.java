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
 * Created by ${chen} on 2017/5/9.
 * 工具箱适配器
 */
public class UtilsAdapter extends SimpleBaseAdapter<JSONObject> {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Context context;
    private List<JSONObject> list = new ArrayList<JSONObject>();
    public UtilsAdapter(Context context) {
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
        return R.layout.item_utils;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        TextView textview = holder.getView(R.id.utils_name);

        textview.setText(datas.get(position).optString("TOOL_NAME"));

        return convertView;
    }
}
