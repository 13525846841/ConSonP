package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.home.AddBabyActivity;
import com.yksj.healthtalk.entity.DocPlanEntity;

import org.json.JSONObject;

/**
 * Created by ${chen} on 2017/1/9.
 */
public class DiseaseDetailAdapter  extends SimpleBaseAdapter<JSONObject> {
    private Context context;
    public DiseaseDetailAdapter(Context context) {
        super(context);
        this.context=context;
    }
    @Override
    public int getCount() {
        return datas.size();
    }
    @Override
    public int getItemResource() {
        return R.layout.disease_detail_list_item;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        TextView tv_comment_name =holder.getView(R.id.tv_comment_name);
        TextView tv_comment_title =  holder.getView(R.id.tv_comment_title);
        TextView tv_comment_hospital = holder.getView(R.id.tv_comment_hospital);
        TextView tv_comment_specially = holder.getView(R.id.tv_comment_specially);
        ImageView image_head = holder.getView(R.id.image_head);

        tv_comment_name.setText(datas.get(position).optString("DOCTOR_REAL_NAME"));
        tv_comment_title.setText(datas.get(position).optString("TITLE_NAME"));
        tv_comment_hospital.setText(datas.get(position).optString("DOCTOR_HOSPITAL"));
        tv_comment_specially.setText("擅长： "+datas.get(position).optString("DOCTOR_SPECIALLY"));

        //图片展示
        String url= HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW+datas.get(position).optString("ICON_DOCTOR_PICTURE");
        Picasso.with(context).load(url).placeholder(R.drawable.waterfall_default).into(image_head);
        return convertView;
    }
}
