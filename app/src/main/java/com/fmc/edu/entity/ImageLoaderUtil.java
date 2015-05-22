package com.fmc.edu.entity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;

import com.fmc.edu.R;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Candy on 2015/5/21.
 */
public class ImageLoaderUtil {
    public static ImageLoader initCacheImageLoader(Context context) {
        String CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "imageLoader/cache";
        new File(CACHE_DIR).mkdirs();

        File cacheDir = StorageUtils.getOwnCacheDirectory(context, CACHE_DIR);
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pic_default)
                .showImageForEmptyUri(R.mipmap.pic_default)
                .showImageOnFail(R.mipmap.pic_default)
                .cacheOnDisk(true)
                .cacheInMemory(true)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();//构建完成

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options)
                .threadPoolSize(3)
                .diskCacheFileCount(100)
                .diskCacheExtraOptions(300, 300, null)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSizePercentage(75) // defaultF
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .memoryCache(new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        return imageLoader;
    }

    public static ImageLoader initTitleImageLoader(Context context) {
        String CACHE_DIR = Environment.getExternalStorageDirectory().getAbsolutePath() + "imageLoader/cache";
        new File(CACHE_DIR).mkdirs();

        File cacheDir = StorageUtils.getOwnCacheDirectory(context, CACHE_DIR);
        DisplayImageOptions options;
        options = new DisplayImageOptions.Builder()
                .showImageOnLoading(R.mipmap.pic_defalut_long)
                .showImageForEmptyUri(R.mipmap.pic_defalut_long)
                .showImageOnFail(R.mipmap.pic_defalut_long)
                .cacheOnDisk(true)
                .cacheInMemory(false)
                .imageScaleType(ImageScaleType.IN_SAMPLE_INT)//设置图片以如何的编码方式显示
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();//构建完成

        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(context)
                .defaultDisplayImageOptions(options)
                .threadPoolSize(3)
                .diskCacheFileCount(100)
                .diskCacheExtraOptions(480, 400, null)
                .denyCacheImageMultipleSizesInMemory()
                .memoryCacheSizePercentage(75) // defaultF
                .diskCache(new UnlimitedDiscCache(cacheDir))
                .memoryCache(new WeakMemoryCache());

        ImageLoaderConfiguration config = builder.build();
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        return imageLoader;
    }

    public static void clearImageLoaderCache(ImageLoader imageLoader) {
        imageLoader.clearDiskCache();
        imageLoader.clearMemoryCache();
    }

}
