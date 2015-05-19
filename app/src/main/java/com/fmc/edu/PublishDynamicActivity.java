package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;

import com.fmc.edu.common.CrashHandler;


public class PublishDynamicActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        setContentView(R.layout.activity_publish_dynamic);
    }

}
