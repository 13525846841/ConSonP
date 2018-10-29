package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.TimeUtil;

import org.json.JSONObject;

/**
 * Created by ${chen} on 2017/7/24.
 */
public class CaseManageAdapter extends SimpleBaseAdapter<JSONObject> {
    private Context context;

    public CaseManageAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();

    }

    @Override
    public int getItemResource() {
        return R.layout.item_case_manage;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        TextView name = holder.getView(R.id.main);
        TextView time = holder.getView(R.id.time);
        TextView illness = holder.getView(R.id.illness);


        if (!HStringUtil.isEmpty(datas.get(position).optString("MEDICAL_NAME"))) {
            name.setText("主诉: " + datas.get(position).optString("MEDICAL_NAME"));
        }
        if (!HStringUtil.isEmpty(datas.get(position).optString("RECORD_TIME"))) {
            time.setText("日期: " + TimeUtil.getTimeStr(datas.get(position).optString("RECORD_TIME")));
        }
        if (!HStringUtil.isEmpty(datas.get(position).optString("DISEASE_STATEMENT"))) {
            illness.setText("病请说明:" + "\n" +"\n" + datas.get(position).optString("DISEASE_STATEMENT"));
        }



        return convertView;
    }
}
