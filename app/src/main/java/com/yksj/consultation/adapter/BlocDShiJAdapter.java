package com.yksj.consultation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.BlocDaShiJEntity;
import com.yksj.healthtalk.utils.TimeUtil;

import org.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by hekl on 18/7/4.
 */

public class BlocDShiJAdapter extends RecyclerView.Adapter<BlocDShiJAdapter.DsjViewHolder> {
    private List<BlocDaShiJEntity.ResultBean.ListBean> list;
    private Context context;

    public BlocDShiJAdapter(List<BlocDaShiJEntity.ResultBean.ListBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public DsjViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DsjViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_doctor_bloc_home_bottom,parent,false));
    }

    @Override
    public void onBindViewHolder(DsjViewHolder holder, int position) {
        BlocDaShiJEntity.ResultBean.ListBean listBean = list.get(position);
        Object event_image = listBean.getEVENT_IMAGE();
        String imgUrl="";
        if (event_image!=null){
            imgUrl=event_image.toString();
        }
        if (position==0){
            holder.imgTopJianTou.setVisibility(View.VISIBLE);
            holder.lineBlocLeft.setVisibility(View.INVISIBLE);
            holder.lineBlocRight.setVisibility(View.VISIBLE);
            holder.viewLeft.setVisibility(View.INVISIBLE);
            holder.viewRight.setVisibility(View.VISIBLE);
            holder.dsjTime1.setText(TimeUtil.getTimeStr8(listBean.getEVENT_TIME()));
            holder.tvDsjTitle1.setText(listBean.getEVENT_TITLE());
            Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(imgUrl)).error(R.drawable.waterfall_default).placeholder(R.drawable.waterfall_default).into(holder.imgDsj1);
        }else {
            if (position%2!=0) {
                holder.viewLeft.setVisibility(View.VISIBLE);
                holder.viewRight.setVisibility(View.INVISIBLE);
                holder.lineBlocLeft.setVisibility(View.VISIBLE);
                holder.lineBlocRight.setVisibility(View.INVISIBLE);
                holder.dsjTime.setText(TimeUtil.getTimeStr8(listBean.getEVENT_TIME()));
                holder.tvDsjTitle.setText(listBean.getEVENT_TITLE());
                Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(imgUrl)).error(R.drawable.waterfall_default).placeholder(R.drawable.waterfall_default).into(holder.imgDsj);
            }else {
                holder.viewLeft.setVisibility(View.INVISIBLE);
                holder.viewRight.setVisibility(View.VISIBLE);
                holder.lineBlocLeft.setVisibility(View.INVISIBLE);
                holder.lineBlocRight.setVisibility(View.VISIBLE);
                holder.dsjTime1.setText(TimeUtil.getTimeStr8(listBean.getEVENT_TIME()));
                holder.tvDsjTitle1.setText(listBean.getEVENT_TITLE());
                Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(imgUrl)).error(R.drawable.waterfall_default).placeholder(R.drawable.waterfall_default).into(holder.imgDsj1);
            }
            holder.imgTopJianTou.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class DsjViewHolder extends RecyclerView.ViewHolder{
        LinearLayout lineBlocLeft,lineBlocRight;
        View viewLeft,viewRight;
        ImageView imgTopJianTou,imgDsj,imgDsj1;
        TextView dsjTime,dsjTime1,tvDsjTitle,tvDsjTitle1;
        DsjViewHolder(View itemView) {
            super(itemView);
            lineBlocLeft= (LinearLayout) itemView.findViewById(R.id.lineBlocLeft);
            lineBlocRight= (LinearLayout) itemView.findViewById(R.id.lineBlocRight);
            viewLeft= itemView.findViewById(R.id.viewLeft);
            viewRight= itemView.findViewById(R.id.viewRight);
            imgTopJianTou= (ImageView) itemView.findViewById(R.id.imgTopJianTou);
            imgDsj= (ImageView) itemView.findViewById(R.id.imgDsj);
            imgDsj1= (ImageView) itemView.findViewById(R.id.imgDsj1);
            dsjTime= (TextView) itemView.findViewById(R.id.dsjTime);
            dsjTime1= (TextView) itemView.findViewById(R.id.dsjTime1);
            tvDsjTitle= (TextView) itemView.findViewById(R.id.tvDsjTitle);
            tvDsjTitle1= (TextView) itemView.findViewById(R.id.tvDsjTitle1);
        }
    }

}
