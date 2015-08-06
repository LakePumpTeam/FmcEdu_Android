package com.fmc.edu.service;

import android.app.ActivityManager;
import android.app.Application;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.baidu.android.pushservice.BasicPushNotificationBuilder;
import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.fmc.edu.FmcApplication;
import com.fmc.edu.MainActivity;
import com.fmc.edu.R;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.util.List;
import java.util.Map;

public class StillStartService extends Service {
    public StillStartService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        flags = START_STICKY;
        return super.onStartCommand(intent, flags, startId);
    }


    public static void startStillStartService(Context context) {
        baiduStartWork(context);
        if (isServiceRunning(context)) {
            return;
        }
        Intent service = new Intent(context, StillStartService.class);
        context.startService(service);
    }

    public static void stopStillStartService(Context context) {
        Intent service = new Intent(context, StillStartService.class);
        if (!isServiceRunning(context)) {
            return;
        }
        context.stopService(service);
        PushManager.stopWork(context);
    }


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

    public static boolean isServiceRunning(Context context) {
        final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        final List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        for (ActivityManager.RunningServiceInfo runningServiceInfo : services) {
            if (runningServiceInfo.service.getClassName().equals(StillStartService.class.getName())) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void onDestroy() {
        Intent intent = new Intent("fmcedu.alarm.action");
        sendBroadcast(intent);
    }
}
