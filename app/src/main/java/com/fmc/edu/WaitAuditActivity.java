package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.fmc.edu.adapter.WaitAuditAdapter;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WaitAuditActivity extends Activity {

    private TopBarControl topBar;
    private SlideListView list;

    private WaitAuditAdapter mWaitAuditAdapter;
    private ProgressControl mProgressControl;
    private String mHostUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_audit);
        mProgressControl = new ProgressControl(this);
        mHostUrl = AppConfigUtils.getServiceHost();
        initViews();
        initViewEvents();
        initData();
    }

    private void initViews() {
        topBar = (TopBarControl) findViewById(R.id.wait_audit_top_bar);
        list = (SlideListView) findViewById(R.id.wait_audit_slide_list);
    }

    private void initViewEvents() {
        topBar.setOnOperateOnClickListener(allPassOperateListener);
       // list.setOnLoadMoreListener(onLoadMoreListener);
    }

    private void initData() {
        mWaitAuditAdapter = new WaitAuditAdapter(this, getWaitList());
        list.setAdapter(mWaitAuditAdapter);
    }

    private TopBarControl.OnOperateOnClickListener allPassOperateListener = new TopBarControl.OnOperateOnClickListener() {
        @Override
        public void onOperateClick(View v) {
            mProgressControl.showWindow(topBar);
            LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(WaitAuditActivity.this);
            Map<String, Object> params = new HashMap<>();
            params.put("teacherId", loginUserEntity.userId);
            params.put("allPass", 1);
            MyIon.httpPost(WaitAuditActivity.this, mHostUrl + "profile/requestParentAuditAll", params, mProgressControl, new MyIon.AfterCallBack() {
                @Override
                public void afterCallBack(Map<String, Object> data) {
                }
            });
        }
    };


    private List<Map<String, Object>> getWaitList() {
        //TODO 获取待审核列表
        return null;
    }
}
