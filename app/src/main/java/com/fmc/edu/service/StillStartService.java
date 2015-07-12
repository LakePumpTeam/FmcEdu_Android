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

import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;
import com.fmc.edu.FmcApplication;
import com.fmc.edu.MainActivity;
import com.fmc.edu.R;

import java.util.List;

public class StillStartService extends Service {
    public StillStartService() {
        if (PushManager.isConnected(FmcApplication.getApplication()) && PushManager.isPushEnabled(FmcApplication.getApplication())) {
            return;
        }
        PushManager.startWork(FmcApplication.getApplication(), PushConstants.LOGIN_TYPE_API_KEY, "SLHL6GPxiQo6niknDQEI1c1V");
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
        if (isServiceRunning(context)) {
            return;
        }

        Intent service = new Intent(context, StillStartService.class);
        context.startService(service);
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
        Intent intent = new Intent("com.fmcedu.destroy");
        sendBroadcast(intent);
    }
}
