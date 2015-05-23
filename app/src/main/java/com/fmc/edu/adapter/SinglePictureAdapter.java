package com.fmc.edu.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.fmc.edu.R;
import com.fmc.edu.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

/**
 * Created by Candy on 2015/5/23.
 */
public class SinglePictureAdapter extends FmcBaseAdapter<String> {
    public SinglePictureAdapter(Context context, List<String> items) {
        super(context, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == mItems) {
            return convertView;
        }
        if (null == convertView) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_publish_dynamic_grid, null);
        }
        String imageUrl = mItems.get(position);
        final ImageView imageView = (ImageView) convertView.findViewById(R.id.item_publish_dynamic_img);
        ImageLoader imageLoader = ImageLoaderUtil.initCacheImageLoader(mContext);
        imageLoader.displayImage(imageUrl, imageView,
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
