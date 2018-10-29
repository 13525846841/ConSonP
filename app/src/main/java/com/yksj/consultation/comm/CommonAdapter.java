package com.yksj.consultation.comm;

import java.util.ArrayList;
import java.util.List;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public abstract class CommonAdapter<T> extends BaseAdapter
{
	protected LayoutInflater mInflater;
	protected Context mContext;
	public List<T> mDatas =new ArrayList<T>() ;
	protected int mItemLayoutId;
	public  CommonAdapter(Context context, List<T> datas)
	{
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		if(datas!=null){
			mDatas.clear();
			mDatas.addAll(datas);
		}
			
	}
	public CommonAdapter(Context context)
	{
		this.mContext = context;
		this.mInflater = LayoutInflater.from(mContext);
		mDatas=new ArrayList<T>();
	}

	@Override
	public int getCount()
	{
		return mDatas.size();
	}

	@Override
	public T getItem(int position)
	{
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position)
	{
		return position;
	}

	public void addAll(List<T> datas){
		if(datas!=null){
			mDatas.addAll(datas);
			notifyDataSetChanged();
		}
	}
	
	public void removeAll(){
		mDatas.clear();
		notifyDataSetChanged();
	}
	
	public void remove(T entity){
		if(entity!=null && mDatas.contains(entity)){
			mDatas.remove(entity);
			notifyDataSetChanged();
		}
	}
	
	public void onBoundData(List<T> datas){
		if(datas!=null){
			mDatas.clear();
			mDatas.addAll(datas);
			notifyDataSetChanged();
		}
	}
	
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent)
	{
		final CommonViewHolder viewHolder = getViewHolder(position, convertView,
				parent);
		onBoundView(viewHolder, getItem(position));
		return viewHolder.getConvertView();

	}

	/**
	 * 绑定数据
	 * @param helper
	 * @param item  对象
	 */
	public abstract void onBoundView(CommonViewHolder helper, T item);
	
	/**
	 * item layout 
	 * @return
	 */
	public abstract int viewLayout();

	private CommonViewHolder getViewHolder(int position, View convertView,
			ViewGroup parent)
	{
		this.mItemLayoutId = viewLayout();
		return CommonViewHolder.get(mContext, convertView, parent, mItemLayoutId,
				position);
	}

}
