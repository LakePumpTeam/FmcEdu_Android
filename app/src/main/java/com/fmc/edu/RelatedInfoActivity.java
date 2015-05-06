package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fmc.edu.customcontrol.SelectListControl;
import com.fmc.edu.entity.CommonEntity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class RelatedInfoActivity extends Activity {
    private Button btnSubmitAudit;
    private EditText editAddress;
    private EditText editBirthday;
    private TextView txtClass;
    private TextView txtCity;
    private EditText editComment;
    private EditText editDeviceCardNum;
    private EditText editDeviceCode;
    private TextView txtGrade;
    private EditText editParName;
    private TextView txtProvince;
    private EditText editRelation;
    private TextView txtSchool;
    private EditText editStuName;
    private TextView txtTeacher;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related_info);
        initViews();
        bindViewEvents();
        testInitData();
    }

    private void initViews() {
        btnSubmitAudit = (Button) findViewById(R.id.related_info_btn_submit_audit);

        editAddress = (EditText) findViewById(R.id.related_info_edit_address);
        editBirthday = (EditText) findViewById(R.id.related_info_edit_birthday);
        editComment = (EditText) findViewById(R.id.related_info_edit_comment);
        editDeviceCardNum = (EditText) findViewById(R.id.related_info_edit_device_card_num);
        editDeviceCode = (EditText) findViewById(R.id.related_info_edit_device_code);
        editParName = (EditText) findViewById(R.id.related_info_edit_par_name);
        editRelation = (EditText) findViewById(R.id.related_info_edit_relation);
        editStuName = (EditText) findViewById(R.id.related_info_edit_stu_name);

        txtClass = (TextView) findViewById(R.id.related_info_txt_class);
        txtCity = (TextView) findViewById(R.id.related_info_txt_city);
        txtGrade = (TextView) findViewById(R.id.related_info_txt_grade);
        txtProvince = (TextView) findViewById(R.id.related_info_txt_province);
        txtSchool = (TextView) findViewById(R.id.related_info_txt_school);
        txtTeacher = (TextView) findViewById(R.id.related_info_txt_teacher);
    }

    private void bindViewEvents() {
        btnSubmitAudit.setOnClickListener(btnSubmitAuditOnClickListener);
        txtClass.setOnClickListener(txtClassOnClickListener);
        txtCity.setOnClickListener(txtCityOnClickListener);
        txtGrade.setOnClickListener(txtGradeOnClickListener);
        txtProvince.setOnClickListener(txtProvinceOnClickListener);
        txtSchool.setOnClickListener(txtSchoolOnClickListener);
    }

    private View.OnClickListener btnSubmitAuditOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 提交审核

        }
    };
    private View.OnClickListener txtClassOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 年级列表
//            DialogFragment classListControl = SelectListControl.newInstance(selectedItemListener, null, "", v, "班级列表");
            SelectListControl classListControl = new SelectListControl(RelatedInfoActivity.this, testClassList(), "班级列表", v);
            classListControl.showAtLocation(v, Gravity.CENTER, 0, 0);

        }
    };

    private SelectListControl.OnItemSelectedListener selectedItemListener = new SelectListControl.OnItemSelectedListener() {
        @Override
        public void onItemSelected(CommonEntity obj, View view) {

        }
    };

    private View.OnClickListener txtCityOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 城市列表
        }
    };

    private View.OnClickListener txtGradeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 班级列表
        }
    };

    private View.OnClickListener txtProvinceOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 省列表
        }
    };

    private View.OnClickListener txtSchoolOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO 学校列表
        }
    };


    private void testInitData() {
        editAddress.setText("高薪区华阳");
        editBirthday.setText("2015年05月05日");

        txtClass.setText("7");
        txtClass.setTag(1);

        txtCity.setText("成都市");
        txtCity.setTag(0);
        editComment.setText("学号35325234");
        editDeviceCardNum.setText("fadfasfe4534");
        editDeviceCode.setText("fadadfa");

        txtGrade.setText("5");
        txtGrade.setTag(2);

        editParName.setText("张爸");
        txtProvince.setText("四川省");
        txtProvince.setTag(1);

        editRelation.setText("父子");

        txtSchool.setText("成都理工大学");
        txtSchool.setTag(1);

        editStuName.setText("张三");
        txtTeacher.setText("李老师");
    }

    private List<CommonEntity> testClassList() {
        List<CommonEntity> list = new ArrayList<CommonEntity>();

        for (int i = 0; i < 15; i++) {
            CommonEntity item = new CommonEntity(String.valueOf(i), "班级" + i);
            list.add(item);
        }
        return list;
    }

}
