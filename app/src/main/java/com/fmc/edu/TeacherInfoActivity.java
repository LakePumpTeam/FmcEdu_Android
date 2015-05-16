package com.fmc.edu;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class TeacherInfoActivity extends Activity {
    private Button btnSubmit;
    private EditText editCourse;
    private EditText editName;
    private EditText editRecord;
    private EditText editCellphone;
    private RadioGroup rgSex;
    private TextView txtBirth;
    private ProgressControl mProgressControl;
    private String mHostUrl;
    private boolean mIsModify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_info);
        mProgressControl = new ProgressControl(this);
        mHostUrl = AppConfigUtils.getServiceHost();
        mIsModify = getIntent().getBooleanExtra("isModify", false);
        initViews();
        initViewEvent();
        initPageData();
    }

    private void initViews() {
        btnSubmit = (Button) findViewById(R.id.teacher_info_btn_submit);
        editCourse = (EditText) findViewById(R.id.teacher_info_edit_course);
        editCellphone = (EditText) findViewById(R.id.teacher_info_edit_cellphone);
        editName = (EditText) findViewById(R.id.teacher_info_edit_name);
        editRecord = (EditText) findViewById(R.id.teacher_info_edit_record);
        rgSex = (RadioGroup) findViewById(R.id.teacher_info_rg_sex);
        txtBirth = (TextView) findViewById(R.id.teacher_info_txt_birth);
    }

    private void initViewEvent() {
        btnSubmit.setOnClickListener(btnSubmitListener);
        txtBirth.setOnClickListener(txtBirthListener);
    }

    private void initPageData() {
        String teacherId = getIntent().getStringExtra("teacherId");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("teacherId", teacherId);
        //TODO 配置路径
        MyIon.httpPost(this, mHostUrl + "", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                bindViewData(data);
            }
        });
    }

    private void bindViewData(Map<String, Object> data) {
        //TODO 绑定初始值
    }

    private void bindEnable() {
        if (!mIsModify) {
            return;
        }
        editCourse.setEnabled(true);
        editCellphone.setEnabled(true);
        editName.setEnabled(true);
        editRecord.setEnabled(true);
        rgSex.setEnabled(true);
        btnSubmit.setVisibility(View.GONE);

    }


    private View.OnClickListener btnSubmitListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//TODO 修改教师信息接口
        }
    };

    private View.OnClickListener txtBirthListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(TeacherInfoActivity.this, dateDialogDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
    };

    private DatePickerDialog.OnDateSetListener dateDialogDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String birthDay = String.valueOf(year) + "-" + String.valueOf(monthOfYear) + "-" + String.valueOf(dayOfMonth);
            txtBirth.setText(birthDay);
        }
    };
}
