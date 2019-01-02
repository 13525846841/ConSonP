package com.yksj.consultation.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.dmsj.newask.Activity.WelcomeActivity;
import com.stx.xhb.xbanner.XBanner;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.news.AtyNewsCenter;
import com.yksj.consultation.son.doctor.ExpertMainUI;
import com.yksj.consultation.son.home.DoctorBlocActivity;
import com.yksj.consultation.son.home.FamousDoctorShareActivity;
import com.yksj.consultation.son.home.HealthLectureActivity;
import com.yksj.consultation.son.home.InstitutionHomeActivity;
import com.yksj.consultation.son.home.PatientHomeActivity;
import com.yksj.consultation.son.home.StationMainUI;
import com.yksj.consultation.son.listener.OnRecyclerClickListener;
import com.yksj.consultation.son.login.UserLoginActivity;
import com.yksj.consultation.son.smallone.bean.Configs;
import com.yksj.consultation.son.views.HomeBannerView;
import com.yksj.healthtalk.entity.PatientHomeEntity;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.TimeUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hekl on 18/10/25.
 */

public class PatientHomeAdapter extends RecyclerView.Adapter<PatientHomeAdapter.PatientViewHolder> implements View.OnClickListener {
    private List<List> dataList;
    private Context context;

    private OnRecyclerClickListener mOnRecyclerClickListener;
    public void setmOnRecyclerClickListener(OnRecyclerClickListener mOnRecyclerClickListener) {
        this.mOnRecyclerClickListener = mOnRecyclerClickListener;
    }

    public PatientHomeAdapter(Context context, ArrayList<List> dataList) {
        this.context = context;
        this.dataList = dataList;
    }

    @Override
    public PatientHomeAdapter.PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType==1){
            return new PatientViewHolder(View.inflate(context, R.layout.recycler_patient_home_top_item,null));
        }else {
            return new PatientViewHolder(LayoutInflater.from(context).inflate( R.layout.recycler_patient_home_bottom_item,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(PatientHomeAdapter.PatientViewHolder holder, int position) {
        initClickListener(holder,position);
    }

    private void initClickListener(final PatientViewHolder holder, final int position) {
        if (position==0){
            final List imgUrlList =dataList.get(0);
            holder.homeBanner.setData(imgUrlList,null);
            holder.homeBanner.loadImage(new XBanner.XBannerAdapter() {
                @Override
                public void loadBanner(XBanner banner, Object model, View view, int position) {
                    PatientHomeEntity.SowingListBean sowingListBean = (PatientHomeEntity.SowingListBean) imgUrlList.get(position);
                    Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(sowingListBean.getSowingMap())).placeholder(R.drawable.waterfall_default).dontAnimate().error(R.drawable.waterfall_default).into((ImageView) view);
                }
            });
            holder.imgZhaoyisheng.setOnClickListener(this);
            holder.imgDocTeam.setOnClickListener(this);
            holder.imgZhaojigou.setOnClickListener(this);
            holder.imgLiuyiboshi.setOnClickListener(this);
            holder.imgDocShare.setOnClickListener(this);
            holder.imgJiankangjiangtang.setOnClickListener(this);
        }else {
            List newsList = dataList.get(1);
            if (newsList.size()==0)return;
            PatientHomeEntity.AllNewsBean allNewsBean= (PatientHomeEntity.AllNewsBean) newsList.get(position-1);
            Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(allNewsBean.getINFO_PICTURE()))
                    .placeholder(R.drawable.waterfall_default).dontAnimate()
                    .error(R.drawable.waterfall_default)
                    .centerCrop().into(holder.imgNews);
            holder.tvNewsTitle.setText(allNewsBean.getINFO_NAME());
            holder.tvNewsTime.setText(TimeUtil.getTimeStr8(allNewsBean.getPUBLISH_TIME()));
            holder.tvNewsMore.setOnClickListener(this);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {mOnRecyclerClickListener.onRecyclerItemClickListener(position-1,holder.itemView,0);
                }
            });
            if (position==1){
                holder.tvRightTip.setVisibility(View.VISIBLE);
                holder.tvNewsMore.setVisibility(View.VISIBLE);
            }else {
                holder.tvRightTip.setVisibility(View.GONE);
                holder.tvNewsMore.setVisibility(View.GONE);
            }
            if (newsList.size()==position){
                holder.bottomV.setVisibility(View.VISIBLE);
            }else {
                holder.bottomV.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return 1+dataList.get(1).size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public void onClick(View v) {
       switch (v.getId()){
           case R.id.imgZhaoyisheng:
               if (!LoginServiceManeger.instance().isVisitor) {
                   context.startActivity(new Intent(context, ExpertMainUI.class));
               } else {
                   context.startActivity(new Intent(context, UserLoginActivity.class));
               }
               break;
           case R.id.imgDocTeam:
               context.startActivity( new Intent(context, StationMainUI.class));
               break;
           case R.id.imgZhaojigou:
               context.startActivity(new Intent(context, InstitutionHomeActivity.class));
               break;
           case R.id.imgLiuyiboshi:
               WelcomeActivity.MERCHANT_ID = Configs.MERCHANT_ID;
               WelcomeActivity.site_id = Configs.site_id;
               WelcomeActivity.themeId = Configs.theme_Id;
               Intent intent1 = new Intent(context, com.dmsj.newask.Activity.ChatActivity.class);
               context.startActivity(intent1);
               break;
           case R.id.imgDocShare:
               if (!LoginServiceManeger.instance().isVisitor) {
                   Intent shareIntent = new Intent(context, FamousDoctorShareActivity.class);
                   shareIntent.putExtra("type","all");
                   context.startActivity(shareIntent);
               } else {
                   context.startActivity(new Intent(context, UserLoginActivity.class));
               }
               break;
           case R.id.imgJiankangjiangtang:
               Intent intent4 = new Intent(context, HealthLectureActivity.class);
               intent4.putExtra("lectureType","all");
               context.startActivity(intent4);
               break;
           case R.id.tvNewsMore:
               Intent intent = new Intent(context, AtyNewsCenter.class);
               intent.putExtra(AtyNewsCenter.TYPE, "News");
               context.startActivity(intent);
               break;
       }
    }

    class PatientViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgZhaoyisheng,imgDocTeam,imgZhaojigou,imgLiuyiboshi,imgDocShare,imgJiankangjiangtang;
        private TextView tvRightTip,tvNewsMore,tvNewsTitle,tvNewsTime;
        private ImageView imgNews;
        private XBanner homeBanner;
        private View bottomV;
        public PatientViewHolder(View itemView) {
            super(itemView);
            homeBanner= (XBanner) itemView.findViewById(R.id.xbanner);
            imgZhaoyisheng= (ImageView) itemView.findViewById(R.id.imgZhaoyisheng);
            imgDocTeam= (ImageView) itemView.findViewById(R.id.imgDocTeam);
            imgZhaojigou= (ImageView) itemView.findViewById(R.id.imgZhaojigou);
            imgLiuyiboshi= (ImageView) itemView.findViewById(R.id.imgLiuyiboshi);
            imgDocShare= (ImageView) itemView.findViewById(R.id.imgDocShare);
            imgJiankangjiangtang= (ImageView) itemView.findViewById(R.id.imgJiankangjiangtang);
            imgNews= (ImageView) itemView.findViewById(R.id.imgNews);
            tvRightTip= (TextView) itemView.findViewById(R.id.tvRightTip);
            tvNewsMore= (TextView) itemView.findViewById(R.id.tvNewsMore);
            tvNewsTitle= (TextView) itemView.findViewById(R.id.tvNewsTitle);
            tvNewsTime= (TextView) itemView.findViewById(R.id.tvNewsTime);
            bottomV=itemView.findViewById(R.id.bottomV);
        }
    }
}
