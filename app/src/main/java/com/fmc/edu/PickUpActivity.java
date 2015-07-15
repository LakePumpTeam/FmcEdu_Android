package com.fmc.edu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.fmc.edu.adapter.PickUpAdapter;
import com.fmc.edu.customcontrol.SlideListView;

import java.util.List;
import java.util.Map;


public class PickUpActivity extends BaseActivity {
    private SlideListView slideListView;
    private LinearLayout llBack;
    private LinearLayout llSetting;
    private LinearLayout llMsgList;
    private PickUpAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_pick_up);
        findViews();
        bindViewEvent();
        initData();
    }

    private void findViews() {
        slideListView = (SlideListView) findViewById(R.id.pick_up_slide_list);
        llBack = (LinearLayout) findViewById(R.id.pick_up_ll_back);
        llSetting = (LinearLayout) findViewById(R.id.pick_up_ll_setting);
        llMsgList = (LinearLayout) findViewById(R.id.pick_up_ll_msg);
    }

    private void bindViewEvent() {
        llBack.setOnClickListener(OnClickListener);
    }

    private void initData() {
        List<Map<String, Object>> list = (List<Map<String, Object>>) getIntent().getExtras().getSerializable("list");
        mAdapter = new PickUpAdapter(this, list);
        slideListView.setAdapter(mAdapter);
    }

    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pick_up_ll_back:
                    finish();
                    break;
                case R.id.pick_up_ll_setting:
                    Intent intent = new Intent(PickUpActivity.this, CardSettingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.pick_up_ll_msg:
                    Intent intent1 = new Intent(PickUpActivity.this, MessageListActivity.class);
                    startActivity(intent1);
                    break;
            }
        }
    };
}
