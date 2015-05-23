package com.fmc.edu.customcontrol;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.fmc.edu.R;
import com.fmc.edu.adapter.MultiPictureItemAdapter;
import com.fmc.edu.entity.ImageItemEntity;
import com.fmc.edu.utils.ImageLoaderUtil;
import com.fmc.edu.utils.ToastToolUtils;
import com.nostra13.universalimageloader.core.ImageLoader;

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
    private MultiPictureItemAdapter mAdapter;
    private List<ImageItemEntity> mSelectedList;
    private int mMaxCount = 4;

    public interface OnSelectedListener {

        void onSelected(List<ImageItemEntity> selectedImageList);
    }

    public MultiPictureControl(Context context, int maxCount, List<ImageItemEntity> selectedList) {
        super(context, null);
        this.mContext = context;
        this.mMaxCount = maxCount;
        this.mSelectedList = selectedList;
        mDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        initPopWindow();
        initContentView();
        mImageLoader = ImageLoaderUtil.initCacheImageLoader(context);
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
        grid.setSelector(new ColorDrawable(Color.TRANSPARENT));
        llBack = (LinearLayout) view.findViewById(R.id.multi_picture_ll_back);
        txtSelected = (TextView) view.findViewById(R.id.multi_picture_txt_selected);
        txtOk = (TextView) view.findViewById(R.id.multi_picture_txt_ok);

        txtOk.setOnClickListener(btnOKOnClickListener);
        llBack.setOnClickListener(llBackOnClickListener);
        linearLayout.addView(view);
        this.setContentView(linearLayout);
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
                    item.thumbUrl = imagecursor.getString(dataColumnIndex);
                    if (isSelected(item.thumbUrl)) {
                        item.isCheck = true;
                    }
                    galleryList.add(item);
                }
                imagecursor.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Collections.reverse(galleryList);
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
            MultiPictureControl.this.dismiss();
        }
    };

    private View.OnClickListener btnOKOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (null == mOnSelectedListener) {
                return;
            }
            MultiPictureControl.this.dismiss();
            clearCache();
            mOnSelectedListener.onSelected(mAdapter.getSelectedList());
        }
    };

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.mOnSelectedListener = onSelectedListener;
    }

    private void clearCache() {
        mImageLoader.clearMemoryCache();
        mImageLoader.clearDiskCache();
    }

    public void showWindow(View parentView) {
        this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }
}