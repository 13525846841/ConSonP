package com.yksj.consultation.comm;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ResultFragmtDialog extends DialogFragment {
	String content;
	
	static ResultFragmtDialog newInstance(String content){
		ResultFragmtDialog dialog = new ResultFragmtDialog();
		Bundle bundle = new Bundle();
		bundle.putString("content",content);
		dialog.setArguments(bundle);
		return dialog;
	}
	
	public static void showSuccessDialog(FragmentManager manager,String content){
		ResultFragmtDialog dialog = ResultFragmtDialog.newInstance(content);
		dialog.show(manager,"result");
	}
	
	public static void showFailDialog(FragmentManager manager,String content){
		Fragment fragment = manager.findFragmentByTag("result");
		FragmentTransaction ft = manager.beginTransaction();
		if(fragment != null){
			ft.remove(fragment);
		}
		ResultFragmtDialog dialog = ResultFragmtDialog.newInstance(content);
		dialog.show(ft,"result");
	}
	
	public static void dimiss(FragmentManager manager){
		if(manager == null)return;
		DialogFragment dialogFragment = (DialogFragment)manager.findFragmentByTag("result");
		if(dialogFragment != null){
			dialogFragment.dismissAllowingStateLoss();
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public String getContent(){
		return getArguments().getString("content");
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog dialog = new Builder(getActivity())
		.setMessage(getContent())
		.setPositiveButton("确定",new Dialog.OnClickListener(){
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		})
		.create();
		dialog.show();
		return dialog;
	}
	
	
}
    