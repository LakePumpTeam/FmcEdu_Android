package com.fmc.edu.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Candy on 2015/7/17.
 */
public class PickUpEntity implements Serializable {
    public String date;
    public String week;
    public String time;
    public String parentName;
    public String parnetId;
    public String studentName;
    public String studentId;
    public boolean isArrival;


    public static List<PickUpEntity> toPickUpEntityList(List<Map<String,Object>> mapList){
        List<PickUpEntity> pickUpList = new ArrayList<>();
        for(Map<String,Object> mapItem : mapList){
            PickUpEntity pickUpItem = toPickUpEntity(mapItem);
            pickUpList.add(pickUpItem);
        }
        return  pickUpList;
    }

    private static PickUpEntity toPickUpEntity(Map<String, Object> mapItem) {
        PickUpEntity pickUpEntity = new PickUpEntity();
        return pickUpEntity;
    }
}
