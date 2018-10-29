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
 * Created by ${chen} on 2016/11/28.
 */
public class CommentAdapter extends SimpleBaseAdapter<JSONObject> {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Context context;
    private List<JSONObject> list = new ArrayList<JSONObject>();

    public CommentAdapter( Context context,List<JSONObject> list) {
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
        return R.layout.item_comment;
    }

    @Override
    public View getItemView(int position, View convertView, SimpleBaseAdapter<JSONObject>.ViewHolder holder) {

        TextView name = holder.getView(R.id.case_dis_comment_item_name);
        TextView time = holder.getView(R.id.case_dis_comment_item_time);
        ImageView headView = holder.getView(R.id.case_dis_comment_item_img);
        TextView content = holder.getView(R.id.case_dis_comment_item_content);

        if (HStringUtil.isEmpty(datas.get(position).optJSONObject("customer").optString("CUSTOMER_NICKNAME"))){
            name.setText(datas.get(position).optJSONObject("customer").optString("CUSTOMER_ACCOUNTS"));
        }else {
            name.setText(datas.get(position).optJSONObject("customer").optString("CUSTOMER_NICKNAME"));
        }

        if (HStringUtil.isEmpty(datas.get(position).optString("COMMENT_TIME"))){
            time.setVisibility(View.GONE);
        }else {
            time.setText(TimeUtil.format(datas.get(position).optString("COMMENT_TIME")));
        }

        if (HStringUtil.isEmpty(datas.get(position).optString("COMMENT_CONTENT"))){
            content.setVisibility(View.GONE);
        }else {
            content.setText(datas.get(position).optString("COMMENT_CONTENT"));
        }

        //图片展示
        String url= HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW+datas.get(position).optJSONObject("customer").optString("CLIENT_ICON_BACKGROUND");
        Picasso.with(context).load(url).placeholder(R.drawable.default_head_patient).into(headView);
        return convertView;
    }

}
