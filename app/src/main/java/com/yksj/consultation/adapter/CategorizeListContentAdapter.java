package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;

import org.json.JSONObject;

/**
 * Created by ${chen} on 2017/8/9.
 * 商城右面适配器
 */
public class CategorizeListContentAdapter extends SimpleBaseAdapter<JSONObject> {

    public Context mContext;
    public CategorizeListContentAdapter(Context context) {
        super(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public int getItemResource() {
        return R.layout.item_categorize;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {

        TextView goodName = holder.getView(R.id.good_name);
        TextView goodNumber = holder.getView(R.id.good_name_number);
        ImageView picture = holder.getView(R.id.image_head);

        goodName.setText(datas.get(position).optString("GOODS_NAME"));
        goodNumber.setText("￥ "+datas.get(position).optString("CURRENT_PRICE"));

        String url = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW + datas.get(position).optString("GOOD_BIG_PIC");
        Picasso.with(mContext).load(url).placeholder(R.drawable.order_detail).into(picture);

        return convertView;
    }
}
