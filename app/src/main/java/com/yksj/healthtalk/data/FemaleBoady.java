package com.yksj.healthtalk.data;


import android.graphics.RectF;
/**
 * 女性身体部位坐标结果实体
 * @author zhao
 */
public class FemaleBoady implements IBoady {
/*	public static final int positionLimbs = 14;//四肢
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
*/	
	RectF headerRectF = new RectF(101.00f, 0f, 167.05f, 87.05f);//头部
	RectF leftLegRectF = new RectF(77.00f, 342.10f, 127.05f, 628.15f);//左腿
	RectF rightLegRectF = new RectF(141.05f, 342.10f,190.40f,628.15f);//右腿
	
	RectF leftHandRectF = new RectF(7.00f,109.05f,75.00f, 362.10f);//左边手
	RectF rightHandRectF = new RectF(191.05f, 109.05f,265.05f,361.10f);//右边手
	
	RectF chestRectF = new RectF(86.50f,122.50f,181.50f, 188.50f);//胸部 
	RectF backRectF = new RectF(77.00f, 115.00f,188.50f, 207.55f);//背部
	
	RectF waistRectF = new RectF(91.00f,208.55f,179.00f,249.05f);//腰部
	
	RectF assRectF = new RectF(77.00f, 251.10f, 189.50f, 340.10f);//屁股
	
	RectF abdomenRectF = new RectF(81.50f,190.00f,189.00f,279.70f);//腹部
	RectF privatePartsRectF = new RectF(116.00f,282.00f,143.70f,322.90f);//阴部
	RectF neckRectF = new RectF(106.50f,87.05f,163.50f,114.00f);//颈部
	FemaleHeader femaleHeader = new FemaleHeader();
	
	public int isRectCollisionBoady(int deration,float x ,float y){
		//向前
		if(deration == boadyFrontDirection){
			return isRectCollisionFrontBoady(x,y);
		//向后	
		}else{
			return isRectCollisionBehindBoady(x,y);
		}
	}
	
	/**
	 * 前部碰撞检测
	 * @param x
	 * @param y
	 */
	public int isRectCollisionFrontBoady(float x,float y){
		if(headerRectF.contains(x, y)){
			return positionHeader;
		}else if(leftHandRectF.contains(x, y) || rightHandRectF.contains(x, y) || leftLegRectF.contains(x, y) || rightLegRectF.contains(x, y)){
			return positionLimbs;
		}else if(chestRectF.contains(x,y)){
			return positionChest;
		}else if(abdomenRectF.contains(x, y)){
			return positionAbdomen;
		}else if(privatePartsRectF.contains(x, y)){
			return positionPrivateParts;
		}
		return -1;
	}
	
	/**
	 * 后部碰撞检测
	 * @param x
	 * @param y
	 */
	public int isRectCollisionBehindBoady(float x ,float y){
		if(chestRectF.contains(x,y)){
			return positionBack;
		}else if(waistRectF.contains(x,y)){
			return positionWaist;
		}else if(assRectF.contains(x,y)){
			return positionAss;
		}else if(headerRectF.contains(x,y)){//头部
			return positionHeader;
		}else if(leftHandRectF.contains(x, y) || rightHandRectF.contains(x, y) || leftLegRectF.contains(x, y) || rightLegRectF.contains(x, y)){
			return positionLimbs;
		}
		return -1;
	}
	
	/**
	 * 头部检测
	 */
	public int isRectCollisionHeader(float x,float y){
		return femaleHeader.isRectCollisionHeader(x, y);
	}
	
}
