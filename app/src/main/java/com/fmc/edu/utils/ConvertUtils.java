package com.fmc.edu.utils;

import java.math.BigDecimal;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/7.
 */
public class ConvertUtils {
    public static Integer getInteger(Object str) {
        return getInteger(str, null);
    }

    public static Integer getInteger(Object str, Integer def) {
        if (str == null || "null" == str) {
            return def;
        }

        return Integer.valueOf(Double.valueOf(str.toString()).intValue());
    }


    public static Long getLong(Object str) {

        return getLong(str, null);
    }

    public static Long getLong(Object str, Long defaultValue) {
        if (str == null || "null" == str) {
            return defaultValue;
        }
        BigDecimal db = new BigDecimal(ConvertUtils.getString(str, "0"));
        return Long.valueOf(db.toPlainString());
    }

    public static Float getFloat(Object str) {
        return getFloat(str, null);
    }

    public static Float getFloat(Object str, Float defaultValue) {
        if (str == null || "null" == str) {
            return defaultValue;
        }

        return Float.valueOf(str.toString());
    }

    public static Double getDouble(Object str) {

        return getDouble(str, null);
    }

    public static Double getDouble(Object str, Double defaultValue) {
        if (str == null || "null" == str) {
            return defaultValue;
        }

        return Double.valueOf(str.toString());
    }

    public static Boolean getBoolean(Object str) {

        return getBoolean(str, null);
    }

    public static Boolean getBoolean(Object str, Boolean defaultValue) {
        if (str == null || "null" == str) {
            return defaultValue;
        }

        return Boolean.valueOf(str.toString());
    }

    public static Date getDate(Object str) {
        return getDate(str, null);
    }

    public static Date getDate(Object str, Date defaultValue) {
        if (str == null || "null" == str) {
            return defaultValue;
        }
        return new Date(java.util.Date.parse(str.toString()));
    }

    public static String getString(Object obj) {
        return getString(obj, "");
    }

    public static String getString(Object obj, String defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        String value = String.valueOf(obj);
        if ("null".equalsIgnoreCase(value)) {
            return defaultValue;
        }
        return value;
    }

    public static String getIDString(Object obj, String defaultValue) {
        if (obj == null || "null" == obj) {
            return defaultValue;
        }

        return getString(getInteger(obj));

    }

    public static String getIDString(Object obj) {
        return getIDString(obj, "0");

    }

    public static List<Map<String, Object>> getList(Object obj) {
        if (null == obj || "null" == obj) {
            return new ArrayList<Map<String, Object>>();
        }
        return (List<Map<String, Object>>) obj;
    }

    public static java.util.Date getDate(Object str, java.util.Date defaultValue) {
        if (str == null || "null".equals(str)) {
            return defaultValue;
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return simpleDateFormat.parse(getString(str));
        } catch (ParseException e) {
            e.printStackTrace();
            return defaultValue;
        }
    }

    public static String getFormatDateStr(java.util.Date date, String format) {
        if (date == null || date.equals("null")) {
            return "";
        }
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    public static String getFormatDateStr(Long dateLong, String format) {
        Date date = new Date(dateLong);
        return getFormatDateStr(date, format);
    }


    public static String getFormatDateStr(String dateStr, String format) {
        if (dateStr == null || dateStr.equals("null")) {
            return "";
        }

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            java.util.Date date = simpleDateFormat.parse(dateStr);
            return simpleDateFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }
}
