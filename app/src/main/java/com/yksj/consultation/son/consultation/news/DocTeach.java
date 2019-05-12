package com.yksj.consultation.son.consultation.news;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.AdtConsultationOrders;
import com.yksj.consultation.adapter.DocTeachAdapter;
import com.yksj.consultation.adapter.FamousdochosAdapter;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.CommonwealAidAty;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.utils.ToastUtil;

import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

/**
 * Created by ${chen} on 2016/12/4.
 *名医讲堂
 */
public class DocTeach extends RootFragment implements AdapterView.OnItemClickListener {
    private PullToRefreshListView mRefreshableView;
    public DocTeachAdapter adapter;
    private List<JSONObject> mList = new ArrayList<>();
    private String class_id ="101";
    private View mEmptyView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.doc_teach, null);
        initView(view);
        return view;
    }


    private void initView(View view) {
        mRefreshableView = (PullToRefreshListView) view.findViewById(R.id.pull_refresh_listview);
        ListView mListView = mRefreshableView.getRefreshableView();
        adapter = new DocTeachAdapter(mList,getActivity());
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        mEmptyView = view.findViewById(R.id.fail_data_doc_teach);
        initData();
    }


    private void initData() {
        Map<String,String> map=new HashMap<>();
        map.put("class_id", class_id);//class_id 101
        HttpRestClient.OKHttpFamDocClassRoomDetail(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        JSONArray array = obj.optJSONArray("forum");
                        mList = new ArrayList<>();
                        if (array!=null){
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonobject = array.getJSONObject(i);
                                mList.add(jsonobject);
                            }
                        }

                        adapter.onBoundData(mList);
                        if (mList.size()==0){
                            mEmptyView.setVisibility(View.VISIBLE);
                            mRefreshableView.setVisibility(View.GONE);
                        }else {
                            mEmptyView.setVisibility(View.GONE);
                            mRefreshableView.setVisibility(View.VISIBLE);
                        }
                    }else{
                        ToastUtil.showShort(obj.optString("message"));
                        mEmptyView.setVisibility(View.VISIBLE);
                        mRefreshableView.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },this);
    }

    public void setClassId(String roomObject){
        class_id = roomObject;
        initData();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(),DoctorDetailActivity.class);
        intent.putExtra("FORUM_ID",adapter.getForum(position-1));

        startActivity(intent);
    }

}
