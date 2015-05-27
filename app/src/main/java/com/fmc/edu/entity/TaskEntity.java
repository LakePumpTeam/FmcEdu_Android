package com.fmc.edu.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015-05-27.
 */
public class TaskEntity implements Serializable {
    public int taskId;
    public boolean status;
    public String subject;
    public String ManagerName;
    public String date;
    public String content;


    public static List<TaskEntity> toTaskEntityList(List<Map<String, Object>> list) {
        List<TaskEntity> taskList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> item = list.get(i);
            TaskEntity taskItem = new TaskEntity();
            taskList.add(taskItem);
        }

        return taskList;
    }
}
