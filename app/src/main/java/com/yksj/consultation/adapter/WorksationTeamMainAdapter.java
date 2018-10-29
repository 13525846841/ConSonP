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
import com.yksj.healthtalk.entity.DoctorWorkstationMainEntity;

import org.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by hekl on 18/4/28.
 */

public class WorksationTeamMainAdapter extends RecyclerView.Adapter<WorksationTeamMainAdapter.WorksationHolder> {

    private Context context;
    private List<DoctorWorkstationMainEntity.ResultBean.SiteMemberBean> list;
    private OnRecyclerClickListener onRecyclerClickListener;

    public void setOnRecyclerClickListener(OnRecyclerClickListener onRecyclerClickListener) {
        this.onRecyclerClickListener = onRecyclerClickListener;
    }

    public WorksationTeamMainAdapter(Context context, List<DoctorWorkstationMainEntity.ResultBean.SiteMemberBean> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public WorksationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WorksationHolder(View.inflate(context, R.layout.gridview_team_meber_item,null));
    }

    @Override
    public void onBindViewHolder(WorksationHolder holder, int position) {
        DoctorWorkstationMainEntity.ResultBean.SiteMemberBean model = list.get(position);
        Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(model.getICON_DOCTOR_PICTURE())).placeholder(R.drawable.default_head_doctor)
                .error(R.drawable.default_head_doctor).into(holder.imgHeader);
        holder.doctorName.setText(model.getDOCTOR_REAL_NAME());
        holder.doctorAddress.setText(model.getWORK_LOCATION_DESC());
        holder.doctorJobTitle.setText(model.getTITLE_NAME());
        holder.doctorDepartment.setText(model.getOFFICE_NAME());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class WorksationHolder extends RecyclerView.ViewHolder{
        ImageView imgHeader;
        TextView doctorName,doctorAddress,doctorJobTitle,doctorDepartment;
        public WorksationHolder(final View itemView) {
            super(itemView);
            imgHeader= (ImageView) itemView.findViewById(R.id.imgHeader);
            doctorName= (TextView) itemView.findViewById(R.id.doctorName);
            doctorAddress= (TextView) itemView.findViewById(R.id.doctorAddress);
            doctorJobTitle= (TextView) itemView.findViewById(R.id.doctorJobTitle);
            doctorDepartment= (TextView) itemView.findViewById(R.id.doctorDepartment);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerClickListener.onRecyclerItemClickListener(getAdapterPosition(),itemView,0);
                }
            });
        }
    }
}
