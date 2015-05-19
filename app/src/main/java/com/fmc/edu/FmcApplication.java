package com.fmc.edu;

import android.app.Activity;
import android.app.Application;

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

    public static void exit() {
        clearAllActiviy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static void clearAllActiviy(){
        for (Activity activity : list) {
            if (activity != null && !activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    public static FmcApplication getApplication() {
        return application;
    }
}
