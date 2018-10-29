package com.yksj.consultation.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.DynamicMessageListEntity;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.views.RoundImageView;

import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

import java.util.HashMap;

/**
 * Created by ${chen} on 2016/11/14.
 */
public class HotActivityAdapter  extends SimpleBaseAdapter<DynamicMessageListEntity>{
    public LayoutInflater mInflater;//得到一个LayoutInfalter对象用来导入布局
    public Context context;
    private ImageLoader instance;
    private DisplayImageOptions mOptions;
    private int type;
    HashMap<String, String> mAlreadyRead;

    public HotActivityAdapter(Context context){
        super(context);
        this.context = context;
    }
    public HotActivityAdapter( Context context,int type, HashMap<String, String> mAlreadyRead) {
        super(context);
        this.context = context;
        instance = ImageLoader.getInstance();
        this.type = type;
        this.mAlreadyRead = mAlreadyRead;
        mOptions = DefaultConfigurationFactory.createDynamicMesDisplayImageOptions(context);
    }
    @Override
    public int getCount() {
        return datas.size();
    }

//    @Override
//    public Object getItem(int position) {
//        return null;
//    }
//    @Override
//    public long getItemId(int position) {
//        return 0;
//    }

    @Override
    public int getItemResource() {
        return R.layout.item_hot_news;
    }

    @Override
    public View getItemView(final int position, final View convertView,
                            com.yksj.consultation.adapter.SimpleBaseAdapter.ViewHolder holder) {
        // final JSONObject object = (JSONObject) getItem(position);
        // 时间显示布局
        RelativeLayout layout1 = (RelativeLayout) holder.getView(R.id.rl_view);// 专家
        RelativeLayout layout2 = (RelativeLayout) holder.getView(R.id.rl_view2);// 会诊医生、患者
        // 消息题目
        TextView messtitle = (TextView) holder.getView(R.id.tv_messtitle);
        // 专家时间
        TextView time = (TextView) holder.getView(R.id.tv_time);
        TextView time2 = (TextView) holder.getView(R.id.tv_time3);
        // 消息缩略图
        RoundImageView messagePic = (RoundImageView) holder.getView(R.id.dynimic_image);
        // 审核标志
        TextView examine = (TextView) holder.getView(R.id.tv_examine);
        // 编辑标志
        ImageView imgeEdit = (ImageView) holder.getView(R.id.health_topic_item_follow);
        final DynamicMessageListEntity entity = datas.get(position);
        // 设置消息题目
        messtitle.setText(entity.getInfoName().toString());
        String id = entity.getInfoId() + "";
        if (mAlreadyRead != null) {
            if (type != 2 && mAlreadyRead.containsKey(id)) {
                messtitle.setTextColor(context.getResources().getColor(R.color.news_readed_color));
            } else {
                messtitle.setTextColor(Color.BLACK);
            }
        } else {
            messtitle.setTextColor(Color.BLACK);
        }

        // 图片展示
        instance.displayImage(entity.getInfoPicture().toString(), messagePic, mOptions);
        // 端判断
        switch (type) {
            case 0:
                layout1.setVisibility(View.GONE);
                layout2.setVisibility(View.VISIBLE);
                time2.setText(entity.getPublishTime());
                break;
            case 1:
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
                examine.setVisibility(View.GONE);
                time.setText(entity.getPublishTime());
                if (!(String.valueOf(entity.getCustomerId()).equals(SmartFoxClient.getLoginUserId()))) {
                    imgeEdit.setVisibility(View.GONE);
                }
                break;
            case 2:
                layout1.setVisibility(View.VISIBLE);
                layout2.setVisibility(View.GONE);
                examine.setVisibility(View.VISIBLE);
                imgeEdit.setVisibility(View.VISIBLE);
                time.setText(entity.getPublishTime());
                Log.i("time++++++","time");
                if ("30".equals(entity.getPublishTime())) {
                    examine.setText("审核失败");
                } else if ("10".equals(entity.getInfoStaus().toString())) {
                    examine.setText("审核中");
                } else if ("20".equals(entity.getInfoStaus().toString())) {
                    examine.setText("审核成功");
                }
                break;
        }
//        imgeEdit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
////                Intent intent = new Intent(maActivity, DAtyConsultMessageEdit.class);
////                intent.putExtra("flag", 1);
////                intent.putExtra("infoId", entity.getInfoId());
////                maActivity.startActivityForResult(intent, 100);
//            }
//        });
        convertView.setBackgroundColor(Color.WHITE);
        return convertView;
    }
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder;
//        if (convertView == null) {
//            convertView = mInflater.inflate(R.layout.item_hot_news, null);
//            holder = new ViewHolder();
//            holder.name = (TextView) convertView.findViewById(R.id.tv_messtitle);
//            holder.headView = (ImageView) convertView.findViewById(R.id.dynimic_image);
//            holder.time = (TextView) convertView.findViewById(R.id.tv_time3);
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//        return convertView;
//    }
//    /**
//     * 存放控件
//     */
//    public final class ViewHolder {
//        public TextView name;
//        public ImageView headView;
//        public TextView time;
//    }
}
