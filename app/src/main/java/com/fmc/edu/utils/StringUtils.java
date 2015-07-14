package com.fmc.edu.utils;

import android.util.Base64;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Candy on 2015/5/3.
 */
public class StringUtils {
    public static boolean isEmptyOrNull(Object str) {
        if (null == str) {
            return true;
        }
        if ("".equals(str.toString())) {
            return true;
        }
        if ("null".equals(str.toString())) {
            return true;
        }
        if (JSONObject.NULL == str) {
            return true;
        }
        return false;
    }

    public static String MD5(String salt, String strInput) {
        strInput = salt + strInput;
        StringBuffer buf = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(strInput.getBytes());
            byte b[] = md.digest();
            buf = new StringBuffer(b.length * 2);
            for (int i = 0; i < b.length; i++) {
                if (((int) b[i] & 0xff) < 0x10) {
                    buf.append("0");
                }
                buf.append(Long.toHexString((int) b[i] & 0xff));
            }
        } catch (NoSuchAlgorithmException ex) {
            ex.printStackTrace();
        }
        return buf.toString();
    }

    public static String base64Encode(Object obj) {
        if (StringUtils.isEmptyOrNull(obj)) {
            return "";
        }
        byte[] bytes = obj.toString().getBytes();
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static String dayForWeek(String pTime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(pTime));
            switch (c.get(Calendar.DAY_OF_WEEK)) {
                case 1:
                    return "星期天";
                case 2:
                    return "星期一";
                case 3:
                    return "星期二";
                case 4:
                    return "星期三";
                case 5:
                    return "星期四";
                case 6:
                    return "星期五";
                case 7:
                    return "星期六";
                default:
                    return "";

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }
}
