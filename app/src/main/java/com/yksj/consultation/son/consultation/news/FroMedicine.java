package com.yksj.consultation.son.consultation.news;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.FroMedicineAdapter;
import com.yksj.consultation.adapter.PubFundAdapter;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.ItemDetailActivity;
import com.yksj.consultation.son.consultation.bean.NewsClass;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
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
 * Created by ${chen} on 2016/12/19.
 * 医学前沿
 * by chen
 */
public class FroMedicine extends RootFragment implements AdapterView.OnItemClickListener {

    private ListView mListview;
    public FroMedicineAdapter adapter;
    private List<JSONObject> mList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fro_medicine, null);
        initView(view);
        return view;
    }

    private void initView(View view) {
        mListview = (ListView) view.findViewById(R.id.fro_medicine);
        adapter = new FroMedicineAdapter(mList,getActivity());
        mListview.setAdapter(adapter);
        mListview.setOnItemClickListener(this);

        initData();
    }

    private void initData() {
        HttpRestClient.doHttpFroMedicine(new AsyncHttpResponseHandler(){
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        JSONArray array = obj.optJSONArray("frontier");
                        mList = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonobject = array.getJSONObject(i);
                            mList.add(jsonobject);
                        }
                        adapter.onBoundData(mList);
                    }else{
                        ToastUtil.showShort(obj.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
                ToastUtil.showShort("添加失败");
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getActivity(),ItemDetailActivity.class);
        //医学前沿ID
        intent.putExtra("frontiers_id",mList.get(position).optString("FRONTIERS_ID"));
        startActivity(intent);
    }

}
