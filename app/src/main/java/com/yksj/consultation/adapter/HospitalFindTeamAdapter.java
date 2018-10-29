package com.yksj.consultation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.HospitaFindTeamEntity;

import org.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by hekl on 18/4/24.
 */

public class HospitalFindTeamAdapter extends SimpleBaseAdapter<HospitaFindTeamEntity.ResultBean.ListBean> {
    public HospitalFindTeamAdapter(Context context, List<HospitaFindTeamEntity.ResultBean.ListBean> datas) {
        super(context, datas);
    }

    @Override
    public int getItemResource() {
        return R.layout.listview_doctor_team_item;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        ImageView imDoctorHeader = (ImageView) convertView.findViewById(R.id.imDoctorHeader);
        TextView doctorName = (TextView) convertView.findViewById(R.id.doctorName);
        TextView department = (TextView) convertView.findViewById(R.id.department);
        TextView hospital = (TextView) convertView.findViewById(R.id.hospital);
        TextView tvLook = (TextView) convertView.findViewById(R.id.tvLook);
        TextView doctorCount = (TextView) convertView.findViewById(R.id.doctorCount);
        HospitaFindTeamEntity.ResultBean.ListBean listBean = datas.get(position);
        Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(listBean.getSITE_BIG_PIC())).error(R.drawable.default_head_doctor).placeholder(R.drawable.default_head_doctor).into(imDoctorHeader);
        Log.i("hhh", "getItemView: " + ImageLoader.getInstance().getDownPathUri(listBean.getSITE_BIG_PIC()));
        doctorName.setText(listBean.getDOCTOR_NAME());
        department.setText(listBean.getOFFICE_NAME());
        if (listBean.getSITE_HOSPOTAL()!=null) {
            hospital.setText(listBean.getSITE_HOSPOTAL());
            hospital.setVisibility(View.VISIBLE);
        }else {
            hospital.setVisibility(View.GONE);
        }
        tvLook.setText(listBean.getVISIT_TIME()+"");
        doctorCount.setText(listBean.getMEMBER_NUM()+"");
        return convertView;
    }
}
