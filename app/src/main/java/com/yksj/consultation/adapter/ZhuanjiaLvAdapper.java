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
 * Created by tidus on 2016/11/8.
 */
public class ZhuanjiaLvAdapper  extends BaseAdapter{
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Context context;
    private List<JSONObject> list = new ArrayList<JSONObject>();

    public ZhuanjiaLvAdapper( List<JSONObject> list,Context context) {
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
            convertView = mInflater.inflate(R.layout.zhuanjia_lv_item, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.tv_item_remen);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if ("null".equals(list.get(position).optString("WORDS"))) {
            holder.name.setText("");
        } else {
            holder.name.setText(list.get(position).optString("WORDS"));
        }

        return convertView;
    }
    public String getOffice_id(int position){
        return list.get(position).optString("WORDS_ID").toString();
    }
    /**
     * 存放控件
     */
    public final class ViewHolder {
        public TextView name;
    }
    public void onBoundData(List<JSONObject> datas) {
        if (list != null) {
            list.clear();
            list.addAll(datas);
            notifyDataSetChanged();
        }
    }
}
