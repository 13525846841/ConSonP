package com.yksj.consultation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.dossier.AtyDossierDetails;
import com.yksj.healthtalk.utils.TimeUtil;

import java.util.Iterator;
import java.util.Map;

/**
 * Created by zheng on 2015/7/8.
 */
public class LookHistoryAdapter extends SimpleBaseAdapter<Map<String ,String>>{

    public LookHistoryAdapter(Context context) {
        super(context);
    }

    @Override
    public int getItemResource() {
        return R.layout.look_list_itme;
    }

    @Override
    public View getItemView(int position, View convertView, ViewHolder holder) {
        final Map<String ,String> map = datas.get(position);
        TextView mName= (TextView) holder.getView(R.id.Look_name);
		TextView mTime= (TextView) holder.getView(R.id.look_history_time);
        RelativeLayout mlayout = holder.getView(R.id.dossier_layout);
        mName.setText(map.get("MEDICAL_NAME"));
        Iterator keys = map.keySet().iterator();
        while(keys.hasNext()){
            String key = (String)keys.next();
            if("SHARE_TIME".equals(key)){
                mTime.setText(TimeUtil.format(map.get("SHARE_TIME")));
            }else if("RELATION_TIME".equals(key)){
                mTime.setText(TimeUtil.format(map.get("RELATION_TIME")));
            }
        }
        mlayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, AtyDossierDetails.class);
                intent.putExtra("TITLE", map.get("MEDICAL_NAME"));
                intent.putExtra("ID", map.get("MEDICAL_RECORD_ID"));
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
