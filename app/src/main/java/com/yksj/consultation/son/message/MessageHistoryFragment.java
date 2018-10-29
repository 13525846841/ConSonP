package com.yksj.consultation.son.message;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.socks.zlistview.widget.ZListView;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.MessageHistoryListAdapter;
import com.yksj.consultation.adapter.MessageHistoryListAdapter.onClickdeleteMsgeListener;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.chatting.ChatActivity;
import com.yksj.consultation.son.salon.SalonSelectPaymentOptionActivity.OnBuyTicketHandlerListener;
import com.yksj.healthtalk.bean.BaseBean;
import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.healthtalk.entity.MsgLaterEntity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.services.CoreService;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息列表
 *
 * @author jack_tang
 */
public class MessageHistoryFragment extends RootFragment implements OnItemClickListener,
        OnBuyTicketHandlerListener, onClickdeleteMsgeListener {

    private MessageHistoryListAdapter mAdapter;
    private ZListView mRefreshListView;
    public static final String ACTION_NOTITYFY_MSG = "ACTION_NOTITYFY_MSG";

    //更新消息通知
    final BroadcastReceiver mReceiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            initData();
        }

        ;
    };
    private View view;

    private synchronized void onSendLoadMsgLine() {
        //通知加载离线消息
        Intent intent = new Intent(mActivity, CoreService.class);
        intent.setAction(CoreService.ACTION_LOAD_OFFLINE_MSG);
        mActivity.startService(intent);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fgt_message_history_layout, null);
        initView();
        return view;
    }

    private void initView() {
        mRefreshListView = (ZListView) view.findViewById(R.id.message_his_listView);
        mRefreshListView.setAdapter(mAdapter = new MessageHistoryListAdapter(getActivity()));
        mAdapter.setonClickdeleteMsgeListener(this);
        mRefreshListView.setPullLoadEnable(false);
        mRefreshListView.setPullRefreshEnable(false);
        mRefreshListView.setOnItemClickListener(this);

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    public void onStart() {
        super.onStart();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_NOTITYFY_MSG);
        filter.addAction(CoreService.ACTION_MESSAGE);
        getActivity().registerReceiver(mReceiver, filter);
    }

    @Override
    public void onStop() {
        super.onStop();
        getActivity().unregisterReceiver(mReceiver);
    }

    private void initData() {
        //http://220.194.46.204/DuoMeiHealth/ConsultationInfoSet?TYPE=findMessageList&CUSTOMERID=124031
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("CUSTOMERID", LoginServiceManeger.instance().getLoginEntity().getId()));
        pairs.add(new BasicNameValuePair("TYPE", "findMessageList"));
        HttpRestClient.doGetConsultationInfoSet(pairs, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {

            }

            @Override
            public void onResponse(String response) {
                BaseBean bb = com.alibaba.fastjson.JSONObject.parseObject(response, BaseBean.class);
                if ("1".equals(bb.code)) {
                    try {
                        JSONArray array = new JSONArray(bb.result);
                        List<MsgLaterEntity> lists = MsgLaterEntity.parseToList(array);
                        mAdapter.onBoundData(lists);
                        if (mAdapter.getCount() == 0) {
                            view.findViewById(R.id.load_faile_layout).setVisibility(View.VISIBLE);
                            mRefreshListView.setVisibility(View.GONE);
                        } else {
                            view.findViewById(R.id.load_faile_layout).setVisibility(View.GONE);
                            mRefreshListView.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        view.findViewById(R.id.load_faile_layout).setVisibility(View.VISIBLE);
                        mRefreshListView.setVisibility(View.GONE);
                    }
                } else
                    ToastUtil.showShort(getActivity(), bb.message);
            }
        }, this);

//				RequestParams params = new RequestParams();
//				params.put("TYPE", "findMessageList");
//				params.put("MYCUSTOMERID", SmartFoxClient.getLoginUserId());
//				HttpRestClient.doHttpFRIENDSINFOSET(params, new AsyncHttpResponseHandler(mActivity) {
//					@Override
//					public void onSuccess(String content) {
//						super.onSuccess(content);
//						try {
//							JSONObject object = new JSONObject(content);
//							if (object.has("error_message")) {
//								ToastUtil.showToastPanl(object.optString("error_message"));
//							} else {
//								JSONArray array = object.optJSONArray("findMessageList");
//								List<MsgLaterEntity> lists = MsgLaterEntity.parseToList(array);
//								mAdapter.onBoundData(lists);
//							}
//
//							if (mAdapter.getCount() == 0) {
//								view.findViewById(R.id.load_faile_layout).setVisibility(View.VISIBLE);
//								mRefreshListView.setVisibility(View.GONE);
//							} else {
//								view.findViewById(R.id.load_faile_layout).setVisibility(View.GONE);
//								mRefreshListView.setVisibility(View.VISIBLE);
//							}
//						} catch (Exception e) {
//							view.findViewById(R.id.load_faile_layout).setVisibility(View.VISIBLE);
//							mRefreshListView.setVisibility(View.GONE);
//						}
//					}
//				});

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
        if (arg2 == 0) return;
        MsgLaterEntity item = mAdapter.mListData.get(arg2 - 1);
        if ("1".equals(item.getIsGroup())) {//话题
//			GroupInfoEntity entity =new GroupInfoEntity();
//			entity.setId(item.getSendId());
//			entity.setName(item.getNickName());
//			entity.setIsBL(item.getIsBL());
//			entity.setObjectType(item.getObjectType());
//
//			Intent intent1 = new Intent();
//			intent1.putExtra("consultId", entity.getId());
//			intent1.putExtra("objectType", entity.getObjectType());
//			intent1.putExtra("conName",item.getNickName());
//			intent1.setClass(getActivity(), ChatActivity.class);
//			startActivity(intent1);
            if (HStringUtil.isEmpty(item.getGroupId())) {
                ToastUtil.showShort("该群聊异常");
                return;
            }

            Intent intent = new Intent();
            intent.putExtra(ChatActivity.CONSULTATION_ID, item.getConsultId());
            intent.putExtra(ChatActivity.GROUP_ID, item.getGroupId());
            intent.putExtra(ChatActivity.CONSULTATION_NAME, item.getNickName());
            intent.putExtra(ChatActivity.OBJECT_TYPE, item.getObjectType());
            intent.setClass(getActivity(), ChatActivity.class);
            startActivity(intent);


//			ChatUserHelper.getInstance().deleteFromIdmsgCount(entity.getId());
//			SalonHttpUtil.onItemClick(getActivity(),this,getChildFragmentManager(),entity,true);
        } else {//人
            Intent intent = new Intent();
            intent.putExtra(ChatActivity.CONSULTATION_ID, item.getConsultId());
            intent.putExtra(ChatActivity.SINGLE_ID, item.getSendId());
            intent.putExtra(ChatActivity.SINGLE_NAME, item.getNickName());
            intent.putExtra(ChatActivity.OBJECT_TYPE, item.getObjectType());
            intent.setClass(getActivity(), ChatActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onTicketHandler(String state, GroupInfoEntity entity) {
        if ("0".equals(state)) {
        } else if ("-1".equals(state)) {
            ToastUtil.showBasicErrorShortToast(getActivity());
        } else {
            Intent intent1 = new Intent();
            intent1.putExtra(ChatActivity.KEY_PARAME, entity);
            intent1.setClass(getActivity(), ChatActivity.class);
            startActivity(intent1);
        }
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                getResources().getDisplayMetrics());
    }

    @Override
    public void onClickDeleteMsg(final int positon) {
        final MsgLaterEntity entity = mAdapter.mListData.get(positon);
        entity.getMsgId();
        //DuoMeiHealth/ConsultationInfoSet?TYPE=deleteLastMessage&BELONGID=&OFFLINEID=
        RequestParams params = new RequestParams();
        params.put("TYPE", "deleteLastMessage");
        params.put("BELONGID", SmartFoxClient.getLoginUserId());
        params.put("OFFLINEID", entity.getMsgId());
        HttpRestClient.doHttpFRIENDSINFOSET(params, new AsyncHttpResponseHandler(mActivity) {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject object = new JSONObject(content);
                    if (object.has("error_message")) {
                        ToastUtil.showShort(object.optString("error_message"));
                    } else {
//						ChatUserHelper.getInstance().deleteFromMsgSingleMsg(entity.getSendId());
                        ToastUtil.showShort(object.optString("message"));
                        mAdapter.remove(positon);
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                }
            }
        });
    }
}

