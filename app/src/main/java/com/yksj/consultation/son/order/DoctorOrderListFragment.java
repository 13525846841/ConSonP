package com.yksj.consultation.son.order;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.yksj.consultation.adapter.DoctorOrderAdapter;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;

import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshBase.OnLastItemVisibleListener;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * 医生订单
 *
 * @author jack_tang
 */
public class DoctorOrderListFragment extends RootFragment implements PullToRefreshBase.OnRefreshListener2, OnLastItemVisibleListener {

    private String TYPE = "7";
    private int PAGE_NUMBER = 1; //页数
    private DoctorOrderAdapter mAdapter;
    private PullToRefreshListView mRefreshableView;
    private JSONArray mArray ;
    private List<JSONObject> mList ;
    private View mEmptyView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doctor_order_fragment_layout, null);
        initView(view);
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("type", TYPE);
        outState.putString("PAGE_NUMBER", String.valueOf(PAGE_NUMBER));
        JSONArray array = new JSONArray();
        for (int i = 0; i < mAdapter.getDatas().size(); i++) {
            array.put(mAdapter.getDatas().get(i));
        }
        outState.putString("data", array.toString());
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TYPE = getArguments().getString("type");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//		initData();
        if (savedInstanceState != null) {
            TYPE = savedInstanceState.getString("type");
            PAGE_NUMBER = Integer.valueOf(savedInstanceState.getString("PAGE_NUMBER"));
            String data = savedInstanceState.getString("data");
            try {
                JSONArray mArray = new JSONArray(data);
                List<JSONObject> mList = new ArrayList<JSONObject>();
                for (int i = 0; i < mArray.length(); i++) {
                    mList.add(mArray.optJSONObject(i));
                }
                mAdapter.onBoundData(mList);
            } catch (Exception e) {
            }
        } else {
            mRefreshableView.setRefreshing();

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        PAGE_NUMBER = 1;
        initData();
    }

    private void initView(View view) {
        mRefreshableView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_listview);
        mEmptyView = view.findViewById(R.id.empty_view);
        ListView mListView = mRefreshableView.getRefreshableView();
        mRefreshableView.setOnRefreshListener(this);
        mAdapter = new DoctorOrderAdapter(mActivity);
        mListView.setAdapter(mAdapter);
        mRefreshableView.setOnLastItemVisibleListener(this);
    }


    private synchronized void initData() {
        /**
         * 我的订单（医生端） 192.168.16.157:8899/DuoMeiHealth/FindMyPatientDetails32
         customerId 我的id  type 列表序号  pageSize 第几页  pageNum 每页几条 date（格式：201303）日期
         serviceTypeId 服务类型   (目前已完成列表无日期，服务类型筛选date=0，serviceTypeId=0即可)
         列表序号 0-服务中 1-待服务  3-已完成 4-待支付  7-全部
         */
        RequestParams mParams = new RequestParams();
        mParams.put("TERMINAL_TYPE", "patient");
        mParams.put("TYPE", TYPE);
        mParams.put("PAGESIZE", String.valueOf(PAGE_NUMBER));
        mParams.put("PAGENUM", "10");
        mParams.put("CUSTOMERID", LoginServiceManeger.instance().getLoginEntity().getId());
        HttpRestClient.doHttpFindmypatientdetails32(mParams, new AsyncHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, String response) {
                super.onSuccess(statusCode, response);
                PAGE_NUMBER++;
                if (!TextUtils.isEmpty(response)) {
                    try {
                        JSONObject object = new JSONObject(response);
                        int count = object.getJSONArray("result").length();
                        mList = new ArrayList<JSONObject>();
                        mArray= new JSONArray();
                        for (int i = 0; i < count; i++) {
                            mArray.put(object.getJSONArray("result").get(i));
                            mList.add(mArray.optJSONObject(i));
                        }
                        if (PAGE_NUMBER == 2)
                            mAdapter.datas.clear();
                        mAdapter.addAll(mList);
                        if (mAdapter.datas.size() == 0) {
                            mEmptyView.setVisibility(View.VISIBLE);
                            mRefreshableView.setVisibility(View.GONE);
                        } else {
                            mEmptyView.setVisibility(View.GONE);
                            mRefreshableView.setVisibility(View.VISIBLE);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFinish() {
                super.onFinish();
                mRefreshableView.onRefreshComplete();
            }

        });
    }


//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position,
//                            long id) {
//        CustomerInfoEntity entity = new CustomerInfoEntity();
//        JSONObject jsonObject = mAdapter.getDatas().get(position - 1);
//        String uid = jsonObject.optString("customerId");
//        //昵称
//        if (!HStringUtil.isEmpty(jsonObject.optString("remarksName", ""))) {
//            entity.setName(jsonObject.optString("remarksName", ""));
//        } else {
//            entity.setName(jsonObject.optString("customerNickname", ""));
//        }
//        entity.setId(uid);
//        FriendHttpUtil.chatFromPerson(getActivity(), entity);
//    }

    @Override
    public void onLastItemVisible() {
        initData();
    }


    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        PAGE_NUMBER=1;
        initData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        initData();
    }
}
