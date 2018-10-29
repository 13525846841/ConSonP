package com.yksj.consultation.son.consultation.news;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.DynamicMesAllAdapter;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.app.HTalkApplication;
import com.yksj.consultation.son.consultation.DAtyConslutDynMes;
import com.yksj.consultation.son.consultation.bean.NewsClass;
import com.yksj.healthtalk.entity.DynamicMessageListEntity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.FileUtils;
import com.yksj.healthtalk.utils.SharePreUtils;
import com.yksj.healthtalk.utils.TimeUtil;
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

/**
 * Created by HEKL on 16/5/9.
 * Used for
 */

public class FrgDynamicMessage extends RootFragment implements PullToRefreshBase.OnRefreshListener2<ListView>, View.OnClickListener,
        AdapterView.OnItemClickListener {
    private PullToRefreshListView mPullToRefreshListView;
    private ListView mListView;
    private DynamicMesAllAdapter nfeAdapter;
    private int mPagesize = 0;// 页码
    private List<DynamicMessageListEntity> nfeList;
    private DynamicMessageListEntity dnlEntity;
    private View mNullView;
    private HashMap<String, String> mAlreadyRead;// 已读

    public static final String TYPEID = "type_id";
    public static final String TYPENAME = "type_name";
    public static final String CONTENT = "content";
    public static final String TYPE = "type";//分类排序0,1,2...
    private String typeid = "";
    private String type = "";
    private String typename = "";
    private NewsClass newsClass;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.consultant_center_viewpager_listview3, null, false);
        initView(view);
        return view;
    }

    private String content;
    private void initView(View view) {
        Bundle args = getArguments();
        type = args != null ? args.getString(TYPE) : "";
        typeid = args != null ? args.getString(TYPEID) : "";
        typename = args != null ? args.getString(TYPENAME) : "";
        content = args != null ? args.getString(CONTENT) : "";

        if (!LoginServiceManeger.instance().isVisitor) {
            mAlreadyRead = FileUtils.fatchReadedDynMes();
        }
        mNullView = view.findViewById(R.id.mnullview);
        mPullToRefreshListView = (PullToRefreshListView) view.findViewById(R.id.consultation_newsfeed);
        mListView = mPullToRefreshListView.getRefreshableView();

        if (!LoginServiceManeger.instance().isVisitor) {
            nfeAdapter = new DynamicMesAllAdapter(getActivity(), 0, mAlreadyRead);
        } else {
            nfeAdapter = new DynamicMesAllAdapter(getActivity(), 0, null);
        }
        mListView.setAdapter(nfeAdapter);
        mListView.setOnItemClickListener(this);
        mPullToRefreshListView.setOnRefreshListener(this);
//      mPullToRefreshListView.setRefreshing();
//      initData3();
        initData4();
    }

    private void initData4() {
        nfeList = new ArrayList<>();
        try {
            JSONArray array = new JSONArray(content);
            JSONObject item;
            for (int i = 0; i < array.length(); i++) {
                item = array.getJSONObject(i);
                dnlEntity = new DynamicMessageListEntity();
                dnlEntity.setConsultationCenterId(item.optInt("CONSULTATION_CENTER_ID"));
                dnlEntity.setCustomerId(item.optInt("CUSTOMER_ID"));
                dnlEntity.setInfoId(item.optInt("INFO_ID"));
                dnlEntity.setInfoPicture(item.optString("INFO_PICTURE"));
                String time = TimeUtil.formatTime(item.optString("PUBLISH_TIME"));
                dnlEntity.setPublishTime(time);
                dnlEntity.setStatusTime(item.optString("STATUS_TIME"));
                dnlEntity.setInfoStaus(item.optString("INFO_STATUS"));
                dnlEntity.setInfoName(item.optString("INFO_NAME"));
                dnlEntity.setColorchage(0);
                nfeList.add(dnlEntity);
            }
            SharePreUtils.saveDynamicReadedId(dnlEntity.getInfoId() + "");
            if (nfeList.size() != 0) {
                if (mPagesize == 1) {
                    nfeAdapter.replaceAll(nfeList);
                } else {
                    nfeAdapter.addAll(nfeList);
                }
            }else {
                ToastUtil.showShort("没有更多了");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private String info_class_id = "10101";
    public void setClassId(String roomObject,String mType){
        if (mType.equals(type)){
            info_class_id = roomObject;
            initData();
        }

    }

    //单独数据
    private void initData() {
        Map<String,String> map=new HashMap<>();
        map.put("consultation_center_id",HTalkApplication.APP_CONSULTATION_CENTERID);
        map.put("info_class_id",info_class_id);//101 热点新闻
        HttpRestClient.OKHttpNewsCenter(map, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    JSONObject jsonobject = obj.optJSONObject("news");
                    List<DynamicMessageListEntity> mList = new ArrayList<>();
                    JSONArray array = jsonobject.optJSONArray("artList");
                    DynamicMessageListEntity entity=new DynamicMessageListEntity();
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject jsonobject1 = array.getJSONObject(i);
                        entity = new DynamicMessageListEntity();
                        entity.setConsultationCenterId(jsonobject1.optInt("CONSULTATION_CENTER_ID"));
                        entity.setCustomerId(jsonobject1.optInt("CUSTOMER_ID"));
                        entity.setInfoId(jsonobject1.optInt("INFO_ID"));
                        entity.setInfoPicture(jsonobject1.optString("INFO_PICTURE"));
                        String time = TimeUtil.formatTime(jsonobject1.optString("PUBLISH_TIME"));
                        entity.setPublishTime(time);
                        entity.setStatusTime(jsonobject1.optString("STATUS_TIME"));
                        entity.setInfoStaus(jsonobject1.optString("INFO_STATUS"));
                        entity.setInfoName(jsonobject1.optString("INFO_NAME"));
                        entity.setColorchage(0);
                        mList.add(entity);
                    }
                    SharePreUtils.saveDynamicReadedId(entity.getInfoId() + "");
                    nfeAdapter.onBoundData(mList);
                    if (mList.size()==0){
                        mNullView.setVisibility(View.VISIBLE);
                        mPullToRefreshListView.setVisibility(View.GONE);
                    }else {
                        mNullView.setVisibility(View.GONE);
                        mPullToRefreshListView.setVisibility(View.VISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }

    @Override
    public void onClick(View v) {
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String infoId = nfeAdapter.datas.get(position - 1).getInfoId() + "";
        if (!LoginServiceManeger.instance().isVisitor) {
            mAlreadyRead.put(infoId, infoId);
            FileUtils.updateReadedDynMesIds(mAlreadyRead);
        }
        TextView textView = (TextView) view.findViewById(R.id.tv_messtitle);
        textView.setTextColor(getResources().getColor(R.color.news_readed_color));
        Intent intent = new Intent(getActivity(), DAtyConslutDynMes.class);
        intent.putExtra("conId", HTalkApplication.APP_CONSULTATION_CENTERID);
        intent.putExtra("infoId", "" + nfeAdapter.datas.get(position - 1).getInfoId());
        intent.putExtra("title", typename);
        startActivity(intent);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
        mPagesize = 1;
        initData4();
        refreshView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
        mPagesize = 1;
        initData4();
        refreshView.onRefreshComplete();

    }

    /**
     * 全部动态消息数据加载
     */
//      private void initData3() {
//          Map<String, String> map = new HashMap<>();
//          map.put("consultation_center_id", HTalkApplication.APP_CONSULTATION_CENTERID);
//          map.put("info_class_id", info_class_id);//101 热点新闻
//          HttpRestClient.OKHttpNewsCenter(map, new OkHttpClientManager.ResultCallback<String>() {
//              @Override
//              public void onError(Request request, Exception e) {
//              }
//              @Override
//              public void onResponse(String content) {
//                  try {
//                      JSONObject obj = new JSONObject(content);
//                      newsClass = new NewsClass();
//                      JSONObject jsonobject = obj.optJSONObject("news");
//                      if (jsonobject.has("children")) {
//                          JSONArray array = jsonobject.optJSONArray("children");
//                          for (int i = 0; i < array.length(); i++) {
//                              JSONObject jsonobject1 = array.getJSONObject(i);
//
//                          }
//                      }
//                  } catch (JSONException e) {
//                      e.printStackTrace();
//                  }
//
//              }
//          }, this);
//      }

}
