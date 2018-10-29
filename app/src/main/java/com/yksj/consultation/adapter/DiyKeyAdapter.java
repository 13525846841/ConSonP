package com.yksj.consultation.adapter;

import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yksj.consultation.son.R;
import java.util.Map;

/**
 * Created by zheng on 2015/7/14.
 */
public class DiyKeyAdapter extends SimpleBaseAdapter<Map<String ,String>> {
    private DeleteClick deleteClick;
    public DiyKeyAdapter(Context context) {
        super(context);
    }
    public interface DeleteClick{
        void delete(int position);
        void add(int position,String key);
    }
    @Override
    public int getItemResource() {
        return R.layout.flow_message_view;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {
        Map<String,String> map=datas.get(position);
        ImageView delete=(ImageView)holder.getView(R.id.message_btn_delete);
        TextView textView = (TextView)holder.getView(R.id.flow_message_text);
        final EditText key = (EditText)holder.getView(R.id.message_tv_num11);
        textView.setVisibility(View.VISIBLE);
        textView.setText(map.get("KEYWORD"));
        if(datas.size()==(position+1)){
//            key.setBackgroundResource(R.drawable.img_create_topic_add_label);
            textView.setVisibility(View.GONE);
            delete.setVisibility(View.GONE);
            key.setVisibility(View.VISIBLE);
//            key.setBackgroundResource();
            key.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                @Override
                public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                    if (i == EditorInfo.IME_ACTION_DONE) {
                        textView.setVisibility(View.VISIBLE);
                        String str=key.getText().toString();
                        textView.setText(str);
                        if (str!=null&&str.length()>0)
                            deleteClick.add(position, key.getText().toString());
                        return true;
                    }
                    return false;
                }
            });
//            key.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (event != null && KeyEvent.ACTION_DOWN == event.getAction() && keyCode == KeyEvent.KEYCODE_ENTER) {
//                    deleteClick.add(position, key.getText().toString());
//                    return true;
//                }
//                return false;
//            }
//        });
        }
//        key.setText(map.get("KEYWORD"));
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteClick.delete(position);
            }
        });

        return convertView;
    }

    public void setDeleteClick(DeleteClick deleteClick) {
        this.deleteClick = deleteClick;
    }
}
