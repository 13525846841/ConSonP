package com.yksj.consultation.son.consultation.main;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.ShopGoodsSearchAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
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

public class SearchShopActivity extends BaseFragmentActivity implements TextView.OnEditorActionListener, View.OnClickListener, AdapterView.OnItemClickListener, CompoundButton.OnCheckedChangeListener,PullToRefreshBase.OnRefreshListener2<ListView> {

    private CategorizeProductAdapter categorizeProductAdapter;
    private String text = "";

    private ListView mSearchList;
    private ShopGoodsSearchAdapter adapter;

    private PullToRefreshListView mRefreshableView;
    private boolean isSearch = false;
    private List<JSONObject> mList = null;//搜索结果

    private CheckBox allSelect;
    private CheckBox hotSelect;
    private CheckBox priceSelect;

    private String comprehensive = "1";//综合排序
    private String price = "0";//价格
    private String saleCount = "0";//销量
    public EditText editSearchGoods;
    private int conPageSize = 1;//当前的页数
    private View mEmptyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_shop);
        initView();
    }

    private void initView() {
        titleLeftBtn = (ImageView) findViewById(R.id.title_back);
        editSearchGoods = (EditText) findViewById(R.id.seach_text);

        editSearchGoods.setHint("输入搜索商品名称");
        editSearchGoods.setOnEditorActionListener(this);
        mEmptyView = findViewById(R.id.empty_view_famous);
        titleLeftBtn.setOnClickListener(this);

        mRefreshableView = (PullToRefreshListView)findViewById(R.id.search_goods);
        mSearchList = mRefreshableView.getRefreshableView();
        mRefreshableView.setOnRefreshListener(this);
        adapter = new ShopGoodsSearchAdapter(this);
        mSearchList.setAdapter(adapter);
        mSearchList.setOnItemClickListener(this);

        allSelect = (CheckBox) findViewById(R.id.room_region);
        hotSelect = (CheckBox) findViewById(R.id.intelligent_sorting);
        priceSelect = (CheckBox) findViewById(R.id.intelligent_sorting2);

        allSelect.setOnCheckedChangeListener(this);
        hotSelect.setOnCheckedChangeListener(this);
        priceSelect.setOnCheckedChangeListener(this);

        initRadioGroupView();
        initSearchView();


        findViewById(R.id.main).setOnClickListener(this);
        findViewById(R.id.order).setOnClickListener(this);
        if (getIntent().hasExtra("result")) {
            text=getIntent().getStringExtra("result");
        }
        initData();
    }
    /**
     * 加载搜索到的数据
     */
    private void initData() {

        isSearch = true;
        Map<String,String> map=new HashMap<>();
        map.put("Type", "findGoodsByClassId");
        map.put("pageNum",conPageSize + "" );
        map.put("pageSize", "10");
        map.put("search", text);
        map.put("comprehensive", comprehensive);
        map.put("price", price);
        map.put("saleCount", saleCount);
        HttpRestClient.OKHttGoodsServlet(map, new HResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    mList = new ArrayList<>();
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        if (!HStringUtil.isEmpty(obj.optString("server_params"))){
                            JSONObject object = obj.optJSONObject("server_params");
                            JSONArray array = object.optJSONArray("result");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonobject = array.getJSONObject(i);
                                mList.add(jsonobject);
                            }
                            if (conPageSize == 1) {//第一次加载
                                if (mList.size() == 0) {
                                    adapter.removeAll();
                                    adapter.addAll(mList);
                                    mEmptyView.setVisibility(View.VISIBLE);
                                    mRefreshableView.setVisibility(View.GONE);
                                } else {
                                    mEmptyView.setVisibility(View.GONE);
                                    mRefreshableView.setVisibility(View.VISIBLE);
                                    adapter.removeAll();
                                    adapter.addAll(mList);
                                }
                            } else {
                                if (mList.size() != 0) {//加载出了数据
                                    adapter.addAll(mList);
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
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            text = editSearchGoods.getText().toString().trim();
            if (text != null && text.length() != 0) {

                initData();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editSearchGoods.getWindowToken(), 0);//关闭软键盘
                handled = true;
            }
        } else {
            ToastUtil.showShort("请输入内容");
        }
        return handled;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.main:
                onBackPressed();
                break;
            case R.id.order:
                Intent intent1 = new Intent(this,ShopOrderActivity.class);
                startActivity(intent1);
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent  = new Intent(this,ProductDetailAty.class);
        intent.putExtra("good_id",mList.get(position-1).optString("GOODS_ID"));
        startActivity(intent);
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.room_region:
                if (isChecked){
                    comprehensive = "1";
                }else {
                    comprehensive = "1";
                }
                initData();
                break;
            case R.id.intelligent_sorting:
                if (isChecked){
                    price = "1";
                }else {
                    price = "0";
                }
                initData();
                break;
            case R.id.intelligent_sorting2:
                if (isChecked){
                    saleCount = "1";
                }else {
                    saleCount = "0";
                }
                initData();
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        conPageSize = 1;
        initData();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        conPageSize++;
        initData();

    }
}
