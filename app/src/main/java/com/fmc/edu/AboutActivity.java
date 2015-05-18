package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;

import com.fmc.edu.common.CrashHandler;


public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
    }

}
