package com.fmc.edu;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fmc.edu.common.Constant;
import com.fmc.edu.customcontrol.CircleImageControl;
import com.fmc.edu.customcontrol.MenuItemControl;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.DynamicItemEntity;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.entity.CourseEntity;
import com.fmc.edu.entity.TaskEntity;
import com.fmc.edu.entity.WaitAuditEntity;
import com.fmc.edu.entity.WeekCourseEntity;
import com.fmc.edu.enums.DynamicTypeEnum;
import com.fmc.edu.enums.UserRoleEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.RequestCodeUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends BaseActivity {

    private CircleImageControl circleImgHeadPhoto;
    private ImageView imgSendNewMsg;
    private MenuItemControl menuSchoolDynamic;
    private MenuItemControl menuGradeDynamic;
    private MenuItemControl menuSyllabusDynamic;
    private MenuItemControl menuParenting;
    private MenuItemControl menuKidsSchool;
    private MenuItemControl menuCampus;
    private MenuItemControl menuLocation;
    private MenuItemControl menuAudit;
    private TextView txtTeacher;
    private TextView txtClassGrade;
    private TopBarControl topBar;
    private RelativeLayout rlAudit;
    private RelativeLayout rlLocation;

    private int mUserRole;
    private Bundle mBundle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_main);
        initViews();
        mBundle = getIntent().getExtras();
        initViewEvents();
        afterInitData();
        initNewDynamic();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void initViews() {
        circleImgHeadPhoto = (CircleImageControl) findViewById(R.id.main_circle_img_head_photo);
        imgSendNewMsg = (ImageView) findViewById(R.id.main_img_send_new_msg);
        menuSchoolDynamic = (MenuItemControl) findViewById(R.id.main_menu_school_dynamic);
        menuGradeDynamic = (MenuItemControl) findViewById(R.id.main_menu_grade_dynamic);
        menuSyllabusDynamic = (MenuItemControl) findViewById(R.id.main_menu_syllabus_dynamic);
        menuParenting = (MenuItemControl) findViewById(R.id.main_menu_parenting);
        menuKidsSchool = (MenuItemControl) findViewById(R.id.main_menu_kid_school);
        menuCampus = (MenuItemControl) findViewById(R.id.main_menu_campus);
        menuLocation = (MenuItemControl) findViewById(R.id.main_menu_location);
        menuAudit = (MenuItemControl) findViewById(R.id.main_menu_audit);
        txtTeacher = (TextView) findViewById(R.id.main_txt_teacher);
        txtClassGrade = (TextView) findViewById(R.id.main_txt_class_grade);
        topBar = (TopBarControl) findViewById(R.id.main_top_bar);
        rlAudit = (RelativeLayout) findViewById(R.id.main_menu_item_rl_audit);
        rlLocation = (RelativeLayout) findViewById(R.id.main_menu_item_rl_location);
    }

    private void initViewEvents() {
        imgSendNewMsg.setOnClickListener(imgSendNewMsgOnClick);
        topBar.setOnOperateOnClickListener(settingOperatorOnClickListener);

        menuSchoolDynamic.setOnClickListener(menuItemOnClickListener);
        menuSyllabusDynamic.setOnClickListener(menuItemOnClickListener);
        menuGradeDynamic.setOnClickListener(menuItemOnClickListener);
        menuParenting.setOnClickListener(menuItemOnClickListener);
        menuKidsSchool.setOnClickListener(menuItemOnClickListener);
        menuCampus.setOnClickListener(menuItemOnClickListener);
        menuLocation.setOnClickListener(menuItemOnClickListener);
        menuAudit.setOnClickListener(menuItemOnClickListener);
        circleImgHeadPhoto.setOnClickListener(photoOnClickListener);
        txtTeacher.setOnClickListener(txtTeacherOnClickListener);
    }

    private void afterInitData() {
        if (null == mBundle) {
            return;
        }
        txtTeacher.setText(mBundle.getString("teacherName"));
        txtTeacher.setTag(mBundle.getString("teacherId"));
        txtClassGrade.setText(mBundle.getString("className"));
        mUserRole = mBundle.getInt("userRole");
        ServicePreferenceUtils.saveSexPreference(this, mBundle.getBoolean("sex"));
        if (mBundle.getBoolean("sex")) {
            circleImgHeadPhoto.setImageDrawable(getResources().getDrawable(R.mipmap.head_photo_boy));
        } else {
            circleImgHeadPhoto.setImageDrawable(getResources().getDrawable(R.mipmap.head_photo_girl));
        }
        if (mUserRole == UserRoleEnum.getValue(UserRoleEnum.Teacher)) {
            rlLocation.setVisibility(View.GONE);
            rlAudit.setVisibility(View.VISIBLE);
            imgSendNewMsg.setVisibility(View.VISIBLE);
        } else if (mUserRole == UserRoleEnum.getValue(UserRoleEnum.Parent)) {
            rlLocation.setVisibility(View.VISIBLE);
            rlAudit.setVisibility(View.GONE);
            imgSendNewMsg.setVisibility(View.GONE);
        }
    }

    private void initNewDynamic() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", FmcApplication.getLoginUser().userId);
        MyIon.httpPost(this, "news/checkNewNews", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                menuSchoolDynamic.setHasDynamic(ConvertUtils.getBoolean(data.get("schoolNews"), false));
                menuGradeDynamic.setHasDynamic(ConvertUtils.getBoolean(data.get("classNews"), false));
                menuSyllabusDynamic.setHasDynamic(false);
                menuParenting.setHasDynamic(ConvertUtils.getBoolean(data.get("pcdNews"), false));
                menuKidsSchool.setHasDynamic(ConvertUtils.getBoolean(data.get("parentingClassNews"), false));
                menuCampus.setHasDynamic(ConvertUtils.getBoolean(data.get("bbsNews"), false));
                menuLocation.setHasDynamic(false);
                menuAudit.setHasDynamic(false);
            }
        });
    }

    private View.OnClickListener imgSendNewMsgOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (AppConfigUtils.isDevelopTwo()) {
                gotoSendDynamicActivity();
            }
        }
    };

    private View.OnClickListener photoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mUserRole == 1) {
                LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(MainActivity.this);
                gotoTeacherActivity(loginUserEntity.userId, true);
                return;
            }
            gotoRelationActivity();
        }
    };

    private View.OnClickListener txtTeacherOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mUserRole == 1) {
                return;
            }
            gotoTeacherActivity(ConvertUtils.getInteger(v.getTag()), false);
        }
    };


    private TopBarControl.OnOperateOnClickListener settingOperatorOnClickListener = new TopBarControl.OnOperateOnClickListener() {
        @Override
        public void onBackClick(View view) {

        }

        @Override
        public void onOperateClick(View v) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FmcApplication.clearAllActiviy();
    }

    private View.OnClickListener menuItemOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int clickId = v.getId();
            switch (clickId) {
                case R.id.main_menu_school_dynamic:
                    if (AppConfigUtils.isDevelopTwo()) {
                        menuSchoolDynamic.setHasDynamic(false);
                        gotoDynamicList(DynamicTypeEnum.SchoolActivity);
                    }
                    break;
                case R.id.main_menu_grade_dynamic:
                    if (AppConfigUtils.isDevelopTwo()) {
                        menuGradeDynamic.setHasDynamic(false);
                        gotoDynamicList(DynamicTypeEnum.ClassDynamic);
                    }
                    break;
                case R.id.main_menu_syllabus_dynamic:
                    if (AppConfigUtils.isDevelopThree()) {
                        menuSyllabusDynamic.setHasDynamic(false);
                        gotoSyllabusActivity();
                    }
                    break;
                case R.id.main_menu_parenting:
                    if (AppConfigUtils.isDevelopThree()) {
                        menuParenting.setHasDynamic(false);
                        gotoTaskListActivity();
                    }
                    break;
                case R.id.main_menu_kid_school:
                    if (AppConfigUtils.isDevelopTwo()) {
                        menuKidsSchool.setHasDynamic(false);
                        gotoDynamicList(DynamicTypeEnum.KidSchool);
                    }
                    break;
                case R.id.main_menu_campus:
                    if (AppConfigUtils.isDevelopThree()) {
                        menuCampus.setHasDynamic(false);
                        gotoCampusActivity();
                    }
                    break;
                case R.id.main_menu_location:
                    if (AppConfigUtils.isDevelopThree()) {
                        menuLocation.setHasDynamic(false);
                        gotoLocationActivity();
                    }
                    break;
                case R.id.main_menu_audit:
                    gotoWaitAuditActivity();
                    break;
                default:
                    break;
            }
        }
    };

    private void gotoDynamicList(final DynamicTypeEnum dynamicType) {
        mProgressControl.showWindow();
        String url = "news/requestNewsList";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pageIndex", 1);
        params.put("pageSize", Constant.PAGE_SIZE);
        params.put("userId", FmcApplication.getLoginUser().userId);
        params.put("type", DynamicTypeEnum.getValue(dynamicType));
        MyIon.httpPost(MainActivity.this, url, params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                List<Map<String, Object>> list = ConvertUtils.getList(data.get("newsList"));
                gotoDynamicActivity(DynamicItemEntity.toDynamicItemEntity(list), dynamicType, ConvertUtils.getBoolean(data.get("isLastPage")));
            }
        });
    }

    private void gotoDynamicActivity(List<DynamicItemEntity> list, DynamicTypeEnum dynamicType, boolean isLastPage) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("list", (Serializable) list);
        bundle.putBoolean("isLastPage", isLastPage);
        Intent intent = new Intent();
        if (dynamicType == DynamicTypeEnum.SchoolActivity) {
            intent.setClass(MainActivity.this, SchoolDynamicActivity.class);
        } else if (dynamicType == DynamicTypeEnum.KidSchool) {
            intent.setClass(MainActivity.this, KidSchoolActivity.class);
        } else {
            intent.setClass(MainActivity.this, ClassDynamicActivity.class);
        }
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void gotoSyllabusActivity() {
        mProgressControl.showWindow();
        LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(this);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("classId", loginUserEntity.classId);
        MyIon.httpPost(this, "school/requestClassCourseList", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("courseList");
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) WeekCourseEntity.toWeekCourseList(list));
                Intent intent = new Intent(MainActivity.this, SyllabusActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void gotoTaskListActivity() {
        mProgressControl.showWindow();
        Map<String, Object> param = new HashMap<>();
        param.put("userId", FmcApplication.getLoginUser().userId);
        param.put("pageIndex", 1);
        param.put("pageSize", Constant.PAGE_SIZE);
        param.put("filter", "");
        param.put("status", 0);
        MyIon.httpPost(this, "task/requestTaskList", param, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("taskList");

                Intent intent = new Intent(MainActivity.this, TaskListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) TaskEntity.toTaskEntityList(list));
                bundle.putBoolean("isLastPage", ConvertUtils.getBoolean(data.get("isLastPage")));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void gotoCampusActivity() {
        mProgressControl.showWindow();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pageIndex", 1);
        params.put("pageSize", Constant.PAGE_SIZE);
        params.put("userId", FmcApplication.getLoginUser().userId);
        params.put("type", DynamicTypeEnum.getValue(DynamicTypeEnum.Campus));
        MyIon.httpPost(MainActivity.this, "news/requestNewsList", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                menuCampus.setHasDynamic(false);
                List<Map<String, Object>> list = ConvertUtils.getList(data.get("newsList"));
                Bundle bundle = new Bundle();
                bundle.putSerializable("list", (Serializable) DynamicItemEntity.toDynamicItemEntity(list));
                bundle.putBoolean("isLastPage", ConvertUtils.getBoolean(data.get("isLastPage")));
                Intent intent = new Intent(MainActivity.this, CampusActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void gotoLocationActivity() {
        Intent intent = new Intent(MainActivity.this, IntelligentLocationActivity.class);
        startActivity(intent);
    }

    private void gotoWaitAuditActivity() {
        mProgressControl.showWindow();
        LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(MainActivity.this);
        Map<String, Object> params = new HashMap<>();
        params.put("teacherId", loginUserEntity.userId);
        MyIon.httpPost(MainActivity.this, "profile/requestPendingAuditParentList", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                List<WaitAuditEntity> list = WaitAuditEntity.ToWaitAuditEntity((List<Map<String, Object>>) data.get("parentsAuditList"));
                menuAudit.setHasDynamic(false);
                Intent intent = new Intent(MainActivity.this, WaitAuditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) list);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void gotoTeacherActivity(final int teacherId, final boolean isModify) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("teacherId", teacherId);
        MyIon.httpPost(this, "school/requestTeacherInfo", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                Bundle bundle = new Bundle();
                bundle.putInt("teacherId", teacherId);
                bundle.putString("course", ConvertUtils.getString(data.get("course")));
                bundle.putString("cellPhone", ConvertUtils.getString(data.get("cellPhone")));
                bundle.putString("teacherName", ConvertUtils.getString(data.get("teacherName")));
                bundle.putString("resume", ConvertUtils.getString(data.get("resume")));
                bundle.putString("teacherBirth", ConvertUtils.getString(data.get("teacherBirth")));
                bundle.putBoolean("teacherSex", ConvertUtils.getBoolean(data.get("teacherSex")));
                bundle.putBoolean("isModify", isModify);
                Intent intent = new Intent(MainActivity.this, TeacherInfoActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void gotoRelationActivity() {
        mProgressControl.showWindow();
        LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(this);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("parentId", loginUserEntity.userId);
        MyIon.httpPost(this, "profile/requestGetRelateInfo", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                Intent intent = new Intent(MainActivity.this, RelatedInfoActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("address", ConvertUtils.getString(data.get("address"), ""));
                bundle.putString("studentBirth", ConvertUtils.getString(data.get("studentBirth"), ""));
                bundle.putString("braceletCardNumber", ConvertUtils.getString(data.get("braceletCardNumber"), ""));
                bundle.putString("braceletNumber", ConvertUtils.getString(data.get("braceletNumber"), ""));
                bundle.putString("parentName", ConvertUtils.getString(data.get("parentName"), ""));
                bundle.putString("relation", ConvertUtils.getString(data.get("relation"), ""));
                bundle.putString("studentName", ConvertUtils.getString(data.get("studentName"), ""));
                bundle.putString("cellPhone", ConvertUtils.getString(data.get("cellPhone"), ""));
                bundle.putString("classId", ConvertUtils.getString(data.get("classId"), ""));
                bundle.putString("className", ConvertUtils.getString(data.get("className"), ""));
                bundle.putString("cityId", ConvertUtils.getString(data.get("cityId"), ""));
                bundle.putString("cityName", ConvertUtils.getString(data.get("cityName"), ""));
                bundle.putString("provId", ConvertUtils.getString(data.get("provId"), ""));
                bundle.putString("provName", ConvertUtils.getString(data.get("provName"), ""));
                bundle.putString("schoolId", ConvertUtils.getString(data.get("schoolId"), ""));
                bundle.putString("schoolName", ConvertUtils.getString(data.get("schoolName"), ""));
                bundle.putString("studentId", ConvertUtils.getString(data.get("studentId"), "0"));
                bundle.putString("addressId", ConvertUtils.getString(data.get("addressId"), "0"));
                bundle.putString("teacherId", ConvertUtils.getString(data.get("teacherId"), ""));
                bundle.putString("teacherName", ConvertUtils.getString(data.get("teacherName"), ""));
                bundle.putBoolean("studentSex", ConvertUtils.getBoolean(data.get("studentSex"), false));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void gotoSendDynamicActivity() {
        Intent intent = new Intent(MainActivity.this, PublishDynamicActivity.class);
        startActivityForResult(intent, RequestCodeUtils.PUBLISH_CLASS_DYNAMIC);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == RequestCodeUtils.PUBLISH_CLASS_DYNAMIC) {
            menuGradeDynamic.setHasDynamic(true);
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
