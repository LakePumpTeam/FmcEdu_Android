package com.fmc.edu.entity;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Candy on 2015/5/22.
 */
public class CommentItemEntity implements Parcelable {
    public int userId;
    public String userName;
    public String comment;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(userId);
        dest.writeString(userName);
        dest.writeString(comment);
    }

    public static final Parcelable.Creator<CommentItemEntity> CREATOR = new Creator<CommentItemEntity>() {
        @Override
        public CommentItemEntity createFromParcel(Parcel source) {
            CommentItemEntity commentItemEntity = new CommentItemEntity();
            commentItemEntity.userId = source.readInt();
            commentItemEntity.userName = source.readString();
            commentItemEntity.comment = source.readString();
            return commentItemEntity;
        }

        @Override
        public CommentItemEntity[] newArray(int size) {
            return new CommentItemEntity[size];
        }
    };

}
