package com.yksj.healthtalk.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;

import com.yksj.consultation.comm.CommonAdapter;
import com.yksj.consultation.comm.CommonViewHolder;
import com.yksj.consultation.son.R;
import com.yksj.healthtalk.bean.AlertEntity;

import java.util.List;


/**
 * Created by jack_tang on 15/10/28.
 */
public class AlertDialogUtils {



    public interface OnClickListener {

        void onClick(View v, AlertEntity entity);
    }


    public static void show(Context context, LayoutInflater layoutInflater, List<AlertEntity> lists, final AlertDialogUtils.OnClickListener onClickListener, String title) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = layoutInflater.inflate(R.layout.alert_list_view, null);
        ListView mListView = (ListView) view.findViewById(R.id.list);
        if (title != null)
            builder.setTitle(title);
        builder.setView(view);
        final AlertDialog alertDialog = builder.create();
        final CommonAdapter commonAdapter;
        mListView.setAdapter(commonAdapter = new CommonAdapter<AlertEntity>(context) {
            @Override
            public void onBoundView(CommonViewHolder helper, AlertEntity item) {
                helper.setText(R.id.content, item.name);
            }

            @Override
            public int viewLayout() {
                return R.layout.alert_item_text;
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                alertDialog.dismiss();
                if (onClickListener != null)
                    onClickListener.onClick(view, (AlertEntity) commonAdapter.getItem(position));

            }
        });
        commonAdapter.onBoundData(lists);
        alertDialog.show();
    }

    public static void show(Context context, LayoutInflater layoutInflater, List<AlertEntity> lists, final AlertDialogUtils.OnClickListener onClickListener) {
        show(context, layoutInflater, lists, onClickListener, null);
    }


    public interface dilaogClick {
        void onDismiss(DialogInterface dialog);

        void onClick(DialogInterface dialog);
    }

    public interface dialogDateClick {
        void onDismiss(DialogInterface dialog);

        void onClick(DialogInterface dialog, String date);
    }

    /**
     * @param context
     * @param message     内容
     * @param NegativeTxt 取消
     * @param PositTxt    执行
     * @param listener    监听
     * @return
     */
    public static AlertDialog show(Context context, String message, String NegativeTxt, String PositTxt, final dilaogClick listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("提示");
        builder.setMessage(message);
        builder.setNegativeButton(NegativeTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (listener != null) listener.onDismiss(dialog);
            }
        });
        builder.setPositiveButton(PositTxt, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (listener != null) listener.onClick(dialog);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }

    /**
     * 日期选择
     *
     * @param context
     * @param
     * @param NegativeTxt 取消
     * @param PositTxt    执行
     * @param listener    监听
     * @return
     */
    public static AlertDialog showDate(Context context, String NegativeTxt, String PositTxt, final dialogDateClick listener) {
//        View view = LayoutInflater.from(context).inflate(R.layout.add_popup_time_dialog, null);
//        final DatePicker dp = (DatePicker) view.findViewById(R.id.datepicker);

        final DatePicker dp =new DatePicker(context);
        dp.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        dp.setCalendarViewShown(false);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setView(dp);
        builder.setTitle("时间");
        builder.setNegativeButton(NegativeTxt, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                if (listener != null) listener.onDismiss(dialog);
            }
        });
        builder.setPositiveButton(PositTxt, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                String month = "";
                String dayOfMonth = "";
                if (String.valueOf(dp.getMonth() + 1).length() == 1) {
                    month = "0" + String.valueOf(dp.getMonth() + 1);
                } else if (String.valueOf(dp.getMonth() + 1).length() == 2) {
                    month = String.valueOf(dp.getMonth() + 1);
                }
                if (String.valueOf(dp.getDayOfMonth()).length() == 1) {
                    dayOfMonth = "0" + String.valueOf(dp.getDayOfMonth());
                } else {
                    dayOfMonth = String.valueOf(dp.getDayOfMonth());
                }

                String date = (String.valueOf(dp.getYear()) + "-" + month + "-" + dayOfMonth);
                if (listener != null) listener.onClick(dialog, date);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
        return dialog;
    }


}
