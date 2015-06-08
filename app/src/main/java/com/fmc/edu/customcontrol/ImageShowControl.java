package com.fmc.edu.customcontrol;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.List;

/**
 * Created by Candy on 2015-05-19.
 */
public class ImageShowControl extends PopupWindow {
    private Context mContext;
    private DisplayMetrics mDisplayMetrics;

    private SlideImageControl imgBigPicture;

    public ImageShowControl(Context context) {
        super(context, null);
        this.mContext = context;
        mDisplayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
        initPopWindow();
        initContentView();
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
        linearLayout.setBackgroundColor(Color.parseColor("#ff333333"));
        imgBigPicture = new SlideImageControl(mContext, null);
        LinearLayout.LayoutParams slideImgParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        imgBigPicture.setLayoutParams(slideImgParams);
        linearLayout.addView(imgBigPicture);
        imgBigPicture.setOnSlideItemClickListener(onSlideItemClickListener);
        this.setContentView(linearLayout);
    }

    public void showWindow(View parentView, List<String> url,int selectIndex) {
        imgBigPicture.setPageData(url,selectIndex);
        this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
    }

    private SlideImageControl.OnSlideItemClickListener onSlideItemClickListener=new SlideImageControl.OnSlideItemClickListener() {
        @Override
        public void onSlideItemClick() {
            ImageShowControl.this.dismiss();
        }
    };
}
