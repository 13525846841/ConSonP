package com.yksj.consultation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.listener.OnRecyclerClickListener;
import com.yksj.healthtalk.entity.HospitalHotAreaEntity;

import java.util.List;

/**
 * Created by hekl on 18/4/25.
 */

public class HotAreaAdapter extends RecyclerView.Adapter<HotAreaAdapter.HotAreaHolder> {
    private List<HospitalHotAreaEntity.ResultBean> list;
    private Context context;
    private OnRecyclerClickListener onRecyclerClickListener;

    public HotAreaAdapter(List<HospitalHotAreaEntity.ResultBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setOnRecyclerClickListener(OnRecyclerClickListener onRecyclerClickListener) {
        this.onRecyclerClickListener = onRecyclerClickListener;
    }

    @Override
    public HotAreaAdapter.HotAreaHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HotAreaHolder(View.inflate(context, R.layout.recycler_hospital_find_team_city_item,null));
    }

    @Override
    public void onBindViewHolder(HotAreaAdapter.HotAreaHolder holder, int position) {
        holder.hotArea.setText(list.get(position).getAREA_NAME2());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class HotAreaHolder extends RecyclerView.ViewHolder{
        Button hotArea;
        public HotAreaHolder(final View itemView) {
            super(itemView);
            hotArea= (Button) itemView.findViewById(R.id.hotArea);
            hotArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerClickListener.onRecyclerItemClickListener(getAdapterPosition(),itemView,0);
                }
            });
        }
    }
}
