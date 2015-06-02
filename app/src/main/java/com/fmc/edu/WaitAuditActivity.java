package com.fmc.edu;

import android.os.Bundle;
import android.view.View;

import com.fmc.edu.adapter.WaitAuditAdapter;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.entity.WaitAuditEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class WaitAuditActivity extends BaseActivity {

    private TopBarControl topBar;
    private SlideListView slideList;

    private WaitAuditAdapter mWaitAuditAdapter;
    private String mHostUrl;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_wait_audit);
        mBundle = getIntent().getExtras();
        mHostUrl = AppConfigUtils.getServiceHost();
        initViews();
        initViewEvents();
        initData();
    }

    private void initViews() {
        topBar = (TopBarControl) findViewById(R.id.wait_audit_top_bar);
        slideList = (SlideListView) findViewById(R.id.wait_audit_slide_list);
    }

    private void initViewEvents() {
        topBar.setOnOperateOnClickListener(allPassOperateListener);
    }

    private void initData() {
        if (null == mBundle) {
            return;
        }
        List<WaitAuditEntity> list = mBundle.getParcelableArrayList("list");
        mWaitAuditAdapter = new WaitAuditAdapter(WaitAuditActivity.this, list);
        slideList.setAdapter(mWaitAuditAdapter);
    }

    private TopBarControl.OnOperateOnClickListener allPassOperateListener = new TopBarControl.OnOperateOnClickListener() {
        @Override
        public void onBackClick(View view) {

        }

        @Override
        public void onOperateClick(View v) {
            mProgressControl.showWindow();
            LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(WaitAuditActivity.this);
            Map<String, Object> params = new HashMap<>();
            params.put("teacherId", loginUserEntity.userId);
            params.put("allPass", 1);
            MyIon.httpPost(WaitAuditActivity.this, mHostUrl + "profile/requestParentAuditAll", params, mProgressControl, new MyIon.AfterCallBack() {
                @Override
                public void afterCallBack(Map<String, Object> data) {
                    ToastToolUtils.showLong("审核成功");
                    WaitAuditActivity.this.finish();
                }
            });
        }
    };
}
