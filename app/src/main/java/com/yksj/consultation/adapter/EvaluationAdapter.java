package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.EvaluationEntity;
import com.yksj.healthtalk.entity.HospitaFindTeamEntity;
import com.yksj.healthtalk.utils.TimeUtil;

import org.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by hekl on 18/4/24.
 */

public class EvaluationAdapter extends SimpleBaseAdapter<EvaluationEntity.ResultBean> {
    public EvaluationAdapter(Context context, List<EvaluationEntity.ResultBean> datas) {
        super(context, datas);
    }

    @Override
    public int getItemResource() {
        return R.layout.listview_evaluation_item;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        ImageView header= (ImageView) convertView.findViewById(R.id.header);
        TextView nickName= (TextView) convertView.findViewById(R.id.nickName);
        TextView time= (TextView) convertView.findViewById(R.id.time);
        TextView content= (TextView) convertView.findViewById(R.id.content);
        EvaluationEntity.ResultBean resultBean = datas.get(position);
        Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(resultBean.getBIG_ICON_BACKGROUND()))
                .error(R.drawable.default_head_patient).placeholder(R.drawable.default_head_patient)
                .into(header);
        nickName.setText(resultBean.getCUSTOMER_NAME());
        time.setText(TimeUtil.getFormatDate(resultBean.getEVALUATE_TIME()));
        content.setText(resultBean.getNOTE());
        return convertView;
    }
}
