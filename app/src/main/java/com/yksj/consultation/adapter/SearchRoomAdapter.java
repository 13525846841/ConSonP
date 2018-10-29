package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.HStringUtil;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ${chen} on 2016/12/31.
 */
public class SearchRoomAdapter extends SimpleBaseAdapter<JSONObject> {
    private Context context;
    private List<JSONObject> list = new ArrayList<JSONObject>();

    public SearchRoomAdapter(Context context) {
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
        return R.layout.item_pop_pro;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        TextView name = holder.getView(R.id.pop_place);

        if (!HStringUtil.isEmpty(datas.get(position).optString("OFFICE_NAME"))){
            name.setText( datas.get(position).optString("OFFICE_NAME"));
        }
        if (!HStringUtil.isEmpty(datas.get(position).optString("WORDS"))){
            name.setText( datas.get(position).optString("WORDS"));
        }


        return convertView;
    }
}
