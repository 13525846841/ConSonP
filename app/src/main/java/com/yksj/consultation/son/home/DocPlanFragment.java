package com.yksj.consultation.son.home;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.MyDocPlanAdapter;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.entity.PlanEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
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
 * Created by ${chen} on 2016/11/13.
 * 医教计划中的XX的医教计划中的fragment
 */

public class DocPlanFragment extends RootFragment implements PullToRefreshBase.OnRefreshListener2<ListView>, AdapterView.OnItemClickListener {

    private View view;
    private PullToRefreshListView pullListView;
    private ListView mListView;
    private MyDocPlanAdapter mAdapter;
    private String TYPELIST;
    private List<PlanEntity> data;
    private PlanEntity pEntity;
    private String children_id;
    public String name;
    public String sex;
    public String age;
    public String url;
    private View mEmptyView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.docplan_list_fragment,null);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        TYPELIST = getArguments().getString("typeList");   //分类型
        children_id = getArguments().getString("children_id");

        name = getArguments().getString("name");
        age = getArguments().getString("age");
        sex = getArguments().getString("sex");
        url = getArguments().getString("url");

        loadData();
    }

    /**
     * 加载数据
     */

    private void loadData() {
        Map<String,String> map=new HashMap<>();
        map.put("children_id", children_id);
        map.put("plan_complete", TYPELIST);
        HttpRestClient.OKHttpISRun(map, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
                ToastUtil.showShort("添加失败");
            }

            @Override
            public void onResponse(String content) {
                data = new ArrayList<PlanEntity>();
                try {
                    JSONObject obj = new JSONObject(content);
                    JSONArray array = obj.getJSONArray("plans");
                    JSONObject item;
                    for (int i = 0; i < array.length(); i++) {
                        item = array.getJSONObject(i);
                        pEntity = new PlanEntity();
                        pEntity.setPlan_title(item.optString("PLAN_TITLE"));
                        pEntity.setPlan_cycle(item.optString("PLAN_CYCLE"));
                        pEntity.setStart_time(item.optString("PLAN_START"));
                        pEntity.setPlan_id(item.optString("PLAN_ID"));
                        pEntity.setPlan_status(item.optString("PLAN_STATUS"));
                        data.add(pEntity);
                    }
                    mAdapter.onBoundData(data);
                    if (data.size()==0){
                        mEmptyView.setVisibility(View.VISIBLE);
                        pullListView.setVisibility(View.GONE);
                    }else {
                        mEmptyView.setVisibility(View.GONE);
                        pullListView.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }

    private void initView(View view) {
      //TYPELIST = getArguments().getInt("typeList");   //分类型
        pullListView = (PullToRefreshListView) view.findViewById(R.id.my_plan_pulllist);
        mEmptyView = view.findViewById(R.id.empty_view_famous1);
        mListView = pullListView.getRefreshableView();
        mAdapter=new MyDocPlanAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {

    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {

    }
    public String plan_id = "100083";
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(),SeePlanActivity.class);
        String planId=mAdapter.datas.get(position-1).getPlan_id();
        String PLAN_START = mAdapter.datas.get(position-1).getPlan_status();
        intent.putExtra("plan_id",planId);
        intent.putExtra("PLAN_START",PLAN_START);
        intent.putExtra("children_id",children_id);
        intent.putExtra("name",name);
        intent.putExtra("url",url);
        intent.putExtra("sex",sex);
        intent.putExtra("age",age);
        startActivity(intent);
    }
}
