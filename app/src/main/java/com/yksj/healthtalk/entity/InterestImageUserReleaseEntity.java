package com.yksj.healthtalk.entity;

import android.os.Parcel;
import android.os.Parcelable;

public class InterestImageUserReleaseEntity implements Parcelable{

	private String imageurl;// 图片地址
	// private String clickCount;//点击次数
	private String collectionCount; // 收藏次数
	private String commentCount;// 评论次数
	private String shareCount;// 分享次数
	private String verifystate;// 代表审核状态 10为未审核。 20 为审核通过。30为审核失败
	private String forwardCount;// 转发次数
	private String groupDesc;// 沙龙描述
	private String groupLevel;// 沙龙等级
	private String groupName;// 沙龙名称
	private String groupId;// 沙龙id
	private String groupClass;// 沙龙类别
	private String iconUrl; // 小图标地址
	private String infolayId;// 信息层面id
	private String picId;
	private String picUrl;// 图片关联店铺地址
	private String picDesc;// 图片描述
	private String picName;// 图片名字
	private String picSize;// 图片大小
	private String uploadTime;// 图片上传时间

	@Override
	public boolean equals(Object o) {
		if(o instanceof InterestImageUserReleaseEntity) {
			InterestImageUserReleaseEntity entity = (InterestImageUserReleaseEntity) o;
			if(entity.picId.equals(this.picId)) {
				return true;	
			}
			return false;
		} else {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return Integer.parseInt(picId);
	}
	
	public String getImageurl() {
		return imageurl;
	}

	public void setImageurl(String imageurl) {
		this.imageurl = imageurl;
	}

	public String getCollectionCount() {
		return collectionCount;
	}

	public void setCollectionCount(String collectionCount) {
		this.collectionCount = collectionCount;
	}

	public String getCommentCount() {
		return commentCount;
	}

	public void setCommentCount(String commentCount) {
		this.commentCount = commentCount;
	}

	public String getShareCount() {
		return shareCount;
	}

	public void setShareCount(String shareCount) {
		this.shareCount = shareCount;
	}

	public String getVerifystate() {
		return verifystate;
	}

	public void setVerifystate(String verifystate) {
		this.verifystate = verifystate;
	}

	public String getForwardCount() {
		return forwardCount;
	}

	public void setForwardCount(String forwardCount) {
		this.forwardCount = forwardCount;
	}

	public String getGroupDesc() {
		return groupDesc;
	}

	public void setGroupDesc(String groupDesc) {
		this.groupDesc = groupDesc;
	}

	public String getGroupLevel() {
		return groupLevel;
	}

	public void setGroupLevel(String groupLevel) {
		this.groupLevel = groupLevel;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	public String getGroupClass() {
		return groupClass;
	}

	public void setGroupClass(String groupClass) {
		this.groupClass = groupClass;
	}

	public String getIconUrl() {
		return iconUrl;
	}

	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}

	public String getInfolayId() {
		return infolayId;
	}

	public void setInfolayId(String infolayId) {
		this.infolayId = infolayId;
	}

	public String getPicId() {
		return picId;
	}

	public void setPicId(String picId) {
		this.picId = picId;
	}

	public String getPicUrl() {
		return picUrl;
	}

	public void setPicUrl(String picUrl) {
		this.picUrl = picUrl;
	}

	public String getPicDesc() {
		return picDesc;
	}

	public void setPicDesc(String picDesc) {
		this.picDesc = picDesc;
	}

	public String getPicName() {
		return picName;
	}

	public void setPicName(String picName) {
		this.picName = picName;
	}

	public String getPicSize() {
		return picSize;
	}

	public void setPicSize(String picSize) {
		this.picSize = picSize;
	}

	public String getUploadTime() {
		return uploadTime;
	}

	public void setUploadTime(String uploadTime) {
		this.uploadTime = uploadTime;
	}

	public class Constant {
		public static final String BIGPICTUREPATH = "bigPicturePath";
		public static final String CLICK_COUNT = "click_Count";
		public static final String COLLECTION_COUNT = "collection_Count";
		public static final String COMMENT_COUNT = "comment_Count";
		public static final String CUSTID = "custid";
		public static final String FORWARD_COUNT = "forward_Count";
		public static final String GROUPDESC = "groupDesc";
		public static final String GROUPLEVEL = "groupLevel";
		public static final String GROUPNAME = "groupName";
		public static final String GROUP_ID = "group_id";
		public static final String GROUPCLASS = "groupclass";
		public static final String ICONPICTUREPATH = "iconPicturePath";
		public static final String INFOLAYID = "infoLayid";
		public static final String PICTUREHEIGHTPX = "pictureHeightPx";
		public static final String PICTUREID = "pictureID";
		public static final String PICTUREWIDTHPX = "pictureWidthPx";
		public static final String PICTURE_URL = "picture_url";
		public static final String PUBLICCUSTOMERINFO = "publicCustomerInfo";
		public static final String PUCTUREDESC = "puctureDesc";
		public static final String PUCTURENAME = "puctureName";
		public static final String PUCTURESIZE = "puctureSize";
		public static final String RELEASESYSTEMMESSAGE = "releaseSystemMessage";
		public static final String SHARE_COUNT = "share_Count";
		public static final String UPLOADTIME = "uploadTime";
		public static final String VERIFYSTATE = "verifystate";
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(this.collectionCount);
		dest.writeString(this.commentCount);
		dest.writeString(this.forwardCount);
		dest.writeString(this.groupClass);
		dest.writeString(this.groupDesc);
		dest.writeString(this.groupId);
		dest.writeString(this.groupLevel);
		dest.writeString(this.groupName);
		dest.writeString(this.iconUrl);
		dest.writeString(this.imageurl);
		dest.writeString(this.infolayId);
		dest.writeString(this.picDesc);
		dest.writeString(this.picId);
		dest.writeString(this.picName);
		dest.writeString(this.picSize);
		dest.writeString(this.picUrl);
		dest.writeString(this.shareCount);
		dest.writeString(this.uploadTime);
		dest.writeString(this.verifystate);
	}
	
	public static final Parcelable.Creator<InterestImageUserReleaseEntity> CREATOR = new Parcelable.Creator<InterestImageUserReleaseEntity>() {

		@Override
		public InterestImageUserReleaseEntity createFromParcel(Parcel source) {
			InterestImageUserReleaseEntity entity = new InterestImageUserReleaseEntity();
			entity.setCollectionCount(source.readString());
			entity.setCommentCount(source.readString());
			entity.setForwardCount(source.readString());
			entity.setGroupClass(source.readString());
			entity.setGroupDesc(source.readString());
			entity.setGroupId(source.readString());
			entity.setGroupLevel(source.readString());
			entity.setGroupName(source.readString());
			entity.setIconUrl(source.readString());
			entity.setImageurl(source.readString());
			entity.setInfolayId(source.readString());
			entity.setPicDesc(source.readString());
			entity.setPicId(source.readString());
			entity.setPicName(source.readString());
			entity.setPicSize(source.readString());
			entity.setPicUrl(source.readString());
			entity.setShareCount(source.readString());
			entity.setUploadTime(source.readString());
			entity.setVerifystate(source.readString());
			return entity;
		}

		@Override
		public InterestImageUserReleaseEntity[] newArray(int size) {
			return null;
		}
		
	};

	@Override
	public String toString() {
		return "InterestImageUserReleaseEntity [imageurl=" + imageurl
				+ ", collectionCount=" + collectionCount + ", commentCount="
				+ commentCount + ", shareCount=" + shareCount
				+ ", verifystate=" + verifystate + ", forwardCount="
				+ forwardCount + ", groupDesc=" + groupDesc + ", groupLevel="
				+ groupLevel + ", groupName=" + groupName + ", groupId="
				+ groupId + ", groupClass=" + groupClass + ", iconUrl="
				+ iconUrl + ", infolayId=" + infolayId + ", picId=" + picId
				+ ", picUrl=" + picUrl + ", picDesc=" + picDesc + ", picName="
				+ picName + ", picSize=" + picSize + ", uploadTime="
				+ uploadTime + "]";
	}
	
	
	
}
