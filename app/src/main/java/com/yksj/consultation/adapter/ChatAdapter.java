package com.yksj.consultation.adapter;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.chatting.ChatActivity;
import com.yksj.consultation.son.consultation.AtyPatientMassage;
import com.yksj.consultation.son.consultation.consultationorders.AtyOrderDetails;
import com.yksj.consultation.son.consultation.consultationorders.AtyOrdersDetails;
import com.yksj.consultation.son.consultation.main.ConsultActivity;
import com.yksj.consultation.son.consultation.member.PlayVideoActiviy;
import com.yksj.consultation.son.consultation.outpatient.AtyOutPatientDetail;
import com.yksj.consultation.son.home.DoctorStudioActivity;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.MessageEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.DataParseUtil;
import com.yksj.healthtalk.utils.EntityObjUtils;
import com.yksj.healthtalk.utils.FaceParse;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.TimeUtil;

import org.apache.commons.lang.math.NumberUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;
import org.universalimageloader.core.assist.FailReason;
import org.universalimageloader.core.assist.ImageLoadingListener;
import org.universalimageloader.core.assist.ImageSize;
import org.universalimageloader.core.assist.MemoryCacheUtil;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 聊天适配器
 *
 * @author zhao
 */
public class ChatAdapter extends BaseAdapter {
    /**
     * 需要显示多少种不同的布局
     */
    private final int VIEW_TYPE_COUNT = 16;
    private final ChatActivity mContext;
    private final LayoutInflater mInflater;
    private final ImageLoader mImageLoader;
    private final DisplayImageOptions mGalleryDisplayOptions;// 聊天图片
    private final DisplayImageOptions mMapDisplayOptions;// 地图
    private final String groupCreaterId;
    private String objectType;

    private final SimpleDateFormat hourSdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
    private final SimpleDateFormat yearSdf = new SimpleDateFormat(
            "yy年-MM月-dd日 HH:mm", Locale.getDefault());

    private final LinkedHashMap<MessageEntity, MessageEntity> mChoosedMap = new LinkedHashMap<MessageEntity, MessageEntity>() {
    };// 已经选中的消息
    private final List<MessageEntity> mList = new ArrayList<MessageEntity>();
    private final FaceParse mFaceParse;// 表情
    private final ConcurrentHashMap<String, Object> mInformationMap;// 用户资料缓存
    private boolean isEditor = false;
    final ImageSize mImageSize;
    final Drawable mGroupDrawable;
    final Drawable mUserDrawable, manDrawable;

    public ChatAdapter(ChatActivity context, boolean isGroupChat,
                       String chatId, String objectType) {
        Resources resources = context.getResources();
        int size = resources.getDimensionPixelSize(R.dimen.chat_header_size);
        mImageSize = new ImageSize(size, size);
        mGroupDrawable = new BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.default_head_group));
        mGroupDrawable.setBounds(0, 0, mImageSize.getWidth(), mImageSize.getHeight());
        mUserDrawable = new BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.unkonw_head_mankind));
        manDrawable = new BitmapDrawable(resources, BitmapFactory.decodeResource(resources, R.drawable.default_head_mankind_24));
        mUserDrawable.setBounds(0, 0, mImageSize.getWidth(), mImageSize.getHeight());
        this.mInformationMap = new ConcurrentHashMap<String, Object>(HTalkApplication.getAppData().cacheInformation);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this.mImageLoader = ImageLoader.getInstance();
        this.groupCreaterId = chatId;
        this.objectType = objectType;
        this.mGalleryDisplayOptions = DefaultConfigurationFactory.createGalleryDisplayImageOptions(context);
        this.mMapDisplayOptions = DefaultConfigurationFactory.createChatMapDisplayImageOptions(context);
        this.mFaceParse = FaceParse.getChatFaceParse(context);
    }

    /**
     * 设置聊天时间
     *
     * @param textView
     * @param lastTime
     * @param
     */
    private void setChatTime(TextView textView, long lastTime, long nextTime) {
        // 计算间隔时间
        long intervalTime = nextTime - lastTime;
        intervalTime = Math.abs(intervalTime);
        // 间隔两分钟给予显示
        if (intervalTime <= 120000) {
            textView.setVisibility(View.GONE);
        } else {
            try {
                String timeStr = null;
                // 消息时间
                Date nextday = new Date(nextTime);
                int temp = TimeUtil.getChatTime(nextTime);
                switch (temp) {
                    case 0:
                        timeStr = "今天 " + hourSdf.format(nextday);
                        break;
                    case 1:
                        timeStr = "昨天 " + hourSdf.format(nextday);
                        break;
                    case 2:
                        timeStr = "前天" + hourSdf.format(nextday);
                        break;
                    default:
                        timeStr = yearSdf.format(nextday);
                        break;
                }
                if (TextUtils.isEmpty(timeStr)) {
                    textView.setVisibility(View.GONE);
                } else {
                    textView.setVisibility(View.VISIBLE);
                    textView.setText(timeStr);
                }
            } catch (Exception e) {
                e.printStackTrace();
                textView.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 全选
     */
    public void onSelectAll() {
        for (MessageEntity msg : mList) {
            mChoosedMap.put(msg, msg);
        }
        notifyDataSetChanged();
        onChooseSizeChange();
    }

    private void onChooseSizeChange() {
        ChatActivity mActivity = (ChatActivity) mContext;
        mActivity.onUpdateSelectedNumber(mChoosedMap.size());
    }

    /**
     * 查询用户资料
     *
     * @param
     */
    private void doQueryCustomerInfo(final CustomerInfoEntity infoEntity,
                                     final MessageEntity entity) {
        HttpRestClient.doGetCustomerInfoByCustId(infoEntity.getId(), new OkHttpClientManager.ResultCallback<JSONObject>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(JSONObject response) {
                JSONObject object = response.optJSONObject("result");
                CustomerInfoEntity customerInfoEntity = DataParseUtil.JsonToCustmerInfo(object.optJSONObject("patientInfo"));
                try {
                    EntityObjUtils.copyProperties(customerInfoEntity, infoEntity);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }, this);


        HttpRestClient.doHttpFindCustomerInfoByCustId(null, null,
                infoEntity.getId(), null, new ObjectHttpResponseHandler() {
                    @Override
                    public Object onParseResponse(String cotent) {
                        try {
                            JSONObject jsonObject = new JSONObject(cotent);
                            CustomerInfoEntity customerInfoEntity = DataParseUtil.jsonToCustmerInfo(jsonObject);
                            EntityObjUtils.copyProperties(customerInfoEntity, infoEntity);
                            return infoEntity;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return null;
                    }

                    @Override
                    public void onSuccess(int statusCode, Object response) {
                        notifyDataSetChanged();
                    }
                });
    }

    /**
     * 获得第一个id
     *
     * @return
     */
    public String getFirstMesgId() {
        String id = null;
        for (MessageEntity msg : mList) {
            if (msg.getType() != MessageEntity.TYPE_TIME) {
                id = msg.getId();
                break;
            }
        }
        if (id == null)
            id = String.valueOf(Long.MAX_VALUE);
        return id;
    }

    /**
     * 系统通知的第一个消息id
     *
     * @return
     */
    public String getHelperMesgId() {
        String id = null;
//		if(mList.size()==1 && SmartFoxClient.helperId.equals(mList.get(0).getSenderId()))
//			return	String.valueOf(Long.MAX_VALUE);

        for (MessageEntity msg : mList) {
            if (msg.getType() != MessageEntity.TYPE_TIME && !HStringUtil.isEmpty(msg.getId())) {
                id = msg.getId();
                break;
            }
        }
        if (id == null)
            id = String.valueOf(Long.MAX_VALUE);
        return id;
    }

    public boolean isEditor() {
        return isEditor;
    }

    public void setEditor(boolean isEditor) {
        if (this.isEditor == isEditor)
            return;
        this.isEditor = isEditor;
        if (isEditor) {
            mChoosedMap.clear();
        }
        onChooseSizeChange();
        notifyDataSetChanged();
    }

    /**
     * 改变为编辑模式
     */
    public void onEditorMode() {
        setEditor(true);
    }

    /**
     * 删除所有
     */
    public void onDeleteAll() {
        mList.clear();
        notifyDataSetChanged();
        mChoosedMap.clear();
        onChooseSizeChange();
    }

    /**
     * 选中删除
     *
     * @param list
     */
    public void onDeleteSelectedl(List<MessageEntity> list) {
        mList.removeAll(list);
        mChoosedMap.clear();
        notifyDataSetChanged();
        onChooseSizeChange();
    }

    public List<MessageEntity> getList() {
        return mList;
    }

    /**
     * 非编辑模式
     */
    public void onUnEditorMode() {
        setEditor(false);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public MessageEntity getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        MessageEntity entity = getItem(position);
        if (entity.getType() == MessageEntity.TYPE_TIME)
            return 0;
        // 群主发送
        /*
         * if(groupCreaterId != null &&
		 * entity.getSenderId().equals(groupCreaterId)){
		 * if(entity.isSendFlag()){ if(entity.getType() ==
		 * MessageEntity.TYPE_TEXT){ return 5; }else if(entity.getType() ==
		 * MessageEntity.TYPE_VOICE){ return 6; }else if(entity.getType() ==
		 * MessageEntity.TYPE_PICTURE){ return 7; }else if(entity.getType() ==
		 * MessageEntity.TYPE_LOCATION){ return 8; } }else{ if(entity.getType()
		 * == MessageEntity.TYPE_TEXT){ return -5; }else if(entity.getType() ==
		 * MessageEntity.TYPE_VOICE){ return -6; }else if(entity.getType() ==
		 * MessageEntity.TYPE_PICTURE){ return -7; }else if(entity.getType() ==
		 * MessageEntity.TYPE_LOCATION){ return -8; } } }
		 */
        // 普通发送 区别消息发送与接收
        if (entity.isSendFlag()) {
            if (entity.getType() == MessageEntity.TYPE_TEXT) {
                return 1;
            } else if (entity.getType() == MessageEntity.TYPE_VOICE) {
                return 2;
            } else if (entity.getType() == MessageEntity.TYPE_PICTURE) {
                return 3;
            } else if (entity.getType() == MessageEntity.TYPE_LOCATION) {
                return 4;
            } else if (entity.getType() == MessageEntity.TYPE_VIDEO) {
                return 7;
            }
        } else {
            if (entity.getType() == MessageEntity.TYPE_TEXT) {
                return -1;
            } else if (entity.getType() == MessageEntity.TYPE_VOICE) {
                return -2;
            } else if (entity.getType() == MessageEntity.TYPE_PICTURE) {
                return -3;
            } else if (entity.getType() == MessageEntity.TYPE_LOCATION) {
                return -4;
            } else if (entity.getType() == MessageEntity.TYPE_INVITATION) {
                return -5;
            } else if (entity.getType() == MessageEntity.TYPE_VIDEO) {
                return -7;
            }
        }
        return 1;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    public void addNew(MessageEntity messageEntity) {
        final List<MessageEntity> entities = new ArrayList<MessageEntity>();
        entities.add(messageEntity);
        addCollectionToEnd(entities);
    }

    public void addCollectionToEnd(List<MessageEntity> list) {
        if (list.size() != 0) {
            mList.addAll(list);
            notifyDataSetChanged();
        }
    }

    public void addCollectionToTopOutP(List<MessageEntity> list) {
        if (list.size() != 0) {
            mList.clear();
            mList.addAll(0, list);
            notifyDataSetChanged();
        }
    }

    public void addCollectionToTop(List<MessageEntity> list) {
        mList.addAll(0, list);
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final ViewHolder viewHolder;
        final MessageEntity entity = getItem(position);
        final int msgType = entity.getType();

        if (convertView == null) {
            final int type = getItemViewType(position);
            viewHolder = new ViewHolder();
            convertView = onCreateView(type, viewHolder);
            convertView.setTag(viewHolder);
            viewHolder.entity = entity;// 放到缓存中
            viewHolder.entity.viewHolder = new WeakReference<ViewHolder>(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.entity.viewHolder = null;
            viewHolder.entity = entity;
            viewHolder.entity.viewHolder = new WeakReference<ViewHolder>(viewHolder);
        }

        if (msgType == MessageEntity.TYPE_TEXT) {// 文字
            doBindTxt(viewHolder, entity);
        } else if (msgType == MessageEntity.TYPE_VOICE) {
            doBindVoice(viewHolder, entity);// 语音
            doMeasureVoiceLenght(entity, viewHolder, parent);
        } else if (msgType == MessageEntity.TYPE_LOCATION) {// 地图
            doBindLocation(viewHolder, entity);
        } else if (msgType == MessageEntity.TYPE_PICTURE) {// 图片
            doBindImage(viewHolder, entity);
        } else if (msgType == MessageEntity.TYPE_VIDEO) {// 视频
            doBindVideo(viewHolder, entity);
        } else if (msgType == MessageEntity.TYPE_TIME) {//没有付费需要跳转
            doBindtTost(viewHolder, entity.getContent());
        }

        if (msgType != MessageEntity.TYPE_TIME) {
            long lastTime = 0;
            if (position > 0) {
                final MessageEntity entity2 = getItem(--position);
                lastTime = entity2.getDate();
            }
            // 设置时间
            setChatTime(viewHolder.timeTextV, lastTime, entity.getDate());
        }

        // 记录选中
        if (viewHolder.chooseBox != null) {
            if (isEditor) {// 编辑状态下
                if (viewHolder.chooseBox.getVisibility() != View.VISIBLE)
                    viewHolder.chooseBox.setVisibility(View.VISIBLE);
                viewHolder.chooseBox.setChecked(mChoosedMap
                        .containsValue(entity));
                viewHolder.chooseBox
                        .setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (mChoosedMap.containsValue(entity)) {
                                    mChoosedMap.remove(entity);
                                } else {
                                    mChoosedMap.put(entity, entity);
                                }
                                onChooseSizeChange();
                            }
                        });
            } else {
                if (viewHolder.chooseBox.getVisibility() != View.GONE)
                    viewHolder.chooseBox.setVisibility(View.GONE);
            }
        }
        // 头像点击事件
        if (msgType != MessageEntity.TYPE_TIME
                || viewHolder.headerImageV != null) {
            doBindHeaderImage(viewHolder, entity);
        }
        return convertView;
    }

    /**
     * 没有付费提示
     *
     * @param holder
     * @param time
     */
    private void doBindtTost(ViewHolder holder, String time) {
        holder.timeTextV.setClickable(true);
        holder.timeTextV.setText(time);
        if (HStringUtil.isEmpty(time)) holder.timeTextV.setVisibility(View.GONE);
        holder.timeTextV.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.toPay();
            }
        });
    }

    /**
     * 绑定头像
     *
     * @param
     * @param
     */
    private void doBindHeaderImage(ViewHolder viewHolder,
                                   MessageEntity messageEntity) {
        ImageView imge = viewHolder.headerImageV;
        final String userid = messageEntity.getSenderId();
        boolean isContain = HTalkApplication.getAppData().cacheInformation.containsKey(userid);
        String headerPath = null;
        CustomerInfoEntity baseInfoEntity = null;
        if (isContain) {// 包含资料
            baseInfoEntity = (CustomerInfoEntity) HTalkApplication.getAppData().cacheInformation.get(userid);
            headerPath = baseInfoEntity.getNormalHeadIcon();
        } else {// 不包含资料请求
            baseInfoEntity = new CustomerInfoEntity();
            baseInfoEntity.setId(userid);
            HTalkApplication.getAppData().cacheInformation.put(userid, baseInfoEntity);
            doQueryCustomerInfo(baseInfoEntity, messageEntity);
        }
        mImageLoader.displayImage(baseInfoEntity.getSex(), headerPath, imge);
        if (!isEditor)
            imge.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    String loginUserId = SmartFoxClient.getLoginUserId();
                    Intent intent = null;
                    // 系统通知
//                    if (SmartFoxClient.helperId.equals(userid) || AppData.DYHSID.equals(userid)) {
////						intent = new Intent(mContext,SystemUserInforActivity.class);
////						intent.putExtra("id", userid);
//                        return;
//                    } else if (loginUserId.equals(userid)) {
//                        intent = new Intent(mContext, AtyPatientMassage.class);
//                    } else {
//                        CustomerInfoEntity infoEntity = (CustomerInfoEntity) mInformationMap.get(userid);
//                        if (infoEntity.isDoctor()) {
//                            intent = new Intent(mContext, MyInfoActivity.class);
//                            intent.putExtra("docId", userid);
//                            intent.putExtra("docName", infoEntity.getRealname());
//                        } else {
//                            intent = new Intent(mContext, AtyPatientMassage.class);
//                            intent.putExtra("PID", userid);
//                            intent.putExtra("MAIN", "main");
//                        }
//                    }
//                    mContext.startActivity(intent);
                    if (loginUserId.equals(userid)) {
                        intent = new Intent(mContext, AtyPatientMassage.class);
                    } else {
                        intent = new Intent(mContext, DoctorStudioActivity.class);
                        intent.putExtra("DOCTOR_ID", userid);
                    }
                    mContext.startActivity(intent);
                }
            });
    }

    /**
     * 绑定语音
     */
    private void doBindVoice(ViewHolder holder, final MessageEntity entity) {
        setSendState(entity.getSendState(), holder.stateCheckbV);
        holder.playPbV.setProgress(entity.playProgres);
        holder.voiceLenthTextV.setText(entity.getVoiceLength());
        holder.playPbV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ChatActivity) mContext).onVoicePlay(entity);
            }
        });
    }

    /**
     * 绑定文字
     *
     * @param holder
     * @param entity
     */
    private void doBindTxt(ViewHolder holder, MessageEntity entity) {
        setSendState(entity.getSendState(), holder.stateCheckbV);
        if (entity.contCharSequence == null) {
            final JSONArray array = entity.getContentJsonArray();
            if (array == null) {// 非连接
                entity.contCharSequence = mFaceParse.parseSmileTxt(entity.getContent());
            } else {
                entity.contCharSequence = onParseLinkTxt(array, holder, entity);
            }
        }
        if ("1".equals(entity.getIsWeChat())) {
            Spanned spanned = Html.fromHtml(entity.contCharSequence.toString());
            holder.contentTextV.setText(spanned);
        } else {
            holder.contentTextV.setText(entity.contCharSequence);
        }
    }

    /**
     * 绑定图片
     *
     * @param holder
     * @param entity
     */
    private void doBindImage(ViewHolder holder, final MessageEntity entity) {
        setSendState(entity.getSendState(), holder.stateCheckbV);
        String content = entity.getContent();
        final String[] urls = content.split("&");
        if (urls == null) {
            mImageLoader.displayImage(entity.getContent(),
                    holder.contentImageV, mGalleryDisplayOptions);
        } else {
            mImageLoader.displayImage(urls[0], holder.contentImageV,
                    mGalleryDisplayOptions);
        }
        holder.contentImageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = entity.getContent();
                if (content == null)
                    return;
                ChatActivity activity = (ChatActivity) mContext;
                String[] urls = content.split("&");
                final String path = urls.length >= 2 ? urls[1] : urls[0];
                activity.onShowBigImage(path);
            }
        });
    }


    /**
     * 绑定视频
     *
     * @param holder
     * @param entity
     */
    private void doBindVideo(ViewHolder holder, final MessageEntity entity) {
        setSendState(entity.getSendState(), holder.stateCheckbV);
        String content = entity.getContent();
        final String[] urls = content.split("&");
        if (urls.length < 2) {
            holder.contentImageV.setImageBitmap(PlayVideoActiviy.getVideoThumbnail(entity.getContent()));
        } else {
            mImageLoader.displayImage(urls[0],
                    holder.contentImageV, mGalleryDisplayOptions);
        }
        holder.contentImageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = entity.getContent();
                final String[] urls2 = content.split("&");
                ChatActivity activity = (ChatActivity) mContext;
                if (content == null)
                    return;
                if (urls2.length < 2) {
                    activity.onShowVideo(content);
                } else {
                    activity.onShowVideo(HTalkApplication.getHttpUrls().URL_DOWNLOAVIDEO + urls2[1]);
                }
            }
        });
    }

    public interface onLocationMesgClick{
        void onClick(MessageEntity entity);
    }
    /**
     * 绑定地图
     *
     * @param holder
     * @param entity
     */
    private void doBindLocation(ViewHolder holder, final MessageEntity entity) {
        setSendState(entity.getSendState(), holder.stateCheckbV);
        String content = entity.getContent();
        if (!content.startsWith("http")) {
            String[] str = content.split("&");
            if (str.length > 1) {
                content = HttpRestClient.getGoogleMapUrl(str[0], str[1]);
            }
//			content = "http://api.map.baidu.com/staticimage?center=116.403874,39.914888&width=300&height=200&zoom=19";
            // entity.setContent(content);
        }
        holder.contentTextV.setText(entity.getAddress());

        holder.contentImageV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((ChatActivity) mContext).onLocationMesgClick(entity);
            }
        });
        mImageLoader.displayImage(content,
                holder.contentImageV, mMapDisplayOptions);
        //mImageLoader.displayImage(content, holder.contentImageV,mMapDisplayOptions);
    }

    /**
     * 解析需要跳转的内容链接
     *
     * @param
     */
    private CharSequence onParseLinkTxt(JSONArray jsonArray, ViewHolder holder,
                                        MessageEntity entity) {
        String object_type = "";
        if (jsonArray == null)
            return "";
        SpannableStringBuilder builder = new SpannableStringBuilder();
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                int type = jsonObject.getInt("type");
                if (type == 1) {// 文字
                    final String content = jsonObject.getString("cont");
                    builder.append(content);
                } else if (type == 2) {// 表情
                    final String content = jsonObject.getString("cont");
                    builder.append(mFaceParse.parseSmileTxt(content));
                } else if (type == 3) {// 文字跳转
                    int typeTmp = 3;// 跳转类型
                    final String content = jsonObject.optString("cont", "  ");
                    String customerId;// jsonObject.optString("customerId","");
                    if (jsonObject.has("customerId")) {
                        customerId = jsonObject.optString("customerId", "  ");
                    } else {
                        customerId = entity.getSenderId();
                    }
                    String linkType = jsonObject.optString("linkType", null);
                    String param = "";
                    if ("group".equals(linkType)) {//salon
                        typeTmp = 1;
                    } else if ("customer".equals(linkType)) {//person
                        typeTmp = 2;
                    } else if ("sarchDoctor".equals(linkType)) {//科室
                        typeTmp = 4;
                        customerId = jsonObject.optString("officeId");
                    } else if ("goSeeSee".equals(linkType)) {
                        typeTmp = 5;
//						customerId=jsonObject.optString("consultationId");//这里存放会诊状态
                        if (jsonObject.has("Object_ID")) {
                            customerId = jsonObject.optString("Object_ID");//这里存放会诊状态
                        } else if (jsonObject.has("object_id")) {
                            customerId = jsonObject.optString("object_id");//这里存放会诊状态
                        }
                        param = jsonObject.optString("consultationStatus");
                        if (jsonObject.has("Object_Type")) {
                            object_type = jsonObject.optString("Object_Type");
                        } else if (jsonObject.has("object_type")) {
                            object_type = jsonObject.optString("object_type");
                        }
                        param = jsonObject.optString("consultationStatus");
                    } else if ("goSee6".equals(linkType)) {//特殊服务体验i
                        typeTmp = 6;
                    } else if ("goSee7".equals(linkType)) {//特殊服务图文
                        typeTmp = 7;
                    } else if ("goSee8".equals(linkType)) {//特殊服务包月
                        typeTmp = 8;
                    } else if (linkType.startsWith("siteGoSee5")) {//医生集团图文
                        typeTmp = 9;
                    } else if (linkType.startsWith("siteGoSee6")) {//医生集团电话
                        typeTmp = 10;
                    } else if (linkType.startsWith("siteGoSee8")) {//医生集团视频
                        typeTmp = 11;
                    } else {// 药品跳转
                        typeTmp = 3;
                    }
                    createLinkType(builder, typeTmp, content, customerId, param, object_type, linkType);
                } else if (type == 4) {// 图片显示
                    String srcImage = jsonObject.optString("url", null);
                    if (srcImage == null)
                        continue;
                    String path = mImageLoader.getDownPathUri(srcImage);
                    path = MemoryCacheUtil.generateKey(path, mImageSize);
                    Bitmap bitmap = mImageLoader.getMemoryCache().get(path);
                    boolean isNeedDown = false;
                    Drawable drawable;
                    if (bitmap != null) {// 不要下載
                        drawable = new BitmapDrawable(bitmap);
                        drawable.setBounds(0, 0, mImageSize.getWidth(),
                                mImageSize.getHeight());
                    } else {// 下載
                        isNeedDown = true;
                        //assets/customerIcons/s_zcmale_24.png
                        if ("头像不知名_24.png".equals(srcImage)) {
                            drawable = mUserDrawable;
                        } else if ("groupdefault_24.png".equals(srcImage)) {
                            drawable = mGroupDrawable;
                        } else if ("//assets/customerIcons/s_zcmale_24.png".equals(srcImage)) {
                            drawable = manDrawable;
                        } else {
                            drawable = mUserDrawable;
                        }
                    }
                    ImageSpan imageSpan = new ImageSpan(drawable);
                    SpannableString spannableString = new SpannableString(
                            srcImage);
                    spannableString.setSpan(imageSpan, 0,
                            spannableString.length(),
                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                    builder.append(spannableString);
                    // 是否需要下载
                    if (isNeedDown) {
                        int startIndex = builder.getSpanStart(imageSpan);
                        int endIndex = builder.getSpanEnd(imageSpan);
                        mImageLoader.loadImage(mContext, srcImage,
                                new SpanImageLoadListener(builder, mImageSize,
                                        holder.contentTextV, srcImage,
                                        new int[]{startIndex, endIndex}));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return builder;
    }

    private void createLinkType(SpannableStringBuilder builder, int typeTmp,
                                final String content, String customerId, String param, String object_type, String linkType) {
        ClickMessageSpan clickSpan = new ClickMessageSpan(mContext,
                typeTmp, customerId, content, param, object_type, linkType) {
            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(Color.argb(255, 19, 109, 215));       //设置文件颜色
                ds.setUnderlineText(false);      //设置下划线
            }
        };
//		UnderlineSpan underlineSpan = new UnderlineSpan();
//		spannableStr.setSpan(underlineSpan, 0, length,
//				Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        SpannableString spannableStr = new SpannableString(content);
        final int length = spannableStr.length();
        spannableStr.setSpan(clickSpan, 0, length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        builder.append(spannableStr);
    }

    /**
     * 计算语音要显示的长度
     */
    private void doMeasureVoiceLenght(MessageEntity messageEntity,
                                      ViewHolder viewHolder, ViewGroup parent) {
        ProgressBar bar = viewHolder.playPbV;
        FrameLayout frameLayout = (FrameLayout) bar.getParent();
        LayoutParams layoutParams = (LayoutParams) frameLayout
                .getLayoutParams();
        if (messageEntity.voiceLayoutWidth > 0) {// 已经计算过
            layoutParams.width = messageEntity.voiceLayoutWidth;
        } else {// 第一次需要计算
            String length = messageEntity.getVoiceLength();
            float voiceLength = NumberUtils.toFloat(length);
            if (voiceLength > 4) {// 计算宽度
                int widthP = parent.getWidth();
                float voiceWeight = Math.min(0.8f, voiceLength / 60f);// 计算比重
                widthP = (int) (voiceWeight * (widthP - layoutParams.height * 5));
                widthP += layoutParams.height;
                messageEntity.voiceLayoutWidth = Math.max(widthP,
                        layoutParams.height);
                layoutParams.width = messageEntity.voiceLayoutWidth;
            } else {// 默认宽度
                messageEntity.voiceLayoutWidth = layoutParams.height;
            }
        }
    }

    //发送状态
    private void setSendState(int state, CheckBox checkBox) {
        if (checkBox == null)
            return;
        if (state == MessageEntity.STATE_FAIL) {
            checkBox.setChecked(false);
            checkBox.setText("发送失败");
            checkBox.setVisibility(View.VISIBLE);
        } else if (state == MessageEntity.STATE_OK) {
            checkBox.setText("发送成功");
            checkBox.setChecked(true);
            checkBox.setVisibility(View.GONE);
        } else if (state == MessageEntity.STATE_PROCESING) {
            checkBox.setText("发送中");
            checkBox.setChecked(false);
            checkBox.setVisibility(View.GONE);
        }
    }

    /**
     * 获得选中的数据
     *
     * @return
     */
    public List<MessageEntity> onDeletSelectedMesg() {
        List<MessageEntity> list = new ArrayList<MessageEntity>(
                mChoosedMap.values());
        notifyDataSetChanged();
        return list;
    }

    /**
     * 根据消息类型的不同创建不同的view
     *
     * @param type
     * @return
     */
    private View onCreateView(int type, ViewHolder holder) {
        View view = null;
        switch (type) {
            case 0:
                view = mInflater.inflate(R.layout.chat_time_layout, null);
                holder.timeTextV = (TextView) view.findViewById(R.id.chat_time);
                break;
            case 1:// 文字
                view = mInflater.inflate(R.layout.chat_right_txt_blue_item, null);
                holder.contentTextV = (TextView) view.findViewById(R.id.chat_content);
                holder.headerImageV = (ImageView) view.findViewById(R.id.chat_head);
                holder.stateCheckbV = (CheckBox) view.findViewById(R.id.msg_state);
                holder.timeTextV = (TextView) view.findViewById(R.id.chat_time);
                holder.contentTextV.setMovementMethod(LinkMovementMethod
                        .getInstance());
                mContext.registerForContextMenu(holder.contentTextV);
                break;
            case -1:
                view = mInflater.inflate(R.layout.chat_left_txt_white_item, null);
                holder.contentTextV = (TextView) view.findViewById(R.id.chat_content);
                holder.headerImageV = (ImageView) view.findViewById(R.id.chat_head);
                holder.timeTextV = (TextView) view.findViewById(R.id.chat_time);
                holder.contentTextV.setMovementMethod(LinkMovementMethod
                        .getInstance());
                mContext.registerForContextMenu(holder.contentTextV);
                break;
            case 5:
                view = mInflater.inflate(R.layout.chat_right_txt_blue_item, null);
                holder.contentTextV = (TextView) view.findViewById(R.id.chat_content);
                holder.headerImageV = (ImageView) view.findViewById(R.id.chat_head);
                holder.stateCheckbV = (CheckBox) view.findViewById(R.id.msg_state);
                holder.timeTextV = (TextView) view.findViewById(R.id.chat_time);
                holder.contentTextV.setMovementMethod(LinkMovementMethod
                        .getInstance());
                break;
            case -5:// 邀请
                view = mInflater.inflate(R.layout.chat_left_txt_white_item, null);
                holder.contentTextV = (TextView) view.findViewById(R.id.chat_content);
                holder.headerImageV = (ImageView) view.findViewById(R.id.chat_head);
                holder.timeTextV = (TextView) view.findViewById(R.id.chat_time);
                holder.contentTextV.setMovementMethod(LinkMovementMethod
                        .getInstance());
                break;
            case 2:// 普通语音
                view = mInflater.inflate(R.layout.chat_right_voice_item, null);
                holder.voiceLenthTextV = (TextView) view.findViewById(R.id.chat_voice_length);
                holder.playPbV = (ProgressBar) view.findViewById(R.id.voice_playing_progres);
                holder.stateCheckbV = (CheckBox) view.findViewById(R.id.msg_state);
                holder.headerImageV = (ImageView) view.findViewById(R.id.chat_head);
                holder.timeTextV = (TextView) view.findViewById(R.id.chat_time);
                break;
            case -2:
                view = mInflater.inflate(R.layout.chat_left_voice_item, null);
                holder.voiceLenthTextV = (TextView) view.findViewById(R.id.chat_voice_length);
                holder.playPbV = (ProgressBar) view.findViewById(R.id.voice_playing_progres);
                holder.stateCheckbV = (CheckBox) view.findViewById(R.id.msg_state);
                holder.headerImageV = (ImageView) view.findViewById(R.id.chat_head);
                holder.timeTextV = (TextView) view.findViewById(R.id.chat_time);
                break;
            case 6:// 群主语音
                view = mInflater.inflate(R.layout.chat_right_voice_item, null);
                holder.voiceLenthTextV = (TextView) view.findViewById(R.id.chat_voice_length);
                holder.playPbV = (ProgressBar) view.findViewById(R.id.voice_playing_progres);
                holder.stateCheckbV = (CheckBox) view.findViewById(R.id.msg_state);
                holder.headerImageV = (ImageView) view.findViewById(R.id.chat_head);
                holder.timeTextV = (TextView) view.findViewById(R.id.chat_time);
                break;
            case -6:
                view = mInflater.inflate(R.layout.chat_left_voice_item, null);
                holder.stateCheckbV = (CheckBox) view.findViewById(R.id.msg_state);
                holder.voiceLenthTextV = (TextView) view.findViewById(R.id.chat_voice_length);
                holder.playPbV = (ProgressBar) view.findViewById(R.id.voice_playing_progres);
                holder.headerImageV = (ImageView) view.findViewById(R.id.chat_head);
                holder.timeTextV = (TextView) view.findViewById(R.id.chat_time);
                break;
            case -4:// 地图
                view = mInflater.inflate(R.layout.chat_left_location_item, null);
                holder.headerImageV = (ImageView) view.findViewById(R.id.chat_head);
                holder.contentImageV = (ImageView) view.findViewById(R.id.chat_content);
                holder.contentTextV = (TextView) view.findViewById(R.id.chat_location_txt);
                holder.timeTextV = (TextView) view.findViewById(R.id.chat_time);
                break;
            case 4:
                view = mInflater.inflate(R.layout.chat_right_location_item, null);
                holder.stateCheckbV = (CheckBox) view.findViewById(R.id.msg_state);
                holder.headerImageV = (ImageView) view.findViewById(R.id.chat_head);
                holder.contentImageV = (ImageView) view.findViewById(R.id.chat_content);
                holder.contentTextV = (TextView) view.findViewById(R.id.chat_location_txt);
                holder.timeTextV = (TextView) view.findViewById(R.id.chat_time);
                break;
            case 3:// 图片
                view = mInflater.inflate(R.layout.chat_right_image_item, null);
                holder.stateCheckbV = (CheckBox) view.findViewById(R.id.msg_state);
                holder.headerImageV = (ImageView) view.findViewById(R.id.chat_head);
                holder.contentImageV = (ImageView) view.findViewById(R.id.chat_content);
                holder.timeTextV = (TextView) view.findViewById(R.id.chat_time);
                break;
            case -3:
                view = mInflater.inflate(R.layout.chat_left_image_item, null);
                holder.headerImageV = (ImageView) view.findViewById(R.id.chat_head);
                holder.contentImageV = (ImageView) view
                        .findViewById(R.id.chat_content);
                holder.timeTextV = (TextView) view.findViewById(R.id.chat_time);
                break;
            case 7:// 右视频
                view = mInflater.inflate(R.layout.chat_right_video_item, null);
                holder.stateCheckbV = (CheckBox) view.findViewById(R.id.msg_state);
                holder.headerImageV = (ImageView) view.findViewById(R.id.chat_head);
                holder.contentImageV = (ImageView) view.findViewById(R.id.chat_content);
                holder.timeTextV = (TextView) view.findViewById(R.id.chat_time);
                break;
            case -7://左视频
                view = mInflater.inflate(R.layout.chat_left_video_item, null);
                holder.headerImageV = (ImageView) view.findViewById(R.id.chat_head);
                holder.contentImageV = (ImageView) view.findViewById(R.id.chat_content);
                holder.timeTextV = (TextView) view.findViewById(R.id.chat_time);
                break;
        }
        if (type != 0) {
            holder.chooseBox = (CheckBox) view.findViewById(R.id.selected);
        }

        if (holder.stateCheckbV != null) {
            holder.stateCheckbV.setVisibility(View.GONE);
        }
        return view;
    }

    public static class ViewHolder {
        public ImageView headerImageV;
        public ImageView contentImageV;
        public TextView timeTextV;
        public CheckBox stateCheckbV;
        public TextView contentTextV;
        public ProgressBar playPbV;// 播放进度
        public MessageEntity entity;
        public CustomerInfoEntity infoEntity;
        public TextView voiceLenthTextV;
        public CheckBox chooseBox;
    }

    static class ClickMessageSpan extends ClickableSpan {
        // 类型1群2用户 3药品跳转
        int type;
        String id;
        String name;
        String param;
        String object_type;
        String linkType;
        WeakReference<FragmentActivity> mReference;
        Activity mActivity;

        public ClickMessageSpan(FragmentActivity activity, int type, String id,
                                String name, String param, String object_type, String linkType) {
            this.type = type;
            this.id = id;
            this.name = name;
            this.param = param;
            this.object_type = object_type;
            this.linkType = linkType;
            mReference = new WeakReference<FragmentActivity>(activity);
        }

        @Override
        public void onClick(View widget) {
            FragmentActivity activity = mReference.get();
            if (activity == null)
                return;
            Intent intent;
            switch (type) {
                case 2:// 用户资料跳转
//					String loginUserId = SmartFoxClient.getLoginUserId();
//					if (loginUserId.equals(id)) {
//						intent = new Intent(activity,
//								PersonInfoActivity.class);
//						activity.startActivity(intent);
//					} else {
//						ActivityUtils.startUserInfoActivity(activity,
//								activity.getSupportFragmentManager(), id);
//					}
                    break;
                case 5://会诊跳转
                    if ("10".equals(object_type)) {
                        if ("10".equals(param) || "15".equals(param)) {
                            intent = new Intent(activity, AtyOrdersDetails.class);
                        } else {
                            intent = new Intent(activity, AtyOrderDetails.class);
                        }
                        intent.putExtra("CONID", Integer.parseInt(id));
                        activity.startActivity(intent);
                    } else if ("20".equals(object_type)) {
                        intent = new Intent(activity, AtyOutPatientDetail.class);
                        intent.putExtra("ORIDERID", id);
                        activity.startActivity(intent);
                    }
                    break;
                case 6://体验服务
                    intent = new Intent(activity, ConsultActivity.class);
                    intent.putExtra(ConsultActivity.TITLE, "体验咨询");
                    intent.putExtra(ConsultActivity.SERVICE_TYPE_ID, "9");
                    activity.startActivity(intent);
                    break;

                case 7://图文咨询
                case 9://集团图文
                    intent = new Intent(activity, ConsultActivity.class);
                    intent.putExtra(ConsultActivity.TITLE, "图文咨询");
                    intent.putExtra(ConsultActivity.SERVICE_TYPE_ID, "5");
                    activity.startActivity(intent);
                    break;
                case 8://包月咨询
                    intent = new Intent(activity, ConsultActivity.class);
                    intent.putExtra(ConsultActivity.TITLE, "包月咨询");
                    intent.putExtra(ConsultActivity.SERVICE_TYPE_ID, "7");
                    activity.startActivity(intent);
                    break;
                case 10://集团电话
                    intent = new Intent(activity, ConsultActivity.class);
                    intent.putExtra(ConsultActivity.TITLE, "电话咨询");
                    intent.putExtra(ConsultActivity.SERVICE_TYPE_ID, "6");
                    activity.startActivity(intent);
                    break;
                case 11://集团视频
                    intent = new Intent(activity, ConsultActivity.class);
                    intent.putExtra(ConsultActivity.TITLE, "视频咨询");
                    intent.putExtra(ConsultActivity.SERVICE_TYPE_ID, "8");
                    activity.startActivity(intent);
                    break;

            }
        }
    }

    /**
     * imagespan 图片下載
     *
     * @author zhao
     */
    public static class SpanImageLoadListener implements ImageLoadingListener {
        WeakReference<TextView> mReference;
        final SpannableStringBuilder mBuilder;
        String mPath;
        int[] mIndexs;// imagespan 的索引位置
        final ImageSize mImageSize;

        public SpanImageLoadListener(SpannableStringBuilder builder,
                                     ImageSize imageSize, TextView textView, String path,
                                     int[] indexs) {
            mReference = new WeakReference<TextView>(textView);
            mBuilder = builder;
            mPath = path;
            mIndexs = indexs;
            mImageSize = imageSize;
        }

        @Override
        public void onLoadingStarted() {

        }

        @Override
        public void onLoadingFailed(FailReason failReason) {

        }

        @Override
        public void onLoadingComplete(Bitmap loadedImage) {// 更新到ui
            if (loadedImage == null)
                return;
            TextView textView = mReference.get();
            if (textView != null && (textView.getTag(R.id.list) == mBuilder)) {
                BitmapDrawable bitmapDrawable = new BitmapDrawable(loadedImage);
                bitmapDrawable.setCallback(null);
                bitmapDrawable.setBounds(0, 0, mImageSize.getWidth(),
                        mImageSize.getHeight());
                ImageSpan imageSpan = new ImageSpan(bitmapDrawable);
                SpannableString spannableString = new SpannableString(mPath);
                spannableString.setSpan(imageSpan, 0, spannableString.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                mBuilder.replace(mIndexs[0], mIndexs[1], spannableString);
                textView.setText(mBuilder);
            }
        }

        @Override
        public void onLoadingCancelled() {

        }
    }

    /**
     * 消息发送失败 保存消息状态 更新界面
     *
     * @param mesgId
     */
    public void onChangMesge(String mesgId) {
        for (MessageEntity msg : mList) {
            if (msg.getId().equals(mesgId)) {
                msg.setSendState(MessageEntity.STATE_FAIL);
                notifyDataSetChanged();
                break;
            }
        }

    }


    private void upLoadHeadView(CustomerInfoEntity baseInfoEntity, MessageEntity entity) {
        final String userid = entity.getSenderId();
        baseInfoEntity = new CustomerInfoEntity();
        baseInfoEntity.setId(userid);
        mInformationMap.put(userid, baseInfoEntity);
        doQueryCustomerInfo(baseInfoEntity, entity);
    }

    public void removeAll() {
        this.mList.clear();
        notifyDataSetChanged();
    }

    public void removeLast() {
        this.mList.remove(mList.size() - 1);
        notifyDataSetChanged();
    }

    public void addAll(List<MessageEntity> datas) {
        this.mList.addAll(datas);
        notifyDataSetChanged();
    }
}
