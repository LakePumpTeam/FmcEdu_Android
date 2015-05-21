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

    public static final Parcelable.Creator<SchoolDynamicEntity> CREATOR = new Parcelable.Creator<SchoolDynamicEntity>() {
        @Override
        public SchoolDynamicEntity createFromParcel(Parcel source) {
            SchoolDynamicEntity schoolDynamicEntity = new SchoolDynamicEntity();
            schoolDynamicEntity.newsId = source.readInt();
            schoolDynamicEntity.subject = source.readString();
            schoolDynamicEntity.content = source.readString();
            schoolDynamicEntity.createDate = source.readString();
            source.readList(schoolDynamicEntity.imageUrls, ClassLoader.getSystemClassLoader());
            return schoolDynamicEntity;
        }

        @Override
        public SchoolDynamicEntity[] newArray(int size) {
            return new SchoolDynamicEntity[size];
        }
    };
}
