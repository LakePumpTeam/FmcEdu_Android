package com.fmc.edu.entity;

import com.fmc.edu.enums.DynamicTypeEnum;

import java.util.List;

/**
 * Created by Candy on 2015/5/21.
 */
public class DynamicEntity {
    public int id;
    public String Title;
    public String Content;
    public List<String> ThumbnailList;
    public List<String> ArtWorkList;
    public String datetime;
    public DynamicTypeEnum type;
    public int commentCount;
}
