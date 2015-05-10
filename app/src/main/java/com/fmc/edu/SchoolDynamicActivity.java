package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fmc.edu.adapter.SchoolListItemAdapter;
import com.fmc.edu.entity.SchoolDynamicEntity;

import java.util.ArrayList;
import java.util.List;


public class SchoolDynamicActivity extends Activity {

    private ListView list;
    private RadioGroup rgSchoolDynamicTab;
    private SchoolListItemAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_school_dynamic);
        initViews();
        initViewEvents();
        initPageData();
    }

    private void initViews() {
        list = (ListView) findViewById(R.id.school_dynamic_list);
        rgSchoolDynamicTab = (RadioGroup) findViewById(R.id.school_dynamic_rg_tab);
        ((RadioButton) rgSchoolDynamicTab.getChildAt(0)).setChecked(true);
    }

    private void initViewEvents() {
        rgSchoolDynamicTab.setOnCheckedChangeListener(rgSchoolDynamicTabOnCheckedChangeListener);
    }

    private void initPageData() {
        mAdapter = new SchoolListItemAdapter(this, getDynamicList());
        list.setAdapter(mAdapter);
    }

    private List<SchoolDynamicEntity> getDynamicList() {
        //TODO 调用接口 获取动态
        List<SchoolDynamicEntity> list = new ArrayList<>();
        SchoolDynamicEntity item = new SchoolDynamicEntity();
        item.content = "test";
        item.title = "test";
        item.date = "2015-05-10";
        list.add(item);
        return list;
    }

    private RadioGroup.OnCheckedChangeListener rgSchoolDynamicTabOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
//TODO tab变换时调用接口更新数据
        }
    };

}
