package com.yksj.consultation.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.DoctorInfoEntity;

import java.util.List;

/**
 * Created by hekl on 18/5/3.
 */

public class WorkstationIntroduceAdapter extends BaseRecyclerAdapter<DoctorInfoEntity.ResultBean.SiteDescBean> {
    public WorkstationIntroduceAdapter(List<DoctorInfoEntity.ResultBean.SiteDescBean> list, Context context) {
        super(list, context);
    }

    @Override
    public int returnLayout() {
        return R.layout.recycler_workstation_introduce_item;
    }


    @Override
    public void onBaseBindViewHolder(ViewHolder holder, int position) {
        TextView tvIntroduceTitle= (TextView) holder.itemView.findViewById(R.id.tvIntroduceTitle);
        final TextView tvIntroduceContent= (TextView) holder.itemView.findViewById(R.id.tvIntroduceContent);
        final TextView tvIntroduceMore= (TextView) holder.itemView.findViewById(R.id.tvIntroduceMore);
        DoctorInfoEntity.ResultBean.SiteDescBean siteDescBean = list.get(position);
        tvIntroduceTitle.setText(siteDescBean.getSITE_NAME());
        tvIntroduceContent.setText(siteDescBean.getSITE_DESC());

        final ViewTreeObserver tvObs= tvIntroduceContent.getViewTreeObserver();
        tvObs.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                ViewTreeObserver obs = tvIntroduceContent.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (tvIntroduceContent.getLineCount() < 3) {
                    tvIntroduceMore.setVisibility(View.GONE);
                }else {
                    tvIntroduceMore.setVisibility(View.VISIBLE);
                }
            }
        });
        tvIntroduceMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvIntroduceContent.setMaxLines(Integer.MAX_VALUE);
                tvIntroduceMore.setVisibility(View.GONE);
            }
        });
    }

}
