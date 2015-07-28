package com.fmc.edu;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.fmc.edu.adapter.PickUpAdapter;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.entity.PickUpEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class PickUpActivity extends BaseActivity {
    private SlideListView slideListView;
    private LinearLayout llBack;
    private LinearLayout llSetting;
    private LinearLayout llMsgList;
    private PickUpAdapter mAdapter;
    private int mPageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_pick_up);
        findViews();
        bindViewEvent();
        initData();
    }

    private void findViews() {
        slideListView = (SlideListView) findViewById(R.id.pick_up_slide_list);
        llBack = (LinearLayout) findViewById(R.id.pick_up_ll_back);
        llSetting = (LinearLayout) findViewById(R.id.pick_up_ll_setting);
        llMsgList = (LinearLayout) findViewById(R.id.pick_up_ll_msg);
    }

    private void bindViewEvent() {
        llBack.setOnClickListener(OnClickListener);
        llSetting.setOnClickListener(OnClickListener);
        llMsgList.setOnClickListener(OnClickListener);
        slideListView.setOnLoadMoreListener(slideLoadedMoreListener);
    }

    private void initData() {
        List<PickUpEntity> list = (List<PickUpEntity>) getIntent().getExtras().getSerializable("list");
        if (null == list || 0 == list.size()) {
            ToastToolUtils.showShort("最近七天没有数据");
            return;
        }

        mAdapter = new PickUpAdapter(this, list);
        slideListView.setAdapter(mAdapter);
    }

    private View.OnClickListener OnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pick_up_ll_back:
                    finish();
                    break;
                case R.id.pick_up_ll_setting:
                    CardSettingActivity.startCardSettingActivity(PickUpActivity.this);
                    break;
                case R.id.pick_up_ll_msg:
                    MessageListActivity.startMessageActivity(PickUpActivity.this);
                    break;
            }
        }
    };


    private SlideListView.OnLoadMoreListener slideLoadedMoreListener = new SlideListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore(View footerView) {
            mPageIndex++;
            slideListView.setFooterViewVisible(true);
            getPickUpList();
        }
    };


    private void getPickUpList() {
        Map<String, Object> params = new HashMap<>();
        LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
        params.put("pageIndex", mPageIndex);
        params.put("studentId", loginUserEntity.studentId);
        params.put("type", 1);
        MyIon.httpPost(PickUpActivity.this, "clock/in/clockInRecords", params, null, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                List<Map<String, Object>> list = ConvertUtils.getList(data.get("record"));
                if (null == list || 0 == list.size()) {
                    ToastToolUtils.showShort("最近七天没有数据");
                    return;
                }
                List<PickUpEntity> pickUpList = PickUpEntity.toPickUpEntityList(list);
                mAdapter.addAllItems(pickUpList, false);
            }
        });
    }

    public static void startPickUpActivity(final BaseActivity activity) {
        activity.mProgressControl.showWindow();
        Map<String, Object> params = new HashMap<>();
        LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
        params.put("pageIndex", 1);
        params.put("studentId", loginUserEntity.studentId);
        params.put("type", 1 );
        MyIon.httpPost(activity, "clock/in/clockInRecords", params, activity.mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                List<Map<String, Object>> list = ConvertUtils.getList(data.get("record"));

                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) PickUpEntity.toPickUpEntityList(list));
                Intent intent = new Intent(activity, PickUpActivity.class);
                intent.putExtras(bundle);
                activity.startActivity(intent);
            }
        });
    }
}
