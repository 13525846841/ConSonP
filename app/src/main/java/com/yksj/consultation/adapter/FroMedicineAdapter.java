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
 * Created by ${chen} on 2016/12/21.
 */
public class FroMedicineAdapter extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Context context;
    private List<JSONObject> mList = new ArrayList<JSONObject>();

    public FroMedicineAdapter( List<JSONObject> mList,Context context) {
        this.mList = mList;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
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
        if ("null".equals(mList.get(position).optString("FRONTIERS_NAME"))) {
            holder.title.setText("");
        } else {
            holder.title.setText(mList.get(position).optString("FRONTIERS_NAME"));
        }

        if ("null".equals(mList.get(position).optString("PUBLIC_TIME"))) {
            holder.time.setText("");
        } else {
            holder.time.setText(TimeUtil.getTimeStr(mList.get(position).optString("PUBLIC_TIME")));
        }
        //图片展示
        String url= HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW + mList.get(position).optString("PICTURE_URL");
        Picasso.with(context).load(url).placeholder(R.drawable.waterfall_default).into(holder.imageview);
        return convertView;
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
        if (mList != null) {
            mList.clear();
            mList.addAll(datas);
            notifyDataSetChanged();
        }
    }
}
