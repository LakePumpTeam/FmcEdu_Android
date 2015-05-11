package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fmc.edu.utils.ServicePreferenceUtils;


public class SettingActivity extends Activity {

    private Button btnLoginOut;
    private TextView txtAbout;
    private TextView txtModifyPassword;
    private TextView txtNewMsgNotice;
    private TextView txtVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initViews();
        initViewEvents();
    }

    private void initViews() {
        btnLoginOut = (Button) findViewById(R.id.setting_btn_login_out);
        txtAbout = (TextView) findViewById(R.id.setting_txt_about);
        txtModifyPassword = (TextView) findViewById(R.id.setting_txt_modify_password);
        txtNewMsgNotice = (TextView) findViewById(R.id.setting_txt_new_msg_notice);
        txtVersion = (TextView) findViewById(R.id.setting_txt_version);
    }

    private void initViewEvents() {
        btnLoginOut.setOnClickListener(btnLoginOutOnClickListener);
        txtAbout.setOnClickListener(txtAboutOnClickListener);
        txtModifyPassword.setOnClickListener(txtModifyPasswordOnClickListener);
        txtNewMsgNotice.setOnClickListener(txtNewMsgNoticeOnClickListener);
        txtVersion.setOnClickListener(txtVersionOnClickListener);
    }

    private View.OnClickListener btnLoginOutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ServicePreferenceUtils.clearLoginUserPreference(SettingActivity.this);
            Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
            startActivity(intent);
            SettingActivity.this.finish();
        }
    };


    private View.OnClickListener txtAboutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 关于
        }
    };
    private View.OnClickListener txtModifyPasswordOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 修改密码
        }
    };

    private View.OnClickListener txtNewMsgNoticeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 新消息通知设置
        }
    };


    private View.OnClickListener txtVersionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 版本信息
        }
    };


}