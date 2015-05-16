package com.fmc.edu;

import android.app.Activity;
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

import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.SelectListControl;
import com.fmc.edu.entity.CommonEntity;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RelatedInfoActivity extends Activity {
    private Button btnSubmitAudit;
    private EditText editAddress;
    private EditText editBraceletCardNum;
    private EditText editBraceletNumber;
    private EditText editParName;
    private EditText editRelation;
    private EditText editStuName;
    private RadioGroup rgSex;
    private TextView txtBirthday;
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
    private boolean mIsModify;
    private OperateType mCurrentOperateType;
    private boolean mIsLastPage = true;
    private View mSelectView;
    private Map<String, Object> mOldData;
    private boolean mIsAudit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_related_info);
        initViews();
        bindViewEvents();
        mCellphone = getIntent().getStringExtra("cellphone");
        mIsModify = getIntent().getBooleanExtra("isModify", false);
        mProgressControl = new ProgressControl(this);
        mHostUrl = AppConfigUtils.getServiceHost();
        mPageSize = AppConfigUtils.getPageSize();
        mParams = new HashMap<>();
        initPageData();
    }

    private void initViews() {
        btnSubmitAudit = (Button) findViewById(R.id.related_info_btn_submit_audit);
        editAddress = (EditText) findViewById(R.id.related_info_edit_address);
        txtBirthday = (TextView) findViewById(R.id.related_info_txt_birthday);
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
        txtBirthday.setOnClickListener(txtBirthListener);
    }

    private void initPageData() {
        if (!mIsModify) {
            ((RadioButton) rgSex.getChildAt(0)).setChecked(true);
            txtCellphone.setText(mCellphone);
            return;
        }
        LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(this);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("parentId", loginUserEntity.userId);
        MyIon.httpPost(this, mHostUrl + "profile/requestGetRelateInfo", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                mOldData = data;
                bindPageData();
            }
        });
    }

    private void bindPageData() {
        editAddress.setText(ConvertUtils.getString(mOldData.get("address")));
        txtBirthday.setText(ConvertUtils.getString(mOldData.get("studentBirth")));
//        editComment.setText(ConvertUtils.getString(mOldData.get("cellPhone")));
        editBraceletCardNum.setText(ConvertUtils.getString(mOldData.get("braceletCardNumber")));
        editBraceletNumber.setText(ConvertUtils.getString(mOldData.get("braceletNumber")));
        editParName.setText(ConvertUtils.getString(mOldData.get("parentName")));
        editRelation.setText(ConvertUtils.getString(mOldData.get("relation")));
        editStuName.setText(ConvertUtils.getString(mOldData.get("studentName")));
        txtCellphone.setText(ConvertUtils.getString(mOldData.get("cellPhone")));
        txtClass.setTag(mOldData.get("classId"));
        txtClass.setText(ConvertUtils.getString(mOldData.get("className")));
        txtCity.setTag(mOldData.get("cityId"));
        txtCity.setText(ConvertUtils.getString(mOldData.get("cityName")));
        txtProvince.setTag(mOldData.get("provId"));
        txtProvince.setText(ConvertUtils.getString(mOldData.get("provName")));
        txtSchool.setTag(mOldData.get("schoolId"));
        txtSchool.setText(ConvertUtils.getString(mOldData.get("schoolName")));
        txtTeacher.setTag(mOldData.get("teacherId"));
        txtTeacher.setText(ConvertUtils.getString(mOldData.get("teacherName")));
        if (ConvertUtils.getBoolean(mOldData.get("studentSex"))) {
            ((RadioButton) rgSex.getChildAt(1)).setChecked(true);
        } else {
            ((RadioButton) rgSex.getChildAt(0)).setChecked(true);
        }
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
    private View.OnClickListener txtBirthListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePickerDialog = new DatePickerDialog(RelatedInfoActivity.this, dateDialogDateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
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

    private void handleDropDownClick() {
        mPageIndex = 1;
        classListControl = null;
        mProgressControl.showWindow(mSelectView);
        requestHttp();
    }

    private void requestHttp() {
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
        handleParams();
        MyIon.httpPost(this, mHostUrl + url, mParams, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                afterGetList(data);
            }
        });
    }

    private void doSubmitAudit(View view) {
        mProgressControl.showWindow(view);
        String url = mHostUrl + "profile/requestRegisterBaseInfo";
        MyIon.httpPost(this, url, getParams(), mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                afterInitSubmit(data);
            }
        });
    }

    private Map<String, Object> getParams() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("cellPhone", mCellphone);
        params.put("provId", String.valueOf(txtProvince.getTag()));
        params.put("cityId", String.valueOf(txtCity.getTag()));
        params.put("schoolId", String.valueOf(txtSchool.getTag()));
        params.put("classId", String.valueOf(txtClass.getTag()));
        params.put("teacherId", String.valueOf(txtTeacher.getTag()));
        params.put("studentName", editStuName.getText().toString());
        params.put("studentSex", (findViewById(rgSex.getCheckedRadioButtonId())).getTag().toString());
        params.put("studentAge", txtBirthday.getText().toString());
        params.put("parentName", editParName.getText().toString());
        params.put("relation", editRelation.getText().toString());
        params.put("address", editAddress.getText().toString());
        params.put("braceletCardNumber", editBraceletCardNum.getText().toString());
        params.put("braceletNumber", editBraceletNumber.getText().toString());
        mIsAudit = isAudit(params);
        params.put("isAudit", mIsAudit);
        return params;
    }

    private boolean isAudit(Map<String, Object> newData) {
        if (null == mOldData) {
            return true;
        }
        if (!mOldData.get("provId").equals(newData.get("provId"))) {
            return true;
        }
        if (!mOldData.get("cityId").equals(newData.get("cityId"))) {
            return true;
        }
        if (!mOldData.get("schoolId").equals(newData.get("schoolId"))) {
            return true;
        }
        if (!mOldData.get("classId").equals(newData.get("classId"))) {
            return true;
        }
        if (!mOldData.get("teacherId").equals(newData.get("teacherId"))) {
            return true;
        }
        if (!mOldData.get("studentName").equals(newData.get("studentName"))) {
            return true;
        }
        if (!mOldData.get("studentAge").equals(newData.get("studentAge"))) {
            return true;
        }
        if (!mOldData.get("parentName").equals(newData.get("parentName"))) {
            return true;
        }
        if (!mOldData.get("relation").equals(newData.get("relation"))) {
            return true;
        }
        return false;
    }

    private void afterInitSubmit(Object resultData) {
        if (null == resultData) {
            return;
        }
        if (!mIsAudit) {
            this.finish();
            return;
        }

        this.finish();
        Intent intent = new Intent(RelatedInfoActivity.this, AuditingActivity.class);
        startActivity(intent);
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
        if (null == data || 0 == data.size()) {
            return;
        }
        if (null == classListControl) {
            classListControl = new SelectListControl(RelatedInfoActivity.this, data, mIsLastPage, title, view);
            classListControl.setOnItemClickListener(selectedItemListener);
            classListControl.setOnLoadMoreListener(onLoadMoreListener);
            classListControl.showAtLocation(view, Gravity.CENTER, 0, 0);
        } else {
            classListControl.setLoadMoreData(data, mIsLastPage);
        }
    }

    private SelectListControl.OnItemSelectedListener selectedItemListener = new SelectListControl.OnItemSelectedListener() {
        @Override
        public void onItemSelected(CommonEntity obj, View view) {
            TextView textView = (TextView) view;
            if (view.getTag().equals(obj.getId())) {
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
            if (mIsLastPage) {
                return;
            }
            mPageIndex++;
            requestHttp();
        }
    };

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
        MyIon.httpPost(this, mHostUrl + "school/requestHeadTeacher", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
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
