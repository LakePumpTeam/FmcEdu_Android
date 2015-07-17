package com.fmc.edu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.fmc.edu.adapter.PickUpAdapter;
import com.fmc.edu.customcontrol.SlideListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
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
        llMsgList = (LinearLayout) findViewById(R.id.pick_up_ll_msg);
    }

    private void bindViewEvent() {
        llBack.setOnClickListener(OnClickListener);
        llSetting.setOnClickListener(OnClickListener);
        llMsgList.setOnClickListener(OnClickListener);
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
                    gotoCardSettingActivity();
                    break;
                case R.id.pick_up_ll_msg:
                    MessageListActivity.startMessageActivity(PickUpActivity.this);
                    break;
            }
        }
    };


    private void gotoCardSettingActivity() {
        List<Map<String, Object>> list = buildCardSettingData();
        Intent cardSettingIntent = new Intent(PickUpActivity.this, CardSettingActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", (Serializable) list);
        cardSettingIntent.putExtras(bundle);
        startActivity(cardSettingIntent);
    }



    private List<Map<String, Object>> buildCardSettingData() {
        List<Map<String, Object>> list = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("cardNo", "100000" + i);
            item.put("parent", "家长" + i);
            item.put("isLose", i % 2 == 0);
            item.put("comment", "备注" + i);
            list.add(item);
        }
        return list;
    }
}
