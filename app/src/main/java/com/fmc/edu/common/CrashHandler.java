package com.fmc.edu.common;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Looper;
import android.util.Log;

import com.fmc.edu.FmcApplication;
import com.fmc.edu.utils.ToastToolUtils;

/**
 * Created by Candy on 2015/5/17.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String TAG = "CrashHandler";
    private static CrashHandler INSTANCE = new CrashHandler();
    private Context mContext;
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return INSTANCE;
    }

    public void init(Context ctx) {
        mContext = ctx;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {
        System.out.println("uncaughtException");
//        ToastToolUtils.showLong(ex.getMessage());
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
//                ToastToolUtils.showLong(ex.getMessage());
                Log.e("CrashHandler", ex.getMessage());
                new AlertDialog.Builder(mContext).setTitle("提示").setCancelable(false)
                        .setMessage("程序崩溃...").setNeutralButton("我知道", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        System.exit(0);
                        FmcApplication.exit(mContext);

                    }
                })
                        .create().show();
                Looper.loop();
            }
        }.start();
    }

    /**
     * 自定义错误处理,收集错误信息 发送错误报告等操作均完. 发者根据自情况自定义异处理逻辑
     *
     * @param ex
     * @return true:处理该异信息;否则返false
     */
    private boolean handleException(Throwable ex) {
        if (ex == null) {
            return true;
        }
        return true;
    }
}
