package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yksj.consultation.son.R;

import java.util.Map;

/**
 * Created by zheng on 2015/9/17.
 */
public class CaseDisListAdapter extends SimpleBaseAdapter<Map<String ,String>> {

    public CaseDisListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemResource() {
        return R.layout.case_dis_list_item;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        TextView name=(TextView) holder.getView(R.id.case_name);
        TextView time=(TextView) holder.getView(R.id.case_time);
        TextView keshi=(TextView) holder.getView(R.id.case_keshi);
        TextView talkNum=(TextView) holder.getView(R.id.case_talk_num);
        return convertView;
    }
}
