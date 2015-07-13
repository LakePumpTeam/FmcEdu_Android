package com.fmc.edu.receiver;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;

import com.fmc.edu.utils.ToastToolUtils;

public class BootBroadcastReceiver extends BroadcastReceiver {
    public BootBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ToastToolUtils.showShort("启动了");
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {

            Intent alarmIntent = new Intent(context, AlarmReceiver.class);
            alarmIntent.setAction("fmcedu.alarm.action");
            PendingIntent sender = PendingIntent.getBroadcast(context, 0, alarmIntent, 0);
            long firstime = SystemClock.elapsedRealtime();
            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            // 10秒一个周期，不停的发送广播
            am.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, firstime,10 * 1000, sender);
        }
    }
}
