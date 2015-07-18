package com.fmc.edu.enums;

/**
 * Created by Candy on 2015/7/17.
 */
public enum MessageTypeEnum {
    Remind(-1), Warning(0), Common(1);

    private int value;

    private MessageTypeEnum(int value) {
        this.value = value;
    }

    public static int getValue(MessageTypeEnum messageTypeEnum) {
        switch (messageTypeEnum) {
            case Remind:
                return -1;
            case Warning:
                return 0;
            case Common:
                return 1;
            default:
                return 1;
        }
    }

    public static MessageTypeEnum ConvertMessageType(int value) {
        switch (value) {
            case -1:
                return Remind;
            case 0:
                return Warning;
            case 1:
                return Common;
            default:
                return Common;
        }
    }

    public static String GetMessageTypeName(int value) {
        switch (value) {
            case -1:
                return "提醒 ";
            case 0:
                return "警告 ";
            case 1:
                return "";
            default:
                return "";
        }
    }
}
