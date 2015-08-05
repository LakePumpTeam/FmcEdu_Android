package com.fmc.edu.entity;

import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/7/17.
 */
public class PickUpEntity implements Serializable {
    public String date;
    public String week;
    public String time;
    public String parentName;
    public String parentId;
    public String studentName;
    public String studentId;


    public static List<PickUpEntity> toPickUpEntityList(List<Map<String, Object>> mapList) {
        List<PickUpEntity> pickUpList = new ArrayList<>();
        for (Map<String, Object> mapItem : mapList) {
            PickUpEntity pickUpItem = toPickUpEntity(mapItem);
            pickUpList.add(pickUpItem);
        }
        return pickUpList;
    }

    private static PickUpEntity toPickUpEntity(Map<String, Object> mapItem) {
        PickUpEntity pickUpEntity = new PickUpEntity();
        pickUpEntity.date = ConvertUtils.getString(mapItem.get("date"), "");
        pickUpEntity.week = StringUtils.dayForWeek(pickUpEntity.date);
        pickUpEntity.time = ConvertUtils.getString(mapItem.get("time"), "");
        pickUpEntity.parentName = ConvertUtils.getString(mapItem.get("name"), "");
        pickUpEntity.parentId = ConvertUtils.getString(mapItem.get("parentId"), "");
        pickUpEntity.studentName = ConvertUtils.getString(mapItem.get("studentName"), "");
        pickUpEntity.studentId = ConvertUtils.getString(mapItem.get("studentId"), "");
        return pickUpEntity;
    }
}
