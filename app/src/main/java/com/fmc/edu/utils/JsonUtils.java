package com.fmc.edu.utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/8.
 */
public class JsonUtils {
    public static List<Map<String, Object>> getList(String jsonString) {
        if (StringUtils.isEmptyOrNull(jsonString)) {
            return Collections.EMPTY_LIST;
        }
        List<Map<String, Object>> list = Collections.EMPTY_LIST;

        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject;

            list = new ArrayList<Map<String, Object>>();

            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject = jsonArray.getJSONObject(i);
                list.add(getMap(jsonObject.toString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static Map<String, Object> getMap(String jsonString) {
        if (StringUtils.isEmptyOrNull(jsonString)) {
            return Collections.EMPTY_MAP;
        }

        JSONObject jsonObject;
        try {
            jsonObject = new JSONObject(jsonString);

            Iterator<String> keyIter = jsonObject.keys();
            String key;
            Object value;
            Map<String, Object> valueMap = new HashMap<String, Object>();

            while (keyIter.hasNext()) {
                key = (String) keyIter.next();
                value = jsonObject.get(key);
                valueMap.put(key, value.toString());
            }

            return valueMap;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return Collections.EMPTY_MAP;
    }
}
