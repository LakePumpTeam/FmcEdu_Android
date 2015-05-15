package com.fmc.edu;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.fmc.edu.adapter.SelectListControlAdapter;
import com.fmc.edu.adapter.WaitAuditAdapter;
import com.fmc.edu.customcontrol.TopBarControl;

import java.util.List;
import java.util.Map;


public class WaitAuditActivity extends Activity {

    private TopBarControl topBar;
    private ListView list;
    private WaitAuditAdapter mWaitAuditAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wait_audit);
        initViews();
        initViewEvents();
        initData();
    }

    private void initViews() {
        topBar = (TopBarControl) findViewById(R.id.wait_audit_top_bar);
        list = (ListView) findViewById(R.id.wait_audit_list);
    }

    private void initViewEvents() {
        topBar.setOnOperateOnClickListener(allPassOperateListener);
    }

    private void initData() {
        mWaitAuditAdapter = new WaitAuditAdapter(this, getWaitList());
        list.setAdapter(mWaitAuditAdapter);
    }

    private TopBarControl.OnOperateOnClickListener allPassOperateListener = new TopBarControl.OnOperateOnClickListener() {
        @Override
        public void onOperateClick(View v) {
//TODO 批量审核调用接口
        }
    };


    private List<Map<String, Object>> getWaitList() {
        //TODO 获取待审核列表
        return null;
    }

}
