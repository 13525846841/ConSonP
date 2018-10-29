package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.utils.TimeUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2016/12/19.
 */
public class PubFundAdapter extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Context context;
    private List<JSONObject> list = new ArrayList<JSONObject>();
    public PubFundAdapter( Context context) {

        this.list = list;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_pub_fund, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.tv_messtitle_fund);
            holder.time = (TextView) convertView.findViewById(R.id.tv_time_fund);
            holder.imageview = (ImageView) convertView.findViewById(R.id.dynimic_image_fund);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if ("null".equals(list.get(position).optString("FUND_NAME"))) {
            holder.title.setText("");
        } else {
            holder.title.setText(list.get(position).optString("FUND_NAME"));
        }

        if ("null".equals(list.get(position).optString("PUBLIC_TIME"))) {
            holder.time.setText("");
        } else {
            holder.time.setText(TimeUtil.getTimeStr(list.get(position).optString("PUBLIC_TIME")));
        }

        String url= HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW+list.get(position).optString("PICTURE_URL");
        Picasso.with(context).load(url).placeholder(R.drawable.news).into(holder.imageview);
        // 图片展示
       // Picasso.with(context).load(list.get(position).optString("PICTURE_URL_2X")).placeholder(R.drawable.waterfall_default).into(holder.imageview);
       // Picasso.with(context).load(list.get(position).optString("PICTURE_URL")).placeholder(R.drawable.waterfall_default).into(holder.imageview);



        return convertView;
    }
    public String getOffice_id(int position){
        return list.get(position).optString("WORDS_ID").toString();
    }
    /**
     * 存放控件
     */
    public final class ViewHolder {
        public TextView title;
        public TextView time;
        public ImageView imageview;

    }
    public void onBoundData(List<JSONObject> datas) {
        if (list != null) {
            list.clear();
            list.addAll(datas);
            notifyDataSetChanged();
        }
    }
}
