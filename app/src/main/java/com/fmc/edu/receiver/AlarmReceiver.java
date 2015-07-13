package com.fmc.edu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

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
}
