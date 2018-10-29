package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.HStringUtil;

import org.json.JSONObject;

/**
 * Created by ${chen} on 2017/7/24.
 */
public class PersonSeekAdapter extends SimpleBaseAdapter<JSONObject>{
    private Context context;
    public PersonSeekAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();

    }

    @Override
    public int getItemResource() {
        return R.layout.item_person_seek;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        TextView custName = holder.getView(R.id.name);
        TextView cusSex = holder.getView(R.id.sex);
        TextView cusAge = holder.getView(R.id.age);

        TextView phone = holder.getView(R.id.phone);
        TextView idCard = holder.getView(R.id.id_card);
        TextView allergy = holder.getView(R.id.allergy);


        if (HStringUtil.isEmpty(datas.get(position).optString("PERSON_NAME"))){
            custName.setVisibility(View.GONE);
        }else {
            custName.setText("姓名: "+datas.get(position).optString("PERSON_NAME"));
        }

        if (HStringUtil.isEmpty(datas.get(position).optString("PERSON_SEX"))){
            cusSex.setVisibility(View.GONE);
        }else {
            if ("M".equals(datas.get(position).optString("PERSON_SEX"))){
                cusSex.setText("性别: 男");
            }else if ("W".equals(datas.get(position).optString("PERSON_SEX"))){
                cusSex.setText("性别: 女");
            }

        }

        if (HStringUtil.isEmpty(datas.get(position).optString("PERSON_AGE"))){
            cusAge.setVisibility(View.GONE);
        }else {
            cusAge.setText("年龄: "+datas.get(position).optString("PERSON_AGE"));
        }

        if (HStringUtil.isEmpty(datas.get(position).optString("PERSON_PHONE"))){
            phone.setVisibility(View.GONE);
        }else {
            phone.setText("手机: "+datas.get(position).optString("PERSON_PHONE"));
        }

        if (HStringUtil.isEmpty(datas.get(position).optString("PERSON_IDENTITY"))){
            idCard.setVisibility(View.GONE);
        }else {
            idCard.setText("身份证号: "+datas.get(position).optString("PERSON_IDENTITY"));
        }

        if (HStringUtil.isEmpty(datas.get(position).optString("PERSON_ALLERGY"))){
            allergy.setVisibility(View.GONE);
        }else {
            allergy.setText("过敏史: "+datas.get(position).optString("PERSON_ALLERGY"));
        }


        return convertView;
    }
}
