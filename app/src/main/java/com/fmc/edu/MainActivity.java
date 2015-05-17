package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fmc.edu.common.CrashHandler;
import com.fmc.edu.customcontrol.AlertWindowControl;
import com.fmc.edu.customcontrol.CircleImageControl;
import com.fmc.edu.customcontrol.MenuItemControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.http.FMCMapFutureCallback;
import com.fmc.edu.http.HttpTools;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.http.NetWorkUnAvailableException;
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
import java.nio.charset.Charset;
import java.util.HashMap;
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

    private ProgressControl progressControl;
    private ImageLoader mImageLoader;
    private int mUserRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        CrashHandler crashHandler = CrashHandler.getInstance();
         crashHandler.init(this);
        progressControl = new ProgressControl(this);
        initImageLoader();
        initViewEvents();
    }

    @Override
    protected void onStart() {
        super.onStart();
        initData();
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

    private void initData() {
        String url = AppConfigUtils.getServiceHost() + "home/requestHeaderTeacherForHomePage";
        LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(this);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("userId", loginUserEntity.userId);
        MyIon.httpPost(this, url, params, null, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                afterInitData(data);
            }
        });
    }

    private void afterInitData(Map<String, Object> initData) {
//        mImageLoader.displayImage(initData.get("imgurl").toString(), circleImgHeadPhoto);
        menuSchoolDynamic.setHasDynamic(false);
        menuGradeDynamic.setHasDynamic(false);
        menuSyllabusDynamic.setHasDynamic(false);
        menuParenting.setHasDynamic(false);
        menuKidsSchool.setHasDynamic(false);
        menuCampus.setHasDynamic(false);
        menuLocation.setHasDynamic(false);
        menuAudit.setHasDynamic(false);
        txtTeacher.setText(ConvertUtils.getString(initData.get("teacherName")));
        txtTeacher.setTag(ConvertUtils.getString(initData.get("teacherId")));
        txtClassGrade.setText(ConvertUtils.getString(initData.get("className")));
        mUserRole = ConvertUtils.getInteger(initData.get("userRole"));
        if (ConvertUtils.getBoolean(initData.get("sex"))) {
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
            //TODO 发送新动态
        }
    };

    private View.OnClickListener photoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mUserRole == 1) {
                LoginUserEntity loginUserEntity = ServicePreferenceUtils.getLoginUserByPreference(MainActivity.this);
                Intent intent = new Intent(MainActivity.this, TeacherInfoActivity.class);
                intent.putExtra("teacherId", loginUserEntity.userId);
                intent.putExtra("isModify", true);
                startActivity(intent);
                return;
            }
            Intent intent = new Intent(MainActivity.this, RelatedInfoActivity.class);
            intent.putExtra("isModify", true);
            startActivity(intent);
        }
    };

    private View.OnClickListener txtTeacherOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mUserRole == 1) {
                return;
            }
            Intent intent = new Intent(MainActivity.this, TeacherInfoActivity.class);
            intent.putExtra("teacherId", ConvertUtils.getString(v.getTag()));
            startActivity(intent);
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
                    gotoDetailPage(v, WaitAuditActivity.class);
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
}
