package com.yksj.consultation.adapter;

import android.app.Activity;
import android.text.Html;
import android.text.Spanned;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.socks.zlistview.enums.DragEdge;
import com.socks.zlistview.enums.ShowMode;
import com.socks.zlistview.widget.ZSwipeItem;
import com.squareup.okhttp.Request;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.entity.CustomerInfoEntity;
import com.yksj.healthtalk.entity.MsgLaterEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.DataParseUtil;
import com.yksj.healthtalk.utils.EntityObjUtils;
import com.yksj.healthtalk.utils.ViewFinder;

import org.json.JSONObject;
import org.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息列表
 *
 * @author jack_tang
 */
public class MessageHistoryListAdapter extends BaseSwipeAdapter {

    public List<MsgLaterEntity> mListData = new ArrayList<MsgLaterEntity>();
    Activity context;
    private ImageLoader mInstance;

    onClickdeleteMsgeListener clickdeleteMsgeListener;

    public interface onClickdeleteMsgeListener {
        void onClickDeleteMsg(int positon);
    }

    public void setonClickdeleteMsgeListener(onClickdeleteMsgeListener attentionListener) {
        this.clickdeleteMsgeListener = attentionListener;
    }

    ;


    public MessageHistoryListAdapter(Activity activity) {
        context = activity;
        mInstance = ImageLoader.getInstance();
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mListData.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getSwipeLayoutResourceId(int position) {
        // TODO Auto-generated method stub
        return R.id.swipe_item;
    }

    @Override
    public View generateView(int position, ViewGroup parent) {
        return context.getLayoutInflater().inflate(R.layout.message_history_list_item_layout,
                parent, false);
    }

    @Override
    public void fillValues(final int position, View convertView) {
        final ZSwipeItem swipeItem = (ZSwipeItem) convertView.findViewById(R.id.swipe_item);
        LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll);
        swipeItem.setShowMode(ShowMode.PullOut);
        swipeItem.setDragEdge(DragEdge.Right);
        ll.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (clickdeleteMsgeListener != null) {
                    clickdeleteMsgeListener.onClickDeleteMsg(position);
                }
                swipeItem.close();
            }
        });

        final MsgLaterEntity item = mListData.get(position);
        ViewFinder viewFinder = new ViewFinder(convertView);
        ImageView mHead = viewFinder.imageView(R.id.chat_head);

        if (SmartFoxClient.helperId.equals(item.getSendId())) {//多美助理
            mHead.setImageResource(R.drawable.launcher_logo);
            viewFinder.setText(R.id.name, "系统通知");
        } else if ("1".equals(item.getIsGroup())) {//群
            mInstance.displayImageFromId("4", item.getSendId(), mHead);
            viewFinder.setText(R.id.name, item.getNickName());
        } else {

            final String userid = item.getSendId();
            boolean isContain = HTalkApplication.getAppData().cacheInformation.containsKey(userid);
            String headerPath = null;
            CustomerInfoEntity baseInfoEntity = null;
            if (isContain) {// 包含资料
                baseInfoEntity = (CustomerInfoEntity) HTalkApplication.getAppData().cacheInformation.get(userid);
//            baseInfoEntity = (CustomerInfoEntity) mInformationMap.get(userid);
                headerPath = baseInfoEntity.getNormalHeadIcon();
            } else {// 不包含资料请求
                baseInfoEntity = new CustomerInfoEntity();
                baseInfoEntity.setId(userid);
//            mInformationMap.put(userid, baseInfoEntity);
                HTalkApplication.getAppData().cacheInformation.put(userid, baseInfoEntity);
                doQueryCustomerInfo(baseInfoEntity);
            }

            mInstance.displayImage(baseInfoEntity.getSex(), headerPath, mHead);
//            String picUrl = HTalkApplication.getHttpUrls().URL_QUERYHEADIMAGE + item.getHeaderPath();
//            Picasso.with(context).load(picUrl).error(R.drawable.default_head_mankind).placeholder(R.drawable.default_head_mankind).into(mHead);
            viewFinder.setText(R.id.name, item.getNickName());
        }


        viewFinder.setText(R.id.time, item.getMsgTime());
        if ("0".equals(item.getMsgCount())) {
            viewFinder.find(R.id.messagecount).setVisibility(View.GONE);
        } else {
            viewFinder.setText(R.id.messagecount, item.getMsgCount());
            viewFinder.find(R.id.messagecount).setVisibility(View.VISIBLE);
        }
        Spanned spanned= Html.fromHtml(item.getContent());
        viewFinder.setText(R.id.message,spanned);
        //MESSAGE_TYPE    消息类型(0-纯文本，1-语音，2-图片，3-虚拟医生链接功能，4-聊天表情文本
    }


    /**
     * 查询用户资料
     */
    private void doQueryCustomerInfo(final CustomerInfoEntity infoEntity) {
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


    }

    public void onBoundData(List<MsgLaterEntity> lists) {
        if (lists == null) return;
        mListData.clear();
        notifyDataSetChanged();
        mListData.addAll(lists);
        notifyDataSetChanged();

    }

    public void remove(int positon) {
        mListData.remove(positon);
        notifyDataSetChanged();
    }

}
