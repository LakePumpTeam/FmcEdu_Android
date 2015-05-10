package com.fmc.edu.customcontrol;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.widget.ImageView;

/**
 * Created by Candy on 2015-05-08.
 */
public class CircleImageControl extends ImageView {

    Paint paint;
    private int radius = 80;


    public CircleImageControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        initImg(context, attrs);
    }

    public CircleImageControl(Context context) {
        super(context);
        initImg(context, null);
    }

    private void initImg(Context context, AttributeSet attrs) {
        paint = new Paint();

        if (null == attrs) {
            return;
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        Drawable drawable = getDrawable();
        Bitmap bitmap = MatirxBitMap(((BitmapDrawable) drawable).getBitmap());
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        RadialGradient gradient = new RadialGradient(width / 2, height / 2, width / 2,
                new int[]{0xffffffff, 0xffffffff, 0xffffffff},
                new float[]{0.f, 0.8f, 1.0f},
                Shader.TileMode.CLAMP);
        paint.setShader(gradient);

        Bitmap circleMap = toRoundBitmap(bitmap);
        canvas.drawBitmap(circleMap, 0, 0, paint);

//        paint.setColor(Color.WHITE);
//        paint.setAntiAlias(true);
//        DisplayMetrics dm = new DisplayMetrics();
//        dm = getResources().getDisplayMetrics();
//        double density = dm.density;
//        int strokeWidth = (int) (5 * density);
//        paint.setStrokeWidth(strokeWidth);
//        paint.setStyle(Paint.Style.STROKE);
//        int stockeRadius = (int) (width / 2 - 3 * density);
//        canvas.drawCircle(width / 2, width / 2, 0, paint);
    }

    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();

        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            left = 0;
            top = 0;
            right = width;
            bottom = width;
            height = width;

            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            float clip = (width - height) / 2;
            left = clip;
            top = 0;
            right = width - clip;
            bottom = height;
            width = height;

            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();

        final Rect src = new Rect((int) left, (int) top, (int) right, (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top, (int) dst_right, (int) dst_bottom);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(Color.WHITE);

        int r = (int) (right / 2);
        canvas.drawCircle(r, r, r - 4, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    private Bitmap MatirxBitMap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        DisplayMetrics dm = new DisplayMetrics();
        dm = getResources().getDisplayMetrics();
        double density = dm.density;
        int newWidth = (int) (radius * density);
        int newHeight = (int) (radius * density);

        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }
}