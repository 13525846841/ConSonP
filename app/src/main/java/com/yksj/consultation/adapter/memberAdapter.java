package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.DocPlanEntity;
import com.yksj.healthtalk.entity.DocPlanMemberEntity;
import com.yksj.healthtalk.utils.HStringUtil;

import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

/**
 * Created by tidus on 2016/11/10.
 */
public class memberAdapter  extends SimpleBaseAdapter<DocPlanMemberEntity>{
    private LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    private Context context;
    private int CREATOR = 0;//创建者
    private ImageLoader instance;
    private DisplayImageOptions mOptions;
    public memberAdapter( Context context) {
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
        return R.layout.item_member;
    }

    @Override
    public View getItemView(int position, View convertView, SimpleBaseAdapter<DocPlanMemberEntity>.ViewHolder holder) {
        TextView name = holder.getView(R.id.tv_member_name);
        TextView sex = holder.getView(R.id.tv_member_sex);
        ImageView image = holder.getView(R.id.image_head);
        ImageView boolean_setup = holder.getView(R.id.boolean_setup);
        DocPlanMemberEntity dpmEntity = datas.get(position);
        //name.setText(dpmEntity.getName());
        if ("W".equals(dpmEntity.getSex())){
            sex.setText("男");
        }else if ("M".equals(dpmEntity.getSex())){
            sex.setText("女");
        }

        if (HStringUtil.isEmpty(dpmEntity.getCustomer_remark())){
            name.setText(dpmEntity.getName());
        }else {
            name.setText(dpmEntity.getCustomer_remark());
        }

        String create_id = dpmEntity.getCREATOR_ID();
        String customer_id = dpmEntity.getCustomer_id();

        if (create_id.equals(customer_id)){
            boolean_setup.setVisibility(View.VISIBLE);
        }else
        boolean_setup.setVisibility(View.INVISIBLE);
        // 图片展示
        instance.displayImage(dpmEntity.getImage().toString(), image, mOptions);
        return convertView;
    }

}
