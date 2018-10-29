package com.yksj.healthtalk.entity;

 

/**
 * 症状实体类
 * @author zhao
 *
 */
public class SymptomEntity {
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getParentCode() {
		return parentCode;
	}
	public void setParentCode(int parentCode) {
		this.parentCode = parentCode;
	}
	public boolean isSelected() {
		return isSelected;
	}
	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}
	
	public String getSuperposition() {
		return superposition;
	}
	public void setSuperposition(String superposition) {
		this.superposition = superposition;
	}
	public String getPositionName() {
		return positionName;
	}
	public void setPositionName(String positionName) {
		this.positionName = positionName;
	}
	private int code;
	private String name;
	private int parentCode;
	private boolean isSelected;//是否选中
	private String superposition;//大部位
	private String positionName;//部位名称

	
}
