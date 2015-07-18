package com.fmc.edu;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.fmc.edu.adapter.TeacherPickArrivalAdapter;
import com.fmc.edu.adapter.TeacherPickUnArrivalAdapter;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.entity.PickUpEntity;

import java.util.ArrayList;
import java.util.List;


public class TeacherPickActivity extends BaseActivity {
    private FrameLayout flMessage;
    private ImageView imgHasDynamic;
    private RadioGroup rgTab;
    private RadioButton rbArrival;
    private RadioButton rbUnArrival;
    private LinearLayout llBack;
    private LinearLayout llUnArrival;
    private LinearLayout llArrival;
    private SlideListView slideArrival;
    private ListView lvUnArrival;

    private TeacherPickArrivalAdapter mArrivalAdapter;
    private TeacherPickUnArrivalAdapter mUnArrivalAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_teacher_pick);
        findViews();
        initData();
        bindEvent();
    }

    private void findViews() {
        flMessage = (FrameLayout) findViewById(R.id.teacher_pick_fl_message);
        imgHasDynamic = (ImageView) findViewById(R.id.teacher_pick_img_has_dynamic);
        rgTab = (RadioGroup) findViewById(R.id.teacher_pick_rg_tab);
        rbArrival = (RadioButton) findViewById(R.id.teacher_pick_rb_arrival);
        rbUnArrival = (RadioButton) findViewById(R.id.teacher_pick_rb_un_arrival);
        llBack = (LinearLayout) findViewById(R.id.teacher_pick_ll_back);
        llArrival = (LinearLayout) findViewById(R.id.teacher_pick_ll_arrival);
        llUnArrival = (LinearLayout) findViewById(R.id.teacher_pick_ll_un_arrival);
        slideArrival = (SlideListView) findViewById(R.id.teacher_pick_slide_arrival);
        lvUnArrival = (ListView) findViewById(R.id.teacher_pick_lv_un_arrival);
    }

    private void bindEvent() {
        rgTab.setOnCheckedChangeListener(checkChangeListener);
        slideArrival.setOnLoadMoreListener(lodeMoreListener);
        llBack.setOnClickListener(onClickListener);
        flMessage.setOnClickListener(onClickListener);
    }

    private void initData() {
        rbUnArrival.setChecked(true);
        mArrivalAdapter = new TeacherPickArrivalAdapter(this, new ArrayList<PickUpEntity>());
        slideArrival.setAdapter(mArrivalAdapter);
        mUnArrivalAdapter = new TeacherPickUnArrivalAdapter(this, new ArrayList<PickUpEntity>());
        lvUnArrival.setAdapter(mUnArrivalAdapter);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.teacher_pick_ll_back:
                    TeacherPickActivity.this.finish();
                    break;
                case R.id.teacher_pick_fl_message:
                    MessageListActivity.startMessageActivity(TeacherPickActivity.this);
                    break;
            }
        }
    };

    private RadioGroup.OnCheckedChangeListener checkChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            if (checkedId == R.id.teacher_pick_rb_arrival) {
                llArrival.setVisibility(View.VISIBLE);
                llUnArrival.setVisibility(View.GONE);
                List<PickUpEntity> list = getArrivalData();
                mArrivalAdapter.addAllItems(list, true);
                mArrivalAdapter.notifyDataSetChanged();

            } else if (checkedId == R.id.teacher_pick_rb_un_arrival) {
                llArrival.setVisibility(View.GONE);
                llUnArrival.setVisibility(View.VISIBLE);
                List<PickUpEntity> list = getUnArrivalData();
                mUnArrivalAdapter.addAllItems(list, true);
                mUnArrivalAdapter.notifyDataSetChanged();
            }
        }
    };

    private SlideListView.OnLoadMoreListener lodeMoreListener = new SlideListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore(View footerView) {
            List<PickUpEntity> list = getArrivalData();
            mArrivalAdapter.addAllItems(list, true);
            mArrivalAdapter.notifyDataSetChanged();
        }
    };

    private List<PickUpEntity> getArrivalData() {
        List<PickUpEntity> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            PickUpEntity pickEntity = new PickUpEntity();
            pickEntity.date = "2015-07-18";
            pickEntity.week = "周三";
            pickEntity.time = "15:20:11";
            pickEntity.parentName = "张三" + i;
            pickEntity.parnetId = i + "";
            pickEntity.studentName = "张小三" + i;
            pickEntity.studentId = i + "";
            pickEntity.isArrival = i % 2 == 0;
            list.add(pickEntity);
        }
        return list;
    }

    private List<PickUpEntity> getUnArrivalData() {
        List<PickUpEntity> list = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            PickUpEntity pickEntity = new PickUpEntity();
            pickEntity.date = "2015-07-18";
            pickEntity.week = "周三";
            pickEntity.time = "15:20:11";
            pickEntity.parentName = "张三" + i;
            pickEntity.parnetId = i + "";
            pickEntity.studentName = "张小三" + i;
            pickEntity.studentId = i + "";
            pickEntity.isArrival = i % 2 == 0;
            list.add(pickEntity);
        }
        return list;
    }
}
