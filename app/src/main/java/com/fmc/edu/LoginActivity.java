package com.fmc.edu;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fmc.edu.customcontrol.PromptWindowControl;
import com.fmc.edu.customcontrol.SelectListControl;
import com.fmc.edu.entity.CommonEntity;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.enums.AuditStateTypeEnum;
import com.fmc.edu.enums.UserRoleEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.fmc.edu.utils.ValidationUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fmc.edu.customcontrol.PromptWindowControl.OnOperateOnClickListener;


public class LoginActivity extends BaseActivity {
    private Button btnLogin;
    private EditText editCellphone;
    private EditText editPassword;
    private TextView txtForgetPassword;
    private TextView txtRegister;

    private int REQUEST_CODE_REGISTER = 1;
    private PromptWindowControl promptWindowControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_login);
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
        mProgressControl.showWindow();
        getLoginSalt(cellphone, password);
    }

    private void getLoginSalt(final String cellPhone, final String password) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cellPhone", cellPhone);
        MyIon.httpPost(this, "profile/requestSalt", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                String salt = ConvertUtils.getString(data.get("salt"), "");
                loginRequestHttp(cellPhone, password, salt);
            }
        });
    }

    private void loginRequestHttp(final String cellphone, final String password, final String salt) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userAccount", cellphone);
        params.put("password", StringUtils.MD5(salt, password));
        MyIon.httpPost(this, "profile/requestLogin", params, null, new MyIon.AfterCallBack() {
                    @Override
                    public void afterCallBack(Map<String, Object> data) {
                        saveLocalLoginInfo(cellphone, password, salt, data);
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
            gotoMainData(data);
            return;
        }
        ToastToolUtils.showLong("信息审核不通过");
        gotoRelationPage();
    }

    private void gotoMainData(Map<String, Object> data) {
        List<Map<String, Object>> list = ConvertUtils.getList(data.get("optionList"));
        UserRoleEnum userRole = UserRoleEnum.getEnumValue(ConvertUtils.getInteger(data.get("userRole")));
        if (null == list || list.size() == 0) {
            if (userRole == UserRoleEnum.Parent) {
                ToastToolUtils.showLong("未绑定学生，不能进入");
                return;
            }
            if (userRole == UserRoleEnum.Teacher) {
                ToastToolUtils.showLong("未绑定班级，不能进入");
                return;
            }
            if (list.size() == 1) {
                Map<String, Object> classInfo = list.get(0);
                int classId = userRole == UserRoleEnum.Parent ? ConvertUtils.getInteger(classInfo.get("classId"), 0) : ConvertUtils.getInteger(classInfo.get("optionId"), 0);
                int studentId = userRole == UserRoleEnum.Parent ? ConvertUtils.getInteger(classInfo.get("optionId"), 0) : 0;
                gotoMainActivity(classId, studentId);
                return;
            }
            String title = userRole == UserRoleEnum.Teacher ? "选择班级" : "选择学生";
            btnLogin.setTag(data);
            SelectListControl classListControl = new SelectListControl(LoginActivity.this, getCommonEntityList(list), true, title, btnLogin);
            classListControl.setOnItemSelectedListener(selectedItemListener);
            classListControl.showAtLocation(btnLogin, Gravity.CENTER, 0, 0);

        }
    }

    private SelectListControl.OnItemSelectedListener selectedItemListener = new SelectListControl.OnItemSelectedListener() {
        @Override
        public void onItemSelected(CommonEntity obj, View view) {
            Map<String, Object> data = (Map<String, Object>) view.getTag();
            List<Map<String, Object>> list = ConvertUtils.getList(data.get("optionList"));
            UserRoleEnum userRole = UserRoleEnum.getEnumValue(ConvertUtils.getInteger(data.get("userRole")));
            int selectId = ConvertUtils.getInteger(obj.getId());
            if (userRole == UserRoleEnum.Teacher) {
                gotoMainActivity(selectId, 0);
                return;
            }
            int classId = getClassId(list, selectId);
            gotoMainActivity(classId, selectId);
        }
    };


    private List<CommonEntity> getCommonEntityList(List<Map<String, Object>> list) {

        List<CommonEntity> commonEntityList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> item = list.get(i);
            CommonEntity commonEntity = new CommonEntity(item.get("optionId").toString(), item.get("optionName").toString());
            commonEntityList.add(commonEntity);
        }
        return commonEntityList;
    }

    private void gotoMainActivity(int classId, int studentId) {
        ServicePreferenceUtils.saveClassIdPreference(LoginActivity.this, classId);
        ServicePreferenceUtils.saveStudentIdPreference(LoginActivity.this, studentId);
        mProgressControl.showWindow();
        LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(this);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", loginUserEntity.userId);
        MyIon.httpPost(this, "home/requestHeaderTeacherForHomePage", params, mProgressControl, new MyIon.AfterCallBack() {
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

    private int getClassId(List<Map<String, Object>> list, int optionId) {
        for (Map<String, Object> item : list) {
            if (ConvertUtils.getInteger(item.get("optionId"), 0) == optionId) {
                return ConvertUtils.getInteger(item.get("classId"), 0);
            }
        }
        return 0;
    }

    private void gotoRelationPage() {
        mProgressControl.showWindow();
        LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(this);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("parentId", loginUserEntity.userId);
        MyIon.httpPost(this, "profile/requestGetRelateInfo", params, mProgressControl, new MyIon.AfterCallBack() {
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

    private void saveLocalLoginInfo(String cellPhone, String password, String salt, Map<String, Object> userData) {
        LoginUserEntity userEntity = new LoginUserEntity();
        userEntity.userId = ConvertUtils.getInteger(userData.get("userId"));
        userEntity.cellphone = cellPhone;
        userEntity.password = password;
        userEntity.salt = salt;
        userEntity.userRole = UserRoleEnum.getEnumValue(ConvertUtils.getInteger(userData.get("userRole")));
        userEntity.userName = ConvertUtils.getString(userData.get("userName"), "");
        userEntity.userCardNum = ConvertUtils.getString(userData.get("braceletCardNumber"), "0");
        ServicePreferenceUtils.saveLoginUserPreference(this, userEntity);
    }
}

