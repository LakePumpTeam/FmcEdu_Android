package com.fmc.edu;

import android.app.Application;

/**
 * Created by Candy on 2015/5/3.
 */
public class FmcApplication extends Application {

    private static FmcApplication application;

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static FmcApplication getApplication() {
        return application;
    }
}
