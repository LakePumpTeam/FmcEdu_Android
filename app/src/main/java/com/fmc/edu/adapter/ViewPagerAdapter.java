package com.fmc.edu.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fmc.edu.utils.ImageLoaderUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.util.List;

/**
 * Created by Candy on 2015/5/22.
 */
public class ViewPagerAdapter extends PagerAdapter {
    private Context mContext;
    private List<String> mImgUrls;

    public ViewPagerAdapter(Context context, List<String> imgUrls) {
        mContext = context;
        mImgUrls = imgUrls;
    }

    @Override
    public int getCount() {
        return mImgUrls.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(mContext);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        ImageLoader imageLoader = ImageLoaderUtil.initCacheImageLoader(mContext);
        imageLoader.displayImage(mImgUrls.get(position), imageView, new SimpleImageLoadingListener() {
        });
        container.addView(imageView);
        return imageView;
    }
}
