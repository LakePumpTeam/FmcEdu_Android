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
    public String userName;
    public String comment;

    public static List<CommentItemEntity> toCommentEntityList(List<Map<String, Object>> commentList) {

        List<CommentItemEntity> list = new ArrayList<CommentItemEntity>();
        for (int i = 0; i < commentList.size(); i++) {
            Map<String, Object> commentItem = commentList.get(i);
            CommentItemEntity item = new CommentItemEntity();
            item.userId = ConvertUtils.getInteger(commentItem.get("userId"), 0);
            item.userName = ConvertUtils.getString(commentItem.get("userName"), "");
            item.comment = ConvertUtils.getString(commentItem.get("comment"), "");
        }
        return list;
    }

}
