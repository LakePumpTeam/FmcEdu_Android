package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fmc.edu.adapter.DynamicItemAdapter;
import com.fmc.edu.common.Constant;
import com.fmc.edu.common.CrashHandler;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.entity.DynamicItemEntity;
import com.fmc.edu.entity.ImageItemEntity;
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
    private List<DynamicItemEntity> mList;
    private ProgressControl mProgressControl;
    private int mPageIndex = 1;
    private String mHostUrl;
    private int mCurrentTag = 2;
    private boolean mIsLastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this);
        setContentView(R.layout.activity_school_dynamic);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        mProgressControl = new ProgressControl(this);
        mHostUrl = AppConfigUtils.getServiceHost();
        Bundle bundle = getIntent().getExtras();
        mList = (List<DynamicItemEntity>) bundle.getSerializable("list");
        mIsLastPage = bundle.getBoolean("isLastPage", false);
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

    private RadioGroup.OnCheckedChangeListener rgSchoolDynamicTabOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            mPageIndex = 1;
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            mCurrentTag = ConvertUtils.getInteger(radioButton.getTag(), 2);
            mProgressControl.showWindow(rgSchoolDynamicTab);
            getDynamicData(true);
        }
    };

    private SlideListView.OnLoadMoreListener slideLoadedMoreListener = new SlideListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore(View footerView) {
            if (mIsLastPage) {
                return;
            }
            mPageIndex++;
            getDynamicData(false);
        }
    };


    private void getDynamicData(boolean isShowProgress) {
        String url = mHostUrl + "news/requestNewsList";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pageIndex", mPageIndex);
        params.put("pageSize", Constant.PAGE_SIZE);
        params.put("userId", FmcApplication.getLoginUser().userId);
        params.put("type", mCurrentTag);
        MyIon.httpPost(SchoolDynamicActivity.this, url, params, isShowProgress ? mProgressControl : null, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                if (null == data.get("newsList")) {
                    return;
                }
                mIsLastPage = ConvertUtils.getBoolean(data.get("isLastPage"));
                afterGetDynamic(DynamicItemEntity.toDynamicItemEntity((List<Map<String, Object>>) data.get("newsList")));
            }
        });
    }

    private void afterGetDynamic(List<DynamicItemEntity> list) {
        if (mPageIndex == 1) {
            mAdapter.addAllItems(list, true);
            return;
        }
        mAdapter.addAllItems(list, false);
    }
}
