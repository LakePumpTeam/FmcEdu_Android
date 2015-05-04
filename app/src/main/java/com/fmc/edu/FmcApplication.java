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

    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
    }

    public static FmcApplication getApplication() {
        return application;
    }
}
