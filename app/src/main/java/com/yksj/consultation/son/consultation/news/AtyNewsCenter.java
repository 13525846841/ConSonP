package com.yksj.consultation.son.consultation.news;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.HealthPlaceAdapter;
import com.yksj.consultation.adapter.KnowledgeFragmentPagerAdapter;
import com.yksj.consultation.adapter.NewsPlaceAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.LoadingFragmentDialog;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.bean.NewsClass;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.utils.ScreenUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.views.ColumnHorizontalScrollView;
import com.yksj.healthtalk.views.MyViewPager;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by HEKL on 16/5/9.
 * Used for
 */
public class AtyNewsCenter extends BaseFragmentActivity implements View.OnClickListener {
    /**
     * 左阴影部分
     */
    public ImageView shade_left;
    /**
     * 右阴影部分
     */
    public ImageView shade_right;
    /**
     * 屏幕宽度
     */
    private int mScreenWidth = 0;
    /**
     * Item宽度
     */
    private int mItemWidth = 0;

    LoadingFragmentDialog dialog;//网络加载提示
    KnowledgeFragmentPagerAdapter mAdapetr;
    List<NewsClass> knowledgelist = new ArrayList<NewsClass>();//知识库分类
    private NewsClass newsClass;
    RelativeLayout rl_column;
    LinearLayout mRadioGroup_content;
    LinearLayout ll_more_columns;
    private int columnSelectIndex = 0;
    private ArrayList<RootFragment> fragments = new ArrayList<RootFragment>();
    private ImageView button_more_columns;
    private MyViewPager mViewPager;
    private ColumnHorizontalScrollView mColumnHorizontalScrollView;
    public int savePosition = 0;
    private LayoutInflater mInflater;
    public static final String TYPE = "type";
    String type = "";
    private boolean isClick =false;
    View greyView;
    private LinearLayout popupWLayout;
    private List<JSONObject> mmList = new ArrayList<>();
    private List<JSONObject> mmList1 = new ArrayList<>();
    public RadioButton columnTextView;
    public FrgDynamicMessage newfragment1;
    public FrgDynamicMessage newfragment2;
    public FrgDynamicMessage newfragment3;
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_knowlegebase);
//      EventBus.getDefault().register(this);
        initView();
    }

    private void initView() {
        mInflater = LayoutInflater.from(this);
        if (getIntent().hasExtra(TYPE))
            type = getIntent().getStringExtra(TYPE);

        initTitle();

        if ("News".equals(type)) {
            titleTextV.setText("新闻中心");
        } else if ("Encyclopedia".equals(type)) {
            titleTextV.setText("六一百科");
        }

        if (getIntent().hasExtra("TITLE"))
            titleTextV.setText(getIntent().getStringExtra("TITLE"));

        titleLeftBtn.setVisibility(View.VISIBLE);
        titleLeftBtn.setOnClickListener(this);
        mScreenWidth = ScreenUtils.getScreenWidth(this);
//      mItemWidth = mScreenWidth / 4;
        mColumnHorizontalScrollView = (ColumnHorizontalScrollView) findViewById(R.id.mColumnHorizontalScrollView);
        rl_column = (RelativeLayout) findViewById(R.id.rl_column);
        mRadioGroup_content = (LinearLayout) findViewById(R.id.mRadioGroup_content);
        shade_left = (ImageView) findViewById(R.id.shade_left);
        shade_right = (ImageView) findViewById(R.id.shade_right);
        ll_more_columns = (LinearLayout) findViewById(R.id.ll_more_columns);
        button_more_columns = (ImageView) findViewById(R.id.button_more_columns);
        button_more_columns.setOnClickListener(this);
        mViewPager = (MyViewPager) findViewById(R.id.mViewPager);
        getKnowLedgeData();
    }

    @Override
    protected void onStart() {
        super.onStart();
//      mViewPager.setCurrentItem(savePosition);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//      EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
//                startActivity(new Intent(this, MainActivity.class));
//                finish();
                break;
        }
    }
    /**
     * ViewPager切换监听方法
     */
    public ViewPager.OnPageChangeListener pageListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            mViewPager.setCurrentItem(position);
            selectTab(position);
            savePosition = position;
        }
    };

    /**
     * 获取知识库分类
     */
    private String info_class_id = "101";
    public void getKnowLedgeData() {
        Map<String,String> map=new HashMap<>();
        map.put("consultation_center_id",HTalkApplication.APP_CONSULTATION_CENTERID);
        map.put("info_class_id",info_class_id);//101 热点新闻
        HttpRestClient.OKHttpNewsCenter(map, new HResultCallback<String>(this)  {
            @Override
            public void onResponse(String content) {
                super.onResponse(content);
                Log.i("gggg", "onResponse: "+content);
                try {
                    JSONObject obj = new JSONObject(content);
                    newsClass = new NewsClass();
                    if("0".equals(obj.optString("code"))){
                        JSONObject jsonobject = obj.optJSONObject("news");
                        if (jsonobject.has("children")){
                            JSONArray array = jsonobject.optJSONArray("children");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonobject1 = array.getJSONObject(i);
                                knowledgelist.add(NewsClass.parseFormat(jsonobject1));
                            }
                        }
                    }else {
                        ToastUtil.showShort(obj.optString("message"));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                initTabColumn();
                initFragment();
                if (knowledgelist.size() <= 1) {
                    findViewById(R.id.ll_title).setVisibility(View.GONE);
                    findViewById(R.id.line2).setVisibility(View.GONE);
                }
            }

        },this);
    }
    /**
     * 初始化Fragment
     */
    private void initFragment() {
        fragments.clear();//清空fragment
        int count = knowledgelist.size();
//        if (count > 0) {
//            for (int i = 0; i < count; i++) {
//                Bundle datas = new Bundle();
//                if (count > 0) {
//                    datas.putString(FrgDynamicMessage.TYPEID, knowledgelist.get(i).getINFO_CLASS_ID());
//                    datas.putString(FrgDynamicMessage.TYPENAME, knowledgelist.get(i).getINFO_CLASS_NAME());
//                    datas.putString(FrgDynamicMessage.CONTENT,knowledgelist.get(i).getContent());
//                    datas.putString(FrgDynamicMessage.TYPE,i+"");
//                }
//                newfragment = new FrgDynamicMessage();
//                newfragment.setArguments(datas);
//                fragments.add(newfragment);
//            }
//        } else if (count == 0) {
//            Bundle data = new Bundle();
//            data.putString(FrgDynamicMessage.TYPEID, getIntent().getStringExtra("INFO_CLASS_ID"));
//            data.putString(FrgDynamicMessage.TYPENAME, getIntent().getStringExtra("INFO_CLASS_NAME"));
//            data.putString(FrgDynamicMessage.CONTENT,getIntent().getStringExtra("artList"));
//            newfragment = new FrgDynamicMessage();
//            newfragment.setArguments(data);
//            fragments.add(newfragment);
//        }
            mAdapetr = new KnowledgeFragmentPagerAdapter(getSupportFragmentManager(), fragments);
            newfragment1 = new FrgDynamicMessage();
            Bundle datas1 = new Bundle();
            datas1.putString(FrgDynamicMessage.TYPEID, knowledgelist.get(0).getINFO_CLASS_ID());
            datas1.putString(FrgDynamicMessage.TYPENAME, knowledgelist.get(0).getINFO_CLASS_NAME());
            datas1.putString(FrgDynamicMessage.CONTENT,knowledgelist.get(0).getContent());
            datas1.putString(FrgDynamicMessage.TYPE,"0");
            newfragment1.setArguments(datas1);
            fragments.add(newfragment1);

            newfragment2 = new FrgDynamicMessage();
            Bundle datas2 = new Bundle();
            datas2.putString(FrgDynamicMessage.TYPEID, knowledgelist.get(1).getINFO_CLASS_ID());
            datas2.putString(FrgDynamicMessage.TYPENAME, knowledgelist.get(1).getINFO_CLASS_NAME());
            datas2.putString(FrgDynamicMessage.CONTENT,knowledgelist.get(1).getContent());
            datas2.putString(FrgDynamicMessage.TYPE,"1");
            newfragment2.setArguments(datas2);
            fragments.add(newfragment2);

            newfragment3 = new FrgDynamicMessage();
            Bundle datas3 = new Bundle();
            datas3.putString(FrgDynamicMessage.TYPEID, knowledgelist.get(2).getINFO_CLASS_ID());
            datas3.putString(FrgDynamicMessage.TYPENAME, knowledgelist.get(2).getINFO_CLASS_NAME());
            datas3.putString(FrgDynamicMessage.CONTENT,knowledgelist.get(2).getContent());
            datas3.putString(FrgDynamicMessage.TYPE,"2");
            newfragment3.setArguments(datas3);
            fragments.add(newfragment3);

            mViewPager.setOffscreenPageLimit(0);
            mViewPager.setAdapter(mAdapetr);
            mViewPager.setOnPageChangeListener(pageListener);
    }
    /**
     * 初始化知识库栏目项
     */
    private void initTabColumn() {
        mRadioGroup_content.removeAllViews();//清空title
        int count = knowledgelist.size();
        mRadioGroup_content.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        mRadioGroup_content.setDividerDrawable(getResources().getDrawable(R.drawable.common_line_vertical));
        mColumnHorizontalScrollView.setParam(this, mScreenWidth, mRadioGroup_content, shade_left, shade_right, ll_more_columns, rl_column);
        if (count>0){
            if (count<=4){
                mItemWidth=mScreenWidth/count;
            }else {
                mItemWidth=mScreenWidth/4;
            }
        }else {
            return;
        }
        for (int i = 0; i < count; i++) {
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mItemWidth, ViewGroup.LayoutParams.MATCH_PARENT);
//            final RadioButton columnTextView;
//            params.leftMargin = 10;
//            params.rightMargin = 10;
//            View view = mInflater.inflate(R.layout.colunm_radio_item, null);//左侧item
            if (i==2){
                columnTextView = (RadioButton) mInflater.inflate(R.layout.colunm_radio_item, null);//左侧item
            }else{
                columnTextView = (RadioButton) mInflater.inflate(R.layout.colunm_radio_item_news, null);//左侧item
            }
           //RadioButton columnTextView = (RadioButton) mInflater.inflate(R.layout.colunm_radio_item, null);//左侧item
//			View view =  View.inflate(this,R.layout.colunm_radio_item, null);
//          TextView columnTextView = new TextView(this);
//          RadioButton columnTextView = (RadioButton) view.findViewById(R.id.rb_text);
//          columnTextView.setTextAppearance(this, R.style.top_category_scroll_view_item_text);
//			localTextView.setBackground(getResources().getDrawable(R.drawable.top_category_scroll_text_view_bg));
//          columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
//          columnTextView.setBackgroundResource(R.drawable.radio_buttong_bg);
//          columnTextView.setBackgroundResource(R.drawable.rb_top_text_bg);
//          columnTextView.setBackgroundResource(R.drawable.tab_bg);
            columnTextView.setGravity(Gravity.CENTER);
//          columnTextView.setPadding(10, 10, 10, 10);
            columnTextView.setId(i);
            columnTextView.setTextSize(16);
            columnTextView.setText(knowledgelist.get(i).getINFO_CLASS_NAME());
            columnTextView.setTextColor(getResources().getColorStateList(R.color.top_category_scroll_text_color_day));
            if (columnSelectIndex == i) {
                columnTextView.setSelected(true);
            }

            columnTextView.setOnClickListener(new View.OnClickListener() {

                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
                        RadioButton localView = (RadioButton) mRadioGroup_content.getChildAt(i);
                        if (localView != v)
//                          localView.setSelected(false);
                            localView.setChecked(false);
                        else {
//                          localView.setSelected(true);
                            localView.setChecked(true);
                            mViewPager.setCurrentItem(i);
                            savePosition=i;
                        }
                        if(v== mRadioGroup_content.getChildAt(0)){
                            String HotType = "10101";
                            showPopupWindow(HotType);
                        }else  if (v==mRadioGroup_content.getChildAt(1)){
                            String healthType = "10102";
                            showPopupWindow1(healthType);
                        }
                    }
                }
            });
            mRadioGroup_content.addView(columnTextView, i, params);
            if (mRadioGroup_content.getChildCount() > 0) {
                RadioButton checkView = (RadioButton) mRadioGroup_content.getChildAt(0);
                checkView.setChecked(true);
            }
            if (mRadioGroup_content.getChildCount() > 3) {
                findViewById(R.id.ll_line).setVisibility(View.GONE);
            }
//            mRadioGroup_content.addView(view, i, params);
        }
    }
    //热点新闻的弹出框
    private PopupWindow pw;
    private ListView mlv_news;
    private ListView mlv_news1;
    private NewsPlaceAdapter adapter;
    private HealthPlaceAdapter adapter1;
    private String class_id = "101";

    private void showPopupWindow(String infoClassId) {
        Map<String,String> map=new HashMap<>();
        map.put("consultation_center_id",HTalkApplication.APP_CONSULTATION_CENTERID);
        map.put("info_class_id",infoClassId);//10102 健康咨询//10101热点新闻
        HttpRestClient.OKHttpNewsCenter(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e){
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    mmList = new ArrayList<>();
                    JSONObject jsonobject = obj.optJSONObject("news");
                    JSONArray array = jsonobject.optJSONArray("children");
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonobject1 = array.getJSONObject(i);
                        mmList.add(jsonobject1);
                    }
                    adapter.onBoundData(mmList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
        mlv_news= (ListView) View.inflate(this,R.layout.list_news_popup,null);
        if (pw == null) {
            pw = new PopupWindow(this);
            pw.setWidth(ViewPager.LayoutParams.MATCH_PARENT);
            pw.setHeight(ViewPager.LayoutParams.WRAP_CONTENT);
            pw.setFocusable(true);
            pw.setOutsideTouchable(true);
            pw.setBackgroundDrawable(new BitmapDrawable());
        }
        adapter = new NewsPlaceAdapter(mmList,this);
        pw.setContentView(mlv_news);
        mlv_news.setAdapter(adapter);

//      WindowManager.LayoutParams params=this.getWindow().getAttributes();
//      params.alpha=0.8f;
//      this.getWindow().setAttributes(params);
        if(!pw.isShowing());
        pw.showAsDropDown(mRadioGroup_content,0,0);
//        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                lp.alpha = 1f;
//                getWindow().setAttributes(lp);
//            }
//        });
        mlv_news.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (pw.isShowing()){
                pw.dismiss();
            }
            RadioButton checkView = (RadioButton) mRadioGroup_content.getChildAt(savePosition);
            checkView.setText(mmList.get(position).optString("INFO_CLASS_NAME"));
                class_id =  mmList.get(position).optString("INFO_CLASS_ID");
                info_class_id = mmList.get(position).optString("INFO_CLASS_ID");
                newfragment1.setClassId(info_class_id,savePosition+"");
            }
        });
    }


    private void showPopupWindow1(String infoClassId) {
        Map<String,String> map=new HashMap<>();
        map.put("consultation_center_id",HTalkApplication.APP_CONSULTATION_CENTERID);
        map.put("info_class_id",infoClassId);//10102 健康咨询//10101热点新闻
        HttpRestClient.OKHttpNewsCenter(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e){
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    mmList1 = new ArrayList<>();

                    JSONObject jsonobject = obj.optJSONObject("news");
                    JSONArray array = jsonobject.optJSONArray("children");

                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonobject1 = array.getJSONObject(i);
                        mmList1.add(jsonobject1);
                    }
                    adapter1.onBoundData(mmList1);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
        mlv_news1= (ListView) View.inflate(this,R.layout.list_news_popup,null);
        if (pw == null) {
            pw = new PopupWindow(this);
            pw.setWidth(ViewPager.LayoutParams.MATCH_PARENT);
            pw.setHeight(ViewPager.LayoutParams.WRAP_CONTENT);
            pw.setFocusable(true);
            pw.setOutsideTouchable(true);
            pw.setBackgroundDrawable(new BitmapDrawable());
        }
        adapter1 = new HealthPlaceAdapter(mmList1,this);
        pw.setContentView(mlv_news1);
        mlv_news1.setAdapter(adapter1);

//      WindowManager.LayoutParams params=this.getWindow().getAttributes();
//      params.alpha=0.8f;
//      this.getWindow().setAttributes(params);
        if(!pw.isShowing());
        pw.showAsDropDown(mRadioGroup_content,0,0);
//        pw.setOnDismissListener(new PopupWindow.OnDismissListener() {
//            @Override
//            public void onDismiss() {
//                WindowManager.LayoutParams lp = getWindow().getAttributes();
//                lp.alpha = 1f;
//                getWindow().setAttributes(lp);
//            }
//        });
        mlv_news1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (pw.isShowing()){
                pw.dismiss();
            }
            RadioButton checkView = (RadioButton) mRadioGroup_content.getChildAt(savePosition);
            checkView.setText(mmList1.get(position).optString("INFO_CLASS_NAME"));
                class_id =  mmList1.get(position).optString("INFO_CLASS_ID");
                info_class_id = mmList1.get(position).optString("INFO_CLASS_ID");
                newfragment2.setClassId(info_class_id,savePosition+"");
            }
        });
    }
    /**
     * 选择的Column里面的Tab
     */
    private void selectTab(int tab_postion) {
        columnSelectIndex = tab_postion;
        for (int i = 0; i < mRadioGroup_content.getChildCount(); i++) {
            View checkView = mRadioGroup_content.getChildAt(tab_postion);
            int k = checkView.getMeasuredWidth();
            int l = checkView.getLeft();
            int i2 = l + k / 2 - mScreenWidth / 2;
            //rg_nav_content.getParent()).smoothScrollTo(i2, 0);
            mColumnHorizontalScrollView.smoothScrollTo(i2, 0);
            //mColumnHorizontalScrollView.smoothScrollTo((position - 2) *
            //mItemWidth , 0);
        }

        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
            RadioButton checkView = (RadioButton) mRadioGroup_content.getChildAt(j);
            checkView.setSelected(true);
        }

        //判断是否选中
        for (int j = 0; j < mRadioGroup_content.getChildCount(); j++) {
//          RadioButton checkView = (RadioButton) mRadioGroup_content.getChildAt(j).findViewById(R.id.rb_text);
            RadioButton checkView = (RadioButton) mRadioGroup_content.getChildAt(j);
            boolean ischeck;
            if (j == tab_postion) {
                ischeck = true;
                checkView.setTextColor(getResources().getColor(R.color.color_blue));
            } else {
                ischeck = false;
                checkView.setTextColor(getResources().getColor(R.color.gray_text));
            }
//          checkView.setSelected(ischeck);
            checkView.setChecked(ischeck);
        }
    }
}

//        HttpRestClient.OKHttpNewsCenter( "6","101",new AsyncHttpResponseHandler(this){
//            @Override
//            public void onSuccess(String content) {
//                super.onSuccess(content);
//                try {
//                    JSONObject obj = new JSONObject(content);
//                    newsClass = new NewsClass();
//                    JSONObject jsonobject = obj.optJSONObject("news");
//                    JSONArray array = jsonobject.optJSONArray("children");
//
//                    for (int i = 0; i < array.length(); i++) {
//                        JSONObject jsonobject1 = array.getJSONObject(i);
//                        knowledgelist.add(NewsClass.parseFormat(jsonobject1));
//
//                }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//                initTabColumn();
//                initFragment();
//                if (knowledgelist.size() <= 1) {
//                    findViewById(R.id.ll_title).setVisibility(View.GONE);
//                    findViewById(R.id.line2).setVisibility(View.GONE);
//                }
//            }
//
//            @Override
//            public void onFailure(Throwable error, String content) {
//                super.onFailure(error, content);
//
//            }
//        });


//        final List<BasicNameValuePair> list = new ArrayList<>();
//        list.add(new BasicNameValuePair("TYPE", "queryTitle"));
//        list.add(new BasicNameValuePair("CATEGORY_NAME", type));
//        HttpRestClient.addHttpHeader("client_type", HTalkApplication.CLIENT_TYPE);
//        HttpRestClient.OKHttpGetTitle(list, new OkHttpClientManager.ResultCallback<JSONObject>() {
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
//                        knowledgelist.clear();//数据清空
//                        JSONArray array = response.getJSONArray("result");
//                        int count = array.length();
//                        if (count > 0) {
//                            for (int i = 0; i < count; i++) {
//                                knowledgelist.add(NewsClass.parseFormat((JSONObject) array.get(i)));
//                            }
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
////                  KBAddChildAty.setKnowledgebase(knowledgelist);
//                    initTabColumn();
//                    initFragment();
//                    if (knowledgelist.size() <= 1) {
//                        findViewById(R.id.ll_title).setVisibility(View.GONE);
//                        findViewById(R.id.line2).setVisibility(View.GONE);
//                    }
//                } else {
//                    ToastUtil.showShort(response.optString("message"));
//                }
//            }
//        }, this);