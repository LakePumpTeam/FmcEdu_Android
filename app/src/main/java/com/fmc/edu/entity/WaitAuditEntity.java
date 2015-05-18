package com.fmc.edu.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Candy on 2015/5/18.
 */
public class WaitAuditEntity implements Parcelable {
    public String parentName;
    public String cellphone;
    public int parentId;
    public int auditStatus;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(parentName);
        dest.writeString(cellphone);
        dest.writeInt(parentId);
        dest.writeInt(auditStatus);
    }

    public static final Parcelable.Creator<WaitAuditEntity> CREATOR = new Creator<WaitAuditEntity>() {
        @Override
        public WaitAuditEntity createFromParcel(Parcel source) {
            WaitAuditEntity waitAuditEntity = new WaitAuditEntity();
            waitAuditEntity.parentName = source.readString();
            waitAuditEntity.cellphone = source.readString();
            waitAuditEntity.parentId = source.readInt();
            waitAuditEntity.auditStatus = source.readInt();
            return waitAuditEntity;
        }

        @Override
        public WaitAuditEntity[] newArray(int size) {
            return new WaitAuditEntity[size];
        }
    };
}
