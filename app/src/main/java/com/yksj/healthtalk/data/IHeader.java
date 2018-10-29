package com.yksj.healthtalk.data;

public interface IHeader {
	public static int positionEye = 20;//眼睛
	public static int positionNose = 15;//鼻子
	public static int positionEar = 16;//耳朵
	public static int positionMouth = 17;//嘴巴
	public static int positionBrain = 18;//脑
	public static int positionThroat = 19;//咽喉
	public static int positionNeck = 7;//颈部
	public static final int positionFace = 21;//脸部
	/**
	 * 头部检测
	 * @param x
	 * @param y
	 * @return
	 */
	public int isRectCollisionHeader(float x,float y);
}
