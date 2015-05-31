package com.fmc.edu.entity;

import com.fmc.edu.enums.UserRoleEnum;
import com.fmc.edu.utils.ConvertUtils;

import java.util.Map;

/**
 * Created by Candy on 2015/5/7.
 */
public class LoginUserEntity {
    public int userId;
    public String userName;
    public int auditState;
    public UserRoleEnum userRole;
    public String cellphone;
    public String password;
    public String salt;
    public String userCardNum;
    public int repayState;
    public boolean sex;

    public static LoginUserEntity toLoginUserEntity(Map<String, Object> mapData) {
        LoginUserEntity loginUserEntity = new LoginUserEntity();
        loginUserEntity.userId = ConvertUtils.getInteger(mapData.get("userId"));
        loginUserEntity.userRole = UserRoleEnum.getEnumValue(ConvertUtils.getInteger(mapData.get("userRole")));
        return loginUserEntity;
    }
}
