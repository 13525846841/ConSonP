package com.yksj.healthtalk.entity;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;

public class ShopImageEntitiy implements Parcelable,Serializable {
	
	/**
	 * 兼容
	 */
	private static final long serialVersionUID = 1L;
	private String picId;
	public int getPicHeight() {
		return picHeight;
	}
	public void setPicHeight(int picHeight) {
		this.picHeight = picHeight;
	}
	public int getPicWidth() {
		return picWidth;
	}
	public void setPicWidth(int picWidth) {
		this.picWidth = picWidth;
	}
	public int getIconHeight() {
		return iconHeight;
	}
	public void setIconHeight(int iconHeight) {
		this.iconHeight = iconHeight;
	}
	public int getIconWidth() {
		return iconWidth;
	}
	public void setIconWidth(int iconWidth) {
		this.iconWidth = iconWidth;
	}
	public int getPicSize() {
		return picSize;
	}
	public void setPicSize(int picSize) {
		this.picSize = picSize;
	}

	private String picTitle;
	private String picDesc;
	/**
	 * 分类id 存于服务器上 用于搜索匹配
	 */
	private String infoLayId;
	/**
	 * 分组id  显示与每个馆中。
	 */
	private String classId;
	/**
	 * 代表这张图片来自哪个馆
	 */
	private String ShopId;
	/**
	 * 是否显示到兴趣墙 
	 */
	private String addInterest;
	/**
	 * 图片地址
	 */
	private String imageAddress;
	/**
	 * 小图标地址
	 */
	private String iconImageAddress;
	
	private int picHeight;
	private int picWidth;
	private int iconHeight;
	private int iconWidth;
	
	private int picSize;
	
	
	public String getPicId() {
		return picId;
	}
	public void setPicId(String picId) {
		this.picId = picId;
	}
	public String getPicTitle() {
		return picTitle;
	}
	public void setPicTitle(String picTitle) {
		this.picTitle = picTitle;
	}
	public String getPicDesc() {
		return picDesc;
	}
	public void setPicDesc(String picDesc) {
		this.picDesc = picDesc;
	}
	public String getInfoLayId() {
		return infoLayId;
	}
	public void setInfoLayId(String infoLayId) {
		this.infoLayId = infoLayId;
	}
	public String getClassId() {
		return classId;
	}
	public void setClassId(String classId) {
		this.classId = classId;
	}
	public String getShopId() {
		return ShopId;
	}
	public void setShopId(String shopId) {
		ShopId = shopId;
	}
	public String getAddInterest() {
		return addInterest;
	}
	public void setAddInterest(String addInterest) {
		this.addInterest = addInterest;
	}
	public String getImageAddress() {
		return imageAddress;
	}
	public void setImageAddress(String imageAddress) {
		this.imageAddress = imageAddress;
	}
	public String getIconImageAddress() {
		return iconImageAddress;
	}
	public void setIconImageAddress(String iconImageAddress) {
		this.iconImageAddress = iconImageAddress;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(ShopId);
		dest.writeString(addInterest);
		dest.writeString(classId);
		dest.writeString(iconImageAddress);
		dest.writeString(imageAddress);
		dest.writeString(infoLayId);
		dest.writeString(picDesc);
		dest.writeString(picId);
		dest.writeString(picTitle);
		dest.writeInt(iconHeight);
		dest.writeInt(iconWidth);
		dest.writeInt(picHeight);
		dest.writeInt(picWidth);
		dest.writeInt(picSize);
	}

	public static final Creator<ShopImageEntitiy> CREATOR = new Creator<ShopImageEntitiy>() {

		@Override
		public ShopImageEntitiy createFromParcel(Parcel source) {
			ShopImageEntitiy entitiy = new ShopImageEntitiy();
			entitiy.setShopId(source.readString());
			entitiy.setAddInterest(source.readString());
			entitiy.setClassId(source.readString());
			entitiy.setIconImageAddress(source.readString());
			entitiy.setImageAddress(source.readString());
			entitiy.setInfoLayId(source.readString());
			entitiy.setPicDesc(source.readString());
			entitiy.setPicId(source.readString());
			entitiy.setPicTitle(source.readString());
			entitiy.setIconHeight(source.readInt());
			entitiy.setIconWidth(source.readInt());
			entitiy.setPicHeight(source.readInt());
			entitiy.setPicWidth(source.readInt());
			entitiy.setPicSize(source.readInt());
			return entitiy;
		}

		@Override
		public ShopImageEntitiy[] newArray(int size) {
			return null;
		}
	};
	
	public boolean equals(Object o) {
		if(o instanceof ShopImageEntitiy) {
			ShopImageEntitiy entitiy = (ShopImageEntitiy) o;
			if(entitiy.getPicId().equals(this.getPicId()))
				return true;
		} 
		return false;
	};
	
}
