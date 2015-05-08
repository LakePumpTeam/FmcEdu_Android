package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import com.fmc.edu.common.MyTextWatcher;
import com.fmc.edu.customcontrol.AlertWindowControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.ValidateButtonControl;
import com.fmc.edu.http.FMCMapFutureCallback;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.http.NetWorkUnAvailableException;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ValidationUtils;

import java.nio.charset.Charset;
import java.util.Map;


public class RegisterActivity extends Activity {

    private Button btnNextStep;
    private CheckBox ckReadAgreement;
    private EditText editCellphone;
    private EditText editAuthCode;
    private EditText editPassword;
    private EditText editConfirmPassword;
    private ValidateButtonControl validateBtnGetAuthCode;

    private ProgressControl progressControl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        initViewEvents();
        progressControl = new ProgressControl(this);

        if (AppConfigUtils.IsDevelopment()) {
            initTestData();
        }
    }

    private void initViews() {
        btnNextStep = (Button) findViewById(R.id.register_btn_next_step);
        ckReadAgreement = (CheckBox) findViewById(R.id.register_ck_read_agreement);
        editCellphone = (EditText) findViewById(R.id.register_edit_cellphone);
        editAuthCode = (EditText) findViewById(R.id.register_edit_auth_code);
        editPassword = (EditText) findViewById(R.id.register_edit_password);
        editConfirmPassword = (EditText) findViewById(R.id.register_edit_confirm_password);
        validateBtnGetAuthCode = (ValidateButtonControl) findViewById(R.id.register_validate_btn_get_auth_code);
    }

    private void initViewEvents() {
        btnNextStep.setOnClickListener(btnNextStepOnClickListener);
        validateBtnGetAuthCode.setOnClickListener(btnGetAuthCodeOnClickListener);

        MyTextWatcher validatePhoneTextWatcher = new MyTextWatcher();
        editCellphone.addTextChangedListener(validatePhoneTextWatcher);
        validatePhoneTextWatcher.setOnTextChangedListener(validateCellphoneTextChangeListener);

        MyTextWatcher validateInputTextWatcher = new MyTextWatcher();
        editAuthCode.addTextChangedListener(validateInputTextWatcher);
        validateInputTextWatcher.setOnTextChangedListener(validateInputTextChangeListener);

        editPassword.addTextChangedListener(validateInputTextWatcher);
        validateInputTextWatcher.setOnTextChangedListener(validateInputTextChangeListener);

        ckReadAgreement.setOnClickListener(ckReadAgreementOnClickListener);
    }

    private View.OnClickListener btnNextStepOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doNextStep(v);
        }

    };

    private View.OnClickListener btnGetAuthCodeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getAuthCode(v);
        }
    };

    private View.OnClickListener ckReadAgreementOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            CheckValidateInput();
        }
    };

    private MyTextWatcher.OnTextChangedListener validateCellphoneTextChangeListener = new MyTextWatcher.OnTextChangedListener() {
        @Override
        public void onTextChanged() {
            String cellphoneNum = editCellphone.getText().toString();
            boolean isEffectPhone = !StringUtils.isEmptyOrNull(cellphoneNum) && ValidationUtils.isMobilePhone(cellphoneNum);
            validateBtnGetAuthCode.setEnabled(isEffectPhone);
            CheckValidateInput();
        }
    };


    private MyTextWatcher.OnTextChangedListener validateInputTextChangeListener = new MyTextWatcher.OnTextChangedListener() {
        @Override
        public void onTextChanged() {
            CheckValidateInput();
        }
    };


    private void getAuthCode(View view) {
        try {
            progressControl.showWindow(view);
            String url = AppConfigUtils.getServiceHost() + "profile/requestPhoneIdentify";
            MyIon.with(this)
                    .load(url)
                    .setBodyParameter("cellphone", editCellphone.getText().toString())
                    .asString(Charset.forName("utf8"))
                    .setCallback(new FMCMapFutureCallback() {
                        @Override
                        public void onTranslateCompleted(Exception e, Map<String, Object> result) {
                            progressControl.dismiss();
                        }
                    });
        } catch (NetWorkUnAvailableException e) {
            progressControl.dismiss();
            e.printStackTrace();
        }
    }

    private void doNextStep(View view) {
//        try {
//            //TODO 路径没有配好
//            progressControl.showWindow(view);
//            MyIon.with(this)
//                    .load(AppConfigUtils.getServiceHost() + "url")
//                    .setBodyParameter("authcode", editCellphone.getText().toString())
//                    .as(new MapTokenTypeUtils())
//                    .setCallback(new FutureCallback<Map<String, Object>>() {
//                        @Override
//                        public void onCompleted(Exception e, Map<String, Object> result) {
//                            progressControl.dismiss();
//                            if (!HttpTools.isRequestSuccessfully(e, result)) {
//                                return;
//                            }
        afterNextStep();
//                        }
//                    });
//        } catch (NetWorkUnAvailableException e) {
//            progressControl.dismiss();
//            e.printStackTrace();
//        }

    }

    private void afterNextStep() {
        String password = editPassword.getText().toString();
        String confirmPassword = editConfirmPassword.getText().toString();
        if (!password.equals(confirmPassword)) {
            AlertWindowControl alertWindowControl = new AlertWindowControl(RegisterActivity.this);
            alertWindowControl.showWindow(btnNextStep, "注册失败", "两次密码输入不一致");
            return;
        }
        Intent intent = new Intent(RegisterActivity.this, RelatedInfoActivity.class);
        intent.putExtra("cellphone", editCellphone.getText().toString());
        intent.putExtra("password", editPassword.getText().toString());
        startActivity(intent);
    }

    private void CheckValidateInput() {
        boolean isValidatePhone = !StringUtils.isEmptyOrNull(editCellphone.getText().toString()) && ValidationUtils.isMobilePhone(editCellphone.getText().toString());
        boolean isValidateCode = !StringUtils.isEmptyOrNull(editAuthCode.getText());
        boolean isValidatePassword = editPassword.length() >= 6 && editPassword.length() <= 16;
        if (isValidatePhone && isValidateCode && ckReadAgreement.isChecked() && isValidatePassword) {
            btnNextStep.setEnabled(true);
            return;
        }
        btnNextStep.setEnabled(false);
    }

    private void initTestData() {
        ckReadAgreement.setChecked(true);
        editCellphone.setText("13880454117");
        editAuthCode.setText("123456");
        editPassword.setText("123456");
        editConfirmPassword.setText("123456");
    }

}
