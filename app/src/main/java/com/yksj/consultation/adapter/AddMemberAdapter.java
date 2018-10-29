package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.AddMemberEntity;

import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

/**
 * Created by ${chen} on 2016/12/8.
 */
public class AddMemberAdapter extends SimpleBaseAdapter<AddMemberEntity> {
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Context context;
    private ImageLoader instance;
    private DisplayImageOptions mOptions;
    private boolean isSelector = false;
    public AddMemberEntity dpmEntity;

    public AddMemberAdapter(Context context) {
        super(context);
        this.context = context;
        this.mInflater = LayoutInflater.from(context);
        instance = ImageLoader.getInstance();
        mOptions = DefaultConfigurationFactory.createDynamicMesDisplayImageOptions(context);
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public int getItemResource() {
        return R.layout.item_addmember;
    }

    @Override
    public View getItemView(int position, View convertView, SimpleBaseAdapter<AddMemberEntity>.ViewHolder holder) {
        TextView name = holder.getView(R.id.tv_member_name);
        TextView sex = holder.getView(R.id.tv_member_sex);
        ImageView image = holder.getView(R.id.image_head);

        final ImageView select = holder.getView(R.id.image_select);
        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSelector == true) {
                    isSelector = false;
                    select.setSelected(true);
                } else {
                    isSelector = true;
                    select.setSelected(false);
                }
            }
        });
        dpmEntity = datas.get(position);
        name.setText(dpmEntity.getName());
        if ("w".equals(dpmEntity.getSex())) {
            sex.setText("男");
        } else {
            sex.setText("女");
        }
        // 图片展示
        instance.displayImage(dpmEntity.getImage().toString(), image, mOptions);
        return convertView;
    }


    public String getChildrenId() {
        if (isSelector) {
            return "";
        } else {
            if (dpmEntity != null) {
                return dpmEntity.getCustomer_id();
            } else {
                return "";
            }

        }

    }
}
