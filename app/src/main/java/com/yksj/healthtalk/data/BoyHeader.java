package com.yksj.healthtalk.data;

import android.graphics.RectF;

public class BoyHeader implements IHeader {

	RectF leftEyeRectF = new RectF(77.60f, 98.50f,130.15f,135.25f);//左眼
	RectF rightEyeRectF = new RectF(143.00f,98.50f,194.60f,135.15f);//右眼
	RectF leftEarRectF = new RectF(44.90f,101.60f,73.20f,161.80f);//左耳
	RectF rightEarRectF = new RectF(198.55f, 106.35f,224.65f,161.70f);//右耳
	
	
	RectF mouthRectF = new RectF(110.00f,169.00f, 161.40f,186.45f);//嘴巴
	RectF throatRectF = new RectF(107.75f,201.75f,164.30f,240.10f);//咽喉
	RectF neckRectF = new RectF(56.25f,194.50f,217.50f, 287.50f);//颈部
	
	RectF brainRectF = new RectF(54.95f,4.00f, 215.95f, 80.00f);//脑
	RectF leftFaceF = new RectF(75.45f,135.00f,120.25f,211.00f);//左脸
	RectF rightFaceF = new RectF(155.45f,135.00f,200.25f,211.00f);//右脸
	RectF noseRectF = new RectF(117.85f,110.75f, 154.80f,161.80f);//鼻子

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
			return positionFace;
		}
		return -1;
	}

}
