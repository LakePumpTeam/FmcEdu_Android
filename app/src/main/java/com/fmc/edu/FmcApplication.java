package com.fmc.edu;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Candy on 2015/5/3.
 */
public class FmcApplication extends Application {

    private static FmcApplication application;
    private static List<Activity> list = new ArrayList<Activity>();

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static void addActivity(Activity activity) {
        list.add(activity);
    }

    public static void exit(Context context) {
        for (Activity activity : list) {
            if (activity != null && activity.isFinishing()) {
                activity.finish();
            }
        }
        ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityMgr.killBackgroundProcesses(context.getPackageName());
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static FmcApplication getApplication() {
        return application;
    }
}
