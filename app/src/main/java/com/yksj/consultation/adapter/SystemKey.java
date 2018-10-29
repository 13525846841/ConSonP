package com.yksj.consultation.adapter;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yksj.consultation.son.R;

import org.json.JSONObject;

/**
 * Created by Administrator on 2015/7/14.
 */
public class SystemKey extends SimpleBaseAdapter<JSONObject> {
    public SystemKey(Context context) {
        super(context);
    }

    @Override
    public int getItemResource() {
        return R.layout.add_key_itme;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        JSONObject keyjson=datas.get(position);
        TextView key=(TextView)holder.getView(R.id.add_key_itmes);
        key.setText(keyjson.optString("TAG_NAME"));
        return convertView;
    }
}
