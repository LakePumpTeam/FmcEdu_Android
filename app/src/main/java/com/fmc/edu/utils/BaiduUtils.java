package com.fmc.edu.utils;

import android.app.Notification;
import android.content.Context;

import com.baidu.android.pushservice.BasicPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.fmc.edu.R;

import java.util.Map;

/**
 * Created by Candy on 2015/8/26.
 */
public class BaiduUtils {
    public static void baiduStartWork(Context context) {

        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder();
        builder.setStatusbarIcon(R.mipmap.send_msg_2);
        builder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);  //设置为自动消失
        Map<String, Boolean> settingData = ServicePreferenceUtils.getNoticeSettingByPreference(context);
        int defaultLights = Notification.DEFAULT_VIBRATE;
        if (!settingData.get("shake")) {
            builder.setNotificationVibrate(new long[]{0, 0, 0, 0});
        }
        if (settingData.get("ring")) {
        }
        builder.setNotificationDefaults(defaultLights);
        PushManager.setDefaultNotificationBuilder(context, builder);
        PushManager.startWork(context, PushConstants.LOGIN_TYPE_API_KEY, AppConfigUtils.getBaiduAppKey());
    }



    public static void stopStartWork(Context context) {
        if (PushManager.isConnected(context) && PushManager.isPushEnabled(context)) {
            PushManager.stopWork(context);
        }
    }
}
