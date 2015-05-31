package com.fmc.edu.entity;

import com.fmc.edu.enums.UserRoleEnum;
import com.fmc.edu.utils.ConvertUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015-05-27.
 */
public class TaskEntity implements Serializable {
    public int taskId;
    public int status;
    public String subject;
    public String studentName;
    public int studentId;
    public String deadline;
    public String content;
    public UserRoleEnum userRole;
    public List<CommentItemEntity> commentList;


    public static List<TaskEntity> toTaskEntityList(List<Map<String, Object>> list) {
        List<TaskEntity> taskList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> item = list.get(i);
            TaskEntity taskItem = toTaskEntity(item);
            taskList.add(taskItem);
        }

        return taskList;
    }

    public static TaskEntity toTaskEntity(Map<String, Object> data) {
        TaskEntity taskEntity = new TaskEntity();
        taskEntity.taskId = ConvertUtils.getInteger(data.get("taskId"), 0);
        taskEntity.subject = ConvertUtils.getString(data.get("subject"));
        taskEntity.studentName = ConvertUtils.getString(data.get("studentName"));
        taskEntity.studentId = ConvertUtils.getInteger(data.get("studentId"));
        taskEntity.deadline = ConvertUtils.getString(data.get("deadline"));
        taskEntity.status = ConvertUtils.getInteger(data.get("status"), 0);
        taskEntity.userRole = UserRoleEnum.getEnumValue(ConvertUtils.getInteger(data.get("userRole"), 0));
        taskEntity.commentList = CommentItemEntity.toCommentEntityList(ConvertUtils.getList(data.get("commentList")));
        return taskEntity;
    }
}
