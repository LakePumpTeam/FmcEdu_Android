package com.fmc.edu.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Candy on 2015/5/19.
 */
public class ImageItemEntity implements Parcelable {
    public String  thumbUrl;
    public String origUrl;
    public boolean isCheck;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString( thumbUrl);
        dest.writeString(origUrl);
        dest.writeInt(isCheck ? 1 : 0);
    }

    public static final Parcelable.Creator<ImageItemEntity> CREATOR = new Creator<ImageItemEntity>() {
        @Override
        public ImageItemEntity createFromParcel(Parcel source) {
            ImageItemEntity imageItemEntity = new ImageItemEntity();
            imageItemEntity. thumbUrl = source.readString();
            imageItemEntity.origUrl = source.readString();
            imageItemEntity.isCheck = source.readInt() == 1 ? true : false;
            return imageItemEntity;
        }

        @Override
        public ImageItemEntity[] newArray(int size) {
            return new ImageItemEntity[size];
        }
    };
}
