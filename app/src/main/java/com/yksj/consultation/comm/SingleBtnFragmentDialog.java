package com.yksj.consultation.comm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.login.UserLoginActivity;

/**
 * 单按钮弹出框
 *
 * @author zhao
 */
@SuppressLint("ValidFragment")
public class SingleBtnFragmentDialog extends DialogFragment {

    private String shareDialog = "";
    private Activity onAttachActivity;
    private OnClickSureBtnListener listener;

    public SingleBtnFragmentDialog(OnClickSureBtnListener listener) {
        super();
        this.listener = listener;
    }

    public SingleBtnFragmentDialog() {
        super();
    }


    public static SingleBtnFragmentDialog show(FragmentManager manager, String title, String content, String btnStr) {
        Fragment fragment = manager.findFragmentByTag("charge");
        FragmentTransaction ft = manager.beginTransaction();
        if (fragment != null) {
            ft.remove(fragment);
        }
        SingleBtnFragmentDialog dialog = new SingleBtnFragmentDialog();
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putString("btnStr", btnStr);
        dialog.setArguments(bundle);
        ft.add(dialog, "dialog1");
        ft.commitAllowingStateLoss();
        return dialog;
    }

    /**
     * 默认title为六一健康 button为知道了
     *
     * @param manager
     * @param content
     */
    public static SingleBtnFragmentDialog showDefault(FragmentManager manager, String content) {
        return show(manager, "六一健康", content, "知道了");
    }

    /**
     * 用户异地登录通知,为了适配MIUI系统,不适用dialog
     *
     * @param manager
     * @param content
     */
    public static SingleBtnFragmentDialog showLogOut(FragmentManager manager, final Context context) {
        return show(manager, "六一健康", "您的账号在别处登录,当前登录下线", "确定", new OnClickSureBtnListener() {
            @Override
            public void onClickSureHander() {
                Intent intent = new Intent(context, UserLoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
    }

    /**
     * 默认title为六一健康 button为知道了
     *
     * @param manager
     * @param content
     * @param listener1
     */
    public static SingleBtnFragmentDialog showDefault(FragmentManager manager, String content, OnClickSureBtnListener listener1) {
        return show(manager, "六一健康", content, "知道了", listener1);
    }

    /**
     * 默认title为六一健康 button为知道了
     *
     * @param manager
     * @param content
     * @param listener1
     */
    public static SingleBtnFragmentDialog showDefaultNot(FragmentManager manager, String content, OnClickSureBtnListener listener1) {
        return showNot(manager, "六一健康", content, "知道了", listener1);
    }

    public static SingleBtnFragmentDialog showNot(FragmentManager manager, String title, String content, String btnStr, OnClickSureBtnListener listener1) {
        Fragment fragment = manager.findFragmentByTag("charge");
        FragmentTransaction ft = manager.beginTransaction();
        if (fragment != null) {
            ft.remove(fragment);
        }
        SingleBtnFragmentDialog dialog = new SingleBtnFragmentDialog(listener1);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putString("btnStr", btnStr);
        dialog.setArguments(bundle);
        dialog.setCancelable(false);
        ft.add(dialog, "dialog1");
        ft.commitAllowingStateLoss();
        return dialog;
    }

    public static SingleBtnFragmentDialog show(FragmentManager manager, String title, String content, String btnStr, OnClickSureBtnListener listener1) {
        Fragment fragment = manager.findFragmentByTag("charge");
        FragmentTransaction ft = manager.beginTransaction();
        if (fragment != null) {
            ft.remove(fragment);
        }
        SingleBtnFragmentDialog dialog = new SingleBtnFragmentDialog(listener1);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putString("btnStr", btnStr);
        dialog.setArguments(bundle);
        dialog.setCancelable(false);
        ft.add(dialog, "dialog1");
        ft.commitAllowingStateLoss();
        return dialog;
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
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                }
                return true;
            }
        });
        Bundle bundle = getArguments();
        if (!shareDialog.equals("share")) {
            dialog.setContentView(R.layout.dialog_singlebtn_layout);
            TextView titleTxtV = (TextView) dialog.findViewById(R.id.dialog_title);
            TextView contentTxtV = (TextView) dialog.findViewById(R.id.dialog_note);
            contentTxtV.setMovementMethod(ScrollingMovementMethod.getInstance());
            Button button = (Button) dialog.findViewById(R.id.dialog_ok);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismissAllowingStateLoss();
                    if (listener != null) {
                        listener.onClickSureHander();
                    }
                }
            });
            titleTxtV.setText(bundle.getString("title"));
            contentTxtV.setText(bundle.getString("content"));
            button.setText(bundle.getString("btnStr"));
        }
        return dialog;
    }

    public interface OnClickSureBtnListener {
        void onClickSureHander();
    }

    public static Dialog showSinglebtn(Context context, String textName, String btn1, final OnClickSureBtnListener singlelistener) {
        final Dialog dialog = new Dialog(context, R.style.translucent_dialog);
        dialog.setCancelable(false);//使双按钮对话框点击屏幕不可被取消
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                }
                return true;
            }
        });
        dialog.setContentView(R.layout.self_single_dialog);
        TextView textNameTag = (TextView) dialog.findViewById(R.id.selete_way_ke);
        Button close = (Button) dialog.findViewById(R.id.close_way);
        Button button1 = (Button) dialog.findViewById(R.id.selete_way);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                singlelistener.onClickSureHander();
            }
        });
        textNameTag.setText(textName);
        button1.setText(btn1);
        return dialog;
    }

}
