//package com.yksj.consultation.son.smallone.adapter;
//
//import android.annotation.TargetApi;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.graphics.Color;
//import android.graphics.drawable.AnimationDrawable;
//import android.graphics.drawable.BitmapDrawable;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.Build;
//import android.support.v4.app.DialogFragment;
//import android.text.Html;
//import android.text.Spannable;
//import android.text.SpannableString;
//import android.text.SpannableStringBuilder;
//import android.text.Spanned;
//import android.text.TextPaint;
//import android.text.TextUtils;
//import android.text.method.LinkMovementMethod;
//import android.text.style.ClickableSpan;
//import android.text.style.ForegroundColorSpan;
//import android.text.style.StrikethroughSpan;
//import android.text.style.UnderlineSpan;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.animation.Animation;
//import android.view.animation.AnimationUtils;
//import android.widget.BaseAdapter;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import com.dmsj.newask.Views.CircleImageView;
//import com.squareup.picasso.Callback;
//import com.squareup.picasso.Picasso;
//import com.yksj.consultation.comm.BaseFragmentActivity;
//import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
//import com.yksj.consultation.comm.ImageGalleryActivity;
//import com.yksj.consultation.comm.LodingFragmentDialogB;
//import com.yksj.consultation.comm.SingleBtnFragmentDialog;
//import com.yksj.consultation.son.R;
//import com.yksj.consultation.son.app.HTalkApplication;
//import com.yksj.consultation.son.consultation.main.AtyConsultMain;
//import com.yksj.consultation.son.smallone.ChatActivityB;
//import com.yksj.consultation.son.smallone.TyJsonObject;
//import com.yksj.consultation.son.smallone.bean.CardType;
//import com.yksj.consultation.son.smallone.event.TalkEvent;
//import com.yksj.consultation.son.smallone.manager.LoginServiceManegerB;
//import com.yksj.consultation.son.smallone.service.MusicService;
//import com.yksj.consultation.son.smallone.ui.CommonWebUIActivity;
//import com.yksj.consultation.son.smallone.ui.DisplyContentActivity;
//import com.yksj.consultation.son.smallone.ui.DisplyContentPicActivity;
//import com.yksj.consultation.son.smallone.ui.DoctorChatMyinformation;
//import com.yksj.consultation.son.smallone.ui.InternetVideoDemo;
//import com.yksj.consultation.son.smallone.ui.OrderUserInfoActivity;
//import com.yksj.consultation.son.smallone.ui.SoundToTextActivity;
//import com.yksj.healthtalk.entity.MessageEntity;
//import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
//import com.yksj.healthtalk.net.http.HttpRestClient;
//import com.yksj.healthtalk.net.http.HttpRestClientB;
//import com.yksj.healthtalk.net.http.JsonHttpResponseHandler;
//import com.yksj.healthtalk.net.http.RequestParams;
//import com.yksj.healthtalk.utils.ActivityUtils;
//import com.yksj.healthtalk.utils.AnimaUtils;
//import com.yksj.healthtalk.utils.HStringUtil;
//import com.yksj.healthtalk.utils.SharePreUtils;
//import com.yksj.healthtalk.utils.SpeechUtils;
//import com.yksj.healthtalk.utils.SystemUtils;
//import com.yksj.healthtalk.utils.TimeUtil;
//import com.yksj.healthtalk.utils.ToastUtil;
//import com.yksj.healthtalk.utils.UnitUtil;
//import com.yksj.healthtalk.utils.ViewFinder;
//import com.yksj.healthtalk.utils.WeakHandler;
//import com.yksj.healthtalk.utils.WheelUtils;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//import de.greenrobot.event.EventBus;
//
///**
// * Created by HEKL on 16/5/11.
// * Used for
// */
//
//public class ChatInfoAdapter extends BaseAdapter implements View.OnClickListener {
//    private final LayoutInflater mInflater;
//    private List<MessageEntity> mList = new ArrayList<MessageEntity>();//列表数据
//    private boolean isReceive;//true 表示接受的最后一条内容 false 表示下拉刷新 或者别的
//    private BaseFragmentActivity mActivity;
//    private boolean isOpenSound = false;
//    private final SpeechUtils mSpeechUtils;
//    private ListView mListView;
//    public static final int ITEM_LEFT_LAYOUT = 0;//左边对话框
//    public static final int ITEM_RIGHTT_LAYOUT = 1;//右边对话框
//    public boolean isSoftword = true;//是否是键盘输入的  true 表示键盘输入 false 表示语音输入
//    private final Resources mResources;
//    private final int mChatTCommonColor;
//    private final int mChatTWhiteColor;
//    private boolean isClick = true;//表示是否可以点击btn  (历史只能看,不能点击)
//    private int[] mZdrawables = new int[]{R.drawable.chat_item_title_1, R.drawable.chat_item_title_2, R.drawable.chat_item_title_3};
//    private int[] mZdraColor = new int[]{R.color.mzdracolor_1, R.color.mzdracolor_2, R.color.mzdracolor_3};
//    private LodingFragmentDialogB mLodingFragmentDialog;
//    AnimationDrawable animation;
//    WeakHandler mHandler = new WeakHandler();
//
//    public ChatInfoAdapter(BaseFragmentActivity activity) {
//        mActivity = activity;
//        mInflater = LayoutInflater.from(activity);
//        mResources = activity.getResources();
//        mChatTCommonColor = mResources.getColor(R.color.imput_color);
//        mChatTWhiteColor = mResources.getColor(R.color.white);
//        mSpeechUtils = new SpeechUtils(mActivity);
//        mGreenDrawable = mResources.getDrawable(R.drawable.item_chat_l_green_bg);
//        mWhriteDrawable = mResources.getDrawable(R.drawable.item_chat_l_white_bg);
//    }
//
//    /**
//     * 上拉加载历史
//     *
//     * @param list
//     */
//    public void onPullDataChange(List<MessageEntity> list) {
//        mList.addAll(0, list);
//        isReceive = false;
//        notifyDataSetChanged();
//    }
//
//    /**
//     * 添加单条消息
//     *
//     * @param messageEntity
//     * @return
//     */
//    public MessageEntity onDataChange(MessageEntity messageEntity) {
//        messageEntity.isAdd = true;
//        if ("-1".equals(messageEntity.getId())) {
//            mList.add(messageEntity);
//        } else {
////            long id = ChatUserHelper.getInstance(HApplication.getApplication()).insertChatMessageDoctor(messageEntity);
////            messageEntity.setId(String.valueOf(id));
//            mList.add(messageEntity);
//        }
//        isReceive = !messageEntity.isSendFlag();
//        isOpenSound = SharePreUtils.getSoundsBoolean(mActivity);
//        notifyDataSetChanged();
//
//
//        return messageEntity;
//    }
//
//    public void setListView(ListView mListView) {
//        this.mListView = mListView;
//    }
//
//    @Override
//    public int getCount() {
//        return mList.size();
//    }
//
//    @Override
//    public MessageEntity getItem(int position) {
//        return mList.get(position);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        final MessageEntity entity = getItem(position);
//        int type = getItemViewType(position);
//        long lastTime = 0;
//        LeftViewHolder leftViewHolder = null;
//        RightViewHolder rightViewHolder;
//        if (convertView == null || convertView.getTag() == null) {
//            if (position > 0) {
//                final MessageEntity entity2 = mList.get(--position);
//                lastTime = entity2.getDate();
//            }
//            switch (type) {
//                case ITEM_LEFT_LAYOUT:
//                    leftViewHolder = new LeftViewHolder();
//                    convertView = mInflater.inflate(R.layout.item_chat_l, null);//左侧item
//                    // 得到TextView控件对象
//                    leftViewHolder.chat_head = (CircleImageView) convertView.findViewById(R.id.chat_head);//头像
//                    leftViewHolder.textView_sound = (TextView) convertView.findViewById(R.id.chat_sound);
//                    leftViewHolder.mViewGroup = ((LinearLayout) convertView.findViewById(R.id.group));
//                    leftViewHolder.timeTextV = (TextView) convertView.findViewById(R.id.chat_time);
//                    leftViewHolder.imageViewSound = (ImageView) convertView.findViewById(R.id.imageview_sound_src);
//                    leftViewHolder.imageViewDianDian = (ImageView) convertView.findViewById(R.id.chat_diandiandian);
//                    leftViewHolder.contentTextV = (TextView) convertView.findViewById(R.id.chat_content);
//                    leftViewHolder.contentTextV.setLineSpacing(5f, 1.1f);
//                    leftViewHolder.headerImageV = (Button) convertView.findViewById(R.id.header_image);
//                    leftViewHolder.mContent_layout = (LinearLayout) convertView.findViewById(R.id.content_layout);
//                    leftViewHolder.timeView = convertView.findViewById(R.id.time_view);
//                    leftViewHolder.imageView_chat = (ImageView) convertView.findViewById(R.id.chat_image_l);
//                    onBoundData(leftViewHolder, entity, lastTime, position);
//                    convertView.setTag(leftViewHolder);
//                    break;
//                case ITEM_RIGHTT_LAYOUT:
//                    rightViewHolder = new RightViewHolder();
//                    convertView = mInflater.inflate(R.layout.item_chat_r, null);
//                    rightViewHolder.timeTextV = (TextView) convertView.findViewById(R.id.chat_time);
//                    rightViewHolder.contentTextV = (TextView) convertView.findViewById(R.id.chat_content);
//                    rightViewHolder.contentTextV.setLineSpacing(5f, 1.1f);
//                    rightViewHolder.timeView = convertView.findViewById(R.id.time_view);
//                    rightViewHolder.imageView = (ImageView) convertView.findViewById(R.id.chat_image);
//                    onBoundData(rightViewHolder, entity, lastTime, position);
//                    convertView.setTag(rightViewHolder);
//
//                    break;
//            }
//        } else {
//            if (position > 0) {
//                final MessageEntity entity2 = mList.get(position - 1);
//                lastTime = entity2.getDate();
//            }
//            switch (type) {
//                case ITEM_LEFT_LAYOUT:
//                    leftViewHolder = (LeftViewHolder) convertView.getTag();
//                    onBoundData(leftViewHolder, entity, lastTime, position);
//                    break;
//                case ITEM_RIGHTT_LAYOUT:
//                    rightViewHolder = (RightViewHolder) convertView.getTag();
//                    onBoundData(rightViewHolder, entity, lastTime, position);
//                    break;
//            }
//        }
//        if (position == mList.size() - 1 && isReceive && isSoftword && isOpenSound) {
//            String content;
//            try {
//                content = entity.getContentJsonArray().optJSONObject(1).optString("talkname", "");
//            } catch (Exception e) {
//                content = "";
//            }
//
//
//            String text;
//            if ("".equals(content)) {
//                text = leftViewHolder.contentTextV.getText().toString();
////                mSpeechUtils.startPeed(leftViewHolder.contentTextV.getText().toString(), null);
//            } else {
//                text = content;
////                mSpeechUtils.startPeed(content, null);
//            }
//            if (!TextUtils.isEmpty(text)) {
//                MusicService.stop(mActivity);
//                mSpeechUtils.startPeed(text, null);
//            }
//            isReceive = false;
//        }
//
//        return convertView;
//    }
//
//    /**
//     * 绑定数据
//     *
//     * @param o
//     * @param entity
//     * @param lastTime
//     * @param position
//     */
//    private void onBoundData(final Object o, MessageEntity entity, long lastTime, int position) {
//        if (o instanceof LeftViewHolder) {
//            setChatTime(((LeftViewHolder) o).timeTextV, lastTime, entity.getDate(), ((LeftViewHolder) o).timeView);
//            onParseContent(entity, (LeftViewHolder) o, position);
//        } else if (o instanceof RightViewHolder) {
//
//            setChatTime(((RightViewHolder) o).timeTextV, lastTime, entity.getDate(), ((RightViewHolder) o).timeView);
//            if (entity.getType() == 1) {
//                ((RightViewHolder) o).contentTextV.setVisibility(View.GONE);
//                ((RightViewHolder) o).imageView.setVisibility(View.VISIBLE);
//                String[] urls = entity.getContent().split("&");
//
//                if (!TextUtils.isEmpty(urls[0])) {
//                    Animation operatingAnim = AnimationUtils.loadAnimation(mActivity, R.anim.rotate);
//                    ((RightViewHolder) o).imageView.startAnimation(operatingAnim);
//                    Picasso.with(mActivity).load(HttpRestClientB.getmHttpUrlsB().DOWNBITMAP + urls[0]).placeholder(R.drawable.downloading_src).error(R.drawable.download_erral).into(((RightViewHolder) o).imageView, new Callback() {
//                        @Override
//                        public void onSuccess() {
//                            ((RightViewHolder) o).imageView.clearAnimation();
//
//                        }
//
//                        @Override
//                        public void onError() {
//                            ((RightViewHolder) o).imageView.clearAnimation();
//                        }
//                    });
//                }
//                final String url = HttpRestClient.getmHttpUrls().DOWNBITMAP + urls[1];
//                ((RightViewHolder) o).imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        Intent intent = new Intent(mActivity, ImageGalleryActivity.class);
//                        intent.putExtra(ImageGalleryActivity.URLS_KEY, new String[]{url});
//                        intent.putExtra(ImageGalleryActivity.TYPE_KEY, 0);//0,1单个,多个
//                        intent.putExtra(ImageGalleryActivity.POSITION, 0);
//                        mActivity.startActivity(intent);
//                    }
//                });
//            } else {
//                ((RightViewHolder) o).contentTextV.setVisibility(View.VISIBLE);
//                ((RightViewHolder) o).imageView.setVisibility(View.GONE);
//                ((RightViewHolder) o).contentTextV.setText(entity.getContent());
//            }
//        }
//    }
//
//    /**
//     * 设置聊天时间
//     * 20151120191722
//     * 00000000000000
//     */
//    private void setChatTime(TextView textView, long lastTime, long nextTime, View timeView) {
//        long c = nextTime - lastTime;
//        if (c <= 200) {//两分钟以内 不显示
//            timeView.setVisibility(View.GONE);
//        } else {
//            try {
//                textView.setText(TimeUtil.getMesgTime(String.valueOf(nextTime)));
//                timeView.setVisibility(View.VISIBLE);
//            } catch (Exception e) {
//                timeView.setVisibility(View.GONE);
//            }
//        }
//    }
//
//    /**
//     * 获取item展示方式
//     *
//     * @param position
//     * @return
//     */
//    @Override
//    public int getItemViewType(int position) {
//        final MessageEntity entity = mList.get(position);
//        if (entity.isSendFlag())
//            return ITEM_RIGHTT_LAYOUT;
//        else
//            return ITEM_LEFT_LAYOUT;
//    }
//
//    /**
//     * item类型数
//     *
//     * @return
//     */
//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }
//
//    private int LEFT_MARGIN = 10;
//    private int RIGHT_MARGIN = 17;
//
//    /**
//     * 解析数据
//     *
//     * @param entity
//     * @param vh
//     * @param position
//     */
//    private Drawable mWhriteDrawable, mGreenDrawable;//都属于左边背景
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
//    private void onParseContent(final MessageEntity entity, final LeftViewHolder vh, int position) {
//
//        //确定 并设置背景
//        if (entity.isAdd) {
//            vh.mContent_layout.setVisibility(View.INVISIBLE);
//            vh.imageViewDianDian.setVisibility(View.VISIBLE);
//            vh.textView_sound.setVisibility(View.GONE);
//            AnimationDrawable animation = (AnimationDrawable) vh.imageViewDianDian.getDrawable();
//            animation.start();
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    vh.mContent_layout.setVisibility(View.VISIBLE);
//                    vh.imageViewDianDian.setVisibility(View.GONE);
//                    mListView.setSelection(getCount());
//                    // mListView.invalidate();
//                    if ((boolean) vh.textView_sound.getTag()) {
//                        vh.textView_sound.setVisibility(View.VISIBLE);
//                    }
//                    entity.isAdd = false;
//                }
//            }, 1000);
////            Observable.timer(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
////                    .map(new Func1<Long, Object>() {
////                        @Override
////                        public Object call(Long aLong) {
////                            vh.mContent_layout.setVisibility(View.VISIBLE);
////                            vh.imageViewDianDian.setVisibility(View.GONE);
////                            mListView.setSelection(getCount());
////                            // mListView.invalidate();
////                            if ((boolean)vh.textView_sound.getTag()){
////                                vh.textView_sound.setVisibility(View.VISIBLE);
////                            }
////                            entity.isAdd = false;
////                            return null;
////                        }
////                    }).subscribe();
//        } else {
//            vh.imageViewDianDian.setVisibility(View.GONE);
//        }
//        LinearLayout contentView = vh.mContent_layout;
//
//        vh.headerImageV.setOnClickListener(this);
//
//        ((RelativeLayout) vh.contentTextV.getParent()).setVisibility(View.VISIBLE);
//        vh.mViewGroup.removeAllViews();//添加控件的父类
//        RelativeLayout.LayoutParams layoutParams;
//        // TODO: 2016-3-29
//        if (!TextUtils.isEmpty(ChatActivityB.merchant_head)) {
////            Picasso.with(mActivity).load(ChatActivityB.merchant_head).placeholder(R.drawable.base_messon).error(R.drawable.base_messon).into(vh.chat_head);
//        }
//        if (!HStringUtil.isEmpty(entity.getCARDTYPE())) {
//            onBoundOrderView(entity, vh);
//            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(LEFT_MARGIN, 0, RIGHT_MARGIN, 0);
//            contentView.setLayoutParams(layoutParams);
//            return;
//        } else {
//            vh.contentTextV.setVisibility(View.VISIBLE);
//            vh.imageViewSound.setVisibility(View.GONE);
//            vh.textView_sound.setVisibility(View.GONE);
//            vh.textView_sound.setTag(false);
//            vh.imageView_chat.setVisibility(View.GONE);
//        }
//
//        //直接设置纯文本
//
//
////        if (!TextUtils.isEmpty(entity.getContent())) {
////
////            vh.mViewGroup.setVisibility(View.GONE);
////            vh.contentTextV.setText(HStringUtil.ToFormatDBC(entity.getContent()));
////            vh.contentTextV.setVisibility(View.VISIBLE);
////            vh.contentTextV.setTextColor(mChatTCommonColor);
////            mWhriteDrawable = mResources.getDrawable(R.drawable.item_chat_l_white_bg);
////            contentView.setBackgroundDrawable(mWhriteDrawable);
////            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
////            layoutParams.setMargins(LEFT_MARGIN, 0, RIGHT_MARGIN, 0);
////            contentView.setLayoutParams(layoutParams);
////            return;
////        }
//        vh.mViewGroup.setVisibility(View.VISIBLE);
//        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(LEFT_MARGIN, 0, RIGHT_MARGIN, 0);
//        contentView.setLayoutParams(layoutParams);
//
//        JSONObject object = getJsonObject(entity);
//        JSONObject responData = null;
//        if (entity.getContentJsonArray() != null) {
//            try {
//                if (entity.getResponData().has("videoUrl") && !TextUtils.isEmpty(entity.getResponData().optString("videoUrl"))) {
//                    playVideo(entity.getResponData(), vh);
//                    return;
//                } else {
//                    responData = new JSONObject(entity.getResponData().optString("retlist").toString());
//                }
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
//        if (responData != null && responData.has("sub_titles")) {
//            mGreenDrawable = mResources.getDrawable(R.drawable.item_chat_l_green_bg);
//            contentView.setBackgroundDrawable(mGreenDrawable);
//            vh.contentTextV.setTextColor(mChatTWhiteColor);
//        } else {
//            mWhriteDrawable = mResources.getDrawable(R.drawable.item_chat_l_white_bg);
//            contentView.setBackgroundDrawable(mWhriteDrawable);
//            vh.contentTextV.setTextColor(mChatTCommonColor);
//        }
//        if (!HStringUtil.isEmpty(entity.getContent()) && entity.getContent().contains("cont")) {
//            try {
//                JSONArray array = new JSONArray(entity.getContent());
//                JSONObject jt = array.getJSONObject(0);
//                if (jt.has("btns")) {
//                    JSONArray jsonArray = new JSONArray(jt.optString("btns"));
//                    for (int i = 0; i < jsonArray.length(); i++) {
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        if (jsonObject != null && jsonObject.has("type") && jsonObject.optInt("type") == 3) {
//                            jsonObject.put("type", 4);
//                        }
//                    }
//                    vh.contentTextV.setVisibility(View.VISIBLE);
////                    onParseLinkTxt(entity, jsonArray, vh, position);
//                    onParseLinkTxt(entity, array, vh, position);
//                } else {
//                    layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.setMargins(LEFT_MARGIN, 0, RIGHT_MARGIN, 0);
//                    contentView.setLayoutParams(layoutParams);
//                    vh.mViewGroup.setVisibility(View.GONE);
//                    vh.contentTextV.setVisibility(View.VISIBLE);
//                    String msgContent = jt.optString("cont");
////                vh.contentTextV.setText(jt.optString("cont").replace("\n", ""));
//                    msgContent = msgContent.replace("\\n", "");
//                    msgContent = msgContent.replace("<\\/br>", "\n");
//                    Spanned spanned = Html.fromHtml(msgContent);
//                    vh.contentTextV.setText(spanned);
//                }
//
//                if (TextUtils.isEmpty(vh.contentTextV.getText().toString().trim())) {
//                    ((RelativeLayout) vh.contentTextV.getParent()).setVisibility(View.GONE);
//                }
//
//            } catch (Exception e) {
//                try {
//                    vh.mViewGroup.setVisibility(View.GONE);
//                    vh.contentTextV.setVisibility(View.VISIBLE);
//                    vh.contentTextV.setTextColor(mChatTCommonColor);
//                    JSONArray array = new JSONArray(entity.getContent());
//                    JSONObject jt = array.getJSONObject(0);
//                    String msgContent = jt.optString("cont");
////                vh.contentTextV.setText(jt.optString("cont").replace("\n", ""));
//                    msgContent = msgContent.replace("\\n", "");
//                    msgContent = msgContent.replace("<\\/br>", "\n");
//                    Spanned spanned = Html.fromHtml(msgContent);
//                    vh.contentTextV.setText(spanned);
////                    vh.contentTextV.setText(entity.getContentJsonArray().getJSONObject(0).getString("smsTEXT"));
//                    layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                    layoutParams.setMargins(LEFT_MARGIN, 0, RIGHT_MARGIN, 0);
//                    contentView.setLayoutParams(layoutParams);
//                } catch (JSONException e1) {
//                }
//            }
//
//        } else {
//            vh.mViewGroup.setVisibility(View.GONE);
//            String msgContent = HStringUtil.ToFormatDBC(entity.getContent());
////                vh.contentTextV.setText(jt.optString("cont").replace("\n", ""));
//            msgContent = msgContent.replace("\\n", "");
//            msgContent = msgContent.replace("<\\/br>", "\n");
//            Spanned spanned = Html.fromHtml(msgContent);
//            vh.contentTextV.setText(spanned);
////            vh.contentTextV.setText(HStringUtil.ToFormatDBC(entity.getContent()));
//            vh.contentTextV.setVisibility(View.VISIBLE);
//            vh.contentTextV.setTextColor(mChatTCommonColor);
//            mWhriteDrawable = mResources.getDrawable(R.drawable.item_chat_l_white_bg);
//            contentView.setBackgroundDrawable(mWhriteDrawable);
//            layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//            layoutParams.setMargins(LEFT_MARGIN, 0, RIGHT_MARGIN, 0);
//            contentView.setLayoutParams(layoutParams);
//            return;
//        }
//
//
////        if (object != null && !object.has("keyWords")) {
////            try {
////
////                JSONObject jt = new JSONObject(entity.getContentJsonArray().getJSONObject(0).getString("smsTEXT"));
////                if (jt.has("btns")) {
////                    JSONArray jsonArray = new JSONArray(jt.optString("btns"));
////                    for (int i = 0; i < jsonArray.length(); i++) {
////                        JSONObject jsonObject = jsonArray.getJSONObject(i);
////                        if (jsonObject != null && jsonObject.has("type") && jsonObject.optInt("type") == 3) {
////                            jsonObject.put("type", 4);
////                        }
////                    }
////                    onParseLinkTxt(entity, jsonArray, vh, position);
////                } else {
////                    layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
////                    layoutParams.setMargins(LEFT_MARGIN, 0, RIGHT_MARGIN, 0);
////                    contentView.setLayoutParams(layoutParams);
////                    vh.mViewGroup.setVisibility(View.GONE);
////                }
////                vh.contentTextV.setVisibility(View.VISIBLE);
////                vh.contentTextV.setText(jt.optString("cont").replace("\n", ""));
////                if (TextUtils.isEmpty(vh.contentTextV.getText().toString().trim())) {
////                    ((RelativeLayout) vh.contentTextV.getParent()).setVisibility(View.GONE);
////                }
////
////            } catch (Exception e) {
////                try {
////                    vh.mViewGroup.setVisibility(View.GONE);
////                    vh.contentTextV.setVisibility(View.VISIBLE);
////                    vh.contentTextV.setTextColor(mChatTCommonColor);
////                    vh.contentTextV.setText(entity.getContentJsonArray().getJSONObject(0).getString("smsTEXT"));
////                    layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
////                    layoutParams.setMargins(LEFT_MARGIN, 0, RIGHT_MARGIN, 0);
////                    contentView.setLayoutParams(layoutParams);
////                } catch (JSONException e1) {
////                }
////            }
////
////        } else {
////
////            if (entity.isLocation()) {//地图
////                onParseLinkTxt(entity, null, vh, position);
////            } else if (object != null && object.has("keyWords") && object.optJSONArray("keyWords").length() > 0) {
////                //包含keyWords   说明是retclientstr
////                onParseLinkTxt(entity, object.optJSONArray("keyWords"), vh, position);
////                vh.contentTextV.setText(entity.contCharSequence);
////                vh.contentTextV.setVisibility(View.VISIBLE);
////            } else {
////                if (object != null) {
////                    String content1 = getContent(object);
////                    entity.setContent(content1);
////                    vh.contentTextV.setText(content1);
////                    vh.contentTextV.setVisibility(View.VISIBLE);
////                    layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
////                    layoutParams.setMargins(LEFT_MARGIN, 0, RIGHT_MARGIN, 0);
////                    contentView.setLayoutParams(layoutParams);
////                }
////
////            }
////        }
//
//
//    }
//
//    /**
//     * 卡片绑定
//     *
//     * @param entity
//     * @param vh
//     */
//    private void onBoundOrderView(final MessageEntity entity, final LeftViewHolder vh) {
//        if (Integer.valueOf(entity.getCARDTYPE()) == 1) {
//            vh.contentTextV.setVisibility(View.GONE);
//            vh.contentTextV.setText(null);
//            vh.imageView_chat.setVisibility(View.VISIBLE);
//            vh.imageViewSound.setVisibility(View.GONE);
//            vh.textView_sound.setVisibility(View.GONE);
//            vh.textView_sound.setTag(false);
//            final String[] urls = entity.getContent().split("&");
//
//            if (!TextUtils.isEmpty(urls[0])) {
//                vh.mViewGroup.setVisibility(View.GONE);
//                Animation operatingAnim = AnimationUtils.loadAnimation(mActivity, R.anim.rotate);
//                vh.imageView_chat.startAnimation(operatingAnim);
//                Picasso.with(mActivity).load(HttpRestClient.getmHttpUrls().DOWNBITMAP + urls[0]).placeholder(R.drawable.downloading_src).error(R.drawable.download_erral).into(((LeftViewHolder) vh).imageView_chat, new Callback() {
//                    @Override
//                    public void onSuccess() {
//                        vh.imageView_chat.clearAnimation();
//
//                    }
//
//                    @Override
//                    public void onError() {
//                        ((LeftViewHolder) vh).imageView_chat.clearAnimation();
//                    }
//                });
//            }
//            final String url = HttpRestClient.getmHttpUrls().DOWNBITMAP + urls[1];
//            vh.imageView_chat.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mActivity, ImageGalleryActivity.class);
//                    intent.putExtra(ImageGalleryActivity.URLS_KEY, new String[]{url});
//                    intent.putExtra(ImageGalleryActivity.TYPE_KEY, 0);//0,1单个,多个
//                    intent.putExtra(ImageGalleryActivity.POSITION, 0);
//                    mActivity.startActivity(intent);
//                }
//            });
//            mWhriteDrawable = mResources.getDrawable(R.drawable.item_chat_l_white_bg);
//
//            vh.mContent_layout.setBackgroundDrawable(mWhriteDrawable);
//            return;
//        } else if (Integer.valueOf(entity.getCARDTYPE()) == 2) {
//            final String[] url = entity.getContent().split("&");
//            vh.mViewGroup.setVisibility(View.GONE);
//            vh.contentTextV.setVisibility(View.GONE);
//            if (entity.isAdd) {
//
//
//                vh.textView_sound.setTag(true);
//            } else {
//
//                vh.textView_sound.setVisibility(View.VISIBLE);
//            }
//            vh.contentTextV.setText(null);
//            vh.textView_sound.setText(url[1]);
//            //   vh.textView_sound.setTextColor(Color.BLACK);
//            vh.imageView_chat.setVisibility(View.GONE);
//            vh.imageViewSound.setVisibility(View.VISIBLE);
//            ((AnimationDrawable) vh.imageViewSound.getDrawable()).stop();
////转文字 暂时还没想好怎么做
//
////            vh.imageViewSound.setOnLongClickListener(new View.OnLongClickListener() {
////                @Override
////                public boolean onLongClick(View v) {
////                    showPopOfficeWindow(vh.mContent_layout, HttpRestClient.getmHttpUrls().DOWNBITMAP + url[0]);
////                    return true;
////                }
////            });
//            vh.imageViewSound.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    startAnimation(vh.imageViewSound);
//                    stopRecoard();
//                    MusicService.Play(mActivity, HttpRestClient.getmHttpUrls().DOWNBITMAP + url[0], MusicService.CREATE);
//                }
//            });
//            mWhriteDrawable = mResources.getDrawable(R.drawable.item_chat_l_white_bg);
//
//            vh.mContent_layout.setBackgroundDrawable(mWhriteDrawable);
//            return;
//        } else {
//            vh.mViewGroup.setVisibility(View.VISIBLE);
//            vh.contentTextV.setVisibility(View.VISIBLE);
//            vh.textView_sound.setVisibility(View.GONE);
//            vh.imageView_chat.setVisibility(View.GONE);
//            vh.imageViewSound.setVisibility(View.GONE);
//            vh.textView_sound.setTag(false);
//        }
//        mWhriteDrawable = mResources.getDrawable(R.drawable.item_chat_l_white_bg);
//
//        vh.mContent_layout.setBackgroundDrawable(mWhriteDrawable);
//        vh.contentTextV.setText(HStringUtil.ToFormatDBC(entity.getContent()));
//        vh.contentTextV.setVisibility(View.VISIBLE);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams.setMargins(LEFT_MARGIN, 0, RIGHT_MARGIN, 0);
//        vh.mContent_layout.setLayoutParams(layoutParams);
//        switch (Integer.valueOf(entity.getCARDTYPE())) {
//            case CardType.SHOP_CARD://商品卡片
//                try {
//                    TyJsonObject jsonObject = new TyJsonObject(entity.getContent());
//                    JSONArray array = new JSONArray(jsonObject.optString("goods"));
//                    int len = array.length();
//                    for (int i = 0; i < len; i++) {
//                        final TyJsonObject ob = new TyJsonObject(array.optString(i));
//                        View view = mInflater.inflate(R.layout.chat_push_shop_item, null);
//                        vh.mViewGroup.addView(view);
//                        final ViewFinder finder = new ViewFinder(view);
//                        //名称
//                        finder.setText(R.id.name, ob.optString("goodsTitle"));
//                        TextView subtitle = finder.setText(R.id.goodssubtitle, ob.optString("goodsSubTitle"));
//                        if (HStringUtil.isEmpty(subtitle.getText().toString())) {
//                            subtitle.setVisibility(View.GONE);
//                        } else {
//                            subtitle.setVisibility(View.VISIBLE);
//                        }
//
//                        //备注
//                        TextView beizhu = finder.setText(R.id.beizhu, ob.optString("goodsSubTitle4") + "\n" + ob.optString("goodsSubTitle3"));
//                        if (HStringUtil.isEmpty(beizhu.getText().toString())) {
//                            beizhu.setVisibility(View.GONE);
//                        } else {
//                            beizhu.setVisibility(View.VISIBLE);
//                        }
//
//                        //描述
//                        TextView desc = finder.setText(R.id.desc, "");
//                        if (ob.has("goodsDesc")) {//判断是否有描述
//                            desc.setText((new TyJsonObject(ob.optString("goodsDesc"))).optString("TEMPLE_CONTENT"));
//                        } else {
//                            desc.setVisibility(View.GONE);
//                        }
//                        Picasso.with(mActivity).load(ob.optJSONObject("goodsIcon").optString("TEMPLE_IMG_URL")).placeholder(R.drawable.meizhuang_plat_holder).into(finder.imageView(R.id.image));
//                        //商品图片
////                        mInstance.displayImage(ob.optJSONObject("goodsIcon").optString("TEMPLE_IMG_URL"), finder.imageView(R.id.image), HApplication.createShopDisplayImageOptions());
//                        view.findViewById(R.id.look_desc_bottom).setVisibility(View.GONE);
//                        view.findViewById(R.id.look_desc_right_bg).setVisibility(View.GONE);
//                        finder.imageView(R.id.look_desc_right).setVisibility(View.GONE);
//                        JSONArray btnJs = new JSONArray(ob.optString("buttons"));//显示下面按钮
//                        if (len > 1 && btnJs.length() == 1) {
//                            final ImageView btn = finder.imageView(R.id.look_desc_right);
//                            btn.setVisibility(View.VISIBLE);
//                            final JSONObject j = btnJs.optJSONObject(0);
//                            StringBuffer buf = new StringBuffer(j.optString("BUTTONS_NAME"));
////                            btn.setText(buf.insert(2, "\n"));
//                            btn.setTag(j);
//                            btn.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    onTouchBtn(btn, ob, j.optInt("FUNCTION_CODE"));
//                                }
//                            });
//                            view.findViewById(R.id.look_desc_right_bg).setVisibility(View.VISIBLE);
//                            view.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    onTouchBtn(btn, ob, j.optInt("FUNCTION_CODE"));
//                                }
//                            });
//                        } else {
//                            if (btnJs != null && btnJs.length() > 0) {
//                                LinearLayout btns = (LinearLayout) view.findViewById(R.id.look_desc_bottom);
//                                btns.setVisibility(View.VISIBLE);
//                                for (int b = 0; b < btns.getChildCount(); b++) {
//                                    btns.getChildAt(b).setVisibility(View.GONE);
//                                }
//                                for (int x = 0; x < btnJs.length(); x++) {
//                                    final JSONObject buttonjson = btnJs.optJSONObject(x);
//                                    TextView button = (TextView) btns.getChildAt(x);
//                                    button.setVisibility(View.VISIBLE);
//                                    button.setText(buttonjson.optString("BUTTONS_NAME"));
//                                    button.setTag(buttonjson);
//                                    button.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            onTouchBtn(v, ob, buttonjson.optInt("FUNCTION_CODE"));
//                                        }
//                                    });
//                                }
//
//                            }
//                        }
//
//                        /**
//                         * 价格设置
//                         */
//                        TextView price = finder.find(R.id.price);
//                        SpannableStringBuilder builder = new SpannableStringBuilder();
//                        builder.append("价格 : ");
//                        if (ob.optString("goodsPrice1").equals("未知") || HStringUtil.isEmpty(ob.optString("goodsPrice1"))) {//判断价格：未知元
//                            price.setVisibility(View.GONE);
//                        } else {
//                            SpannableString s1 = new SpannableString(ob.optString("goodsPrice2"));
//                            ForegroundColorSpan colorSpan = new ForegroundColorSpan(Color.RED);
//                            s1.setSpan(colorSpan, 0, s1.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            builder.append(s1).append("元  ");
//                            SpannableString s2 = new SpannableString(ob.optString("goodsPrice1"));
//                            StrikethroughSpan span2 = new StrikethroughSpan();
//                            s2.setSpan(span2, 0, s2.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                            builder.append(s2).append("元");
//                        }
//                        finder.setText(R.id.price, builder);
//
//                        View line = mInflater.inflate(R.layout.common_line, null);
//                        vh.mViewGroup.addView(line);
//                    }
//
//                    if (len > 0) {
//                        vh.mViewGroup.removeViewAt(vh.mViewGroup.getChildCount() - 1);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                break;
//            case CardType.LABLE_CARD://标签卡片
//                try {
//                    final TyJsonObject lableJson = new TyJsonObject(entity.getContent());
//                    View lableView = mInflater.inflate(R.layout.chat_push_pay_lable_item, null);
//                    ViewFinder finder = new ViewFinder(lableView);
//                    Picasso.with(mActivity).load(lableJson.optString("PICADDR")).into(finder.imageView(R.id.image));
////                    mInstance.displayImage(lableJson.optString("PICADDR"), finder.imageView(R.id.image));
//                    finder.setText(R.id.name, lableJson.optString("ORDER_NAME"));
//                    finder.setText(R.id.desc, lableJson.optString("ORDER_DESC"));
//                    finder.setText(R.id.price, lableJson.optString("PAY_PRICE") + "元");
//                    finder.setText(R.id.beizhu, lableJson.optString("Tagging"));
//                    finder.onClick(R.id.btn_pay, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            payBeforRespon(lableJson.optString("ORDER_ID"), lableJson.optString("sms_target_id"));
//                        }
//                    });
//                    vh.mViewGroup.addView(lableView);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
//            case CardType.GUAHAO_CARD://挂号卡片
//                try {
//                    final TyJsonObject ghJson = new TyJsonObject(entity.getContent());
//                    View guahaoView = mInflater.inflate(R.layout.chat_push_guahao_item, null);
//                    vh.mViewGroup.addView(guahaoView);
//                    ViewFinder finder = new ViewFinder(guahaoView);
//                    TextView beizhu = finder.find(R.id.beizhu);
//                    if (!HStringUtil.isEmpty(ghJson.optString("HsptDesc"))) {
//                        finder.setText(R.id.beizhu, ghJson.optString("HsptDesc"));
//                    } else {
//                        beizhu.setVisibility(View.GONE);
//                    }
//                    //商品图片
////                    mInstance.displayImage(ghJson.optString("Pic"), finder.imageView(R.id.image), HApplication.createDocHeadDisplayImageOptions());
//                    Picasso.with(mActivity).load(ghJson.optString("Pic")).placeholder(R.drawable.meizhuang_plat_holder).into(finder.imageView(R.id.image));
//                    StringBuffer buffer = new StringBuffer();
//                    buffer.append(ghJson.optString("DocName")).append(" ").append(ghJson.optString("professionalTitles"))
//                            .append("\n")
//                            .append(ghJson.optString("HospitalAddress"))
//                            .append("\n")
//                            .append(ghJson.optString("departments"))
//                            .append("\n")
//                            .append(ghJson.optString("Price"))
//                            .append("\n")
//                            .append(ghJson.optString("registeTime"));
//                    finder.setText(R.id.name, buffer);
//                    finder.onClick(R.id.btn1, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            agreeRespon(ghJson, "0", (TextView) v);
//
//                        }
//                    });
//                    finder.onClick(R.id.btn2, new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            agreeRespon(ghJson, "1", (TextView) v);
//                        }
//                    });
//
//                    //agreeRespon
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                break;
//            case CardType.COMPARE_CARD://比较案例
//                try {
//                    final TyJsonObject comJson = new TyJsonObject(entity.getContent());
//                    View compareView = mInflater.inflate(R.layout.chat_push_compare_item, null);
//                    vh.mViewGroup.addView(compareView);
//                    ViewFinder finder = new ViewFinder(compareView);
//                    finder.setText(R.id.name, comJson.optString("CONTENT"));
//                    JSONArray array = new JSONArray(comJson.optString("piclist"));
//                    if (array.length() > 0) {
//                        compareView.findViewById(R.id.pic_group).setVisibility(View.VISIBLE);
//                        finder.textView(R.id.txt1).setVisibility(View.GONE);
//                        finder.imageView(R.id.image1).setVisibility(View.GONE);
//                        finder.imageView(R.id.image2).setVisibility(View.GONE);
//                        finder.textView(R.id.txt2).setVisibility(View.GONE);
//                    } else {
//                        compareView.findViewById(R.id.pic_group).setVisibility(View.GONE);
//                    }
//
//                    for (int i = 0; i < array.length(); i++) {
//                        TyJsonObject tyJsonObject = new TyJsonObject(array.optString(i));
//                        if (i == 0) {
//                            finder.imageView(R.id.image1).setVisibility(View.VISIBLE);
//                            finder.textView(R.id.txt1).setVisibility(View.VISIBLE);
//                            Picasso.with(mActivity).load(tyJsonObject.optString("PIC_ADDR"))
//                                    .placeholder(R.drawable.chat_default_bg).
//                                    into(finder.imageView(R.id.image1));
//
////                            mInstance.displayImage(tyJsonObject.optString("PIC_ADDR"), finder.imageView(R.id.image1));
//                            finder.setText(R.id.txt1, tyJsonObject.optString("PIC_SEQ_NOTE"));
//                        } else if (i == 1) {
//                            finder.imageView(R.id.image2).setVisibility(View.VISIBLE);
//                            finder.textView(R.id.txt2).setVisibility(View.VISIBLE);
////                            mInstance.displayImage(tyJsonObject.optString("PIC_ADDR"), finder.imageView(R.id.image2));
//                            Picasso.with(mActivity).load(tyJsonObject.optString("PIC_ADDR"))
//                                    .placeholder(R.drawable.chat_default_bg).
//                                    into(finder.imageView(R.id.image2));
//                            finder.setText(R.id.txt2, tyJsonObject.optString("PIC_SEQ_NOTE"));
//                        }
//                    }
//
//
//                    JSONArray btns = new JSONArray(comJson.optString("buttons"));
//                    LinearLayout line = (LinearLayout) compareView.findViewById(R.id.group_view);
//                    line.removeAllViews();
//                    for (int i = 0; i < btns.length(); i++) {
//                        final TyJsonObject ty = new TyJsonObject(btns.optString(i));
//                        View vv = mInflater.inflate(R.layout.chat_push_compare_txt_layout, null);
//                        TextView mvew = (TextView) vv.findViewById(R.id.btn_pay);
//                        line.addView(vv);
//                        mvew.setText(ty.optString("BUTTONS_NAME"));
//                        mvew.setTag(ty);
//                        mvew.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                onTouchBtn(v, comJson, ty.optInt("FUNCTION_CODE"));
//                            }
//                        });
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                break;
//            case CardType.STRING_CARD://商品卡片
//                try {
//                    final TyJsonObject comJson = new TyJsonObject(entity.getContent());
//                    String msg = comJson.optString("cont");
//                    if (!TextUtils.isEmpty(msg)) {
//                        LinearLayout contentView = vh.mContent_layout;
//                        vh.contentTextV.setText(msg);
//                        vh.contentTextV.setVisibility(View.VISIBLE);
//                        mWhriteDrawable = mResources.getDrawable(R.drawable.item_chat_l_white_bg);
//
//
//                        contentView.setBackgroundDrawable(mWhriteDrawable);
//                        vh.mViewGroup.setVisibility(View.GONE);
//                        vh.contentTextV.setTextColor(mChatTCommonColor);
//                        layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//                        layoutParams.setMargins(LEFT_MARGIN, 0, RIGHT_MARGIN, 0);
//                        contentView.setLayoutParams(layoutParams);
//                        return;
//                    }
//
//                } catch (JSONException e) {
//                    vh.contentTextV.setTextColor(mActivity.getResources().getColor(R.color.imput_color));
//                    vh.mViewGroup.setVisibility(View.GONE);
//
//                }
//                break;
//            case 1:
//
//
//                break;
//        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    private void onParseLinkTxt(final MessageEntity entity, JSONArray keyWords, final LeftViewHolder vh, final int position) {
//        JSONObject responData = null;
//        String msgContent = "";
//        if (entity.getContent() != null) {
//            try {
////                responData = new JSONObject(entity.getResponData().optString("retlist").toString());
//                responData = keyWords.getJSONObject(0);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//        if (responData == null) {//地图 数据绑定
//            try {
//                /**
//                 *  "POSITION_Y": "116.376785",
//                 "POSITION_X": "39.95285",
//                 "ADDRESS": "德胜门外安康胡同5号",
//                 "NAME": "北京安定医院"
//                 */
//
//                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 400);
//                final JSONObject locationMsg = new JSONObject(entity.getLocationMsg());
//                ImageView imageView = new ImageView(mActivity);
//                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
//                String uri = HttpRestClient.getGoogleMapUrl(locationMsg.optString("POSITION_Y"), locationMsg.optString("POSITION_X"));
////                mInstance.displayImage(uri, imageView);
//                Picasso.with(mActivity).load(uri).into(imageView);
//                vh.contentTextV.setText("您选择的是:" + locationMsg.optString("NAME") + mResources.getString(R.string.address_location) + HStringUtil.stringFormat(locationMsg.optString("ADDRESS")));
//                vh.mViewGroup.addView(imageView, layoutParams);
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (!isClick) return;
//                        ActivityUtils.startMapActivity(mActivity, locationMsg.optString("POSITION_Y"), locationMsg.optString("POSITION_X"), locationMsg.optString("NAME"));
//                    }
//                });
//
//
//                if (locationMsg.optInt("mesgType") != 2) {
//                    View btnGroup = mInflater.inflate(R.layout.layout_item_btn_single, null);
//                    final Button bt = (Button) btnGroup.findViewById(R.id.btn);
//                    bt.setText("我要挂号");
//                    bt.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            chatWithServiceFromGuaHao(bt.getText().toString(), locationMsg, entity);
//                        }
//                    });
//                    bt.setVisibility(View.GONE);
//                    vh.mViewGroup.addView(btnGroup);
//                }
//
//
//            } catch (Exception e) {
//            }
//        } else if (responData.has("sub_titles")) {
//            msgContent = responData.optString("cont");
//            msgContent = msgContent.replace("<\\/br>", "\n");
//            vh.contentTextV.setText(msgContent);
//            if (mList.get(position).list != null) {
//                for (int i = 0; i < mList.get(position).list.size(); i++) {
//                    vh.mViewGroup.addView(mList.get(position).list.get(i));
//                }
//                vh.mViewGroup.setPaddingRelative(0, 0, 0, 0);
//                return;
//            }
//            mList.get(position).list = new ArrayList<View>();
//            JSONArray sub_titles = responData.optJSONArray("sub_titles");
//            Button button = null;
//            /**suGroup 为其他或者更多中包含的数据,如果id为100时 将显示
//             */
//            final LinearLayout suGroup = new LinearLayout(mActivity);
//            suGroup.setOrientation(LinearLayout.VERTICAL);
//            suGroup.setBackgroundColor(Color.WHITE);
//
//
//            JSONObject btn;
//            for (int i = 0; i < sub_titles.length(); i++) {
//                final View btnGroup = mInflater.inflate(R.layout.layout_item_btn_single_green, null);
//                vh.mViewGroup.addView(btnGroup);
//                mList.get(position).list.add(btnGroup);
//                button = (Button) btnGroup.findViewById(R.id.btn);
//                btn = sub_titles.optJSONObject(i);
//                button.setText(btn.optString("cont"));
//                button.setTag(btn);
//                final View view = btnGroup.findViewById(R.id.line2);
//                view.setVisibility(View.GONE);
//                if (i == sub_titles.length() - 1) {
//                    btnGroup.setBackgroundResource(R.drawable.btn_down_green);
//                } else {
//                    btnGroup.setBackgroundResource(R.drawable.btn_middle_green);
//                }
//
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (isOncliBtn()) return;
//                        if (((JSONObject) v.getTag()).optInt("id") == 100) {
//                            if (suGroup.getVisibility() == View.GONE) {
//                                AnimaUtils.viewDownVisitly(suGroup);
//                                view.setVisibility(View.VISIBLE);
//                                btnGroup.setBackgroundResource(R.drawable.btn_middle_green);
//                                TalkEvent talkEvent = new TalkEvent();
//                                talkEvent.setWhat(TalkEvent.UPTALK);
//                                talkEvent.setArg1(position);
//                                talkEvent.setArg2(vh.mViewGroup.getHeight() - view.getHeight());
////                                talkEvent.setObject(suGroup);
//                                EventBus.getDefault().post(talkEvent);
//                            } else {
//                                AnimaUtils.viewUpGone(suGroup);
//                                view.setVisibility(View.GONE);
//                                btnGroup.setBackgroundResource(R.drawable.btn_down_green);
//                            }
//                        } else {
//                            LinearLayout linearLayouts = (LinearLayout) btnGroup.findViewById(R.id.desc_group);
//                            onResponDesc(entity, (Button) v, 0, linearLayouts);
//                        }
//                    }
//                });
//            }
//
//            //解析更多中得数据
//            JSONArray btns = responData.optJSONArray("btns");
//
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            mList.get(position).list.add(suGroup);
//            vh.mViewGroup.addView(suGroup, layoutParams);
//            suGroup.setVisibility(View.GONE);
//            suGroup.setPadding(0, UnitUtil.dip2px(mActivity, 10), 0, 1);
//
//            suGroup.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.btn_bg_gray));
//            View su_desc = null;//点击更多显示布局  id为100
//            for (int i = 0; i < btns.length(); i++) {
//                if (i % 2 == 0) {
//                    su_desc = mInflater.inflate(R.layout.layout_item_btn_double, null);
//                    suGroup.addView(su_desc);
//                    button = (Button) su_desc.findViewById(R.id.btn);
//                } else {
//                    button = (Button) su_desc.findViewById(R.id.btn2);
//                    button.setVisibility(View.VISIBLE);
//                }
//                btn = btns.optJSONObject(i);
//                button.setText(btn.optString("cont"));
//                button.setTag(btn);
//
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        oncliCkTouch(entity, (Button) v, 1, false);
//                    }
//                });
//            }
//            vh.mViewGroup.setPaddingRelative(0, 0, 0, 0);
//        } else if (responData.has("max_display")) {//症状
//            int maxDisplay = responData.optInt("max_display");
//            JSONObject btn;
//            msgContent = responData.optString("cont");
//            msgContent = msgContent.replace("<\\/br>", "\n");
//            Spanned spanned = Html.fromHtml(msgContent);
//            vh.contentTextV.setText(spanned);
//            JSONArray btns = responData.optJSONArray("btns");
//            Button button = null;
//            View btnGroup = null;
//            for (int i = 0; i < btns.length(); i++) {
//                btn = btns.optJSONObject(i);
//                btnGroup = mInflater.inflate(R.layout.layout_item_btn_single, null);
//                button = (Button) btnGroup.findViewById(R.id.btn);
//                button.setText(btn.optString("cont"));
//                button.setTag(btn);
//                if (maxDisplay > i) {
//                    button.setVisibility(View.VISIBLE);
//                } else if (btns.length() != 4) {
//                    button.setVisibility(View.GONE);
//                }
//
//                if (i < 3) {
//                    button.setBackgroundResource(mZdrawables[i]);
//                    button.setTextColor(mActivity.getResources().getColor(mZdraColor[i]));
//                }
//                vh.mViewGroup.addView(btnGroup);
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        oncliCkTouch(entity, (Button) v, 0, false);
//                    }
//                });
//            }
//            vh.mViewGroup.setPaddingRelative(0, 0, 0, 18);
//
//            if (responData.has("btn_more_name") && btns.length() != 4) {
//                btnGroup = mInflater.inflate(R.layout.layout_item_btn_single, null);
//                final View coptyView = btnGroup;
//                vh.mViewGroup.addView(btnGroup);
//                final Button bMore = (Button) btnGroup.findViewById(R.id.btn);
//                bMore.setText(responData.optString("btn_more_name"));
//                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnGroup.getLayoutParams();
//                layoutParams.setMargins(0, 0, 0, 20);
//                bMore.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        int childCount = vh.mViewGroup.getChildCount();
//                        for (int i = 0; i < childCount; i++) {
//                            View ch = vh.mViewGroup.getChildAt(i).findViewById(R.id.btn);
//                            ch.setVisibility(View.VISIBLE);
//                        }
//                        coptyView.setVisibility(View.GONE);
//                    }
//                });
//            }
//
//
//        } else if (responData.has("btn_align") && responData.optInt("btn_align") == 1) {
//            /**
//             * {"cont":"您的个人资料是：30岁、男，是否正确？",
//             * "btns"[{"id":"2","cont":"不正确","type":"3"},
//             * {"id":"1","cont":"正确","type":"3"}],"btn_align":1,"btn_type":0}
//             */
//            vh.mViewGroup.setPaddingRelative(0, 0, 0, 0);
//            msgContent = responData.optString("cont");
//            msgContent = msgContent.replace("<\\/br>", "\n");
//            Spanned spanned = Html.fromHtml(msgContent);
//            vh.contentTextV.setText(spanned);
//            View linearLayout = null;
//            JSONArray btns = responData.optJSONArray("btns");
//            JSONObject btn;
//            Button button;
//            for (int i = 0; i < btns.length(); i++) {
//                if (i % 2 == 0) {
//                    linearLayout = mInflater.inflate(R.layout.layout_item_btn_double, null);
//                    vh.mViewGroup.addView(linearLayout);
//                    button = (Button) linearLayout.findViewById(R.id.btn);
//                } else {
//                    button = (Button) linearLayout.findViewById(R.id.btn2);
//                    button.setVisibility(View.VISIBLE);
//                }
//
//                btn = btns.optJSONObject(i);
//                button.setText(btn.optString("cont"));
//                button.setTag(btn);
//                button.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        oncliCkTouch(entity, (Button) v, 0, false);
//                    }
//                });
//            }
//        } else if (responData.length() == 1) {
//            vh.mViewGroup.removeAllViews();
//            msgContent = responData.optString("cont");
//            msgContent = msgContent.replace("<\\/br>", "\n");
//            Spanned spanned = Html.fromHtml(msgContent);
//            vh.contentTextV.setText(spanned);
//            vh.mViewGroup.setPaddingRelative(0, 0, 0, 0);
//        } else {//
//            if (position == mList.size() - 1) {
//                vh.mViewGroup.setPaddingRelative(0, 0, 0, 0);
//                /**
//                 * {"cont":"您头疼发病时是什么情况？",
//                 * "btns"[{"id":"1","cont":"急性起病伴发热","type":"3"},
//                 * {"id":"2","cont":"急剧持续头痛伴意识障碍无发热","type":"3"},
//                 * {"id":"3","cont":"反复发作性或搏动性头痛","type":"3"},
//                 * {"id":"4","cont":"慢性头痛进行性加重","type":"3"},
//                 * {"id":"5","cont":"青壮年患者，慢性头痛","type":"3"},
//                 * {"id":"6","cont":"没有以上情况","type":"3"}],"btn_align":0,"btn_type":0}
//                 */
//                msgContent = responData.optString("cont");
//                msgContent = msgContent.replace("<\\/br>", "\n");
//                Spanned spanned = Html.fromHtml(msgContent);
//                vh.contentTextV.setText(spanned);
//                final JSONArray btns = responData.optJSONArray("btns");
//                JSONObject btn;
//                View btnGroup;
//                Button button;
//                for (int i = 0; i < btns.length(); i++) {
//                    btnGroup = mInflater.inflate(R.layout.layout_item_btn_single, null);
//                    vh.mViewGroup.addView(btnGroup);
//                    button = (Button) btnGroup.findViewById(R.id.btn);
//
//
//                    if (i >= 4) {
//                        button.setText("更多");
//                        button.setTag(btns);
//                        button.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                showMorePopWindow(btns, vh.mViewGroup, new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        int index1 = (Integer) v.getTag(R.id.wheel_one);
//                                        JSONObject json = btns.optJSONObject(index1);
//                                        oncliCkTouch(entity, json, 0, false);
//                                    }
//                                });
//                            }
//                        });
//                        vh.mViewGroup.setPaddingRelative(0, 0, 0, 18);
//                        break;
//                    } else {
//                        btn = btns.optJSONObject(i);
//                        button.setText(btn.optString("cont"));
//                        button.setTag(btn);
//                        button.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                oncliCkTouch(entity, (Button) v, 0, false);
//                            }
//                        });
//                    }
//                    if (i == btns.length() - 1) {
//                        vh.mViewGroup.setPaddingRelative(0, 0, 0, 18);
////                    LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnGroup.getLayoutParams();
////                    layoutParams.setMargins(0, 0, 0, 18);
//                    }
//                }
//            } else {
//
//                /**
//                 * {"cont":"您头疼发病时是什么情况？",
//                 * "btns"[{"id":"1","cont":"急性起病伴发热","type":"3"},
//                 * {"id":"2","cont":"急剧持续头痛伴意识障碍无发热","type":"3"},
//                 * {"id":"3","cont":"反复发作性或搏动性头痛","type":"3"},
//                 * {"id":"4","cont":"慢性头痛进行性加重","type":"3"},
//                 * {"id":"5","cont":"青壮年患者，慢性头痛","type":"3"},
//                 * {"id":"6","cont":"没有以上情况","type":"3"}],"btn_align":0,"btn_type":0}
//                 */
//                msgContent = responData.optString("cont");
//                msgContent = msgContent.replace("<\\/br>", "\n");
//                StringBuffer buffer = new StringBuffer();
//                buffer.append(msgContent + "<br/>");
//                final JSONArray btns = responData.optJSONArray("btns");
//                for (int i = 0; i < btns.length(); i++) {
//                    if (i==btns.length()-1){
////                        buffer.append((i + 1) + "，" + btns.optJSONObject(i).optString("cont")+"。");
//                        buffer.append((i + 1) + "、" + btns.optJSONObject(i).optString("cont"));
//                    }else {
////                        buffer.append((i + 1) + "、" + btns.optJSONObject(i).optString("cont")+"；"+"<br/>");
//                        buffer.append((i + 1) + "、" + btns.optJSONObject(i).optString("cont")+"<br/>");
//                    }
//                }
//                Spanned spanned = Html.fromHtml(buffer.toString());
//                vh.contentTextV.setText(spanned);
//                vh.mViewGroup.removeAllViews();
//            }
//        }
//    }
//
//    /**
//     * 更多 以滚轮显示
//     *
//     * @param btns
//     */
//    private void showMorePopWindow(final JSONArray btns, View v, View.OnClickListener listener) {
//        final List<Map<String, String>> maps = new ArrayList<Map<String, String>>();
//
//        for (int i = 0; i < btns.length(); i++) {
//            Map<String, String> map = new HashMap<String, String>();
//            map.put("name", btns.optJSONObject(i).optString("cont"));
//            maps.add(map);
//        }
//        WheelUtils.showSingleWheel(mActivity, maps, v, listener);
//    }
//
//    /**
//     * 直接展开
//     *
//     * @param entity        消息实体
//     * @param v             点击按钮
//     * @param position      position
//     * @param linearLayouts 可添加的布局
//     */
//    private synchronized void onResponDesc(MessageEntity entity, Button v, int position, LinearLayout linearLayouts) {
//        try {
//            if (linearLayouts.getVisibility() == View.VISIBLE) {
//                linearLayouts.setVisibility(View.GONE);
//                return;
//            }
//            JSONObject j = (JSONObject) v.getTag();
//            String id = j.optString("id");
//            JSONObject json = entity.getContentJsonArray().getJSONObject(1);
//
//            //附近医院 点击预约医院id 99         附近药店id 101
//            if (("101".equals(id)) || (json.has("retJBTZGN") && json.optInt("retJBTZGN") == 1 && "99".equals(id))) {
//                getLocation(linearLayouts, json, entity, id);
//            } else {
//                if (linearLayouts.getChildCount() == 0) {
//                    JSONObject jsot = onreadyData(entity, j);
//                    jsot.put("vpage", "");
//
//                    if (isOncliBtn()) return;
//                    HttpRestClient.doHttpVirtualDoctor(jsot, new MJsonHttpResponseHandler(entity, linearLayouts, v));
//                } else {
//                    linearLayouts.setVisibility(View.VISIBLE);
//                }
//            }
//        } catch (Exception e) {
//
//        }
//    }
//
//    private boolean isOncliBtn() {
////        !HStringUtil.isEmpty(HApplication.getApplication().getmTalkServiceId())
//        return false;
//    }
//
//    /**
//     * 点击跳转
//     *
//     * @param entity
//     * @param v
//     * @param i
//     * @param b      true 表示来自more 需要加参数
//     */
//    private void oncliCkTouch(MessageEntity entity, Button v, int i, boolean b) {
//        if (!isClick) return;
//        oncliCkTouch(entity, (JSONObject) v.getTag(), i, b);
//    }
//
//    private void oncliCkTouch(MessageEntity entity, JSONObject j, int i, boolean b) {
//        if (!isClick) return;
//        try {
//            String id = j.optString("id");
//            String name = j.optString("cont");
//            JSONObject json = new JSONArray(entity.getContent()).getJSONObject(0);
////            JSONObject json = entity.getContentJsonArray().getJSONObject(1);
//            if (json.has("retJBTZGN") && json.optInt("retJBTZGN") == 1 && "11".equals(id)) {//找医生
////                getLocation();
//            } else if (json.has("customer_info") && 2 == json.optInt("customer_info", -100) && "2".equals(id)) {//修改界面
//                if (isOncliBtn())
//                    return;
////                if (!LoginServiceManegerB.isLogined()) {
////                    DoubleBtnFragmentDialog.showDefault(mActivity.getSupportFragmentManager(), "请登录后使用该功能", "取消", "登录", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
////                        @Override
////                        public void onDismiss(DialogFragment fragment) {
////                        }
////
////                        @Override
////                        public void onClick(DialogFragment fragment, View v) {
////                            mActivity.startActivity(new Intent(mActivity, LoginActivity.class));
////                        }
////                    });
////                    return;
////                }
//                Intent intent = new Intent(mActivity, DoctorChatMyinformation.class);
//                intent.putExtra("modify_Content", json.getString("modify_Content"));
//                intent.putExtra("data", json.toString());
//                if (HStringUtil.isEmpty(json.optString("ltalkname")))
//                    intent.putExtra("titleName", "个人资料");
//                else
//                    intent.putExtra("titleName", json.optString("ltalkname"));
//                mActivity.startActivityForResult(intent, 200);
//            } else if (json.has("retJBTZGN") && json.optInt("retJBTZGN") == 1) {//表示跳转话题 找病友 聊话题 页面
//                if (isOncliBtn()) return;
//
//                JSONObject data = onreadyData(entity, j);
//                if (data != null)
//                    sendMessage(data, name, i, b);
//            } else if (j.has("identifier") && "detailForm".equals(j.optString("identifier"))) {
//                onSendIsWriteInfo(j);//填写资料
//            } else {
//                if (isOncliBtn()) return;
//                JSONObject data = onreadyData(entity, j);
//                if (data != null)
//                    sendMessage(data, name, i, b);
//
//            }
//        } catch (Exception e) {
//            ToastUtil.showShort("1");
//        }
//    }
//
//    /**
//     * 是否可以去改变订单人的信息
//     *
//     * @param j
//     */
//    private void onSendIsWriteInfo(final JSONObject j) {
//
///**
// * DummyXiaoyiURL *url = [DummyXiaoyiURL urlWithServlet:@"dmys/createOrder", nil];
// [url appendKey:@"Type" value:@"queryOrderStatus"];
// [url appendKey:@"ORDER_ID" value:[button.attributes UTF8String:@"ORDER_ID"]];
// [url appendKey:@"CUSTOMER_ID" value:[[CustomerInfo me] customerId]];
// */
//
//        if (!LoginServiceManegerB.isLogined()) {
//            DoubleBtnFragmentDialog.showDefault(mActivity.getSupportFragmentManager(), "请登录后使用该功能", "取消", "登录", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
//                @Override
//                public void onDismiss(DialogFragment fragment) {
//                }
//
//                @Override
//                public void onClick(DialogFragment fragment, View v) {
////                    mActivity.startActivity(new Intent(mActivity, LoginRegisterActivity.class));
//                }
//            });
//            return;
//        }
//
//        RequestParams params = new RequestParams();
//        params.put("Type", "queryOrderStatus");
//        params.put("ORDER_ID", j.optString("ORDER_ID"));
//        params.put("btnflag", "1");
//        params.put("CUSTOMER_ID", LoginServiceManegerB.instance().getLoginId());
//        HttpRestClientB.doHttpcreateOrder(params, new JsonHttpResponseHandler(mActivity) {
//            @Override
//            public void onSuccess(JSONObject response) {
//                super.onSuccess(response);
//                if (response.optInt("code") == 1) {
//                    Intent intent = new Intent(mActivity, OrderUserInfoActivity.class);
//                    intent.putExtra("data", j.toString());
//                    intent.putExtra("TalkId", HTalkApplication.getApplication().getmTalkServiceId());
//                    mActivity.startActivityForResult(intent, 400);
//                } else {
//                    ToastUtil.showShort(response.optString("message"));
//                }
//            }
//        });
//
//    }
//
//    /**
//     * @param jsonObject
//     * @param name
//     * @param i          1表示切页面
//     * @param b
//     */
//    private void sendMessage(JSONObject jsonObject, String name, int i, boolean b) {
//
//        if (b) {//表示点击查看more
//            try {
//                jsonObject.put("vpage", "100");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//        }
//
////        if (i == 1) {//切换页面
//        if (i < 0) {//切换页面
//            Intent intent = new Intent(mActivity, DisplyContentActivity.class);
//            intent.putExtra("title", name);
//            intent.putExtra("data", jsonObject.toString());
//            if (mSpeechUtils.img != null) {
//                mSpeechUtils.img.setSelected(false);
//            }
//            mActivity.startActivityForResult(intent, 500);
//        } else {
//            if (mActivity instanceof ChatActivityB)
//                ((ChatActivityB) mActivity).sendMessage(jsonObject, name);
//            else {
//                // TODO: 2016-3-29
////                ((HistoryChatContentActivity) mActivity).sendMessage(jsonObject, name);
//            }
//        }
//    }
//
//    private class MJsonHttpResponseHandler extends AsyncHttpResponseHandler {
//
//        MessageEntity entity;
//        LinearLayout linearLayouts;
//        Button mPitent;//点击的view
//
//        public MJsonHttpResponseHandler(MessageEntity entity, LinearLayout linearLayouts, Button v) {
//            super(mActivity);
//            this.entity = entity;
//            this.linearLayouts = linearLayouts;
//            mPitent = v;
//        }
//
//        @Override
//        public void onSuccess(int statusCode, String content) {
//            super.onSuccess(statusCode, content);
//            if (!mActivity.isFinishing()) {
//                if (statusCode == 200 && content != null)
//                    try {
//                        JSONObject response = new JSONObject(content);
//                        onMessageReceive(response, entity, linearLayouts, mPitent);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//            }
//        }
//    }
//
//    private void onMessageReceive(final JSONObject response, final MessageEntity entity, LinearLayout linearLayouts, final Button mPitent) {
//        try {
//            final JSONObject json = new JSONObject(response.optString("retlist").toString());
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//            //文字
//            TextView content = new TextView(mActivity);
//            content.setText(parseToText(json.optString("cont")));
//            content.setTextSize(16);
//            content.setMovementMethod(LinkMovementMethod.getInstance());
//            layoutParams.setMargins(12, 10, 10, 10);
//            content.setLayoutParams(layoutParams);
//            linearLayouts.addView(content);
//            linearLayouts.setVisibility(View.VISIBLE);
//
//            //动态添加五个按钮
//            String functionid = response.getString("functionid");
//            if (HStringUtil.isEmpty(functionid)) return;
//            String[] strings = functionid.split(",");
//            linearLayouts.addView(onCreateMoreView(strings, entity, mPitent, response));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
//
//    public View onCreateMoreView(String[] strings, final MessageEntity entity, final Button mPitent, final JSONObject response) {
//        final View view = mInflater.inflate(R.layout.good_bad_share_layout, null);
//        try {
//            final JSONObject json = new JSONObject(response.optString("retlist").toString());
//            int id;
//            for (int i = 0; i < strings.length; i++) {
//                if (strings[i].length() == 1) {
//                    id = Integer.valueOf(strings[i]);
//                    if (id == 1) {//更多
//                        ImageView more_icon = (ImageView) view.findViewById(R.id.more_icon);
//                        view.findViewById(R.id.line1).setVisibility(View.VISIBLE);
//                        more_icon.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//
//                                oncliCkTouch(entity, mPitent, 1, true);
//                            }
//                        });
//                    } else if (id == 4) {//分享
//                        ImageView share_icon = (ImageView) view.findViewById(R.id.share_icon);
//                        view.findViewById(R.id.line4).setVisibility(View.VISIBLE);
//                        share_icon.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (!isClick) return;
//                                View popV = mActivity.getWindow().getDecorView().findViewById(R.id.chat_info_lv);
////                                HttpUrls httpUrls = HApplication.getHttpUrls();
////                                DialogUtils.sharePopOut(mActivity, popV, response.optString("retDiseaseName") + "-" + json.optString("cont"), httpUrls.SHARE_CONTENT_LINT + "ID=" + response.optString("retDiseaseName"));
//                            }
//                        });
//                    } else if (id == 5) {//语音
//                        final ImageView talk_icon = (ImageView) view.findViewById(R.id.talk_icon);
//                        view.findViewById(R.id.line5).setVisibility(View.VISIBLE);
//                        talk_icon.setSelected(false);
//                        talk_icon.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (!isClick) return;
//                                talk_icon.setSelected(!talk_icon.isSelected());
//                                mSpeechUtils.startPeed(json.optString("cont"), talk_icon);
//                            }
//                        });
//                    } else if (id == 6) {//查看图片
//                        final ImageView pic_icon = (ImageView) view.findViewById(R.id.pic_icon);
//                        view.findViewById(R.id.line6).setVisibility(View.VISIBLE);
//                        pic_icon.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                if (!isClick) return;
//                                Intent intent = new Intent(mActivity, DisplyContentPicActivity.class);
//                                intent.putExtra(DisplyContentPicActivity.PATHS, response.optString("functionname"));
//                                intent.putExtra(DisplyContentPicActivity.TITLE_NAME, response.optString("retDiseaseName"));
//                                mActivity.startActivity(intent);
//                            }
//                        });
//                    }
//                } else {
//                    String[] splits = strings[i].split("-");
//                    id = Integer.valueOf(splits[0]);
//                    final ImageView good_btn = (ImageView) view.findViewById(R.id.good_btn);
//                    final ImageView bad_icon = (ImageView) view.findViewById(R.id.bad_icon);
//                    if (id == 2) {//好评
//                        view.findViewById(R.id.line2).setVisibility(View.VISIBLE);
//                        good_btn.setEnabled("1".equals(splits[1]));
//                        good_btn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                reviewOnChangeHttp(0, entity, good_btn, bad_icon);
//                            }
//                        });
//                    } else if (id == 3) {//差评
//                        view.findViewById(R.id.line3).setVisibility(View.VISIBLE);
//                        bad_icon.setEnabled("1".equals(splits[1]));
//                        bad_icon.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                reviewOnChangeHttp(1, entity, good_btn, bad_icon);
//                            }
//                        });
//                    }
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return view;
//    }
//
//    /**
//     * 好评,差评
//     *
//     * @param i
//     * @param entity
//     * @param good_btn
//     * @param bad_icon
//     */
//    private void reviewOnChangeHttp(final int i, MessageEntity entity, final ImageView good_btn, final ImageView bad_icon) {
//        if (!isClick) return;
//        RequestParams params = new RequestParams();
//        String name = entity.getContentJsonArray().optJSONObject(1).optString("retDiseaseName");
//        params.put("CUSTOMER_ID", LoginServiceManegerB.instance().getLoginId());
//        params.put("terminalId", SystemUtils.getIMEI(mActivity));
//        params.put("DiseaseName", name);
//        params.put("btnflag", "1");
//        params.put("EvaluateState", "" + i);
//        HttpRestClientB.doHttpCusevaluationDisease(params, new JsonHttpResponseHandler(mActivity) {
//            @Override
//            public void onSuccess(JSONObject response) {
//                super.onSuccess(response);
//                ToastUtil.showShort(mActivity, response.optString("message"));
//                //
//                if (1 == response.optInt("code"))
//                    good_btn.setEnabled(i == 1);
//                bad_icon.setEnabled(i != 1);
//            }
//        });
//    }
//
//    /**
//     * 附近医院
//     * http://192.168.16.65:9090/dmys/nearbyHospital?POSITION_X=&POSITION_Y=
//     *
//     * @param strings
//     * @param groups
//     * @param js
//     * @param entity
//     * @param id      99  附近医院  101 附近药品
//     */
//    private synchronized void requestHttpHistopty(String[] strings, final LinearLayout groups, final JSONObject js, MessageEntity entity, String id) {
//        RequestParams params = new RequestParams();
//        params.put("POSITION_X", strings[0]);
//        params.put("POSITION_Y", strings[1]);
//        params.put("customerId", LoginServiceManegerB.instance().getLoginId());
//        if ("99".equals(id))
//            params.put("select_type", "1");
//        else
//            params.put("select_type", "2");
//        params.put("diseasename", HStringUtil.stringFormat(js.optString("retDiseaseName")));
//        params.put("officename", HStringUtil.stringFormat(js.optString("retOfficeName")));
//        params.put("btnflag", "1");
//        groups.removeAllViews();
//        HttpRestClientB.doHttpNEARBYHOSPITAL2(params, new JsonHttpResponseHandler() {
//                    @Override
//                    public void onFinish() {
//                        super.onFinish();
//                        mLodingFragmentDialog.dismissAllowingStateLoss();
//                    }
//
//                    @Override
//                    public void onSuccess(JSONObject response) {
//                        super.onSuccess(response);
//                        if (response.optInt("code") != 1) return;
//                        groups.setVisibility(View.VISIBLE);
//                        JSONArray array = response.optJSONObject("result").optJSONArray("c_main30");
//                        groups.removeAllViews();
//                        View btnGroup;
//                        Button button;
//                        for (int i = 0; i < array.length(); i++) {
//                            btnGroup = mInflater.inflate(R.layout.layout_item_btn_single, null);
//                            groups.addView(btnGroup);
//                            JSONObject json = array.optJSONObject(i);
//                            button = (Button) btnGroup.findViewById(R.id.btn);
//                            button.setText(json.optString("NAME"));
//                            button.setTag(json);
//                            button.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View v) {
//                                    JSONObject jscLocation = null;
//                                    try {
//                                        jscLocation = new JSONObject(v.getTag().toString());
//                                        if (jscLocation.optInt("POSITION_Y") == 0 && jscLocation.optInt("POSITION_X") == 0) {
//                                            onSendTalkWithServicer(jscLocation, js);
//                                        } else {
//                                            MessageEntity msg = new MessageEntity();
//                                            msg.setSendFlag(false);
//                                            msg.setId(String.valueOf(System.currentTimeMillis()));
//                                            msg.setisLocation(true);
//                                            msg.setLocationMsg(v.getTag().toString());
//                                            msg.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
//                                            onDataChange(msg);
//                                        }
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }
//                            });
//                            if (i == array.length() - 1) {
//                                LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnGroup.getLayoutParams();
//                                layoutParams.setMargins(0, 0, 0, 20);
//                            }
//
//                        }
//                    }
//                }
//        );
//
//    }
//
//    /**
//     * 收听语音
//     */
//    public void onSound() {
//
//    }
//
//    /**
//     * 点击自主选择医院
//     *
//     * @param jscLocation
//     * @param js
//     */
//    private void onSendTalkWithServicer(JSONObject jscLocation, JSONObject js) {
//
//        RequestParams params = new RequestParams();
//        params.put("Type", "XiaoYi_Guahao");
//        params.put("TERMINAL_ID", SystemUtils.getIMEI(mActivity));
//        params.put("CUSTOMER_ID", LoginServiceManegerB.instance().getLoginId());
//        params.put("REGISTER_HOSPITAL", HStringUtil.stringFormat(jscLocation.optString("NAME")));
//        params.put("diseasename", HStringUtil.stringFormat(js.optString("retDiseaseName")));
//        params.put("officename", HStringUtil.stringFormat(js.optString("retOfficeName")));
//        params.put("btnflag", "1");
//        HttpRestClientB.doHttpcreateOrder(params, new JsonHttpResponseHandler(mActivity) {
//            @Override
//            public void onSuccess(JSONObject response) {
//                super.onSuccess(response);
//                if (response.optInt("code") == 1) {
//                    JSONObject jo = response.optJSONObject("result");
//                    MessageEntity entity = new MessageEntity();
//                    entity.setContent(jo.optString("retList"));
//                    entity.setSERVICE_TYPE(jo.optString("SERVICE_TYPE"));
//                    entity.setSendFlag(false);
//                    HTalkApplication.getApplication().mTalkServiceType = jo.optString("SERVICE_TYPE");
//                    if (!"".equals(HStringUtil.stringFormat(jo.optString("retServiceId"))))
//                        HTalkApplication.getApplication().setMTalkServiceId(HStringUtil.stringFormat(jo.optString("retServiceId")));
//                    entity.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
//                    onDataChange(entity);
//                } else {
//                    ToastUtil.showShort(response.optString("message"));
//                }
//            }
//        });
//
//    }
//
//    static class LeftViewHolder {
//        public Button headerImageV;
//        public TextView timeTextV;
//        public ImageView imageView_chat, imageViewSound, imageViewDianDian;
//        public CircleImageView chat_head;
//        public View timeView;
//        public TextView contentTextV;
//        public TextView textView_sound;
//        public MessageEntity entity;
//        public LinearLayout mViewGroup;//添加空间的父布局
//        public LinearLayout mContent_layout;//添加空间的父布局
//    }
//
//    static class RightViewHolder {
//        public TextView timeTextV;
//        public TextView contentTextV;
//        public ImageView imageView;
//        public MessageEntity entity;
//        public View timeView;
//    }
//
//    private void getLocation(final LinearLayout groups, final JSONObject js, final MessageEntity entity, final String id) {
//        if (isOncliBtn()) return;
//        mLodingFragmentDialog = LodingFragmentDialogB.showLodingDialog(mActivity.getSupportFragmentManager(), "正在获取您的位置");
//
////        if (HApplication.getApplication().location[0] == null || HApplication.getApplication().location[1] == null) {
////
////            BaiduLocationManager.getInstance(mActivity).setCallBack(new BaiduLocationManager.LocationListenerCallBack() {
////                @Override
////                public void locationListenerCallBack(double longitude, double latitude) {
////                    HApplication.getApplication().location = new String[]{longitude + "", latitude + ""};
////                    requestHttpHistopty(HApplication.getApplication().location, groups, js, entity, id);
////                }
////
////                @Override
////                public void locationListenerCallBackFaile() {
////                }
////            });
////            BaiduLocationManager.getInstance().startLocation();
////        } else {
////            requestHttpHistopty(HApplication.getApplication().location, groups, js, entity, id);
////        }
//    }
//
//    public void addData(MessageEntity jo) {
//        jo.isAdd = true;
//        isReceive = !jo.isSendFlag();
//        isOpenSound = SharePreUtils.getSoundsBoolean(mActivity);
//        mList.add(jo);
//        notifyDataSetChanged();
//    }
//
//    private CharSequence parseToText(String content) {
//
//        SpannableStringBuilder builder = new SpannableStringBuilder();
//        Pattern mPattern = Pattern.compile("<a>([^<]*)</a>");
//        final Matcher matcher = mPattern.matcher(content);
//        int endnume = 0;
//        while (matcher.find()) {
//            final CharSequence con = matcher.group(1);
//            builder.append(content.subSequence(endnume, matcher.start()));
//            builder.append(con);
//            endnume = matcher.end();
//            builder.setSpan(new ClickableSpan() {
//                                @Override
//                                public void onClick(View widget) {
//                                    chatWithServiceFromDrugs(con.toString());
//                                }
//
//                                @Override
//                                public void updateDrawState(TextPaint ds) {
//                                    super.updateDrawState(ds);
//                                    ds.setColor(Color.RED);       //设置文件颜色
//                                    ds.setUnderlineText(true);      //设置下划线
//                                }
//                            }, builder.length() - matcher.group(1).length(), builder.length(),
//                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//            builder.setSpan(new UnderlineSpan(), builder.length() - matcher.group(1).length(), builder.length(),
//                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//        if (content.length() > endnume) {
//            builder.append(content.subSequence(endnume, content.length()));
//        }
//        return builder;
//    }
//
//    /**
//     * {
//     * code: "1",
//     * message: "查询成功",
//     * result: {
//     * retServiceId: "225305",
//     * retList: "好的，稍等一小会儿~",
//     * SERVICE_TYPE: "99999",
//     * timeout: "5",
//     * function_parm: "我要购买胃蛋白酶(购买)"
//     * }
//     * } 买药
//     * http://220.194.46.204/dmys/createOrder?key=18447051826c13cc63420b7a82703d1b&appID=com.dummyvision.DummyXiaoYi&Type=XiaoYi_text_kefu_DaoRu&smstext=%E8%83%83%E8%9B%8B%E7%99%BD%E9%85%B6%28%E8%B4%AD%E4%B9%B0%29&CUSTOMER_ID=225245&TERMINAL_ID=8332B911-61BD-4D25-92F8-8B8E43CCE16B
//     */
//    private void chatWithServiceFromDrugs(final String txt) {
////        sendMsgToSelf(txt);
//        RequestParams params = new RequestParams();
//        params.put("key", HTalkApplication.mKey);
//        params.put("appID", HTalkApplication.getApplication().getPackageName());
//        params.put("Type", "XiaoYi_text_kefu_DaoRu");
//        params.put("LNG", HTalkApplication.getApplication().location[0]);
//        params.put("LAT", HTalkApplication.getApplication().location[1]);
//        params.put("smstext", txt);
//        params.put("CUSTOMER_ID", LoginServiceManegerB.instance().getLoginId());
//        params.put("TERMINAL_ID", HTalkApplication.getDeviceNumber());
//        params.put("btnflag", "1");
//        HttpRestClientB.doHttpcreateOrder(params, new JsonHttpResponseHandler() {
//                    @Override
//                    public void onSuccess(JSONObject response) {
//                        super.onSuccess(response);
//                        if (response.optInt("code") == 1) {
//                            JSONObject jo = response.optJSONObject("result");
//                            HTalkApplication.getApplication().mTalkServiceType = jo.optString("SERVICE_TYPE");
//                            HTalkApplication.getApplication().setMTalkServiceId(jo.optString("retServiceId"));
//                            if (!HStringUtil.isEmpty(jo.optString("href"))) {//此处判断，如果有药品链接就跳转，没有就请求客服
//                                if (jo.optString("href").contains("jd.com")) {
//                                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//                                        Uri uri = Uri.parse(jo.optString("href"));
//                                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                                        mActivity.startActivity(intent);
//                                    } else {
//                                        CommonWebUIActivity.startWeb(mActivity, jo.optString("href"), jo.optString("title"));
//                                    }
//                                } else {
//                                    CommonWebUIActivity.startWeb(mActivity, jo.optString("href"), jo.optString("title"));
//                                }
//                            } else {
//                                sendMsgToSelf(txt);//没有链接，请求客服
//                                sendMsgToService(jo.optString("retList"));
//                            }
//                        }
//                    }
//
//                }
//        );
//    }
//
//    public void sendMsgToJD(JSONObject json) {
//        sendMsgToSelf(json.optString("JD_TEXT"));//没有链接，请求客服
//        sendMsgToService(json.optString("retList"));
//    }
//
//    /**
//     * {
//     * code: "1",
//     * message: "查询成功",
//     * result: {
//     * retServiceId: "225305",
//     * retList: "好的，稍等一小会儿~",
//     * SERVICE_TYPE: "99999",
//     * timeout: "5",
//     * function_parm: "我要购买胃蛋白酶(购买)"
//     * }
//     * <p>
//     * <p>
//     * <p>
//     * <p>
//     * <p>
//     * DummyXiaoyiURL *url = [DummyXiaoyiURL urlWithServlet:@"dmys/createOrder", nil];
//     * [url appendKey:@"Type" value:@"XiaoYi_Guahao"];
//     * [url appendKey:@"TERMINAL_ID" value:[[AppConfig globalConfig] terminalId]];
//     * [url appendKey:@"CUSTOMER_ID" value:[[CustomerInfo me] customerId]];
//     * [url appendKey:@"REGISTER_HOSPITAL" value:hospital];
//     * [url appendKey:@"officename" value:[attribute UTF8String:@"officename"]];
//     * [url appendKey:@"diseasename" value:[attribute UTF8String:@"diseasename"]];
//     * }  我要挂号请求
//     */
//
//    private void chatWithServiceFromGuaHao(String txt, JSONObject jsonObject, MessageEntity entity) {
//        if (!isClick) return;
//        if (!LoginServiceManegerB.isLogined()) {
//            DoubleBtnFragmentDialog.showDefault(mActivity.getSupportFragmentManager(), "请登录后使用该功能", "取消", "登录", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
//                @Override
//                public void onDismiss(DialogFragment fragment) {
//                }
//
//                @Override
//                public void onClick(DialogFragment fragment, View v) {
//                    mActivity.startActivity(new Intent(mActivity, AtyConsultMain.class));
//                }
//            });
//            return;
//        }
//        sendMsgToSelf(txt);
//        RequestParams params = new RequestParams();
//        params.put("Type", "XiaoYi_Guahao");
//        params.put("TERMINAL_ID", HTalkApplication.getApplication().getDeviceNumber());
//        params.put("CUSTOMER_ID", LoginServiceManegerB.instance().getLoginId());
//        params.put("REGISTER_HOSPITAL", jsonObject.optString("NAME"));
//        params.put("btnflag", "1");
//        params.put("diseasename", HStringUtil.stringFormat(jsonObject.optString("diseasename")));
//        params.put("officename", HStringUtil.stringFormat(jsonObject.optString("officename")));
//        HttpRestClientB.doHttpcreateOrder(params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, JSONObject response) {
//                super.onSuccess(statusCode, response);
//                if (response.optInt("code") == 1) {
//                    JSONObject jo = response.optJSONObject("result");
//                    HTalkApplication.getApplication().mTalkServiceType = jo.optString("SERVICE_TYPE");
//                    HTalkApplication.getApplication().setMTalkServiceId(jo.optString("retServiceId"));
//                    sendMsgToService(jo.optString("retList"));
//                }
//            }
//        });
//    }
//
//    /**
//     * 客户发送
//     *
//     * @param content
//     */
//
//    public void sendMsgToSelf(String content) {
//        MessageEntity entity = new MessageEntity();
//        entity.setSendFlag(true);
//        entity.setType(MessageEntity.TYPE_TEXT);
//        entity.setContent(content);
//        entity.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
//        onDataChange(entity);
//    }
//
//    /**
//     * 客服发送
//     *
//     * @param content
//     */
//    public void sendMsgToService(String content) {
//        MessageEntity entity = new MessageEntity();
//        entity.setSendFlag(false);
//        entity.setSenderId(HTalkApplication.getApplication().getmTalkServiceId());
//        entity.setType(MessageEntity.TYPE_TEXT);
//        entity.setContent(content);
//        entity.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
//        onDataChange(entity);
//    }
//
//    /**
//     * 卡片
//     *
//     * @param v      按钮
//     * @param object 整条消息
//     * @param code   code
//     */
//    public void onTouchBtn(View v, TyJsonObject object, int code) {
//        if (!isClick) return;
//        JSONObject j = (JSONObject) v.getTag();
//        if (!LoginServiceManegerB.isLogined()) {
//            DoubleBtnFragmentDialog.showDefault(mActivity.getSupportFragmentManager(), "请登录后使用该功能", "取消", "登录", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
//                @Override
//                public void onDismiss(DialogFragment fragment) {
//                }
//
//                @Override
//                public void onClick(DialogFragment fragment, View v) {
////                    mActivity.startActivity(new Intent(mActivity, LoginRegisterActivity.class));
//                }
//            });
//            return;
//        }
//        switch (code) {
//            case 1010://查看商品
//                if (HStringUtil.isEmpty(object.optString("ORDER_ID"))) {//判断有无订单号是否带去京东
//                    CommonWebUIActivity.startWeb(mActivity, HttpRestClientB.getmHttpUrlsB().RETURNHTML + "?url=" + j.optString("FUNCTION_PARA"), j.optString("FUNCTION_TITLE"));
//                } else {
//                    CommonWebUIActivity.startWeb(mActivity, HttpRestClientB.getmHttpUrlsB().RETURNHTML + "?url=" + j.optString("FUNCTION_PARA") + "&ORDER_ID=" + object.optString("ORDER_ID") + "&Type=buttonReturn", j.optString("FUNCTION_TITLE"));
//                }
//                break;
//            case 1020://"FUNCTION_PARA" -> "4008010231,,2871"
//                SystemUtils.callPhone(mActivity, j.optString("FUNCTION_PARA"));
//                break;
//            case 1040://案例
//                compareToPic(object);
//                break;
//        }
//    }
//
//    /**
//     * 支付
//     *
//     * @param id
//     * @param cusid
//     */
//    private void payBeforRespon(final String id, String cusid) {
//        if (!isClick) return;
//        if (!LoginServiceManegerB.isLogined()) {
//            DoubleBtnFragmentDialog.showDefault(mActivity.getSupportFragmentManager(), "请登录后使用该功能", "取消", "登录", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
//                @Override
//                public void onDismiss(DialogFragment fragment) {
//                }
//
//                @Override
//                public void onClick(DialogFragment fragment, View v) {
////                    mActivity.startActivity(new Intent(mActivity, LoginRegisterActivity.class));
//                }
//            });
//            return;
//        }
//        RequestParams params = new RequestParams();
//        params.put("Type", "isCanPay");
//        params.put("ORDER_ID", id);
//        params.put("btnflag", "1");
//        params.put("CUSTOMER_ID", cusid);
//        HttpRestClientB.doHttpPAYMENTMESG(params, new JsonHttpResponseHandler(mActivity) {
//            @Override
//            public void onSuccess(JSONObject response) {
//                super.onSuccess(response);
//                if (response.optInt("code") == 1) {
//                    // TODO: 2016-3-29 支付
////                    Intent intent = new Intent(mActivity, PayChoiseActivity.class);
////                    intent.putExtra(PayChoiseActivity.ORDER_ID, id);
////                    mActivity.startActivity(intent);
//                } else {
//                    SingleBtnFragmentDialog.showDefault(mActivity.getSupportFragmentManager(), response.optString("message"));
//                }
//            }
//        });
//    }
//
//    /**
//     * 同意 拒绝
//     *
//     * @param json
//     * @param type
//     * @param v
//     */
//    private void agreeRespon(TyJsonObject json, String type, final TextView v) {
//        if (!isClick) return;
//        if (!LoginServiceManegerB.isLogined()) {
//            DoubleBtnFragmentDialog.showDefault(mActivity.getSupportFragmentManager(), "请登录后使用该功能", "取消", "登录", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
//                @Override
//                public void onDismiss(DialogFragment fragment) {
//                }
//
//                @Override
//                public void onClick(DialogFragment fragment, View v) {
//                    mActivity.startActivity(new Intent(mActivity, AtyConsultMain.class));
//                }
//            });
//            return;
//        }
//        RequestParams params = new RequestParams();
//        params.put("SERVICE_ID", json.optString("customerId"));
//        params.put("CUSTOMER_ID", json.optString("sms_target_id"));
//        params.put("DocName", json.optString("DocName"));
//        params.put("Price", json.optString("Price"));
//        params.put("dateid", json.optString("dateid"));
//        params.put("departments", json.optString("departments"));
//        params.put("departmentsId", json.optString("departmentsId"));
//        params.put("hospitalId", json.optString("hospitalId"));
//        params.put("professionalTitles", json.optString("professionalTitles"));
//        params.put("agreeType", type);
//        params.put("Type", "UsrAgree");
//        params.put("ORDER_ID", json.optString("ORDER_ID"));
//        params.put("btnflag", "1");
//        HttpRestClientB.doHttpcreateOrder(params, new JsonHttpResponseHandler(mActivity) {
//            @Override
//            public void onSuccess(JSONObject response) {
//                super.onSuccess(response);
////                SingleBtnFragmentDialog.showDefault(mActivity.getSupportFragmentManager(), response.optString("message"));
//                if (response.optInt("code") == 1) {
//                    v.setText("已" + v.getText().toString());
//                }
//            }
//        });
//    }
//
//    /**
//     * 比较
//     * DummyXiaoyiURL *url = [DummyXiaoyiURL urlWithServlet:@"dmys/createOrder", nil];
//     * [url appendKey:@"Type" value:@"queryPicUse"];
//     * [url appendKey:@"MERCHANT_ID" value:object.merchantId];
//     * [url appendKey:@"GOODS_ID" value:object.goodsId];
//     * [url appendKey:@"ORDER_ID" value:object.orderId];
//     */
//    private void compareToPic(TyJsonObject object) {
//        RequestParams params = new RequestParams();
//        params.put("Type", "queryPicUse");
//        params.put("MERCHANT_ID", object.optString("MERCHANT_ID"));
//        params.put("GOODS_ID", object.optString("GOODS_ID"));
//        params.put("ORDER_ID", object.optString("ORDER_ID"));
//        params.put("btnflag", "1");
//        HttpRestClientB.doHttpcreateOrder(params, new JsonHttpResponseHandler(mActivity) {
//            @Override
//            public void onSuccess(JSONObject response) {
//                super.onSuccess(response);
//                if (response.optInt("code") == 1) {
//                    MessageEntity entity = new MessageEntity();
//                    entity.setSendFlag(false);
//                    entity.setSenderId(HTalkApplication.getApplication().getmTalkServiceId());
//                    entity.setType(MessageEntity.TYPE_TEXT);
//                    entity.setContent(response.optString("result"));
//                    entity.setDate(Long.valueOf(TimeUtil.getCurrentTimeAsNumber()));
//                    entity.setCARDTYPE(HStringUtil.stringFormat(String.valueOf(CardType.COMPARE_CARD)));
//                    onDataChange(entity);
//                    mListView.setSelection(getCount());
//                }
//            }
//        });
//    }
//
//    /**
//     * 停止语音
//     */
//    public void stopRecoard() {
//        mSpeechUtils.stopPeed();
//    }
//
//    /**
//     * 释放语音
//     */
//    public void destorySpeed() {
//        mSpeechUtils.destory();
//    }
//
//    /**
//     * 数据的最后准备
//     *
//     * @param entity
//     * @param j
//     * @return
//     */
//    private JSONObject onreadyData(MessageEntity entity, JSONObject j) {
//        JSONObject jsonObject = new JSONObject();
//        try {
//            String id = j.optString("id");
////            JSONObject json = entity.getContentJsonArray().getJSONObject(1);
//            JSONObject json = new JSONArray(entity.getContent()).getJSONObject(0);
////            jsonObject = entity.getContentJsonArray().getJSONObject(2);
//            if (jsonObject != null) {
//                jsonObject.put("smsTEXT", json.optString("retserial") + " " + id);
//                jsonObject.put("smsCode", json.optString("retCode"));
//                jsonObject.put("smsserial", json.optString("retserial"));
//                jsonObject.put("smsClass", json.optString("retClass"));
//                jsonObject.put("vpage", json.optString("vpage"));
//                jsonObject.put("zjbb_jump", json.optString("zjbb_jump"));
//                jsonObject.put("btnflag", "1");
//                if (!jsonObject.has("original_smstext"))
//                    jsonObject.put("original_smstext", "");
//                jsonObject.put("Customerid", LoginServiceManegerB.instance().getLoginId());
//                return jsonObject;
//            } else {
//                return null;
//            }
//
//        } catch (JSONException e) {
//            return null;
//        }
//    }
//
//    private JSONObject getJsonObject(MessageEntity entity) {
//        try {
//            if (entity.getContentJsonArray() != null && entity.getContentJsonArray().length() != 0)
//                return entity.getContentJsonArray().getJSONObject(0);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
//
//    /**
//     * 获取聊天内容
//     *
//     * @param jsonObject
//     * @return
//     */
//    public String getContent(JSONObject jsonObject) {
//        try {
//            return jsonObject.getString("smsTEXT");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        return "";
//    }
//
//    @Override
//    public void onClick(View v) {
//
//    }
//
//
//    public boolean doEquestoId(String mesgId) {
//        boolean isId = false;
//        for (int i = 0; i < mList.size(); i++) {
//            if (mesgId.equals(mList.get(i).getId())) {
//                isId = true;
//                break;
//            }
//        }
//        return isId;
//    }
//
//    public void addAll(List<MessageEntity> lists) {
//        mList.addAll(lists);
//        notifyDataSetChanged();
//    }
//
//    public void removeAll() {
//        mList.clear();
//        notifyDataSetChanged();
//    }
//
//    public void startAnimation(ImageView imageView) {
//        if (animation != null) {
//            animation.selectDrawable(0);
//            animation.stop();
//
//            animation = null;
//            animation = (AnimationDrawable) imageView.getDrawable();
//            animation.start();
//        } else {
//            animation = (AnimationDrawable) imageView.getDrawable();
//            animation.start();
//        }
//    }
//
//    public void stopAnimation() {
//        if (animation != null) {
//            animation.selectDrawable(0);
//            animation.stop();
//
//            animation = null;
//        }
//    }
//
//
//    PopupWindow popOfficeWindow;
//    LinearLayout popOfficeView;
//
//    private void showPopOfficeWindow(View view, final String url) {
//        if (popOfficeWindow != null && popOfficeWindow.isShowing()) {
//            popOfficeWindow.dismiss();
//            // titleRightBtn2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.selete_room, 0, 0, 0);
//            return;
//        }
//        // titleRightBtn2.setCompoundDrawablesWithIntrinsicBounds(R.drawable.im_more_press, 0, 0, 0);
//
//        if (popOfficeWindow == null) {
//            popOfficeView = new LinearLayout(mActivity);
//            popOfficeView.setPadding(0, 10, 0, 10);
//            popOfficeView.setBackgroundColor(0xfffafbf6);
//            popOfficeView.setGravity(Gravity.CENTER);
//            popOfficeView.setOrientation(LinearLayout.HORIZONTAL);
//            TextView textView = new TextView(mActivity);
//            textView.setTextColor(mActivity.getResources().getColor(R.color.imput_color));
//            textView.setText("转文字");
//            textView.setGravity(Gravity.CENTER);
//            popOfficeView.setBackgroundDrawable(mActivity.getResources().getDrawable(R.drawable.longenter_pop));
//            popOfficeView.addView(textView);
//            popOfficeWindow = new PopupWindow(popOfficeView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
//            popOfficeWindow.setTouchable(true);
//            // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
//            // 我觉得这里是API的一个bug
//            popOfficeWindow.setBackgroundDrawable(new BitmapDrawable());
////            popOfficeWindow.setAnimationStyle(R.style.poPPreview);
////            return;
//        } else {
//            // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
//            // 我觉得这里是API的一个bug
//            popOfficeWindow.setBackgroundDrawable(new BitmapDrawable());
////            popOfficeWindow.setAnimationStyle(R.style.poPPreview);
//
//        }
////        popOfficeWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
////            @Override
////            public void onDismiss() {
////                //   titleLeftBtn.setCompoundDrawablesWithIntrinsicBounds(R.drawable.im_more, 0, 0, 0);
////                windowLp.alpha = 1.0f;
////                getWindow().setAttributes(windowLp);
////            }
////        });
////        windowLp.alpha = 0.8f;
////        getWindow().setAttributes(windowLp);
//        popOfficeView.getChildAt(0).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(mActivity, SoundToTextActivity.class);
//                intent.putExtra("url", url);
//                mActivity.startActivity(intent);
//                popOfficeWindow.dismiss();
//            }
//        });
//
//        popOfficeWindow.showAsDropDown(view, 0, -2 * view.getHeight());
//    }
//
//
//    public void playVideo(JSONObject jsonObject, LeftViewHolder view) {
//        mWhriteDrawable = mResources.getDrawable(R.drawable.item_chat_l_white_bg);
//        view.mContent_layout.setBackgroundDrawable(mWhriteDrawable);
//        view.contentTextV.setTextColor(mChatTCommonColor);
//        JSONObject object = null;
//        try {
//            object = new JSONObject(jsonObject.optString("retlist"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        view.contentTextV.setText(object.optString("cont"));
//        String urls[] = jsonObject.optString("videoUrl").split("#");
//        String img[] = jsonObject.optString("thumbnailUrl").split("#");
//        String length[] = jsonObject.optString("videolength").split("#");
//        view.mViewGroup.removeAllViews();
//        for (int i = 0; i < urls.length; i++) {
//            final View viewL = mInflater.inflate(R.layout.video_view, null);
//            Picasso.with(mActivity).load(img[i]).placeholder(R.drawable.video_src_erral).error(R.drawable.video_src_erral).into((ImageView) viewL.findViewById(R.id.video_bg));
//            ((TextView) viewL.findViewById(R.id.video_lenth)).setText(length[i]);
//            ImageView viewPlay = (ImageView) viewL.findViewById(R.id.video_play);
//            viewPlay.setTag(urls[i]);
//            viewPlay.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(mActivity, InternetVideoDemo.class);
//                    intent.putExtra("url", v.getTag().toString());
//                    mActivity.startActivity(intent);
//                }
//            });
//            view.mViewGroup.addView(viewL);
//        }
//
//    }
//}