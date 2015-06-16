package com.fmc.edu;

import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fmc.edu.adapter.SchoolDynamicItemAdapter;
import com.fmc.edu.common.Constant;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.entity.DynamicItemEntity;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.ConvertUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SchoolDynamicActivity extends BaseActivity {

    private SlideListView slideListView;
    private RadioGroup rgSchoolDynamicTab;
    private SchoolDynamicItemAdapter mAdapter;
    private List<DynamicItemEntity> mList;
    private int mPageIndex = 1;
    private int mCurrentTag = 2;
    private boolean mIsLastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_school_dynamic);
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
        mAdapter = new SchoolDynamicItemAdapter(this, mList);
        slideListView.setAdapter(mAdapter);
    }

    private RadioGroup.OnCheckedChangeListener rgSchoolDynamicTabOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            mPageIndex = 1;
            RadioButton radioButton = (RadioButton) findViewById(checkedId);
            mCurrentTag = ConvertUtils.getInteger(radioButton.getTag(), 2);
            mProgressControl.showWindow();
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
            slideListView.setFooterViewVisible(true);
            getDynamicData(false);
        }
    };


    private void getDynamicData(boolean isShowProgress) {
        Map<String, Object> params = new HashMap<String, Object>();
        LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
        params.put("pageIndex", mPageIndex);
        params.put("pageSize", Constant.PAGE_SIZE);
        params.put("userId", loginUserEntity.userId);
        params.put("type", mCurrentTag);
        params.put("classId", loginUserEntity.classId);
        MyIon.httpPost(SchoolDynamicActivity.this, "news/requestNewsList", params, isShowProgress ? mProgressControl : null, new MyIon.AfterCallBack() {
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
        slideListView.setFooterViewVisible(false);
    }
}
