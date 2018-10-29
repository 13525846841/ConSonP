package com.yksj.consultation.son.doctor;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.yksj.consultation.adapter.HistoryListAdapter;
import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.SharePreUtils;
import com.yksj.healthtalk.utils.ToastUtil;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 搜索专家界面
 *
 */
public class SearchExpertActivity extends BaseFragmentActivity implements View.OnClickListener,
        HistoryListAdapter.OnClickDeleteHistoryListener,AdapterView.OnItemClickListener{

    TextView tvClear,tvCancel;
    ListView historyList;
    EditText editSearch;
    private ArrayList<HashMap<String, String>> history;//搜索历史
    private HistoryListAdapter historyAdapter;//搜索历史适配器
    private static final String keyName=SearchExpertActivity.class.getName();
    private String officeCode;
    private int goalType=0;//0表示搜索专家,1表示搜索医生  2搜索专家重选
    private String consultId;
    private String officeName;


    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        setContentView(R.layout.aty_search_expert);
        officeCode=getIntent().getStringExtra("OFFICECODE");
        officeName=getIntent().getStringExtra("OFFICENAME");

        goalType=getIntent().getIntExtra("type",0);
        if (goalType==2)
            consultId=getIntent().getStringExtra("consultId");
        initView();
    }

    @Override
    protected void onStart() {
        super.onStart();
        showHistory();
    }

    private void showHistory() {
        history.clear();
        history.addAll(SharePreUtils.getSearchHistory(this, keyName));
        if(history.size()!=0){
            historyList.setVisibility(View.VISIBLE);
            historyAdapter.notifyDataSetChanged();
        }else{
            historyList.setVisibility(View.GONE);
            return;
        }
    }

    private void initView() {
        tvClear= (TextView) findViewById(R.id.search_expert_clear_history);
        tvCancel= (TextView) findViewById(R.id.cancel_onclick);
        historyList= (ListView) findViewById(R.id.search_expert_history);
        editSearch= (EditText) findViewById(R.id.seach_text);
        if (getIntent().hasExtra("Tip")){
            editSearch.setHint("请输入医生姓名");
        }else {
            editSearch.setHint(R.string.search_expert_hint);
        }
        tvClear.setOnClickListener(this);
        tvCancel.setOnClickListener(this);

        history=new ArrayList<HashMap<String,String>>();
        historyAdapter=new HistoryListAdapter(SearchExpertActivity.this,history);
        historyAdapter.setDeleteListener(this);
        historyList.setAdapter(historyAdapter);
        editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    String text = editSearch.getText().toString().trim();
                    if (text != null && text.length() != 0) {
                        SharePreUtils.saveSearchHistory(SearchExpertActivity.this, keyName, text);
                        editSearch.setText("");
                        Intent intent = new Intent(SearchExpertActivity.this, SearchExpertResultActivity.class);
                        intent.putExtra("result", text);
                        intent.putExtra("type", goalType);
                        if (getIntent().hasExtra("CLINIC")){
                            intent.putExtra("CLINIC", "CLINIC");
                        }
                        if (goalType==2)
                            intent.putExtra("consultId",consultId);
                        intent.putExtra("OFFICECODE", officeCode);
                        intent.putExtra("OFFICENAME", officeName);
                        startActivityForResult(intent, 201);
                        handled = true;
                    } else {
                        ToastUtil.showShort(getString(R.string.inputThemeName));
                    }
                }
                return handled;
            }
        });
        historyList.setOnItemClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent intent=null;
        switch (v.getId()){
            case R.id.search_expert_clear_history:
                ToastUtil.showShort(this, "清空历史");
                SharePreUtils.clearSearchHistory(this, keyName);
                historyList.setVisibility(View.GONE);
                break;
            case R.id.cancel_onclick:
                onBackPressed();
                break;
//            case R.id.input_case:
//                intent=new Intent(SearchExpertActivity.this, CreateCaseActivity.class);
//                startActivity(intent);
//
//                break;
//            case R.id.upload_case:
//                intent=new Intent(SearchExpertActivity.this, UploadCaseActivity.class);
//                startActivity(intent);
//
//                break;
        }
    }

    @Override
    public void onDeleteItem(int pos) {
        history.remove(pos);
        StringBuilder sb=new StringBuilder("");
        for(int i=0;i<history.size();i++){
            sb.append(history.get(i).get("name")+",");

        }
        String result=sb.toString();
        if(history.size()>0){
            result=result.substring(0, sb.length()-1);
        }
        SharePreUtils.saveResultHistory(SearchExpertActivity.this,keyName,result);
        showHistory();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode){
            case 201://搜索返回
                if (resultCode == RESULT_OK) {
                    setResult(RESULT_OK,data);
                    finish();
                }
                break;
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(SearchExpertActivity.this, SearchExpertResultActivity.class);
        intent.putExtra("result", history.get(position).get("name"));
        intent.putExtra("type", goalType);
        if (getIntent().hasExtra("CLINIC")){
            intent.putExtra("CLINIC", "CLINIC");
        }
        if (goalType==2)
            intent.putExtra("consultId",consultId);
        intent.putExtra("OFFICECODE", officeCode);
        intent.putExtra("OFFICENAME", officeName);
        startActivityForResult(intent, 201);
    }
}
