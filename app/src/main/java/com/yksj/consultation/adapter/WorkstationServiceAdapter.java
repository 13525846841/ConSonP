package com.yksj.consultation.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.DoctorInfoEntity;

import java.util.List;

/**
 * Created by hekl on 18/5/3.
 */

public class WorkstationServiceAdapter extends BaseRecyclerAdapter<DoctorInfoEntity.ResultBean.SiteServiceBean> {
    public WorkstationServiceAdapter(List<DoctorInfoEntity.ResultBean.SiteServiceBean> list, Context context) {
        super(list, context);
    }

    @Override
    public int returnLayout() {
        return R.layout.recycler_workstation_service_item;
    }


    @Override
    public void onBaseBindViewHolder(ViewHolder holder, int position) {
       TextView tvZixunTitle = (TextView) holder.itemView.findViewById(R.id.tvZixunTitle);
       TextView tvZixunNum = (TextView) holder.itemView.findViewById(R.id.tvZixunNum);
       TextView tvZixunPrice = (TextView) holder.itemView.findViewById(R.id.tvZixunPrice);
        DoctorInfoEntity.ResultBean.SiteServiceBean siteServiceBean = list.get(position);
        String newTitle="向"+ siteServiceBean.getSITE_NAME() +"咨询";
        SpannableString spannableString = new SpannableString(newTitle);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#41b8b6"));
        spannableString.setSpan(foregroundColorSpan,1,newTitle.length()-2, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvZixunTitle.setText(spannableString);
        tvZixunPrice.setText("RMB:"+siteServiceBean.getSERVICE_PRICE()+"/次");
        tvZixunNum.setText("共"+siteServiceBean.getORDER_NUM()+"次购买");
    }

}
