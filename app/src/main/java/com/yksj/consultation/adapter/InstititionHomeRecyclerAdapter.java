package com.yksj.consultation.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.InstitutionHomeEntity;

import org.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by hekl on 18/7/31.
 */

public class InstititionHomeRecyclerAdapter extends BaseRecyclerAdapter<InstitutionHomeEntity.ResultBean> {

    public InstititionHomeRecyclerAdapter(List<InstitutionHomeEntity.ResultBean> list, Context context) {
        super(list, context);
    }

    @Override
    public int returnLayout() {
        return R.layout.recycler_institution_home_item;
    }

    @Override
    public void onBaseBindViewHolder(ViewHolder holder, int position) {
        ImageView topImg = (ImageView) holder.itemView.findViewById(R.id.topImg);
        TextView titleTv = (TextView) holder.itemView.findViewById(R.id.titleTv);
        TextView typeTv = (TextView) holder.itemView.findViewById(R.id.typeTv);
        TextView addressTv = (TextView) holder.itemView.findViewById(R.id.addressTv);
        InstitutionHomeEntity.ResultBean resultBean = list.get(position);
        Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(resultBean.getUNIT_PIC1())).error(R.drawable.waterfall_default).placeholder(R.drawable.waterfall_default)
                .dontAnimate().into(topImg);
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
        addressTv.setText(resultBean.getADDRESS());
    }
}
