package com.yksj.healthtalk.entity;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 分组信息实体
 * 
 * @author crj
 * 
 */
public class MyZoneGroupEntity implements Parcelable, Serializable {

	/**
	 * 兼容
	 */
	private static final long serialVersionUID = 8978004386149113419L;
	/**
	 * 分组id
	 */
	private String classId = "-2";
	private String className;
	/**
	 * 每个馆的唯一标示 2为摄影馆
	 */
	private String shopId;
	/**
	 * 能否修改 1-能修改 0-不能修改
	 */
	private String canInput = "1";
	/**
	 * 是否已经修改 -1为没有修改 0为已经修改
	 */
	private int hasChanged = -1;
	/**
	 * 是否被选中 0为没被选中，1为被选中
	 */
	private int isSelected = 0;
	
	private int appOpen = 0;


	public int getAppOpen() {
		return appOpen;
	}

	public void setAppOpen(int appOpen) {
		this.appOpen = appOpen;
	}

	public int getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(int isSelected) {
		this.isSelected = isSelected;
	}

	public MyZoneGroupEntity() {
	}

	public MyZoneGroupEntity(String classId, String className, String shopId,
			String canInput) {
		this.classId = classId;
		this.className = className;
		this.shopId = shopId;
		this.canInput = canInput;
	}

	public int getHasChanged() {
		return hasChanged;
	}

	public void setHasChanged(int hasChanged) {
		this.hasChanged = hasChanged;
	}

	public String getClassId() {
		return classId;
	}

	public void setClassId(String classId) {
		this.classId = classId;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(classId);
		dest.writeString(className);
		dest.writeString(shopId);
		dest.writeString(this.canInput);
		dest.writeInt(this.hasChanged);
		dest.writeInt(isSelected);
		dest.writeInt(appOpen);
	}

	public String getCanInput() {
		return canInput;
	}

	public void setCanInput(String canInput) {
		this.canInput = canInput;
	}

	public static final Creator<MyZoneGroupEntity> CREATOR = new Creator<MyZoneGroupEntity>() {

		@Override
		public MyZoneGroupEntity createFromParcel(Parcel source) {
			MyZoneGroupEntity entity = new MyZoneGroupEntity();
			entity.setClassId(source.readString());
			entity.setClassName(source.readString());
			entity.setShopId(source.readString());
			entity.setCanInput(source.readString());
			entity.setHasChanged(source.readInt());
			entity.setIsSelected(source.readInt());
			entity.setAppOpen(source.readInt());
			return entity;
		}

		@Override
		public MyZoneGroupEntity[] newArray(int size) {
			return null;
		}
	};

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof MyZoneGroupEntity) {
			MyZoneGroupEntity entity = (MyZoneGroupEntity) o;
			if (entity.className == this.className) {
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
}
