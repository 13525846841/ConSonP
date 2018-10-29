package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.utils.HStringUtil;

import org.json.JSONObject;


/**
 * Created by ${chen} on 2017/7/5.
 */
public class StationMemberAdapter extends SimpleBaseAdapter<JSONObject> {
    public StationMemberAdapter(Context context) {
        super(context);
    }
    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public int getItemResource() {
        return R.layout.item_station_member;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        ImageView custHaed = holder.getView(R.id.det_img_head);
        TextView custName = holder.getView(R.id.tv_station_pro);
        TextView consultnum = holder.getView(R.id.tv_station_show);


        String url= HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW+datas.get(position).optString("CLIENT_ICON_BACKGROUND");
        Picasso.with(context).load(url).placeholder(R.drawable.default_head_doctor).into(custHaed);


        custName.setText(datas.get(position).optString("DOCTOR_REAL_NAME"));
        if (!HStringUtil.isEmpty(datas.get(position).optString("INTRODUCTION"))){
            consultnum.setText("简介: "+datas.get(position).optString("INTRODUCTION"));
        }
        return convertView;
    }
}
