package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.SelectListControl;
import com.fmc.edu.entity.CommonEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RelatedInfoActivity extends Activity {
    private Button btnSubmitAudit;
    private EditText editAddress;
    private EditText editBirthday;
    private EditText editComment;
    private EditText editBraceletCardNum;
    private EditText editBraceletNumber;
    private EditText editParName;
    private EditText editRelation;
    private EditText editStuName;
    private RadioGroup rgSex;
    private TextView txtClass;
    private TextView txtCity;
    private TextView txtProvince;
    private TextView txtSchool;
    private TextView txtTeacher;
    private TextView txtCellphone;
    private String mCellphone;
    private ProgressControl mProgressControl;
    private String mHostUrl;
    private Map<String, Object> mParams;
    private SelectListControl classListControl;
    private int mPageIndex;
    private int mPageSize;
    private OperateType mCurrentOperateType;
    private View mSelectView;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related_info);
        initViews();
        bindViewEvents();
        mCellphone = getIntent().getStringExtra("cellphone");
        mProgressControl = new ProgressControl(this);
        mHostUrl = AppConfigUtils.getServiceHost();
        mPageSize = AppConfigUtils.getPageSize();
        mParams = new HashMap<>();
        ((RadioButton) rgSex.getChildAt(0)).setChecked(true);
        txtCellphone.setText(mCellphone);
    }

    private void initViews() {
        btnSubmitAudit = (Button) findViewById(R.id.related_info_btn_submit_audit);
        editAddress = (EditText) findViewById(R.id.related_info_edit_address);
        editBirthday = (EditText) findViewById(R.id.related_info_edit_birthday);
        editComment = (EditText) findViewById(R.id.related_info_edit_comment);
        editBraceletCardNum = (EditText) findViewById(R.id.related_info_edit_device_card_num);
        editBraceletNumber = (EditText) findViewById(R.id.related_info_edit_device_code);
        editParName = (EditText) findViewById(R.id.related_info_edit_par_name);
        editRelation = (EditText) findViewById(R.id.related_info_edit_relation);
        editStuName = (EditText) findViewById(R.id.related_info_edit_stu_name);
        txtCellphone = (TextView) findViewById(R.id.related_info_txt_cellphone);
        rgSex = (RadioGroup) findViewById(R.id.related_info_rg_sex);
        txtClass = (TextView) findViewById(R.id.related_info_txt_class);
        txtCity = (TextView) findViewById(R.id.related_info_txt_city);
        txtProvince = (TextView) findViewById(R.id.related_info_txt_province);
        txtSchool = (TextView) findViewById(R.id.related_info_txt_school);
        txtTeacher = (TextView) findViewById(R.id.related_info_txt_teacher);
    }

    private void bindViewEvents() {
        btnSubmitAudit.setOnClickListener(btnSubmitAuditOnClickListener);
        txtClass.setOnClickListener(txtClassOnClickListener);
        txtCity.setOnClickListener(txtCityOnClickListener);
        txtProvince.setOnClickListener(txtProvinceOnClickListener);
        txtSchool.setOnClickListener(txtSchoolOnClickListener);
    }

    private View.OnClickListener txtProvinceOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCurrentOperateType = OperateType.Province;
            mSelectView = v;
            handleDropDownClick();
        }
    };
    private View.OnClickListener txtCityOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCurrentOperateType = OperateType.City;
            mSelectView = v;
            handleDropDownClick();
        }
    };

    private View.OnClickListener txtSchoolOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCurrentOperateType = OperateType.School;
            mSelectView = v;
            handleDropDownClick();
        }
    };

    private View.OnClickListener txtClassOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCurrentOperateType = OperateType.Class;
            mSelectView = v;
            handleDropDownClick();
        }
    };

    private View.OnClickListener btnSubmitAuditOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isInputFinish()) {
                return;
            }
            doSubmitAudit(v);
        }
    };

    private void handleDropDownClick() {
        mParams = new HashMap<>();
        mPageIndex = 1;
        classListControl = null;
        switch (mCurrentOperateType) {
            case Province:
                handleDropDownClick("location/requestProv");
                break;
            case City:
                if (0 == txtProvince.getTag()) {
                    ToastToolUtils.showLong("请先选择省份");
                    return;
                }
                handleDropDownClick("location/requestCities");
                break;
            case School:
                if (0 == txtCity.getTag()) {
                    ToastToolUtils.showLong("请先选择城市");
                    return;
                }
                handleDropDownClick("school/requestSchools");
                break;
            case Class:
                if (0 == txtSchool.getTag()) {
                    ToastToolUtils.showLong("请先选择学校");
                    return;
                }
                handleDropDownClick("school/requestClasses");
                break;
            default:
                break;
        }
    }

    private void handleDropDownClick(String url) {
        mProgressControl.showWindow(mSelectView);
        handleParams();
        MyIon.httpPost(this, mHostUrl + url, mParams, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Object resultData) {
                if (StringUtils.isEmptyOrNull(resultData)) {
                    ToastToolUtils.showLong("没有数据");
                    return;
                }
                Map<String, Object> data = (Map<String, Object>) resultData;
                afterGetList(data);
            }
        });
    }

    private void doSubmitAudit(View view) {
        mProgressControl.showWindow(view);
        String url = mHostUrl + "profile/requestRegisterBaseInfo";
        MyIon.httpPost(this, url, getParams(), mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Object resultData) {
                afterInitSubmit(resultData);
            }
        });
    }

    private Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cellphone", mCellphone);
        params.put("provId", String.valueOf(txtProvince.getTag()));
        params.put("cityId", String.valueOf(txtCity.getTag()));
        params.put("schoolId", String.valueOf(txtSchool.getTag()));
        params.put("classId", String.valueOf(txtClass.getTag()));
        params.put("teacherId", String.valueOf(txtTeacher.getTag()));
        params.put("studentName", editStuName.getText().toString());
        params.put("studentSex", (findViewById(rgSex.getCheckedRadioButtonId())).getTag().toString());
        params.put("studentAge", editBirthday.getText().toString());
        params.put("parentName", editParName.getText().toString());
        params.put("relation", editRelation.getText().toString());
        params.put("address", editAddress.getText().toString());
        params.put("braceletCardNumber", editBraceletCardNum.getText().toString());
        params.put("braceletNumber", editBraceletNumber.getText().toString());
        return params;
    }


    private void afterInitSubmit(Object resultData) {
        if (null == resultData) {
            return;
        }
        Intent intent = new Intent(RelatedInfoActivity.this, AuditingActivity.class);
        startActivity(intent);
        RelatedInfoActivity.this.finish();
    }

    private void clearSelectInfo() {

        if (mCurrentOperateType == OperateType.Province) {
            clearTextView(txtCity);
            clearTextView(txtSchool);
            clearTextView(txtClass);
            return;
        }
        if (mCurrentOperateType == OperateType.City) {
            clearTextView(txtSchool);
            clearTextView(txtClass);
            return;
        }
        if (mCurrentOperateType == OperateType.School) {
            clearTextView(txtClass);
        }
    }

    private void clearTextView(TextView textView) {
        textView.setText("");
        textView.setTag(0);
    }

    private void handleParams() {
        mParams.put("pageIndex", mPageIndex);
        mParams.put("pageSize", mPageSize);
        switch (mCurrentOperateType) {
            case City:
                mParams.put("provId", txtProvince.getTag());
                break;
            case School:
                mParams.put("cityId", txtCity.getTag());
                break;
            case Class:
                mParams.put("schoolId", txtSchool.getTag());
                break;
        }
    }

    private void afterGetList(Map<String, Object> result) {
        List<CommonEntity> data = new ArrayList<>();
        String title = "";
        View view = null;
        switch (mCurrentOperateType) {
            case Province:
                data = getCommonEntityList(result, "provinces");
                view = txtProvince;
                title = "省份列表";
                break;
            case City:
                data = getCommonEntityList(result, "cities");
                view = txtCity;
                title = "城市列表";
                break;
            case School:
                data = getCommonEntityList(result, "schools");
                view = txtSchool;
                title = "学校列表";
                break;
            case Class:
                data = getCommonEntityList(result, "classList");
                view = txtClass;
                title = "班级列表";
                break;
        }
        if (null == data || 0 == data.size()) {
            return;
        }
        if (null == classListControl) {
            classListControl = new SelectListControl(RelatedInfoActivity.this, data, false, title, view);
            classListControl.setOnItemClickListener(selectedItemListener);
            classListControl.setOnLoadMoreListener(onLoadMoreListener);
            classListControl.showAtLocation(view, Gravity.CENTER, 0, 0);
        } else {
            classListControl.setLoadMoreData(data);
        }
    }

    private SelectListControl.OnItemSelectedListener selectedItemListener = new SelectListControl.OnItemSelectedListener() {
        @Override
        public void onItemSelected(CommonEntity obj, View view) {
            TextView textView = (TextView) view;
            if (textView.getTag() == obj.getId()) {
                return;
            }
            textView.setText(obj.getFullName());
            textView.setTag(obj.getId());
            clearSelectInfo();
            if (mCurrentOperateType == OperateType.Class) {
                getTeacher();
            }
        }
    };

    private SelectListControl.OnLoadMoreListener onLoadMoreListener = new SelectListControl.OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            mPageIndex++;
        }
    };

    private List<CommonEntity> getCommonEntityList(Map<String, Object> result, String key) {

        Object obj = result.get(key);
        if (null == obj) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> data = (List<Map<String, Object>>) obj;
        return getCommonEntityList(data);
    }

    private List<CommonEntity> getCommonEntityList(List<Map<String, Object>> list) {

        List<CommonEntity> commonEntityList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> item = list.get(i);
            CommonEntity commonEntity = mapToCommonEntity(item);
            commonEntityList.add(commonEntity);
        }
        return commonEntityList;
    }

    private CommonEntity mapToCommonEntity(Map<String, Object> map) {
        switch (mCurrentOperateType) {
            case Province:
                return new CommonEntity(map.get("provId").toString(), map.get("name").toString());
            case City:
                return new CommonEntity(map.get("cityId").toString(), map.get("name").toString());
            case School:
                return new CommonEntity(map.get("cityId").toString(), map.get("name").toString());
            case Class:
                return new CommonEntity(map.get("cityId").toString(), map.get("name").toString());

            default:
                return new CommonEntity();
        }

    }

    private boolean isInputFinish() {
        if (StringUtils.isEmptyOrNull(txtProvince.getText())) {
            ToastToolUtils.showLong("请选择省份");
            return false;
        }
        if (StringUtils.isEmptyOrNull(txtCity.getText())) {
            ToastToolUtils.showLong("请选择城市");
            return false;
        }
        if (StringUtils.isEmptyOrNull(txtSchool.getText())) {
            ToastToolUtils.showLong("请选择学校");
            return false;
        }
        if (StringUtils.isEmptyOrNull(txtClass.getText())) {
            ToastToolUtils.showLong("请选择年级");
            return false;
        }
        if (StringUtils.isEmptyOrNull(txtTeacher.getText())) {
            ToastToolUtils.showLong("班主任不能为空");
            return false;
        }
        if (StringUtils.isEmptyOrNull(editParName.getText())) {
            ToastToolUtils.showLong("请输入家长姓名");
            return false;
        }
        if (StringUtils.isEmptyOrNull(editStuName.getText())) {
            ToastToolUtils.showLong("请输入学生姓名");
            return false;
        }
        if (StringUtils.isEmptyOrNull(editRelation.getText())) {
            ToastToolUtils.showLong("请录入与学生的关系");
            return false;
        }
        if (StringUtils.isEmptyOrNull(txtProvince.getText())) {
            ToastToolUtils.showLong("请选择省份");
            return false;
        }
        return true;
    }

    private void getTeacher() {
        mProgressControl.showWindow(txtTeacher);
        Map<String, Object> params = new HashMap<>();
        params.put("classId", txtClass.getTag());
        MyIon.httpPost(this, mHostUrl + "school/requestHeadTeacher", mParams, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Object resultData) {
                if (StringUtils.isEmptyOrNull(resultData)) {
                    ToastToolUtils.showLong("没有数据");
                    return;
                }
                Map<String, Object> data = (Map<String, Object>) resultData;
                txtTeacher.setTag(data.get("headTeacherId").toString());
                txtTeacher.setText(data.get("headTeacherName").toString());
            }
        });
    }

    enum OperateType {
        Province,
        City,
        School,
        Class;
    }
}
