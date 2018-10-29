package com.yksj.healthtalk.data;

import android.graphics.RectF;

public class FemaleHeader implements IHeader{
	/*public static final int positionEye = 20;//眼睛
	public static final int positionNose = 15;//鼻子
	public static  final int positionEar = 16;//耳朵
	public static final int positionMouth = 17;//嘴巴
	public static final int positionBrain = 18;//脑
	public static final int positionThroat = 19;//咽喉
	public static final int positionNeck = 7;//颈部
	public static final int positionFace = 21;//脸部
*/	
	RectF leftEyeRectF = new RectF(80.05f, 65.00f,119.05f,105.00f);//左眼
	RectF rightEyeRectF = new RectF(149.80f,65.00f,188.80f,105.00f);//右眼
	RectF leftEarRectF = new RectF(58.90f,87.10f,77.90f,136.10f);//左耳
	RectF rightEarRectF = new RectF(192.90f,87.10f,211.15f,136.10f);//右耳
	
	RectF noseRectF = new RectF(119.95f,79.00f, 149.80f,134.10f);//鼻子
	RectF mouthRectF = new RectF(102.95f,152.65f, 166.95f,178.65f);//嘴巴
	RectF throatRectF = new RectF(82.00f,164.10f, 187.00f, 201.10f);//咽喉
	RectF neckRectF = new RectF(68.00f,201.35f,202.00f, 256.35f);//颈部
	RectF brainRectF = new RectF(66.15f,0.0f, 204.90f, 64.25f);//脑
	
	RectF leftFaceF = new RectF(77f,103.50f,120f,135.25f);//左脸
	RectF rightFaceF = new RectF(149.50f,103.50f,192.50f,135.25f);//右脸
	
	/**
	 * 检测头部检测区域
	 * @param x
	 * @param y
	 * @return
	 */
	public int isRectCollisionHeader(float x,float y){
		if(leftEyeRectF.contains(x, y) || rightEyeRectF.contains(x,y)){
			return positionEye;
		}else if(leftEarRectF.contains(x,y) || rightEarRectF.contains(x,y)){
			return positionEar;
		}else if(noseRectF.contains(x, y)){
			return positionNose;
		}else if(mouthRectF.contains(x, y)){
			return positionMouth;
		}else if(throatRectF.contains(x, y)){
			return positionThroat;
		}else if(neckRectF.contains(x, y)){
			return positionNeck;
		}else if(brainRectF.contains(x, y)){
			return positionBrain;
		}else if(leftFaceF.contains(x,y) || rightFaceF.contains(x, y)){
			return positionFace;
		}
		return -1;
	}
	
	

}
