package com.yksj.consultation.comm;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.SystemUtils;

import java.util.Timer;
import java.util.TimerTask;

/**
 * 单按钮弹出框
 *
 * @author zhao
 */
public class WalletPayFragmentDialog extends DialogFragment {

    private String shareDialog = "";
    private Activity onAttachActivity;
    private OnClickSureBtnListener listener;
    private EditText mMoney;

    public static void show(FragmentManager manager, String title, String content) {
        Fragment fragment = manager.findFragmentByTag("charge");
        FragmentTransaction ft = manager.beginTransaction();
        if (fragment != null) {
            ft.remove(fragment);
        }
        DoubleBtnFragmentDialog dialog = new DoubleBtnFragmentDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        dialog.setArguments(bundle);
        ft.add(dialog, "dialog1");
        ft.commitAllowingStateLoss();
    }


    @Override
    public void onActivityCreated(Bundle arg0) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
                           public void run() {
                               InputMethodManager inputManager =
                                       (InputMethodManager) mMoney.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                               inputManager.showSoftInput(mMoney, 0);
                           }
                       },
                250);

        super.onActivityCreated(arg0);
    }


    @Override
    public void onCancel(DialogInterface dialog) {
        super.onCancel(dialog);
        setStyle(DialogFragment.STYLE_NO_TITLE, 0);
    }

    @Override
    public void onAttach(Activity activity) {
        try {
            onAttachActivity = activity;
            listener = (OnClickSureBtnListener) activity;
        } catch (ClassCastException e) {
        }
        super.onAttach(activity);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.translucent_dialog);
        Bundle bundle = getArguments();
        if (!shareDialog.equals("share")) {
            dialog.setContentView(R.layout.wallet_pay_layout);
            TextView titleTxtV = (TextView) dialog.findViewById(R.id.dialog_title);
            TextView contentTxtV = (TextView) dialog.findViewById(R.id.dialog_note);
            contentTxtV.setMovementMethod(ScrollingMovementMethod.getInstance());
            Button dialog_ok = (Button) dialog.findViewById(R.id.dialog_ok);
            Button dialog_cancel = (Button) dialog.findViewById(R.id.dialog_cancel);
            mMoney = (EditText) dialog.findViewById(R.id.money);
            dialog_ok.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SystemUtils.hideSoftBord(onAttachActivity, mMoney);
                    dismissAllowingStateLoss();
                    if (listener != null) {
                        listener.onClickSureHander(mMoney.getText().toString().trim());
                    }
                }
            });
            dialog_cancel.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    SystemUtils.hideSoftBord(onAttachActivity, mMoney);
                    dismissAllowingStateLoss();

                }
            });
            titleTxtV.setText(bundle.getString("title"));
            contentTxtV.setText(bundle.getString("content"));
        }
        return dialog;
    }

    public interface OnClickSureBtnListener {
        void onClickSureHander(String money);
    }

}
