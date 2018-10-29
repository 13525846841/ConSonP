package com.yksj.consultation.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.yksj.consultation.son.listener.OnRecyclerClickListener;

import java.util.List;

/**
 * Created by hekl on 18/5/3.
 */

public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter<T>.ViewHolder> {

    public List<T> list;
    public Context context;
    private int type=0;
    private OnRecyclerClickListener mOnRecyclerClickListener;

    public void setmOnRecyclerClickListener(OnRecyclerClickListener mOnRecyclerClickListener) {
        this.mOnRecyclerClickListener = mOnRecyclerClickListener;
    }

    public BaseRecyclerAdapter(List<T> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public BaseRecyclerAdapter<T>.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(View.inflate(context,returnLayout(),null));
    }


    public void onBindViewHolder(BaseRecyclerAdapter<T>.ViewHolder holder, int position) {
         onBaseBindViewHolder(holder,position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(final View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnRecyclerClickListener.onRecyclerItemClickListener(getAdapterPosition(),itemView,type);
                }
            });
        }
    }
    public void setRecyclerType(int type) {
        this.type=type;
    }

    public abstract int returnLayout();
    public abstract void onBaseBindViewHolder(BaseRecyclerAdapter<T>.ViewHolder holder, int position);
}
