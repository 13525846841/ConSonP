package com.yksj.consultation.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.member.FlowMassageActivity;

import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2016/12/19.
 */
public class SelExpertListAdapter extends BaseAdapter {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Context context;
    private List<JSONObject> list = new ArrayList<JSONObject>();
    private ImageLoader instance;
    private DisplayImageOptions mOptions;

    public SelExpertListAdapter(Context context,List<JSONObject> list) {
        this.list = list;
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
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_sel_expert, null);
            holder = new ViewHolder();
            holder.name = (TextView) convertView.findViewById(R.id.select_expert_list_item_name);
            holder.post = (TextView) convertView.findViewById(R.id.select_expert_list_item_doctitle);
            holder.hospital = (TextView) convertView.findViewById(R.id.select_expert_list_item_hospital);
            holder.begoodat = (TextView) convertView.findViewById(R.id.select_expert_list_item_spetical);
            holder.room = (TextView) convertView.findViewById(R.id.select_expert_list_item_docroom);
            holder.image = (ImageView) convertView.findViewById(R.id.select_expert_list_item_headicon);
            holder.price = (TextView) convertView.findViewById(R.id.select_expert_list_item_price);
            holder.number = (TextView) convertView.findViewById(R.id.select_expert_list_item_num);
            holder.btn = (Button) convertView.findViewById(R.id.select_expert_list_item_select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FlowMassageActivity.class);
                context.startActivity(intent);
            }
        });

        if ("null".equals(list.get(position).optString("DOCTOR_REAL_NAME"))) {
            holder.name.setText("");
        } else {
            holder.name.setText(list.get(position).optString("DOCTOR_REAL_NAME"));
        }

        if ("null".equals(list.get(position).optString("TITLE_NAME"))) {
            holder.post.setText("");
        } else {
            holder.post.setText(list.get(position).optString("TITLE_NAME"));
        }

        if ("null".equals(list.get(position).optString("OFFICE_NAME"))) {
            holder.room.setText("");
        } else {
            holder.room.setText(list.get(position).optString("OFFICE_NAME"));
        }

        if ("null".equals(list.get(position).optString("DOCTOR_HOSPITAL"))) {
            holder.hospital.setText("");
        } else {
            holder.hospital.setText(list.get(position).optString("DOCTOR_HOSPITAL"));
        }
        if ("null".equals(list.get(position).optString("DOCTOR_SPECIALLY"))) {
            holder.begoodat.setText("");
        } else {
            holder.begoodat.setText(list.get(position).optString("DOCTOR_SPECIALLY"));
        }

        if ("null".equals(list.get(position).optString("SERVICE_PRICE"))) {
            holder.price.setText("");
        } else {
            holder.price.setText(list.get(position).optString("SERVICE_PRICE") + "元");
        }

        if (list.get(position).optInt("SERVICE_SURPLUS")>0){
            holder.number.setText("剩余"+list.get(position).optString("SERVICE_SURPLUS") +"个名额");
        }else{
            holder.number.setText("服务名额已满");
            holder.number.setTextColor(context.getResources().getColor(R.color.red_text));
        }
        // 图片展示
        instance.displayImage("", list.get(position).optString("HEAD_PORTRAIT_ICON"), holder.image);
        return convertView;
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
        private Button btn;
    }
    public void onBoundData(List<JSONObject> datas) {
        if (list != null) {
            list.clear();
            list.addAll(datas);
            notifyDataSetChanged();
        }
    }
}
