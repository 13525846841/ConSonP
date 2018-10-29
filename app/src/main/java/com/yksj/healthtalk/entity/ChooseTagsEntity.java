package com.yksj.healthtalk.entity;


import android.os.Parcel;
import android.os.Parcelable;
/**
 * 个人资料选择标签的实体类
 * @author Administrator
 */
  public class ChooseTagsEntity implements Parcelable {
 
	private String id;
	private String name;
	private String type;
	private boolean isSelected;
	public ChooseTagsEntity(String id, String name, String type,
			boolean isSelected) {
		super();
		this.id = id;
		this.name = name;
		this.type = type;
		this.isSelected = isSelected;
	}
	public ChooseTagsEntity(String id, String name, String type
			) {
		this.id = id;
		this.name = name;
		this.type = type;
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
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeString(this.id);
		dest.writeString(this.name);
		dest.writeString(this.type);
	}
	
	// 添加一个静态成员,名为CREATOR,该对象实现了Parcelable.Creator接口
	public static final Parcelable.Creator<ChooseTagsEntity> CREATOR = new Creator<ChooseTagsEntity>() {

		@Override
		public ChooseTagsEntity[] newArray(int size) {
			return new ChooseTagsEntity[size];
		}

		@Override
		public ChooseTagsEntity createFromParcel(Parcel source) {
			return new ChooseTagsEntity(source.readString(), source.readString(), source.readString());
		}
	};
	
	
}
