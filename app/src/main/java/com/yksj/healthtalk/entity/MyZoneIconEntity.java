package com.yksj.healthtalk.entity;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 图标实体类
 * @author crj
 *
 */
public class MyZoneIconEntity implements Parcelable,Comparable<MyZoneIconEntity>,Serializable{
	
	/**
	 * 兼容
	 */
	private static final long serialVersionUID = 0L;
	/**
	 * 图标id
	 */
	private String id;
	/**
	 * 图标名称
	 */
	private String name;
	/**
	 * 图标类型，1为必选 0为可选
	 */
	private String type;
	/**
	 * 图标排序位置
	 */
	private String sequence;
	/**
	 * 图标图片
	 */
	private int IconImageId;
	
	public int getIconImageId() {
		return IconImageId;
	}

	public void setIconImageId(int iconImageId) {
		IconImageId = iconImageId;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.name);
		dest.writeString(this.type);
		dest.writeString(this.sequence);
		dest.writeInt(this.IconImageId);
	}

	public static final Parcelable.Creator<MyZoneIconEntity> CREATOR = new Creator<MyZoneIconEntity>() {

		@Override
		public MyZoneIconEntity createFromParcel(Parcel source) {
			MyZoneIconEntity entity = new MyZoneIconEntity();
			entity.setId(source.readString());
			entity.setName(source.readString());
			entity.setType(source.readString());
			entity.setSequence(source.readString());
			entity.setIconImageId(source.readInt());
			return entity;
		}

		@Override
		public MyZoneIconEntity[] newArray(int size) {
			return null;
		}
	};

	@Override
	public int compareTo(MyZoneIconEntity another) {
		if(Integer.parseInt(this.sequence)>Integer.parseInt(another.sequence))
			return 1;
		else if(Integer.parseInt(this.sequence)<Integer.parseInt(another.sequence))
			return -1;
		return 0;
	}
	
	@Override
	public boolean equals(Object o) {
		if(o instanceof MyZoneIconEntity) {
			MyZoneIconEntity entity = (MyZoneIconEntity) o;
			if(this.getName().equals(entity.getName())) {
				return true;
			} else {
				return false;
			}
		}
		return super.equals(o);
	}

}
