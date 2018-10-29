package com.yksj.consultation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.listener.OnRecyclerClickListener;
import com.yksj.healthtalk.entity.DepartmentFindTeamEntity;

import org.universalimageloader.core.ImageLoader;

import java.util.List;


/**
 * Created by hekl on 18/4/24.
 */

public class DepartementFindTeamAdapter extends RecyclerView.Adapter<DepartementFindTeamAdapter.DepViewHolder> {
    private List<DepartmentFindTeamEntity.ResultBean> list;
    private Context context;
    private OnRecyclerClickListener onRecyclerClickListener;

    public void setOnRecyclerClickListener(OnRecyclerClickListener onRecyclerClickListener) {
        this.onRecyclerClickListener = onRecyclerClickListener;
    }

    public DepartementFindTeamAdapter(List<DepartmentFindTeamEntity.ResultBean> list, Context context) {
        this.list=list;
        this.context=context;
    }

    @Override
    public DepViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DepViewHolder(View.inflate(context, R.layout.recycler_department_find_team,null));
    }

    @Override
    public void onBindViewHolder(DepViewHolder holder, int position) {
        DepartmentFindTeamEntity.ResultBean resultBean = list.get(position);
        holder.sortTitle.setText(resultBean.getOFFICE_NAME());
        holder.teamNum.setText(""+resultBean.getSITE_COUNT());
        holder.doctorNum.setText(""+resultBean.getDOCTOR_COUNT());
        Glide.with(context).load(ImageLoader.getInstance().getDownPathUri("/CusZiYuan/resources/office_icon/"+resultBean.getOFFICE_ID()+".png")).error(R.drawable.icon_departement_find_team_icon).placeholder(R.drawable.icon_departement_find_team_icon).into(holder.headerImg);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DepViewHolder extends RecyclerView.ViewHolder {
        TextView sortTitle,teamNum,doctorNum;
        ImageView headerImg;
        public DepViewHolder(final View itemView) {
            super(itemView);
            sortTitle= (TextView) itemView.findViewById(R.id.sortTitle);
            teamNum= (TextView) itemView.findViewById(R.id.teamNum);
            doctorNum= (TextView) itemView.findViewById(R.id.doctorNum);
            headerImg= (ImageView) itemView.findViewById(R.id.headerImg);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerClickListener.onRecyclerItemClickListener(getAdapterPosition(),itemView,0);
                }
            });
        }
    }
}
