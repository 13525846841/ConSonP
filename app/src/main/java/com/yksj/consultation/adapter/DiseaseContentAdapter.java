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

import static java.lang.Integer.parseInt;
import static java.lang.String.*;

/**
 * Created by ${chen} on 2017/1/9.
 */
public class DiseaseContentAdapter extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Context context;
    public List<JSONObject> list = null;

    public DiseaseContentAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        list = new ArrayList<JSONObject>();
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
        ViewHolder holder = new ViewHolder();
        int type = list.get(position).optInt("CONTENT_TYPE");
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_content_dis, null);
            holder.title = (TextView) convertView.findViewById(R.id.title1);
            holder.content = (TextView) convertView.findViewById(R.id.content1);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
//        if (String.valueOf(type)==null){
//            holder.content.setText((list.get(position).optString("INFO_CONTENT")).substring(3,list.get(position).optString("INFO_CONTENT").length()));
//        }
        switch (type) {

            case 10://介绍
                holder.title.setText(list.get(position).optString("NOTE"));
                holder.content.setText(list.get(position).optString("INFO_CONTENT"));
                break;
            case 20://症状
                holder.title.setText(list.get(position).optString("NOTE"));
                holder.content.setText(list.get(position).optString("INFO_CONTENT"));
                break;
            case 30://诊断
                holder.title.setText(list.get(position).optString("NOTE"));
                holder.content.setText(list.get(position).optString("INFO_CONTENT"));
                break;
            case 40://治疗
                holder.title.setText(list.get(position).optString("NOTE"));
                holder.content.setText(list.get(position).optString("INFO_CONTENT"));
                break;
            case 50://防御
                holder.title.setText(list.get(position).optString("NOTE"));
                holder.content.setText(list.get(position).optString("INFO_CONTENT"));
                break;
            default:
                break;
        }

        return convertView;
    }

    /**
     * 存放控件
     */
    public final class ViewHolder {
        public TextView title;
        public TextView content;
    }

    public void onBoundData(List<JSONObject> datas) {
        if (list != null) {
            list.clear();
            list.addAll(datas);
            notifyDataSetChanged();
        }
    }
}
