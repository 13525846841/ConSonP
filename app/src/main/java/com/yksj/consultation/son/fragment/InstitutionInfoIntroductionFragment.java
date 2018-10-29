package com.yksj.consultation.son.fragment;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.yksj.consultation.comm.DoubleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.bean.MyEvent;
import com.yksj.consultation.son.home.InstitutionInfoMainActivity;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.OkHttpClientManager;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * A simple {@link Fragment} subclass.
 */
public class InstitutionInfoIntroductionFragment extends Fragment implements View.OnClickListener {


    public static final int EVENT_MSG = 10101;
    private TextView tvInsName;
    private TextView tvInsAddress;
    private TextView tvLuXian;
    private TextView tvContent;
    private String unit_code;
    private String tel="";
    private TextView tipTv;
    public InstitutionInfoIntroductionFragment() {
        // Required empty public constructor
    }

    public static InstitutionInfoIntroductionFragment newInstance(int Unit_Code) {

        Bundle args = new Bundle();
        args.putString(InstitutionInfoMainActivity.Unit_Code,Unit_Code+"");
        InstitutionInfoIntroductionFragment fragment = new InstitutionInfoIntroductionFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_institution_info_introduction, container, false);
        initView(view);
        loadData();
        return view;
    }

    private void initView(View view) {
        unit_code = getArguments().getString("Unit_Code");
        tipTv = (TextView) view.findViewById(R.id.tipTv);
        tipTv.setOnClickListener(this);
        tipTv.setEnabled(false);
        tvInsName = (TextView) view.findViewById(R.id.tvInsName);
        tvInsAddress = (TextView) view.findViewById(R.id.tvInsAddress);
        tvLuXian = (TextView) view.findViewById(R.id.tvLuXian);
        tvContent = (TextView) view.findViewById(R.id.tvContent);
        LinearLayout linePhone= (LinearLayout) view.findViewById(R.id.linePhone);
        linePhone.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linePhone:
                DoubleBtnFragmentDialog.showDefault(getChildFragmentManager(), "是否拨打电话？", "取消", "确定", new DoubleBtnFragmentDialog.OnDilaogClickListener() {
                    @Override
                    public void onDismiss(DialogFragment fragment) {
                        fragment.dismiss();
                    }

                    @Override
                    public void onClick(DialogFragment fragment, View v) {
                        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+tel));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });

                break;
            case R.id.tipTv:
                tipTv.setText("加载中...");
                tipTv.setEnabled(false);
                loadData();
                break;
        }
    }

    private void loadData() {
        List<BasicNameValuePair> pairs = new ArrayList<>();
        pairs.add(new BasicNameValuePair("op", "queryDetails"));
        pairs.add(new BasicNameValuePair("att", "10"));
        pairs.add(new BasicNameValuePair("Unit_Code", unit_code));

        HttpRestClient.doGetInstitutionsServlet(pairs, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                tipTv.setText("加载失败，点击重试");
                tipTv.setEnabled(true);
            }

            @Override
            public void onResponse(String response) {
                Log.i("lll", "onResponse: "+response);
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject rjson = jsonObject.getJSONObject("result");
                    tvInsName.setText(rjson.getString("UNIT_NAME"));
                    tvInsAddress.setText(rjson.getString("UNIT_ADDRESS_DESC"));
                    tvContent.setText(rjson.getString("UNIT_SPECIALTY_DESC"));
                    String unit_address_path = rjson.getString("UNIT_ADDRESS_PATH");
                    if (unit_address_path.equals("null")) {
                        tvLuXian.setText("暂无出行路线");
                        Log.i("ggg", "onResponse: "+unit_address_path);
                    }else {
                        tvLuXian.setText(unit_address_path);
                        Log.i("ggg", "onResponse: "+unit_address_path);
                    }
                    tel=rjson.getString("UNIT_TEL1");
                    EventBus.getDefault().post(new MyEvent(rjson.getString("UNIT_PIC1"), EVENT_MSG));
                    tipTv.setVisibility(View.GONE);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },this);
    }
}
