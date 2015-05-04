package com.fmc.edu.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Candy on 2015/5/4.
 */
public class ValidationUtils {
    private static String PHONE_REG_PATH = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";

    private static String EMAIL_REG_PATH = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";

    private static String DATA_PATTERN_1 = "\\d{4}-\\d{2}-\\d{2}";
    private static String DATE_PATTERN_2 = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))";

    public static boolean isMobilePhone(String phoneNo) {
        Pattern p = Pattern.compile(PHONE_REG_PATH);
        Matcher m = p.matcher(phoneNo);
        return m.matches();
    }

    public static boolean isEmail(String email) {
        Pattern p = Pattern.compile(EMAIL_REG_PATH);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    public static boolean isValidDate(String sDate) {
        if ((sDate != null)) {
            Pattern pattern = Pattern.compile(DATA_PATTERN_1);
            Matcher match = pattern.matcher(sDate);
            if (match.matches()) {
                pattern = Pattern.compile(DATE_PATTERN_2);
                match = pattern.matcher(sDate);
                return match.matches();
            } else {
                return false;
            }
        }
        return false;
    }
}
