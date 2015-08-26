package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fmc.edu.customcontrol.AlertWindowControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.enums.UserRoleEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.service.StillStartService;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.BaiduUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;

import java.util.HashMap;
import java.util.Map;


public class SettingActivity extends BaseActivity {

    private Button btnLoginOut;
    private TextView txtAbout;
    private TextView txtModifyPassword;
    private TextView txtNewMsgNotice;
    private TextView txtVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_setting);
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
            mProgressControl.showWindow();
            Map<String, Object> params = new HashMap<>();
            LoginUserEntity currentLoginUser = FmcApplication.getLoginUser();
            params.put("userId", currentLoginUser.userRole == UserRoleEnum.Parent ? currentLoginUser.studentId : currentLoginUser.userId);
            MyIon.httpPost(SettingActivity.this, "profile/requestLogout", params, mProgressControl, new MyIon.AfterCallBack() {
                @Override
                public void afterCallBack(Map<String, Object> data) {
                    BaiduUtils.stopStartWork(SettingActivity.this);
                    ServicePreferenceUtils.clearPasswordPreference(SettingActivity.this);
                    Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                    startActivity(intent);
                    SettingActivity.this.finish();
                    FmcApplication.clearAllActiviy();
                }
            });
        }
    };


    private View.OnClickListener txtAboutOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SettingActivity.this, AboutActivity.class);
            startActivity(intent);
        }
    };
    private View.OnClickListener txtModifyPasswordOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SettingActivity.this, ModifyPasswordActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener txtNewMsgNoticeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MessageNoticeSettingActivity.startMessageNoticeSettingActivity(SettingActivity.this);
        }
    };


    private View.OnClickListener txtVersionOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String version = AppConfigUtils.getVersion(SettingActivity.this);
            AlertWindowControl alertWindowControl = new AlertWindowControl(SettingActivity.this);
            alertWindowControl.showWindow(v, "版本信息", "当前版本:" + version);
        }
    };


}
