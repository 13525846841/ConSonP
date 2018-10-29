package com.yksj.consultation.son.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.memberAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.listener.DialogOnClickListener;
import com.yksj.healthtalk.entity.DocPlanEntity;
import com.yksj.healthtalk.entity.DocPlanMemberEntity;
import com.yksj.healthtalk.entity.TextEntity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.net.socket.SmartFoxClient;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ToastUtil;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 成员列表
 */
public class MemberActivity extends BaseFragmentActivity implements View.OnClickListener, AdapterView.OnItemClickListener {
    private PullToRefreshListView mPullRefreshListView;
    private ListView mListView;
    private memberAdapter adapter;
    private MyAlertDialog dialog;
    private DocPlanMemberEntity docPlanMemberEntity;
    private List<DocPlanMemberEntity> data;
    private String children_id;//宝贝ID
    private List<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initView() {
        initTitle();
        Intent intent = getIntent();
        children_id = intent.getStringExtra("CHILDREN_ID");

        titleTextV.setText("成员");
        titleRightBtn2.setVisibility(View.VISIBLE);
        titleRightBtn2.setText("添加");
        titleLeftBtn.setOnClickListener(this);
        titleRightBtn2.setOnClickListener(this);
        mPullRefreshListView=(PullToRefreshListView) findViewById(R.id.member_list);
        mListView=mPullRefreshListView.getRefreshableView();
        adapter = new memberAdapter(this);
        mListView.setAdapter(adapter);
        mListView.setOnItemClickListener(this);
        data = new ArrayList<DocPlanMemberEntity>();
    }

    /**
     * 加载数据
     */
    private String CHILDREN_IDchildren_id = "100003";

    private void initData() {
        if (data!=null){
            data.clear();
        }
        Map<String,String> map=new HashMap<>();
        map.put("children_id",children_id);
        HttpRestClient.OKHttpGetMemberList(map, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {

            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    JSONArray array = obj.getJSONArray("plans");
                    JSONObject item;
                    for (int i = 0; i < array.length(); i++) {
                        item = array.getJSONObject(i);
                        docPlanMemberEntity = new DocPlanMemberEntity();
                        docPlanMemberEntity.setName(item.optString("CUSTOMER_NICKNAME"));
                        docPlanMemberEntity.setImage(item.optString("CLIENT_ICON_BACKGROUND"));
                        docPlanMemberEntity.setSex(item.optString("CUSTOMER_SEX"));
                        docPlanMemberEntity.setCREATOR_ID(item.optString("CREATOR_ID"));
                        docPlanMemberEntity.setCustomer_id(item.optString("CUSTOMER_ID"));
                        docPlanMemberEntity.setCustomer_remark(item.optString("CUSTOMER_REMARK"));
                        data.add(docPlanMemberEntity);
                        list.add(item.optString("CUSTOMER_ID"));
                    }
                    adapter.onBoundData(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.title_right2:
                intent = new Intent(this,AddMemberActivity.class);
                intent.putExtra("CHILDREN_ID",children_id);
                intent.putExtra("customer_id",list.toString());
                startActivity(intent);
                break;
        }
    }
    private String member_id;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        member_id = adapter.datas.get(position-1).getCustomer_id();
        initMDDialog(position,member_id);
        dialog.show();
    }

    /**
     * 弹出dialog
     */
    private void initMDDialog(int position, final String member_id) {
        final int itemPosition = position;
        dialog = new MyAlertDialog.Builder(MemberActivity.this)
                .setHeight(0.21f)  //屏幕高度*0.21
                .setWidth(0.7f)  //屏幕宽度*0.7
                .setTitleText("添加备注名称")
                .setOnclickListener(new DialogOnClickListener() {
                    @Override
                    public void clickButton(View view) {
                        modificationName(itemPosition,member_id);
                    }
                })
                .build();
    }

    /**
     * 添加备注姓名
     */
    private String name;
    private String customer_id = LoginServiceManeger.instance().getLoginUserId();;//3176
    private void modificationName(final int itemPosition,String member_id) {
        name = dialog.edittext().toString();
        if (HStringUtil.isEmpty(name)){
            ToastUtil.showShort("请输入备注姓名");
            return;
        }
        HttpRestClient.OKHttpUpdateMemberRemark(children_id,member_id,name, new AsyncHttpResponseHandler(this){
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        dialog.dismiss();
                        ToastUtil.showShort(obj.optString("message"));
                        adapter.datas.get(itemPosition-1).setCustomer_remark(name);
                        adapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable error, String content) {
                super.onFailure(error, content);
            }
        });
    }
}
