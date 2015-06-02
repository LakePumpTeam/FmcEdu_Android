package com.fmc.edu.entity;

import com.fmc.edu.utils.ConvertUtils;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/6/2.
 */
public class CampusEntity implements Serializable {
    public int campusId;
    public boolean partined;
    public int partinCount;
    public String subject;
    public String content;
    public String date;
    public List<ImageItemEntity> imageUrls;

    public static List<CampusEntity> toCampusEntityList(List<Map<String, Object>> data) {
        List<CampusEntity> list = new ArrayList<CampusEntity>();
        for (int i = 0; i < data.size(); i++) {
            CampusEntity dynamicItem = new CampusEntity();
            Map<String, Object> item = data.get(i);
            dynamicItem.campusId = ConvertUtils.getInteger(item.get("campusId"));
            dynamicItem.subject = ConvertUtils.getString(item.get("subject"));
            dynamicItem.content = ConvertUtils.getString(item.get("content"));
            dynamicItem.date = ConvertUtils.getString(item.get("date"));
            dynamicItem.imageUrls = ImageItemEntity.initImageItemEntity(ConvertUtils.getList(item.get("imageUrls")));
            dynamicItem.partinCount = ConvertUtils.getInteger(item.get("partinCount"), 0);
            dynamicItem.partined = ConvertUtils.getBoolean(item.get("partined"), false);
            list.add(dynamicItem);
        }
        return list;
    }
}
