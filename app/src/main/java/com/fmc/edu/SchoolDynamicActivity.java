package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fmc.edu.adapter.DynamicItemAdapter;
import com.fmc.edu.common.CrashHandler;
import com.fmc.edu.entity.SchoolDynamicEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SchoolDynamicActivity extends Activity {

    private ListView list;
    private RadioGroup rgSchoolDynamicTab;
    private DynamicItemAdapter mAdapter;
    private List<SchoolDynamicEntity> mList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this);
        setContentView(R.layout.activity_school_dynamic);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        mList = getIntent().getExtras().getParcelableArrayList("list");
        mList = getDynamicList();
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
        mAdapter = new DynamicItemAdapter(this, mList);
        list.setAdapter(mAdapter);
    }

    private List<SchoolDynamicEntity> getDynamicList() {
        //TODO 调用接口 获取动态
        List<SchoolDynamicEntity> list = new ArrayList<>();
        SchoolDynamicEntity item = new SchoolDynamicEntity();
        item.newsId = 12;
        item.content = "测试内容";
        item.createDate = "2015-05-10";
        List<Map<String, String>> imageUrls = new ArrayList<Map<String, String>>();
        Map<String, String> itemUrl = new HashMap<String, String>();
        itemUrl.put("origUrl", "http://www.kenrockwell.com/canon/lenses/images/100mm/IMG_1906.JPG");
        itemUrl.put("thumbUrl", "http://www.kenrockwell.com/canon/lenses/images/100mm/IMG_1906.JPG");
        imageUrls.add(itemUrl);

        Map<String, String> itemUrl1 = new HashMap<String, String>();
        itemUrl1.put("origUrl", "http://www.kenrockwell.com/canon/lenses/images/100mm/IMG_1906.JPG");
        itemUrl1.put("thumbUrl", "http://www.kenrockwell.com/canon/lenses/images/100mm/IMG_1906.JPG");
        imageUrls.add(itemUrl1);

        Map<String, String> itemUrl2 = new HashMap<String, String>();
        itemUrl2.put("origUrl", "http://www.kenrockwell.com/canon/lenses/images/100mm/IMG_1906.JPG");
        itemUrl2.put("thumbUrl", "http://www.kenrockwell.com/canon/lenses/images/100mm/IMG_1906.JPG");
        imageUrls.add(itemUrl2);

        Map<String, String> itemUr3 = new HashMap<String, String>();
        itemUr3.put("origUrl", "http://www.kenrockwell.com/canon/lenses/images/100mm/IMG_1906.JPG");
        itemUr3.put("thumbUrl", "http://www.kenrockwell.com/canon/lenses/images/100mm/IMG_1906.JPG");
        imageUrls.add(itemUr3);
        item.imageUrls = imageUrls;
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
