package com.fmc.edu.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.fmc.edu.FmcApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Candy on 2015/5/7.
 */
public class AppConfigUtils {
    private static String SERVICE_HOST = "com.fmc.edu.service_host";
    private static String IS_DEVELOPMENT = "com.fmc.edu.is_development";
    private static String PAGE_SIZE = "com.fmc.edu.page_size";
    private static String DEVELOPER_TWO = "com.fmc.edu.two";
    private static String DEVELOPER_THREE = "com.fmc.edu.three";
    private static String DEVELOPER_FOUR = "com.fmc.edu.four";
    private static String BAIDU_APPKEY = "baiDuAppKey";
    private static  String IS_KINDERGARTEN ="com.fmc.edu.is_kindergarten";

    private static Map<String, Object> configCacheMap = new HashMap<String, Object>(10);

    public static String getServiceHost() {
        return ConvertUtils.getString(getValue(SERVICE_HOST, null));
    }

    public static String getBaiduAppKey() {
        return ConvertUtils.getString(getValue(BAIDU_APPKEY, ""));
    }

    public static boolean isKindergarten() {
        return ConvertUtils.getBoolean(getValue(IS_KINDERGARTEN, false));

    }

    public static boolean isDevelopment() {
        return ConvertUtils.getBoolean(getValue(IS_DEVELOPMENT, false));

    }

    public static boolean isDevelopTwo() {
        return ConvertUtils.getBoolean(getValue(DEVELOPER_TWO, true));
    }

    public static boolean isDevelopThree() {
        return ConvertUtils.getBoolean(getValue(DEVELOPER_THREE, true));
    }

    public static boolean isDevelopFour() {
        return ConvertUtils.getBoolean(getValue(DEVELOPER_FOUR, true));
    }


    public static String getVersion(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            String versionName = pi.versionName;
            if (versionName == null || versionName.length() <= 0) {
                return "1.0";
            }
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return "1.0";
        }
    }

    public static int getPageSize() {
        return ConvertUtils.getInteger(getValue(PAGE_SIZE, 10));
    }

    public static Object getValue(String metaKey, Object defaultVal) {
        if (metaKey == null) {
            return defaultVal;
        }
        Bundle metaData = null;
        Object value = getValueFromCache(metaKey);
        if (value != null) {
            return value;
        }
        try {
            ApplicationInfo ai = FmcApplication.getApplication().getPackageManager()
                    .getApplicationInfo(FmcApplication.getApplication().getPackageName(),
                            PackageManager.GET_META_DATA);
            if (null != ai) {
                metaData = ai.metaData;
            }
            if (null != metaData) {
                value = metaData.get(metaKey);
                configCacheMap.put(metaKey, value);
            }
        } catch (PackageManager.NameNotFoundException e) {

        }
        return value == null ? defaultVal : value;
    }

    private static Object getValueFromCache(String key) {
        if (configCacheMap.size() == 0) {
            return null;
        }
        return configCacheMap.get(key);
    }
}
