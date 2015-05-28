package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.PromptWindowControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.enums.AuditStateTypeEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.fmc.edu.utils.ValidationUtils;

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
    private PromptWindowControl promptWindowControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this);
        setContentView(R.layout.activity_login);
        mProgressControl = new ProgressControl(this);
        mHostUrl = AppConfigUtils.getServiceHost();
        initViews();
        bindViewEvents();
        autoLogin();
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

    private void autoLogin() {

        LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(this);
        if (null == loginUserEntity) {
            return;
        }
        if (StringUtils.isEmptyOrNull(loginUserEntity.cellphone)) {
            return;
        }

        if (StringUtils.isEmptyOrNull(loginUserEntity.password)) {
            editCellphone.setText(loginUserEntity.cellphone);
            return;
        }
        loginRequestHttp(loginUserEntity.cellphone, loginUserEntity.password, loginUserEntity.salt);
    }

    private View.OnClickListener btnLoginOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String cellphone = ConvertUtils.getString(editCellphone.getText());
            String password = ConvertUtils.getString(editPassword.getText());
            if (StringUtils.isEmptyOrNull(cellphone)) {
                ToastToolUtils.showLong("请输入账号");
                return;
            }
            if (StringUtils.isEmptyOrNull(password)) {
                ToastToolUtils.showLong("请输入密码");
                return;
            }
            doLogin(v, cellphone, password);
        }
    };

    private View.OnClickListener txtForgetPasswordOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            promptWindowControl = new PromptWindowControl(LoginActivity.this);
            promptWindowControl.setOnOperateOnClickListener(operateOnClickListener);
            promptWindowControl.showWindow(v, "忘记密码?", "你可以通过注册手机重置密码", "重置密码");
        }
    };

    private OnOperateOnClickListener operateOnClickListener = new OnOperateOnClickListener() {
        @Override
        public void onOperateOnClick() {
            if (null != promptWindowControl) {
                promptWindowControl.dismiss();
            }
            Intent intent = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
            intent.putExtra("cellPhone", editCellphone.getText());
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

    private void doLogin(View view, String cellphone, String password) {
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
        getLoginSalt(cellphone, password);
    }

    private void getLoginSalt(final String cellPhone, final String password) {
        String url = mHostUrl + "profile/requestSalt";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cellPhone", cellPhone);
        MyIon.httpPost(this, url, params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                String salt = ConvertUtils.getString(data.get("salt"), "");
                loginRequestHttp(cellPhone, password, salt);
            }
        });
    }

    private void loginRequestHttp(final String cellphone, final String password, final String salt) {
        String url = mHostUrl + "profile/requestLogin";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userAccount", cellphone);
        params.put("password", StringUtils.MD5(salt, password));
        MyIon.httpPost(this, url, params, null, new MyIon.AfterCallBack() {
                    @Override
                    public void afterCallBack(Map<String, Object> data) {
                        saveLocalLoginInfo(ConvertUtils.getInteger(data.get("userId")), cellphone, password, salt, ConvertUtils.getString(data.get("userName")));
                        afterLogin(data);
                    }
                }
        );
    }

    private void afterLogin(Map<String, Object> data) {
        int auditState = ConvertUtils.getInteger(data.get("auditState"), 1);
        if (auditState == AuditStateTypeEnum.getValue(AuditStateTypeEnum.Auditing)) {
            this.finish();
            Intent intent = new Intent(LoginActivity.this, AuditingActivity.class);
            startActivity(intent);
            return;
        }
        if (auditState == AuditStateTypeEnum.getValue(AuditStateTypeEnum.Pass)) {
            gotoMainData();
            return;
        }
        ToastToolUtils.showLong("信息审核不通过");
        gotoRelationPage();
    }

    private void gotoMainData() {
        mProgressControl.showWindow(btnLogin);
        String url = AppConfigUtils.getServiceHost() + "home/requestHeaderTeacherForHomePage";
        LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(this);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", loginUserEntity.userId);
        MyIon.httpPost(this, url, params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                LoginActivity.this.finish();
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putBoolean("sex", ConvertUtils.getBoolean(data.get("sex"), false));
                bundle.putString("teacherName", ConvertUtils.getString(data.get("teacherName"), ""));
                bundle.putString("teacherId", ConvertUtils.getString(data.get("teacherId"), ""));
                bundle.putString("className", ConvertUtils.getString(data.get("className"), ""));
                bundle.putInt("userRole", ConvertUtils.getInteger(data.get("userRole"), 1));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void gotoRelationPage() {
        mProgressControl.showWindow(btnLogin);
        LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(this);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("parentId", loginUserEntity.userId);
        MyIon.httpPost(this, mHostUrl + "profile/requestGetRelateInfo", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                LoginActivity.this.finish();
                Intent intent = new Intent(LoginActivity.this, RelatedInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("address", ConvertUtils.getString(data.get("address"), ""));
                bundle.putString("studentBirth", ConvertUtils.getString(data.get("studentBirth"), ""));
                bundle.putString("braceletCardNumber", ConvertUtils.getString(data.get("braceletCardNumber"), ""));
                bundle.putString("braceletNumber", ConvertUtils.getString(data.get("braceletNumber"), ""));
                bundle.putString("parentName", ConvertUtils.getString(data.get("parentName"), ""));
                bundle.putString("relation", ConvertUtils.getString(data.get("relation"), ""));
                bundle.putString("studentName", ConvertUtils.getString(data.get("studentName"), ""));
                bundle.putString("cellPhone", ConvertUtils.getString(data.get("cellPhone"), ""));
                bundle.putString("classId", ConvertUtils.getString(data.get("classId"), ""));
                bundle.putString("className", ConvertUtils.getString(data.get("className"), ""));
                bundle.putString("cityId", ConvertUtils.getString(data.get("cityId"), ""));
                bundle.putString("cityName", ConvertUtils.getString(data.get("cityName"), ""));
                bundle.putString("provId", ConvertUtils.getString(data.get("provId"), ""));
                bundle.putString("provName", ConvertUtils.getString(data.get("provName"), ""));
                bundle.putString("schoolId", ConvertUtils.getString(data.get("schoolId"), ""));
                bundle.putString("schoolName", ConvertUtils.getString(data.get("schoolName"), ""));
                bundle.putString("teacherId", ConvertUtils.getString(data.get("teacherId"), ""));
                bundle.putString("teacherName", ConvertUtils.getString(data.get("teacherName"), ""));
                bundle.putString("studentId", ConvertUtils.getString(data.get("studentId"), "0"));
                bundle.putString("addressId", ConvertUtils.getString(data.get("addressId"), "0"));
                bundle.putBoolean("studentSex", ConvertUtils.getBoolean(data.get("studentSex"), false));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void saveLocalLoginInfo(int userId, String cellPhone, String password, String salt, String userName) {
        LoginUserEntity userEntity = new LoginUserEntity();
        userEntity.userId = userId;
        userEntity.cellphone = cellPhone;
        userEntity.password = password;
        userEntity.salt = salt;
        userEntity.userName = userName;
        ServicePreferenceUtils.saveLoginUserPreference(this, userEntity);
    }
}

