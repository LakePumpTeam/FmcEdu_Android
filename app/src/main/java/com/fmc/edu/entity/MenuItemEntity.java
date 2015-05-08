package com.fmc.edu.entity;

/**
 * Created by Candy on 2015/5/8.
 */
public class MenuItemEntity {
    public String menuName;
    public int menuIconId;
    public boolean hasNewDynamic;

    public MenuItemEntity(String menuName, int menuIconId, boolean hasNewDynamic) {
        this.menuName = menuName;
        this.menuIconId = menuIconId;
        this.hasNewDynamic = hasNewDynamic;
    }
}
