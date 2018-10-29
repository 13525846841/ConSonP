package com.yksj.consultation.caledar;

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

public class CalendarAdapter extends BaseAdapter{
	List<Calendar> mlists =new ArrayList<Calendar>();
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
	public CalendarAdapter(Context con){
		this.con=con;
		resources = con.getResources();
		time = Calendar.getInstance();
		busydrawable = resources.getDrawable(R.drawable.calader_color_service_busy_selector);
		bookabledrawable = resources.getDrawable(R.drawable.calader_color_service_bookable_selector);
		commondrawable = resources.getDrawable(R.drawable.calader_color_service_common_selector);
		writecolor = resources.getColor(R.color.calader_color_write);
		commoncolor = resources.getColor(R.color.calader_color_common);
	}
	public void onboundData(List<Calendar> mlist){
		mlists.clear();
		this.mlists=mlist;
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
		if(maps.size()!=0){
			Calendar calendar = compacareDate(maps, date);
			if(calendar!=null){
				String isbusy = maps.get(calendar);//闲  忙
				mapTag.put(date, isbusy);
				if(CaledarObject.busy.equals(isbusy)){//忙
					view.setBackgroundDrawable(busydrawable);
					view.setTag(R.id.tag_press, R.drawable.calader_color_service_busy_press);
					view.setTag(R.id.tag_normal, R.drawable.calader_color_service_busy_normal);
					object.setFlage(CaledarObject.busy);
					view.setTextColor(writecolor);
//					System.out.println("==========="+calendar.get(Calendar.MONTH) + "月" +calendar.get(Calendar.DAY_OF_MONTH)+"日"+"----约满");
				}else if(CaledarObject.noBusy.equals(isbusy)){//闲
					object.setFlage(CaledarObject.noBusy);
					view.setBackgroundDrawable(bookabledrawable);
					view.setTag(R.id.tag_press, R.drawable.calader_color_service_bookable_press);
					view.setTag(R.id.tag_normal, R.drawable.calader_color_service_bookable_normal);
					view.setTextColor(writecolor);
//					System.out.println("==========="+calendar.get(Calendar.MONTH)+1 + "月" +calendar.get(Calendar.DAY_OF_MONTH)+"日"+"----可约");
				}else{//没有任何预约
					object.setFlage(CaledarObject.ilder);
					view.setBackgroundDrawable(commondrawable);
					view.setTag(R.id.tag_press, R.drawable.calader_color_service_common_press);
					view.setTag(R.id.tag_normal, R.drawable.calader_color_service_common_normal);
//					System.out.println("==========="+calendar.get(Calendar.MONTH) + "月" +calendar.get(Calendar.DAY_OF_MONTH)+"日"+"----没有");
				}
			}else{
				object.setFlage(CaledarObject.ilder);
				view.setTextColor(commoncolor);
				view.setBackgroundDrawable(commondrawable);
				view.setTag(R.id.tag_press, R.drawable.calader_color_service_common_press);
				view.setTag(R.id.tag_normal, R.drawable.calader_color_service_common_normal);
//				System.out.println("==========="+date.get(Calendar.MONTH) + "月" +date.get(Calendar.DAY_OF_MONTH)+"日"+"----没有");
			}
		}else{
			object.setFlage(CaledarObject.ilder);
			view.setTextColor(commoncolor);
			view.setBackgroundDrawable(commondrawable);
			view.setTag(R.id.tag_press, R.drawable.calader_color_service_common_press);
			view.setTag(R.id.tag_normal, R.drawable.calader_color_service_common_normal);
		}
		
		if(sameDatetoday(time,date) && object.getFlage().equals(CaledarObject.busy)){//当天  并且 忙
			view.setTextColor(resources.getColor(R.color.calader_color_today));
			view.setBackgroundDrawable(busydrawable);
			view.setTag(R.id.tag_press, R.drawable.calader_color_service_busy_press);
			view.setTag(R.id.tag_normal, R.drawable.calader_color_service_busy_normal);
		}else if(sameDatetoday(time,date) && object.getFlage().equals(CaledarObject.noBusy)){//当天  并且 闲
			view.setTextColor(resources.getColor(R.color.calader_color_today));
			view.setBackgroundDrawable(bookabledrawable);
			view.setTag(R.id.tag_press, R.drawable.calader_color_service_bookable_press);
			view.setTag(R.id.tag_normal, R.drawable.calader_color_service_bookable_normal);
		}else if(sameDatetoday(time, date)){//今天
			view.setTextColor(resources.getColor(R.color.calader_color_today));
			view.setBackgroundResource(R.drawable.calader_color_service_today_selector);
			view.setTag(R.id.tag_press, R.drawable.calader_color_service_today_press);
			view.setTag(R.id.tag_normal, R.drawable.calader_color_service_today_normal);
		}else if(object!=null && object.getFlage().equals(CaledarObject.ilder)){
			view.setTextColor(commoncolor);
			view.setBackgroundDrawable(commondrawable);
			view.setTag(R.id.tag_press, R.drawable.calader_color_service_common_press);
			view.setTag(R.id.tag_normal, R.drawable.calader_color_service_common_normal);
		}
		
		convertView.setTag(object);
//		convertView.setBackgroundColor(Color.TRANSPARENT);
		return convertView;
	}
	
	View cacheView;
	public void pressDate(View convertView){
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

}
