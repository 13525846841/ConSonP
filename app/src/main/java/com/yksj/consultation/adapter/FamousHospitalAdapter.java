package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.FamousHospitalEntity;

import java.util.List;

/**
 * Created by hekl on 18/5/14.
 */

public class FamousHospitalAdapter extends BaseRecyclerAdapter<FamousHospitalEntity.ResultBean> {

    private int hideLetterPosition=0;
    public FamousHospitalAdapter(List<FamousHospitalEntity.ResultBean> list, Context context) {
        super(list, context);
    }

    @Override
    public int returnLayout() {
        return R.layout.recycler_famous_hospital_item;
    }

    @Override
    public void onBaseBindViewHolder(ViewHolder holder, int position) {
        TextView letterTitle= (TextView) holder.itemView.findViewById(R.id.letterTitle);
        TextView content= (TextView) holder.itemView.findViewById(R.id.content);
        FamousHospitalEntity.ResultBean resultBean = list.get(position);
        letterTitle.setText(resultBean.getFIRST_LETTER().toUpperCase());
        content.setText(resultBean.getHOSPITAL_NAME());
//        if (position!=list.size()-1&&!resultBean.getFIRST_LETTER().equals(list.get(position+1).getFIRST_LETTER())){
//            hideLetterPosition=position+1;
//        }
//        if (hideLetterPosition==position) {
//            letterTitle.setVisibility(View.VISIBLE);
//        }else if (hideLetterPosition+1==position){
//            letterTitle.setVisibility(View.INVISIBLE);
//        }else {
//            letterTitle.setVisibility(View.GONE);
//        }

        if (position!=0&&!resultBean.getFIRST_LETTER().equals(list.get(position-1).getFIRST_LETTER())){
            letterTitle.setVisibility(View.VISIBLE);
        }else {
            if (position==0) {
                letterTitle.setVisibility(View.VISIBLE);
            }else {
                letterTitle.setVisibility(View.GONE);
            }
        }
        if (position!=0&&position!=1&&list.get(position).getFIRST_LETTER().equals(list.get(position-1).getFIRST_LETTER())&&!list.get(position).getFIRST_LETTER().equals(list.get(position-2).getFIRST_LETTER())){
            letterTitle.setVisibility(View.INVISIBLE);
        }
        if (position==1&&list.get(position).getFIRST_LETTER().equals(list.get(position-1).getFIRST_LETTER())){
            letterTitle.setVisibility(View.INVISIBLE);
        }
    }
}
