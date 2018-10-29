package com.yksj.consultation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.DoctorworksTeamEntity;

import org.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by hekl on 18/5/2.
 */

public class DoctorTeamGridAdapter extends SimpleBaseAdapter<DoctorworksTeamEntity.ResultBean>{


    public DoctorTeamGridAdapter(Context context, List<DoctorworksTeamEntity.ResultBean> datas) {
        super(context, datas);
    }

    @Override
    public int getItemResource() {
        return R.layout.gridview_team_meber_item;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        ImageView imHeader= (ImageView) convertView.findViewById(R.id.imgHeader);
        TextView doctorName= (TextView) convertView.findViewById(R.id.doctorName);
        TextView doctorAddress= (TextView) convertView.findViewById(R.id.doctorAddress);
        TextView doctorJobTitle= (TextView) convertView.findViewById(R.id.doctorJobTitle);
        TextView doctorDepartment= (TextView) convertView.findViewById(R.id.doctorDepartment);
        DoctorworksTeamEntity.ResultBean resultBean = datas.get(position);
        Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(resultBean.getBIG_ICON_BACKGROUND()))
                .placeholder(R.drawable.default_head_doctor).error(R.drawable.default_head_doctor).into(imHeader);
        doctorName.setText(resultBean.getDOCTOR_REAL_NAME());
        doctorAddress.setText(resultBean.getWORK_LOCATION_DESC());
        doctorJobTitle.setText(resultBean.getTITLE_NAME());
        doctorDepartment.setText(resultBean.getOFFICE_NAME());
        Log.i("yy","yyyyyyyyyyy");
        return convertView;
    }
}
