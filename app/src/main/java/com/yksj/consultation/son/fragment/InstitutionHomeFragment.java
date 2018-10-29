package com.yksj.consultation.son.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.InstititionHomeRecyclerAdapter;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.home.InstitutionHomeActivity;
import com.yksj.consultation.son.home.InstitutionInfoMainActivity;
import com.yksj.consultation.son.listener.OnRecyclerClickListener;
import com.yksj.healthtalk.entity.InstitutionHomeEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstitutionHomeFragment extends Fragment implements OnRecyclerClickListener, View.OnClickListener {
    private static final String insF = "insF";
    private static final String code = "code";
    private List<InstitutionHomeEntity.ResultBean> mList = new ArrayList<>();
    private String dataType = "";
    private String cityCode = "110101";
    private InstititionHomeRecyclerAdapter adapter;
    private NestedScrollView mEmptyView;
    private RecyclerView insHomeRecycler;
    private int pageNum=1;
    private TwinklingRefreshLayout refresh;
    private TextView tipTv;
    public static InstitutionHomeFragment newInstance(int type, String code) {
        Bundle args = new Bundle();
        args.putInt(insF, type);
        args.putString(code, code);
        InstitutionHomeFragment fragment = new InstitutionHomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_imstiution_home, container, false);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        int insType = getArguments().getInt(insF);
        if (insType == 0) dataType = "hot";
        else if (insType == 1) dataType = "new";
        else dataType = "near";
        cityCode = getArguments().getString(code,"110101");
        tipTv= (TextView) view.findViewById(R.id.tipTv);
        tipTv.setOnClickListener(this);
        tipTv.setEnabled(false);
        refresh = (TwinklingRefreshLayout) view.findViewById(R.id.refresh);
        refresh.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onLoadMore(TwinklingRefreshLayout refreshLayout) {
                super.onLoadMore(refreshLayout);
                pageNum++;
                loadData();
            }
        });
        mEmptyView = (NestedScrollView) view.findViewById(R.id.load_faile_nestedScrollView);
        insHomeRecycler = (RecyclerView) view.findViewById(R.id.insHomeRecycler);
        insHomeRecycler.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        adapter = new InstititionHomeRecyclerAdapter(mList, getActivity());
        adapter.setmOnRecyclerClickListener(this);
        insHomeRecycler.setAdapter(adapter);
        loadData();
        return view;
    }

    private void loadData() {
        final List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("op", "queryCenter"));
        pairs.add(new BasicNameValuePair("center", ""));
        pairs.add(new BasicNameValuePair("type", dataType));
//        pairs.add(new BasicNameValuePair("code", "0"));
        pairs.add(new BasicNameValuePair("code", cityCode));
        pairs.add(new BasicNameValuePair("pageNum", pageNum+""));

        HttpRestClient.doGetInstitutionsServlet(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                tipTv.setText("加载失败，点击重试");
                tipTv.setEnabled(true);
            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                InstitutionHomeEntity institutionHomeEntity = gson.fromJson(response, InstitutionHomeEntity.class);
                List<InstitutionHomeEntity.ResultBean> result = institutionHomeEntity.getResult();
                mList.addAll(result);
                adapter.notifyDataSetChanged();
                if (mList.size() == 0) {
                    mEmptyView.setVisibility(View.VISIBLE);
                    insHomeRecycler.setVisibility(View.GONE);
                } else {
                    mEmptyView.setVisibility(View.GONE);
                    insHomeRecycler.setVisibility(View.VISIBLE);
                }
                if (result.size()==0&&pageNum>1){
                    ToastUtil.onShow(getActivity(),"没有更多数据了",1000);
                }
                refresh.finishLoadmore();
                tipTv.setVisibility(View.GONE);
            }
        }, this);
    }

    @Override
    public void onRecyclerItemClickListener(int position, View itemView, int type) {
        Intent intent = new Intent(getActivity(), InstitutionInfoMainActivity.class);
        intent.putExtra(InstitutionInfoMainActivity.Unit_Code, mList.get(position).getUNIT_CODE());
        startActivity(intent);
    }
    public void onEvent(MyEvent event) {
        if (event.code== InstitutionHomeActivity.EVENT_HOME_MSG) {
            cityCode=event.what;
            mList.clear();
            loadData();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().unregister(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tipTv:
                tipTv.setText("加载中...");
                tipTv.setEnabled(false);
                loadData();
                break;
        }
    }
}
