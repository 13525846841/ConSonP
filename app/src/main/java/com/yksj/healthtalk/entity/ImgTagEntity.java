package com.yksj.healthtalk.entity;

/**
 * 通知消息img标签实体类
 * @author zhao
 *
 */
public class ImgTagEntity {
	private String src;//图片
	private String placeHolder;//替代图片
	private int width = 24;//宽度
	private int height = 24;//高度
	public String getSrc() {
		return src;
	}
	public void setSrc(String src) {
		this.src = src;
	}
	public String getPlaceHolder() {
		return placeHolder;
	}
	public void setPlaceHolder(String placeHolder) {
		this.placeHolder = placeHolder;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
}
