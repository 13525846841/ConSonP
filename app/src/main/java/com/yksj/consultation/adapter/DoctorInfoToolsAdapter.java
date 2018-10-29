package com.yksj.consultation.adapter;

import android.content.Context;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.DoctorInfoEntity;

import java.util.List;

/**
 * Created by hekl on 18/5/10.
 */

public class DoctorInfoToolsAdapter extends BaseRecyclerAdapter<DoctorInfoEntity.ResultBean.ToolsBean> {
    public DoctorInfoToolsAdapter(List<DoctorInfoEntity.ResultBean.ToolsBean> list, Context context) {
        super(list, context);
    }

    @Override
    public int returnLayout() {
        return R.layout.recycler_doctor_info_tools_item;
    }

    @Override
    public void onBaseBindViewHolder(ViewHolder holder, int position) {
        TextView toolsName= (TextView) holder.itemView.findViewById(R.id.toolsName);
        DoctorInfoEntity.ResultBean.ToolsBean toolsBean = list.get(position);
        toolsName.setText(toolsBean.getTOOL_NAME());
    }
}
