package com.yksj.consultation.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.home.InstitutionDoingActivity;
import com.yksj.consultation.son.listener.OnRecyclerClickListener;
import com.yksj.healthtalk.entity.InsActEntity;

import java.util.List;

/**
 * Created by hekl on 18/8/1.
 */

public class InstitutionInfoActAdapter extends RecyclerView.Adapter<InstitutionInfoActAdapter.InsViewHolder>{
    private List<InsActEntity.ResultBean> list;
    private Context context;
    private boolean isSelf;
    private OnRecyclerClickListener mOnRecyclerClickListener;

    public void setmOnRecyclerClickListener(OnRecyclerClickListener mOnRecyclerClickListener) {
        this.mOnRecyclerClickListener = mOnRecyclerClickListener;
    }
    public InstitutionInfoActAdapter(List<InsActEntity.ResultBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public InsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new InsViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_institution_info_act,parent,false));
    }

    @Override
    public void onBindViewHolder(final InsViewHolder holder, final int position) {
        InsActEntity.ResultBean resultBean = list.get(position);
        holder.tvActTitle.setText(resultBean.getACTIV_TITLE());
        holder.tvActTime.setText(resultBean.getACTIV_TIME_DESC());
        holder.tvActContent.setText(resultBean.getACTIV_DESC());
        holder.editAct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnRecyclerClickListener.onRecyclerItemClickListener(position,holder.editAct,0);
            }
        });
        if (isSelf){
            holder.editAct.setVisibility(View.VISIBLE);
        }else {
            holder.editAct.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class InsViewHolder extends RecyclerView.ViewHolder{
        TextView tvActTitle,tvActTime,tvActContent;
        ImageView editAct;
        public InsViewHolder(View itemView) {
            super(itemView);
            tvActTitle= (TextView)itemView.findViewById(R.id.tvActTitle);
            tvActTime= (TextView) itemView.findViewById(R.id.tvActTime);
            tvActContent= (TextView) itemView.findViewById(R.id.tvActContent);
            editAct= (ImageView) itemView.findViewById(R.id.editAct);
        }
    }

    public void setIsSelf(boolean isSelf){
        this.isSelf=isSelf;
    }
}
