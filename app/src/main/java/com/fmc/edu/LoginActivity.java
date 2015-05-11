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
import com.fmc.edu.http.HttpTools;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.http.NetWorkUnAvailableException;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.MapTokenTypeUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.fmc.edu.utils.ValidationUtils;
import com.koushikdutta.async.future.FutureCallback;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initViews();
        bindViewEvents();
        mProgressControl = new ProgressControl(this);
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
//            doLogin(v);
            afterLogin();
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
        try {
            //TODO 路径没有配好
            mProgressControl.showWindow(view);
            MyIon.with(this)
                    .load(AppConfigUtils.getServiceHost() + "登录路径")
                    .setBodyParameter("cellphone", editCellphone.getText().toString())
                    .setBodyParameter("password", editPassword.getText().toString())
                    .as(new MapTokenTypeUtils())
                    .setCallback(new FutureCallback<Map<String, Object>>() {
                        @Override
                        public void onCompleted(Exception e, Map<String, Object> result) {
                            mProgressControl.dismiss();
                            if (!HttpTools.isRequestSuccessfully(e, result)) {
                                AlertWindowControl alertWindowControl = new AlertWindowControl(LoginActivity.this);
                                alertWindowControl.showWindow(btnLogin, "登录失败", e.getMessage());
                                return;
                            }
                            afterLogin();
                        }
                    });
        } catch (NetWorkUnAvailableException e) {
            mProgressControl.dismiss();
            e.printStackTrace();
        }
    }

    private void afterLogin() {
        //TODO 登录成功后存入本地数据
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);

    }
}

