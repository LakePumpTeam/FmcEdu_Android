package com.fmc.edu;

import android.os.Bundle;

import com.fmc.edu.adapter.TimeWorkAdapter;
import com.fmc.edu.customcontrol.SlideListView;

import java.util.List;
import java.util.Map;


public class TimeWorkActivity extends BaseActivity {
    private SlideListView slideListView;
    private TimeWorkAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_time_work);
        findViews();
        initData();
    }

    private void findViews() {
        slideListView = (SlideListView) findViewById(R.id.time_work_slide_list);
    }

    private void initData() {
        List<Map<String,Object>> list  = (List<Map<String, Object>>) getIntent().getExtras().getSerializable("list");
        mAdapter = new TimeWorkAdapter(this,list);
        slideListView.setAdapter(mAdapter);
    }
}
