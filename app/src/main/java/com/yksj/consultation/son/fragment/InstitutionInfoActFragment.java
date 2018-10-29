package com.yksj.consultation.son.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.InstitutionInfoActAdapter;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.home.InstitutionDoingActivity;
import com.yksj.consultation.son.home.InstitutionInfoMainActivity;
import com.yksj.consultation.son.listener.OnRecyclerClickListener;
import com.yksj.consultation.son.simcpux.Constants;
import com.yksj.healthtalk.entity.InsActEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstitutionInfoActFragment extends Fragment implements View.OnClickListener, OnRecyclerClickListener {


    public static final int IS_SELF = 999;
    private InstitutionInfoActAdapter adapter;
    private RecyclerView actRecycler;
    private TextView tipTv;
    public InstitutionInfoActFragment() {
        // Required empty public constructor
    }

    private String unit_code;
    private List<InsActEntity.ResultBean> mList = new ArrayList<>();
    private NestedScrollView mEmptyView;
    public static InstitutionInfoActFragment newInstance(int unit_code, int self) {
        Bundle args = new Bundle();
        args.putString(InstitutionInfoMainActivity.Unit_Code, unit_code + "");
        args.putInt(InstitutionInfoMainActivity.SELF, self);
        InstitutionInfoActFragment fragment = new InstitutionInfoActFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_institution_info_act, container, false);
        EventBus.getDefault().register(this);
        initView(view);
        loadData();
        return view;
    }

    private void initView(View view) {
        unit_code = getArguments().getString("Unit_Code");
        int selfAct = getArguments().getInt("self", -1);
        Button addAct = (Button) view.findViewById(R.id.addAct);
        addAct.setOnClickListener(this);
        tipTv = (TextView) view.findViewById(R.id.tipTv);
        tipTv.setOnClickListener(this);
        tipTv.setEnabled(false);
        mEmptyView = (NestedScrollView) view.findViewById(R.id.load_faile_nestedScrollView);
        actRecycler = (RecyclerView) view.findViewById(R.id.actRecycler);
        actRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new InstitutionInfoActAdapter(mList, getActivity());
        adapter.setmOnRecyclerClickListener(this);
        if (selfAct == IS_SELF) {
            adapter.setIsSelf(true);//只有是查看自己机构是 可以编辑或添加活动
            addAct.setVisibility(View.VISIBLE);
        }
        actRecycler.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addAct:
                Intent intent = new Intent(getActivity(), InstitutionDoingActivity.class);
                intent.putExtra("Unit_Code", unit_code);
                startActivity(intent);
                break;
            case R.id.tipTv:
                tipTv.setText("加载中...");
                tipTv.setEnabled(false);
                loadData();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12333 && resultCode == 33321 && data != null) {
            mList.clear();
            loadData();
        }
    }

    private void loadData() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("op", "queryDetails"));
        pairs.add(new BasicNameValuePair("att", "20"));
        pairs.add(new BasicNameValuePair("Unit_Code", unit_code));

        HttpRestClient.doGetInstitutionsServlet(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                tipTv.setText("加载失败，点击重试");
                tipTv.setEnabled(true);
            }

            @Override
            public void onResponse(String response) {
                Log.i("lll", "onResponse: " + response);
                Gson gson = new Gson();
                InsActEntity insActEntity = gson.fromJson(response, InsActEntity.class);
                List<InsActEntity.ResultBean> result = insActEntity.getResult();
                mList.addAll(result);
                adapter.notifyDataSetChanged();
                if (mList.size()==0){
                    mEmptyView.setVisibility(View.VISIBLE);
                    actRecycler.setVisibility(View.GONE);
                }else {
                    mEmptyView.setVisibility(View.GONE);
                    actRecycler.setVisibility(View.VISIBLE);
                }
                tipTv.setVisibility(View.GONE);
            }
        }, this);
    }

    //item又上角编辑活动的点击事件
    @Override
    public void onRecyclerItemClickListener(int position, View itemView, int type) {
        Intent intent = new Intent(getActivity(), InstitutionDoingActivity.class);
        intent.putExtra("activ_Code", mList.get(position).getACTIV_CODE() + "");
        intent.putExtra("Unit_Code", unit_code);
        intent.putExtra("doing", mList.get(position));
        startActivity(intent);
    }


    public void onEvent(MyEvent event) {
        if (event.code== 12333) {
            mList.clear();
            loadData();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
