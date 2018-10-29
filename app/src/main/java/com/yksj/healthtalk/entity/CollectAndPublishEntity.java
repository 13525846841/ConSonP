package com.yksj.healthtalk.entity;

public class CollectAndPublishEntity {


	public String MENUNAME;
	public String MENUTIME;
	public String MENUCODE;
	public String pictureName;
	public String time;
	public String consultationId;
	public String infoId;
	public String pictureID;
	public boolean isCheck;
	
	
	 @Override
	 public boolean equals(Object obj) {
	  if(obj instanceof CollectAndPublishEntity){
		  CollectAndPublishEntity city = (CollectAndPublishEntity) obj;
	   return true;
	  }
	  return false;
	}
}
