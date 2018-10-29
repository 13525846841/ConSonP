package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.R;

import java.util.Map;

/**
 * Created by android_zl on 15/9/16.
 */
public class DiacussCaseAtyAdapter extends SimpleBaseAdapter<Map<String, String>> {
    public DiacussCaseAtyAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemResource() {
        return R.layout.discuss_case_list_item;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        TextView name = (TextView) holder.getView(R.id.talk_name);
        TextView time = (TextView) holder.getView(R.id.talk_time);
        TextView con = (TextView) holder.getView(R.id.talk_con);
        ImageView header = (ImageView) holder.getView(R.id.talk_header);

        return convertView;
    }
}
