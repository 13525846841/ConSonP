package com.yksj.healthtalk.data;

import android.graphics.RectF;

/**
 * 
 *男性身体部位坐标结构图
 * @author zhao
 *
 */
public class ManBoady implements IBoady {
	/*public int positionLimbs = 14;//四肢
	public int boadyBehindDirection = 1;//后部
	public int boadyFrontDirection = 0;//前部
	public int positionHeader = 0;//头部
	
	public int positionNeck = 7;//颈部
	public int positionHand = 1;//手
	public int positionLeg = 2;//腿
	public int positionChest = 3;//胸部
	public int positionPrivateParts = 4;//阴部
	public int positionBack = 5;//背部
	public int positionAss = 6;//屁股
	
	public int positionNose = 8;//鼻子
	public int positionEar = 9;//耳朵
	public int positionEye = 10;//眼睛
	public int positionMouth = 11;//嘴巴
	public int positionWaist = 12;//腰部
	public int positionAbdomen = 13;//腹部
*/	
	
	RectF headerRectF = new RectF(106f, 0f, 175.05f, 103.95f);//头部
	RectF leftLegRectF = new RectF(64.55f, 355.10f, 138.00f, 621.05f);//左腿
	RectF rightLegRectF = new RectF(143.60f, 355.10f, 217.60f, 621.05f);//右腿
	
	RectF leftHandRectF = new RectF(8.45f,110.15f,73.90f, 355.10f);//左边手
	RectF rightHandRectF = new RectF(207.65f, 110.15f,272.15f,355.10f);//右边手
	RectF chestRectF = new RectF(75.40f, 105.95f, 201.65f, 208.95f);//胸部 背部
	RectF waistRectF = new RectF(84.35f,213.20f,197.10f,258.20f);//腰部
	RectF assRectF = new RectF(72.45f, 259.35f, 207.65f, 347.35f);//屁股
	RectF abdomenRectF = new RectF(84.35f,213.20f, 197.85f,284.05f);//腹部
	RectF privatePartsRectF = new RectF(112.00f,283.05f, 173.05f, 329.05f);//阴部
	ManHeader manHeader = new ManHeader();
	
	/**
	 * 判断坐标是否在范围内
	 * @param direction 前部0,后部1
	 * @param x
	 * @param y
	 */
	public int isRectCollisionBoady(int direction,float x,float y){
		//后部
		if(boadyBehindDirection == direction){
			return isRectCollisionBehindBoady(x,y);
		}else{
			return isRectCollisionFrontBoady(x,y);
		}
	}
	
	/**
	 * 前部碰撞检测
	 * @param x
	 * @param y
	 */
	public int isRectCollisionFrontBoady(float x,float y){
		if(headerRectF.contains(x, y)){
			//System.out.println("头部");
			return positionHeader;
		}else if(leftHandRectF.contains(x, y) || rightHandRectF.contains(x, y) || leftLegRectF.contains(x, y) || rightLegRectF.contains(x, y)){
			//System.out.println("四肢部位");
			return positionLimbs;
		}else if(chestRectF.contains(x,y)){
			//System.out.println("胸部 背部");
			return positionChest;
		}else if(abdomenRectF.contains(x, y)){
			//System.out.println("腹部");
			return positionAbdomen;
		}else if(privatePartsRectF.contains(x, y)){
			//System.out.println("阴部");
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
		}else if(headerRectF.contains(x, y)){//头部
			return positionHeader;
		}else if(leftHandRectF.contains(x, y) || rightHandRectF.contains(x, y) || leftLegRectF.contains(x, y) || rightLegRectF.contains(x, y)){
			return positionLimbs;
		}
		return -1;
	}
	
	/**
	 * 后部碰撞检测
	 * @param x
	 * @param y
	 */
/*	public int isRectCollisionBehindBoady(float x ,float y){
		if(chestRectF.contains(x,y)){
			//System.out.println("背部");
		}else if(waistRectF.contains(x,y)){
			//System.out.println("腰部");
		}else if(assRectF.contains(x,y)){
			//System.out.println("屁股");
		}
		return 0;
	}*/
	
	/**
	 * 头部检测
	 */
	public int isRectCollisionHeader(float x,float y){
		return manHeader.isRectCollisionHeader(x, y);
	}
	
	
}
