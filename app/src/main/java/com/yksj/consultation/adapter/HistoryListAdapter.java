package com.yksj.consultation.adapter;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.yksj.consultation.son.R;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 搜索历史纪录适配器
 * @author Administrator
 *
 */
public class HistoryListAdapter extends BaseAdapter{

	private ArrayList<HashMap<String,String>> datas;
    private OnClickDeleteHistoryListener del;
    private Context context;

    public HistoryListAdapter(Context context,ArrayList<HashMap<String,String>> datas){
        this.context=context;
        this.datas=datas;
    }
     
     public void setData(ArrayList<HashMap<String,String>> datas){
    	 this.datas=datas;
     }

    public void setDeleteListener(OnClickDeleteHistoryListener listener){
        this.del=listener;
    }
      
     @Override
     public int getCount() {
         return datas.size();
     }

     @Override
     public Object getItem(int position) {
         return datas.get(position);
     }

     @Override
     public long getItemId(int position) {
         return position;
     }

     @Override
     public View getView(final int position, View convertView, ViewGroup parent) {
         Holder holder;

         if (convertView == null) {
             holder = new Holder();
             convertView = LayoutInflater.from(context).inflate(R.layout.search_doctor_history_item, null);
             holder.img = (TextView) convertView.findViewById(R.id.search_doc_item_delete);
             holder.desc = (TextView) convertView.findViewById(R.id.search_doc_item_text);
             convertView.setTag(holder);
         } else {
             holder = (Holder) convertView.getTag();
         }
         HashMap<String,String> data=datas.get(position);
         holder.desc.setText(data.get("name"));
         holder.img.setVisibility(View.VISIBLE);
         holder.img.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 if(del!=null){
                     del.onDeleteItem(position);
                 }
             }
         });

         return convertView;
     }

     class Holder {
         public TextView img;
         public TextView desc;
     }

    public interface OnClickDeleteHistoryListener{
        public void onDeleteItem(int pos);
    }

}
