package com.yksj.healthtalk.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.yksj.healthtalk.utils.StringFormatUtils;

public class ShopListItemEntity implements Serializable{

	String MERCHANT_ID;//商户ID
	String GOODS_ID;//商品ID
	String GOODS_NAME;//商品名称
	String GOODS_DESC;//商品描述说明
	String CLASS_ID;//商品大分类
	String SPECIFICATIONS;//商品规格
	String CURRENT_PRICE;//当前价格
	String VIP_PRICE;//会员价格
	String DISCOUNT_INFO;//折扣信息
	String FACTORY;//生产厂家
	String SHELVES_FLAG;//0-未上架，1-已上架，2-撤销  默认0
	String SHELVES_TIME;//商品上架时间
	String LIFECYCLE_END;//商品生命周期的终止时间
	String NOTES;//备注
	String LINK_ADDR;//跳转链接地址
	String PICTURE_ADDR;//图片地址
	
	String SHOP_PRICE;//市场价格
	Integer GOODS_COUNT;//商品数量
	String GOODS_GOLD;
	Integer BUY_COUNT;
	String HaveShoppingCar;
	boolean isSelect = false;
	

	public boolean isSelect() {
		return isSelect;
	}


	public void setSelect(boolean isSelect) {
		this.isSelect = isSelect;
	}


	public String getHaveShoppingCar() {
		return HaveShoppingCar;
	}


	public void setHaveShoppingCar(String haveShoppingCar) {
		HaveShoppingCar = haveShoppingCar;
	}


	public Integer getBUY_COUNT() {
		return BUY_COUNT;
	}


	public void setBUY_COUNT(Integer bUY_COUNT) {
		BUY_COUNT = bUY_COUNT;
	}


	public String getSHOP_PRICE() {
		return SHOP_PRICE;
	}


	public void setSHOP_PRICE(String sHOP_PRICE) {
		SHOP_PRICE = sHOP_PRICE;
	}


	public Integer getGOODS_COUNT() {
		return GOODS_COUNT;
	}


	public void setGOODS_COUNT(Integer gOODS_COUNT) {
		GOODS_COUNT = gOODS_COUNT;
	}


	public String getGOODS_GOLD() {
		return GOODS_GOLD;
	}


	public void setGOODS_GOLD(String gOODS_GOLD) {
		GOODS_GOLD = gOODS_GOLD;
	}


	public String getPICTURE_ADDR() {
		return PICTURE_ADDR;
	}


	public void setPICTURE_ADDR(String pICTURE_ADDR) {
		PICTURE_ADDR = pICTURE_ADDR;
	}


	public String getMERCHANT_ID() {
		return MERCHANT_ID;
	}


	public void setMERCHANT_ID(String mERCHANT_ID) {
		MERCHANT_ID = mERCHANT_ID;
	}


	public String getGOODS_ID() {
		return GOODS_ID;
	}


	public void setGOODS_ID(String gOODS_ID) {
		GOODS_ID = gOODS_ID;
	}


	public String getGOODS_NAME() {
		return GOODS_NAME;
	}


	public void setGOODS_NAME(String gOODS_NAME) {
		GOODS_NAME = gOODS_NAME;
	}


	public String getGOODS_DESC() {
		return GOODS_DESC;
	}


	public void setGOODS_DESC(String gOODS_DESC) {
		GOODS_DESC = gOODS_DESC;
	}


	public String getCLASS_ID() {
		return CLASS_ID;
	}


	public void setCLASS_ID(String cLASS_ID) {
		CLASS_ID = cLASS_ID;
	}


	public String getSPECIFICATIONS() {
		return SPECIFICATIONS;
	}


	public void setSPECIFICATIONS(String sPECIFICATIONS) {
		SPECIFICATIONS = sPECIFICATIONS;
	}


	public String getCURRENT_PRICE() {
		return CURRENT_PRICE;
	}


	public void setCURRENT_PRICE(String cURRENT_PRICE) {
		CURRENT_PRICE = cURRENT_PRICE;
	}


	public String getVIP_PRICE() {
		return VIP_PRICE;
	}


	public void setVIP_PRICE(String vIP_PRICE) {
		VIP_PRICE = vIP_PRICE;
	}


	public String getDISCOUNT_INFO() {
		return DISCOUNT_INFO;
	}


	public void setDISCOUNT_INFO(String dISCOUNT_INFO) {
		DISCOUNT_INFO = dISCOUNT_INFO;
	}


	public String getFACTORY() {
		return FACTORY;
	}


	public void setFACTORY(String fACTORY) {
		FACTORY = fACTORY;
	}


	public String getSHELVES_FLAG() {
		return SHELVES_FLAG;
	}


	public void setSHELVES_FLAG(String sHELVES_FLAG) {
		SHELVES_FLAG = sHELVES_FLAG;
	}


	public String getSHELVES_TIME() {
		return SHELVES_TIME;
	}


	public void setSHELVES_TIME(String sHELVES_TIME) {
		SHELVES_TIME = sHELVES_TIME;
	}


	public String getLIFECYCLE_END() {
		return LIFECYCLE_END;
	}


	public void setLIFECYCLE_END(String lIFECYCLE_END) {
		LIFECYCLE_END = lIFECYCLE_END;
	}


	public String getNOTES() {
		return NOTES;
	}


	public void setNOTES(String nOTES) {
		NOTES = nOTES;
	}


	public String getLINK_ADDR() {
		return LINK_ADDR;
	}


	public void setLINK_ADDR(String lINK_ADDR) {
		LINK_ADDR = lINK_ADDR;
	}


	public static List<ShopListItemEntity> parseToEntity(String content){
		List<ShopListItemEntity> entities=new ArrayList<ShopListItemEntity>();
		JSONArray array=null;
		try {
			if(content.startsWith("{")){
				JSONObject obje=new JSONObject(content);
				if(obje.has("GOODSLIST")){
					array=obje.getJSONArray("GOODSLIST");
				}
				else{
					array=obje.getJSONArray("data");	
				}
			}else{
				array=new JSONArray(content);
			}
			for (int i = 0; i < array.length(); i++) {
				ShopListItemEntity entity=new ShopListItemEntity();
				JSONObject object= (JSONObject) array.get(i);
				entity.setGOODS_ID(object.optString("GOODS_ID",""));
				entity.setGOODS_NAME(StringFormatUtils.ObjectToString(object.optString("GOODS_NAME","")));
				entity.setGOODS_DESC(StringFormatUtils.ObjectToString(object.optString("GOODS_DESC","")));
				entity.setCLASS_ID(StringFormatUtils.ObjectToString(object.optString("CLASS_ID","")));
				entity.setMERCHANT_ID(object.optString("MERCHANT_ID",""));
				entity.setSPECIFICATIONS(StringFormatUtils.ObjectToString(object.optString("SPECIFICATIONS","")));
				entity.setCURRENT_PRICE(StringFormatUtils.ObjectToString(object.optString("CURRENT_PRICE","")));
				entity.setVIP_PRICE(StringFormatUtils.ObjectToString(object.optString("VIP_PRICE","")));
				entity.setDISCOUNT_INFO(object.optString("DISCOUNT_INFO",""));
				entity.setFACTORY(StringFormatUtils.ObjectToString(object.optString("FACTORY","")));
				entity.setSHELVES_FLAG(StringFormatUtils.ObjectToString(object.optString("SHELVES_FLAG","")));
				entity.setDISCOUNT_INFO(StringFormatUtils.ObjectToString(object.optString("DISCOUNT_INFO","")));
				entity.setLIFECYCLE_END(StringFormatUtils.ObjectToString(object.optString("LIFECYCLE_END","")));
				entity.setNOTES(StringFormatUtils.ObjectToString(object.optString("NOTES","")));
				entity.setPICTURE_ADDR(object.optString("PICTURE_ADDR",""));
				entity.setLINK_ADDR(object.optString("LINK_ADDR",""));
				entity.setHaveShoppingCar(object.optString("HaveShoppingCar"));
				entities.add(entity);
			}
		} catch (JSONException e) {
			return null;
		}
		return entities;
	}
	
	
	public static ShopListItemEntity parseEntity(String content){
		try {
			ShopListItemEntity entity=new ShopListItemEntity();
			JSONObject object= new JSONObject(content);
			entity.setGOODS_ID(object.optString("GOODS_ID",""));
			entity.setGOODS_NAME(StringFormatUtils.ObjectToString(object.optString("GOODS_NAME","")));
			entity.setGOODS_DESC(StringFormatUtils.ObjectToString(object.optString("GOODS_DESC","")));
			entity.setCLASS_ID(StringFormatUtils.ObjectToString(object.optString("CLASS_ID","")));
			entity.setMERCHANT_ID(object.optString("MERCHANT_ID",""));
			entity.setSPECIFICATIONS(StringFormatUtils.ObjectToString(object.optString("SPECIFICATIONS","")));
			entity.setCURRENT_PRICE(StringFormatUtils.ObjectToString(object.optString("CURRENT_PRICE","")));
			entity.setVIP_PRICE(StringFormatUtils.ObjectToString(object.optString("VIP_PRICE","")));
			entity.setDISCOUNT_INFO(object.optString("DISCOUNT_INFO",""));
			entity.setFACTORY(StringFormatUtils.ObjectToString(object.optString("FACTORY","")));
			entity.setSHELVES_FLAG(StringFormatUtils.ObjectToString(object.optString("SHELVES_FLAG","")));
			entity.setDISCOUNT_INFO(StringFormatUtils.ObjectToString(object.optString("DISCOUNT_INFO","")));
			entity.setLIFECYCLE_END(StringFormatUtils.ObjectToString(object.optString("LIFECYCLE_END","")));
			entity.setNOTES(StringFormatUtils.ObjectToString(object.optString("NOTES","")));
			entity.setPICTURE_ADDR(object.optString("PICTURE_ADDR",""));
			entity.setLINK_ADDR(object.optString("LINK_ADDR",""));
			entity.setHaveShoppingCar(object.optString("HaveShoppingCar"));
			return entity;
		} catch (Exception e) {
			return null;
		}
	}
	
}
