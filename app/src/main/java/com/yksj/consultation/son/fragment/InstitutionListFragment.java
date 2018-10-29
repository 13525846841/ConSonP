package com.yksj.consultation.son.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.HospitalFindTeamAdapter;
import com.yksj.consultation.adapter.InstitutionListAdapter;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.home.InstitutionInfoMainActivity;
import com.yksj.consultation.son.home.InstitutionListActivity;
import com.yksj.healthtalk.entity.InstitutionHomeEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;

import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstitutionListFragment extends Fragment implements AdapterView.OnItemClickListener, PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener {


    private InstitutionListAdapter mAdapter;
    private int sort;//-1 我的机构  0体检中心 1拓展中心 2康复中心 3 兴趣中心
    private PullToRefreshListView pullListView;
    private int self;
    private TextView tipTv;

    public InstitutionListFragment() {
        // Required empty public constructor
    }

    private int pageNum = 1;
    private List<InstitutionHomeEntity.ResultBean> mList = new ArrayList<>();
    private View mEmptyView;
    private ListView mListView;
    private String cityType = "";
    private String cityCode = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_institution_list, container, false);
        initData();
        initView(view);
        loadData();
        return view;
    }

    private void initData() {
        int insType = getArguments().getInt(InstitutionListActivity.insF);
        self = getArguments().getInt(InstitutionListActivity.SELF);
        sort = getArguments().getInt(InstitutionListActivity.SORT);
        if (insType == 0) cityType = "hot";
        else if (insType == 1) cityType = "new";
        else cityType = "near";
        cityCode = getArguments().getString(InstitutionListActivity.ADDRESS);
    }

    private void initView(View view) {
        pullListView = (PullToRefreshListView) view.findViewById(R.id.pulllist);
        mEmptyView = view.findViewById(R.id.load_faile_layout);
        pullListView.setOnRefreshListener(this);
        tipTv = (TextView) view.findViewById(R.id.tipTv);
        tipTv.setOnClickListener(this);
        tipTv.setEnabled(false);
        mListView = pullListView.getRefreshableView();
        mListView.setSelector(new ColorDrawable(Color.parseColor("#00000000")));
        mListView.setOnItemClickListener(this);
        mAdapter = new InstitutionListAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);
    }

    private void loadData() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        if (sort == -1) {
            pairs.add(new BasicNameValuePair("op", "myOwn"));
            pairs.add(new BasicNameValuePair("customer_id", LoginServiceManeger.instance().getLoginUserId()));
        } else {
            pairs.add(new BasicNameValuePair("op", "queryCenter"));
            pairs.add(new BasicNameValuePair("center", (sort + 1) + ""));
            pairs.add(new BasicNameValuePair("type", cityType));
            pairs.add(new BasicNameValuePair("code", cityCode));
        }
        pairs.add(new BasicNameValuePair("pageNum", pageNum + ""));

        HttpRestClient.doGetInstitutionsServlet(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                tipTv.setText("加载失败，点击重试");
                tipTv.setEnabled(true);
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                pullListView.setRefreshing();
            }

            @Override
            public void onAfter() {
                pullListView.onRefreshComplete();
                super.onAfter();
            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                InstitutionHomeEntity institutionHomeEntity = gson.fromJson(response, InstitutionHomeEntity.class);
                mList.addAll(institutionHomeEntity.getResult());
                mAdapter.onBoundData(mList);
                if (mList.size() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    pullListView.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    pullListView.setVisibility(View.VISIBLE);
                }
                tipTv.setVisibility(View.GONE);
            }
        }, this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(), InstitutionInfoMainActivity.class);
        intent.putExtra(InstitutionInfoMainActivity.Unit_Code, mList.get(position - 1).getUNIT_CODE());
        if (self == InstitutionInfoActFragment.IS_SELF) {
            intent.putExtra(InstitutionInfoMainActivity.SELF, self);
        }
        startActivity(intent);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        mList.clear();
        pageNum = 1;
        loadData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        pageNum++;
        loadData();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tipTv:
                tipTv.setText("加载中...");
                tipTv.setEnabled(false);
                loadData();
                break;
        }
    }
}
