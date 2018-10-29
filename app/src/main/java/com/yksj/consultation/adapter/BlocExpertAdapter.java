package com.yksj.consultation.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.home.BlocExpertActivity;
import com.yksj.healthtalk.entity.BlocExpertEntity;

import org.universalimageloader.core.ImageLoader;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by hekl on 18/7/4.
 */

public class BlocExpertAdapter extends SimpleBaseAdapter<BlocExpertEntity.ResultBean.ListBean>{

    public BlocExpertAdapter(Context context, List<BlocExpertEntity.ResultBean.ListBean> datas) {
        super(context, datas);
    }

    @Override
    public int getItemResource() {
        return R.layout.listview_bloc_expert_item;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        BlocExpertEntity.ResultBean.ListBean listBean = datas.get(position);
        TextView tvHospital= (TextView) convertView.findViewById(R.id.tvHospital);
        TextView tvTitle= (TextView) convertView.findViewById(R.id.tvTitle);
        TextView expertAtt= (TextView) convertView.findViewById(R.id.expertAtt);
        TextView expertLook= (TextView) convertView.findViewById(R.id.expertLook);
        CircleImageView imgHeader= (CircleImageView) convertView.findViewById(R.id.imgHeader);
        String textAtt = "关注 "+listBean.getFOLLOW_COUNT();
        String textLook = "浏览 "+listBean.getVISIT_TIME();
        tvTitle.setText(listBean.getSITE_NAME());
        Object site_hospotal = listBean.getSITE_HOSPOTAL();
        String hospotal="";
        if (site_hospotal!=null){
            hospotal=site_hospotal.toString();
        }
        tvHospital.setText(hospotal);
        Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(listBean.getICON_DOCTOR_PICTURE())).error(R.drawable.default_head_doctor).placeholder(R.drawable.default_head_doctor).into(imgHeader);
        SpannableString spTextAtt = new SpannableString(textAtt);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#41b8b6"));
        spTextAtt.setSpan(foregroundColorSpan,2,textAtt.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        expertAtt.setText(spTextAtt);
        SpannableString sptextLook = new SpannableString(textLook);
        sptextLook.setSpan(foregroundColorSpan,2,textLook.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        expertLook.setText(sptextLook);
        return convertView;
    }
}