package com.fmc.edu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fmc.edu.adapter.CampusListAdapter;
import com.fmc.edu.common.Constant;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.entity.DynamicItemEntity;
import com.fmc.edu.enums.DynamicTypeEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.RequestCodeUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CampusActivity extends BaseActivity {
    private SlideListView slideList;
    private CampusListAdapter mAdapter;
    private List<DynamicItemEntity> mList;
    private int mPageIndex = 1;
    private boolean mIsLastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_campus);
        Bundle bundle = getIntent().getExtras();
        mList = (List<DynamicItemEntity>) bundle.getSerializable("list");
        mIsLastPage = bundle.getBoolean("isLastPage", false);
        initViews();
        initViewEvents();
        initPageData();
    }

    private void initViews() {
        slideList = (SlideListView) findViewById(R.id.campus_slide_list);
    }

    private void initViewEvents() {
        slideList.setOnLoadMoreListener(slideLoadedMoreListener);
    }

    private void initPageData() {
        mPageIndex = 1;
        mAdapter = new CampusListAdapter(this, mList);
        slideList.setAdapter(mAdapter);
    }

    private SlideListView.OnLoadMoreListener slideLoadedMoreListener = new SlideListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore(View footerView) {
            if (mIsLastPage) {
                return;
            }
            mPageIndex++;
            slideList.setFooterViewVisible(true);
            getCampusData();
        }
    };

    private void getCampusData() {
        mProgressControl.showWindow();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pageIndex", mPageIndex);
        params.put("pageSize", Constant.PAGE_SIZE);
        params.put("userId", FmcApplication.getLoginUser().userId);
        params.put("type", DynamicTypeEnum.getValue(DynamicTypeEnum.Campus));
        MyIon.httpPost(CampusActivity.this, "news/requestNewsList", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                if (null == data.get("newsList")) {
                    return;
                }
                List<DynamicItemEntity> list = DynamicItemEntity.toDynamicItemEntity((List<Map<String, Object>>) data.get("newsList"));
                mIsLastPage = ConvertUtils.getBoolean(data.get("isLastPage"));
                if (mPageIndex == 1) {
                    mAdapter.addAllItems(list, true);
                    return;
                }
                mAdapter.addAllItems(list, false);
                slideList.setFooterViewVisible(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        Bundle bundle = data.getExtras();
        if (requestCode == RequestCodeUtils.CAMPUS_DETAIL && bundle.getBoolean("isSubmit", false)) {
            mAdapter.updateParticipationCount(bundle.getInt("newsId"));
            return;
        }

    }
}
