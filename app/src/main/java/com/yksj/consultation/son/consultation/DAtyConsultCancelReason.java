package com.yksj.consultation.son.consultation;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.yksj.consultation.comm.BaseFragmentActivity;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.net.http.AsyncHttpResponseHandler;
import com.yksj.healthtalk.net.http.HttpRestClient;
import com.yksj.healthtalk.net.http.RequestParams;
import com.yksj.healthtalk.utils.LogUtil;
import com.yksj.healthtalk.utils.TimeUtil;
import com.yksj.healthtalk.utils.ToastUtil;

/**
 * @author HEKL
 *         取消服务原因
 */
public class DAtyConsultCancelReason extends BaseFragmentActivity implements OnClickListener {
	private int conId;// 会诊Id
	private TextView mCancelPerson, mCancelTime, mCancelReason;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.aty_consult_canceled);
		initView();
	}

	private void initView() {
		initTitle();
		titleLeftBtn.setOnClickListener(this);
		titleTextV.setText("取消服务原因");
		mCancelPerson = (TextView) findViewById(R.id.tv_cancelMen);
		mCancelTime = (TextView) findViewById(R.id.tv_cancelTime);
		mCancelReason = (TextView) findViewById(R.id.tv_cancelResons);
		conId = getIntent().getIntExtra("conId", 0);
		doHttpCheckReasons();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.title_back:
			onBackPressed();
			break;
		}
	}

	/**
	 * 查看取消服务原因
	 */
	private void doHttpCheckReasons() {
		RequestParams params = new RequestParams();
		params.put("OPTION", "8");
		params.put("CONSULTATIONID", conId+"");
		HttpRestClient.doHttpDoctorService(params, new AsyncHttpResponseHandler() {
			JSONObject obj = null;
			@Override
			public void onSuccess(int statusCode, String content) {
				try {
					obj=new JSONObject(content);
					if (content.contains("errormessage")) {
						ToastUtil.showShort(obj.getString("errormessage"));
					} else {
						mCancelPerson.setText(obj.getString("NAME").toString());
						mCancelTime.setText(TimeUtil.format(obj.getString("TIME").toString()));
						mCancelReason.setText(obj.getString("REASON").toString());
						LogUtil.e("mCancelPerson", obj.getString("NAME").toString());
					}
				} catch (JSONException e) {
					e.printStackTrace();
				}

				super.onSuccess(statusCode, content);
			}
		});
	}
}
