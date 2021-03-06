package com.fmc.edu.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.enums.UserRoleEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Candy on 2015/5/7.
 */
public class ServicePreferenceUtils {

    public static void saveLoginUserPreference(Context context, LoginUserEntity userEntity) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("userId", userEntity.userId);
        editor.putInt("userRole", UserRoleEnum.getValue(userEntity.userRole));
        editor.putString("cellphone", userEntity.cellphone);
        editor.putString("password", encryptPWD(userEntity.password));
        editor.putString("salt", userEntity.salt);
        editor.putString("userName", userEntity.userName);
        editor.putString("userCardNum", userEntity.userCardNum);
        editor.commit();
    }

    public static void saveSexPreference(Context context, boolean sex) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("sex", sex);
        editor.commit();
    }

    public static void saveUserNamePreference(Context context, String userName) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userName", userName);
        editor.commit();
    }
    public static void saveCellphonePreference(Context context, String cellPhone) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cellphone", cellPhone);
        editor.commit();
    }
    public static void saveClassIdPreference(Context context, int classId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("classId", classId);
        editor.commit();
    }

    public static void saveHeadTeacherPreference(Context context, boolean isHeadTeacher) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("headTeacher", isHeadTeacher);
        editor.commit();
    }

    public static void saveUserCardNumPreference(Context context, String userCardNum) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("userCardNum", userCardNum);
        editor.commit();
    }

    public static void saveStudentIdPreference(Context context, int studentId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("studentId", studentId);
        editor.commit();
    }

    public static void saveNoticeSettingPreference(Context context, Map<String, Boolean> setting) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConvertUtils.getString(getLoginUserByPreference(context).userId), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("shake", setting.get("shake"));
        editor.putBoolean("ring", setting.get("ring"));
        editor.commit();
    }

    public static Map<String, Boolean> getNoticeSettingByPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(ConvertUtils.getString(getLoginUserByPreference(context).userId), Context.MODE_PRIVATE);
        Map<String, Boolean> mapData = new HashMap<String, Boolean>();
        if (null == sharedPreferences) {
            mapData.put("shake",  true);
            mapData.put("ring", true);
            return mapData;
        }

        mapData.put("shake", sharedPreferences.getBoolean("shake", true));
        mapData.put("ring", sharedPreferences.getBoolean("ring", true));
        return mapData;
    }

    public static void clearLoginUserPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static void clearPasswordPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("password", "");
        editor.commit();
    }

    public static LoginUserEntity getLoginUserByPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        if (null == sharedPreferences) {
            return null;
        }
        LoginUserEntity loginUserEntity = new LoginUserEntity();
        loginUserEntity.cellphone = sharedPreferences.getString("cellphone", "");
        loginUserEntity.password = decryptPWD(sharedPreferences.getString("password", ""));
        loginUserEntity.userId = sharedPreferences.getInt("userId", 0);
        loginUserEntity.userRole = UserRoleEnum.getEnumValue(sharedPreferences.getInt("userRole", 1));
        loginUserEntity.salt = sharedPreferences.getString("salt", "");
        loginUserEntity.userName = sharedPreferences.getString("userName", "");
        loginUserEntity.sex = sharedPreferences.getBoolean("sex", true);
        loginUserEntity.userCardNum = sharedPreferences.getString("userCardNum", "0");
        loginUserEntity.classId = sharedPreferences.getInt("classId", 0);
        loginUserEntity.studentId = sharedPreferences.getInt("studentId", 0);
        loginUserEntity.headTeacher = sharedPreferences.getBoolean("headTeacher", false);
        return loginUserEntity;
    }


    public static String encryptPWD(String ssoToken) {
        try {
            byte[] _ssoToken = ssoToken.getBytes("ISO-8859-1");
            String name = new String();
            for (int i = 0; i < _ssoToken.length; i++) {
                int asc = _ssoToken[i];
                _ssoToken[i] = (byte) (asc + 27);
                name = name + (asc + 27) + "%";
            }
            return name;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptPWD(String ssoToken) {
        try {
            String name = new String();
            java.util.StringTokenizer st = new java.util.StringTokenizer(ssoToken, "%");
            while (st.hasMoreElements()) {
                int asc = Integer.parseInt((String) st.nextElement()) - 27;
                name = name + (char) asc;
            }

            return name;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
