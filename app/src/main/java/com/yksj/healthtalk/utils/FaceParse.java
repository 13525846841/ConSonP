package com.yksj.healthtalk.utils;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.EditText;

import com.yksj.consultation.son.R;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 聊天表情解析类
 * @author zhao yuan
 */
public class FaceParse {
	
	private Pattern mPattern;
	
	final Map<String,Drawable> mSmileMap;
	
	//表情文字
	private  String[] mSmileTxtArray;
	
	private int mtxtArrayResId;
	
	private int mdrawArrayResId;
	
	private final int mWidth;
	private final int mHeigth;
	
	private static FaceParse chatSmileParse;//聊天表情
	
/*	public FaceParse(Context context,int txtArrayResId,int drawArrayResId){
		this.mSmileMap = new LinkedHashMap<String,Drawable>();
		mtxtArrayResId = txtArrayResId;
		mdrawArrayResId = drawArrayResId;
		
		initDrawable(context);
		mPattern = initPattern();
	}*/
	
	/**
	 * 
	 * @param context
	 * @param txtArrayResId
	 * @param drawArrayResId
	 * @param size 指定表情大小
	 */
	public FaceParse(Context context,int txtArrayResId,int drawArrayResId,int size){
		this.mSmileMap = new LinkedHashMap<String,Drawable>();
		mtxtArrayResId = txtArrayResId;
		mdrawArrayResId = drawArrayResId;
		this.mWidth = size;
		this.mHeigth = size;
		initDrawable(size,context);
		mPattern = initPattern();
	}
	
	/**
	 * 制定大小
	 * @param size
	 */
	private void initDrawable(int size,Context mContext){
		Resources resources = mContext.getResources();
		mSmileTxtArray = resources.getStringArray(mtxtArrayResId);
		TypedArray typedArray = resources.obtainTypedArray(mdrawArrayResId);
		for (int i = 0; i < mSmileTxtArray.length; i++) {
			Drawable drawable = typedArray.getDrawable(i);
			drawable.setBounds(0, 0,size,size);
			mSmileMap.put(mSmileTxtArray[i],drawable);
		}
	}
	
	/**
	 * 初始化图片
	 */
	/*private void initDrawable(Context mContext){
		Resources resources = mContext.getResources();
		mSmileTxtArray = resources.getStringArray(mtxtArrayResId);
		TypedArray typedArray = resources.obtainTypedArray(mdrawArrayResId);
		for (int i = 0; i < mSmileTxtArray.length; i++) {
			Drawable drawable = typedArray.getDrawable(i);
			drawable.setBounds(0, 0,drawable.getIntrinsicWidth(),drawable.getIntrinsicHeight());
			mSmileMap.put(mSmileTxtArray[i],drawable);
		}
	}*/
	
	/**
	 * 获取聊天表情
	 * @param context
	 * @return
	 */
	public static FaceParse getChatFaceParse(Context context){
		if(chatSmileParse == null){
			int size = (int)context.getResources().getDimension(R.dimen.speac_24);
//			int size = (int)context.getResources().getDimension(R.dimen.chatSmileSize);
			chatSmileParse = new FaceParse(
					context.getApplicationContext(),
					R.array.smailey_txt,
					R.array.smiley_drawable_ids,
					size);
		}
		return chatSmileParse;
	}
	
	/**
	 * 
	 * 通知表情解析
	 * @param context
	 * @return
	 */
	public static FaceParse getNotifyFaceParse(Context context){
		int size = (int)context.getResources().getDimension(R.dimen.speac_24);
		return new FaceParse(
				context.getApplicationContext(),
				R.array.smailey_txt,
				R.array.smiley_drawable_ids,
				size);
	}
	
	/**
	 * 建立表情正则表达式
	 * @return
	 */
	private Pattern initPattern(){
		StringBuilder patternString = new StringBuilder(mSmileTxtArray.length*4);
		patternString.append("(");
		for(String str:mSmileTxtArray){
			patternString.append(Pattern.quote(str));
			patternString.append("|");
		}
		patternString.replace(patternString.length()-1, patternString.length(),")");
		return Pattern.compile(patternString.toString());
	}
	
	/**
	 * 
	 * 解析表情字符串将文字替换为图片
	 * @param charSequence
	 * @return
	 */
	public CharSequence parseSmileTxt(CharSequence charSequence){
		SpannableStringBuilder builder = new SpannableStringBuilder(charSequence);
		Matcher matcher = mPattern.matcher(charSequence);
		while(matcher.find()){
			Drawable drawable = mSmileMap.get(matcher.group());
			drawable.setBounds(0, 0,mWidth,mHeigth);
			builder.setSpan(new ImageSpan(drawable),matcher.start(),matcher.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		return builder;
	}
	
	/**
	 * 解析表情添加到文本输入框中
	 * @param editText
	 * @param charSequence
	 */
	public void appendFaceToEdite(EditText editText,CharSequence charSequence){
		SpannableString spannableString = new SpannableString(charSequence);
		Matcher matcher = mPattern.matcher(charSequence);
		while(matcher.find()){
			Drawable drawable = mSmileMap.get(matcher.group());
			drawable.setBounds(0, 0,mWidth,mHeigth);
			spannableString.setSpan(new ImageSpan(drawable),matcher.start(),matcher.end(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			Editable editable = editText.getEditableText();
			editable.insert(editText.getSelectionStart(), spannableString);
		}
	}
	
	public void insertToEdite(EditText editText,Drawable drawable,CharSequence charSequence){
		Editable editable = editText.getEditableText();
		SpannableString spannableString = new SpannableString(charSequence);
		spannableString.setSpan(new ImageSpan(drawable),0,charSequence.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		editable.insert(editText.getSelectionStart(),spannableString);
	}
	
	public Map<String, Drawable> getSmileMap() {
		return mSmileMap;
	}

	public String[] getFaceList(){
		return mSmileTxtArray;
	}
	
	public Drawable getDrawable(String txt){
		return mSmileMap.get(txt);
	}

	public  List<String[]> getFaceGroups(int size){
		if(size <= 0)return null;
		List<String[]> lists = new ArrayList<String[]>();
		int startIndex = 0;
		int faceLength = mSmileTxtArray.length;
		while(startIndex < faceLength){
			String[] strList;
			int length = 0;
			int endIndex = startIndex+size;
			if(endIndex >= faceLength){
				endIndex = faceLength;
			}
			length = endIndex-startIndex;
			strList = new String[length];
			System.arraycopy(mSmileTxtArray,startIndex,strList,0,length);
			lists.add(strList);
			startIndex+=size;
		}
		return lists;
	}
	
	
	public void clear(){
		if(mSmileMap != null)mSmileMap.clear();
		if(mSmileTxtArray != null)mSmileMap.clear();
	}
	
}
