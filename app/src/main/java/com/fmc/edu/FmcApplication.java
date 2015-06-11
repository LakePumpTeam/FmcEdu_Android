package com.fmc.edu;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.fmc.edu.common.CrashHandler;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.utils.ServicePreferenceUtils;

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
        activity.setContentView(view);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true, activity);
        }
        activity.mProgressControl = new ProgressControl(activity, view);
    }

    @TargetApi(19)
    private static void setTranslucentStatus(boolean on, BaseActivity activity) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    public static void addActivity(BaseActivity activity, View view) {
        addActivity(activity);
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
