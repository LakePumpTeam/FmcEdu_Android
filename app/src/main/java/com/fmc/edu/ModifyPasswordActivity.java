package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.util.HashMap;
import java.util.Map;


public class ModifyPasswordActivity extends BaseActivity {
    private Button btnSubmit;
    private EditText editOldPassword;
    private EditText editPassword;
    private EditText editConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_modify_password);
        initViews();
        initViewEvents();
    }

    private void initViews() {
        btnSubmit = (Button) findViewById(R.id.modify_password_btn_submit);
        editOldPassword = (EditText) findViewById(R.id.modify_password_edit_old);
        editPassword = (EditText) findViewById(R.id.modify_password_edit_new);
        editConfirmPassword = (EditText) findViewById(R.id.modify_password_edit_confirm);
    }

    private void initViewEvents() {
        btnSubmit.setOnClickListener(btnSubmitOnClickListener);
    }

    private View.OnClickListener btnSubmitOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String password = editPassword.getText().toString();
            String confirmPassword = editConfirmPassword.getText().toString();
            if (password.length() < 6 || password.length() > 16) {
                ToastToolUtils.showLong("请输入6-16位的密码");
                return;
            }
            if (!password.equals(confirmPassword)) {
                ToastToolUtils.showLong("两次密码输入不一致");
                return;
            }
            doSubmitOnClick();
        }
    };

    private void doSubmitOnClick() {
        mProgressControl.showWindow();
        Map<String, Object> params = getSubmitParams();
        MyIon.httpPost(this, "profile/requestAlterPwd", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                ToastToolUtils.showLong("修改成功!");
                afterModifyPassword();
            }
        });
    }

    private Map<String, Object> getSubmitParams() {
        Map<String, Object> data = new HashMap<String, Object>();
        LoginUserEntity loginUser = ServicePreferenceUtils.getLoginUserByPreference(this);
        String md5OldPassword = StringUtils.MD5(loginUser.salt, editOldPassword.getText().toString());
        String md5Password = StringUtils.MD5(loginUser.salt, editPassword.getText().toString());
        data.put("userId", loginUser.userId);
        data.put("oldPassword", md5OldPassword);
        data.put("newPassword", md5Password);
        return data;
    }

    private void afterModifyPassword() {
        ServicePreferenceUtils.clearPasswordPreference(this);
        this.finish();
        FmcApplication.clearAllActiviy();
        Intent intent = new Intent(ModifyPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
