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
        List<CourseEntity> courseList = new ArrayList<>();
        for (Map<String, Object> item : data) {
            CourseEntity courseEntity = new CourseEntity();
            courseEntity.courseId = ConvertUtils.getInteger(item.get("courseId"), 0);
            courseEntity.order = ConvertUtils.getInteger(item.get("order"), 0);
            courseEntity.orderName = ConvertUtils.getString(item.get("orderName"));
            courseEntity.courseName = ConvertUtils.getString(item.get("courseName"), "");
            Calendar calendar = Calendar.getInstance();
            courseEntity.startTime = ConvertUtils.getTime(item.get("startTime"), calendar.getTime());
            courseEntity.endTime = ConvertUtils.getTime(item.get("endTime"), calendar.getTime());
            courseList.add(courseEntity);
        }
        return courseList;
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

    public static List<CourseEntity> cloneCourseList(List<CourseEntity> list) {
        List<CourseEntity> cloneList = new ArrayList<>();
        for (CourseEntity item : list) {
            cloneList.add(clone(item));
        }
        return cloneList;
    }

    public static CourseEntity clone(CourseEntity courseEntity) {
        CourseEntity cloneCourse = new CourseEntity();
        cloneCourse.courseId = courseEntity.courseId;
        cloneCourse.order = courseEntity.order;
        cloneCourse.orderName = courseEntity.orderName;
        cloneCourse.courseName = courseEntity.courseName;
        cloneCourse.startTime = courseEntity.startTime;
        cloneCourse.endTime = courseEntity.endTime;
        return cloneCourse;
    }
}
