package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fmc.edu.adapter.DynamicItemAdapter;
import com.fmc.edu.common.Constant;
import com.fmc.edu.common.CrashHandler;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.SelectListControl;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.entity.ImageItemEntity;
import com.fmc.edu.entity.SchoolDynamicEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SchoolDynamicActivity extends Activity {

    private SlideListView slideListView;
    private RadioGroup rgSchoolDynamicTab;
    private DynamicItemAdapter mAdapter;
    private List<SchoolDynamicEntity> mList;
    private ProgressControl mProgressControl;
    private int mPageIndex = 1;
    private String mHostUrl;
    private int mCurrentTag = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this);
        setContentView(R.layout.activity_school_dynamic);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        mProgressControl = new ProgressControl(this);
        mHostUrl = AppConfigUtils.getServiceHost();
        mList = getIntent().getExtras().getParcelableArrayList("list");
        mList = getDynamicList();
        initViews();
        initViewEvents();
        initPageData();
    }

    private void initViews() {
        slideListView = (SlideListView) findViewById(R.id.school_dynamic_slide_list);
        rgSchoolDynamicTab = (RadioGroup) findViewById(R.id.school_dynamic_rg_tab);
        ((RadioButton) rgSchoolDynamicTab.getChildAt(0)).setChecked(true);
    }

    private void initViewEvents() {
        rgSchoolDynamicTab.setOnCheckedChangeListener(rgSchoolDynamicTabOnCheckedChangeListener);
        slideListView.setOnLoadMoreListener(slideLoadedMoreListener);
    }

    private void initPageData() {
        mAdapter = new DynamicItemAdapter(this, mList);
        slideListView.setAdapter(mAdapter);
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
            mPageIndex = 1;
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            mCurrentTag = ConvertUtils.getInteger(radioButton.getTag(), 2);
            getDynamicData();
        }
    };

    private SlideListView.OnLoadMoreListener slideLoadedMoreListener = new SlideListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore(View footerView) {
            mPageIndex++;
            getDynamicData();
        }
    };

    private void getDynamicData() {
        mProgressControl.showWindow(rgSchoolDynamicTab);
        String url = mHostUrl + "requestNewsList";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pageIndex", mPageIndex);
        params.put("pageSize", Constant.PAGE_SIZE);
        params.put("userId", FmcApplication.getLoginUser().userId);
        params.put("type", mCurrentTag);
        MyIon.httpPost(SchoolDynamicActivity.this, url, params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                if (null == data.get("newsList")) {
                    return;
                }
                List<SchoolDynamicEntity> list = (List<SchoolDynamicEntity>) data.get("newsList");
                afterGetDynamic(list);
            }
        });
    }

    private void afterGetDynamic(List<SchoolDynamicEntity> list) {
        if (mPageIndex == 1) {
            mAdapter.addAllItems(list, true);
            return;
        }
        mAdapter.addAllItems(list, false);
    }
}
