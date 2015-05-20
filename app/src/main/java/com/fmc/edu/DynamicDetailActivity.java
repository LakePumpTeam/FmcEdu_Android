package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;

import com.fmc.edu.common.CrashHandler;


public class DynamicDetailActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this);
        setContentView(R.layout.activity_dynamic_detail);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

}
