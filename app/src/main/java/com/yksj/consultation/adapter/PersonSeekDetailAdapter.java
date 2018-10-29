package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.TimeUtil;

import org.json.JSONObject;

/**
 * Created by ${chen} on 2017/7/25.
 */
public class PersonSeekDetailAdapter  extends SimpleBaseAdapter<JSONObject> {

    public PersonSeekDetailAdapter(Context context) {
        super(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }
    @Override
    public int getItemResource() {
        return R.layout.item_person_seek_detail;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        TextView cusTime = holder.getView(R.id.seek_time);
        TextView cusContent = holder.getView(R.id.content);


        if (HStringUtil.isEmpty(datas.get(position).optString("CREATE_TIME"))){
            cusTime.setVisibility(View.GONE);
        }else {
            cusTime.setText(TimeUtil.format(datas.get(position).optString("CREATE_TIME")));
        }

        if (HStringUtil.isEmpty(datas.get(position).optString("DISEASE_DESC"))){
            cusContent.setVisibility(View.GONE);
        }else {
            cusContent.setText(datas.get(position).optString("DISEASE_DESC"));
        }
        return convertView;
    }
}
