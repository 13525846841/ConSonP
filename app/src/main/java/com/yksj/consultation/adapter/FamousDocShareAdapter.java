package com.yksj.consultation.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.yksj.consultation.comm.ZoomImgeDialogFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.home.DoctorInfoActivity;
import com.yksj.consultation.son.home.FamousDoctorShareActivity;
import com.yksj.consultation.son.listener.OnListItemClick;
import com.yksj.consultation.son.listener.OnRecyclerClickListener;
import com.yksj.healthtalk.entity.HospitaFindTeamEntity;
import com.yksj.healthtalk.entity.ShareEntity;
import com.yksj.healthtalk.utils.TimeUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import static com.yksj.consultation.son.R.id.imageView;

/**
 * Created by hekl on 18/4/24.
 */

public class FamousDocShareAdapter extends SimpleBaseAdapter<ShareEntity.ResultBean> implements OnRecyclerClickListener {
    private String siteId="";
    private FamousDocCommentAdapter famousDocCommentAdapter;
    private TextView goodNumber;
    private FragmentManager supportFragmentManager;
    public FamousDocShareAdapter(Context context, List<ShareEntity.ResultBean> datas, FragmentManager supportFragmentManager) {
        super(context, datas);
        this.supportFragmentManager=supportFragmentManager;
    }

    private List<String> tagList=new ArrayList<>();
    private List<ShareEntity.ResultBean.CommentBean> commentList=null;
    private OnListItemClick onListItemClick;

    public void setOnListItemClick(OnListItemClick onListItemClick) {
        this.onListItemClick = onListItemClick;
    }

    @Override
    public int getItemResource() {
        return R.layout.item_share;
    }

    @Override
    public View getItemView(final int position, final View convertView, ViewHolder holder) {
        TextView docName= (TextView) convertView.findViewById(R.id.text_doc_name);
        TextView office= (TextView) convertView.findViewById(R.id.text_share_office);
        final TextView content= (TextView) convertView.findViewById(R.id.text_content);
        final TextView text_expand= (TextView) convertView.findViewById(R.id.text_expand);
        final ImageView share_doctor_head= (ImageView) convertView.findViewById(R.id.share_doctor_head);
        RecyclerView imgRecyclerView= (RecyclerView) convertView.findViewById(R.id.imFamousShare);
        TextView shareTime= (TextView) convertView.findViewById(R.id.text_share_time);
        goodNumber = (TextView) convertView.findViewById(R.id.text_good_number);
        final ImageView zan= (ImageView) convertView.findViewById(R.id.image_zan);
        final ImageView snsBtn= (ImageView) convertView.findViewById(R.id.snsBtn);
        RecyclerView commentRecycler = (RecyclerView) convertView.findViewById(R.id.commentRecycler);
        final ShareEntity.ResultBean resultBean = datas.get(position);
        List<ShareEntity.ResultBean.CommentBean> comment = datas.get(position).getComment();
        if (comment!=null) {
            commentList=comment;
            commentRecycler.setVisibility(View.VISIBLE);
            commentRecycler.setLayoutManager(new LinearLayoutManager(context));
            famousDocCommentAdapter = new FamousDocCommentAdapter(resultBean.getComment(), context);
            famousDocCommentAdapter.setmOnRecyclerClickListener(this);
            commentRecycler.setAdapter(famousDocCommentAdapter);
        }
        final List<ShareEntity.ResultBean.PictureBean> picture = resultBean.getPicture();
        if (picture.size()==0){imgRecyclerView.setVisibility(View.GONE);}else {imgRecyclerView.setVisibility(View.VISIBLE);}
        FamousShareImg famousShareImg = new FamousShareImg(resultBean.getPicture(), context);
        famousShareImg.setmOnRecyclerClickListener(new OnRecyclerClickListener() {
            @Override
            public void onRecyclerItemClickListener(int position, View itemView, int type) {
                ZoomImgeDialogFragment.show(ImageLoader.getInstance().getDownPathUri(picture.get(position).getPICTURE_PATH()),supportFragmentManager)
                ;
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
        imgRecyclerView.setLayoutManager(linearLayoutManager);
        imgRecyclerView.setAdapter(famousShareImg);

        Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(resultBean.getCLIENT_ICON_BACKGROUP())).error(R.drawable.default_head_doctor)
                .placeholder(R.drawable.default_head_doctor).dontAnimate().into(share_doctor_head);
//        Log.i("fff", "getItemView: "+ImageLoader.getInstance().getDownPathUri(resultBean.getCLIENT_ICON_BACKGROUP()));
        share_doctor_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DoctorInfoActivity.class);
                intent.putExtra("customer_id",resultBean.getCUSTOMER_ID());
                intent.putExtra(DoctorInfoActivity.SITE_ID,siteId);
                context.startActivity(intent);
            }
        });

        text_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (text_expand.getText().equals("展开")){
                    text_expand.setText("收起");
                    content.setMaxLines(Integer.MAX_VALUE);
                    tagList.add(position+"");
                } else {
                    text_expand.setText("展开");
                    content.setMaxLines(5);
                    tagList.remove(position+"");
                }
                }
        });
        //防止复用时展开收起内容时 错乱
        if (tagList.contains(position+"")){
            text_expand.setText("收起");
            content.setMaxLines(Integer.MAX_VALUE);
        }else {
            text_expand.setText("展开");
            content.setMaxLines(5);
        }
        //内容小于5行  将展开和收起隐藏
        if (content.getLineCount()<5) {
            text_expand.setVisibility(View.GONE);
        }else {
            text_expand.setVisibility(View.VISIBLE);
        }

        zan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (resultBean.getISLIKE()>0) {
                    ToastUtil.onShow(context,"已点过赞",1000);
                    return;
                }
                onListItemClick.setOnListItemClick(zan,position);
            }
        });
        snsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onListItemClick.setOnListItemClick(snsBtn,position);
            }
        });

        docName.setText(resultBean.getCUSTOMER_NICKNAME());
        office.setText(resultBean.getOFFICE_NAME());
//        content.setText("将阿里绝世独立飞机撒；垃圾分类；手机发了；健身房将阿里；开始减肥；老手机发了；啊绝世独立封口机阿斯利康的经费了；啊手机发了；啊手机发了看；结束了看飞机撒；垃圾分类；时间浪费；垮了；史蒂夫将阿里；手机发了；卡交水电费了看；就啊失联飞机啊算了；就发了；fjalsj阿里绝世独立飞机撒；垃圾分类；手机发了；健身房将阿里；开始减肥；老手机发了；啊绝世独立封口机阿斯利康的经费了；啊手机发了；啊手机发了看；结束了看飞机撒；垃圾分类；时间浪费；垮了；史蒂夫将阿里；手机发了；卡交水电费了看；就啊失联飞机啊算了；就发了；fjalsj阿里绝世独立飞机撒；垃圾分类；手机发了；健身房将阿里；开始减肥；老手机发了；啊绝世独立封口机阿斯利康的经费了；啊手机发了；啊手机发了看；结束了看飞机撒；垃圾分类；时间浪费；垮了；史蒂夫将阿里；手机发了；卡交水电费了看；就啊失联飞机啊算了；就发了；fjalsj阿里绝世独立飞机撒；垃圾分类；手机发了；健身房将阿里；开始减肥；老手机发了；啊绝世独立封口机阿斯利康的经费了；啊手机发了；啊手机发了看；结束了看飞机撒；垃圾分类；时间浪费；垮了；史蒂夫将阿里；手机发了；卡交水电费了看；就啊失联飞机啊算了；就发了；fjalsjf");
        content.setText(resultBean.getSHARE_CONTENT());
        shareTime.setText(TimeUtil.getTimeStr(resultBean.getPUBLIC_TIME()));
        goodNumber.setText(resultBean.getPRAISE_COUNT()+"");
//        imgLine.removeAllViews();
        float dimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 60, context.getResources().getDisplayMetrics());
//        if (picture.size()<0) {
//            imgLine.setVisibility(View.GONE);
//        }else {
//            for (int i = 0; i < picture.size(); i++) {
//                View inflate = View.inflate(context, R.layout.famous_doc_share_imageview, null);
//               ImageView docShareImg= (ImageView) inflate.findViewById(R.id.docShareImg);
//
//                imgLine.addView(docShareImg);
//                imgLine.setVisibility(View.VISIBLE);
//            }
//        }
        return convertView;
    }

    public void recyclerNotifyDataSetChanged(){
        famousDocCommentAdapter.notifyDataSetChanged();
    }

    public void zanNumber(String number){
        goodNumber.setText(number);
    }

    @Override
    public void onRecyclerItemClickListener(int position, View itemView, int type) {

    }


    class FamousShareImg extends BaseRecyclerAdapter<ShareEntity.ResultBean.PictureBean>{
        public FamousShareImg(List<ShareEntity.ResultBean.PictureBean> list, Context context) {
            super(list, context);
        }

        @Override
        public int returnLayout() {
            return R.layout.famous_doc_share_imageview;
        }

        @Override
        public void onBaseBindViewHolder(ViewHolder holder, int position) {
            ImageView docShareImg= (ImageView) holder.itemView.findViewById(R.id.docShareImg);
            Glide.with(context).load(ImageLoader.getInstance().getDownPathUri(list.get(position).getPICTURE_PATH())).centerCrop().error(R.drawable.plugin_camera_no_pictures)
                    .placeholder(R.drawable.plugin_camera_no_pictures).dontAnimate().into(docShareImg);
        }
    }

    public void setSiteId(String siteId) {
        this.siteId = siteId;
    }
}
