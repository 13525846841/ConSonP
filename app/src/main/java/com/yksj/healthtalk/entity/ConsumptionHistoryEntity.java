package com.yksj.healthtalk.entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 消费历史实体
 * @author Administrator
 *
 */
public class ConsumptionHistoryEntity {

	private String moneyNum;//多美币数量
	private String ConsumptionTime;//交易时间
	private String ConsumptionType;//交易类型
	
	public String getMoneyNum() {
		return moneyNum;
	}
	public void setMoneyNum(String moneyNum) {
		this.moneyNum = moneyNum;
	}
	public String getConsumptionTime() {
		return ConsumptionTime;
	}
	public void setConsumptionTime(String consumptionTime) {
		ConsumptionTime = consumptionTime;
	}
	public String getConsumptionType() {
		return ConsumptionType;
	}
	public void setConsumptionType(String consumptionType) {
		ConsumptionType = consumptionType;
	}
	@Override
	public String toString() {
		return "ConsumptionHistoryEntity [moneyNum=" + moneyNum
				+ ", ConsumptionTime=" + ConsumptionTime + ", ConsumptionType="
				+ ConsumptionType + "]";
	}
	/**
	 * 解析消费历史的,返回集合
	 * @param json
	 * @return
	 */
	public static List<ConsumptionHistoryEntity> parseToList(String object){
	 Map<String,String> haMap=new HashMap<String, String>();
		 haMap.put("101932","话题门票");
		 haMap.put("101933","共享文件");
		 haMap.put("101934","兑换");
		 haMap.put("101935","医生服务");
		List<ConsumptionHistoryEntity> entities=null;
		try {
			entities=new ArrayList<ConsumptionHistoryEntity>();
			JSONArray jsonObject=new JSONArray(object);
			for (int i = 0; i < jsonObject.length(); i++) {
				ConsumptionHistoryEntity entity=new ConsumptionHistoryEntity();
				JSONObject object3 = (JSONObject)jsonObject.get(i);
				entity.setMoneyNum(object3.getString("changeNum"));
				entity.setConsumptionTime(object3.getString("changeTime"));
				entity.setConsumptionType(haMap.get(object3.getString("changeType")));
				entities.add(entity);
			}
			haMap=null;
			return entities;
		} catch (JSONException e) {
			haMap=null;
			return entities;
		}
	}
}
