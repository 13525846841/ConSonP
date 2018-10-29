package com.yksj.consultation.caledar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.yksj.healthtalk.entity.CaledarObject;
import com.yksj.consultation.son.R;

public class SelectCalendarAdapter extends BaseAdapter{
	List<Calendar> mlists =new ArrayList<Calendar>();
	List<String> mSelected =new ArrayList<String>();//已选择的日期
	Map<Calendar,String> maps=new HashMap<Calendar,String>();
	Context con;
	private Calendar date;
	private Resources resources;
	private Calendar time;
	private Drawable busydrawable;//不可约
	private Drawable bookabledrawable;//可约
	private Drawable commondrawable;//普通
	private int writecolor;//白色
	private int commoncolor;//普通
	private int MODE_NUMBER = 1 ;//表示日历单选 多选
	public SelectCalendarAdapter(Context con){
		this.con=con;
		resources = con.getResources();
		time = Calendar.getInstance();
		busydrawable = resources.getDrawable(R.drawable.calader_color_service_busy_selector);
		bookabledrawable = resources.getDrawable(R.drawable.calader_color_service_bookable_selector);
		commondrawable = resources.getDrawable(R.drawable.calader_color_service_common_selector);
		writecolor = resources.getColor(R.color.calader_color_write);
		commoncolor = resources.getColor(R.color.calader_color_common);
		mDateFormat = new SimpleDateFormat("yyyyMMdd");
	}
	private void onboundData(List<Calendar> mlist){
		mlists.clear();
		this.mlists=mlist;
		notifyDataSetChanged();
	}
	
	public void onboundData(List<Calendar> mlists2, ArrayList<String> selected) {
		mlists.clear();
		this.mlists=mlists2;
		this.mSelected=selected;
		notifyDataSetChanged();
	}
	
	@Override
	public int getCount() {
		return mlists.size();
	}

	@Override
	public Object getItem(int position) {
		return mlists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi") @Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if(convertView==null)
		convertView = LayoutInflater.from(con).inflate(R.layout.calader_item, null);
		Button view=(Button) convertView.findViewById(R.id.text);
		CaledarObject object=new CaledarObject();
		Map<Calendar,String> mapTag=new HashMap<Calendar, String>();
		if(getItem(position)==null){
			view.setText("");
			view.setTextColor(Color.BLACK);
			convertView.setVisibility(View.INVISIBLE);
		}else{
			date = (Calendar) getItem(position);
			view.setText(date.get(Calendar.DAY_OF_MONTH)+"");
			view.setTextColor(Color.BLACK);
			object.setDate(date);
			convertView.setVisibility(View.VISIBLE);
		}
		
		if(sameDatetoday(time, date)){//今天
			view.setTextColor(resources.getColor(R.color.calader_color_today));
			view.setBackgroundResource(R.drawable.calader_color_service_today_selector);
			view.setTag(R.id.tag_press, R.drawable.calader_color_service_today_press);
			view.setTag(R.id.tag_normal, R.drawable.calader_color_service_today_normal);
//		}else if(object!=null && object.getFlage().equals(CaledarObject.ilder)){
		}else{
			view.setTextColor(commoncolor);
			view.setBackgroundDrawable(commondrawable);
			view.setTag(R.id.tag_press, R.drawable.calader_color_service_common_press);
			view.setTag(R.id.tag_normal, R.drawable.calader_color_service_common_normal);
		}
		
		
		if(object.getDate() !=null){
			String pre = mDateFormat.format(object.getDate().getTime());
			if(mSelected.contains(pre) || mSelectDate.contains(pre)){
				view.setBackgroundDrawable(busydrawable);
			}else{
				view.setBackgroundDrawable(commondrawable);
			}
		}else{
			view.setBackgroundDrawable(commondrawable);
		}
		
		view.setTag(R.id.tag_normal, R.drawable.calader_color_service_common_normal);
		view.setTag(R.id.tag_press, R.drawable.calader_color_service_busy_press);
		
		convertView.setTag(object);
		return convertView;
	}
	
	View cacheView;
	public void pressDate(View convertView){
		if(MODE_NUMBER != 1) return;
		Button view=(Button) convertView.findViewById(R.id.text);
		if(cacheView != null){
			cacheView.setBackgroundResource((Integer) cacheView.getTag(R.id.tag_normal));
		}
		view.setBackgroundResource((Integer) view.getTag(R.id.tag_press));
		cacheView =view ;
	}
	

	//设置闲  忙
	public void setApplyDate(Map<Calendar,String> dates){
		if(dates==null)return;
		this.maps.clear();
		this.maps.putAll(dates);
		notifyDataSetChanged();
	}
	
	private Calendar compacareDate(Map<Calendar,String> dates,Calendar selectedDate){
		  for (Map.Entry<Calendar,String> entry : dates.entrySet()) {
			  Calendar key = entry.getKey();
			  if(sameDate(key, selectedDate))return key;
			 }
		  return null;
	}
	
	
	  /**
	   * 日期比较是否相同
	   * @param cal
	   * @param selectedDate
	   * @return
	   */
	@SuppressWarnings("deprecation")
	private  boolean sameDate(Calendar cal, Calendar selectedDate) {
		if(cal==null|| selectedDate==null)return false;
		
//		System.out.println("==========="+cal.get(Calendar.YEAR)+"年"+(cal.get(Calendar.MONTH) +1)+"月"+cal.get(Calendar.DAY_OF_MONTH)+"日=="
//				+selectedDate.get(Calendar.YEAR)+"年"+(selectedDate.get(Calendar.MONTH) +1)+"月"+selectedDate.get(Calendar.DAY_OF_MONTH)+"日");
		return cal.get(Calendar.MONTH)+1 == (selectedDate.get(Calendar.MONTH)+1)
				&& cal.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)
				&& cal.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH);
	}
	
	private boolean sameDatetoday(Calendar cal, Calendar selectedDate){
		if(cal==null|| selectedDate==null)return false;
		return cal.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)
				&& cal.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)
				&& cal.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH);
	}
	
	public void setMODEApp(int mODE_NUMBER) {
		//单选 1  否则多选
		this.MODE_NUMBER = mODE_NUMBER;
	}
	
	private List<String> mSelectDate = new ArrayList<String>();
	private SimpleDateFormat mDateFormat;
	public void setDefaultDate(List<String> dates){
		if(dates!=null && dates.size()>0){
			mSelectDate.clear();
			mSelectDate.addAll(dates);
//			notifyDataSetChanged();
		}

		
	}
}
