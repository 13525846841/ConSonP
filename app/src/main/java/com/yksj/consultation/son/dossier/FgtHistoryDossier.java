package com.yksj.consultation.son.dossier;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import com.yksj.consultation.adapter.LookHistoryAdapter;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
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

public class FgtHistoryDossier extends RootFragment  implements PullToRefreshBase.OnRefreshListener2{
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private LookHistoryAdapter mAdapter;
    private int pagesize1=1,pagesize2=1,pagesize3=1;
    private List<Map<String ,String >> mDatas;
    private Map<String ,String> mMap;
    private int mType;
    private int one=1;
    private View nullView;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        Bundle bun=getArguments();
        int type=bun.getInt("DOSSIER");
        View mView = inflater.inflate(R.layout.fragment_fgt_history_dossier, container, false);
        mPullToRefreshListView=(PullToRefreshListView) mView.findViewById(R.id.dossier_list);
        nullView=mView.findViewById(R.id.dossier_list_null);
        mListView=mPullToRefreshListView.getRefreshableView();
        mAdapter = new LookHistoryAdapter(getActivity());
        mListView.setAdapter(mAdapter);
        mPullToRefreshListView.setOnRefreshListener(this);
        if(one==1){
            switch (type){
                case 112:
                    mType=112;
                    initDataShare();
                    break;
                case 113:
                    mType=113;
                    initDataMyShare();
                    break;
                case 114:
                    mType=114;
                    initDataFocus();
                    break;
            }
            one=2;
        }
        return mView;
    }
    //我关注的
    private void initDataFocus() {
        HttpRestClient.doHttpMedicalCaseDiscussionFocus(pagesize3,new ObjectHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                mPullToRefreshListView.setRefreshing();
            }
            @Override
            public void onFinish() {
                super.onFinish();
                mPullToRefreshListView.onRefreshComplete();
            }
            @Override
            public Object onParseResponse(String content) {
                if (content.contains("error_code")) {
                    return content;
                } else {
                    try {
                        JSONObject object = new JSONObject(content);
                        mDatas = new ArrayList<Map<String, String>>();
                        JSONArray array = object.getJSONArray("medicalCaseDiscussionFocus");
                        JSONObject itme = null;
                        for (int i = 0; i < array.length(); i++) {
                            itme = array.getJSONObject(i);
                            mMap = new HashMap<String, String>();
                            mMap.put("MEDICAL_RECORD_ID", itme.optString("MEDICAL_RECORD_ID"));
                            mMap.put("MEDICAL_NAME", itme.optString("MEDICAL_NAME"));
                            mMap.put("RELATION_TIME", itme.optString("RELATION_TIME"));
                            mDatas.add(mMap);
                        }
                        return mDatas;
                    } catch (JSONException e) {
                        return null;
                    }
                }
            }
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                if (response instanceof String) {
                    JSONObject object;
                    try {
                        object = new JSONObject((String) response);
                        ToastUtil.showShort(object.optString("error_message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (response instanceof List) {
                    if(response==null){
                        nullView.setVisibility(View.VISIBLE);
                        mPullToRefreshListView.setVisibility(View.GONE);
                    }else if (response != null) {
                        mAdapter.addAll((List<Map<String, String>>) response);
                        if (mAdapter.datas.size() == 0) {
                            nullView.setVisibility(View.VISIBLE);
                            mPullToRefreshListView.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }
    //我共享的
    private void initDataMyShare() {
        HttpRestClient.doHttpMyMedicalCaseDiscussionShare(pagesize2,new ObjectHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                mPullToRefreshListView.setRefreshing();
            }
            @Override
            public void onFinish() {
                super.onFinish();
                mPullToRefreshListView.onRefreshComplete();
            }
            @Override
            public Object onParseResponse(String content) {
                if (content.contains("error_code")) {
                    return content;
                } else {
                    try {
                        JSONObject object = new JSONObject(content);
                        mDatas = new ArrayList<Map<String, String>>();
                        JSONArray array = object.getJSONArray("myMedicalCaseDiscussionShare");
                        JSONObject itme = null;
                        for (int i = 0; i < array.length(); i++) {
                            itme = array.getJSONObject(i);
                            mMap = new HashMap<String, String>();
                            mMap.put("MEDICAL_RECORD_ID", itme.optString("MEDICAL_RECORD_ID"));
                            mMap.put("MEDICAL_NAME", itme.optString("MEDICAL_NAME"));
                            mMap.put("SHARE_TIME", itme.optString("SHARE_TIME"));
                            mDatas.add(mMap);
                        }
                        return mDatas;
                    } catch (JSONException e) {
                        return null;
                    }
                }
            }
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);

                if (response instanceof String) {
                    JSONObject object;
                    try {
                        object = new JSONObject((String) response);
                        ToastUtil.showShort(object.optString("error_message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (response instanceof List) {
                    if(response==null){
                        nullView.setVisibility(View.VISIBLE);
                        mPullToRefreshListView.setVisibility(View.GONE);
                    }else if (response != null) {
                        mAdapter.addAll((List<Map<String, String>>) response);
                        if (mAdapter.datas.size() == 0) {
                            nullView.setVisibility(View.VISIBLE);
                            mPullToRefreshListView.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }

    //同行共享
    private void initDataShare() {
        HttpRestClient.doHttpMedicalCaseDiscussionShare(pagesize1,new ObjectHttpResponseHandler() {
            @Override
            public void onStart() {
                super.onStart();
                mPullToRefreshListView.setRefreshing();
            }
            @Override
            public void onFinish() {
                super.onFinish();
                mPullToRefreshListView.onRefreshComplete();
            }
            @Override
            public Object onParseResponse(String content) {
                if (content.contains("error_code")) {
                    return content;
                } else {
                    try {
                        JSONObject object = new JSONObject(content);
                        mDatas = new ArrayList<Map<String, String>>();
                        JSONArray array = object.getJSONArray("medicalCaseDiscussionShare");
                        JSONObject itme = null;
                        for (int i = 0; i < array.length(); i++) {
                            itme = array.getJSONObject(i);
                            mMap = new HashMap<String, String>();
                            mMap.put("MEDICAL_RECORD_ID", itme.optString("MEDICAL_RECORD_ID"));
                            mMap.put("MEDICAL_NAME", itme.optString("MEDICAL_NAME"));
                            mMap.put("SHARE_TIME", itme.optString("SHARE_TIME"));
                            mDatas.add(mMap);
                        }
                        return mDatas;
                    } catch (JSONException e) {
                        return null;
                    }
                }
            }
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                if (response instanceof String) {
                    JSONObject object;
                    try {
                        object = new JSONObject((String) response);
                        ToastUtil.showShort(object.optString("error_message"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (response instanceof List) {
                    if(response==null){
                        nullView.setVisibility(View.VISIBLE);
                        mPullToRefreshListView.setVisibility(View.GONE);
                    }else if (response != null) {
                        mAdapter.addAll((List<Map<String, String>>) response);
                        if (mAdapter.datas.size() == 0) {
                            nullView.setVisibility(View.VISIBLE);
                            mPullToRefreshListView.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }
    @Override
    public void onStart() {
        mAdapter.removeAll();
        Bundle bun=getArguments();
        int type=bun.getInt("DOSSIER");
        if(one==1){
            switch (type){
                case 112:
                    mType=112;
                    initDataShare();
                    break;
                case 113:
                    mType=113;
                    initDataMyShare();
                    break;
                case 114:
                    mType=114;
                    initDataFocus();
                    break;
            }
            one=2;
        }
        super.onStart();
    }
    @Override
    public void onStop() {
        mAdapter.removeAll();
        if(one==2){
            one=1;
        }
        super.onStop();
    }
    @Override
    public void onPullDownToRefresh(PullToRefreshBase refreshView) {
        switch (mType){
            case 112:
                pagesize1=1;
                mAdapter.removeAll();
                initDataShare();
                break;
            case 113:
                pagesize2=1;
                mAdapter.removeAll();
                initDataMyShare();
                break;
            case 114:
                pagesize3=1;
                mAdapter.removeAll();
                initDataFocus();
                break;
        }
    }
    @Override
    public void onPullUpToRefresh(PullToRefreshBase refreshView) {
        switch (mType){
            case 112:
                ++pagesize1;
                initDataShare();
                break;
            case 113:
                ++pagesize2;
                initDataMyShare();
                break;
            case 114:
                ++pagesize3;
                initDataFocus();
                break;
        }
    }
}
