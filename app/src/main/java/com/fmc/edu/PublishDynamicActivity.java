package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.fmc.edu.adapter.PublishDynamicGridAdapter;
import com.fmc.edu.common.CrashHandler;
import com.fmc.edu.customcontrol.AlertWindowControl;
import com.fmc.edu.customcontrol.MultiPictureControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.ImageItemEntity;
import com.fmc.edu.http.FMCMapFutureCallback;
import com.fmc.edu.http.HttpTools;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.StringUtils;
import com.fmc.edu.utils.ToastToolUtils;
import com.koushikdutta.async.future.FutureCallback;
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
import java.util.List;
import java.util.Map;


public class PublishDynamicActivity extends Activity {
    private TopBarControl topBarSend;
    private EditText editContent;
    private GridView gridPicture;
    private TextView txtAddPicture;
    private final static int REQUEST_CODE = 1;
    private ImageLoader mImageLoader;
    private String mHostUrl;
    private PublishDynamicGridAdapter mAdapter;
    private ProgressControl mProgressControl;
    MultiPictureControl multiPictureControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        setContentView(R.layout.activity_publish_dynamic);
        mProgressControl = new ProgressControl(this);
        mHostUrl = AppConfigUtils.getServiceHost();
        initViews();
        initViewEvent();
        initImageLoader();
        mAdapter = new PublishDynamicGridAdapter(PublishDynamicActivity.this, null, mImageLoader);
        gridPicture.setAdapter(mAdapter);
    }

    private void initViews() {
        topBarSend = (TopBarControl) findViewById(R.id.publish_dynamic_top_bar_send);
        editContent = (EditText) findViewById(R.id.publish_dynamic_edit_content);
        gridPicture = (GridView) findViewById(R.id.publish_dynamic_grid_picture);
        txtAddPicture = (TextView) findViewById(R.id.publish_dynamic_txt_add_picture);
        gridPicture.setSelector(new ColorDrawable(Color.TRANSPARENT));

    }

    private void initViewEvent() {
        topBarSend.setOnOperateOnClickListener(sendListener);
        txtAddPicture.setOnClickListener(addPictureClickListener);
        gridPicture.setOnItemLongClickListener(gridOnItemLongClickListener);
    }

    private TopBarControl.OnOperateOnClickListener sendListener = new TopBarControl.OnOperateOnClickListener() {
        @Override
        public void onOperateClick(View v) {
            if (StringUtils.isEmptyOrNull(editContent.getText())) {
                ToastToolUtils.showLong("请输入内容呢");
                return;
            }
            ToastToolUtils.showLong("发送成功");
            PublishDynamicActivity.this.finish();
            //TODO 发送动态
//            mProgressControl.showWindow(topBarSend);
//
//            String content = editContent.getText().toString();
//            String base64UserId = Base64.encodeToString(String.valueOf(FmcApplication.getLoginUser().userId).getBytes(), Base64.DEFAULT);
//            try {
//                MyIon.with(PublishDynamicActivity.this)
//                        .load(mHostUrl + "")
//                        .setMultipartFile("image1", mAdapter.isHavePicture(0) ? null : new File(mAdapter.getImageUrl(0)))
//                        .setMultipartFile("image2", mAdapter.isHavePicture(1) ? null : new File(mAdapter.getImageUrl(1)))
//                        .setMultipartFile("image3", mAdapter.isHavePicture(2) ? null : new File(mAdapter.getImageUrl(2)))
//                        .setMultipartFile("image4", mAdapter.isHavePicture(3) ? null : new File(mAdapter.getImageUrl(3)))
//                        .setMultipartParameter("content", Base64.encodeToString(content.getBytes(), Base64.DEFAULT))
//                        .setMultipartParameter("userId", base64UserId)
//                        .asString(Charset.forName("utf8"))
//                        .setCallback(new FMCMapFutureCallback() {
//                            @Override
//                            public void onTranslateCompleted(Exception e, Map<String, ?> result) {
//                                if (null != mProgressControl) {
//                                    mProgressControl.dismiss();
//                                }
//
//                                if (!HttpTools.isRequestSuccessfully(e, result))
//
//                                {
//                                    ToastToolUtils.showLong(result.get("msg").toString());
//                                    return;
//                                }
//
//                                if (StringUtils.isEmptyOrNull(result.get("data")))
//
//                                {
//                                    ToastToolUtils.showLong("服务器出错");
//                                    return;
//                                }
//
//                                Map<String, Object> mapData = (Map<String, Object>) result.get("data");
//                                if (ConvertUtils.getInteger(mapData.get("isSuccess")) != 0) {
//                                    AlertWindowControl alertWindowControl = new AlertWindowControl(PublishDynamicActivity.this);
//                                    alertWindowControl.showWindow(new TextView(PublishDynamicActivity.this), "提示", ConvertUtils.getString(mapData.get("businessMsg")));
//                                    return;
//                                }
//                                ToastToolUtils.showLong("发送成功");
//                                PublishDynamicActivity.this.finish();
//                            }
//                        });
//
//            } catch (Exception ex) {
//                ToastToolUtils.showLong(ex == null ? "发送失败" : ex.getMessage());
//            }
        }
    };
    private View.OnClickListener addPictureClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            MultiPictureControl multiPictureControl = new MultiPictureControl(PublishDynamicActivity.this, 4, mAdapter.getItems());
            multiPictureControl.showWindow(txtAddPicture);
            multiPictureControl.setOnSelectedListener(onSelectedListener);

        }
    };


    private AdapterView.OnItemLongClickListener gridOnItemLongClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            mAdapter.removeItem(position);
            gridPicture.recomputeViewAttributes(view);
            return false;
        }
    };
    private MultiPictureControl.OnSelectedListener onSelectedListener = new MultiPictureControl.OnSelectedListener() {
        @Override
        public void onSelected(List<ImageItemEntity> selectedImageList) {
            mAdapter.addAll(selectedImageList);
        }
    };


    private void initImageLoader() {
        try {
            String CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache";
            new File(CACHE_DIR).mkdirs();

            File cacheDir = StorageUtils.getOwnCacheDirectory(PublishDynamicActivity.this, CACHE_DIR);

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
            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(PublishDynamicActivity.this)
                    .defaultDisplayImageOptions(options)
                    .threadPoolSize(3)
                    .diskCacheFileCount(100)
                    .diskCacheExtraOptions(200, 200, null)
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //可以通过自己的内存缓存实现
                    .memoryCacheSize(50 * 1024 * 1024)  // 内存缓存的最大值
                    .memoryCacheSizePercentage(50) // defaultF
                    .diskCache(new UnlimitedDiscCache(cacheDir))
                    .memoryCache(new WeakMemoryCache());// 图片加载好后渐入的动画时间  ;// max width, max height，即保存的每个缓存文件的最大长宽

            ImageLoaderConfiguration config = builder.build();
            mImageLoader = ImageLoader.getInstance();
            mImageLoader.init(config);

        } catch (Exception e) {
        }
    }
}
