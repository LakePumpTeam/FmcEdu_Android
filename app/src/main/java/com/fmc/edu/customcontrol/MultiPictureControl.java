package com.fmc.edu.customcontrol;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.adapter.ImageSelectItemAdapter;
import com.fmc.edu.adapter.MultiPictureItemAdapter;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Candy on 2015/5/20.
 */
public class MultiPictureControl extends PopupWindow {
    private Context mContext;
    private DisplayMetrics mDisplayMetrics;
    private GridView grid;
    private LinearLayout llBack;
    private TextView txtSelected;
    private TextView txtOk;
    private ImageLoader mImageLoader;
    private OnSelectedListener mOnSelectedListener;

    public MultiPictureItemAdapter mAdapter;

    public interface OnSelectedListener {

        public void onSelected(List<ImageItemEntity> selectedImageList);
    }
    public MultiPictureControl(Context context) {
        super(context, null);
        this.mContext = context;
        mDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        initPopWindow();
        initContentView();
        initImageLoader();
        initPageDataSource();
        initImages();
    }

    private void initPopWindow() {
        this.setWidth(mDisplayMetrics.widthPixels);
        this.setHeight(mDisplayMetrics.heightPixels);
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        this.setTouchable(true);
        ColorDrawable dw = new ColorDrawable(-000000);
        this.setBackgroundDrawable(dw);
    }

    private void initContentView() {
        LinearLayout linearLayout = new LinearLayout(mContext);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
        linearLayout.setLayoutParams(params);
        linearLayout.setGravity(Gravity.CENTER);
        linearLayout.setBackgroundColor(Color.parseColor("#bb666666"));
        View view = LayoutInflater.from(mContext).inflate(R.layout.control_multi_picture, null);
        grid = (GridView) view.findViewById(R.id.multi_picture_grid);
        llBack = (LinearLayout) view.findViewById(R.id.multi_picture_ll_back);
        txtSelected = (TextView) view.findViewById(R.id.multi_picture_txt_selected);
        txtOk = (TextView) view.findViewById(R.id.multi_picture_txt_ok);
        txtOk.setOnClickListener(btnOKOnClickListener);
        llBack.setOnClickListener((View.OnClickListener) llBackOnClickListener);
        linearLayout.addView(view);
        this.setContentView(linearLayout);
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
                    .memoryCacheSizePercentage(50) // defaultF
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

    private void initPageDataSource() {
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
                String titleText = selectCount > 0 ? selectCount + "/4" : "0/4";
                txtSelected.setText(titleText);
                return;
            }
            if (selectCount >= 4) {
                ToastToolUtils.showLong("最多选择4张图片");
                return;
            }
            mAdapter.setCheck(position, true, view);
            selectCount++;
            String titleText = selectCount > 0 ? selectCount + "/4" : "0/4";
            txtSelected.setText(titleText);
        }
    };

    private AdapterView.OnItemClickListener llBackOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            MultiPictureControl.this.dismiss();
        }
    };

    private View.OnClickListener btnOKOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null == mOnSelectedListener) {
                return;
            }
            mOnSelectedListener.onSelected(mAdapter.getSelectedList());
        }
    };

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.mOnSelectedListener = onSelectedListener;
    }

    public void showWindow(View parentView) {
        this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }
}