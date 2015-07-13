package com.fmc.edu;

import android.os.Bundle;


public class TimeWorkActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_time_work);
    }
}
