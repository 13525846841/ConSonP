package com.yksj.consultation.comm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.yksj.healthtalk.entity.GroupInfoEntity;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.salon.SalonSelectPaymentOptionActivity;

/**
 * 沙龙购买门票 日票 月票
 * @author Administrator
 *
 */
@SuppressLint("ValidFragment")
public class BuyNumFramentDialog extends DialogFragment implements OnClickListener{
	String  mTitleName;
	int mDayTicketMoney;
	int mMonthTicketMoney ;
	RadioGroup mRadioGroup;
	GroupInfoEntity mInfoEntity;
	FragmentManager mFragmentManager;
	
	/**
	 * @param manager
	 * @param titleName 标题名称
	 * @param dayTicketMoney 日票 没有则设置为0
	 * @param monthTicketMoney 月票 没有则设置为0
	 * @return
	 */
	public static void showLodingDialog(FragmentManager manager,GroupInfoEntity entity,String titleName,int dayTicketMoney,int monthTicketMoney){
		Fragment fragment = manager.findFragmentByTag("buy");
		FragmentTransaction ft = manager.beginTransaction();
		if(fragment != null){
			ft.remove(fragment);
		}
		DialogFragment dialog = BuyNumFramentDialog.newInstance( titleName,dayTicketMoney,entity,monthTicketMoney,manager);
		ft.add(dialog,"buy");
		ft.commitAllowingStateLoss();
	}
	
	public BuyNumFramentDialog(FragmentManager manager,GroupInfoEntity infoEntity) {
		this.mInfoEntity = infoEntity;
		this.mFragmentManager = manager;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mDayTicketMoney = this.getArguments().getInt("dayTicketMoney");
		mMonthTicketMoney = this.getArguments().getInt("monthTicketMoney");
		mTitleName = this.getArguments().getString("titleName");
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(),R.style.translucent_dialog);
		dialog.setContentView(R.layout.salon_buy_duomeibi);
		Button mCancelButton = (Button)dialog.findViewById(R.id.cancelButton);
		Button mBuyButton = (Button)dialog.findViewById(R.id.buyButton);
//		TextView titleName = (TextView)dialog.findViewById(R.id.remindText);
		mRadioGroup = (RadioGroup)dialog.findViewById(R.id.rp);
		RadioButton dayBtn = (RadioButton)dialog.findViewById(R.id.day_btn);
		RadioButton monthBtn = (RadioButton)dialog.findViewById(R.id.month_btn);
//		titleName.setText(mTitleName);
		if (mMonthTicketMoney < 0) {
			monthBtn.setVisibility(View.GONE);
		}else {
			monthBtn.setVisibility(View.VISIBLE);
			monthBtn.setChecked(true);
			monthBtn.setText(mMonthTicketMoney+"元(月票)");
		}
		
		if (mDayTicketMoney < 0) {
			dayBtn.setVisibility(View.GONE);
		}else {
			dayBtn.setVisibility(View.VISIBLE);
			dayBtn.setChecked(true);
			dayBtn.setText(mDayTicketMoney+"元(日票)");
		}
		mCancelButton.setOnClickListener(this);
		mBuyButton.setOnClickListener(this);
		return dialog;
	}
	
	public static BuyNumFramentDialog newInstance(String titleName , int dayTicketMoney ,GroupInfoEntity entity,int monthTicketMoney,FragmentManager manager){
		BuyNumFramentDialog dialog = new BuyNumFramentDialog(manager,entity);
		Bundle bundle = new Bundle();
		bundle.putString("titleName",titleName);
		bundle.putInt("dayTicketMoney",dayTicketMoney);
		bundle.putInt("monthTicketMoney",monthTicketMoney);
		dialog.setArguments(bundle);
		return dialog;
	}
	
	public static void dismiss(FragmentManager fragmentManager){
		if(fragmentManager == null)return;
		DialogFragment dialogFragment = (DialogFragment)fragmentManager.findFragmentByTag("buy");
		dialogFragment.dismissAllowingStateLoss();
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cancelButton:
			dismiss(mFragmentManager);
			break;
		case R.id.buyButton://购买
			String money = "0";
			String type = "0";
			switch (mRadioGroup.getCheckedRadioButtonId()) {
			case R.id.day_btn:
				money = String.valueOf(mDayTicketMoney);
				type = "1";
				break;
			case R.id.month_btn:
				money = String.valueOf(mMonthTicketMoney);;
				type = "2";
				break;	
			}
			onBuyTicket(type,money);
			dismissAllowingStateLoss();
			break;
		}
	}
	
	/**
	 * 买门票
	 */
	private void onBuyTicket(String type,final String mony){
		Intent intent = new Intent();
		intent.setClass(getActivity(), SalonSelectPaymentOptionActivity.class);
		intent.putExtra("entity", mInfoEntity);
		intent.putExtra("type", type);
		startActivity(intent);
	}
}
