package com.fmc.edu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.fmc.edu.R;
import com.fmc.edu.entity.ImageItemEntity;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

/**
 * Created by Candy on 2015/5/21.
 */
public class DynamicItemGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<ImageItemEntity> mItems;
    private ImageLoader mImageLoader;

    public DynamicItemGridAdapter(Context context, List<ImageItemEntity> items, ImageLoader imageLoader) {
        mContext = context;
        mItems = items;
        mImageLoader = imageLoader;
    }

    @Override
    public int getCount() {
        if (null == mItems) {
            return 0;
        }
        return mItems.size();
    }

    @Override
    public Object getItem(int position) {
        if (null == mItems) {
            return null;
        }
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mItems) {
            return convertView;
        }
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_publish_dynamic_grid, null);
        }
        ImageItemEntity imageItemEntity = mItems.get(position);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.item_publish_dynamic_img);
        convertView.setTag(imageItemEntity.origUrl);
        mImageLoader.displayImage(imageItemEntity.thumbUrl, imageView,
                new SimpleImageLoadingListener() {
                    @Override
                    public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                        super.onLoadingComplete(imageUri, view, loadedImage);
                    }

                    @Override
                    public void onLoadingStarted(String imageUri, View view) {
                        DisplayMetrics dm = new DisplayMetrics();
                        ((WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(dm);
                        ViewGroup.LayoutParams params = imageView.getLayoutParams();
                        params.height = dm.widthPixels / 4 - 10;
                        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                        super.onLoadingStarted(imageUri, view);
                    }
                });
        return convertView;
    }
}
