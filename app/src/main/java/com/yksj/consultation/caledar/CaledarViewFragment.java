package com.yksj.consultation.caledar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

import com.yksj.healthtalk.entity.CaledarObject;
import com.yksj.consultation.comm.RootFragment;
import com.yksj.consultation.comm.SingleBtnFragmentDialog;
import com.yksj.consultation.son.R;
import com.yksj.consultation.son.views.TagsGridView;
import com.yksj.healthtalk.utils.TimeUtil;

/**
 * 日历适配器view
 * @author jack_tang
 *
 */
public class CaledarViewFragment extends RootFragment implements OnClickListener{
	private int number = 0;
    private CalendarAdapter adapter;
	private TextView dateDisplyview;
	OnItemClickCaladerListener listener;
    public interface OnItemClickCaladerListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }
    
    public void setOnItemClickListener(OnItemClickCaladerListener listener) {
    	this.listener = listener;
    }
    
	@Override
	public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
	   View layout_view = inflater.inflate(R.layout.caledar_view_fragment_layout,null);
       dateDisplyview = (TextView) layout_view.findViewById(R.id.month);
		TagsGridView mView= (TagsGridView)  layout_view.findViewById(R.id.view);
       adapter = new CalendarAdapter(getActivity());
       mView.setAdapter(adapter);
       mView.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			CaledarObject descriptor= (CaledarObject) view.getTag();
			SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyyMMdd");
			String presss = mDateFormat.format(descriptor.getDate().getTime());
			//判断选择的日期是否有效
			if(Integer.valueOf(presss)< Integer.valueOf(TimeUtil.getTime().substring(0, 8))){
				SingleBtnFragmentDialog.showDefault(getChildFragmentManager(), "您选择的日期已过期!");
				return ;
			}
			
			if(listener!=null)
			listener.onItemClick(parent, view, position, id);
			adapter.pressDate(view);
		}
	});
       layout_view.findViewById(R.id.prin).setOnClickListener(this);
       layout_view.findViewById(R.id.next).setOnClickListener(this);
		return layout_view;
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
		adapter.onboundData(mlists);
    }

	private  String getWeekDay(Calendar c){
	    if(Calendar.MONDAY == c.get(Calendar.DAY_OF_WEEK)){
	     return "星期一";//2
	    }
	    if(Calendar.TUESDAY == c.get(Calendar.DAY_OF_WEEK)){
	     return "星期二";//3
	    }
	    if(Calendar.WEDNESDAY == c.get(Calendar.DAY_OF_WEEK)){
	     return "星期三";//4
	    }
	    if(Calendar.THURSDAY == c.get(Calendar.DAY_OF_WEEK)){
	     return "星期四";//5
	    }
	    if(Calendar.FRIDAY == c.get(Calendar.DAY_OF_WEEK)){
	     return "星期五";//6
	    }
	    if(Calendar.SATURDAY == c.get(Calendar.DAY_OF_WEEK)){
	     return "星期六";//7
	    }
	    if(Calendar.SUNDAY == c.get(Calendar.DAY_OF_WEEK)){
	     return "星期日";//1
	    }
	    return "星期一";
	 }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.prin:
			number--;
			getDatas();
			break;
		case R.id.next:
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
