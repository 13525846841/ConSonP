package com.yksj.healthtalk.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.yksj.consultation.son.R;

/**
 * Created by zl on 2015/7/20.
 */
public class Redact extends Dialog {
    private EditText etName;
    private OnRedactDialogListener onRedactDialogListener;
    public interface OnRedactDialogListener{
        public void back(String name);
    }
    public Redact(Context context) {
        super(context);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.redact_dialog);
        setTitle("编辑自定义标签");
        etName = (EditText)findViewById(R.id.redact_edit);
        etName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (i == EditorInfo.IME_ACTION_DONE) {
                    String str=etName.getText().toString();
                    textView.setText("完成");
                    if (str!=null&&str.length()>0){
                        onRedactDialogListener.back(str);
                    }
                    return true;
                }
                return false;
            }
        });
    }

    public void setOnRedactDialogListener(OnRedactDialogListener onRedactDialogListener) {
        this.onRedactDialogListener = onRedactDialogListener;
    }
}
