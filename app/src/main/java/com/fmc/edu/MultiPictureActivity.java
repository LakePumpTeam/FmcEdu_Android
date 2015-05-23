package com.fmc.edu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fmc.edu.adapter.MultiPictureItemAdapter;
import com.fmc.edu.common.Constant;
import com.fmc.edu.entity.ImageItemEntity;
import com.fmc.edu.utils.ToastToolUtils;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MultiPictureActivity extends Activity {
    private Context mContext;
    private GridView grid;
    private LinearLayout llBack;
    private TextView txtSelected;
    private TextView txtOk;
    private ImageLoader mImageLoader;
    private MultiPictureItemAdapter mAdapter;
    private List<ImageItemEntity> mSelectedList;
    private int mMaxCount = 4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mContext = this;
        setContentView(R.layout.activity_multi_picture);
        mSelectedList = (List<ImageItemEntity>) getIntent().getExtras().getSerializable("selectedList");
        initView();
        initViewEvents();
        initImageLoader();
        initImages();
        initPageDataSource();
    }

    private void initView() {
        grid = (GridView) findViewById(R.id.multi_picture_grid);
        grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        llBack = (LinearLayout) findViewById(R.id.multi_picture_ll_back);
        txtSelected = (TextView) findViewById(R.id.multi_picture_txt_selected);
        txtOk = (TextView) findViewById(R.id.multi_picture_txt_ok);

    }

    private void initViewEvents() {
        txtOk.setOnClickListener(btnOKOnClickListener);
        llBack.setOnClickListener(llBackOnClickListener);
    }

    private void initImageLoader() {
        try {

            new File(Constant.CACHE_DIR).mkdirs();

            File cacheDir = StorageUtils.getOwnCacheDirectory(mContext, Constant.CACHE_DIR);
            DisplayImageOptions options;
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.mipmap.ic_launcher)
                    .showImageForEmptyUri(R.mipmap.ic_launcher)
                    .showImageOnFail(R.mipmap.ic_launcher)
                    .cacheOnDisk(false)
                    .cacheInMemory(false)
                    .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565)
                    .build();//构建完成

            ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(mContext)
                    .defaultDisplayImageOptions(options)
                    .threadPoolSize(3)
                    .diskCacheFileCount(100)
                    .diskCacheExtraOptions(480, 400, null)
                    .denyCacheImageMultipleSizesInMemory()
                    .memoryCacheSizePercentage(75) // defaultF
                    .diskCache(new UnlimitedDiscCache(cacheDir))
                    .memoryCache(new WeakMemoryCache());

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
        }

    }

    private List<ImageItemEntity> getImageList() {
        ArrayList<ImageItemEntity> galleryList = new ArrayList<ImageItemEntity>();

        try {
            final String[] columns = {MediaStore.Images.Media.DATA,
                    MediaStore.Images.Media._ID};
            final String orderBy = MediaStore.Images.Media._ID;

            Cursor cursor = ((Activity) mContext).managedQuery(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns,
                    null, null, orderBy);

            if (cursor != null && cursor.getCount() > 0) {

                while (cursor.moveToNext()) {
                    ImageItemEntity item = new ImageItemEntity();
                    int dataColumnIndex = cursor
                            .getColumnIndex(MediaStore.Images.Media.DATA);
                    item.thumbUrl = cursor.getString(dataColumnIndex);
                    if (isSelected(item.thumbUrl)) {
                        item.isCheck = true;
                    }
                    galleryList.add(item);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return galleryList;
    }

    private boolean isSelected(String imgUrl) {
        if (null == mSelectedList || mSelectedList.size() == 0) {
            return false;
        }
        for (int i = 0; i < mSelectedList.size(); i++) {
            if (mSelectedList.get(i).thumbUrl.equals(imgUrl)) {
                return true;
            }
        }
        return false;
    }

    private void initPageDataSource() {
        String selectedMsg = (null == mSelectedList ? 0 : mSelectedList.size()) + "/" + mMaxCount;
        txtSelected.setText(selectedMsg);
        mAdapter = new MultiPictureItemAdapter(mContext, mImageLoader);
        grid.setAdapter(mAdapter);
        grid.setOnItemClickListener(gridOnItemClickListener);
    }

    private AdapterView.OnItemClickListener gridOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ImageItemEntity imageItemEntity = (ImageItemEntity) mAdapter.getItem(position);
            int selectCount = mAdapter.getSelectedCount();
            if (imageItemEntity.isCheck) {
                mAdapter.setCheck(position, false, view);
                selectCount--;
                String titleText = (selectCount > 0 ? selectCount + "/" : "0/") + mMaxCount;
                txtSelected.setText(titleText);
                return;
            }
            if (selectCount >= mMaxCount) {
                String msg = "最多选择" + mMaxCount + "张图片";
                ToastToolUtils.showLong(msg);
                return;
            }
            mAdapter.setCheck(position, true, view);
            selectCount++;
            String titleText = (selectCount > 0 ? selectCount + "/" : "0/") + mMaxCount;
            txtSelected.setText(titleText);
        }
    };

    private View.OnClickListener llBackOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            clearCache();
            MultiPictureActivity.this.finish();
        }
    };

    private View.OnClickListener btnOKOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putSerializable("selectedList", (Serializable) mAdapter.getSelectedList());
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            clearCache();
            MultiPictureActivity.this.finish();
        }
    };

    private void clearCache() {
        mImageLoader.clearMemoryCache();
        mImageLoader.clearDiskCache();
    }
}
