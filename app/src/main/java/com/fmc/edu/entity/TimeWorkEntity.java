package com.fmc.edu.entity;

import com.fmc.edu.utils.ConvertUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/7/18.
 */
public class TimeWorkEntity implements Serializable {
    public String date;
    public String week;
    public String time;
    public boolean attendance;
    public String name;
    public String attendanceName;


    public static List<TimeWorkEntity> toTimeWorkList(List<Map<String, Object>> hasList) {
        List<TimeWorkEntity> list = new ArrayList<>();
        for (Map<String, Object> item : hasList) {
            TimeWorkEntity timeWortItem = toTimeWork(item);
            list.add(timeWortItem);
        }
        return list;
    }

    public static TimeWorkEntity toTimeWork(Map<String, Object> hasMap) {
        TimeWorkEntity timeWorkEntity = new TimeWorkEntity();
        timeWorkEntity.date = ConvertUtils.getFormatDateStr(ConvertUtils.getString(hasMap.get("date")), "yyyy-MM-dd");
        timeWorkEntity.week = ConvertUtils.getString(hasMap.get("week"), "");
        timeWorkEntity.time = ConvertUtils.getString(hasMap.get("time"), "");
        timeWorkEntity.name = ConvertUtils.getString(hasMap.get("name"), "");
        timeWorkEntity.attendance = ConvertUtils.getBoolean(hasMap.get("attendance"), false);
        timeWorkEntity.attendanceName =timeWorkEntity.attendance ? "进校" : "离校";
        return timeWorkEntity;
    }
    public static List<TimeWorkEntity> getEmptyTimeWorkEntityList() {
        List<TimeWorkEntity> TimeWorkList = new ArrayList<>();
        for (int i = 0; i < 15; i++) {
            TimeWorkEntity timeWorkEntity = new TimeWorkEntity();
            TimeWorkList.add(timeWorkEntity);
        }
        return TimeWorkList;
    }
}