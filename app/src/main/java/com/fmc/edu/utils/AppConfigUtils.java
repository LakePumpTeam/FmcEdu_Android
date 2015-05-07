package com.fmc.edu.utils;

import android.content.pm.ApplicationInfo;
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
    private static Map<String, Object> configCacheMap = new HashMap<String, Object>(10);

    public static String getServiceHost() {
        return ConvertUtils.getString(getValue(SERVICE_HOST, null));
    }

    public static Object getValue(String metaKey, String defaultVal) {
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
