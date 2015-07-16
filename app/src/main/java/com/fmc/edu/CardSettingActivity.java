package com.fmc.edu;

import android.os.Bundle;
import android.widget.ListView;

import com.fmc.edu.adapter.CardSettingAdapter;

import java.util.List;
import java.util.Map;


public class CardSettingActivity extends BaseActivity {

    private ListView lvList;
    private CardSettingAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_card_setting);
        findViews();
        initData();
    }

    private void findViews() {
        lvList = (ListView) findViewById(R.id.card_setting_lv_list);
    }

    private void initData() {
        List<Map<String, Object>> list = (List<Map<String, Object>>) getIntent().getExtras().getSerializable("list");
        mAdapter = new CardSettingAdapter(this, list);
        lvList.setAdapter(mAdapter);
    }

}
