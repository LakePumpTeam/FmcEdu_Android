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

}

