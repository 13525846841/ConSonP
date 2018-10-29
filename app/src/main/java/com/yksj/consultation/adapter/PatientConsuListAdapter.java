package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.TimeUtil;

import org.json.JSONObject;

/**
 * 个人会诊纪录
 * Created by zheng on 15/10/8.
 */
public class PatientConsuListAdapter extends SimpleBaseAdapter<JSONObject> {

    public PatientConsuListAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemResource() {
        return R.layout.patient_consu_list_ada;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        JSONObject object = (JSONObject)datas.get(position);
        TextView time=holder.getView(R.id.con_time);
        TextView state=holder.getView(R.id.con_state);
        String timeStr=TimeUtil.getTimeStr(object.optString("CREATE_TIME").substring(0,8));
        state.setText(timeStr);
        time.setText(object.optString("CONSULTATION_NAME"));
        return convertView;
    }
}
