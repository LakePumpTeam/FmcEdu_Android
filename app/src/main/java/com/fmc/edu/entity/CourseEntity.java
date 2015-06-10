package com.fmc.edu.entity;

import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.StringUtils;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/6/6.
 */
public class CourseEntity implements Serializable {
    public int courseId;
    public int order;
    public String orderName;
    public String courseName;
    public Date startTime;
    public Date endTime;

    public static List<CourseEntity> toCourseList(List<Map<String, Object>> data) {
        List<CourseEntity> syllabusList = new ArrayList<>();
        for (Map<String, Object> item : data) {
            CourseEntity syllabusEntity = new CourseEntity();
            syllabusEntity.courseId = ConvertUtils.getInteger(item.get("courseId"), 0);
            syllabusEntity.order = ConvertUtils.getInteger(item.get("order"), 0);
            syllabusEntity.orderName = ConvertUtils.getString(item.get("orderName"));
            syllabusEntity.courseName = ConvertUtils.getString(item.get("courseName"), "");
            Calendar calendar = Calendar.getInstance();
            syllabusEntity.startTime = ConvertUtils.getDate(item.get("startTime"), calendar.getTime());
            syllabusEntity.endTime = ConvertUtils.getDate(item.get("endTime"), calendar.getTime());
            syllabusList.add(syllabusEntity);
        }
        return syllabusList;
    }


    public static List<Map<String, Object>> toMapByCourseList(List<CourseEntity> courseList) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        List<Map<String, Object>> result = new ArrayList<>();
        for (CourseEntity item : courseList) {
            Map<String, Object> map = new HashMap<>();
            map.put("courseId", item.courseId);
            map.put("order", item.order);
            map.put("orderName", item.orderName);
            map.put("courseName", item.courseName);
            map.put("startTime", StringUtils.isEmptyOrNull(item.startTime) ? null : dateFormat.format(item.startTime));
            map.put("endTime", StringUtils.isEmptyOrNull(item.endTime) ? null : dateFormat.format(item.endTime));
            result.add(map);
        }
        return result;
    }
}
