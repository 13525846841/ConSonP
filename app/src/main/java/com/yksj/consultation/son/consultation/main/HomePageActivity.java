package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.HisSearchListAdaper;
import com.yksj.consultation.adapter.HotSearchAdapter;
import com.yksj.consultation.adapter.SearchAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.CommonwealAidAty;
import com.yksj.consultation.son.consultation.DAtyConslutDynMes;
import com.yksj.consultation.son.consultation.news.DiseaseDetailActivity;
import com.yksj.consultation.son.home.DoctorInfoActivity;
import com.yksj.consultation.son.home.DoctorStudioActivity;
import com.yksj.consultation.son.home.DoctorWorkstationMainActivity;
import com.yksj.consultation.son.site.StationActivity;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.utils.SharePreUtils;
import com.yksj.healthtalk.utils.ToastUtil;
import com.yksj.healthtalk.utils.WheelUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 首页进入搜索的界面
 */

public class HomePageActivity extends BaseFragmentActivity implements View.OnClickListener, TextView.OnEditorActionListener, TextWatcher, AdapterView.OnItemClickListener {

    private TextView search_expert_clear_history;//清空历史记录
    private TextView cancel_onclick;//取消按钮
    private EditText seach_text;
    private ListView mHistoryList;//历史记录适配器
    private ListView mSearchList;
    private SearchAdapter mmAdapter;
    private LinearLayout ll_main_search;
    private ArrayList<HashMap<String, String>> mainHistory;//搜索历史
    private HisSearchListAdaper adapter;//下文搜索历史
    private static final String hiskeyName = HomePageActivity.class.getName();
    private GridView mGridView;
    private HotSearchAdapter mAdapter;
    private List<JSONObject> mmList = new ArrayList<>();
    private List<JSONObject> list = null;//搜索list
    private boolean isSearch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showHistory();
    }

    private void showHistory() {
        mainHistory.clear();
        mainHistory.addAll(SharePreUtils.getSearchHistory(this, hiskeyName));
        if (mainHistory.size() != 0) {
            mHistoryList.setVisibility(View.VISIBLE);
            adapter.notifyDataSetChanged();
        } else {
            mHistoryList.setVisibility(View.GONE);
            return;
        }
    }

    private void initView() {
        search_expert_clear_history = (TextView) findViewById(R.id.search_expert_clear_history);
        search_expert_clear_history.setOnClickListener(this);
        cancel_onclick = (TextView) findViewById(R.id.cancel_onclick);
        seach_text = (EditText) findViewById(R.id.seach_text);
        cancel_onclick.setOnClickListener(this);
        mHistoryList = (ListView) findViewById(R.id.search_expert_history);
        mainHistory = new ArrayList<HashMap<String, String>>();
        adapter = new HisSearchListAdaper(this, mainHistory);
        mHistoryList.setAdapter(adapter);

        seach_text.setOnEditorActionListener(this);
        mmAdapter = new SearchAdapter(this);
        mSearchList = (ListView) findViewById(R.id.search_expert);
        mSearchList.setAdapter(mmAdapter);

        ll_main_search = (LinearLayout) findViewById(R.id.ll_main_search);
        mGridView = (GridView) findViewById(R.id.gv_hot_search);
        mAdapter = new HotSearchAdapter(mmList, this);
        mGridView.setAdapter(mAdapter);
        mGridView.setOnItemClickListener(this);
        mHistoryList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                HashMap<String, String> data = mainHistory.get(position);
                seach_text.setText(data.get("name"));
                initSearchData(data.get("name"));
            }
        });
        initData();
        seach_text.addTextChangedListener(this);

        mSearchList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int type = list.get(position).optInt("type");
                //1 新闻 3 六一百科 5 医生工作室 7 疾病 9医生集团
                switch (type) {
                    case 1:
                        Intent intent = new Intent(HomePageActivity.this, DAtyConslutDynMes.class);
                        intent.putExtra("conId", HTalkApplication.APP_CONSULTATION_CENTERID);
                        intent.putExtra("infoId", list.get(position).optString("INFO_ID"));
                        intent.putExtra("title", "热点新闻");
                        startActivity(intent);
                        break;
                    case 3:
                        Intent intent1 = new Intent(HomePageActivity.this, CommonwealAidAty.class);
                        intent1.putExtra(CommonwealAidAty.URL, HTalkApplication.getHttpUrls().HTML + "/hos.html" + "?hospital_id=" + list.get(position).optString("HOSPITAL_ID"));
                        intent1.putExtra(CommonwealAidAty.TITLE, "六一百科");
                        startActivity(intent1);
                        break;
                    case 5:
//                        Intent intent2 = new Intent(HomePageActivity.this, DoctorStudioActivity.class);
//                        intent2.putExtra("DOCTOR_ID", list.get(position).optString("CUSTOMER_ID"));
//                        intent2.putExtra(DoctorStudioActivity.SITE_ID, list.get(position).optString("SITE_ID"));
//                        startActivity(intent2);
                        Intent intent2 = new Intent(HomePageActivity.this, DoctorInfoActivity.class);
                        intent2.putExtra("customer_id",Integer.valueOf(list.get(position).optString("CUSTOMER_ID")));
                        String site_id = list.get(position).optString("SITE_ID");
                        int siteID=-1;
                        if (site_id.equals("")){
                            siteID=-1;
                        }else {
                            siteID=Integer.valueOf(list.get(position).optString("SITE_ID"));
                        }
                        intent2.putExtra(DoctorInfoActivity.SITE_ID,siteID);
                        startActivity(intent2);
                        break;
                    case 7:
                        Intent intent3 = new Intent(HomePageActivity.this, DiseaseDetailActivity.class);
                        intent3.putExtra("name", list.get(position).optString("DISEASE_NAME"));
                        intent3.putExtra("disease_id", list.get(position).optString("DISEASE_ID"));
                        startActivity(intent3);
                        break;
                    case 9:
                        Intent intent4 = new Intent(HomePageActivity.this, DoctorWorkstationMainActivity.class);
//                        Intent intent4 = new Intent(HomePageActivity.this, StationActivity.class);
                        intent4.putExtra(DoctorWorkstationMainActivity.SITE_ID, list.get(position).optString("SITE_ID"));
                        startActivity(intent4);
                        break;
                    default:
                        break;
                }
            }
        });
    }

    private void initData() {
        HttpRestClient.doHttppHotSearchWord(new AsyncHttpResponseHandler(this) {
            @Override
            public void onSuccess(String content) {
                super.onSuccess(content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (obj.has("words")) {
                        JSONArray array = obj.optJSONArray("words");
                        mmList = new ArrayList<>();
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject jsonobject1 = array.getJSONObject(i);
                            mmList.add(jsonobject1);
                        }
                        mAdapter.onBoundData(mmList);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_onclick://取消按钮
                if (isSearch == false) {
                    onBackPressed();
                } else if (isSearch == true) {
                    mSearchList.setVisibility(View.INVISIBLE);
                    ll_main_search.setVisibility(View.VISIBLE);
                    isSearch = false;
                }
                break;
            case R.id.search_expert_clear_history://清除历史记录按钮
                ToastUtil.showShort(this, "清空历史");
                SharePreUtils.clearSearchHistory(this, hiskeyName);
                mHistoryList.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
        boolean handled = false;
        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
            String text = seach_text.getText().toString().trim();
            if (text != null && text.length() != 0) {
                SharePreUtils.mainSaveSearchHistory(HomePageActivity.this, hiskeyName, text);
                initSearchData(text);
                handled = true;
            }
        } else {
            ToastUtil.showShort("请输入内容");
        }
        return handled;
    }

    /**
     * 加载搜索数据
     */
    private void initSearchData(String search) {
        WheelUtils.hideInput(HomePageActivity.this, seach_text.getWindowToken());
        mSearchList.setVisibility(View.VISIBLE);
        ll_main_search.setVisibility(View.INVISIBLE);
        isSearch = true;
        Map<String, String> map = new HashMap<>();
        map.put("search", search);
        HttpRestClient.OKHttpSearch(map, new OkHttpClientManager.ResultCallback<String>() {

            @Override
            public void onError(Request request, Exception e) {
            }

            @Override
            public void onResponse(String content) {
//                Log.i("kkk", "onResponse: "+content);
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))) {
                        JSONObject jsonobject = obj.optJSONObject("result");
                        list = new ArrayList<>();
                        list = parseData(jsonobject);
                        mmAdapter.onBoundData(list);
                    } else {
                        ToastUtil.showShort(obj.optString("message"));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, this);
    }

    private List<JSONObject> parseData(JSONObject jsonobject) {
        try {
            JSONArray artsArray = jsonobject.optJSONArray("arts");
            JSONArray hospitalArray = jsonobject.optJSONArray("hosipital");
            JSONArray doctorArray = jsonobject.optJSONArray("doctors");
            JSONArray diseaseArray = jsonobject.optJSONArray("disease");
            JSONArray sitesArray = jsonobject.optJSONArray("sites");

            if (null != artsArray && artsArray.length() > 0) {
                //活动
                JSONObject object1 = new JSONObject();
                object1.put("type", 0);
                object1.put("title", "活动");
                list.add(object1);
                for (int i = 0; i < artsArray.length(); i++) {
                    JSONObject artsObject = artsArray.getJSONObject(i);
                    artsObject.put("type", 1);//活动内容
                    list.add(artsObject);
                }
            }

            if (null != hospitalArray && hospitalArray.length() > 0) {
                //医院
                JSONObject object2 = new JSONObject();
                object2.put("type", 2);
                object2.put("title", "医院");
                list.add(object2);
                for (int j = 0; j < hospitalArray.length(); j++) {
                    JSONObject hospitalObject = hospitalArray.getJSONObject(j);
                    hospitalObject.put("type", 3);//医院列表
                    list.add(hospitalObject);
                }
            }
            if (null != doctorArray && doctorArray.length() > 0) {
                //医生
                JSONObject object3 = new JSONObject();
                object3.put("type", 4);
                object3.put("title", "医生");
                list.add(object3);
                for (int m = 0; m < doctorArray.length(); m++) {
                    JSONObject doctorObject = doctorArray.getJSONObject(m);
                    doctorObject.put("type", 5);//医生列表
                    list.add(doctorObject);
                }
            }
            if (null != diseaseArray && diseaseArray.length() > 0) {
                //疾病
                JSONObject object4 = new JSONObject();
                object4.put("type", 6);
                object4.put("title", "疾病");
                list.add(object4);
                for (int n = 0; n < diseaseArray.length(); n++) {
                    JSONObject diseaseObject = diseaseArray.getJSONObject(n);
                    diseaseObject.put("type", 7);//疾病列表
                    list.add(diseaseObject);
                }
            }
            if (null != sitesArray && sitesArray.length() > 0) {
                //医生集团
                JSONObject object5 = new JSONObject();
                object5.put("type", 8);
                object5.put("title", "集团");
                list.add(object5);
                for (int n = 0; n < sitesArray.length(); n++) {
                    JSONObject diseaseObject = sitesArray.getJSONObject(n);
                    diseaseObject.put("type", 9);//疾病列表
                    list.add(diseaseObject);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
//        String search = seach_text.getText().toString().trim();
//        Map<String,String> map=new HashMap<>();
//        map.put("search", search);
//        HttpRestClient.OKHttpSearch(map, new OkHttpClientManager.ResultCallback<String>() {
//
//            @Override
//            public void onError(Request request, Exception e) {
//                ToastUtil.showShort("添加失败");
//            }
//
//            @Override
//            public void onResponse(String content) {
//                try {
//                    JSONObject obj = new JSONObject(content);
//                    JSONObject jsonobject = obj.optJSONObject("result");
//                    JSONArray array1 = jsonobject.optJSONArray("arts");
//                    for (int i = 0; i < array1.length(); i++) {
//                        JSONObject jsonobject1 = array1.getJSONObject(i);
//
//                    }
//                    JSONArray array2 = jsonobject.optJSONArray("doctors");
//                    for (int i = 0; i < array2.length(); i++) {
//                        JSONObject jsonobject2 = array2.getJSONObject(i);
//
//                    }
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        },this);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        initSearchData(mmList.get(position).optString("WORDS"));
    }
}
