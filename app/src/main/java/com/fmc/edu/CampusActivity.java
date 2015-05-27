package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;


public class CampusActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this);
        setContentView(R.layout.activity_campus);
    }

}
