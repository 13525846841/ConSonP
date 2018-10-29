package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.ShopGoodsAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.home.PatientHomeActivity;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/***
 * 商城界面
 */
public class ShopActivity extends BaseFragmentActivity implements View.OnClickListener, TextView.OnEditorActionListener, AdapterView.OnItemClickListener {

    private TextView[] listMenuTextViews;
    private LayoutInflater linflater;
    private ViewPager viewPager;
    private CategorizeProductAdapter categorizeProductAdapter;
    private String text = "";
    private List<JSONObject> list = null;
    private List<JSONObject> mList = null;//搜索结果
    /**
     * 默认的ViewPager选中的项
     */
    private int currentItem = 0;

    private ListView mSearchList;
    private ShopGoodsAdapter adapter;

    private PullToRefreshListView mRefreshableView;
    private boolean isSearch = false;
    private LinearLayout llShopMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);
        initView();
    }

    private void initView() {
        titleLeftBtn = (ImageView) findViewById(R.id.title_back);
        editSearch= (EditText) findViewById(R.id.seach_text);
        editSearch.setHint("输入搜索商品名称");
        editSearch.setOnEditorActionListener(this);

        titleLeftBtn.setOnClickListener(this);

        mRefreshableView = (PullToRefreshListView)findViewById(R.id.search_goods);
        mSearchList = mRefreshableView.getRefreshableView();
        adapter = new ShopGoodsAdapter(this);
        mSearchList.setAdapter(adapter);
        mSearchList.setOnItemClickListener(this);
        llShopMain = (LinearLayout) findViewById(R.id.shop_main);

        linflater = LayoutInflater.from(this);
        findViewById(R.id.main).setOnClickListener(this);
        findViewById(R.id.order).setOnClickListener(this);

        viewPager = (ViewPager) findViewById(R.id.goods);

        initClassData();

    }

    private void initClassData() {
        Map<String,String> map=new HashMap<>();
        map.put("Type", "findGoodsClassByClassId");
        HttpRestClient.OKHttGoodsServlet(map, new HResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    list = new ArrayList<>();
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        if (!HStringUtil.isEmpty(obj.optString("server_params"))){
                            JSONObject object = obj.optJSONObject("server_params");
                            JSONArray array = object.optJSONArray("clazz");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonobject = array.getJSONObject(i);
                                list.add(jsonobject);
                            }
                            initTools();
                            initViewPager();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }

    private void initViewPager() {
        // 由于使用了支持包所以最终必须确保所有的导入包都是来自支持包
        categorizeProductAdapter = new CategorizeProductAdapter(getSupportFragmentManager(), list);
        viewPager.setAdapter(categorizeProductAdapter);
        // 为ViewPager设置页面变化的监控
        viewPager.setOnPageChangeListener(onPageChangeListener);

    }
    ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            if (viewPager.getCurrentItem() != position) {
                viewPager.setCurrentItem(position);
            }
            // 通过ViewPager监听点击字体颜色和背景的改变
            if (currentItem != position) {
                changeTextColor(position);
            }
            currentItem = position;
        }

        @Override
        public void onPageScrollStateChanged(int state) {


        }
    };
    /**
     * 初始化左边目录
     */
    private void initTools() {
        listMenuTextViews = new TextView[list.size()];
        LinearLayout toolsLayout = (LinearLayout)findViewById(R.id.tools);
        for (int i = 0; i < list.size(); i++) {
            View view = linflater.inflate(R.layout.itemview_categorize_listmenus, null);
            // 给每个View设定唯一标识
            view.setId(i);
//            // 给每个view添加点击监控事件
            view.setOnClickListener(ListItemMenusClickListener);
//            // 获取到左侧栏的的TextView的组件
            TextView textView = (TextView) view.findViewById(R.id.textView);
            textView.setText(list.get(i).optString("CLASS_NAME"));
            toolsLayout.addView(view);
//            // 传入的是地址不是复制的值
            listMenuTextViews[i] = textView;
        }
        changeTextColor(0);
    }


    private void changeTextColor(int position) {
        for (int i = 0; i <list.size(); i++) {
            if (position != i) {
                listMenuTextViews[i].setBackgroundColor(0x00000000);
                listMenuTextViews[i].setTextColor(0xFF000000);
            }
        }
        listMenuTextViews[position].setBackgroundColor(0xFFFFFFFF);
        listMenuTextViews[position].setTextColor(Color.parseColor("#41b8b6"));
    }

    View.OnClickListener ListItemMenusClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            viewPager.setCurrentItem(v.getId());
        }
    };
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.main:
                Intent intent = new Intent(ShopActivity.this,PatientHomeActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.order:
                Intent intent1 = new Intent(this,ShopOrderActivity.class);
                startActivity(intent1);
                break;

        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            text = editSearch.getText().toString().trim();
            if (text != null && text.length() != 0) {
                editSearch.setText("");
                Intent intent = new Intent(ShopActivity.this, SearchShopActivity.class);
                intent.putExtra("result", text);
                startActivity(intent);
                handled = true;
            }
        } else {
            ToastUtil.showShort("请输入内容");
        }
        return handled;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent  intent  = new Intent(this,ProductDetailAty.class);
        intent.putExtra("good_id",mList.get(position-1).optString("GOODS_ID"));
        startActivity(intent);
    }
}
