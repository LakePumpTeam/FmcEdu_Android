package com.fmc.edu.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.fmc.edu.common.Constant;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Candy on 2015-05-21.
 */
public class ImageFactoryUtils {


    public static String saveCroppedImage(Bitmap bmp, String fileName) {
        File file = new File(Constant.TEMP_DIR);
        if (!file.exists())
            file.mkdir();

        String tempPath = Constant.TEMP_DIR + "/" + fileName;
        file = new File(tempPath);
        try {
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return tempPath;

    }

    public static byte[] compressImage(Bitmap image) {

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 50, outputStream);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到outputStream中
        int options = 100;
        while (outputStream.toByteArray().length / 1024 > 100) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            outputStream.reset();
            image.compress(Bitmap.CompressFormat.JPEG, options, outputStream);//这里压缩options%，把压缩后的数据存放到outputStream中
            options -= 10;//每次都减少10
        }
        return outputStream.toByteArray();
    }

    public static String getThumbnailImage(String srcPath) {
        File file = new File(srcPath);
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为
        float hh = 800f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return saveCroppedImage(bitmap, file.getName());//压缩好比例大小后再进行质量压缩
    }
}
