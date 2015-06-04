package com.fmc.edu.entity;

import com.fmc.edu.utils.ConvertUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/6/4.
 */
public class CampusSelectionEntity implements Serializable {
    public int selectionId;
    public String selection;
    public int sortOrder;// 摆放顺序
    public boolean isSelected;

    public static List<CampusSelectionEntity> toCampuseSelection(List<Map<String, Object>> data) {
        List<CampusSelectionEntity> list = new ArrayList<>();
        for (Map<String, Object> item : data) {
            CampusSelectionEntity campusSelectionEntity = new CampusSelectionEntity();
            campusSelectionEntity.selectionId = ConvertUtils.getInteger(item.get("selectionId"), 0);
            campusSelectionEntity.selection = ConvertUtils.getString(item.get("selection"), "");
            campusSelectionEntity.sortOrder = ConvertUtils.getInteger(item.get("sortOrder"), 0);
            campusSelectionEntity.isSelected = ConvertUtils.getBoolean(item.get("isSelected"), false);
            list.add(campusSelectionEntity);
        }
        return list;
    }
}
