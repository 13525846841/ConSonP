package com.yksj.consultation.son.consultation.consultationorders;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.AdtConsultationOrders;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.bean.ConsultListBean;
import com.yksj.healthtalk.bean.ListDetails;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.SmartControlClient;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HEKl on 2015/9/15.
 * Used for 会诊订单_
 */
public class FgtMyOrders extends RootFragment implements PullToRefreshBase.OnRefreshListener2<ListView> {
    private PullToRefreshListView mRefreshableView;
    private AdtConsultationOrders mAdapter;
    private ConsultListBean bean;
    private int TYPELIST;
    private int blankSize;
    private int pageSize = 1;
    private int REFRESH = 0;
    private View mEmptyView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fgt_myorders, null);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        pageSize = 1;
        loadData();
    }

    private void initView(View view) {
        TYPELIST = getArguments().getInt("typeList");
        mEmptyView = view.findViewById(R.id.load_faile_layout);
        mRefreshableView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_listview);
        ListView mListView = mRefreshableView.getRefreshableView();
        mAdapter = new AdtConsultationOrders(getActivity(), TYPELIST);
        mListView.setAdapter(mAdapter);
        mRefreshableView.setOnRefreshListener(this);
    }
    /**
     * 会诊列表
     */
    @SuppressWarnings("deprecation")
    private void loadData() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("TERMINAL_TYPE", "findExpertByPatient"));
        pairs.add(new BasicNameValuePair("TYPE", TYPELIST + ""));
        pairs.add(new BasicNameValuePair("CUSTOMERID", SmartControlClient.getControlClient().getUserId()));
        pairs.add(new BasicNameValuePair("PAGESIZE", pageSize + ""));
        pairs.add(new BasicNameValuePair("PAGENUM", "20"));
        pairs.add(new BasicNameValuePair("VALID_MARK", "40"));
        HttpRestClient.OKHttpConsultationList(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                if (!TextUtils.isEmpty(response)) {
                    bean = gson.fromJson(response, ConsultListBean.class);
                    if (bean.getResult().size() == 0) {
                        blankSize = pageSize;
                        if (pageSize == 1) {
                            mEmptyView.setVisibility(View.VISIBLE);
                        } else {
                            ToastUtil.showShort("没有更多了");
                        }
                    } else if ("0".equals(bean.getCode())) {
                        ToastUtil.showShort(bean.getMessage());
                    } else {
                        ArrayList<ListDetails> success = bean.getResult();
                        mEmptyView.setVisibility(View.GONE);
                        if (pageSize == 1) {
                            if (mAdapter.datas.size() != 0) {
                                mAdapter.removeAll();
                            }
                        }
                        pageSize++;
                        mAdapter.addAll(success);
                    }
                }
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
            }

            @Override
            public void onAfter() {
                super.onAfter();
                mRefreshableView.onRefreshComplete();
                if (REFRESH == 1) {
                    ToastUtil.showShort("已更新");
                }
                REFRESH = 0;
            }
        }, this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mAdapter.removeAll();
    }

    //下拉更新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageSize = 1;
        REFRESH = 1;
        loadData();
    }

    //上拉加载更多
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        if (blankSize > 0) {
            pageSize = blankSize;
        }
        loadData();
    }
}
