package com.yksj.consultation.son.home;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.FamousDocShareAdapter;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.listener.OnListItemClick;
import com.yksj.healthtalk.entity.HospitaFindTeamEntity;
import com.yksj.healthtalk.entity.ShareEntity;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.CommonUtils;
import com.yksj.healthtalk.utils.ToastUtil;

import org.apache.http.message.BasicNameValuePair;
import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yksj.consultation.son.R.id.comment_count;

public class FamousDoctorShareActivity extends FragmentActivity implements PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener, AdapterView.OnItemClickListener, OnListItemClick {

    private PullToRefreshListView pullToList;
    private String Site_Id;
    private int PageSize=10;
    private int Page=1;
    private List<ShareEntity.ResultBean> shareList=new ArrayList<ShareEntity.ResultBean>();
    private FamousDocShareAdapter famousDocShareAdapter;
    private String loadType="works";
    private View mEmptyView;
    private EditText editText;
    private RelativeLayout edittextbody;
    private ImageView sendIv;
    private int commentPosition=0;
    private int doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_famous_doctor_share);
        initView();
        loadDataWss();
    }

    private void initView() {
        loadType = getIntent().getStringExtra("type");
        doctorId = getIntent().getIntExtra("Doctor_ID",-1);
        Site_Id = getIntent().getStringExtra("Site_Id");
        findViewById(R.id.title_back).setOnClickListener(this);
        TextView tvTitle= (TextView) findViewById(R.id.title_lable);
        tvTitle.setText("名医分享");
        edittextbody = (RelativeLayout) findViewById(R.id.editTextBodyLl);
        mEmptyView = findViewById(R.id.empty_view_famous);
        sendIv = (ImageView) findViewById(R.id.sendIv);
        sendIv.setOnClickListener(this);
        editText = (EditText) findViewById(R.id.circleEt);
        pullToList = (PullToRefreshListView) findViewById(R.id.sharePullToList);
        pullToList.setOnRefreshListener(this);
        ListView listView = pullToList.getRefreshableView();
        listView.setSelector(new ColorDrawable(Color.parseColor("#00000000")));
        listView.setOnItemClickListener(this);
        famousDocShareAdapter = new FamousDocShareAdapter(this, shareList,getSupportFragmentManager());
        famousDocShareAdapter.setSiteId(Site_Id+"");
        famousDocShareAdapter.setOnListItemClick(this);
        listView.setAdapter(famousDocShareAdapter);
        listView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (edittextbody.getVisibility() == View.VISIBLE) {
                    updateEditTextBodyVisible(View.GONE);
                    return true;
                }
                return false;
            }
        });
    }


    //医生个人界面和工作站的名医分享  all全部的名医分享
    private void loadDataWss() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        if (loadType.equals("works")){
            pairs.add(new BasicNameValuePair("Type", "shareInWorkSite"));
            pairs.add(new BasicNameValuePair("Site_Id", Site_Id));
            pairs.add(new BasicNameValuePair("Doctor_ID", LoginServiceManeger.instance().getLoginEntity().getId()));
        }else if (loadType.equals("all")){
            pairs.add(new BasicNameValuePair("Type", "allShare"));
        }
        else {
            pairs.add(new BasicNameValuePair("Type", "shareInDoctor"));
            pairs.add(new BasicNameValuePair("Doctor_ID", doctorId+""));
        }

//        pairs.add(new BasicNameValuePair("Area_Code", areaCode));
        pairs.add(new BasicNameValuePair("PageSize", PageSize+""));
        pairs.add(new BasicNameValuePair("Page", Page+""));
        pairs.add(new BasicNameValuePair("Customer_Id", LoginServiceManeger.instance().getLoginEntity().getId()));
        HttpRestClient.doGetWss(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onBefore(Request request) {
                super.onBefore(request);
                pullToList.setRefreshing();
            }

            @Override
            public void onAfter() {
                pullToList.onRefreshComplete();
                super.onAfter();
            }

            @Override
            public void onResponse(String response) {
                Log.i("lll", "onResponse: "+response);
                Gson gson = new Gson();
                ShareEntity shareEntity = gson.fromJson(response, ShareEntity.class);
                shareList.addAll(shareEntity.getResult());
                famousDocShareAdapter.onBoundData(shareList);

                if (shareList.size()==0){
                    mEmptyView.setVisibility(View.VISIBLE);
                }
            }
        },this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.title_back: finish();break;
            case R.id.sendIv:
                //发布评论
                String content = editText.getText().toString().trim();
                if (TextUtils.isEmpty(content)) {
                    ToastUtil.showShort(this, "评论内容不能为空");
                    return;
                }
                addComment(content,commentPosition);
                updateEditTextBodyVisible(View.GONE);
                break;
        }
    }

    /**
     * http://192.168.1.108:8080/DuoMeiHealth/DoctorWorkSiteServlet?op=commentSare&customer_id=3774&comment_customer_id=3773&comment_content=123123&share_id=7
     *
     * @param content
     */
    public void addComment(final String content, final int pos) {
        String share_id = shareList.get(pos).getSHARE_ID()+"";

        Map<String, String> map = new HashMap<>();
        map.put("op", "commentSare");
        map.put("comment_content", content);
        map.put("customer_id", LoginServiceManeger.instance().getLoginEntity().getId());//评论者
        map.put("comment_customer_id", shareList.get(pos).getCUSTOMER_ID()+"");
        map.put("share_id", share_id);
        HttpRestClient.OKHttpStationCommonUrl(map, new HResultCallback<String>(FamousDoctorShareActivity.this) {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if ("1".equals(obj.optString("code"))) {
                        JSONObject result = obj.getJSONObject("result");
                        int comment_id = result.optInt("COMMENT_ID");
                        String comment_time = result.optString("COMMENT_TIME");
                        int customer_id = result.optInt("CUSTOMER_ID");
                        int comment_customer_id = result.optInt("COMMENT_CUSTOMER_ID");
                        String customer_name = result.optString("CUSTOMER_NAME");
                        String comment_content = result.optString("COMMENT_CONTENT");
                        String comment_customer_name = result.optString("COMMENT_CUSTOMER_NAME");
                        shareList.get(pos).getComment().add(new ShareEntity.ResultBean.CommentBean(comment_id,comment_time,customer_id,comment_customer_id,customer_name,comment_content,comment_customer_name));
//                        famousDocShareAdapter.notifyDataSetChanged();
//                        famousDocShareAdapter.famousDocCommentAdapter.notifyDataSetChanged();
                        famousDocShareAdapter.recyclerNotifyDataSetChanged();
//                        //清空评论文本
                        editText.setText("");
                        updateEditTextBodyVisible(View.GONE);
                    } else {
                        ToastUtil.showShort(obj.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }

    /**
     * 点赞  1 点赞  2取消点赞
     */
    private void makeGood(final int pos) {
        String share_id = shareList.get(pos).getSHARE_ID()+"";
        Map<String, String> map = new HashMap<>();
        map.put("op", "likeShare");
        map.put("share_id", share_id);
        map.put("customer_id", LoginServiceManeger.instance().getLoginUserId());
        map.put("status", "1");
        HttpRestClient.OKHttpStationCommonUrl(map, new HResultCallback<String>(this) {
            @Override
            public void onResponse(String response) {
                super.onResponse(response);
                Log.i("ttt", "onResponse: "+LoginServiceManeger.instance().getLoginUserId());
                try {
                    JSONObject obj = new JSONObject(response);
                    if ("1".equals(obj.optString("code"))) {
                        ShareEntity.ResultBean resultBean = shareList.get(pos);
                        int praiseCount = resultBean.getPRAISE_COUNT() + 1;
                        resultBean.setPRAISE_COUNT(praiseCount);
                        resultBean.setISLIKE(1);
                        famousDocShareAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtil.showShort(obj.optString("message"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);}

    public void updateEditTextBodyVisible(int visibility) {
        edittextbody.setVisibility(visibility);
        if (View.VISIBLE == visibility) {
            editText.requestFocus();
            //弹出键盘
            CommonUtils.showSoftInput(editText.getContext(), editText);
        } else if (View.GONE == visibility) {
            //隐藏键盘
            CommonUtils.hideSoftInput(editText.getContext(), editText);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
        Page=1;
        shareList.clear();
        loadDataWss();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> pullToRefreshBase) {
       Page++;
       loadDataWss();
    }

    @Override
    public void setOnListItemClick(View view, int position) {
        switch (view.getId()){
            case R.id.snsBtn:
                commentPosition=position;
                updateEditTextBodyVisible(View.VISIBLE);
                break;
            case R.id.image_zan:
               makeGood(position);
                break;
        }
    }
}
