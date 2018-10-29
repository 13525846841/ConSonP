package com.yksj.consultation.caledar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.yksj.healthtalk.entity.CaledarObject;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.views.TagsGridView;
import com.yksj.healthtalk.utils.TimeUtil;
/**
 * 日历适配器view
 * 用于日历的选择类型
 * @author jack_tang
 *
 */
public class SelectCaledarViewDialogFragment extends DialogFragment implements OnClickListener{
	private int number = 0;
    public SelectCalendarAdapter adapter;
	private TextView dateDisplyview;
	private static  int MODE_NUMBER = 1;
	OnItemClickCaladerListener listener;
	OnBackDataListener backlistener;
	private int newMonth ;//当前月份  
	private ArrayList<String> mSelected = new ArrayList<String>();
	
    public interface OnBackDataListener {
        void onBackData(List<String> str);
    }
    
    public void setOnBackDataListener(OnBackDataListener backlistener) {
    	this.backlistener = backlistener;
    }
	
    public interface OnItemClickCaladerListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }
    
    public void setOnItemClickListener(OnItemClickCaladerListener listener) {
    	this.listener = listener;
    }
    
	final DisplayMetrics metrics = new DisplayMetrics();

	public static void onShowDialog(FragmentManager manager) {
		new SelectCaledarViewDialogFragment().show(manager, "search");
	}

//	List<String> dates =new ArrayList<String>();
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
//		dates.clear();
		mSelected.clear();
		mSelected.addAll(getArguments().getStringArrayList("data"));
//		mSelected.add
	}
    
	
	/**
	 * @param manager
	 * @param content 文字内容
	 */
	public static SelectCaledarViewDialogFragment showLodingDialog(FragmentManager manager,int mode,List<String> dates){
		if(!manager.isDestroyed()){
			Fragment fragment = manager.findFragmentByTag("loading");
			FragmentTransaction ft = manager.beginTransaction();
			if(fragment != null){
				ft.remove(fragment);
			}
			Bundle bundle = new Bundle();
			bundle.putStringArrayList("data", (ArrayList<String>) dates);
			SelectCaledarViewDialogFragment dialog = new SelectCaledarViewDialogFragment();
			dialog.setArguments(bundle);
			ft.add(dialog,"loading");
			MODE_NUMBER = mode;
			ft.commitAllowingStateLoss();
			return dialog;
		}else{
			return null;
		}
	}
	
	/**
	 * @param manager
	 * @param content 文字内容
	 */
	public static SelectCaledarViewDialogFragment showLodingDialog(FragmentManager manager,int mode){
		if(!manager.isDestroyed()){
			Fragment fragment = manager.findFragmentByTag("loading");
			FragmentTransaction ft = manager.beginTransaction();
			if(fragment != null){
				ft.remove(fragment);
			}
			Bundle bundle = new Bundle();
			bundle.putStringArrayList("data",new ArrayList<String>());
			SelectCaledarViewDialogFragment dialog = new SelectCaledarViewDialogFragment();
			dialog.setArguments(bundle);
			ft.add(dialog,"loading");
			MODE_NUMBER = mode;
			ft.commitAllowingStateLoss();
			return dialog;
		}else{
			return null;
		}
	}

	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Dialog dialog = new Dialog(getActivity(),R.style.dialog);
		dialog.setContentView(R.layout.selecte_dialog_fragment_layout);
        dateDisplyview = (TextView) dialog.findViewById(R.id.month);
        TagsGridView mView= (TagsGridView)  dialog.findViewById(R.id.view);
       adapter = new SelectCalendarAdapter(getActivity());
       adapter.setDefaultDate(mSelected);
       mView.setAdapter(adapter);
       adapter.setMODEApp(MODE_NUMBER);
       mView.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			CaledarObject descriptor= (CaledarObject) view.getTag();
			SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMdd");
			String presss = mDateFormat.format(descriptor.getDate().getTime());
			//判断选择的日期是否有效
			if(Integer.valueOf(presss)< Integer.valueOf(TimeUtil.getTime().substring(0, 8))){
				return ;
			}
				 
			
			if(listener!=null)
			listener.onItemClick(parent, view, position, id);
			adapter.pressDate(view);
			
			view =view.findViewById(R.id.text);;
			if(MODE_NUMBER == 1){
				mSelected.clear();
				mSelected.add(presss);
			}else{
				if(mSelected.contains(presss)){
					mSelected.remove(presss);
					view.setBackgroundResource((Integer) view.getTag(R.id.tag_normal));
				}else{
					view.setBackgroundResource((Integer) view.getTag(R.id.tag_press));
					mSelected.add(presss);
				}
			}
		}
       });
       dialog.findViewById(R.id.prin).setOnClickListener(this);
       dialog.findViewById(R.id.next).setOnClickListener(this);
		return dialog;
	}
	
	@Override
	public void onCancel(DialogInterface dialog) {
		super.onCancel(dialog);
		backlistener.onBackData(mSelected);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	       getDatas();
	}
	
    private void getDatas(){
    	Calendar instance = Calendar.getInstance();
		instance.add(Calendar.MONTH,number);
		int actualMaximum = instance.getActualMaximum(Calendar.DAY_OF_MONTH);
		dateDisplyview.setText(instance.get(Calendar.YEAR)+"年"+(instance.get(Calendar.MONTH)+1)+"月");
		newMonth = instance.get(Calendar.MONTH)+1;
		instance.set(Calendar.DATE,1);//从一号开始
		int j = instance.get(Calendar.DAY_OF_WEEK)-1;
		List<Calendar> mlists =new ArrayList<Calendar>();
		for (int i = 0; i <j; i++) {
			mlists.add(null);
		}
		Calendar calendar;
		for (int i = 0; i < actualMaximum; i++) {
			calendar=Calendar.getInstance();
			calendar.set(instance.get(Calendar.YEAR), instance.get(Calendar.MONTH), instance.get(Calendar.DAY_OF_MONTH));
			mlists.add(calendar);
			instance.add(Calendar.DATE,1);
		}
		adapter.onboundData(mlists,mSelected);
    }


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.prin:
			number--;
			getDatas();
			break;
		case R.id.next:
//		int dat =Calendar.getInstance().get(Calendar.MONTH)+1;3116523
			number++;
			getDatas();
			break;
		}
	}
	
	
	/**
	 * 添加闲忙
	 * @param dates
	 */
	public void addApplyDate(Map<Calendar,String> dates){
		adapter.setApplyDate(dates);
	}

	/**
	 * 删除某个时间段
	 * @param key
	 */
	public void removeDate(Calendar key){
		if(key == null) return;
		adapter.maps.remove(key);
		adapter.notifyDataSetChanged();
	}
}
