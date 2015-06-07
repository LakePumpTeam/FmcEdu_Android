package com.fmc.edu.entity;

import com.fmc.edu.utils.ConvertUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/6/6.
 */
public class SyllabusEntity implements Serializable {
    public int courseId;
    public int order;
    public String orderName;
    public String courseName;
    public Date startTime;
    public Date endTime;

    public static List<SyllabusEntity> toSyllabusEntity(List<Map<String, Object>> data) {
        List<SyllabusEntity> syllabusList = new ArrayList<>();
        for (Map<String, Object> item : data) {
            SyllabusEntity syllabusEntity = new SyllabusEntity();
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

    public Map<String, Object> toMapBySyllabus() {
        Map<String, Object> map = new HashMap<>();
        map.put("courseId", this.courseId);
        map.put("order", this.courseId);
        map.put("orderName", this.courseId);
        map.put("courseName", this.courseId);
        map.put("startTime", this.courseId);
        map.put("endTime", this.courseId);
        return map;
    }
}
