package com.yksj.consultation.adapter;


import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.yksj.consultation.comm.ImageGalleryActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.photo.utils.GalleryActivity;
import com.yksj.healthtalk.utils.Bimp;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ImageItem;
import com.yksj.healthtalk.utils.TimeUtil;
import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by ${chen} on 2016/11/25.
 */
public class SeePlanAdapter extends BaseAdapter {
    private Context context;
    private LayoutInflater mIflatter;
    private List<JSONObject> list = null;
    public static final String IMAGEKEY = "image";//图片标志
    private ImageLoader mInstance;
    private DisplayImageOptions mOptions;//画廊异步读取操作

    public SeePlanAdapter(Context context, List<JSONObject> list) {
        this.list = list;
        this.context = context;
        this.mIflatter = LayoutInflater.from(context);
        mInstance = ImageLoader.getInstance();
        mOptions = DefaultConfigurationFactory.createApplyPicDisplayImageOptions(context);
        list = new ArrayList<>();
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
            convertView = mIflatter.inflate(R.layout.item_content, null);
            holder = new ViewHolder();
            holder.title = (TextView) convertView.findViewById(R.id.text_name);
            holder.plan_content = (TextView) convertView.findViewById(R.id.text_content);
            holder.time = (TextView) convertView.findViewById(R.id.text_time);
            holder.imageView = (ImageView) convertView.findViewById(R.id.image);
            holder.images = (LinearLayout) convertView.findViewById(R.id.item_images_plan);
            holder.caseImgLayout = (LinearLayout) convertView.findViewById(R.id.fgt_case_img_layout);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
            holder.caseImgLayout.removeAllViews();
        }
        holder.imagesList = new ArrayList<ImageItem>();
        Bimp.dataMap.put(IMAGEKEY, holder.imagesList);
        Bimp.imgMaxs.put(IMAGEKEY, 12);


//        if (holder.imagesList.size() > 0) {//
//            holder.images.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f));
//        } else//将加号放在中间
//            holder.images.setLayoutParams(new LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0f));

//        for (int i = 0; i < holder.imagesList.size(); i++) {
//            final int index = i;
//            MessageImageView image = new MessageImageView(context);
//            image.setLayoutParams(new ViewGroup.LayoutParams(DensityUtils.dip2px(context, 78), DensityUtils.dip2px(context, 78)));
//            image.setPadding(10, 0, 10, 0);
//            if (holder.imagesList.get(i).isNetPic) {
//                mInstance.displayImage(holder.imagesList.get(i).getThumbnailPath(), image.getImage());
//            } else {
//                image.setImageBitmap(holder.imagesList.get(i).getBitmap());
//            }
//            image.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, GalleryActivity.class);
//                    intent.putExtra("key", IMAGEKEY);
//                    intent.putExtra("position", "1");
//                    intent.putExtra("ID", index);
//                    ((SeePlanActivity)context).startActivityForResult(intent, 100);
//                }
//            });
//
//            holder.images.addView(image);
//        }
//        //图片展示
//        String url= HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW+list.get(position).optString("PICS");
//        Picasso.with(context).load(url).placeholder(R.drawable.waterfall_default).into(holder.imageView);
        if (!HStringUtil.isEmpty(list.get(position).optString("CUSTOMER_REMARK"))) {
            holder.title.setText(list.get(position).optString("CUSTOMER_REMARK"));
        }else{
            holder.title.setText(list.get(position).optString("CUSTOMER_NICKNAME"));
        }


        if (!HStringUtil.isEmpty(list.get(position).optString("RECORD_CONTENT"))) {
            holder.plan_content.setText(list.get(position).optString("RECORD_CONTENT"));
        }

        if (!HStringUtil.isEmpty(list.get(position).optString("CREATE_TIME"))) {
            holder.time.setText(TimeUtil.format(list.get(position).optString("CREATE_TIME")));
        }

        if (HStringUtil.isEmpty(list.get(position).optString("PICS"))||HStringUtil.isEmpty(list.get(position).optString("ICONS"))) {
            holder.caseImgLayout.setVisibility(View.GONE);
        }


        List<String> smallUrls = null;//小图片
        List<String> bigUrls = null;//大图片
        String pics = list.get(position).optString("PICS");
        String icons = list.get(position).optString("ICONS");
        smallUrls = Arrays.asList(pics.split(","));
        bigUrls = Arrays.asList(icons.split(","));


        String[] arrays = null;//病历图片
        //图片key集合
        arrays = new String[bigUrls.size()];
        for (int t = 0; t < bigUrls.size(); t++) {
            arrays[t] = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW+smallUrls.get(t);
          //  arrays[t] = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW+bigUrls.get(t);
        }

        for (int i = 0; i < smallUrls.size(); i++) {
            final int index = i;
            View view = mIflatter.inflate(R.layout.aty_applyform_gallery, holder.caseImgLayout, false);
            ImageView img = (ImageView) view.findViewById(R.id.image_illpic);
            String url = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE_NEW+bigUrls.get(i);

            Picasso.with(context).load(url).placeholder(R.drawable.waterfall_default).into(img);
            final String[] finalArray = arrays;
            img.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ImageGalleryActivity.class);
                    intent.putExtra(ImageGalleryActivity.URLS_KEY,finalArray);
                    intent.putExtra(ImageGalleryActivity.TYPE_KEY, 0);
                    intent.putExtra("type", 1);// 0,1单个,多个
                    intent.putExtra("position", index);
                    context.startActivity(intent);
                }
            });
            holder.caseImgLayout.addView(view);
        }

        return convertView;
    }


    public final class ViewHolder {
        public TextView title;
        public TextView plan_content;
        public TextView time;
        public ImageView imageView;
        private LinearLayout images;//图片线性布局
        private LinearLayout caseImgLayout;//
        ArrayList<ImageItem> imagesList;//图片list类

    }

    public void onBoundData(List<JSONObject> datas) {
        if (list != null) {
            list.clear();
            list.addAll(datas);
            notifyDataSetChanged();
        }
    }
}
