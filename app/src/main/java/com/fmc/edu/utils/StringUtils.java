package com.fmc.edu.utils;

import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

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
        if (JSONObject.NULL ==str) {
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
}
