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
 * Created by ${chen} on 2016/12/4.
 */
public class CommondisAdapter extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Context context;
    private List<JSONObject> mList =null;

    public CommondisAdapter(Context context) {
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        mList= new ArrayList<JSONObject>();
    }
    @Override
    public int getCount() {
        return mList.size();
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_com_dis, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.commpn_dis_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.name.setText(mList.get(position).optString("DISEASE_NAME"));

        return convertView;
    }
    /**
     * 存放控件
     */
    public final class ViewHolder {
        public TextView name;
    }

    public void onBoundData(List<JSONObject> datas) {
        if (mList != null) {
            mList.clear();
            mList.addAll(datas);
            notifyDataSetChanged();
        }
    }
}
