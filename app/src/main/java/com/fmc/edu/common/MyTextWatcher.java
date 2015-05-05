package com.fmc.edu.common;

import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by Candy on 2015-05-05.
 */
public class MyTextWatcher implements TextWatcher {

//    EditText editText;
//    WatcherTypeEnum checkCode;

    public OnTextChangedListener mOnTextChangedListener;

    public interface OnTextChangedListener {
        void onTextChanged();
    }

    public MyTextWatcher() {
//        this.editText = editText;
//        this.checkCode = checkCode;
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (null == this.mOnTextChangedListener) {
            return;
        }
        this.mOnTextChangedListener.onTextChanged();
    }

    public void setOnTextChangedListener(OnTextChangedListener mOnTextChangedListener) {
        this.mOnTextChangedListener = mOnTextChangedListener;
    }
//
//    private void isShowNextStep() {
//        switch (checkCode) {
//            case ValidateCell:
//                SetValidatePhone();
////                case 1:
////                    SetInputInfo();
//        }
//    }
//
//    private void SetValidatePhone() {
//        String text = editText.getText().toString();
//        if (editText.getId() == R.id.register_edit_cellphone) {
//            boolean isEffectPhone = !StringUtils.isEmptyOrNull(text) && ValidationUtils.isMobilePhone(text);
//            validateBtnGetAuthCode.setEnabled(isEffectPhone);
//        }
//
//        boolean isValidatePhone = !StringUtils.isEmptyOrNull(editCellphone.getText()) && ValidationUtils.isMobilePhone(editCellphone.getText().toString());
//        boolean isValidateCode = !StringUtils.isEmptyOrNull(editAuthCode.getText());
//        boolean isValidatePassword = editPassword.length() >= 6 && editPassword.length() <= 16;
//        if (isValidatePhone && isValidateCode && ckReadAgreement.isChecked() && isValidatePassword) {
//            btnNextStep.setEnabled(true);
//            return;
//        }
//        btnNextStep.setEnabled(false);
//    }

//        private void SetInputInfo() {
//            String fullname = et_register_fullname.getText().toString();
//            String drivingSchool = et_register_driving_school.getText().toString();
//            String carNum = et_register_car_num.getText().toString();
//            String cardNum = et_register_card_num.getText().toString();
//            String password = et_register_password.getText().toString();
//
//            if (StringUtils.isNotBlank(fullname) && StringUtils.isNotBlank(drivingSchool)
//                    && StringUtils.isNotBlank(carNum) && StringUtils.isNotBlank(cardNum) && StringUtils.isNotBlank(password)) {
//                btn_to_validate_identity.setEnabled(true);
//                return;
//            }
//            btn_to_validate_identity.setEnabled(false);
//        }
}

