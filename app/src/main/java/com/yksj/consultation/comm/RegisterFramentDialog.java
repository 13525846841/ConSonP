package com.yksj.consultation.comm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.salon.SalonSelectPaymentOptionActivity;

/**
 * 搞号
 * 
 * @author Administrator
 * 
 */
@SuppressLint("ValidFragment")
public class RegisterFramentDialog extends DialogFragment implements
		OnClickListener {
	FragmentManager mFragmentManager;
	private String mTitleName;
	private String message;
	private String payAccount;// 订单号
	private GroupInfoEntity mGroupInfoEntity;

	public static void showLodingDialog(FragmentManager manager,
			String message, String titleName, String payAccount,
			GroupInfoEntity entity) {
		Fragment fragment = manager.findFragmentByTag("register");
		FragmentTransaction ft = manager.beginTransaction();
		if (fragment != null) {
			ft.remove(fragment);
		}
		DialogFragment dialog = RegisterFramentDialog.newInstance(titleName,
				message, payAccount, manager, entity);
		ft.add(dialog, "register");
		ft.commitAllowingStateLoss();
	}

	public RegisterFramentDialog(FragmentManager manager,
			GroupInfoEntity mCustomerInfoEntity
			) {
		this.mFragmentManager = manager;
		this.mGroupInfoEntity = mCustomerInfoEntity;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTitleName = this.getArguments().getString("titleName");
		message = this.getArguments().getString("message");
		payAccount = this.getArguments().getString("payAccount");
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(), R.style.translucent_dialog);
		dialog.setContentView(R.layout.register_dialog);
		Button mCancelButton = (Button) dialog.findViewById(R.id.cancelButton);
		Button mSureButton = (Button) dialog.findViewById(R.id.sureButton);
		TextView titleName = (TextView) dialog.findViewById(R.id.title);
		TextView message = (TextView) dialog.findViewById(R.id.message);
		titleName.setText(mTitleName);
		message.setText(this.message);
		mSureButton.setOnClickListener(this);
		mCancelButton.setOnClickListener(this);
		return dialog;
	}

	public static RegisterFramentDialog newInstance(String titleName,
			String message, String payAccount, FragmentManager manager,
			GroupInfoEntity entity) {
		RegisterFramentDialog dialog = new RegisterFramentDialog(manager,
				entity);
		Bundle bundle = new Bundle();
		bundle.putString("titleName", titleName);
		bundle.putString("message", message);
		bundle.putString("payAccount", payAccount);
		dialog.setArguments(bundle);
		return dialog;
	}

	public static void dismiss(FragmentManager fragmentManager) {
		if (fragmentManager == null)
			return;
		DialogFragment dialogFragment = (DialogFragment) fragmentManager
				.findFragmentByTag("register");
		dialogFragment.dismissAllowingStateLoss();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancelButton:
			dismiss(mFragmentManager);
			break;
		case R.id.sureButton:
			if (!TextUtils.isEmpty(payAccount)) {
				Intent intent = new Intent();
				intent.setClass(getActivity(), SalonSelectPaymentOptionActivity.class);
				intent.putExtra("payId", payAccount);
				intent.putExtra("entity", mGroupInfoEntity);
				startActivity(intent);
				dismiss(mFragmentManager);
			} 
			break;
		}
	}

}
