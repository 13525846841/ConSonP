package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;

import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2016/12/21.
 * 名医义诊适配器
 */
public class FamDoctorAdapter extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Context context;
    private List<JSONObject> list = new ArrayList<JSONObject>();
    private ImageLoader instance;
    private DisplayImageOptions mOptions;

    public FamDoctorAdapter(Context context) {
        this.context = context;
        instance = ImageLoader.getInstance();
        mOptions = DefaultConfigurationFactory.createDynamicMesDisplayImageOptions(context);
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
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_fam_doc, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.select_expert_list_item_name);
            holder.post = (TextView) convertView.findViewById(R.id.select_expert_list_item_doctitle);
            holder.hospital = (TextView) convertView.findViewById(R.id.select_expert_list_item_hospital);
            holder.begoodat = (TextView) convertView.findViewById(R.id.select_expert_list_item_spetical);
            holder.room = (TextView) convertView.findViewById(R.id.select_expert_list_item_docroom);
            holder.image = (ImageView) convertView.findViewById(R.id.select_expert_list_item_headicon);

            //holder.price = (TextView) convertView.findViewById(R.id.prime_cost);//字体
            holder.number = (TextView) convertView.findViewById(R.id.prime_cost_number);//原价
            holder.money = (TextView) convertView.findViewById(R.id.select_expert_list_item_select);//义诊价格
            holder.ll_prime_cost = (LinearLayout) convertView.findViewById(R.id.ll_prime_cost);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if ("null".equals(list.get(position).optString("DOCTOR_REAL_NAME"))) {
            holder.name.setVisibility(View.GONE);
        } else {
            holder.name.setText(list.get(position).optString("DOCTOR_REAL_NAME"));
        }

        if ("null".equals(list.get(position).optString("TITLE_NAME"))) {
            holder.post.setVisibility(View.GONE);
        } else {
            holder.post.setText(list.get(position).optString("TITLE_NAME"));
        }

        if ("null".equals(list.get(position).optString("OFFICE_NAME"))) {
            holder.room.setVisibility(View.GONE);
        } else {
            holder.room.setText(list.get(position).optString("OFFICE_NAME"));
        }

        if ("null".equals(list.get(position).optString("DOCTOR_HOSPITAL"))) {
            holder.hospital.setVisibility(View.GONE);
        } else {
            holder.hospital.setText(list.get(position).optString("DOCTOR_HOSPITAL"));
        }
        if ("null".equals(list.get(position).optString("DOCTOR_SPECIALLY"))) {
            holder.begoodat.setVisibility(View.GONE);
        } else {
            holder.begoodat.setText(list.get(position).optString("DOCTOR_SPECIALLY"));
        }

//        if ("null".equals(list.get(position).optString("SERVICE_PRICE"))) {
//            holder.number.setVisibility(View.GONE);
//        } else {
//            SpannableString ss = new SpannableString("原价  " + list.get(position).optString("SERVICE_PRICE") + "元");
//            ss.setSpan(new StrikethroughSpan(), 0, ss.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//            holder.number.setText(ss);
//        }

//        if ("null".equals(list.get(position).optString("FREE_MEDICAL_PRICE"))) {
//            holder.money.setVisibility(View.GONE);
//        } else {
//            holder.money.setText(list.get(position).optString("FREE_MEDICAL_PRICE") + "元");
//        }

        // 图片展示
        String url = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW + list.get(position).optString("ICON_DOCTOR_PICTURE");
        Picasso.with(context).load(url).placeholder(R.drawable.default_head_doctor).into(holder.image);

        return convertView;
    }

    public String doctorId(int position) {
        return list.get(position - 1).optString("DOCTOR_ID");
    }

    /**
     * 存放控件
     */
    public final class ViewHolder {
        public TextView name;
        private TextView room;
        public TextView post;
        public TextView hospital;
        public TextView begoodat;
        public TextView price;
        public TextView number;
        private ImageView image;
        private TextView money;
        private LinearLayout ll_prime_cost;
    }

    public void onBoundData(List<JSONObject> datas) {
        if (list != null) {
            list.clear();
            list.addAll(datas);
            notifyDataSetChanged();
        }
    }
}
