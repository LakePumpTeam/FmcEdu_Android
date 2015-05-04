package com.fmc.edu.http;

import com.fmc.edu.utils.StringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/3.
 */
public class HttpTools {
    public static String SUCCESS_CODE = "0";

    private static String STATUS_MSG = "msg";
    private static String STATUS = "status";
    private static String DATA = "data";

    public static boolean isRequestSuccessfully(Exception e, Map<String, Object> data) {
        if (e != null) {
            return false;
        }
        String status = getStatus(data);
        if (SUCCESS_CODE.equals(status)) {
            return true;
        }
        return false;
    }

    public static String getStatus(Map<String, Object> data) {
        if (data == null || data.get(STATUS) == null) {
            return null;
        }
        return String.valueOf(Double.valueOf(data.get(STATUS).toString()).intValue());
    }

    public static String getStatusMsg(Exception e, Map<String, Object> data) {
        if (e != null) {
            return e.getMessage();
        }
        if (data == null) {
            return null;
        }
        return (String) data.get(STATUS_MSG);
    }

    public static Map<String, Object> getData(Map<String, Object> data) {
        if (data == null) {
            return Collections.EMPTY_MAP;
        }
        Map<String, Object> dataMap = (Map<String, Object>) data.get(DATA);
        if (dataMap == null) {
            return Collections.EMPTY_MAP;
        }
        return dataMap;
    }

    public static List<Map<String, Object>> getListMap(Map<String, Object> data) {
        if (data == null) {
            return Collections.EMPTY_LIST;
        }
        List<Map<String, Object>> listData = (List<Map<String, Object>>) data.get(DATA);
        if (listData == null) {
            return Collections.EMPTY_LIST;
        }
        return listData;
    }

    public static List<String> getList(Map<String, Object> data) {
        if (data == null) {
            return Collections.EMPTY_LIST;
        }
        List<String> listData = (List<String>) data.get(DATA);
        if (listData == null) {
            return Collections.EMPTY_LIST;
        }
        return listData;
    }

    public static boolean isValidId(String id) {
        if (StringUtils.isEmptyOrNull(id) || id.equals("0.0")) {
            return false;
        }
        return true;
    }
}
