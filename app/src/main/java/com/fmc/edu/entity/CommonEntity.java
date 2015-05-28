package com.fmc.edu.entity;

/**
 * Created by Candy on 2015/5/3.
 */
public class CommonEntity {
    private String mId;
    private String mFullName;

    public CommonEntity() {
    }

    public CommonEntity(String id, String fullName) {
        this.mId = id;
        this.mFullName = fullName;
    }


    public String getId() {
        return mId;
    }

    public String getFullName() {
        return mFullName;
    }


}
