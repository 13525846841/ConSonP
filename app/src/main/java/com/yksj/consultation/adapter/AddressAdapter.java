package com.yksj.consultation.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.main.AddNewAddressActivity;

import org.json.JSONObject;

/**
 * Created by ${chen} on 2017/8/10.
 */
public class AddressAdapter extends SimpleBaseAdapter<JSONObject>{
    private Context context;
    private MyOnClickListener itemsOnClick;
    public AddressAdapter(Context context,MyOnClickListener itemsOnClick) {

        super(context);
        this.itemsOnClick = itemsOnClick;
        this.context = context;
    }

    @Override
    public int getCount() {
        return datas.size();

    }

    @Override
    public int getItemResource() {
        return R.layout.item_address;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {

        TextView tvName = holder.getView(R.id.tv_name);
        TextView nameNumber = holder.getView(R.id.tv_name_number);
        TextView listAddress = holder.getView(R.id.list_address);
        TextView editor = holder.getView(R.id.editor);
        TextView deleteAddress = holder.getView(R.id.delete_address);
        ImageView imageView = holder.getView(R.id.icon_address);
        LinearLayout addLayout = holder.getView(R.id.ll_icon_address);

        tvName.setText(datas.get(position).optString("CUSTOMER_NAME"));
        nameNumber.setText(datas.get(position).optString("CUSTOMER_PHONE"));
        listAddress.setText(datas.get(position).optString("CUSTOMER_REMARK"));

        if (0==position){
            imageView.setImageResource(R.drawable.trueadd);
        }else {
            imageView.setImageResource(R.drawable.falseadd);
        }


        editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent  = new Intent(context, AddNewAddressActivity.class);
                intent.putExtra("address_id",datas.get(position).optString("ADDRESS_ID"));
                context.startActivity(intent);
            }
        });

        final View view = convertView;
        final int pos = position;
        final int id = R.id.delete_address;
        final int iconId = R.id.icon_address;

        deleteAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsOnClick.onDeleteClick(view, pos, id);
            }
        });
        addLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemsOnClick.onDeleteClick(view, pos, iconId);
            }
        });

        return convertView;
    }

    public String addressId(int position){
        return datas.get(position).optString("ADDRESS_ID");
    }
    public interface MyOnClickListener extends View.OnClickListener {
        void onDeleteClick(View view,int position ,int id);
    }
}
