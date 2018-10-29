package com.yksj.consultation.son.consultation;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.CharacterStyle;
import android.text.style.ForegroundColorSpan;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.PCViewPagerAdapter;
import com.yksj.consultation.adapter.PconsultmainGVAdapter;
import com.yksj.consultation.adapter.SearchRoomAdapter;
import com.yksj.consultation.adapter.ZhuanjiaLvAdapper;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.Model;
import com.yksj.consultation.son.consultation.member.FlowMassageActivity;
import com.yksj.consultation.son.doctor.SelectExpertMainUI;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.ObjectHttpResponseHandler;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.LogUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.handmark.pulltorefresh.library.PullToRefreshBase;
import org.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import org.handmark.pulltorefresh.library.PullToRefreshListView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 新六一健康科室列表
 *
 * @author zheng
 * chen 修改于2016年十一月
 */
public class PConsultMainActivity extends BaseFragmentActivity implements OnClickListener, OnItemClickListener, DoubleBtnFragmentDialog.OnFristClickListener, DoubleBtnFragmentDialog.OnSecondClickListener, OnRefreshListener<ListView>, DoubleBtnFragmentDialog.OnOfficFullListener, TextView.OnEditorActionListener {

    private PullToRefreshListView mPullRefreshListView;
    private PConsultMainAdapter mAdapter;
    private Map<String, String> mConsultMap;
    private List<Map<String, String>> mConsultList;
    private ListView mListView;
    private View mNullView;
    private EditText editSearch;
    private String officeName = "";
    private String officeNameNum;
    private String selectedOffice, officeCode;
    private String customerid = "";
    private int index = 0, start = 0, end = 0, color;
    private SpannableStringBuilder spBuilder;
    private ListView mLv_Remen;
    private GridView mGv_Renmen;
    private ZhuanjiaLvAdapper lvAdapper;
    private List<JSONObject> mList = new ArrayList<>();
    //上面推荐的科室记录
    private ViewPager mPager;
    private LinearLayout mLlDot;
    private List<View> mPagerList;
    private List<Model> mDatas;
    private LayoutInflater inflater;
    private String diseaseName;//疾病名称
    /**
     * 总的页数
     */
    private int pageCount;
    /**
     * 每一页显示的个数
     */
    private int pageSize = 6;
    /**
     * 当前显示的是第几页
     */
    private int curIndex = 0;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
//    private GoogleApiClient client;
    private ListView mSearchList;
    private LinearLayout ll_main_search;
    private SearchRoomAdapter mmAdapter;
    private List<JSONObject> list = null;//搜索list
    private List<JSONObject> list1 = null;//搜索list,疾病
    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.consultation_center_home_activity_layout);
        if (!LoginServiceManeger.instance().isVisitor) {//游客判断
            customerid = LoginServiceManeger.instance().getLoginEntity().getId();
            LogUtil.d("TAG", "科室列表用户ID" + LoginServiceManeger.instance().getLoginEntity().getId());
        }
        initView();
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        // client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void initView() {
        initTitle();
        titleTextV.setText("找专家");
        titleLeftBtn.setOnClickListener(this);
        titleRightBtn.setVisibility(View.VISIBLE);
        editSearch = (EditText) findViewById(R.id.edit_search_topp);
        ll_main_search = (LinearLayout) findViewById(R.id.ll_main_content);
        mSearchList = (ListView) findViewById(R.id.search_room);
        mmAdapter = new SearchRoomAdapter(this);
        mSearchList.setAdapter(mmAdapter);
        mSearchList.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                selectedOffice = mmAdapter.datas.get(position).optString("OFFICE_NAME");//科室名称
                officeCode = mmAdapter.datas.get(position).optString("OFFICE_DESC");//科室编码
                diseaseName =  mmAdapter.datas.get(position).optString("WORDS");//疾病名称
                if (!HStringUtil.isEmpty(diseaseName)){
                    initDisease();
                }else if (!HStringUtil.isEmpty(officeCode)){
                    initNum();
                }
            }
        });
        editSearch.setHint("搜索科室和热门疾病");
        mPager = (ViewPager) findViewById(R.id.viewpager);
        mLlDot = (LinearLayout) findViewById(R.id.ll_dot);
        editSearch.setOnEditorActionListener(this);
//        editSearch.addTextChangedListener(new TextWatcher() {//搜索框字数变化监听
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                officeName = editSearch.getText().toString().trim();
//                initseatch(editSearch);
//            }
//        });


//        mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.consultation_pulltorefresh_listview);
//        mListView = mPullRefreshListView.getRefreshableView();
//        mAdapter = new PConsultMainAdapter(this);
//        mNullView = findViewById(R.id.nullview);
//        mListView.setEmptyView(mNullView);
//        mListView.setAdapter(mAdapter);
//        mListView.setOnItemClickListener(this);
//        mPullRefreshListView.setOnRefreshListener(this);
//        initData();
        mLv_Remen = (ListView) findViewById(R.id.remen);
        lvAdapper = new ZhuanjiaLvAdapper(mList,this);
        mLv_Remen.setAdapter(lvAdapper);
        mLv_Remen.setOnItemClickListener(this);

//        GridView gridView = (GridView) inflater.inflate(R.layout.pconsultmain_gridview, mPager, false);
////      gridAdapter = new PconsultmainGVAdapter(this);
//        gridAdapter = new PconsultmainGVAdapter(this,mDatas, 0, pageSize);
//        gridView.setAdapter(gridAdapter);
        /**
         * 按科室找专家的能滑动的gridview的方法
         */
        initDatas();
    }

    /**
     * 按科室找专家的能滑动的gridview的方法
     */
    public PconsultmainGVAdapter gridAdapter;//按科室找专家的适配器
    private void quickFind() {
      //初始化数据源
        inflater = LayoutInflater.from(this);
        //总的页数=总数/每页数量，并取整
        pageCount = (int) Math.ceil(mDatas.size() * 1.0 / pageSize);
        mPagerList = new ArrayList<View>();
        for (int i = 0; i < pageCount; i++) {
            //每个页面都是inflate出一个新实例
            GridView gridView = (GridView) inflater.inflate(R.layout.pconsultmain_gridview, mPager, false);
//            if (i>0){
//                gridAdapter.onBoundData(mDatas,i);
//            }else {
//                gridAdapter = new PconsultmainGVAdapter(PConsultMainActivity.this, mDatas, i, pageSize);
//            }
            gridAdapter = new PconsultmainGVAdapter(PConsultMainActivity.this, mDatas, i, pageSize);
            gridView.setAdapter(gridAdapter);
            mPagerList.add(gridView);

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //
                    int pos = position + curIndex * pageSize;
                    mDatas.get(position).getOffice_id();
//                    Intent intent = new Intent(PConsultMainActivity.this,PCExpertMainUIActivity.class);
//                    startActivity(intent);
                    selectedOffice = mDatas.get(pos).getName();//科室名称
                    officeCode = mDatas.get(pos).getOffice_id();//科室编码
                    initNum();

                }
            });
        }
        //设置适配器
        mPager.setAdapter(new PCViewPagerAdapter(mPagerList));
        //设置圆点
        setOvalLayout();
    }
    private Model model;

    private void initDatas() {
        mDatas = new ArrayList<Model>();
        HttpRestClient.OKHttpFindExpert(new AsyncHttpResponseHandler(this){
                @Override
                public void onSuccess(String content) {
                    super.onSuccess(content);
                    try {
                    JSONObject obj = new JSONObject(content);
                    JSONObject jsonobject  = obj.optJSONObject("info");
                    if (jsonobject.has("office")) {
                        JSONArray array = jsonobject.getJSONArray("office");

                        JSONObject item;
                        for (int i = 0; i < array.length(); i++) {
                            item = array.getJSONObject(i);
                            model = new Model();
                            model.setName(item.optString("OFFICE_NAME"));
                            model.setOffice_id(item.optString("OFFICE_ID"));
                            model.setIconRes(item.optString("ICON_2X"));
                            mDatas.add(model);
                        }
//                        gridAdapter.onBoundData(mDatas);
                        quickFind();
                    }
                        if (jsonobject.has("disease")) {
                            JSONArray array = jsonobject.getJSONArray("disease");
                            mList = new ArrayList<JSONObject>();
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonobject1 = array.getJSONObject(i);
                                mList.add(jsonobject1);
                            }
                            lvAdapper.onBoundData(mList);
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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        diseaseName = mList.get(position).optString("WORDS");
        initDisease();
        //待定
//        Intent intent = new Intent(this, PCExpertMainUIActivity.class);
//        intent.putExtra("CHILDREN_ID",lvAdapper.getOffice_id(position).toString());
//        ToastUtil.showShort(lvAdapper.getOffice_id(position));
//        startActivity(intent);
    }
    /**
     * 设置节点
     */
    private void setOvalLayout() {
//        for (int i = 0; i < pageCount; i++) {
            mLlDot.addView(inflater.inflate(R.layout.dot, null));
//        }
//        // 默认显示第一页
//        mLlDot.getChildAt(0).findViewById(R.id.v_dot)
//                .setBackgroundResource(R.drawable.dot_selected);
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageSelected(int position) {
                // 取消圆点选中
//                mLlDot.getChildAt(curIndex)
//                        .findViewById(R.id.v_dot)
//                        .setBackgroundResource(R.drawable.dot_normal);
//                // 圆点选中
//                mLlDot.getChildAt(position)
//                        .findViewById(R.id.v_dot)
//                        .setBackgroundResource(R.drawable.dot_selected);
                curIndex = position;
            }

            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            public void onPageScrollStateChanged(int arg0) {
            }
        });
    }

    /**
     * 模糊搜索查找专家
     * @param v
     * @param actionId
     * @param event
     * @return
     */
    private int goalType=0;//0表示搜索专家,1表示搜索医生  2搜索专家重选
    private static final String keyName=PConsultMainActivity.class.getName();
    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String text = editSearch.getText().toString().trim();
            if (text != null && text.length() != 0) {;
                editSearch.setText("");
                initSearchData(text);
                handled = true;
            }
        } else {
            ToastUtil.showShort("请输入内容");
        }
        return handled;
//        boolean handled = false;
//        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//            String text = editSearch.getText().toString().trim();
//            if (text != null && text.length() != 0) {
//                SharePreUtils.saveSearchHistory(PConsultMainActivity.this, keyName, text);
//                editSearch.setText("");
//                Intent intent = new Intent(PConsultMainActivity.this, SearchExpertResultActivity.class);
//                intent.putExtra("result", text);
//                intent.putExtra("type", goalType);
//                if (getIntent().hasExtra("CLINIC")){
//                    intent.putExtra("CLINIC", "CLINIC");
//                }
////                if (goalType==2)
////                    intent.putExtra("consultId",consultId);
//                intent.putExtra("OFFICECODE", officeCode);
//                intent.putExtra("OFFICENAME", officeName);
//               startActivity(intent);
//                handled = true;
//            } else {
//                ToastUtil.showShort(getString(R.string.inputThemeName));
//            }
//        }
//        return handled;
////        String text = editSearch.getText().toString().trim();
//        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
//            Intent intent = new Intent(this, PCExpertMainUIActivity.class);
//            intent.putExtra("text",text);
//            ToastUtil.showShort(text);
//            startActivity(intent);
//        }
//
//        return false;
    }

    /**
     * 加载搜索数据
     * @param text
     */
    private void initSearchData(String text) {
        mSearchList.setVisibility(View.VISIBLE);
        ll_main_search.setVisibility(View.INVISIBLE);

        Map<String, String> map = new HashMap<>();
        map.put("search", text);
        HttpRestClient.OKHttpSearchRoom(map, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        list = new ArrayList<>();
                        JSONObject jsonobject = obj.optJSONObject("result");

                        JSONArray array = jsonobject.optJSONArray("office");
                        if (array.length()!=0){
                            for (int i=0;i<array.length();i++){
                                JSONObject object = array.getJSONObject(i);
                                list.add(object);
                            }
                            mmAdapter.onBoundData(list);
                        }
                        JSONArray array1 = jsonobject.optJSONArray("words");
                        list1 = new ArrayList<>();
                        if (array1.length()!=0){
                            for (int i=0;i<array1.length();i++){
                                JSONObject object1 = array1.getJSONObject(i);
                                list1.add(object1);
                            }
                            mmAdapter.onBoundData(list1);
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }

    private void initseatch(final EditText Search) {//科室搜索请求
        Search.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    officeName = Search.getText().toString().trim();
//                    if (officeName != null) {
                    if (!"".equals(officeName)) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(Search.getWindowToken(), 0);//关闭软键盘
                        initData();
                    } else {
                        initData();
                        ToastUtil.showShort("输入科室名称");
                    }
                    handled = true;
                }
                return handled;
            }
        });

//        RequestParams params = new RequestParams();
//        params.put("Type", "queryTiyanCaidan");
//        params.put("MERCHANT_ID", "987");
//        params.put("CUSTOMER_ID", LoginServiceManegerB.instance().getLoginId());
//        HttpRestClientB.doHttpBindCard2(params, new JsonHttpResponseHandler() {
//            @Override
//            public void onSuccess(JSONObject response) {
//                super.onSuccess(response);
//                if (1 == response.optInt("code")) {
////                    chat_send_project.setClickable(true);
////                    chat_send_project.setOnClickListener(PConsultMainActivity.this);
////                    IS_SHOW_B = true;
//                    //   showBottomProject();
//                    final RadioGroup radioGroup = (RadioGroup) findViewById(R.id.group);
//                    radioGroup.removeAllViews();
////        List<TagIndexEntity> lists = LiteOrmDBUtil.getLiteOrm().query(TagIndexEntity.class);
//                    List<TagIndexEntity> lists = null;
//                    try {
//                        lists = TagIndexEntity.parseToList(response.getJSONArray("result"));
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    for (int i = 0; i < lists.size(); i++) {
//                        final TagIndexEntity tagindex = lists.get(i);
//                        RadioButton btn = (RadioButton) LayoutInflater.from(PConsultMainActivity.this).inflate(R.layout.check_fragment_radio_btn_item, null);
//                        btn.setText(tagindex.class_name);
//                        btn.setTag(tagindex);
//                        radioGroup.addView(btn);
//                    }
//
//                    if (lists.size() == 1) {
//                        radioGroup.setVisibility(View.GONE);
//                    } else {
//                        radioGroup.setVisibility(View.VISIBLE);
//                    }
//
//                    radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//                        @Override
//                        public void onCheckedChanged(RadioGroup group, int checkedId) {
//                            RadioButton btn = (RadioButton) radioGroup.findViewById(checkedId);
//                            TagIndexEntity tag = (TagIndexEntity) btn.getTag();
//                            List<TagMenuEntity> data = tag.data;
//                            mPageFragment.setData(data);
//                        }
//                    });
//
//                    if (radioGroup.getChildCount() > 0) {
//                        RadioButton b = (RadioButton) radioGroup.getChildAt(0);
//                        b.setChecked(true);
//                    }
//                }
//            }
//        });

    }

    private void initData() {//科室列表请求
        HttpRestClient.doHttpFindOfficePatient(customerid, officeName, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(String content) {
                LogUtil.d("TAG", "科室列表" + content);
                try {
                    JSONObject onject = new JSONObject(content);
                    JSONArray ss = onject.getJSONArray("result");
                    mConsultList = new ArrayList<>();
                    for (int i = 0; i < ss.length(); i++) {
                        mConsultMap = new HashMap<>();
                        JSONObject dd = ss.getJSONObject(i);
                        mConsultMap.put("OFFICE_CODE", dd.optString("OFFICE_CODE"));
                        mConsultMap.put("OFFICE_NAME", dd.optString("OFFICE_NAME"));
                        mConsultList.add(mConsultMap);
                    }
                    mAdapter.removeAll();
                    mAdapter.addAll(mConsultList);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                super.onSuccess(content);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                mPullRefreshListView.onRefreshComplete();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back://回退
                onBackPressed();
                break;
        }
    }

//    @Override
//    public void onItemClick(AdapterView<?> parent, View view, int position,
//                            long id) {
//        selectedOffice = mAdapter.datas.get(position - 1).get("OFFICE_NAME").toString();//科室名称
//        officeCode = mAdapter.datas.get(position - 1).get("OFFICE_CODE").toString();//科室编码
//        initNum();
//    }

    /**
     * 热门疾病请求界面
     */
    private void initDisease(){
        Map<String, String> map = new HashMap<>();
        map.put("disease", diseaseName);
        HttpRestClient.OKHttpFindExpertByDisease(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                ToastUtil.showShort("");
            }

            @Override
            public void onResponse(String content) {
                try {
                    JSONObject object = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(object.optString("code"))) {
                        officeNameNum = object.optString("count");
                    } else {
                        ToastUtil.showShort(object.optString("message"));
                    }
                    if ("0".equals(officeNameNum.toString())) {
                        String isFull = "共有" + officeNameNum.toString() + "位专家等待为您服务";
//                        showS(isFull);//疾病已满对话框
                        showS(changeColor(isFull));//疾病已满对话框
                    } else {
                        String numText = "共有" + officeNameNum.toString() + "位专家等待为您服务";
                        showDisease(changeColor(numText));//疾病未满对话框
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, this);
    }

    /**
     * 按科室请求界面
     */
    private void initNum() {//科室人数请求
        HttpRestClient.doHttpfindDocNum(customerid, officeCode, new ObjectHttpResponseHandler(this) {
            @Override
            public Object onParseResponse(String content) {
                try {
                    if (content != null) {
                        JSONObject object = new JSONObject(content);
                        if ("1".equals(object.optString("code"))) {
                            officeNameNum = object.optString("result");
                        } else if ("0".equals(object.optString("code"))) {
                            ToastUtil.showToastPanl(object.optString("message"));
                        }
                        return officeNameNum;
                    } else {
                        return null;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            @Override
            public void onSuccess(Object response) {
                super.onSuccess(response);
                if (response != null) {
                    if ("0".equals(response.toString())) {
                        String isFull =  "共有" + response.toString() + "位专家等待为您服务";
                        showM(changeColor(isFull));//科室已满对话框
                    } else {
                        String numText = "共有" + response.toString() + "位专家等待为您服务";
                        showD(changeColor(numText));//科室未满对话框
                    }
                }
            }
        });
    }
    public static String type1 = "";
    private void showDisease(SpannableStringBuilder numDoc) {//科室未满对话框
        type1 = "1";
        DoubleBtnFragmentDialog.showDoubleBtn2(PConsultMainActivity.this, diseaseName, numDoc, "自己找专家", "帮我找专家", this, this).show();
    }

    private void showD(SpannableStringBuilder numDoc) {//科室未满对话框
        type1 = "2";
        DoubleBtnFragmentDialog.showDoubleBtn2(PConsultMainActivity.this, selectedOffice, numDoc, "自己找专家", "帮我找专家", this, this).show();
    }

    private void showS(SpannableStringBuilder numDoc) {//疾病已满对话框
        type1 = "1";
        DoubleBtnFragmentDialog.showDoubleBtn1(PConsultMainActivity.this, diseaseName,numDoc, "帮我找专家", this).show();
    }

    private void showM(SpannableStringBuilder numDoc) {//科室已满对话框
        type1 = "2";
        DoubleBtnFragmentDialog.showDoubleBtn1(PConsultMainActivity.this, selectedOffice,numDoc, "帮我找专家", this).show();
    }

    @Override
    public void onBtn1() {//自己选专家跳转

        if (type1=="1"){  //根据疾病选择专家
            Intent intent = new Intent(PConsultMainActivity.this, SelectExpertMainUI.class);
            intent.putExtra("diseaseName",diseaseName);
            intent.putExtra("OFFICECODE", officeCode);
            intent.putExtra(SelectExpertMainUI.TYPE, "diseaseName");
           //intent.putExtra("OFFICENAME", selectedOffice);
            startActivity(intent);

        }else if(type1 == "2"){  //根基科室选择专家
            Intent intent = new Intent(PConsultMainActivity.this, SelectExpertMainUI.class);
           // intent.putExtra("diseaseName",diseaseName);
            intent.putExtra("OFFICECODE", officeCode);
            intent.putExtra("OFFICENAME", selectedOffice);
            intent.putExtra(SelectExpertMainUI.TYPE, "selectedOffice");
            startActivity(intent);

        }

    }

    @Override
    public void onBtn2() {//帮我选专家跳转
        Intent intent = new Intent(PConsultMainActivity.this, FlowMassageActivity.class);
        intent.putExtra("SELECTA", "SELECTA");
        intent.putExtra("OFFICECODE", officeCode);
        intent.putExtra("OFFICENAME", selectedOffice);
        startActivity(intent);

    }

    @Override
    public void onRefresh(PullToRefreshBase<ListView> refreshView) {//刷新
        initData();
    }

    @Override
    public void onBtn3() {//已满去看看
        if (type1.equals("1")){  //根据疾病选择专家
            Intent intent = new Intent(PConsultMainActivity.this, FlowMassageActivity.class);
            intent.putExtra("diseaseName",diseaseName);
//            intent.putExtra("OFFICECODE", officeCode);
            intent.putExtra("SELECTA", "SELECTA");
            startActivity(intent);
        }else if(type1 == "2"){  //根基科室选择专家
            Intent intent = new Intent(PConsultMainActivity.this, FlowMassageActivity.class);
            intent.putExtra("OFFICECODE", officeCode);
            intent.putExtra("OFFICENAME", selectedOffice);
            intent.putExtra("SELECTA", "SELECTA");
            startActivity(intent);
        }
    }

    //字体颜色（数字变红色）
    private SpannableStringBuilder changeColor(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (Character.isDigit(str.charAt(i))) {
                if (index == 0) {
                    start = i;
                }
                index++;
            }
        }
        end = start + index;
        spBuilder = new SpannableStringBuilder(str);
        color = getResources().getColor(R.color.charge_red);//charge_red
        CharacterStyle charaStyle = new ForegroundColorSpan(color);
        spBuilder.setSpan(charaStyle, start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        index = 0;
        return spBuilder;
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        client.connect();
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "PConsultMain Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.yksj.consultation.son.consultation/http/host/path")
//        );
//        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
//        Action viewAction = Action.newAction(
//                Action.TYPE_VIEW, // TODO: choose an action type.
//                "PConsultMain Page", // TODO: Define a title for the content shown.
//                // TODO: If you have web page content that matches this app activity's content,
//                // make sure this auto-generated web page URL is correct.
//                // Otherwise, set the URL to null.
//                Uri.parse("http://host/path"),
//                // TODO: Make sure this auto-generated app URL is correct.
//                Uri.parse("android-app://com.yksj.consultation.son.consultation/http/host/path")
//        );
//        AppIndex.AppIndexApi.end(client, viewAction);
//        client.disconnect();
    }


}
