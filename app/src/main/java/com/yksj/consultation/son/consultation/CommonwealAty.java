package com.yksj.consultation.son.consultation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.yksj.consultation.adapter.AdtCommonWeal;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.LoadingFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by HEKL on 16/6/24.
 * Used for
 *
 * override by chen on 16/12
 */

public class CommonwealAty extends BaseFragmentActivity implements View.OnClickListener{

    LoadingFragmentDialog dialog;//网络加载提示
    private AdtCommonWeal adapter;
    private View mNullView;
    private List<JSONObject> mList = new ArrayList<>();
    private Button b1,b2,b3,b4;
    private RelativeLayout rl_doc, rl_item,rl_money,rl_give;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_commonweal);
        initView();
        //loadData();
    }

    private void initView() {
        initTitle();

        titleTextV.setText("公益活动");
        titleLeftBtn.setOnClickListener(this);
        mNullView = findViewById(R.id.mnullview);


        rl_doc = (RelativeLayout) findViewById(R.id.rl_doc);
        rl_item = (RelativeLayout) findViewById(R.id.rl_item);
        rl_money = (RelativeLayout) findViewById(R.id.rl_money);
        rl_give = (RelativeLayout) findViewById(R.id.rl_give);

        rl_doc.setOnClickListener(this);
        rl_item.setOnClickListener(this);
        rl_money.setOnClickListener(this);
        rl_give.setOnClickListener(this);

//        ListView mListView = (ListView) findViewById(R.id.list);
//        mListView.addFooterView(new View(this));
//        adapter = new AdtCommonWeal(mList, this);
//        mListView.setAdapter(adapter);
//        mListView.setOnItemClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent ;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.rl_doc://名医义诊
                intent = new Intent(CommonwealAty.this,FamDoctorActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_item://项目发布
                intent = new Intent(CommonwealAty.this,ProReleaseActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_money://公益基金
                intent = new Intent(CommonwealAty.this,PubFundActivity.class);

                startActivity(intent);
                break;
            case R.id.rl_give://我要捐赠
                intent = new Intent(this, CommonwealAidAty.class);
                intent.putExtra(CommonwealAidAty.URL, HTalkApplication.getHttpUrls().HTML+"/first.html");
                intent.putExtra(CommonwealAidAty.TITLE, "我要捐赠");
                startActivity(intent);
                break;

        }
    }

//    /**
//     * 获取公益活动分类
//     */
//    public void loadData() {
//        final List<BasicNameValuePair> list = new ArrayList<>();
//        list.add(new BasicNameValuePair("TYPE", "queryconsultationClass"));
//        list.add(new BasicNameValuePair("CLIENT_TYPE", HTalkApplication.CLIENT_TYPE));
//        list.add(new BasicNameValuePair("CONSULTATION_CENTER_ID", HTalkApplication.APP_CONSULTATION_CENTERID));
//        list.add(new BasicNameValuePair("CATEGORY_NAME", "Commonweal"));
//        HttpRestClient.addHttpHeader("client_type", HTalkApplication.CLIENT_TYPE);
//        HttpRestClient.OKHttpGetEntry(list, new OkHttpClientManager.ResultCallback<JSONObject>() {
//            @Override
//            public void onBefore(Request request) {
//                super.onBefore(request);
//                dialog = LoadingFragmentDialog.showLodingDialog(getSupportFragmentManager(), getResources());
//            }
//
//            @Override
//            public void onAfter() {
//                super.onAfter();
//                dialog.dismissAllowingStateLoss();
//            }
//
//            @Override
//            public void onError(Request request, Exception e) {
//
//            }
//
//            @Override
//            public void onResponse(JSONObject response) {
//                if (response.optInt("code") == 1) {
//                    try {
//                        JSONArray array = response.getJSONArray("result");
//                        int count = array.length();
//                        mList = new ArrayList<JSONObject>();
//                        for (int i = 0; i < count; i++) {
//                            JSONObject obj = array.getJSONObject(i);
//                            mList.add(obj);
//                        }
//                        adapter.onBoundData(mList);
//                        if (adapter.getCount()==0){
//                            mNullView.setVisibility(View.VISIBLE);
//                        }else {
//                            mNullView.setVisibility(View.GONE);
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                } else {
//                    ToastUtil.showShort(response.optString("message"));
//                }
//            }
//        }, this);
//    }
//
//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        Intent intent = null;
//        switch (mList.get(position).optString("OPERATION_TYPE")) {
//            case "RedirectUrl":
//                intent = new Intent(CommonwealAty.this, CommonwealAidAty.class);//公益活动
//                intent.putExtra(CommonwealAidAty.URL, mList.get(position).optString("OPERATION_PARA"));
//                intent.putExtra("TITLE", mList.get(position).optString("INFO_CLASS_NAME"));
//                startActivity(intent);
//                break;
//            case "LoadByCategory"://有分类
//            case "LoadByClass"://无分类
//                intent = new Intent(CommonwealAty.this, AtyNewsCenter.class);
//                intent.putExtra(AtyNewsCenter.TYPE, mList.get(position).optString("OPERATION_PARA"));
//                intent.putExtra("TITLE", mList.get(position).optString("INFO_CLASS_NAME"));
//                intent.putExtra("INFO_CLASS_ID", mList.get(position).optString("INFO_CLASS_ID"));
//                intent.putExtra("INFO_CLASS_NAME", mList.get(position).optString("INFO_CLASS_NAME"));
//                startActivity(intent);
//                break;
//        }
//    }
}
