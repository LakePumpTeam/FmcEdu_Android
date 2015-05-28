package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;


public class BraceletIntroduceActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this);
        setContentView(R.layout.activity_bracelet_introduce);
    }

}
