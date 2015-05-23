package com.fmc.edu.enums;

/**
 * Created by Candy on 2015/5/21.
 */
public enum DynamicTypeEnum {
    KidSchool(1), SchoolActivity(2), SchoolNotice(3), SchoolNews(4), ClassDynamic(5);

    private int value;

    private DynamicTypeEnum(int value) {
        this.value = value;
    }

    public static int getValue(DynamicTypeEnum dynamicTypeEnum) {
        switch (dynamicTypeEnum) {
            case KidSchool:
                return 1;
            case SchoolActivity:
                return 2;
            case SchoolNotice:
                return 3;
            case SchoolNews:
                return 4;
            case ClassDynamic:
                return 5;
            default:
                return 2;
        }
    }

    public static DynamicTypeEnum getEnumValue(int value) {
        switch (value) {
            case 1:
                return KidSchool;
            case 2:
                return SchoolActivity;
            case 3:
                return SchoolNotice;
            case 4:
                return SchoolNews;
            case 5:
                return ClassDynamic;
            default:
                return SchoolActivity;
        }

    }
}
