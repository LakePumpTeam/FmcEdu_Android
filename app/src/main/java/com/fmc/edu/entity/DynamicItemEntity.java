package com.fmc.edu.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.fmc.edu.enums.DynamicTypeEnum;
import com.fmc.edu.utils.ConvertUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/10.
 */
public class DynamicItemEntity implements Serializable {
    public int newsId;
    public int likeCount;
    public int commentCount;
    public DynamicTypeEnum type;
    public String subject;
    public String content;
    public String createDate;
    public List<ImageItemEntity> imageUrls;
    public List<CommentItemEntity> commentList;

    public static List<DynamicItemEntity> toDynamicItemEntity(List<Map<String, Object>> data) {
        List<DynamicItemEntity> list = new ArrayList<DynamicItemEntity>();
        for (int i = 0; i < data.size(); i++) {
            DynamicItemEntity dynamicItem = new DynamicItemEntity();
            Map<String, Object> item = data.get(i);
            dynamicItem.newsId = ConvertUtils.getInteger(item.get("newsId"));
            dynamicItem.subject = ConvertUtils.getString(item.get("subject"));
            dynamicItem.content = ConvertUtils.getString(item.get("content"));
            dynamicItem.createDate = ConvertUtils.getString(item.get("createDate"));
            dynamicItem.imageUrls = ImageItemEntity.initImageItemEntity(ConvertUtils.getList(item.get("imageUrls")));
            dynamicItem.likeCount = ConvertUtils.getInteger(item.get("like"), 0);
            dynamicItem.commentCount = ConvertUtils.getInteger(item.get("commentCount"), 0);
            dynamicItem.type = DynamicTypeEnum.getEnumValue(ConvertUtils.getInteger(item.get("type"), 0));
            List<Map<String, Object>> commentList = (List<Map<String, Object>>) item.get("commentList");
            dynamicItem.commentList = CommentItemEntity.toCommentEntityList(commentList);
            list.add(dynamicItem);
        }
        return list;
    }
}
