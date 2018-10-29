package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.TimeUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2017/5/9.
 * 名医讲堂推荐医生
 */
public class RecomDocAdapter  extends SimpleBaseAdapter<JSONObject> {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Context context;
    private List<JSONObject> list = new ArrayList<JSONObject>();

    public RecomDocAdapter(Context context,List<JSONObject> list) {
        super(context);
        this.list = list;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public int getItemResource() {
        return R.layout.recon_item;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {


        TextView name = holder.getView(R.id.tv_doc_name);
        TextView hospital = holder.getView(R.id.tv_doc_place);
        TextView title = holder.getView(R.id.tv_doc_pro);
        TextView specially = holder.getView(R.id.specially);
        ImageView image = holder.getView(R.id.det_img_head);

//        name.setText(datas.get(position).optString("DOCTOR_REAL_NAME"));
//        hospital.setText(datas.get(position).optString("DOCTOR_HOSPITAL"));
//        title.setText(datas.get(position).optString("TITLE_NAME"));
//        specially.setText(datas.get(position).optString("DOCTOR_SPECIALLY"));
//
//        //图片展示
//        String url= HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW+datas.get(position).optString("ICON_DOCTOR_PICTURE");
//        Picasso.with(context).load(url).placeholder(R.drawable.waterfall_default).into(image);

        return convertView;
    }
}
