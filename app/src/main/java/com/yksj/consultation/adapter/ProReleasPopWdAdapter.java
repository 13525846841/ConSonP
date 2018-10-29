package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.DocPlanMemberEntity;
import com.yksj.healthtalk.utils.HStringUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2016/12/16
 *
 */
public class ProReleasPopWdAdapter extends SimpleBaseAdapter<JSONObject> {

    private Context context;
    private List<JSONObject> list = new ArrayList<JSONObject>();

    public ProReleasPopWdAdapter(Context context,List<JSONObject> list) {
        super(context);
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public int getItemResource() {
        return  R.layout.item_pop_pro;
    }

    @Override
    public View getItemView(int position, View convertView, SimpleBaseAdapter<JSONObject>.ViewHolder holder) {
        TextView name = holder.getView(R.id.pop_place);

        if ("null".equals( datas.get(position).optString("CLASS_NAME"))) {
            name.setText("");
        } else {
            name.setText( datas.get(position).optString("CLASS_NAME"));
        }
        return convertView;
    }
}
