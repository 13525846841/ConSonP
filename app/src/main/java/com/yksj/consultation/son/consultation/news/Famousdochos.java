package com.yksj.consultation.son.consultation.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.FamousdochosAdapter;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.CommonwealAidAty;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
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
 * Created by ${chen} on 2016/12/4.
 *  名医名院
 */
public class Famousdochos extends RootFragment implements AdapterView.OnItemClickListener,PullToRefreshBase.OnRefreshListener2<ListView> {
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    public FamousdochosAdapter adapter;
    private List<JSONObject> list = null;
    private int conPageSize = 1;//当前的页数
    private String class_id ="0";
    private int pageNum = 1;//全部
    private View mEmptyView;
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fam_doc_hos, null);
        initView(view);
        return view;
    }

    private void initView(View view) {

        mPullRefreshListView = (PullToRefreshListView)view.findViewById(R.id.fam_doc_hos);
        mListView = mPullRefreshListView.getRefreshableView();
        mListView.setOnItemClickListener(this);
        mPullRefreshListView.setOnRefreshListener(this);
        adapter = new FamousdochosAdapter(getActivity());
        mListView.setAdapter(adapter);

        mEmptyView = view.findViewById(R.id.empty_view_famous_doctor);
        initData();
    }

    private void initData() {
        Map<String,String> map=new HashMap<>();
        map.put("class_id", class_id);//class_id
        map.put("pageNum", ""+pageNum);
        HttpRestClient.OKHttpFamHosDetail(map, new HResultCallback<String>(getActivity()) {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    list = new ArrayList<>();
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){

                        if (!HStringUtil.isEmpty(obj.optString("hosipital"))){
                            JSONArray array = obj.optJSONArray("hosipital");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonobject = array.getJSONObject(i);
                                list.add(jsonobject);
                            }

                            if (pageNum == 1) {//第一次加载
                                if (list.size() == 0) {
                                    adapter.removeAll();
                                    adapter.addAll(list);
                                    mEmptyView.setVisibility(View.VISIBLE);
                                    mListView.setVisibility(View.GONE);
                                } else {
                                    adapter.removeAll();
                                    adapter.addAll(list);
                                    mEmptyView.setVisibility(View.GONE);
                                    mListView.setVisibility(View.VISIBLE);
                                }

                            }else {
                                if (list.size() != 0) {//加载出了数据
                                    adapter.removeAll();
                                    adapter.addAll(list);
                                }
                            }
                        }else {
                            ToastUtil.showShort("没有更多了");
                        }

                    } else {
                        ToastUtil.showShort(obj.optString("code"));
                        mEmptyView.setVisibility(View.VISIBLE);
                        mListView.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                mPullRefreshListView.setRefreshing();
            }

            @Override
            public void onAfter() {
                mPullRefreshListView.onRefreshComplete();
                super.onAfter();
            }
        },this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(),CommonwealAidAty.class);
        intent.putExtra(CommonwealAidAty.URL, HTalkApplication.getHttpUrls().HTML+"/hos.html"+ "?hospital_id="+list.get(position).optString("HOSPITAL_ID"));
        intent.putExtra(CommonwealAidAty.TITLE,"六一百科");
        intent.putExtra(CommonwealAidAty.TYPE,"1");
        startActivity(intent);
    }

    public void setClassId(String roomObject){
        class_id = roomObject;
        pageNum=1;
        initData();
    }

    //下拉刷新
    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        mPullRefreshListView.onRefreshComplete();
    }
    //上拉加载
    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        pageNum++;
        initData();
    }
}
