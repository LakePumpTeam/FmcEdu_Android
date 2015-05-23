package com.fmc.edu.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.fmc.edu.enums.DynamicTypeEnum;

import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/21.
 */
public class DynamicEntity implements Parcelable {
    public int newsId;
    public String subject;
    public String content;
    public String createDate;
    public List<Map<String, String>> imageUrls;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(newsId);
        dest.writeString(subject);
        dest.writeString(content);
        dest.writeString(createDate);
        dest.writeList(imageUrls);
    }

    public static final Parcelable.Creator<DynamicEntity> CREATOR = new Parcelable.Creator<DynamicEntity>() {
        @Override
        public DynamicEntity createFromParcel(Parcel source) {
            DynamicEntity dynamicEntity = new DynamicEntity();
            dynamicEntity.newsId = source.readInt();
            dynamicEntity.subject = source.readString();
            dynamicEntity.content = source.readString();
            dynamicEntity.createDate = source.readString();
            source.readList(dynamicEntity.imageUrls, ClassLoader.getSystemClassLoader());
            return dynamicEntity;
        }

        @Override
        public DynamicEntity[] newArray(int size) {
            return new DynamicEntity[size];
        }
    };
}
