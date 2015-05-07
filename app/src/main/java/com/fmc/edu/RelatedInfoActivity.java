package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fmc.edu.customcontrol.AlertWindowControl;
import com.fmc.edu.customcontrol.SelectListControl;
import com.fmc.edu.entity.CommonEntity;
import com.fmc.edu.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class RelatedInfoActivity extends Activity {
    private Button btnSubmitAudit;
    private EditText editAddress;
    private EditText editBirthday;
    private EditText editComment;
    private EditText editDeviceCardNum;
    private EditText editDeviceCode;
    private EditText editParName;
    private EditText editRelation;
    private EditText editStuName;
    private RadioGroup rgSex;
    private TextView txtClass;
    private TextView txtCity;
    private TextView txtGrade;
    private TextView txtProvince;
    private TextView txtSchool;
    private TextView txtTeacher;

    private String mCellphone;
    private String mPassword;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related_info);
        initViews();
        bindViewEvents();
        mCellphone = getIntent().getStringExtra("cellphone");
        mPassword = getIntent().getStringExtra("password");
        initData();
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
        rgSex = (RadioGroup) findViewById(R.id.related_info_rg_sex);
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
            if (!isInputFinish(v)) {
                return;
            }
            doSubmitAudit();

        }
    };
    private View.OnClickListener txtClassOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handleDropDownClick(v, OperateType.Class);
        }
    };

    private View.OnClickListener txtCityOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handleDropDownClick(v, OperateType.City);
        }
    };

    private View.OnClickListener txtGradeOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handleDropDownClick(v, OperateType.Grade);
        }
    };

    private View.OnClickListener txtProvinceOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handleDropDownClick(v, OperateType.Province);
        }
    };

    private View.OnClickListener txtSchoolOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            handleDropDownClick(v, OperateType.School);
        }
    };

    private void handleDropDownClick(View v, OperateType operateType) {

        //TODO 处理数据
        switch (operateType) {
            case Province:
                handleClick(v, testClassList(), "省列表");
                break;
            case City:
                handleClick(v, testClassList(), "城市列表");
                break;
            case School:
                handleClick(v, testClassList(), "学校列表");
                break;
            case Class:
                handleClick(v, testClassList(), "年级列表");
                break;
            case Grade:
                handleClick(v, testClassList(), "班级列表");
                break;
            default:
                break;
        }
    }

    private void handleClick(View v, List<CommonEntity> list, String title) {
        SelectListControl classListControl = new SelectListControl(RelatedInfoActivity.this, list, title, v);
        classListControl.setOnItemClickListener(selectedItemListener);
        classListControl.showAtLocation(v, Gravity.CENTER, 0, 0);
    }

    private SelectListControl.OnItemSelectedListener selectedItemListener = new SelectListControl.OnItemSelectedListener() {
        @Override
        public void onItemSelected(CommonEntity obj, View view) {
            TextView textView = (TextView) view;
            textView.setText(obj.getFullName());
            textView.setTag(obj.getId());
        }
    };

    enum OperateType {
        Province,
        City,
        School,
        Class,
        Grade;
    }


    private boolean isInputFinish(View view) {
        AlertWindowControl alertWindowControl = new AlertWindowControl(this);
        if (StringUtils.isEmptyOrNull(txtProvince.getText())) {
            alertWindowControl.showWindow(view, "提交失败", "请选择省份");
            return false;
        }
        if (StringUtils.isEmptyOrNull(txtCity.getText())) {
            alertWindowControl.showWindow(view, "提交失败", "请选择城市");
            return false;
        }
        if (StringUtils.isEmptyOrNull(txtSchool.getText())) {
            alertWindowControl.showWindow(view, "提交失败", "请选择学校");
            return false;
        }
        if (StringUtils.isEmptyOrNull(txtClass.getText())) {
            alertWindowControl.showWindow(view, "提交失败", "请选择年级");
            return false;
        }
        if (StringUtils.isEmptyOrNull(txtTeacher.getText())) {
            alertWindowControl.showWindow(view, "提交失败", "班主任不能为空");
            return false;
        }
        if (StringUtils.isEmptyOrNull(editParName.getText())) {
            alertWindowControl.showWindow(view, "提交失败", "请输入家长姓名");
            return false;
        }
        if (StringUtils.isEmptyOrNull(editStuName.getText())) {
            alertWindowControl.showWindow(view, "提交失败", "请输入学生姓名");
            return false;
        }
        if (StringUtils.isEmptyOrNull(editRelation.getText())) {
            alertWindowControl.showWindow(view, "提交失败", "请录入与学生的关系");
            return false;
        }
        if (StringUtils.isEmptyOrNull(txtProvince.getText())) {
            alertWindowControl.showWindow(view, "提交失败", "请选择省份");
            return false;
        }
        if (StringUtils.isEmptyOrNull(txtProvince.getText())) {
            alertWindowControl.showWindow(view, "提交失败", "请选择省份");
            return false;
        }
        return true;
    }

    private void doSubmitAudit() {
        //TODO 调用提交注册审核的接口
    }

    private void initData() {
        ((RadioButton) rgSex.getChildAt(0)).setChecked(true);
        //TODO 调用接口，对部分数据进行初始化
//        editAddress.setText("高薪区华阳");
//        editBirthday.setText("2015年05月05日");
//
//        txtClass.setText("7");
//        txtClass.setTag(1);
//
//        txtCity.setText("成都市");
//        txtCity.setTag(0);
//        editComment.setText("学号35325234");
//        editDeviceCardNum.setText("fadfasfe4534");
//        editDeviceCode.setText("fadadfa");
//
//        txtGrade.setText("5");
//        txtGrade.setTag(2);
//
//        editParName.setText("张爸");
//        txtProvince.setText("四川省");
//        txtProvince.setTag(1);
//
//        editRelation.setText("父子");
//
//        txtSchool.setText("成都理工大学");
//        txtSchool.setTag(1);
//
//        editStuName.setText("张三");
//        txtTeacher.setText("李老师");
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
