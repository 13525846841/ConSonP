package com.yksj.consultation.son.home;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.FamousHospitalAdapter;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.listener.OnRecyclerClickListener;
import com.yksj.healthtalk.entity.FamousHospitalEntity;
import com.yksj.healthtalk.entity.HospitaFindTeamEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;

import org.apache.http.message.BasicNameValuePair;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class FamousHospitalActivity extends Activity implements View.OnClickListener, OnRecyclerClickListener {

    private List<FamousHospitalEntity.ResultBean> mList=new ArrayList<>();
    private FamousHospitalAdapter hospitalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_famous_hospital);
        initView();
        loadDataWss();
    }
    private int lookupPosition=0;
    private void initView() {
        findViewById(R.id.title_back).setOnClickListener(this);
        TextView title= (TextView) findViewById(R.id.title_lable);
        title.setText("名医名院");
        RecyclerView famousHospitalRectcler= (RecyclerView) findViewById(R.id.famousHospitalRecycler);
        hospitalAdapter = new FamousHospitalAdapter(mList, this);
        hospitalAdapter.setmOnRecyclerClickListener(this);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        //根据返回数据中的字母改变RecyclerView中item的布局方式
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
               int lookupType=1;
                int oooo=0;
                if (position!=mList.size()-1&&!mList.get(position).getFIRST_LETTER().equals(mList.get(position+1).getFIRST_LETTER())){
                    for (int i = 0; i < mList.size(); i++) {
                        if (mList.get(position).getFIRST_LETTER().equals(mList.get(i).getFIRST_LETTER())){
                            oooo++;
                        }
                    }
                    if (oooo%2==0){
                        lookupType=1;
                    }else {
                        lookupType=2;
                    }
                }
                return lookupType;
            }
        });
        famousHospitalRectcler.setLayoutManager(gridLayoutManager);
        famousHospitalRectcler.setAdapter(hospitalAdapter);
    }

    private void loadDataWss(){
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("Type", "consultationHospital"));
        pairs.add(new BasicNameValuePair("flag", "1"));
        HttpRestClient.doGetWss(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String response) {
                Gson gson = new Gson();
                FamousHospitalEntity famousHospitalEntity = gson.fromJson(response, FamousHospitalEntity.class);
                mList.addAll(famousHospitalEntity.getResult());
                Collections.sort(mList, new Comparator<FamousHospitalEntity.ResultBean>() {
                    @Override
                    public int compare(FamousHospitalEntity.ResultBean o1, FamousHospitalEntity.ResultBean o2) {
                        if (o1.getFIRST_LETTER().charAt(0)>o2.getFIRST_LETTER().charAt(0)) {
                           return 1;
                        }else if (o1.getFIRST_LETTER().charAt(0)<o2.getFIRST_LETTER().charAt(0)){
                            return -1;
                        }
                        return 0;
                    }
                });
                hospitalAdapter.notifyDataSetChanged();
                Log.i("ttt", "onResponse: "+mList.size()+"      "+mList.get(0).getFIRST_LETTER());
            }
        },this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
           case R.id.title_back:
            finish();
            break;
        }
    }

    @Override
    public void onRecyclerItemClickListener(int position, View itemView, int type) {
        Intent intent = new Intent(this, DoctorWorkstationActivity.class);
        intent.putExtra("title",mList.get(position).getHOSPITAL_NAME());
        intent.putExtra("area_code",mList.get(position).getAREA_CODE());
        startActivity(intent);
    }
}
