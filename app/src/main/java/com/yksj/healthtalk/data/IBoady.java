package com.yksj.healthtalk.data;

public interface IBoady {
	public static final int positionLimbs = 14;//四肢
	public static final int boadyBehindDirection = 1;//后部
	public static final int boadyFrontDirection = 0;//前部
	public static final int positionHeader = 0;//头部
	
	public static final int positionNeck = 7;//颈部
	public static final int positionHand = 1;//手
	public static final int positionLeg = 2;//腿
	public static final int positionChest = 3;//胸部
	public static final int positionPrivateParts = 4;//阴部
	public static final int positionBack = 5;//背部
	public static final int positionAss = 6;//屁股
	
	public static final int positionNose = 8;//鼻子
	public static final int positionEar = 9;//耳朵
	public static final int positionEye = 10;//眼睛
	public static final int positionMouth = 11;//嘴巴
	public static final int positionWaist = 12;//腰部
	public static final int positionAbdomen = 13;//腹部
	/**
	 * 背部身体点击
	 * @param x
	 * @param y
	 * @return
	 */
	public int isRectCollisionBehindBoady(float x ,float y);
	/**
	 * 前部身体点击
	 * @param x
	 * @param y
	 * @return
	 */
	public int isRectCollisionFrontBoady(float x,float y);
	/**
	 * 是否点击在身体之上
	 * @param direction
	 * @param x
	 * @param y
	 * @return
	 */
	public int isRectCollisionBoady(int direction,float x,float y);
	
	/**
	 * 头部区域检测
	 * @param x
	 * @param y
	 * @return
	 */
	public int isRectCollisionHeader(float x,float y);
}
