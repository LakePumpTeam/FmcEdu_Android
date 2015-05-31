package com.fmc.edu.entity;

import com.fmc.edu.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/30.
 */
public class MultiCommonEntity {

    public boolean isCheck;
    public int id;
    public String name;

    public static List<MultiCommonEntity> toMultiCommonList(List<Map<String, Object>> list) {
        List<MultiCommonEntity> multiCommonList = new ArrayList<>();
        for (Map<String, Object> item : list) {
            MultiCommonEntity multiCommonEntity = new MultiCommonEntity();
            multiCommonEntity.isCheck = ConvertUtils.getBoolean(item.get("isCheck"), false);
            multiCommonEntity.id = ConvertUtils.getInteger(item.get("studentId"), 0);
            multiCommonEntity.name = ConvertUtils.getString(item.get("name"));
            multiCommonList.add(multiCommonEntity);
        }
        return multiCommonList;
    }

}
