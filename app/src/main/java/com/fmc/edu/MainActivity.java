package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fmc.edu.adapter.WaitAuditAdapter;
import com.fmc.edu.common.CrashHandler;
import com.fmc.edu.customcontrol.CircleImageControl;
import com.fmc.edu.customcontrol.MenuItemControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.entity.WaitAuditEntity;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ServicePreferenceUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class MainActivity extends Activity {

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

    private ProgressControl mProgressControl;
    private ImageLoader mImageLoader;
    private int mUserRole;
    private Bundle mBundle;
    private String mHostUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FmcApplication.addActivity(this);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        initViews();
        mBundle = getIntent().getExtras();
        mProgressControl = new ProgressControl(this);
        mHostUrl = AppConfigUtils.getServiceHost();
        initImageLoader();
        initViewEvents();
        afterInitData();
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
    }

    private void initViewEvents() {
        imgSendNewMsg.setOnClickListener(imgSendNewMsgOnClick);
        topBar.setOnOperateOnClickListener(settingOperatorOnClickListener);

        menuSchoolDynamic.setOnClickListener(menuItemOnClickListener);
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
        menuSchoolDynamic.setHasDynamic(false);
        menuGradeDynamic.setHasDynamic(false);
        menuSyllabusDynamic.setHasDynamic(false);
        menuParenting.setHasDynamic(false);
        menuKidsSchool.setHasDynamic(false);
        menuCampus.setHasDynamic(false);
        menuLocation.setHasDynamic(false);
        menuAudit.setHasDynamic(false);
        txtTeacher.setText(mBundle.getString("teacherName"));
        txtTeacher.setTag(mBundle.getString("teacherId"));
        txtClassGrade.setText(mBundle.getString("className"));
        mUserRole = mBundle.getInt("userRole");
        if (mBundle.getBoolean("sex")) {
            circleImgHeadPhoto.setImageDrawable(getResources().getDrawable(R.mipmap.head_photo_boy));
        } else {
            circleImgHeadPhoto.setImageDrawable(getResources().getDrawable(R.mipmap.head_photo_girl));
        }
        if (mUserRole == 1) {
            rlAudit.setVisibility(View.VISIBLE);
        } else if (mUserRole == 2) {
            rlAudit.setVisibility(View.INVISIBLE);
        }
    }

    private void initImageLoader() {
        try {
            String CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache";
            new File(CACHE_DIR).mkdirs();
            File cacheDir = StorageUtils.getOwnCacheDirectory(this, CACHE_DIR);

            DisplayImageOptions options;
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_launcher)
                    .showImageForEmptyUri(R.mipmap.ic_launcher)
                    .showImageOnFail(R.mipmap.ic_launcher)
                    .cacheOnDisk(true)
                    .cacheInMemory(false)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();//构建完成

            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(this)
                    .defaultDisplayImageOptions(options)
                    .threadPoolSize(3)
                    .diskCacheFileCount(100)
                    .diskCacheExtraOptions(100, 100, null)
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //可以通过自己的内存缓存实现
                    .memoryCacheSize(2 * 1024 * 1024)  // 内存缓存的最大值
                    .memoryCacheSizePercentage(13) // defaultF
                    .diskCache(new UnlimitedDiscCache(cacheDir))
                    .memoryCache(new WeakMemoryCache());// 图片加载好后渐入的动画时间  ;// max width, max height，即保存的每个缓存文件的最大长宽

            ImageLoaderConfiguration config = builder.build();
            mImageLoader = ImageLoader.getInstance();
            mImageLoader.init(config);

        } catch (Exception e) {

        }
    }

    private View.OnClickListener imgSendNewMsgOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            gotoSendDynamic();
        }
    };

    private View.OnClickListener photoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mUserRole == 1) {
                LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(MainActivity.this);
                gotoTeacherPage(loginUserEntity.userId, true);
                return;
            }
            gotoRelationPage();
        }
    };

    private View.OnClickListener txtTeacherOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mUserRole == 1) {
                return;
            }
            gotoTeacherPage(ConvertUtils.getInteger(v.getTag()), false);
        }
    };


    private TopBarControl.OnOperateOnClickListener settingOperatorOnClickListener = new TopBarControl.OnOperateOnClickListener() {
        @Override
        public void onOperateClick(View v) {
            Intent intent = new Intent(MainActivity.this, SettingActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener menuItemOnClickListener = new View.OnClickListener() {
        //TODO  每一个Item绑定自己的Class
        @Override
        public void onClick(View v) {
            int clickId = v.getId();
            switch (clickId) {
                case R.id.main_menu_school_dynamic:
                    if (AppConfigUtils.isDevelopTwo()) {
                        gotoDetailPage(v, SchoolDynamicActivity.class);
                    }
                    break;
                case R.id.main_menu_grade_dynamic:
                    if (AppConfigUtils.isDevelopTwo()) {
                        gotoDetailPage(v, RegisterActivity.class);
                    }
                    break;
                case R.id.main_menu_syllabus_dynamic:
                    if (AppConfigUtils.isDevelopTwo()) {
                        gotoDetailPage(v, RegisterActivity.class);
                    }
                    break;
                case R.id.main_menu_parenting:
                    if (AppConfigUtils.isDevelopTwo()) {
                        gotoDetailPage(v, RegisterActivity.class);
                    }
                    break;
                case R.id.main_menu_kid_school:
                    if (AppConfigUtils.isDevelopTwo()) {
                        gotoDetailPage(v, KidSchoolActivity.class);
                    }
                    break;
                case R.id.main_menu_campus:
                    if (AppConfigUtils.isDevelopTwo()) {
                        gotoDetailPage(v, RegisterActivity.class);
                    }
                    break;
                case R.id.main_menu_location:
                    if (AppConfigUtils.isDevelopTwo()) {
                        gotoDetailPage(v, RegisterActivity.class);
                    }
                    break;
                case R.id.main_menu_audit:
                    gotoWaitAuditPage();
                    break;
                default:
                    break;
            }
        }
    };

    private void gotoDetailPage(View view, Class<?> cls) {
        MenuItemControl menuItemControl = (MenuItemControl) view;
        menuItemControl.setHasDynamic(false);
        Intent intent = new Intent(MainActivity.this, cls);
        startActivity(intent);
    }

    private void gotoTeacherPage(final int teacherId, final boolean isModify) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("teacherId", teacherId);
        MyIon.httpPost(this, mHostUrl + "school/requestTeacherInfo", params, mProgressControl, new MyIon.AfterCallBack() {
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

    private void gotoRelationPage() {
        mProgressControl.showWindow(circleImgHeadPhoto);
        LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(this);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("parentId", loginUserEntity.userId);
        MyIon.httpPost(this, mHostUrl + "profile/requestGetRelateInfo", params, mProgressControl, new MyIon.AfterCallBack() {
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

    private void gotoWaitAuditPage() {
        LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(MainActivity.this);
        Map<String, Object> params = new HashMap<>();
        params.put("teacherId", loginUserEntity.userId);
        MyIon.httpPost(MainActivity.this, mHostUrl + "profile/requestPendingAuditParentList", params, null, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                List<WaitAuditEntity> list = ToWaitAuditEntity((List<Map<String, Object>>) data.get("parentsAuditList"));
                menuAudit.setHasDynamic(false);
                Intent intent = new Intent(MainActivity.this, WaitAuditActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) list);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private List<WaitAuditEntity> ToWaitAuditEntity(List<Map<String, Object>> data) {
        List<WaitAuditEntity> list = new ArrayList<WaitAuditEntity>();
        for (int i = 0; i < data.size(); i++) {
            WaitAuditEntity waitAuditItem = new WaitAuditEntity();
            Map<String, Object> item = data.get(i);
            waitAuditItem.parentId = ConvertUtils.getInteger(item.get("parentId"));
            waitAuditItem.cellphone = ConvertUtils.getString(item.get("cellPhone"));
            waitAuditItem.parentName = ConvertUtils.getString(item.get("parentName"));
            waitAuditItem.auditStatus = ConvertUtils.getInteger(item.get("auditStatus"), 1);
            list.add(waitAuditItem);
        }
        return list;
    }

    private void gotoSendDynamic() {
        Intent intent = new Intent(MainActivity.this, PublishDynamicActivity.class);
        startActivity(intent);

    }
}
