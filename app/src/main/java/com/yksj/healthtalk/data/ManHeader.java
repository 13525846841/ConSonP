package com.yksj.healthtalk.data;

import android.graphics.RectF;

/**
 * 男头部
 * @author zhao
 *
 */
public class ManHeader implements IHeader {
	/*public int positionEye = 20;//眼睛
	public int positionNose = 15;//鼻子
	public int positionEar = 16;//耳朵
	public int positionMouth = 17;//嘴巴
	public int positionBrain = 18;//脑
	public int positionThroat = 19;//咽喉
	public int positionNeck = 7;//颈部
*/	
	RectF leftEyeRectF = new RectF(80.05f, 65.00f,119.05f,115.00f);//左眼
	RectF rightEyeRectF = new RectF(149.80f,65.00f,188.80f,115.00f);//右眼
	RectF leftEarRectF = new RectF(55.40f,101.60f,74.15f,158.60f);//左耳
	RectF rightEarRectF = new RectF(195.15f, 101.60f,213.15f,158.60f);//右耳
	
	
	RectF mouthRectF = new RectF(102.95f,152.65f, 166.95f,178.65f);//嘴巴
	RectF throatRectF = new RectF(82.00f,187.10f, 187.00f, 224.10f);//咽喉
	RectF neckRectF = new RectF(68.00f,224.60f,202.00f, 279.60f);//颈部
	RectF brainRectF = new RectF(66.15f,0.0f, 204.90f, 64.25f);//脑
	RectF leftFaceF = new RectF(70.75f,114.75f,110f,151.75f);//左脸
	RectF rightFaceF = new RectF(140.50f,114.75f,190.0f,151.75f);//右脸
	RectF noseRectF = new RectF(119.95f,79.00f, 148.95f,146.00f);//鼻子
	
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
		}else if(leftFaceF.contains(x, y) || rightFaceF.contains(x, y)){
			return FemaleHeader.positionFace;
		}
		return -1;
	}
	
}
