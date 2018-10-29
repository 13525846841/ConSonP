package com.yksj.consultation.son.consultation.main;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.adapter.AddressAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.HResultCallback;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.HttpResult;
import com.yksj.healthtalk.net.socket.LoginServiceManeger;
import com.yksj.healthtalk.utils.HStringUtil;
import com.yksj.healthtalk.utils.ToastUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 商城收货地址 添加或者地址列表
 */
public class AddressActivity extends BaseFragmentActivity implements View.OnClickListener, AddressAdapter.MyOnClickListener {
    private String customerId = "";
    private List<JSONObject> list = null;
    private ListView mLv;
    private AddressAdapter adapter;
    private LinearLayout noAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        initView();
    }
    private void initView() {
        initTitle();
        titleLeftBtn.setOnClickListener(this);
        titleTextV.setText("收货地址");

        findViewById(R.id.add_address).setOnClickListener(this);
        noAddress = (LinearLayout) findViewById(R.id.no_address);
        mLv = (ListView) findViewById(R.id.address_list);
        customerId = LoginServiceManeger.instance().getLoginEntity().getId();
        adapter = new AddressAdapter(this,this);
        mLv.setAdapter(adapter);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        Map<String,String> map=new HashMap<>();
        map.put("Type", "findAddress");
        map.put("customer_id", customerId);
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
                            JSONArray array = object.optJSONArray("addresses");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject jsonobject = array.getJSONObject(i);
                                list.add(jsonobject);
                            }

                            if (list.size()==0){
                                mLv.setVisibility(View.GONE);
                                noAddress.setVisibility(View.VISIBLE);
                            }else {
                                adapter.onBoundData(list);
                                mLv.setVisibility(View.VISIBLE);
                                noAddress.setVisibility(View.GONE);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back:
                onBackPressed();
                break;
            case R.id.add_address://新建地址
                Intent intent  = new Intent(this,AddNewAddressActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onDeleteClick(View view, final int position, int id) {
        switch (id){
            case R.id.delete_address:
                final TextView deleteAddress = (TextView) view.findViewById(R.id.delete_address);
                deleteAddress.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        DoubleBtnFragmentDialog.showDefault(getSupportFragmentManager(), "您确定要删除地址吗？", "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                            @Override
                            public void onDismiss(DialogFragment fragment) {

                            }
                            @Override
                            public void onClick(DialogFragment fragment, View v) {
                                deleteAddress(position);
                            }
                        });

                    }
                });
                break;
            case R.id.ll_icon_address:
            case R.id.icon_address:
                setDefaultAdress(position);
//                final ImageView imageView = (ImageView) view.findViewById(R.id.icon_address);
//                imageView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(final View v) {
//                       setDefaultAdress(position);
//                    }
//                });
                break;
        }
    }

    /**
     * 设置默认地址
     */
    private void setDefaultAdress(int position) {
        address_id = adapter.addressId(position);
        Map<String,String> map=new HashMap<>();
        map.put("Type", "setDefaultAddress");
        map.put("address_id", address_id);
        map.put("customer_id", customerId);
        HttpRestClient.OKHttGoodsServlet(map, new HResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        ToastUtil.showShort(obj.optString("message"));
                        initData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }

    /**
     * 删除地址
     */
    private String address_id = "";
    private void deleteAddress(int position) {
        address_id = adapter.addressId(position);

        Map<String,String> map=new HashMap<>();
        map.put("Type", "deleteAddressById");
        map.put("address_id", address_id);
        HttpRestClient.OKHttGoodsServlet(map, new HResultCallback<String>(this) {
            @Override
            public void onError(Request request, Exception e) {
            }
            @Override
            public void onResponse(String content) {
                try {
                    JSONObject obj = new JSONObject(content);
                    if (HttpResult.SUCCESS.equals(obj.optString("code"))){
                        ToastUtil.showShort(obj.optString("message"));
                        initData();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }
}
