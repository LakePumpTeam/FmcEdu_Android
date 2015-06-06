package com.fmc.edu.entity;

import com.fmc.edu.utils.ConvertUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/6/6.
 */
public class SyllabusEntity implements Serializable {
    public int syllabusId;
    public int sort;
    public String courseNum;
    public String courseName;
    public Date startTime;
    public Date endTime;

    public List<SyllabusEntity> toSyllabusEntity(List<Map<String, Object>> data) {
        List<SyllabusEntity> syllabusList = new ArrayList<>();
        for (Map<String, Object> item : data) {
            SyllabusEntity syllabusEntity = new SyllabusEntity();
            syllabusEntity.syllabusId = ConvertUtils.getInteger(item.get("syllabusId"), 0);
            syllabusEntity.sort = ConvertUtils.getInteger(item.get("sort"), 0);
            syllabusEntity.courseNum = ConvertUtils.getString(item.get("syllabusNum"), "");
            syllabusEntity.courseName = ConvertUtils.getString(item.get("subjectName"), "");
            Calendar calendar = Calendar.getInstance();
            syllabusEntity.startTime = ConvertUtils.getDate(item.get("startTime"), calendar.getTime());
            syllabusEntity.endTime = ConvertUtils.getDate(item.get("endTime"), calendar.getTime());
            syllabusList.add(syllabusEntity);
        }
        return syllabusList;
    }

}
