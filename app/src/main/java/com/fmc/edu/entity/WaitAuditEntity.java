package com.fmc.edu.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.fmc.edu.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    public static List<WaitAuditEntity> ToWaitAuditEntity(List<Map<String, Object>> data) {
        List<WaitAuditEntity> list = new ArrayList<WaitAuditEntity>();
        for (int i = 0; i < data.size(); i++) {
            WaitAuditEntity waitAuditItem = new WaitAuditEntity();
            Map<String, Object> item = data.get(i);
            waitAuditItem.parentId = ConvertUtils.getInteger(item.get("parentId"));
            waitAuditItem.cellphone = ConvertUtils.getString(item.get("cellPhone"));
            waitAuditItem.parentName = ConvertUtils.getString(item.get("parentName"));
            waitAuditItem.auditStatus = ConvertUtils.getInteger(item.get("auditStatus"), 1);
            list.add(waitAuditItem);
        }
        return list;
    }
}
