package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.HospitaFindTeamEntity;
import com.yksj.healthtalk.entity.InstitutionHomeEntity;

import org.universalimageloader.core.ImageLoader;

import java.util.List;

import static com.yksj.consultation.son.R.id.re;
import static com.yksj.consultation.son.R.id.topImg;

/**
 * Created by hekl on 18/4/24.
 */

public class InstitutionListAdapter extends SimpleBaseAdapter<InstitutionHomeEntity.ResultBean> {
    public InstitutionListAdapter(Context context, List<InstitutionHomeEntity.ResultBean> datas) {
        super(context, datas);
    }

    @Override
    public int getItemResource() {
        return R.layout.listview_institution_list_item;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        ImageView leftImg = (ImageView) convertView.findViewById(R.id.leftImg);
        TextView titleTv = (TextView) convertView.findViewById(R.id.titleTv);
        TextView title2Tv = (TextView) convertView.findViewById(R.id.title2Tv);
        TextView typeTv = (TextView) convertView.findViewById(R.id.typeTv);
        TextView addressTv = (TextView) convertView.findViewById(R.id.addressTv);
        InstitutionHomeEntity.ResultBean resultBean = datas.get(position);
        Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(resultBean.getUNIT_PIC1())).error(R.drawable.waterfall_default).placeholder(R.drawable.waterfall_default).into(leftImg);
        titleTv.setText(resultBean.getUNIT_NAME());
        switch (resultBean.getCLASS_TYPE()) {
            case "1":
                typeTv.setText("体检中心");
                break;
            case "2":
                typeTv.setText("拓展中心");
                break;
            case "3":
                typeTv.setText("康复中心");
                break;
            case "4":
                typeTv.setText("兴趣中心");
                break;
        }
        title2Tv.setText(resultBean.getUNIT_SPECIALTY_DESC());
        addressTv.setText(resultBean.getADDRESS());
        return convertView;
    }
}
