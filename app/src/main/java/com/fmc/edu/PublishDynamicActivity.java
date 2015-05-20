package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.fmc.edu.adapter.PublishDynamicGridAdapter;
import com.fmc.edu.common.CrashHandler;
import com.fmc.edu.customcontrol.MultiPictureControl;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.ImageItemEntity;
import com.fmc.edu.utils.ToastToolUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.List;


public class PublishDynamicActivity extends Activity {
    private TopBarControl topBarSend;
    private EditText editContent;
    private GridView gridPicture;
    private TextView txtAddPicture;
    private final static int REQUEST_CODE = 1;
    private ImageLoader mImageLoader;

    private PublishDynamicGridAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        setContentView(R.layout.activity_publish_dynamic);
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

    }

    private TopBarControl.OnOperateOnClickListener sendListener = new TopBarControl.OnOperateOnClickListener() {
        @Override
        public void onOperateClick(View v) {
            //TODO 发送动态
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

    private MultiPictureControl.OnSelectedListener onSelectedListener = new MultiPictureControl.OnSelectedListener() {
        @Override
        public void onSelected(List<ImageItemEntity> selectedImageList) {
            mAdapter.addAll(selectedImageList);

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ToastToolUtils.showLong("test")
        ;
    }


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
                    .diskCacheExtraOptions(480, 800, null)
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
