package com.fmc.edu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.fmc.edu.adapter.TimeWorkAdapter;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.entity.TimeWorkEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.ConvertUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TimeWorkActivity extends BaseActivity {
    private SlideListView slideListView;
    private TimeWorkAdapter mAdapter;
    private int mPageIndex = 1;
    private boolean mIsLastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_time_work);
        findViews();
        initData();
        bindViewEvent();
    }

    private void findViews() {
        slideListView = (SlideListView) findViewById(R.id.time_work_slide_list);
    }

    private void bindViewEvent() {
        slideListView.setOnLoadMoreListener(slideLoadedMoreListener);
    }

    private void initData() {
        mPageIndex = 1;
        List<TimeWorkEntity> list = (List<TimeWorkEntity>) getIntent().getExtras().getSerializable("list");
        mAdapter = new TimeWorkAdapter(this, list);
        slideListView.setAdapter(mAdapter);
    }

    private SlideListView.OnLoadMoreListener slideLoadedMoreListener = new SlideListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore(View footerView) {
            if (mIsLastPage) {
                return;
            }
            mPageIndex++;
            slideListView.setFooterViewVisible(true);
            getTimeWorkList();
        }
    };


    private void getTimeWorkList() {
        Map<String, Object> params = new HashMap<>();
        LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
        params.put("pageIndex", mPageIndex);
        params.put("studentId", loginUserEntity.studentId);
        params.put("type", 0);
        MyIon.httpPost(TimeWorkActivity.this, "clock/in/clockInRecords", params, null, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                List<Map<String, Object>> list = ConvertUtils.getList(data.get("record"));
                mIsLastPage = ConvertUtils.getBoolean(data.get("isLastPage"));
                List<TimeWorkEntity> timeWorkList = TimeWorkEntity.toTimeWorkList(list);
                mAdapter.addAllItems(timeWorkList, false);
            }
        });
    }


    public static void startTimeWorkActivity(final BaseActivity activity) {
        activity.mProgressControl.showWindow();
        Map<String, Object> params = new HashMap<>();
        LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
        params.put("pageIndex", 1);
        params.put("studentId", loginUserEntity.studentId);
        params.put("type", 0);
        MyIon.httpPost(activity, "clock/in/clockInRecords", params, activity.mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                List<Map<String, Object>> list = ConvertUtils.getList(data.get("record"));
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) TimeWorkEntity.toTimeWorkList(list));
                bundle.putBoolean("isLastPage", ConvertUtils.getBoolean(data.get("isLastPage")));
                Intent intent = new Intent(activity, TimeWorkActivity.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
    }

    public static void startNoticeMessageActivity(final Context context) {
        Map<String, Object> params = new HashMap<>();
        LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
        params.put("pageIndex", 1);
        params.put("studentId", loginUserEntity.studentId);
        params.put("type", 0);
        MyIon.httpPost(context, "clock/in/clockInRecords", params, null, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                List<Map<String, Object>> list = ConvertUtils.getList(data.get("record"));
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) TimeWorkEntity.toTimeWorkList(list));
                bundle.putBoolean("isLastPage", ConvertUtils.getBoolean(data.get("isLastPage")));
                Intent intent = new Intent(context, TimeWorkActivity.class);
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });


    }
}
