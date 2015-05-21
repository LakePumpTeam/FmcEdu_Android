package com.fmc.edu.enums;

/**
 * Created by Candy on 2015/5/21.
 */
public enum DynamicTypeEnum {
    SchoolDynamic(0), SchoolActivity(1), SchoolNews(2), ClassDynamic(3);

    private int value;

    private DynamicTypeEnum(int value) {
        this.value = value;
    }

    public static int getValue(DynamicTypeEnum dynamicTypeEnum) {
        switch (dynamicTypeEnum) {
            case SchoolDynamic:
                return 0;
            case SchoolActivity:
                return 1;
            case SchoolNews:
                return 2;
            case ClassDynamic:
                return 2;
            default:
                return 0;
        }
    }
}
