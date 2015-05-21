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
import com.fmc.edu.utils.StringUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

/**
 * Created by Candy on 2015-05-20.
 */
public class PublishDynamicGridAdapter extends BaseAdapter {
    private Context mContext;
    private List<ImageItemEntity> mItems;
    private ImageLoader mImageLoader;

    public PublishDynamicGridAdapter(Context context, List<ImageItemEntity> items, ImageLoader imageLoader) {
        mItems = items;
        mContext = context;
        mImageLoader = imageLoader;
    }

    public List<ImageItemEntity> getItems() {
        return mItems;
    }

    public void addAll(List<ImageItemEntity> list) {
        mItems = list;
        notifyDataSetChanged();
    }

    public void removeItem(int position) {
        if (null == mItems) {
            return;
        }
        mItems.remove(position);
        notifyDataSetChanged();
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

    public boolean isHavePicture(int position) {
        if (null == mItems) {
            return false;
        }
        if (mItems.size() < position) {
            return false;
        }
        if (StringUtils.isEmptyOrNull(mItems.get(position).imageURL)) {
            return false;
        }
        return true;
    }

    public String getImageUrl(int position) {
        if (null == mItems) {
            return "";
        }
        if (null == mItems.get(position)) {
            return "";
        }
        return mItems.get(position).imageURL;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mItems || 0 == mItems.size()) {
            return convertView;
        }
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_publish_dynamic_grid, null);
        }
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.item_publish_dynamic_img);
        final ImageItemEntity item = mItems.get(position);
        mImageLoader.displayImage("file://" + item.imageURL, imageView,
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
