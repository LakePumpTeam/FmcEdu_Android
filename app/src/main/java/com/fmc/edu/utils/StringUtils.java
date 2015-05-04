package com.fmc.edu.utils;

/**
 * Created by Candy on 2015/5/3.
 */
public class StringUtils {
    public static boolean isEmptyOrNull(String str) {
        if (null == str) {
            return true;
        }
        if ("".equals(str) || "null".equals(str)) {
            return true;
        }
        return false;
    }
}
