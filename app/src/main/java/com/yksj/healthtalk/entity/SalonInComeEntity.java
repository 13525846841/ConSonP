package com.yksj.healthtalk.entity;

import java.io.Serializable;
import java.util.ArrayList;


/**
 * 沙龙收入实体类
 * @author Administrator
 *     {   "date": "2013-04-22",
        "income": [
            {
                "changeNum": "51",
                "customerId": "25485",
                "groupId": "",
                "info": "日票(2)"
            },
            {
                "changeNum": "20",
                "customerId": "25485",
                "groupId": "",
                "info": "文件(1)"
            },
            {
                "changeNum": "75",
                "customerId": "25485",
                "groupId": "",
                "info": "音频(2)"
            }
        ]
    }
 */
public class SalonInComeEntity implements Serializable,Comparable<SalonInComeEntity>{
	
	private static final long serialVersionUID = 1L;
//	102110  	沙龙日票赚取
//	102120	 沙龙月票赚取
//	102130	 共享视频赚取
//	102140	 共享图片赚取
//	102150	 共享文件赚取
//	102160	 共享音频赚取
	String date;
	String changeNum;
	String customerId;
	String groupId;
	String info;
	ArrayList<SalonInComeEntity> entities;
	String timeStamp;
	public SalonInComeEntity(String date, String changeNum, String customerId,
			String groupId, String info) {
		super();
		this.date = date;
		this.changeNum = changeNum;
		this.customerId = customerId;
		this.groupId = groupId;
		this.info = info;
		this.timeStamp = timeStamp;
	}
	
	public SalonInComeEntity(){
	}
	
	public ArrayList<SalonInComeEntity> getEntities() {
		return entities;
	}
	public void setEntities(ArrayList<SalonInComeEntity> entities) {
		this.entities = entities;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getChangeNum() {
		return changeNum;
	}
	public void setChangeNum(String changeNum) {
		this.changeNum = changeNum;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getGroupId() {
		return groupId;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
	public String getInfo() {
		return info;
	}
	public void setInfo(String info) {
		this.info = info;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	@Override
	public int compareTo(SalonInComeEntity another) {
		return -(this.date.compareTo(another.getDate()));
	}

}
