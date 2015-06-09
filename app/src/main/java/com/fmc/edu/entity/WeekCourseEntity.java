package com.fmc.edu.entity;

import com.fmc.edu.utils.ConvertUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/6/9.
 */
public class WeekCourseEntity implements Serializable {
    public int week;
    public List<CourseEntity> courseList;

    public static List<WeekCourseEntity> toWeekCourseList(List<Map<String, Object>> list) {
        List<WeekCourseEntity> weekCourseList = new ArrayList<>();
        for (Map<String, Object> item : list) {
            WeekCourseEntity weekCourseItem = new WeekCourseEntity();
            weekCourseItem.week = ConvertUtils.getInteger(item.get("week"), 1);
            weekCourseItem.courseList = CourseEntity.toCourseList((List<Map<String, Object>>) item.get("courses"));
            weekCourseList.add(weekCourseItem);
        }
        return weekCourseList;
    }
}
