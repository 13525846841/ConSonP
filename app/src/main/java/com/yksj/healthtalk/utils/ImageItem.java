package com.yksj.healthtalk.utils;

import android.graphics.Bitmap;

import java.io.IOException;
import java.io.Serializable;

/**
 * 图片实体
 *
 * @author 第三方
 */
public class ImageItem implements Serializable {
    public String imageId;//
    public String thumbnailPath;
    public String imagePath;
    private Bitmap bitmap;
    public boolean isSelected = false;
    public boolean isNetPic = false;//是否是网络图片
    public int seq;//为网络图片是采用  表示顺序
    public int pidId;//为网络图片是采用   表示id

    //视频相关
    public int thumbnailId;//视频缩略图id
    public String _thumbnailPath;//缩略图小图
    public String _imagePath;//缩略图大图

    public String get_imagePath() {
        return _imagePath;
    }

    public void set_imagePath(String _imagePath) {
        this._imagePath = _imagePath;
    }

    public String get_thumbnailPath() {
        return _thumbnailPath;
    }

    public void set_thumbnailPath(String _thumbnailPath) {
        this._thumbnailPath = _thumbnailPath;
    }



    public int getThumbnailId() {
        return thumbnailId;
    }

    public void setThumbnailId(int thumbnailId) {
        this.thumbnailId = thumbnailId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getThumbnailPath() {
        return thumbnailPath;
    }

    public void setThumbnailPath(String thumbnailPath) {
        this.thumbnailPath = thumbnailPath;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

    public Bitmap getBitmap() {
        if (bitmap == null) {
            try {
                bitmap = Bimp.revitionImageSize(imagePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }


}
