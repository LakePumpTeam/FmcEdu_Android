package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;


public class SyllabusActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_syllabus);
    }


}
