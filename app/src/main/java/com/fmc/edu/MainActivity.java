package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fmc.edu.customcontrol.AlertWindowControl;
import com.fmc.edu.customcontrol.CircleImageControl;
import com.fmc.edu.customcontrol.MenuItemControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.http.FMCMapFutureCallback;
import com.fmc.edu.http.HttpTools;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.http.NetWorkUnAvailableException;
import com.fmc.edu.utils.AppConfigUtils;
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
    private ProgressControl progressControl;
    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        progressControl = new ProgressControl(this);
        initImageLoader();
        initViewEvents();
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
    }

    private void initData() {
        //TODO 路径没有配置好
        try {
            progressControl.showWindow(txtTeacher);
            String url = AppConfigUtils.getServiceHost() + "获取初始数据";
            MyIon.with(this)
                    .load(url)
                    .asString(Charset.forName("utf8"))
                    .setCallback(new FMCMapFutureCallback() {
                        @Override
                        public void onTranslateCompleted(Exception e, Map<String, ?> result) {
                            progressControl.dismiss();
                            if (!HttpTools.isRequestSuccessfully(e, result)) {
                                AlertWindowControl alertWindowControl = new AlertWindowControl(MainActivity.this);
                                alertWindowControl.showWindow(txtTeacher, "获取失败", e.getMessage());
                                return;
                            }
                            Map<String, Object> data = (Map<String, Object>) result.get("data");
                            afterInitData(data);
                        }
                    });
        } catch (NetWorkUnAvailableException e) {
            progressControl.dismiss();
            e.printStackTrace();
        }
    }

    private void afterInitData(Map<String, Object> initData) {
        //TODO 根据返回数据绑定值
        mImageLoader.displayImage(initData.get("imgurl").toString(), circleImgHeadPhoto);
        menuSchoolDynamic.setHasDynamic(true);
        menuGradeDynamic.setHasDynamic(true);
        menuSyllabusDynamic.setHasDynamic(true);
        menuParenting.setHasDynamic(true);
        menuKidsSchool.setHasDynamic(true);
        menuCampus.setHasDynamic(true);
        menuLocation.setHasDynamic(true);
        menuAudit.setHasDynamic(true);
        txtTeacher.setText("");
        txtClassGrade.setText("");
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
                    gotoDetailPage(v, SchoolDynamicActivity.class);
                    break;
                case R.id.main_menu_grade_dynamic:
                    gotoDetailPage(v, RegisterActivity.class);
                    break;
                case R.id.main_menu_syllabus_dynamic:
                    gotoDetailPage(v, RegisterActivity.class);
                    break;
                case R.id.main_menu_parenting:
                    gotoDetailPage(v, RegisterActivity.class);
                    break;
                case R.id.main_menu_kid_school:
                    gotoDetailPage(v, KidSchoolActivity.class);
                    break;
                case R.id.main_menu_campus:
                    gotoDetailPage(v, RegisterActivity.class);
                    break;
                case R.id.main_menu_location:
                    gotoDetailPage(v, RegisterActivity.class);
                    break;
                case R.id.main_menu_audit:
                    gotoDetailPage(v, RegisterActivity.class);
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
