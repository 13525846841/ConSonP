package com.yksj.consultation.son.consultation.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.CommondisAdapter;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.CommonwealAidAty;
import com.yksj.consultation.son.consultation.CommonwealAty;
import com.yksj.consultation.son.consultation.bean.Model;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ${chen} on 2016/12/4.
 * 六一百科常见疾病界面
 */
public class Commondis extends RootFragment implements AdapterView.OnItemClickListener {
    private ListView mGv;
    public CommondisAdapter adapter;
    private List<JSONObject> mList = null;
    public String class_id ="107";
    private View mEmptyView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.common_dis, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mEmptyView = view.findViewById(R.id.empty_view_common);
        mGv = (ListView) view.findViewById(R.id.common_dis);
        adapter = new CommondisAdapter(getActivity());
        mGv.setAdapter(adapter);
        mGv.setOnItemClickListener(this);
        initData();
    }

    private void initData() {
        Map<String,String> map=new HashMap<>();
        map.put("class_id",class_id);//101
        HttpRestClient.OKHttpCommonIllById(map,  new HResultCallback<String>(getActivity())  {
            @Override
            public void onError(Request request, Exception e){
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        JSONArray array = obj.optJSONArray("disease");
                        mList = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonobject = array.getJSONObject(i);
                            mList.add(jsonobject);
                        }
                        adapter.onBoundData(mList);
                        if (mList.size()==0){
                            mEmptyView.setVisibility(View.VISIBLE);
                            mGv.setVisibility(View.GONE);
                        }else {
                            mEmptyView.setVisibility(View.GONE);
                            mGv.setVisibility(View.VISIBLE);
                        }
                    }else {
                        ToastUtil.showShort(obj.optString("message"));
                        mEmptyView.setVisibility(View.VISIBLE);
                        mGv.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        },this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = new Intent(getActivity(),CommonwealAidAty.class);
//        intent.putExtra(CommonwealAidAty.URL,mList.get(position).optString("DISEASE_NAME"));
//        intent.putExtra(CommonwealAidAty.TITLE,mList.get(position).optString("DISEASE_NAME"));
//        startActivity(intent);
        Intent intent = new Intent(getActivity(),DiseaseDetailActivity.class);
        intent.putExtra("name",mList.get(position).optString("DISEASE_NAME"));
        intent.putExtra("disease_id",mList.get(position).optString("DISEASE_ID"));
        startActivity(intent);
    }



    public void setClassId(String roomObject){
        class_id = roomObject;
        initData();
    }
}
