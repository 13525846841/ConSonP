package com.yksj.consultation.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.listener.OnRecyclerTypeClickListener;
import com.yksj.healthtalk.entity.SelectChildCityEntity;
import com.yksj.healthtalk.entity.SelectProvinceEntity;

import java.util.List;

/**
 * Created by hekl on 18/4/26.
 */

public class FamousDoctorAreaSelectAdapter extends RecyclerView.Adapter<FamousDoctorAreaSelectAdapter.ViewHolder> {

    private Context context;
    private List list;
    private int type=1;//1是province   2是city
    private int selectPosition=-1;
    private OnRecyclerTypeClickListener onRecyclerClick;

    public void setOnRecyclerClick(OnRecyclerTypeClickListener onRecyclerClick) {
        this.onRecyclerClick = onRecyclerClick;
    }

    public FamousDoctorAreaSelectAdapter(Context context, List list, int type) {
        this.context = context;
        this.list = list;
        this.type = type;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.navigation_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Log.i("erer", "onBindViewHolder: "+type);
        if (type==1) {
            if (position==0&&list.get(0)==null){
                holder.navigation_item_name.setText("全部");
        }else {
            SelectProvinceEntity.AreaBean areaBean = (SelectProvinceEntity.AreaBean) list.get(position);
            holder.navigation_item_name.setText(areaBean.getAREA_NAME());

        }
            if (selectPosition==position){
                holder.navLayout.setBackgroundColor(Color.parseColor("#eeeeee"));
            }else {
                holder.navLayout.setBackgroundColor(Color.parseColor("#ffffff"));
            }
            holder.navigation_item_img_index.setVisibility(View.VISIBLE);
        }else{
            if (position==0&&list.get(0)==null){
                holder.navigation_item_name.setText("全部");
            }else {
                SelectChildCityEntity.AreaBean areaBean = (SelectChildCityEntity.AreaBean) list.get(position);
                if (holder.navigation_item_name!=null&&areaBean.getAREA_NAME()!=null) {
                    holder.navigation_item_name.setText(areaBean.getAREA_NAME());
                }

            }
            holder.navigation_item_img_index.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setSelectProvince(int selectPosition){
        this.selectPosition=selectPosition;
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        TextView navigation_item_name;
        ImageView navigation_item_img_index;
        RelativeLayout navLayout;
        public ViewHolder(final View itemView) {
            super(itemView);
            navigation_item_img_index= (ImageView) itemView.findViewById(R.id.navigation_item_img_index);
            navigation_item_name= (TextView) itemView.findViewById(R.id.navigation_item_name);
            navLayout= (RelativeLayout) itemView.findViewById(R.id.navLayout);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRecyclerClick.setOnCliCkListener(itemView,getAdapterPosition(),type);
                }
            });
        }
    }

}
