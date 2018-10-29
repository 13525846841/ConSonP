package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.TimeUtil;

import org.json.JSONObject;

/**
 * Created by ${chen} on 2017/4/13.
 */
public class FollowUpDetailAdapter extends SimpleBaseAdapter<JSONObject> {
    public Context context;

    public FollowUpDetailAdapter(Context context){
        super(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public int getItemResource() {
        return R.layout.item_followup_detail;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        TextView textview = holder.getView(R.id.followup_time);
        TextView temp_doc_name = holder.getView(R.id.followup_content);

        textview.setText(TimeUtil.getTimeStr(datas.get(position).optString("FOLLOW_TIME")));
        temp_doc_name.setText(datas.get(position).optString("FOLLOW_CONTENT"));
        return convertView;
    }
}
