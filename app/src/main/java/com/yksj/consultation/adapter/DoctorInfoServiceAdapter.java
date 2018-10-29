package com.yksj.consultation.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.DoctorInfoEntity;

import java.util.List;

/**
 * Created by hekl on 18/5/3.
 */

public class DoctorInfoServiceAdapter extends BaseRecyclerAdapter<DoctorInfoEntity.ResultBean.DoctorServiceBean> {
    public DoctorInfoServiceAdapter(List<DoctorInfoEntity.ResultBean.DoctorServiceBean> list, Context context) {
        super(list, context);
    }

    @Override
    public int returnLayout() {
        return R.layout.recycler_workstation_service_item;
    }


    @Override
    public void onBaseBindViewHolder(ViewHolder holder, int position) {
        ImageView imZixun= (ImageView) holder.itemView.findViewById(R.id.zixunImg);
        TextView tvZixunTitle = (TextView) holder.itemView.findViewById(R.id.tvZixunTitle);
        TextView tvZixunNum = (TextView) holder.itemView.findViewById(R.id.tvZixunNum);
        TextView tvZixunPrice = (TextView) holder.itemView.findViewById(R.id.tvZixunPrice);
        TextView yizhenPrice = (TextView) holder.itemView.findViewById(R.id.yizhenPrice);
        DoctorInfoEntity.ResultBean.DoctorServiceBean doctorServiceBean = list.get(position);
        holder.itemView.setVisibility(View.VISIBLE);
        switch (doctorServiceBean.getSERVICE_TYPE_ID()){
            case 1:
                holder.itemView.setVisibility(View.GONE);
                break;
            case 2:
                holder.itemView.setVisibility(View.GONE);
                break;
            case 3:
                tvZixunTitle.setText("门诊预约");
                imZixun.setImageDrawable(context.getResources().getDrawable(R.drawable.menzhenzixun));
                break;
            case 4:
                holder.itemView.setVisibility(View.GONE);
                break;
            case 5:
                tvZixunTitle.setText("图文咨询");
                imZixun.setImageDrawable(context.getResources().getDrawable(R.drawable.work_tuwenzixun));
                break;
            case 6:
                tvZixunTitle.setText("电话咨询");
                imZixun.setImageDrawable(context.getResources().getDrawable(R.drawable.dianhuazixun));
                break;
            case 7:
                tvZixunTitle.setText("包月咨询");
                imZixun.setImageDrawable(context.getResources().getDrawable(R.drawable.baoyuezixun));
                break;
            case 8:
                tvZixunTitle.setText("视频咨询");
                imZixun.setImageDrawable(context.getResources().getDrawable(R.drawable.shipinzixun));
                break;
        }
        if (doctorServiceBean.getFREE_MEDICAL_FLAG()==1){
            yizhenPrice.setText(doctorServiceBean.getSERVICE_PRICE()+"元");
            yizhenPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
            tvZixunPrice.setText("RMB:"+doctorServiceBean.getFREE_MEDICAL_PRICE()+"/次");
            yizhenPrice.setVisibility(View.VISIBLE);
        }else {
            yizhenPrice.setVisibility(View.GONE);
            tvZixunPrice.setText("RMB:"+doctorServiceBean.getSERVICE_PRICE()+"/次");
        }
        tvZixunNum.setText("共"+doctorServiceBean.getORDER_NUM()+"次购买");
    }
}
