package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;

import com.fmc.edu.customcontrol.SlideListView;


public class CampusActivity extends BaseActivity {
    private SlideListView slideList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_campus);
        initViews();
    }

    private void initViews() {
        slideList = (SlideListView) findViewById(R.id.campus_slide_list);
    }

}
