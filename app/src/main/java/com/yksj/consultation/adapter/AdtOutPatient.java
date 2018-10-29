package com.yksj.consultation.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.LevelListDrawable;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.Button;

import com.yksj.consultation.son.R;
import com.yksj.consultation.son.consultation.outpatient.AtyOutPatientDetail;

import org.json.JSONObject;
import org.universalimageloader.core.DefaultConfigurationFactory;
import org.universalimageloader.core.DisplayImageOptions;
import org.universalimageloader.core.ImageLoader;

/**
 * Created by HEKl on 2015/9/18.
 * Used for
 */
public class AdtOutPatient extends SimpleBaseAdapter<JSONObject> {
    private LevelListDrawable drawable;// 分割线
    private FragmentActivity maActivity;
    private int Type;
    private DisplayImageOptions mOptions;
    private ImageLoader instance;

    public AdtOutPatient(Context context, int type) {
        super(context);
        this.context = context;
        this.Type = type;
        maActivity = (FragmentActivity) context;
        instance = ImageLoader.getInstance();
        mOptions = DefaultConfigurationFactory.createHeadDisplayImageOptions(maActivity);
    }

    @Override
    public int getCount() {
        if (Type == 0) {
            return 3;
        } else {
            return 2;
        }
    }

    @Override
    public int getItemResource() {
        return R.layout.outpatient_item_layout;
    }

    @Override
    public View getItemView(final int position, View convertView, ViewHolder holder) {
        final Button state = holder.getView(R.id.btn_status);
        switch (Type) {
            case 0:
                switch (position) {
                    case 0:
                        state.setText("待付款");
                        break;
                    case 1:
                        state.setText("退款中");
                        break;
                    case 2:
                        state.setText("服务中");
                        break;
                }
                break;
            case 1:
                state.setBackgroundResource(R.drawable.leftstate_gray);
                switch (position) {
                    case 0:
                        state.setText("已取消");
                        break;
                    case 1:
                        state.setText("已完成");
                        break;
                }
                break;
        }
        convertView.findViewById(R.id.rl_entry).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=null;
                switch (Type) {
                    case 0:
                        switch (position) {
                            case 0:
                                intent=new Intent(maActivity, AtyOutPatientDetail.class);
                                intent.putExtra("STATE",1);
                                maActivity.startActivity(intent);
                                break;
                            case 1:
                                intent=new Intent(maActivity, AtyOutPatientDetail.class);
                                intent.putExtra("STATE",2);
                                maActivity.startActivity(intent);
                                break;
                            case 2:
                                intent=new Intent(maActivity, AtyOutPatientDetail.class);
                                intent.putExtra("STATE",3);
                                maActivity.startActivity(intent);
                                break;
                        }
                        break;
                    case 1:
                        switch (position) {
                            case 0:
                                intent=new Intent(maActivity, AtyOutPatientDetail.class);
                                intent.putExtra("STATE",4);
                                maActivity.startActivity(intent);
                                break;
                            case 1:
                                intent=new Intent(maActivity, AtyOutPatientDetail.class);
                                intent.putExtra("STATE",5);
                                maActivity.startActivity(intent);
                                break;
                        }
                        break;
                }

            }
        });

        return convertView;
    }
}
