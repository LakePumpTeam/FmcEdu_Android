package com.fmc.edu;

import android.content.Intent;
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
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.entity.PickUpEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private int mCurrentNoDataDays = 1;
    private int mArrivalTotalCount = 0;

    private TeacherPickArrivalAdapter mArrivalAdapter;
    private TeacherPickUnArrivalAdapter mUnArrivalAdapter;
    private int mPageIndex;

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
        getUnArrivalData();
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
            mProgressControl.showWindow();
            mCurrentNoDataDays = 1;
            mPageIndex = 1;
            if (checkedId == R.id.teacher_pick_rb_arrival) {
                mArrivalAdapter.clearItems();
                mArrivalTotalCount = 0;
                llArrival.setVisibility(View.VISIBLE);
                llUnArrival.setVisibility(View.GONE);
                getArrivalData();

            } else if (checkedId == R.id.teacher_pick_rb_un_arrival) {
                mUnArrivalAdapter.clearItems();
                llArrival.setVisibility(View.GONE);
                llUnArrival.setVisibility(View.VISIBLE);
                getUnArrivalData();
            }
        }
    };

    private SlideListView.OnLoadMoreListener lodeMoreListener = new SlideListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore(View footerView) {
            mPageIndex++;
            slideArrival.setFooterViewVisible(true);
            getArrivalData();
        }
    };

    private void getArrivalData() {
        ProgressControl progressControl = mPageIndex == 1 ? mProgressControl : null;
        Map<String, Object> params = new HashMap<>();
        LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
        params.put("pageIndex", mPageIndex);
        params.put("classId", loginUserEntity.classId);
        MyIon.httpPost(TeacherPickActivity.this, "clock/in/queryClockInParent", params, progressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                List<Map<String, Object>> list = ConvertUtils.getList(data.get("records"));
                mArrivalTotalCount += ConvertUtils.getInteger(data.get("parentCount"), 0);
                slideArrival.setFooterViewVisible(false);
                if (null == list || 0 == list.size()) {
                    ToastToolUtils.showShort("最近" + mCurrentNoDataDays * 7 + "天没有数据");
                    mCurrentNoDataDays++;
                    return;
                }
                rbArrival.setText("已到家长(" + (mArrivalTotalCount > 99 ? "99+" : mArrivalTotalCount) + ")");
                mCurrentNoDataDays = 1;
                List<PickUpEntity> pickUpList = PickUpEntity.toPickUpEntityList(list);
                boolean isClear = mPageIndex == 1;
                mArrivalAdapter.addAllItems(pickUpList, isClear);
            }
        });
    }

    private void getUnArrivalData() {
        Map<String, Object> params = new HashMap<>();
        LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
        params.put("classId", loginUserEntity.classId);
        MyIon.httpPost(TeacherPickActivity.this, "clock/in/queryNotClockInParent", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                List<Map<String, Object>> list = ConvertUtils.getList(data.get("records"));
                if (null == list || 0 == list.size()) {
                    ToastToolUtils.showShort("最近" + mCurrentNoDataDays * 7 + "天没有数据");
                    mCurrentNoDataDays++;
                    return;
                }
                rbUnArrival.setText("未到家长(" + data.get("parentCount") + ")");
                List<PickUpEntity> pickUpList = PickUpEntity.toPickUpEntityList(list);
               mUnArrivalAdapter.addAllItems(pickUpList, true);
            }
        });
    }

    public static void startTeacherPickUpActivity(final BaseActivity activity) {
        Intent intent = new Intent(activity, TeacherPickActivity.class);
        activity.startActivity(intent);
    }
}
