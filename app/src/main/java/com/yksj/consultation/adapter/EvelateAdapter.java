package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * Created by zheng on 15/9/25.
 */
public class EvelateAdapter extends BaseAdapter {
    private Context context;
    private List<Map<String, String>> mData = new ArrayList<>();

    public EvelateAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    public void add(List<Map<String, String>> mData) {
        this.mData = mData;
        notifyDataSetChanged();
    }

    public void removeAll() {
        this.mData.clear();
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.comment_list_item, null);
            TextView name = (TextView) view.findViewById(R.id.name);
            TextView time = (TextView) view.findViewById(R.id.time);
            TextView type = (TextView) view.findViewById(R.id.service_type);
            TextView content = (TextView) view.findViewById(R.id.evaluate_content);
            ViewHolder holder = new ViewHolder();
            holder.name = name;
            holder.time = time;
            holder.type = type;
            holder.content = content;
            view.setTag(holder);
        }
        ViewHolder holder = (ViewHolder) view.getTag();
        Map<String, String> map = mData.get(i);
        holder.name.setText("医生：" + map.get("DOCTOR_REAL_NAME") + "    患者：" + map.get("CUSTOMER_NICKNAME"));
        String content = map.get("COMMENT_INFO");
        holder.content.setText(content);
        holder.time.setText(TimeUtil.getTimeStr8(map.get("EVALUATE_TIME")));
        if ("5".equals(map.get("SERVICE_TYPE_ID"))) {
            holder.type.setText("图文咨询");
        } else if ("6".equals(map.get("SERVICE_TYPE_ID"))) {
            holder.type.setText("电话咨询");
        } else if ("8".equals(map.get("SERVICE_TYPE_ID"))) {
            holder.type.setText("视频咨询");
        }
        return view;
    }

    public static class ViewHolder {
        TextView name;
        TextView time;
        TextView type;
        TextView content;
    }
}
