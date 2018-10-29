package com.yksj.healthtalk.entity;


import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 标签实体
 * @author Administrator
 *
 */
public class TagEntity implements Parcelable{
	
	private String id;
	private String name;
	private boolean beChoosed = false;
	private String upperId;
	
	public String getUpperId() {
		return upperId;
	}

	public void setUpperId(String upperId) {
		this.upperId = upperId;
	}

	public TagEntity() {}
	
	public TagEntity(String id, String name) {
		super();
		this.id = id;
		this.name = name;
	}

	
	public TagEntity(String id, String name, boolean beChoosed) {
		this.id = id;
		this.name = name;
		this.beChoosed = beChoosed;
	}


	public boolean isBeChoosed() {
		return beChoosed;
	}

	public void setBeChoosed(boolean beChoosed) {
		this.beChoosed = beChoosed;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.id);
		dest.writeString(this.name);
		dest.writeString(this.upperId);
	}
	
	public static final Parcelable.Creator<TagEntity> CREATOR = new Parcelable.Creator<TagEntity>() {

		@Override
		public TagEntity createFromParcel(Parcel source) {
			TagEntity tagEntity = new TagEntity(source.readString(), source.readString());
			tagEntity.setUpperId(source.readString());
			return tagEntity;
		}

		@Override
		public TagEntity[] newArray(int size) {
			return null;
		}
	};
	
	public boolean equals(Object o) {
		if(this == o) {
			return true;
		}
		if(o instanceof TagEntity) {
			TagEntity tagEntity = (TagEntity)o;
			if(tagEntity.getId().equals(this.id)&&tagEntity.getName().equals(this.name)) {
				return true;
			}
		}
		return false;
	};

	@Override
	public int hashCode() {
		return this.id.hashCode();
	}
	
	/**
	 * 兴趣墙的解析
	 * @param str
	 * @return
	 */
	public static List<TagEntity> parToList(String str){
		List<TagEntity> entities=new ArrayList<TagEntity>();
		try {
			JSONArray array=new JSONArray(str);
			for (int i = 0; i < array.length(); i++) {
				JSONObject jsonObject = array.getJSONObject(i);
				TagEntity entity=new TagEntity(jsonObject.optString("INFO_LAY_ID"), jsonObject.optString("INFO_LAY_NAME"));
				entities.add(entity);
			}
			return entities;
		} catch (JSONException e) {
			return null;
		}
	}
}
