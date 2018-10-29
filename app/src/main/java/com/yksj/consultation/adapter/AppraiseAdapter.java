package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2017/6/29.
 */
public class AppraiseAdapter extends BaseAdapter {

    private static final int LAYOUTTYPECOUNT=2;
    public List<JSONObject> list = null;
    public Context context;
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private String number;

    public AppraiseAdapter(Context context){
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        list = new ArrayList<JSONObject>();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public int getItemViewType(int position) {
        return list.get(position).optInt("type");
    }



    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = new ViewHolder();
        int type = list.get(position).optInt("type");
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_appraise, null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_assisName);
            holder.header = (TextView) convertView.findViewById(R.id.tv_type);
            holder.hospital = (TextView) convertView.findViewById(R.id.tv_hospital);
            holder.mHeadView = (ImageView) convertView.findViewById(R.id.assistant_head);
            switch (type){
                case 0:
                case 2:
                case 4:
                    convertView = mInflater.inflate(R.layout.item_target_title, null);
                    holder.title = (TextView) convertView.findViewById(R.id.tv_targe_title);
                    holder.icon = (ImageView) convertView.findViewById(R.id.item_img);
                    break;
                case 1:
                case 3:
                case 5:
                    convertView = mInflater.inflate(R.layout.item_appraise, null);
                    holder.name = (TextView) convertView.findViewById(R.id.tv_assisName);
                    holder.header = (TextView) convertView.findViewById(R.id.tv_type);
                    holder.hospital = (TextView) convertView.findViewById(R.id.tv_hospital);
                    holder.mHeadView = (ImageView) convertView.findViewById(R.id.assistant_head);
                    holder.ratingBar = (RatingBar) convertView.findViewById(R.id.rb_speed);
                    break;
            }
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        switch (type) {
            case 0://基层
            case 2://邀请
            case 4://主治
                holder.title.setText(list.get(position).optString("title"));
                holder.icon.setVisibility(View.GONE);
                break;
            case 1:
            case 3:
            case 5:
                holder.name.setText(list.get(position).optString("DOCTOR_REAL_NAME"));
                holder.header.setText(list.get(position).optString("TITLE_NAME"));
                holder.hospital.setText(list.get(position).optString("DOCTOR_HOSPITAL"));
                String url2= HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW+list.get(position).optString("DOCTOR_PICTURE");
                Picasso.with(context).load(url2).placeholder(R.drawable.default_head_patient).into(holder.mHeadView);
                holder.ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        number = String.valueOf(rating);

                    }
                });
                break;
            default:
                break;
        }


        return convertView;
    }

    public String ratingNumber(){
        if(number==null){
            number = String.valueOf(5.0);
        }
        return number;
    }

    /**
     * 存放控件
     */
    public final class ViewHolder {
        public TextView title;//标题
        public ImageView img;
        public TextView name;
        public TextView header;//职称
        public TextView hospital;
        public ImageView mHeadView;
        public ImageView icon;
        public RatingBar ratingBar;
    }

    public void onBoundData(List<JSONObject> datas) {
        if (list != null) {
            list.clear();
            list.addAll(datas);
            notifyDataSetChanged();
        }
    }
}
