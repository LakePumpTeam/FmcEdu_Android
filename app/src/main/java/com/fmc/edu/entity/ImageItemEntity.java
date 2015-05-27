package com.fmc.edu.entity;

import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/5/19.
 */
public class ImageItemEntity implements Serializable {
    public String thumbUrl;
    public String origUrl;
    public boolean isCheck;

    public static List<ImageItemEntity> initImageItemEntity(List<Map<String, Object>> imageUrls) {
        List<ImageItemEntity> list = new ArrayList<ImageItemEntity>();
        for (int i = 0; i < imageUrls.size(); i++) {
            Map<String, Object> imageItem = imageUrls.get(i);
            ImageItemEntity imageItemEntity = new ImageItemEntity();
            imageItemEntity.origUrl = AppConfigUtils.getServiceHost() + ConvertUtils.getString(imageItem.get("origUrl"));
            imageItemEntity.thumbUrl = AppConfigUtils.getServiceHost() + ConvertUtils.getString(imageItem.get("thumbUrl"));
            list.add(imageItemEntity);
        }
        return list;
    }

    public static List<String> getOrigUrlList(List<ImageItemEntity> list) {
        List<String> origUrls = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            origUrls.add(list.get(i).origUrl);
        }
        return origUrls;
    }
}
