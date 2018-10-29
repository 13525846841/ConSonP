package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.CategorizeListContentAdapter;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
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
 * Created by ${chen} on 2017/8/9.
 */
public class CategorizeListContentFragment extends Fragment implements PullToRefreshBase.OnRefreshListener2<ListView> {

    private CategorizeListContentAdapter adapter;
    private List<JSONObject> list = null;
    private PullToRefreshListView mRefreshableView;
    public ListView mListView;
    private int conPageSize = 1;//当前的页数
    private int classId;
    private View mEmptyView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.productlist_layout, null);
        mRefreshableView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_listview);
        mListView = mRefreshableView.getRefreshableView();
        mEmptyView = view.findViewById(R.id.empty_view_famous);
        classId = Integer.parseInt(getArguments().getString("index"));
        adapter = new CategorizeListContentAdapter(getActivity());
        mListView.setAdapter(adapter);
        mRefreshableView.setOnRefreshListener(this);
        initData(classId);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent  intent  = new Intent(getActivity(),ProductDetailAty.class);
                intent.putExtra("good_id",list.get(position-1).optString("GOODS_ID"));
                startActivity(intent);
            }
        });
        return view;
    }

    /**
     * 加载数据
     * @param classId
     */
    private void initData(int classId) {
        Map<String,String> map=new HashMap<>();
        map.put("Type", "findGoodsByClassId");
        map.put("class_id", String.valueOf(classId));
        map.put("pageNum",conPageSize + "" );
        map.put("pageSize", "10");
        HttpRestClient.OKHttGoodsServlet(map, new HResultCallback<String>(getActivity()) {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    list = new ArrayList<>();
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        if (!HStringUtil.isEmpty(obj.optString("server_params"))){
                            JSONObject object = obj.optJSONObject("server_params");
                            JSONArray array = object.optJSONArray("result");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonobject = array.getJSONObject(i);
                                list.add(jsonobject);
                            }
                            if (conPageSize == 1) {//第一次加载
                                if (list.size() == 0) {
                                    adapter.removeAll();
                                    adapter.addAll(list);
                                    mEmptyView.setVisibility(View.VISIBLE);
                                    mRefreshableView.setVisibility(View.GONE);
                                } else {
                                    mEmptyView.setVisibility(View.GONE);
                                    mRefreshableView.setVisibility(View.VISIBLE);
                                    adapter.removeAll();
                                    adapter.addAll(list);
                                }
                            } else {
                                if (list.size() != 0) {//加载出了数据
                                    adapter.addAll(list);
                                } else {
                                    ToastUtil.showShort("没有更多了");
                                }
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                mRefreshableView.setRefreshing();
                mRefreshableView.onRefreshComplete();
            }

            @Override
            public void onAfter() {
                mRefreshableView.onRefreshComplete();
                super.onAfter();
            }
        },this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        conPageSize = 1;
        initData(classId);
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        conPageSize++;
        initData(classId);
    }
}
