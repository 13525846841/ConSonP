package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.ConsultAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.chatting.ChatActivity;
import com.yksj.consultation.son.consultation.PAtyConsultStudioGoPaying;
import com.yksj.consultation.son.consultation.avchat.team.util.LogUtil;
import com.yksj.consultation.son.consultation.bean.ObjectType;
import com.yksj.consultation.son.consultation.bean.ServiceType;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.FriendHttpUtil;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 咨询界面
 */
public class ConsultActivity extends BaseFragmentActivity implements View.OnClickListener, PullToRefreshBase.OnRefreshListener2<ListView>, AdapterView.OnItemClickListener {
    private View mEmptyView;
    private PullToRefreshListView mRefreshableView;
    public ListView mListView;
    private ConsultAdapter mAdapter;
    public static final String TITLE = "TITLE";
    public static final String SERVICE_TYPE_ID = "SERVICE_TYPE_ID";
    private String service_type_id = "";
    private List<JSONObject> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consult);
        initView();
    }

    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);


        if (getIntent().hasExtra(TITLE))
            titleTextV.setText(getIntent().getStringExtra(TITLE));
        service_type_id = getIntent().getStringExtra(SERVICE_TYPE_ID);

        mEmptyView = findViewById(R.id.load_faile_layout);
        mRefreshableView = (PullToRefreshListView) findViewById(R.id.pull_refresh_listview);
        mListView = mRefreshableView.getRefreshableView();
        mAdapter = new ConsultAdapter(mList, this);
        mListView.setAdapter(mAdapter);
        mRefreshableView.setOnRefreshListener(this);
        mListView.setOnItemClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private String customer_id = LoginServiceManeger.instance().getLoginUserId();

    private void initData() {
        Map<String, String> map = new HashMap<>();
        map.put("customer_id", customer_id);//
        map.put("service_type_id", service_type_id);
//        Log.i("kkk", " http://220.194.46.204/DuoMeiHealth/see.do?op=findOrderByCustomer&customer_id="+customer_id+"&service_type_id="+service_type_id);
        HttpRestClient.OKHttpFindOrderByCustomer(map, new HResultCallback<String>() {
            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                mRefreshableView.setRefreshing();
            }

            @Override
            public void onAfter() {
                super.onAfter();
                mRefreshableView.onRefreshComplete();
            }

            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.i("kkk", "onResponse: "+LoginServiceManeger.instance().getLoginUserId());
                if (!HStringUtil.isEmpty(response)) {
                    try {
                        JSONObject obj = new JSONObject(response);
                        if (HttpResult.SUCCESS.equals(obj.optString("code"))) {
                            mList = new ArrayList<>();
                            JSONArray array = obj.getJSONArray("orders");
                            JSONObject item;
                            for (int i = 0; i < array.length(); i++) {
                                item = array.getJSONObject(i);
                                mList.add(item);
                            }
                            mAdapter.onBoundData(mList, service_type_id);

                            if (mList.size() == 0) {
                                mEmptyView.setVisibility(View.VISIBLE);
                                mRefreshableView.setVisibility(View.GONE);
                            } else {
                                mEmptyView.setVisibility(View.GONE);
                                mRefreshableView.setVisibility(View.VISIBLE);
                            }
                        } else {
                            ToastUtil.showShort(obj.optString("message"));
                            mEmptyView.setVisibility(View.VISIBLE);
                            mRefreshableView.setVisibility(View.GONE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }, this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        mRefreshableView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        mRefreshableView.onRefreshComplete();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            if (ServiceType.SP.equals(service_type_id) && mAdapter.list.get(position - 1).getJSONObject("site").has("SITE_ID")) {
                Intent intent1 = new Intent(this, PAtyConsultStudioGoPaying.class);
                intent1.putExtra("service_item_id", "0");
                try {
                    intent1.putExtra(PAtyConsultStudioGoPaying.SITE_ID, mAdapter.list.get(position - 1).getJSONObject("site").optString("SITE_ID"));
                    intent1.putExtra("price", mAdapter.list.get(position - 1).optString("SERVICE_GOLD").toString());//价格
                    intent1.putExtra(PAtyConsultStudioGoPaying.ORDER_ID, mAdapter.list.get(position - 1).optString("ORDER_ID"));//订单id
                    intent1.putExtra("doctor_id", mAdapter.list.get(position - 1).getJSONObject("doctor_info").optString("CUSTOMER_ID"));//医生ID
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent1.putExtra("service_id", service_type_id);
                intent1.putExtra(PAtyConsultStudioGoPaying.SERVICETYPEID, service_type_id);
                startActivity(intent1);
            } else if (ServiceType.SP.equals(service_type_id) && !mAdapter.list.get(position - 1).getJSONObject("site").has("SITE_ID")) {
                Intent intent2 = new Intent(this, PAtyConsultStudioGoPaying.class);
                try {
                    intent2.putExtra(PAtyConsultStudioGoPaying.SITE_ID, mAdapter.list.get(position - 1).getJSONObject("site").optString("SITE_ID"));
                    intent2.putExtra("price2", mAdapter.list.get(position - 1).optString("SERVICE_GOLD").toString());//价格
                    intent2.putExtra(PAtyConsultStudioGoPaying.ORDER_ID, mAdapter.list.get(position - 1).optString("ORDER_ID"));//订单id
                    intent2.putExtra("doctor_id", mAdapter.list.get(position - 1).getJSONObject("doctor_info").optString("CUSTOMER_ID"));//医生ID
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                intent2.putExtra("service_id", service_type_id);
                intent2.putExtra(PAtyConsultStudioGoPaying.SERVICETYPEID, service_type_id);
                startActivity(intent2);
            } else {
                if ("5".equals(mAdapter.list.get(position - 1).optString("isBack"))) {
                    Intent intent1 = new Intent(this, PAtyConsultStudioGoPaying.class);
                    intent1.putExtra("service_item_id", "0");
                    try {
                        intent1.putExtra(PAtyConsultStudioGoPaying.SITE_ID, mAdapter.list.get(position - 1).getJSONObject("site").optString("SITE_ID"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    intent1.putExtra("price", mAdapter.list.get(position - 1).optString("SERVICE_GOLD").toString());//价格
                    intent1.putExtra(PAtyConsultStudioGoPaying.ORDER_ID, mAdapter.list.get(position - 1).optString("ORDER_ID"));//订单id
                    intent1.putExtra("doctor_id", mAdapter.list.get(position - 1).getJSONObject("doctor_info").optString("CUSTOMER_ID"));//医生ID
                    intent1.putExtra("service_id", service_type_id);
                    intent1.putExtra(PAtyConsultStudioGoPaying.SERVICETYPEID, service_type_id);
                    startActivity(intent1);
                } else {
                    String name = "";
                    String chatId = "";
                    String orderId = "";
                    try {
                        name = mAdapter.list.get(position - 1).getJSONObject("doctor_info").optString("DOCTOR_REAL_NAME");
                        chatId = mAdapter.list.get(position - 1).optString("SERVICE_CUSTOMER_ID");
                        orderId = mAdapter.list.get(position - 1).optString("ORDER_ID");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (!HStringUtil.isEmpty(mAdapter.list.get(position - 1).optString("site"))) {
                        doChat(mAdapter.list.get(position - 1).optString("GROUP_ID"), mAdapter.list.get(position - 1).optString("RECORD_NAME"), orderId);
                    } else {
                        FriendHttpUtil.chatFromPerson(ConsultActivity.this, chatId, name, orderId, ObjectType.SPECIAL_SERVER);
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        if (mAdapter.list.get(position - 1).optBoolean("isEnd")) {
//            Intent intent = new Intent(this, AppraiseTuAty.class);//订单完成  去评价 .
//            intent.putExtra(AppraiseTuAty.ISCOMMENT, mAdapter.list.get(position - 1).optBoolean("isComment"));
//            intent.putExtra("ORDER_ID", mAdapter.list.get(position - 1).optString("ORDER_ID"));
//            try {
//                intent.putExtra("NAME", mAdapter.list.get(position - 1).getJSONObject("doctor_info").optString("DOCTOR_REAL_NAME"));
//                intent.putExtra("TITLE", mAdapter.list.get(position - 1).getJSONObject("doctor_info").optString("TITLE_NAME"));
//                intent.putExtra("DOCTORID", mAdapter.list.get(position - 1).getJSONObject("doctor_info").optString("CUSTOMER_ID"));
//                intent.putExtra("IMAGE", mAdapter.list.get(position - 1).getJSONObject("doctor_info").optString("DOCTOR_PICTURE"));
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//            startActivity(intent);
//        } else if ("5".equals(mAdapter.list.get(position - 1).optString("isBack"))) {
//            Intent intent1 = new Intent(this, PAtyConsultStudioGoPaying.class);
//            intent1.putExtra("service_item_id", "0");
//            try {
//                intent1.putExtra(PAtyConsultStudioGoPaying.SITE_ID, mAdapter.list.get(position - 1).getJSONObject("site").optString("SITE_ID"));
//                intent1.putExtra("price", mAdapter.list.get(position - 1).optString("SERVICE_GOLD"));//价格
//                intent1.putExtra("doctor_id", mAdapter.list.get(position - 1).getJSONObject("doctor_info").optString("CUSTOMER_ID"));//医生ID
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            intent1.putExtra("service_id", service_type_id);
//            intent1.putExtra(PAtyConsultStudioGoPaying.SERVICETYPEID, service_type_id);
//            startActivity(intent1);
//        } else {
//            String name = "";
//            String chatId = "";
//            String orderId = "";
//            try {
//                name = mAdapter.list.get(position - 1).getJSONObject("doctor_info").optString("DOCTOR_REAL_NAME");
//                chatId = mAdapter.list.get(position - 1).optString("SERVICE_CUSTOMER_ID");
//                orderId = mAdapter.list.get(position - 1).optString("ORDER_ID");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            FriendHttpUtil.chatFromPerson(ConsultActivity.this, chatId, name, orderId, ObjectType.SPECIAL_SERVER);
//
//        }
//        if (HStringUtil.isEmpty(mAdapter.list.get(position).optString("site"))){
//            String name = "";
//            String chatId = "";
//            String orderId = "";
//            try {
//                name = mAdapter.list.get(position - 1).getJSONObject("doctor_info").optString("DOCTOR_REAL_NAME");
//                chatId = mAdapter.list.get(position - 1).optString("SERVICE_CUSTOMER_ID");
//                orderId = mAdapter.list.get(position - 1).optString("ORDER_ID");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            FriendHttpUtil.chatFromPerson(ConsultActivity.this, chatId, name, orderId, ObjectType.SPECIAL_SERVER);
//
//        }else {
//            if (mAdapter.list.get(position-1).optBoolean("isEnd")){
//                Intent intent = new Intent(this, AppraiseTuAty.class);//订单完成  去评价 .
//                intent.putExtra(AppraiseTuAty.ISCOMMENT,mAdapter.list.get(position-1).optBoolean("isComment"));
//                intent.putExtra("ORDER_ID",mAdapter.list.get(position - 1).optString("ORDER_ID"));
//                try {
//                    intent.putExtra("NAME",mAdapter.list.get(position - 1).getJSONObject("doctor_info").optString("DOCTOR_REAL_NAME"));
//                    intent.putExtra("TITLE",mAdapter.list.get(position - 1).getJSONObject("doctor_info").optString("TITLE_NAME"));
//                    intent.putExtra("DOCTORID",mAdapter.list.get(position - 1).getJSONObject("doctor_info").optString("CUSTOMER_ID"));
//                    intent.putExtra("IMAGE",mAdapter.list.get(position - 1).getJSONObject("doctor_info").optString("DOCTOR_PICTURE"));
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                startActivity(intent);
//            }else if ("5".equals(mAdapter.list.get(position).optString("isBack") )){
//                ToastUtil.showShort("去支付");
//            }else {
//                String name = "";
//                String chatId = "";
//                String orderId = "";
//                try {
//                    name = mAdapter.list.get(position - 1).getJSONObject("doctor_info").optString("DOCTOR_REAL_NAME");
//                    chatId = mAdapter.list.get(position - 1).optString("SERVICE_CUSTOMER_ID");
//                    orderId = mAdapter.list.get(position - 1).optString("ORDER_ID");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                FriendHttpUtil.chatFromPerson(ConsultActivity.this, chatId, name, orderId, ObjectType.SPECIAL_SERVER);
//
//
//            }
//        }


    }

    /**
     * 群聊天
     */
    public void doChat(String groupId, String conName, String orderID) {
        Intent intent = new Intent();
        intent.putExtra(ChatActivity.GROUP_ID, groupId);
        intent.putExtra(ChatActivity.ORDER_ID, orderID);
        intent.putExtra(ChatActivity.CONSULTATION_NAME, conName);
        intent.putExtra(ChatActivity.OBJECT_TYPE, ObjectType.TUWEN);
        intent.setClass(this, ChatActivity.class);
        startActivity(intent);
    }
}
