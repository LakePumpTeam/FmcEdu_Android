package com.fmc.edu.customcontrol;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.fmc.edu.R;
import com.fmc.edu.adapter.ImageSelectItemAdapter;
import com.fmc.edu.entity.ImageItemEntity;
import com.fmc.edu.utils.ToastToolUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Candy on 2015/5/19.
 */
public class ImageSelectListControl extends GridView {
    private Context mContext;
    private ImageLoader mImageLoader;
    public ImageSelectItemAdapter mAdapter;
    private static final int SHOW_NUM_COLUMNS = 3;

    private OnAfterSelectedListener mOnAfterSelectedListener;

    public interface OnAfterSelectedListener {
        void onAfterSelected(int selectedCount);

    }

    public void setOnAfterSelectedListener(OnAfterSelectedListener onAfterSelectedListener) {
        this.mOnAfterSelectedListener = onAfterSelectedListener;
    }


    public ImageSelectListControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initContent();
        initImageLoader();
        initImages();
        bindGridViewSource();
       // this.setOnScrollListener(new PauseOnScrollListener(mImageLoader, false, true));
    }

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ToastToolUtils.showLong("test");
        }
    };

    private void initContent() {
        this.setNumColumns(SHOW_NUM_COLUMNS);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setLayoutParams(layoutParams);
        this.setHorizontalSpacing(5);
        this.setVerticalSpacing(5);
        this.setBackgroundColor(Color.WHITE);
        this.setFastScrollEnabled(true);
        this.setStretchMode(STRETCH_COLUMN_WIDTH);
    }

    private void bindGridViewSource() {
        mAdapter = new ImageSelectItemAdapter(mContext, mImageLoader);
        this.setAdapter(mAdapter);
        this.setOnItemClickListener(onItemClickListener);
        //mAdapter.setOnAfterSelectedListener(onAfterSelectedListener);
    }

    private ImageSelectItemAdapter.OnAfterSelectedListener onAfterSelectedListener = new ImageSelectItemAdapter.OnAfterSelectedListener() {
        @Override
        public void onAfterSelected(int selectedCount) {
            if (null == mOnAfterSelectedListener) {
                return;
            }
            mOnAfterSelectedListener.onAfterSelected(selectedCount);
        }
    };

    public void setSelectedList(List<ImageItemEntity> list) {

    }


    private void initImageLoader() {
        try {
            String CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "/cache";
            new File(CACHE_DIR).mkdirs();

            File cacheDir = StorageUtils.getOwnCacheDirectory(mContext, CACHE_DIR);

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
            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(mContext)
                    .defaultDisplayImageOptions(options)
                    .threadPoolSize(3)
                    .diskCacheFileCount(100)
                    .diskCacheExtraOptions(480, 800, null)
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCache(new LruMemoryCache(2 * 1024 * 1024)) //可以通过自己的内存缓存实现
                    .memoryCacheSize(50 * 1024 * 1024)  // 内存缓存的最大值
                    .memoryCacheSizePercentage(13) // defaultF
                    .diskCache(new UnlimitedDiscCache(cacheDir))
                    .memoryCache(new WeakMemoryCache());// 图片加载好后渐入的动画时间  ;// max width, max height，即保存的每个缓存文件的最大长宽

            ImageLoaderConfiguration config = builder.build();
            mImageLoader = ImageLoader.getInstance();
            mImageLoader.init(config);

        } catch (Exception e) {

        }
    }

    private void initImages() {
        try {
            final Handler handler = new Handler();
            new Thread() {
                @Override
                public void run() {
                    Looper.prepare();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            mAdapter.addAllList(getImageList());
                        }
                    });
                    Looper.loop();
                }
            }.start();
        } catch (Exception e) {
            Log.e("initImages", "*******************---------------****************");
        }

    }

    private List<ImageItemEntity> getImageList() {
        ArrayList<ImageItemEntity> galleryList = new ArrayList<ImageItemEntity>();

        try {
            final String[] columns = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media._ID;

            Cursor imagecursor = ((Activity) mContext).managedQuery(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                    null, null, orderBy);

            if (imagecursor != null && imagecursor.getCount() > 0) {

                while (imagecursor.moveToNext()) {
                    ImageItemEntity item = new ImageItemEntity();
                    int dataColumnIndex = imagecursor
                            .getColumnIndex(MediaStore.Images.Media.DATA);
                    item.imageURL = imagecursor.getString(dataColumnIndex);
                    galleryList.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(galleryList);
        return galleryList;
    }

    public void clearCache() {

        mImageLoader.clearMemoryCache();
        mImageLoader.clearDiskCache();
    }
}