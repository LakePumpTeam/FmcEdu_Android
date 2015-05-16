package com.fmc.edu.entity;

import com.fmc.edu.utils.ConvertUtils;

import java.util.Map;

/**
 * Created by Candy on 2015/5/7.
 */
public class LoginUserEntity {
    public int userId;
    public int auditState;
    public int userRole;
    public String cellphone;
    public String password;
    public String userCardNum;
    public int studentSex;
    public int repayState;

    public static LoginUserEntity toLoginUserEntity(Map<String, Object> mapData) {
        LoginUserEntity loginUserEntity = new LoginUserEntity();
        loginUserEntity.userId = ConvertUtils.getInteger(mapData.get("userId"));
        return loginUserEntity;
    }
}
