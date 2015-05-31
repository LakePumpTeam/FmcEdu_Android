package com.fmc.edu.enums;

/**
 * Created by Candy on 2015/5/30.
 */
public enum UserRoleEnum {
    Teacher(1), Parent(2);

    private int value;

    private UserRoleEnum(int value) {
        this.value = value;
    }

    public static int getValue(UserRoleEnum userRoleEnum) {
        switch (userRoleEnum) {
            case Teacher:
                return 1;
            case Parent:
                return 2;
            default:
                return 2;
        }
    }

    public static UserRoleEnum getEnumValue(int value) {
        switch (value) {
            case 1:
                return Teacher;
            case 2:
                return Parent;
            default:
                return Parent;
        }
    }
}
