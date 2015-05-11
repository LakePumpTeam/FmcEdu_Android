package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.http.FMCMapFutureCallback;
import com.fmc.edu.http.HttpTools;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.util.HashMap;
import java.util.Map;


public class ModifyPasswordActivity extends Activity {
    private Button btnSubmit;
    private EditText editOldPassword;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private ProgressControl progressControl;
    private String mHostUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_password);
        initViews();
        initViewEvents();
        progressControl = new ProgressControl(this);
        mHostUrl = AppConfigUtils.getServiceHost();
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
            if (!password.equals(confirmPassword)) {
                ToastToolUtils.showLong("两次密码输入不一致");
                return;
            }
            doSubmitOnClick(v);
        }
    };

    private void doSubmitOnClick(View view) {
        //TODO 修改密码的路径
        progressControl.showWindow(view);
        String url = mHostUrl + "profile/requestRegisterConfirm";

        Map<String, Object> params = getSubmitParams();
        MyIon.setUrlAndBodyParams(this, url, params, progressControl)
                .setCallback(new FMCMapFutureCallback() {
                    @Override
                    public void onTranslateCompleted(Exception e, Map<String, ?> result) {
                        progressControl.dismiss();
                        if (!HttpTools.isRequestSuccessfully(e, result)) {
                            ToastToolUtils.showLong(result.get("msg").toString());
                            return;
                        }
                        afterModifyPassword();
                    }
                });
    }

    private Map<String, Object> getSubmitParams() {
        Map<String, Object> data = new HashMap<String, Object>();
        LoginUserEntity loginUser = ServicePreferenceUtils.getLoginUserByPreference(this);
        String md5OldPassword = StringUtils.MD5(loginUser.cellphone, editOldPassword.getText().toString());
        String md5Password = StringUtils.MD5(loginUser.cellphone, editPassword.getText().toString());
        String md5ConfirmPassword = StringUtils.MD5(loginUser.cellphone, editConfirmPassword.getText().toString());
        data.put("oldPassword", md5OldPassword);
        data.put("password", md5Password);
        data.put("confirmPassword", md5ConfirmPassword);
        return data;
    }

    private void afterModifyPassword() {
        ServicePreferenceUtils.clearPasswordPreference(this);
        Intent intent = new Intent(ModifyPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
