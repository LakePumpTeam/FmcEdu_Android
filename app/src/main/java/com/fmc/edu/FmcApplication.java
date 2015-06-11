package com.fmc.edu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import com.fmc.edu.common.CrashHandler;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.utils.ServicePreferenceUtils;
import com.readystatesoftware.systembartint.SystemBarTintManager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Candy on 2015/5/3.
 */
public class FmcApplication extends Application {

    private static FmcApplication application;
    private static List<Activity> list = new ArrayList<Activity>();
    public LoginUserEntity mLoginUserEntity;
    public static ProgressControl mProgressControl;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static LoginUserEntity getLoginUser() {
        return ServicePreferenceUtils.getLoginUserByPreference(application);
    }

    public static void addActivity(BaseActivity activity, int layoutId) {
        addActivity(activity);
        View view = LayoutInflater.from(activity).inflate(layoutId, null);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, activity);

        }
        addActivity(activity, view);
    }

    @TargetApi(19)
    private static void setTranslucentStatus(boolean on, BaseActivity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintColor(activity.getResources().getColor(R.color.activity_top_bar_color));
            tintManager.setStatusBarTintEnabled(true);
        }
    }

    public static void addActivity(BaseActivity activity, View view) {
        addActivity(activity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            SystemBarTintManager tintManager = new SystemBarTintManager(activity);
            tintManager.setStatusBarTintColor(activity.getResources().getColor(R.color.activity_top_bar_color));
            tintManager.setStatusBarTintEnabled(true);
        }
        activity.setContentView(view);
        activity.mProgressControl = new ProgressControl(activity, view);
    }

    private static void addActivity(Activity activity) {
        list.add(activity);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(activity);
    }

    public static void exit() {
        clearAllActiviy();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

    public static void clearAllActiviy() {
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
