package com.fmc.edu.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.fmc.edu.entity.LoginUserEntity;

/**
 * Created by Candy on 2015/5/7.
 */
public class ServicePreferenceUtils {

    public static void saveLoginUserPreference(Context context, LoginUserEntity userEntity) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("cellphone", userEntity.cellphone);
        editor.putString("password", encryptPWD(userEntity.password));
        editor.commit();
    }

    public static void clearLoginUserPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }

    public static LoginUserEntity getLoginUserByPreference(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("loginUser", Context.MODE_PRIVATE);
        LoginUserEntity loginUserEntity = new LoginUserEntity();
        if (null == sharedPreferences) {
            return loginUserEntity;
        }
        loginUserEntity.cellphone = sharedPreferences.getString("cellphone", "");
        loginUserEntity.password = sharedPreferences.getString("password", "");
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
