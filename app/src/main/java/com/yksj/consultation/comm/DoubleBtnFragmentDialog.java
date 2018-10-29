package com.yksj.consultation.comm;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import com.yksj.healthtalk.utils.HStringUtil;

/**
 * 双按钮弹出框
 *
 * @author zhao
 */
@SuppressLint("ValidFragment")
public class DoubleBtnFragmentDialog extends DialogFragment {

    public interface OnDilaogClickListener {
        void onDismiss(DialogFragment fragment);

        void onClick(DialogFragment fragment, View v);
    }

    public interface OnFirstClickListener {
        void onBtn1(String str);
    }

    public interface OnFristClickListener {
        void onBtn1();
    }

    public interface OnSecondClickListener {
        void onBtn2();
    }

    public interface OnOfficFullListener {
        void onBtn3();
    }

    private OnDilaogClickListener mClickListener;

    public static DoubleBtnFragmentDialog show(FragmentManager manager, String title, String content, String leftBtn, String rightBtn, OnDilaogClickListener click) {
        Fragment fragment = manager.findFragmentByTag("DOUBLE_DIALOG");
        FragmentTransaction ft = manager.beginTransaction();
        if (fragment != null) {
            ft.remove(fragment);
        }
        DoubleBtnFragmentDialog dialog = new DoubleBtnFragmentDialog(click);
        Bundle bundle = new Bundle();
        bundle.putString("title", title);
        bundle.putString("content", content);
        bundle.putString("leftBtn", leftBtn);
        bundle.putString("rightBtn", rightBtn);
        dialog.setArguments(bundle);
        ft.add(dialog, "DOUBLE_DIALOG");
        ft.commitAllowingStateLoss();
        return dialog;
    }

    /*
     *默认 title为新六一健康
     */
    public static DoubleBtnFragmentDialog showDefault(FragmentManager manager, String content, String leftBtn, String rightBtn, OnDilaogClickListener click) {
        return show(manager, "六一健康", content, leftBtn, rightBtn, click);
    }


    public DoubleBtnFragmentDialog() {
    }

    public DoubleBtnFragmentDialog(OnDilaogClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getActivity(), R.style.translucent_dialog);
        Bundle bundle = getArguments();
        dialog.setCancelable(false);//使双按钮对话框点击屏幕不可被取消
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                }
                return true;
            }
        });
        dialog.setContentView(R.layout.dialog_doublebtn_layout);
        TextView titleTxtV = (TextView) dialog.findViewById(R.id.dialog_title);
        TextView contentTxtV = (TextView) dialog.findViewById(R.id.dialog_note);
        Button button1 = (Button) dialog.findViewById(R.id.dialog_cancel);
        Button button2 = (Button) dialog.findViewById(R.id.dialog_ok);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissAllowingStateLoss();
                if (mClickListener != null) mClickListener.onDismiss(DoubleBtnFragmentDialog.this);
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                DoubleBtnFragmentDialog.this.dismissAllowingStateLoss();
                if (mClickListener != null) mClickListener.onClick(DoubleBtnFragmentDialog.this, v);
            }
        });
        titleTxtV.setText(bundle.getString("title"));
        contentTxtV.setText(bundle.getString("content"));
        button1.setText(bundle.getString("leftBtn"));
        button2.setText(bundle.getString("rightBtn"));
        return dialog;
    }

    public static Dialog showDoubleBtn(Context context, String textName, String text, String btn1, String btn2, final OnFristClickListener onFristClickListener,
                                       final OnSecondClickListener onSecondClickListener) {
        final Dialog dialog = new Dialog(context, R.style.translucent_dialog);
        dialog.setCancelable(false);//使双按钮对话框点击屏幕不可被取消
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                }
                return true;
            }
        });
        dialog.setContentView(R.layout.selete_way);
        TextView textNameTag = (TextView) dialog.findViewById(R.id.selete_way_ke);
        TextView textTag = (TextView) dialog.findViewById(R.id.selete_way_num);
        Button close = (Button) dialog.findViewById(R.id.close_way);
        Button button1 = (Button) dialog.findViewById(R.id.selete_way_one);
        Button button2 = (Button) dialog.findViewById(R.id.selete_way_two);

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFristClickListener.onBtn1();
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSecondClickListener.onBtn2();
                dialog.dismiss();
            }
        });
        textNameTag.setText(textName);
        textTag.setText(text);
        button1.setText(btn1);
        button2.setText(btn2);
        return dialog;
    }

    /**
     * 自定义双按钮
     *
     * @param context
     * @param onFristClickListener
     * @param onSecondClickListener
     * @return
     */
    public static Dialog showDoubleBtnPay(final Context context, String title, final OnFirstClickListener onFristClickListener,
                                          final OnSecondClickListener onSecondClickListener) {
        final Dialog dialog = new Dialog(context, R.style.translucent_dialog);
        dialog.setCancelable(false);//使双按钮会话框点击屏幕不可被取消
        dialog.setCanceledOnTouchOutside(false);
        dialog.setContentView(R.layout.dialog_double_layout);
        final EditText editLabel = (EditText) dialog.findViewById(R.id.et_label_add);
        Button cancel = (Button) dialog.findViewById(R.id.btn_cancel);
        Button sure = (Button) dialog.findViewById(R.id.btn_sure);
        TextView textTitle = (TextView) dialog.findViewById(R.id.title);
        textTitle.setText(title);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String label = editLabel.getText().toString().trim();
                if (!HStringUtil.isEmpty(label)) {
                    dialog.dismiss();
                    onFristClickListener.onBtn1(label);
                }
            }
        });

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            public void onShow(DialogInterface dialog) {
                InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editLabel, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        return dialog;
    }

    public static Dialog showDoubleBtn1(Context context, String textName, SpannableStringBuilder text, String btn2, final OnOfficFullListener onOfficFullListener) {
        final Dialog dialog = new Dialog(context, R.style.translucent_dialog);
        dialog.setCancelable(false);//使双按钮对话框点击屏幕不可被取消
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                }
                return true;
            }
        });
        dialog.setContentView(R.layout.offic_full_dia);
//        TextView textNameTag = (TextView)dialog.findViewById(R.id.offie_full_num);
        TextView textNameTag = (TextView) dialog.findViewById(R.id.selete_way_ke);
        TextView textTag = (TextView) dialog.findViewById(R.id.selete_way_num);
        Button close = (Button) dialog.findViewById(R.id.close_way);
        Button button2 = (Button) dialog.findViewById(R.id.offie_full_one);

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onOfficFullListener.onBtn3();
                dialog.dismiss();
            }
        });
        textNameTag.setText(textName);
        textTag.setText(text);
        button2.setText(btn2);
        dialog.dismiss();
        return dialog;
    }

    public static Dialog showDoubleBtn2(Context context, String textName, SpannableStringBuilder text, String btn1, String btn2, final OnFristClickListener onFristClickListener,
                                        final OnSecondClickListener onSecondClickListener) {
        final Dialog dialog = new Dialog(context, R.style.translucent_dialog);
        dialog.setCancelable(false);//使双按钮对话框点击屏幕不可被取消
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                }
                return true;
            }
        });
        dialog.setContentView(R.layout.selete_way);
        TextView textNameTag = (TextView) dialog.findViewById(R.id.selete_way_ke);
        TextView textTag = (TextView) dialog.findViewById(R.id.selete_way_num);
        Button close = (Button) dialog.findViewById(R.id.close_way);
        Button button1 = (Button) dialog.findViewById(R.id.selete_way_one);
        Button button2 = (Button) dialog.findViewById(R.id.selete_way_two);

        close.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFristClickListener.onBtn1();
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSecondClickListener.onBtn2();
                dialog.dismiss();
            }
        });
        textNameTag.setText(textName);
        textTag.setText(text);
        button1.setText(btn1);
        button2.setText(btn2);
        return dialog;
    }

    public static Dialog showDoubleBtn3(Context context, String title, String btn1, String btn2, final OnFristClickListener onFristClickListener,
                                        final OnSecondClickListener onSecondClickListener) {
        final Dialog dialog = new Dialog(context, R.style.translucent_dialog);
        dialog.setCancelable(false);//使双按钮对话框点击屏幕不可被取消
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                }
                return true;
            }
        });
        dialog.setContentView(R.layout.double_dialog_invite);
        TextView textTitle= (TextView) dialog.findViewById(R.id.title);
        Button button1 = (Button) dialog.findViewById(R.id.sure);
        Button button2 = (Button) dialog.findViewById(R.id.cancel);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFristClickListener.onBtn1();
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSecondClickListener.onBtn2();
                dialog.dismiss();
            }
        });
        textTitle.setText(title);
        button1.setText(btn1);
        button2.setText(btn2);
        return dialog;
    }
    public static Dialog showDoubleBtn4(Context context, String title, String btn1, String btn2, final OnFristClickListener onFristClickListener,
                                        final OnSecondClickListener onSecondClickListener) {
        final Dialog dialog = new Dialog(context, R.style.translucent_dialog);
        dialog.setCancelable(false);//使双按钮对话框点击屏幕不可被取消
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnKeyListener(new OnKeyListener() {

            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
                }
                return true;
            }
        });
        dialog.setContentView(R.layout.double_dialog_call_service);
        TextView contentTxtV = (TextView) dialog.findViewById(R.id.dialog_note);
        Button button1 = (Button) dialog.findViewById(R.id.dialog_cancel);
        Button button2 = (Button) dialog.findViewById(R.id.dialog_ok);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onFristClickListener.onBtn1();
                dialog.dismiss();
            }
        });
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onSecondClickListener.onBtn2();
                dialog.dismiss();
            }
        });
        contentTxtV.setText(title);
        button1.setText(btn1);
        button2.setText(btn2);
        return dialog;
    }
}
