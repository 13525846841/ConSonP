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
 * Created by ${chen} on 2017/1/4.
 */
public class HealthPlaceAdapter  extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Context context;
    private List<JSONObject> mlist = new ArrayList<JSONObject>();

    public HealthPlaceAdapter(List<JSONObject> list, Context context) {
        this.mlist = list;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mlist.size();

    }

    @Override
    public Object getItem(int position) {
        return mlist.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_popup, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.popup_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if ("null".equals(mlist.get(position).optString("INFO_CLASS_NAME"))) {
            holder.name.setText("");
        } else {
            holder.name.setText(mlist.get(position).optString("INFO_CLASS_NAME"));
        }
        return convertView;
    }

    public String getName(int position){
        return mlist.get(position).optString("INFO_CLASS_NAME");
    }
    public String getClass_id(int position){
        return mlist.get(position).optString("INFO_CLASS_ID");
    }
    /**
     * 控件
     */
    public final class ViewHolder {
        public TextView name;
    }

    public void onBoundData(List<JSONObject> datas) {
        if (mlist != null) {
            mlist.clear();
            mlist.addAll(datas);
            notifyDataSetChanged();
        }
    }
}
