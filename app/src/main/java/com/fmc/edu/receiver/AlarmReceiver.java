package com.fmc.edu.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.fmc.edu.service.StillStartService;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("fmcedu.alarm.action")) {
            StillStartService.startStillStartService(context);
        }
    }

    public  static void startAlarmReceiver(Context context){
        Intent alarmIntent = new Intent(context, AlarmReceiver.class);
        alarmIntent.setAction("fmcedu.alarm.action");
        PendingIntent sender = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
        long firstime = SystemClock.elapsedRealtime();
        AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        // 10秒一个周期，不停的发送广播
        am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,10 * 1000, sender);
    }
}
