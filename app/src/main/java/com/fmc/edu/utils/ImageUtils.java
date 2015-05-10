package com.fmc.edu.utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

import com.fmc.edu.FmcApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Candy on 2015/5/10.
 */
public class ImageUtils {
    private static byte[] getDefaultIcon(ImageView imageView) {

        // iconView.get
        // BitmapFactory.
        byte[] compressData = null;

        imageView.setDrawingCacheEnabled(true);

        Bitmap bmp = Bitmap.createBitmap(imageView.getDrawingCache());

        imageView.setDrawingCacheEnabled(false);

        if (bmp != null) {
            compressData = getByteByBitmap(bmp);
        }

        return compressData;
    }

    private static byte[] getByteByBitmap(Bitmap bmp) {
        byte[] compressData = null;
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        bmp.compress(Bitmap.CompressFormat.JPEG, 100, outStream);

        // bmp.
        compressData = outStream.toByteArray();

        try {
            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return compressData;
    }

    public static String saveBitmap(String path, String fileName, Bitmap bitmap) {
        checkDirectoryAndCreate(path);
        File f = new File(path, fileName);
        String filePath = f.getAbsolutePath();
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return filePath;
    }

    public static Intent getSelectPhoto() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 200);
        intent.putExtra("outputY", 200);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputFormat", "JPEG"); //输入文件格式
        intent.putExtra("return-data", true);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        return intent;
    }

    public static void checkDirectoryAndCreate(String path) {
        File file = new File(path);
        if (file.exists()) {
            return;
        }
        file.mkdirs();
    }

    public static BitmapDrawable getBitmapDrawable(Bitmap bitmap) {
        return new BitmapDrawable(FmcApplication.getApplication().getResources(), bitmap);
    }
}