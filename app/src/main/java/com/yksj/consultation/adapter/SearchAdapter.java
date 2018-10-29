package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2016/12/28.
 */
public class SearchAdapter extends BaseAdapter {

    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Context context;
    public List<JSONObject> list = null;


    public SearchAdapter(Context context) {
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        int type = list.get(position).optInt("type");
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_target_title, null);
            holder.title = (TextView) convertView.findViewById(R.id.tv_targe_title);
            holder.item_img = (ImageView) convertView.findViewById(R.id.item_img);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_img.setVisibility(View.GONE);
        switch (type) {
            case 0://活动
            case 2://医院
            case 4://医生
            case 6://介绍
            case 8://集团
                String title = list.get(position).optString("title");
                holder.title.setTextSize(20);
                holder.title.setTextColor(context.getResources().getColor(R.color.black));
                holder.title.setText(title);
                break;
            case 1:
                holder.title.setTextSize(16);
                holder.title.setTextColor(context.getResources().getColor(R.color.gray_text));
                holder.title.setText(list.get(position).optString("INFO_NAME"));
                holder.item_img.setVisibility(View.VISIBLE);
                break;
            case 3:
                holder.title.setTextSize(16);
                holder.title.setTextColor(context.getResources().getColor(R.color.gray_text));
                holder.title.setText(list.get(position).optString("HOSPITAL_NAME"));
                holder.item_img.setVisibility(View.VISIBLE);
                break;
            case 5:
                holder.title.setTextSize(16);
                holder.title.setTextColor(context.getResources().getColor(R.color.gray_text));
                holder.title.setText(list.get(position).optString("DOCTOR_REAL_NAME"));
                holder.item_img.setVisibility(View.VISIBLE);
                break;
            case 7:
                holder.title.setTextSize(16);
                holder.title.setTextColor(context.getResources().getColor(R.color.gray_text));
                holder.title.setText(list.get(position).optString("DISEASE_NAME"));
                holder.item_img.setVisibility(View.VISIBLE);
                break;
            case 9:
                holder.title.setTextSize(16);
                holder.title.setTextColor(context.getResources().getColor(R.color.gray_text));
                holder.title.setText(list.get(position).optString("SITE_NAME"));
                holder.item_img.setVisibility(View.VISIBLE);
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
        public ImageView item_img;
    }

    public void onBoundData(List<JSONObject> datas) {
        if (list != null) {
            list.clear();
            list.addAll(datas);
            notifyDataSetChanged();
        }
    }
}
