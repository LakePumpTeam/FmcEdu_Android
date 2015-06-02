package com.fmc.edu.entity;

import com.fmc.edu.utils.ConvertUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015-05-26.
 */
public class CommentItemEntity implements Serializable {
    public int userId;
    public int commentId;
    public String userName;
    public String comment;
    public boolean sex;
    public String relationShip;
    public String date;

    public static List<CommentItemEntity> toCommentEntityList(List<Map<String, Object>> commentList) {
        if (null == commentList) {
            return new ArrayList<CommentItemEntity>();
        }

        List<CommentItemEntity> list = new ArrayList<CommentItemEntity>();
        for (int i = 0; i < commentList.size(); i++) {
            Map<String, Object> commentItem = commentList.get(i);
            CommentItemEntity item = toCommentEntity(commentItem);
            list.add(item);
        }
        return list;
    }

    public static CommentItemEntity toCommentEntity(Map<String, Object> data) {
        CommentItemEntity commentItemEntity = new CommentItemEntity();
        commentItemEntity.commentId = ConvertUtils.getInteger(data.get("commentId"), 0);
        commentItemEntity.userId = ConvertUtils.getInteger(data.get("userId"), 0);
        commentItemEntity.userName = ConvertUtils.getString(data.get("userName"), "");
        commentItemEntity.comment = ConvertUtils.getString(data.get("comment"), "");
        commentItemEntity.sex = ConvertUtils.getBoolean(data.get("sex"), false);
        commentItemEntity.relationShip = ConvertUtils.getString(data.get("relationship"), "");
        commentItemEntity.date = ConvertUtils.getString(data.get("date"), "");
        return commentItemEntity;
    }

}
