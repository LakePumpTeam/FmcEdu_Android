package com.fmc.edu;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fmc.edu.common.Constant;
import com.fmc.edu.customcontrol.SelectListControl;
import com.fmc.edu.entity.CommonEntity;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.enums.OperateTypeEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AddRelationInfoActivity extends BaseActivity {

    private Button btnSubmitAudit;
    private EditText editAddress;
    private EditText editBraceletCardNum;
    private EditText editBraceletNumber;
    private EditText editParName;
    private EditText editStuName;
    private EditText editCellphone;
    private RadioGroup rgSex;
    private TextView txtBirthday;
    private TextView txtClass;
    private TextView txtCity;
    private TextView txtProvince;
    private TextView txtRelation;
    private TextView txtSchool;
    private TextView txtTeacher;
    private Map<String, Object> mParams;
    private SelectListControl classListControl;
    private int mPageIndex;
    private int mPageSize;
    private OperateTypeEnum mCurrentOperateType;
    private boolean mIsLastPage = true;
    private View mSelectView;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_add_relation_info);
        initViews();
        bindViewEvents();
        mPageSize = AppConfigUtils.getPageSize();
        mParams = new HashMap<>();
        mBundle = getIntent().getExtras();
        bindPageData();
    }

    private void initViews() {
        btnSubmitAudit = (Button) findViewById(R.id.add_related_info_btn_submit_audit);
        editAddress = (EditText) findViewById(R.id.add_related_info_edit_address);
        txtBirthday = (TextView) findViewById(R.id.add_related_info_txt_birthday);
        editBraceletCardNum = (EditText) findViewById(R.id.add_related_info_edit_device_card_num);
        editBraceletNumber = (EditText) findViewById(R.id.add_related_info_edit_device_code);
        editParName = (EditText) findViewById(R.id.add_related_info_edit_par_name);
        editStuName = (EditText) findViewById(R.id.add_related_info_edit_stu_name);
        editCellphone = (EditText) findViewById(R.id.add_related_info_edit_cellphone);
        rgSex = (RadioGroup) findViewById(R.id.add_related_info_rg_sex);
        txtClass = (TextView) findViewById(R.id.add_related_info_txt_class);
        txtCity = (TextView) findViewById(R.id.add_related_info_txt_city);
        txtProvince = (TextView) findViewById(R.id.add_related_info_txt_province);
        txtRelation = (TextView) findViewById(R.id.add_related_info_txt_relation);
        txtSchool = (TextView) findViewById(R.id.add_related_info_txt_school);
        txtTeacher = (TextView) findViewById(R.id.add_related_info_txt_teacher);
    }

    private void bindViewEvents() {
        btnSubmitAudit.setOnClickListener(btnSubmitAuditOnClickListener);
        txtClass.setOnClickListener(txtClassOnClickListener);
        txtCity.setOnClickListener(txtCityOnClickListener);
        txtProvince.setOnClickListener(txtProvinceOnClickListener);
        txtRelation.setOnClickListener(txtRelationOnClickListener);
        txtSchool.setOnClickListener(txtSchoolOnClickListener);
        txtBirthday.setOnClickListener(txtBirthListener);
    }

    private void bindPageData() {
        ((RadioButton) rgSex.getChildAt(0)).setChecked(true);
        editCellphone.setText(mBundle.getString("cellPhone"));
        editParName.setText(mBundle.getString("parentName"));
        editParName.setTag(mBundle.getString(""));
        editAddress.setText(mBundle.getString("address"));
        editAddress.setTag(mBundle.getString("addressId"));
    }

    private View.OnClickListener txtProvinceOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCurrentOperateType = OperateTypeEnum.Province;
            mSelectView = v;
            handleDropDownClick();
        }
    };
    private View.OnClickListener txtCityOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCurrentOperateType = OperateTypeEnum.City;
            mSelectView = v;
            handleDropDownClick();
        }
    };

    private View.OnClickListener txtSchoolOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCurrentOperateType = OperateTypeEnum.School;
            mSelectView = v;
            handleDropDownClick();
        }
    };

    private View.OnClickListener txtClassOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            mCurrentOperateType = OperateTypeEnum.Class;
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
    private View.OnClickListener txtBirthListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(AddRelationInfoActivity.this, dateDialogDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        }
    };

    private DatePickerDialog.OnDateSetListener dateDialogDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            String birthDay = String.valueOf(year) + "-" + String.valueOf(monthOfYear) + "-" + String.valueOf(dayOfMonth);
            txtBirthday.setText(birthDay);
        }
    };


    private void handleDropDownClick(String url, boolean isLoadMore) {
        if (!isLoadMore) {
            mProgressControl.showWindow();
        }
        handleParams();
        MyIon.httpPost(this, url, mParams, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                afterGetList(data);
            }
        });
    }

    private void handleParams() {
        mParams = new HashMap<>();
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

    private void handleDropDownClick() {
        mPageIndex = 1;
        classListControl = null;
        requestHttp(false);
    }

    private void requestHttp(boolean isLoadMore) {
        switch (mCurrentOperateType) {
            case Province:
                handleDropDownClick("location/requestProv", isLoadMore);
                break;
            case City:
                if (0 == ConvertUtils.getInteger(txtProvince.getTag()) || StringUtils.isEmptyOrNull(txtProvince.getText())) {
                    ToastToolUtils.showLong("请先选择省份");
                    return;
                }
                handleDropDownClick("location/requestCities", isLoadMore);
                break;
            case School:
                if (0 == ConvertUtils.getInteger(txtCity.getTag()) || StringUtils.isEmptyOrNull(txtCity.getText())) {
                    ToastToolUtils.showLong("请先选择城市");
                    return;
                }
                handleDropDownClick("school/requestSchools", isLoadMore);
                break;
            case Class:
                if (0 == ConvertUtils.getInteger(txtSchool.getTag()) || StringUtils.isEmptyOrNull(txtSchool.getText())) {
                    ToastToolUtils.showLong("请先选择学校");
                    return;
                }
                handleDropDownClick("school/requestClasses", isLoadMore);
                break;
            default:
                break;
        }
    }


    private void afterGetList(Map<String, Object> result) {
        List<CommonEntity> data = new ArrayList<>();
        String title = "";
        View view = null;
        mIsLastPage = ConvertUtils.getBoolean(result.get("isLastPage"));
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
        if (null != classListControl) {
            classListControl.setFooterViewFalse(false);
        }
        if (null == data || 0 == data.size()) {

            return;
        }
        if (null == classListControl) {
            classListControl = new SelectListControl(AddRelationInfoActivity.this, data, mIsLastPage, title, view);
            classListControl.setOnItemSelectedListener(selectedItemListener);
            classListControl.setOnLoadMoreListener(onLoadMoreListener);
            classListControl.showAtLocation(view, Gravity.CENTER, 0, 0);
        } else {
            classListControl.setLoadMoreData(data, mIsLastPage);
        }
    }


    private List<CommonEntity> getCommonEntityList(Map<String, Object> result, String key) {

        Object obj = result.get(key);
        if (StringUtils.isEmptyOrNull(obj)) {
            ToastToolUtils.showLong("无数据");
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
                return new CommonEntity(map.get("schoolId").toString(), map.get("schoolName").toString());
            case Class:
                return new CommonEntity(map.get("classId").toString(), map.get("className").toString());

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
        if (StringUtils.isEmptyOrNull(editStuName.getText())) {
            ToastToolUtils.showLong("请输入学生姓名");
            return false;
        }
        if (StringUtils.isEmptyOrNull(txtRelation.getText())) {
            ToastToolUtils.showLong("请录入与学生的关系");
            return false;
        }
        if (StringUtils.isEmptyOrNull(txtBirthday.getText())) {
            ToastToolUtils.showLong("请录入生日");
            return false;
        }
        return true;
    }


    private void doSubmitAudit(View view) {
        mProgressControl.showWindow();
        MyIon.httpPost(this, "profile/requestRegisterBaseInfo", getParams(), mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                afterInitSubmit(data);
            }
        });
    }

    private SelectListControl.OnItemSelectedListener selectedItemListener = new SelectListControl.OnItemSelectedListener() {
        @Override
        public void onItemSelected(CommonEntity obj, View view) {
            TextView textView = (TextView) view;
            if (obj.getId().equals(view.getTag())) {
                return;
            }
            textView.setText(obj.getFullName());
            textView.setTag(obj.getId());
            clearSelectInfo();
            if (mCurrentOperateType == OperateTypeEnum.Class) {
                getTeacher();
            }
        }
    };

    private void afterInitSubmit(Object resultData) {
        if (null == resultData) {
            return;
        }
        ToastToolUtils.showLong("添加成功");
        this.finish();
    }

    private void clearSelectInfo() {
        if (mCurrentOperateType == OperateTypeEnum.Province) {
            clearTextView(txtCity);
            clearTextView(txtSchool);
            clearTextView(txtClass);
            return;
        }
        if (mCurrentOperateType == OperateTypeEnum.City) {
            clearTextView(txtSchool);
            clearTextView(txtClass);
            return;
        }
        if (mCurrentOperateType == OperateTypeEnum.School) {
            clearTextView(txtClass);
        }
    }


    private void clearTextView(TextView textView) {
        textView.setText("");
        textView.setTag(0);
    }

    private Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(this);
        params.put("parentId", loginUserEntity.userId);
        params.put("cellPhone", editCellphone.getText());
        params.put("provId", String.valueOf(txtProvince.getTag()));
        params.put("cityId", String.valueOf(txtCity.getTag()));
        params.put("schoolId", String.valueOf(txtSchool.getTag()));
        params.put("classId", String.valueOf(txtClass.getTag()));
        params.put("teacherId", String.valueOf(txtTeacher.getTag()));
        params.put("studentName", editStuName.getText().toString());
        params.put("studentId", 0);
        params.put("studentSex", (findViewById(rgSex.getCheckedRadioButtonId())).getTag().toString());
        params.put("studentAge", txtBirthday.getText().toString());
        params.put("parentName", editParName.getText().toString());
        params.put("relation", txtRelation.getText().toString());
        params.put("address", editAddress.getText().toString());
        params.put("addressId", editAddress.getTag().toString());
        params.put("braceletCardNumber", editBraceletCardNum.getText().toString());
        params.put("braceletNumber", editBraceletNumber.getText().toString());
        params.put("isAudit", true);
        return params;
    }

    private SelectListControl.OnLoadMoreListener onLoadMoreListener = new SelectListControl.OnLoadMoreListener() {
        @Override
        public void onLoadMore() {
            if (mIsLastPage) {
                if (null != classListControl) {
                    classListControl.setFooterViewFalse(false);
                }
                return;
            }
            mPageIndex++;
            requestHttp(true);
        }
    };


    private View.OnClickListener txtRelationOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SelectListControl relationListControl = new SelectListControl(AddRelationInfoActivity.this, Constant.getRelationList(), true, "亲子关系", v);
            relationListControl.setOnItemSelectedListener(relationItemListener);
            relationListControl.showAtLocation(v, Gravity.CENTER, 0, 0);
        }
    };

    private SelectListControl.OnItemSelectedListener relationItemListener = new SelectListControl.OnItemSelectedListener() {
        @Override
        public void onItemSelected(CommonEntity obj, View view) {
            txtRelation.setText(obj.getFullName());
        }
    };

    private void getTeacher() {
        mProgressControl.showWindow();
        Map<String, Object> params = new HashMap<>();
        params.put("classId", txtClass.getTag());
        MyIon.httpPost(this, "school/requestHeadTeacher", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                txtTeacher.setTag(ConvertUtils.getString(data.get("headTeacherId"), ""));
                txtTeacher.setText(ConvertUtils.getString(data.get("headTeacherName"), ""));
            }
        });
    }
}
