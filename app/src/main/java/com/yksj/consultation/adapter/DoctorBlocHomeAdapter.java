package com.yksj.consultation.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.home.BlocDShiJActivity;
import com.yksj.consultation.son.home.BlocExpertActivity;
import com.yksj.consultation.son.home.QRcodeDoctorActivity;
import com.yksj.healthtalk.entity.BlocDaShiJEntity;
import com.yksj.healthtalk.entity.DoctorBlocHomeEntity;
import com.yksj.healthtalk.utils.TimeUtil;

import org.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by hekl on 18/7/3.
 */

public class DoctorBlocHomeAdapter extends RecyclerView.Adapter<DoctorBlocHomeAdapter.BlocViewHorder> implements View.OnClickListener {
    private List<Object> list;
    private Context context;

    public DoctorBlocHomeAdapter(List<Object> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public BlocViewHorder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==0){
            View view = LayoutInflater.from(context).inflate( R.layout.recycler_doctor_bloc_home_top, parent,false);
            return new BlocViewHorder(view);
        }else {
            View view = LayoutInflater.from(context).inflate( R.layout.recycler_doctor_bloc_home_bottom, parent,false);
            return new BlocViewHorder(view);
        }
    }

    @Override
    public void onBindViewHolder(final BlocViewHorder holder, int position) {
        if (position==0){
            DoctorBlocHomeEntity.ResultBean result = (DoctorBlocHomeEntity.ResultBean) list.get(position);
            String lookNum=result.getVISIT_TIME()+"\n次浏览";
            String expert=result.getEXPERT_COUNT()+"\n名专家";
            String att=result.getFOLLOW_COUNT()+"\n次关注";
            SpannableString spLookNum = new SpannableString(lookNum);
            SpannableString spExpert = new SpannableString(expert);
            SpannableString spAtt = new SpannableString(att);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#41b8b6"));
            spLookNum.setSpan(foregroundColorSpan,0,spLookNum.length()-3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spExpert.setSpan(foregroundColorSpan,0,spExpert.length()-3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spAtt.setSpan(foregroundColorSpan,0,spAtt.length()-3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            holder.tvLook.setText(spLookNum);
            holder.tvExpertNum.setText(spExpert);
            holder.tvAtt.setText(spAtt);
            holder.skill.setText(result.getBE_GOOD());
            holder.introduce.setText(result.getUNION_DESC());
            holder.lineDshiji.setOnClickListener(this);
            holder.lineZjtuan.setOnClickListener(this);
            holder.lineQr.setOnClickListener(this);
            holder.moreSy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.introduce.setMaxLines(Integer.MAX_VALUE);
                    holder.moreSy.setVisibility(View.GONE);
                }
            });
            Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(result.getBACKGROUND())).into(holder.imgBg);
            holder.title.setText(result.getUNION_NAME());
        }else {
            BlocDaShiJEntity.ResultBean.ListBean listBean = (BlocDaShiJEntity.ResultBean.ListBean) list.get(position);
            Object event_image = listBean.getEVENT_IMAGE();
            String imgUrl="";
            if (event_image!=null){
                imgUrl=event_image.toString();
            }
            if (position==1){
                holder.imgTopJianTou.setVisibility(View.VISIBLE);
                holder.lineBlocLeft.setVisibility(View.INVISIBLE);
                holder.lineBlocRight.setVisibility(View.VISIBLE);
                holder.viewLeft.setVisibility(View.INVISIBLE);
                holder.viewRight.setVisibility(View.VISIBLE);
                holder.dsjTime1.setText(TimeUtil.getTimeStr8(listBean.getEVENT_TIME()));
                holder.tvDsjTitle1.setText(listBean.getEVENT_TITLE());
                Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(imgUrl)).error(R.drawable.waterfall_default).placeholder(R.drawable.waterfall_default).dontAnimate().into(holder.imgDsj1);
            }else {
                if (position%2==0) {
                    holder.viewLeft.setVisibility(View.VISIBLE);
                    holder.viewRight.setVisibility(View.INVISIBLE);
                    holder.lineBlocLeft.setVisibility(View.VISIBLE);
                    holder.lineBlocRight.setVisibility(View.INVISIBLE);
                    holder.dsjTime.setText(TimeUtil.getTimeStr8(listBean.getEVENT_TIME()));
                    holder.tvDsjTitle.setText(listBean.getEVENT_TITLE());
                    Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(imgUrl)).error(R.drawable.waterfall_default).placeholder(R.drawable.waterfall_default).dontAnimate().into(holder.imgDsj);

                }else {
                    holder.viewLeft.setVisibility(View.INVISIBLE);
                    holder.viewRight.setVisibility(View.VISIBLE);
                    holder.lineBlocLeft.setVisibility(View.INVISIBLE);
                    holder.lineBlocRight.setVisibility(View.VISIBLE);
                    holder.dsjTime1.setText(TimeUtil.getTimeStr8(listBean.getEVENT_TIME()));
                    holder.tvDsjTitle1.setText(listBean.getEVENT_TITLE());
                    Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(imgUrl)).error(R.drawable.waterfall_default).placeholder(R.drawable.waterfall_default).dontAnimate().into(holder.imgDsj1);
                }
                holder.imgTopJianTou.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    @Override
    public void onClick(View v) {
        DoctorBlocHomeEntity.ResultBean result = (DoctorBlocHomeEntity.ResultBean) list.get(0);
        switch (v.getId()){
            case R.id.lineDshiji:
                Intent intent = new Intent(context, BlocDShiJActivity.class);
                intent.putExtra("union_id",result.getUNION_ID());
                context.startActivity(intent);
                break;
            case R.id.lineZjtuan:
                Intent intent1 = new Intent(context, BlocExpertActivity.class);
                intent1.putExtra("union_id",result.getUNION_ID());
                context.startActivity(intent1);
                break;
            case R.id.lineQr:
                Intent intent2 = new Intent(context, QRcodeDoctorActivity.class);
                intent2.putExtra("specially",result.getUNION_NAME());
                intent2.putExtra("hisInfo",result.getBE_GOOD());
                intent2.putExtra("qrCodeUrl",result.getQrCodeUrl());
                context.startActivity(intent2);
                break;
        }
    }

    class BlocViewHorder extends RecyclerView.ViewHolder{
        LinearLayout lineBlocLeft,lineBlocRight,lineDshiji,lineZjtuan,lineQr;
        View viewLeft,viewRight;
        ImageView imgTopJianTou,imgBg,imgDsj,imgDsj1;
        TextView title,tvLook,tvExpertNum,tvAtt,tvNotice,skill,introduce,dsjTime,dsjTime1,tvDsjTitle,tvDsjTitle1,moreSy;
        public BlocViewHorder(View itemView) {
            super(itemView);
            lineBlocLeft= (LinearLayout) itemView.findViewById(R.id.lineBlocLeft);
            lineBlocRight= (LinearLayout) itemView.findViewById(R.id.lineBlocRight);
            lineDshiji= (LinearLayout) itemView.findViewById(R.id.lineDshiji);
            lineZjtuan= (LinearLayout) itemView.findViewById(R.id.lineZjtuan);
            lineQr= (LinearLayout) itemView.findViewById(R.id.lineQr);
            viewLeft= itemView.findViewById(R.id.viewLeft);
            viewRight= itemView.findViewById(R.id.viewRight);
            imgTopJianTou= (ImageView) itemView.findViewById(R.id.imgTopJianTou);
            imgBg= (ImageView) itemView.findViewById(R.id.imgBg);
            imgDsj= (ImageView) itemView.findViewById(R.id.imgDsj);
            imgDsj1= (ImageView) itemView.findViewById(R.id.imgDsj1);
            title= (TextView) itemView.findViewById(R.id.title);
            tvLook= (TextView) itemView.findViewById(R.id.tvLook);
            tvExpertNum= (TextView) itemView.findViewById(R.id.tvExpertNum);
            tvAtt= (TextView) itemView.findViewById(R.id.tvAtt);
            tvNotice= (TextView) itemView.findViewById(R.id.tvNotice);
            skill= (TextView) itemView.findViewById(R.id.skill);
            introduce= (TextView) itemView.findViewById(R.id.Introduce);
            dsjTime= (TextView) itemView.findViewById(R.id.dsjTime);
            dsjTime1= (TextView) itemView.findViewById(R.id.dsjTime1);
            tvDsjTitle= (TextView) itemView.findViewById(R.id.tvDsjTitle);
            tvDsjTitle1= (TextView) itemView.findViewById(R.id.tvDsjTitle1);
            moreSy= (TextView) itemView.findViewById(R.id.moreSy);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position==0)return 0;
        else return 1;
    }
}
