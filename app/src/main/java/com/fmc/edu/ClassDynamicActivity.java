package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;

import com.fmc.edu.common.CrashHandler;


public class ClassDynamicActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this);
        setContentView(R.layout.activity_class_dynamic);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

}
