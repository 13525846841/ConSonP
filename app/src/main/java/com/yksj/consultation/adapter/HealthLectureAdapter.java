package com.yksj.consultation.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.HealthLectureWorksEntity;
import com.yksj.healthtalk.entity.HospitaFindTeamEntity;
import com.yksj.healthtalk.utils.TimeUtil;

import org.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by hekl on 18/4/24.
 */

public class HealthLectureAdapter extends SimpleBaseAdapter<HealthLectureWorksEntity.ResultBean> {
    public HealthLectureAdapter(Context context, List<HealthLectureWorksEntity.ResultBean> datas) {
        super(context, datas);
    }

    @Override
    public int getItemResource() {
        return R.layout.listview_health_lecture_item;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        ImageView imHead= (ImageView) convertView.findViewById(R.id.imHead);
        TextView title= (TextView) convertView.findViewById(R.id.title);
        TextView docName= (TextView) convertView.findViewById(R.id.docName);
        TextView time= (TextView) convertView.findViewById(R.id.time);
        TextView priceStatus= (TextView) convertView.findViewById(R.id.priceStatus);
        HealthLectureWorksEntity.ResultBean resultBean = datas.get(position);
        Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(resultBean.getSMALL_PIC()))
                .error(R.drawable.waterfall_default)
                .placeholder(R.drawable.waterfall_default).dontAnimate()
                .into(imHead);
        docName.setText(resultBean.getCOURSE_UP_NAME());
        title.setText(resultBean.getCOURSE_NAME());
        time.setText(TimeUtil.getTimeStr8(resultBean.getCOURSE_UP_TIME()));
        if (resultBean.getCOURSE_PAY().equals("1")){
           priceStatus.setText("付费");
            priceStatus.setTextColor(Color.parseColor("#ee5450"));
        }else {
            priceStatus.setText("免费");
            priceStatus.setTextColor(Color.parseColor("#8e8e8e") );
        }
        return convertView;
    }
}
