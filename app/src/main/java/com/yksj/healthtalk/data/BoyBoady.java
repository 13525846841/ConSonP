package com.yksj.healthtalk.data;

import android.graphics.RectF;

public class BoyBoady implements IBoady  {
	
	RectF headerRectF = new RectF(77.00f,113.00f,179.95f, 212.10f);//头部
	
	RectF leftLegRectF = new RectF(81.00f, 423.10f, 181.55f, 615.15f);//左腿
	RectF rightLegRectF = new RectF(81.00f, 423.10f, 181.55f, 615.15f);//右腿
	RectF leftHandRectF = new RectF(23.50f,253.00f,80.00f, 428.50f);//左边手
	RectF rightHandRectF = new RectF(186.00f, 253.00f,242.00f,428.50f);//右边手
	RectF chestRectF = new RectF(86.05f, 255.70f, 179.15f, 324.25f);//胸部 背部
	RectF waistRectF = new RectF(86.05f,327.00f,177.25f,367.15f);//腰部
	RectF abdomenRectF = new RectF(86.05f,327.00f,177.25f,367.15f);//腹部
	RectF assRectF = new RectF(83.20f, 369.50f,179.75f, 420.00f);//屁股
	RectF privatePartsRectF = new RectF(83.20f, 369.50f,179.75f, 420.00f);//阴部
	
	BoyHeader manHeader = new BoyHeader();
	
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
	 * 头部检测
	 */
	public int isRectCollisionHeader(float x,float y){
		return manHeader.isRectCollisionHeader(x, y);
	}

}
