package com.yksj.consultation.son.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;

import com.yksj.consultation.son.R;


public class PullToRefreshExpandableListView extends PullToRefreshAdapterViewBase<ExpandableListView>{

	class InternalExpandableListView extends ExpandableListView implements EmptyViewMethodAccessor {

		public InternalExpandableListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		public InternalExpandableListView(Context context, AttributeSet attrs,
				int defStyle) {
			super(context, attrs, defStyle);
		}

		public InternalExpandableListView(Context context) {
			super(context);
		}

		@Override
		public void setEmptyView(View emptyView) {
			PullToRefreshExpandableListView.this.setEmptyView(emptyView);
		}

		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}

		public ContextMenuInfo getContextMenuInfo() {
			return super.getContextMenuInfo();
		}
	}

	public PullToRefreshExpandableListView(Context context) {
		super(context);
		this.setDisableScrollingWhileRefreshing(true);
	}

	public PullToRefreshExpandableListView(Context context, int mode) {
		super(context, mode);
		this.setDisableScrollingWhileRefreshing(true);
	}

	public PullToRefreshExpandableListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		this.setDisableScrollingWhileRefreshing(true);
	}
	
	public void notifyDataSetInvalidated(){
		ExpandableListView expandableListView = getRefreshableView();
		BaseExpandableListAdapter  baseExpandableListAdapter = (BaseExpandableListAdapter)expandableListView.getExpandableListAdapter();
		baseExpandableListAdapter.notifyDataSetInvalidated();
		baseExpandableListAdapter.notifyDataSetChanged();
	}

	@Override
	protected final ExpandableListView createRefreshableView(Context context, AttributeSet attrs) {
		//return new InternalExpandableListView(context);
		ExpandableListView expand = (ExpandableListView) LayoutInflater.from(context).inflate(R.layout.main_expandablelistview, null);
		return expand;
	}

}
