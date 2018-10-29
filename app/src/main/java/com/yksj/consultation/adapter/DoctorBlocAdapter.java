package com.yksj.consultation.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.home.DoctorBlocActivity;
import com.yksj.healthtalk.entity.DoctorBlocEntity;

import org.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by hekl on 18/7/3.
 */

public class DoctorBlocAdapter extends SimpleBaseAdapter<DoctorBlocEntity.ResultBean.ListBean>{

    public DoctorBlocAdapter(Context context, List<DoctorBlocEntity.ResultBean.ListBean> datas) {
        super(context, datas);
    }

    @Override
    public int getItemResource() {
        return R.layout.listview_doctor_bloc_item;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        TextView tvWorkNum= (TextView) convertView.findViewById(R.id.tvWorkNum);
        TextView tvTitle= (TextView) convertView.findViewById(R.id.tvTitle);
        TextView tvIntroduction= (TextView) convertView.findViewById(R.id.tvIntroduction);
        ImageView imgBg= (ImageView) convertView.findViewById(R.id.imgBg);
        DoctorBlocEntity.ResultBean.ListBean listBean = datas.get(position);
        tvTitle.setText(listBean.getUNION_NAME());
        tvIntroduction.setText(listBean.getBE_GOOD());
        Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(listBean.getBACKGROUND())).into(imgBg);
//        Log.i("hhh", "getItemView: "+ImageLoader.getInstance().getDownPathUri(listBean.getBACKGROUND()));
        String source = "工作站 "+listBean.getSITE_COUNT();
        SpannableString spannableString = new SpannableString(source);
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#41b8b6"));
        spannableString.setSpan(foregroundColorSpan,3,source.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvWorkNum.setText(spannableString);
        return convertView;
    }
}
