package com.yksj.consultation.comm;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.ToastUtil;

/**
 * 双按钮弹出框
 * @author zhao
 */
@SuppressLint("ValidFragment")
public class EditFragmentDialog extends DialogFragment {

	private EditText editText;
	public interface OnDilaogClickListener{
		void onDismiss(DialogFragment fragment);
		void onClick(DialogFragment fragment, View v);
	}

	private OnDilaogClickListener mClickListener;

	public static EditFragmentDialog show(FragmentManager manager,String title,int maxNum,String leftBtn,String rightBtn,OnDilaogClickListener click){
		Fragment fragment = manager.findFragmentByTag("DOUBLE_DIALOG");
		FragmentTransaction ft = manager.beginTransaction();
		if(fragment != null){
			ft.remove(fragment);
		}
		EditFragmentDialog dialog = new EditFragmentDialog(click);
		Bundle bundle = new Bundle();
		bundle.putString("title",title);
		bundle.putInt("maxNum", maxNum);//限制输入的个数
		bundle.putString("leftBtn", leftBtn);
		bundle.putString("rightBtn",rightBtn);
		dialog.setArguments(bundle);
		ft.add(dialog, "DOUBLE_DIALOG");
		ft.commitAllowingStateLoss();
		AlertDialog d;
		return dialog;
	}



	public EditFragmentDialog() {}

	public EditFragmentDialog(OnDilaogClickListener clickListener) {
		this.mClickListener = clickListener;
	}

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(),R.style.translucent_dialog);
		dialog.setContentView(R.layout.dialog_edit_doublebtn_layout);
		Bundle bundle = getArguments();
		final int maxLength=bundle.getInt("maxNum");
		dialog.setCancelable(false);//使双按钮对话框点击屏幕不可被取消
		dialog.setCanceledOnTouchOutside(false);
//		dialog.setOnKeyListener(new OnKeyListener() {
//
//			@Override
//			public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//				if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//				}
//				return true;
//			}
//		});
		TextView titleTxtV = (TextView)dialog.findViewById(R.id.dialog_title);
		editText = (EditText)dialog.findViewById(R.id.dialog_edittext);
		editText.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if(editText.getText().toString().length()>maxLength){
					ToastUtil.showShort(getActivity(),"最多只能输入"+maxLength+"个字");
					editText.setText(editText.getText().toString().substring(0, maxLength));
					editText.setSelection(maxLength);
				}
			}
		});
		Button button1 = (Button)dialog.findViewById(R.id.dialog_left);
		Button button2 = (Button)dialog.findViewById(R.id.dialog_right);

		button1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dismissAllowingStateLoss();
				if(mClickListener != null)mClickListener.onDismiss(EditFragmentDialog.this);
			}
		});
		button2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				EditFragmentDialog.this.dismissAllowingStateLoss();
				if (mClickListener != null) mClickListener.onClick(EditFragmentDialog.this, v);
			}
		});
		titleTxtV.setText(bundle.getString("title"));
		button1.setText(bundle.getString("leftBtn"));
		button2.setText(bundle.getString("rightBtn"));
		return dialog;
	}

	public String getEditTextStr() {
		return editText.getText().toString().trim();
	}
	public void setEditTextStr(String str) {
		editText.setText(str);
	}

	@Override
	public void onResume() {
		super.onResume();
		(new Handler()).postDelayed(new Runnable() {
			public void run() {
				InputMethodManager inManager = (InputMethodManager) editText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
			}
		}, 500);
	}
}
