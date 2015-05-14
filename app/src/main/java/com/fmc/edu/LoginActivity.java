package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fmc.edu.customcontrol.AlertWindowControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.PromptWindowControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.entity.UserEntity;
import com.fmc.edu.enums.AuditStateTypeEnum;
import com.fmc.edu.http.HttpTools;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.http.NetWorkUnAvailableException;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.MapTokenTypeUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.fmc.edu.utils.ValidationUtils;
import com.koushikdutta.async.future.FutureCallback;

import java.util.HashMap;
import java.util.Map;

import static com.fmc.edu.customcontrol.PromptWindowControl.OnOperateOnClickListener;


public class LoginActivity extends Activity {
    private Button btnLogin;
    private EditText editCellphone;
    private EditText editPassword;
    private TextView txtForgetPassword;
    private TextView txtRegister;

    private int REQUEST_CODE_REGISTER = 1;
    private ProgressControl mProgressControl;
    private String mHostUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mProgressControl = new ProgressControl(this);
        mHostUrl = AppConfigUtils.getServiceHost();
        initViews();
        bindViewEvents();
    }

    private void initViews() {
        btnLogin = (Button) findViewById(R.id.login_btn_login);
        editCellphone = (EditText) findViewById(R.id.login_edit_cellphone);
        editPassword = (EditText) findViewById(R.id.login_edit_password);
        txtForgetPassword = (TextView) findViewById(R.id.login_txt_forget_password);
        txtRegister = (TextView) findViewById(R.id.login_txt_register);
    }

    private void bindViewEvents() {
        btnLogin.setOnClickListener(btnLoginOnClickListener);
        txtForgetPassword.setOnClickListener(txtForgetPasswordOnClickListener);
        txtRegister.setOnClickListener(txtRegisterOnClickListener);
    }

    private View.OnClickListener btnLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doLogin(v);
            //     afterLogin();
        }
    };

    private View.OnClickListener txtForgetPasswordOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            PromptWindowControl promptWindowControl = new PromptWindowControl(LoginActivity.this);
            promptWindowControl.setOnOperateOnClickListener(operateOnClickListener);
            promptWindowControl.showWindow(v, "忘记密码?", "你可以通过注册手机重置密码", "重置密码");
        }
    };


    private OnOperateOnClickListener operateOnClickListener = new OnOperateOnClickListener() {
        @Override
        public void onOperateOnClick() {
            Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener txtRegisterOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivityForResult(intent, REQUEST_CODE_REGISTER);
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == REQUEST_CODE_REGISTER) {
            this.finish();
        }
    }

    private void doLogin(View view) {
        String cellphone = editCellphone.getText().toString();
        String password = editPassword.getText().toString();
        if (StringUtils.isEmptyOrNull(cellphone) && ValidationUtils.isMobilePhone(cellphone)) {
            ToastToolUtils.showLong("请输入有效的电话号码");
            return;
        }
        if (StringUtils.isEmptyOrNull(password)) {
            ToastToolUtils.showLong("请输入密码");
            return;
        }
        if (password.length() < 6 || password.length() > 16) {
            ToastToolUtils.showLong("有效的密码是6-16位的数字或者字符");
            return;
        }
        mProgressControl.showWindow(view);
        String url = mHostUrl + "profile/requestLogin";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userAccount", editCellphone.getText().toString());
        params.put("password", StringUtils.MD5(cellphone, password));
        MyIon.httpPost(this, url, params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Object resultData) {
                afterLogin(resultData);
            }
        });
    }

    private void afterLogin(Object data) {
        if (StringUtils.isEmptyOrNull(data)) {
            return;
        }
        Map<String, Object> mapData = (Map<String, Object>) data;
        LoginUserEntity userEntity = LoginUserEntity.toLoginUserEntity(mapData);
        ServicePreferenceUtils.saveLoginUserPreference(this, userEntity);

        int auditState = ConvertUtils.getInteger(mapData.get("auditState"));
        if (auditState == AuditStateTypeEnum.getValue(AuditStateTypeEnum.Auditing)) {
            Intent intent = new Intent(LoginActivity.this, AuditingActivity.class);
            startActivity(intent);
            return;
        }
        if (auditState == AuditStateTypeEnum.getValue(AuditStateTypeEnum.Pass)) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            return;
        }
        ToastToolUtils.showLong("信息审核不通过");
    }
}

