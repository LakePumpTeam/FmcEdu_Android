package com.fmc.edu.enums;

/**
 * Created by Candy on 2015/5/14.
 */
public enum AuditStateTypeEnum {
    Auditing(0), Pass(1), UnPass(2);

    private int value;

    private AuditStateTypeEnum(int value) {
        this.value = value;
    }

    public static int getValue(AuditStateTypeEnum auditStateTypeEnum) {
        switch (auditStateTypeEnum) {
            case Auditing:
                return 0;
            case Pass:
                return 1;
            case UnPass:
                return 2;
            default:
                return 0;
        }
    }
}

