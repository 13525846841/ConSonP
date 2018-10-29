package com.yksj.consultation.son.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View;
import android.widget.ListView;


public class PullToRefreshListView extends PullToRefreshAdapterViewBase<ListView> {


	final class InternalListView extends ListView implements EmptyViewMethodAccessor {

		public InternalListView(Context context, AttributeSet attrs) {
			super(context, attrs);
		}

		@Override
		public void setEmptyView(View emptyView) {
			PullToRefreshListView.this.setEmptyView(emptyView);
		}
		
		@Override
		public void setEmptyViewInternal(View emptyView) {
			super.setEmptyView(emptyView);
		}

		public ContextMenuInfo getContextMenuInfo() {
			return super.getContextMenuInfo();
		}
	}

	public PullToRefreshListView(Context context) {
		super(context);
		//this.setDisableScrollingWhileRefreshing(true);
	}
	
	public PullToRefreshListView(Context context, int mode) {
		super(context, mode);
		//this.setDisableScrollingWhileRefreshing(true);
	}

	public PullToRefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		//this.setDisableScrollingWhileRefreshing(true);
	}

	@Override
	public ContextMenuInfo getContextMenuInfo() {
		return ((InternalListView) getRefreshableView()).getContextMenuInfo();
	}


	@Override
	public  ListView createRefreshableView(Context context, AttributeSet attrs) {
		ListView lv = new InternalListView(context, attrs);
//		ListView lv = new ListView(context, attrs);
		lv.setId(android.R.id.list);
		return lv;
	}


}
