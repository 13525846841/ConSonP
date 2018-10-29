package com.yksj.healthtalk.entity;

import java.io.Serializable;

import android.os.Parcel;
import android.os.Parcelable;


public class InterestWallEntity implements Serializable,Parcelable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String id;
	private String imageUrl;
	private String customerId;//发布图片人的ID
	private String customerNickname;//发布图片人昵称
	private String clientIconBackground;//发布人小头像
	private String time;//时间
	private String pictureName;//图片名称
	private String bigPicturePath;//大图路径
	private String bigPictureWidthPx;//大图宽
	private String BigPictureHeightPx;//大图高
	private String groupId;
	private String pictureUrl;//是否有链接
	private String customerSex;//性别
	private String roleId;//是否是医生
	private int picHeight;
	private int picWidth;
	private int isCollection;//是否收藏  0  未收藏
	/**
	 * 判断属于审核中哪个状态  10-审核中  20-审核成功 30-审核失败
	 */
	
	
	public String getGroupId() {
		return groupId;
	}
	public String getRoleId() {
		return roleId;
	}
	public void setRoleId(String roleId) {
		this.roleId = roleId;
	}
	public String getCUSTOMER_SEX() {
		return customerSex;
	}
	public void setCUSTOMER_SEX(String cUSTOMER_SEX) {
		this.customerSex = cUSTOMER_SEX;
	}
	public int getIsCollection() {
		return isCollection;
	}
	public void setIsCollection(int isCollection) {
		this.isCollection = isCollection;
	}
	public String getCustomerNickname() {
		return customerNickname;
	}
	public void setCustomerNickname(String customerNickname) {
		this.customerNickname = customerNickname;
	}
	public String getClientIconBackground() {
		return clientIconBackground;
	}
	public void setClientIconBackground(String clientIconBackground) {
		this.clientIconBackground = clientIconBackground;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getPictureName() {
		return pictureName;
	}
	public void setPictureName(String pictureName) {
		this.pictureName = pictureName;
	}
	public String getBigPicturePath() {
		return bigPicturePath;
	}
	public void setBigPicturePath(String bigPicturePath) {
		this.bigPicturePath = bigPicturePath;
	}
	public String getBigPictureWidthPx() {
		return bigPictureWidthPx;
	}
	public void setBigPictureWidthPx(String bigPictureWidthPx) {
		this.bigPictureWidthPx = bigPictureWidthPx;
	}
	public String getBigPictureHeightPx() {
		return BigPictureHeightPx;
	}
	public void setBigPictureHeightPx(String bigPictureHeightPx) {
		BigPictureHeightPx = bigPictureHeightPx;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}
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

	public String getPictureUrl() {
		return pictureUrl;
	}
	public void setPictureUrl(String pictureUrl) {
		this.pictureUrl = pictureUrl;
	}

	/**
	 * 图片状态
	 * 处于审核中的哪个状态
	 */
	private String imageState = "";
	
	public String getImageState() {
		return imageState;
	}
	public void setImageState(String imageState) {
		this.imageState = imageState;
	}
	public String getCustomerId() {
		return customerId;
	}
	public void setCustomerId(String customerId) {
		this.customerId = customerId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	@Override
	public int describeContents() {
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(imageUrl);
		dest.writeString(imageState);
		dest.writeString(customerId);
		dest.writeString(groupId);
		dest.writeString(pictureUrl);
		dest.writeInt(picHeight);
		dest.writeInt(picWidth);
		
		dest.writeString(customerNickname);
		dest.writeString(clientIconBackground);
		dest.writeString(time);
		dest.writeString(pictureName);
		dest.writeString(bigPicturePath);
		dest.writeString(bigPictureWidthPx);
		dest.writeString(BigPictureHeightPx);
		dest.writeInt(isCollection);
		dest.writeString(customerSex);
		
	}
	
	public static final Creator<InterestWallEntity> CREATOR = new Creator<InterestWallEntity>() {

		@Override
		public InterestWallEntity createFromParcel(Parcel source) {
			InterestWallEntity entity = new InterestWallEntity();
			entity.setId(source.readString());
			entity.setImageUrl(source.readString());
			entity.setImageState(source.readString());
			entity.setCustomerId(source.readString());
			entity.setGroupId(source.readString());
			entity.setPictureUrl(source.readString());
			entity.setPicHeight(source.readInt());
			entity.setPicWidth(source.readInt());
			entity.setCustomerNickname(source.readString());
			entity.setClientIconBackground(source.readString());
			entity.setTime(source.readString());
			entity.setPictureName(source.readString());
			entity.setBigPicturePath(source.readString());
			entity.setBigPictureWidthPx(source.readString());
			entity.setBigPictureHeightPx(source.readString());
			entity.setIsCollection(source.readInt());
			entity.setCUSTOMER_SEX(source.readString());
			return entity;
		}

		@Override
		public InterestWallEntity[] newArray(int size) {
			return null;
		}
	};
}
