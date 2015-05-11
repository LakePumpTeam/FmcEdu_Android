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
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.SelectListControl;
import com.fmc.edu.entity.CommonEntity;
import com.fmc.edu.http.FMCMapFutureCallback;
import com.fmc.edu.http.HttpTools;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.http.NetWorkUnAvailableException;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.MapTokenTypeUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.koushikdutta.async.future.FutureCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
    private TextView txtProvince;
    private TextView txtSchool;
    private TextView txtTeacher;
    private String mCellphone;
    private ProgressControl mProgressControl;
    private String mHostUrl;
    private Map<String, Object> mParams;
    private SelectListControl classListControl;
    private int mPageIndex;
    private int mPageSize;


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

    private View.OnClickListener btnSubmitAuditOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (!isInputFinish()) {
                return;
            }
            doSubmitAudit(v);

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
        mParams = new HashMap<String, Object>();
        mPageIndex = 1;
        classListControl = null;
        //TODO 处理数据
        switch (operateType) {
            case Province:
                handleDropDownClick(v, "location/requestProv", OperateType.Province);
                break;
            case City:
                handleDropDownClick(v, "location/requestCities", OperateType.City);
                break;
            case School:
                handleDropDownClick(v, "school/requestSchools", OperateType.School);
                break;
            case Class:
                handleDropDownClick(v, "school/requestClasses", OperateType.Class);
                break;
            default:
                break;
        }
    }


    private SelectListControl.OnItemSelectedListener selectedItemListener = new SelectListControl.OnItemSelectedListener() {
        @Override
        public void onItemSelected(CommonEntity obj, View view) {
            TextView textView = (TextView) view;
            textView.setText(obj.getFullName());
            textView.setTag(obj.getId());
        }
    };


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

    private void doSubmitAudit(View view) {
        try {
            //TODO 路径没有配好
            mProgressControl.showWindow(view);
            MyIon.with(this)
                    .load(AppConfigUtils.getServiceHost() + "提交审核路径")
                    .setBodyParameter("address", editAddress.getText().toString())
                    .setBodyParameter("cellphone", mCellphone)
                    .setBodyParameter("birthday", editBirthday.getText().toString())
                    .setBodyParameter("devicecardnum", editDeviceCardNum.getText().toString())
                    .setBodyParameter("devicecode", editDeviceCode.getText().toString())
                    .setBodyParameter("parname", editParName.getText().toString())
                    .setBodyParameter("relation", editRelation.getText().toString())
                    .setBodyParameter("stuname", editStuName.getText().toString())
                    .setBodyParameter("sex", (findViewById(rgSex.getCheckedRadioButtonId())).getTag().toString())
                    .setBodyParameter("schoolid", String.valueOf(txtSchool.getTag()))
                    .setBodyParameter("proviceid", String.valueOf(txtProvince.getTag()))
                    .setBodyParameter("cityid", String.valueOf(txtCity.getTag()))
                    .setBodyParameter("classid", String.valueOf(txtClass.getTag()))
                    .setBodyParameter("teacherid", String.valueOf(txtTeacher.getTag()))
                    .as(new MapTokenTypeUtils())
                    .setCallback(new FutureCallback<Map<String, Object>>() {
                        @Override
                        public void onCompleted(Exception e, Map<String, Object> result) {
                            mProgressControl.dismiss();
                            if (!HttpTools.isRequestSuccessfully(e, result)) {
                                AlertWindowControl alertWindowControl = new AlertWindowControl(RelatedInfoActivity.this);
                                alertWindowControl.showWindow(btnSubmitAudit, "提交失败", e.getMessage());
                                return;
                            }
                            afterInitSubmit();
                        }
                    });
        } catch (NetWorkUnAvailableException e) {
            mProgressControl.dismiss();
            e.printStackTrace();
        }
    }

    private void initData() {
        ((RadioButton) rgSex.getChildAt(0)).setChecked(true);
        try {
            //TODO 路径没有配好、参数没有定好
            mProgressControl.showWindow(btnSubmitAudit);
            MyIon.with(this)
                    .load(AppConfigUtils.getServiceHost() + "获取初始值路径")
                    .setBodyParameter("cellphone", mCellphone)
                    .as(new MapTokenTypeUtils())
                    .setCallback(new FutureCallback<Map<String, Object>>() {
                        @Override
                        public void onCompleted(Exception e, Map<String, Object> result) {
                            mProgressControl.dismiss();
                            if (!HttpTools.isRequestSuccessfully(e, result)) {
                                return;
                            }
                            afterInitData();
                        }
                    });
        } catch (NetWorkUnAvailableException e) {
            mProgressControl.dismiss();
            e.printStackTrace();
        }

    }

    private void afterInitData() {
        //TODO 调用接口，对返回的数据进行绑定
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

    private void afterInitSubmit() {
//TODO 提交成功后的操作
    }


    private void handleDropDownClick(View view, String url, final OperateType operateType) {
        mProgressControl.showWindow(view);
        handleParams(operateType);
        MyIon.setUrlAndBodyParams(this, mHostUrl + url, mParams, mProgressControl)
                .setCallback(new FMCMapFutureCallback() {
                    @Override
                    public void onTranslateCompleted(Exception e, Map<String, ?> result) {
                        mProgressControl.dismiss();
                        if (!HttpTools.isRequestSuccessfully(e, result)) {
                            ToastToolUtils.showLong(result.get("msg").toString());
                            return;
                        }
                        Map<String, Object> data = (Map<String, Object>) result.get("data");
                        afterGetList(operateType, data);
                    }
                });
    }


    private void handleParams(OperateType operateType) {
        mParams.put("pageIndex", mPageIndex);
        mParams.put("pageSize", mPageSize);
        switch (operateType) {
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

    private void afterGetList(OperateType operateType, Map<String, Object> result) {
        List<CommonEntity> data = new ArrayList<>();
        String title = "";
        View view = null;
        switch (operateType) {
            case Province:
                data = getCommonEntityList(result, "provs", operateType);
                view = txtProvince;
                title = "省份列表";
                break;
            case City:
                data = getCommonEntityList(result, "cities", operateType);
                view = txtCity;
                title = "城市列表";
                break;
            case School:
                data = getCommonEntityList(result, "schools", operateType);
                view = txtSchool;
                title = "学校列表";
                break;
            case Class:
                data = getCommonEntityList(result, "classes", operateType);
                view = txtClass;
                title = "班级列表";
                break;
        }
        if (null == classListControl) {
            classListControl = new SelectListControl(RelatedInfoActivity.this, data, title, view);
            classListControl.setOnItemClickListener(selectedItemListener);
            classListControl.showAtLocation(view, Gravity.CENTER, 0, 0);
        } else {
            classListControl.setLoadMoreData(data);
        }
    }

    private List<CommonEntity> getCommonEntityList(Map<String, Object> result, String key, OperateType operateType) {

        Object obj = result.get("key");
        if (null == obj) {
            return new ArrayList<>();
        }
        List<Map<String, Object>> data = (List<Map<String, Object>>) obj;
        return getCommonEntityList(data, operateType);
    }

    private List<CommonEntity> getCommonEntityList(List<Map<String, Object>> list, OperateType operateType) {

        List<CommonEntity> commonEntityList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> item = list.get(i);
            CommonEntity commonEntity = mapToCommonEntity(item, operateType);
            commonEntityList.add(commonEntity);
        }
        return commonEntityList;
    }


    private CommonEntity mapToCommonEntity(Map<String, Object> map, OperateType operateType) {
        switch (operateType) {
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

    enum OperateType {
        Province,
        City,
        School,
        Class;
    }
}
