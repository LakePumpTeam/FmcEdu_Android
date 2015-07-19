package com.fmc.edu;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.fmc.edu.adapter.TimeWorkAdapter;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.entity.TimeWorkEntity;
import com.fmc.edu.utils.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class TimeWorkActivity extends BaseActivity {
    private SlideListView slideListView;
    private TimeWorkAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_time_work);
        findViews();
        initData();
    }

    private void findViews() {
        slideListView = (SlideListView) findViewById(R.id.time_work_slide_list);
    }

    private void initData() {
        List<TimeWorkEntity> list = (List<TimeWorkEntity>) getIntent().getExtras().getSerializable("list");
        mAdapter = new TimeWorkAdapter(this, list);
        slideListView.setAdapter(mAdapter);
    }

    public static void startTimeWorkActivity(Context context) {

//        mProgressControl.showWindow();
//        Map<String, Object> params = new HashMap<String, Object>();
//        LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
//        params.put("pageIndex", 1);
//        params.put("pageSize", Constant.PAGE_SIZE);
//        params.put("userId", loginUserEntity.userId);
//        MyIon.httpPost(MainActivity.this, "news/requestNewsList", params, mProgressControl, new MyIon.AfterCallBack() {
//            @Override
//            public void afterCallBack(Map<String, Object> data) {
//                menuCampus.setHasDynamic(false);
//                List<Map<String, Object>> list = ConvertUtils.getList(data.get("newsList"));
//                Bundle bundle = new Bundle();
//                bundle.putSerializable("list", (Serializable) DynamicItemEntity.toDynamicItemEntity(list));
//                bundle.putBoolean("isLastPage", ConvertUtils.getBoolean(data.get("isLastPage")));
//                Intent intent = new Intent(MainActivity.this, CampusActivity.class);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        });

        Intent intent = new Intent(context, TimeWorkActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", (Serializable) buildTimeWorkData());
        intent.putExtras(bundle);
        context.startActivity(intent);
    }


    private static List<TimeWorkEntity> buildTimeWorkData() {
        List<TimeWorkEntity> list = new ArrayList<>();

        for (int i = 0; i < 5; i++) {
            TimeWorkEntity item = new TimeWorkEntity();
            String date = "2015-07-0" + (i + 1);
            item.date = date;
            item.week = StringUtils.dayForWeek(date);
            item.time = "10:20:00";
            item.sign = i % 2 == 0;
            list.add(item);
        }
        return list;

    }
}
