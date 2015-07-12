package com.fmc.edu.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.fmc.edu.service.StillStartService;

public class BootBroadcastReceiver extends BroadcastReceiver {
    public BootBroadcastReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("com.fmcedu.destroy")) {
            StillStartService.startStillStartService(context);
        }
    }
}
