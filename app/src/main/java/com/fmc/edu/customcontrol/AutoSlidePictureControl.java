package com.fmc.edu.customcontrol;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fmc.edu.R;
import com.fmc.edu.adapter.ViewPagerAdapter;
import com.fmc.edu.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Candy on 2015-05-22.
 */
public class AutoSlidePictureControl extends LinearLayout {
    private List<String> mImageUrls;
    private int currentItem; //当前页面
    private ScheduledExecutorService scheduledExecutorService;
    private ViewPager mViewPager;
    private LinearLayout dotsLayout;
    private Context mContext;

    private OnSelectedListener mOnSelectedListener;

    public interface OnSelectedListener {
        void onSelected(int position);
    }

    public AutoSlidePictureControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initControlViews();
    }


    private void initControlViews() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.control_auto_slide_picture, null);
        mViewPager = (ViewPager) view.findViewById(R.id.auto_slide_picture_view_pager);
        dotsLayout = (LinearLayout) view.findViewById(R.id.auto_slide_picture_dots);
        this.addView(view);
    }

    public void setPageData(List<String> imageUrls) {
        mImageUrls = imageUrls;
        ViewPagerAdapter adapter = new ViewPagerAdapter(mContext, createImageView(imageUrls));
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(mViewPagerChangeListener);
        onStartPlay();
    }

    public void setOnSelectedListener(OnSelectedListener onSelectedListener) {
        this.mOnSelectedListener = onSelectedListener;
    }

    private List<ImageView> createImageView(List<String> pictureUrls) {
        List<ImageView> list = new ArrayList<ImageView>();
        for (int i = 0; i < pictureUrls.size(); i++) {
            ImageView imgView = new ImageView(mContext);
            ViewPager.LayoutParams param = new ViewPager.LayoutParams();
            param.width = ViewPager.LayoutParams.MATCH_PARENT;
            param.height = ViewPager.LayoutParams.MATCH_PARENT;
            imgView.setScaleType(ImageView.ScaleType.FIT_XY);
            imgView.setLayoutParams(param);
            ImageLoaderUtil.initTitleImageLoader(mContext).displayImage(pictureUrls.get(i), imgView);
            imgView.setOnClickListener(imgViewClickListener);
            list.add(imgView);
        }
        return list;
    }

    private OnClickListener imgViewClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

            if (null == mOnSelectedListener) {
                return;
            }
            mOnSelectedListener.onSelected(currentItem);
        }
    };

    private ViewPager.OnPageChangeListener mViewPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            refreshDotsLayout(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void onStartPlay() {
        refreshDotsLayout(0);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        //每隔10秒钟切换一张图片
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 10, 10, TimeUnit.SECONDS);
    }

    private class ViewPagerTask implements Runnable {

        @Override
        public void run() {
            currentItem = (currentItem + 1) % mImageUrls.size();
            handler.sendEmptyMessage(0);
            handler.obtainMessage().sendToTarget();
        }
    }

    // * 刷新标签元素布局，每次currentItemIndex值改变的时候都应该进行刷新。
    private void refreshDotsLayout(int position) {
        dotsLayout.removeAllViews();
        for (int i = 0; i < mImageUrls.size(); i++) {
            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            linearParams.weight = 1;
            linearParams.setMargins(3, 0, 3, 0);
            RelativeLayout relativeLayout = new RelativeLayout(getContext());
            ImageView image = new ImageView(getContext());
//            ViewGroup.LayoutParams imgParmam = image.getLayoutParams();
//            imgParmam.height=15;
//            imgParmam.width=15;
//            image.setScaleType(ImageView.ScaleType.FIT_XY);
//            image.setLayoutParams(imgParmam);
            if (i == position) {
                image.setBackgroundResource(R.mipmap.select);
            } else {
                image.setBackgroundResource(R.mipmap.unselect);
            }
            RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(15,15);
            relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            relativeLayout.addView(image, relativeParams);
            dotsLayout.addView(relativeLayout, linearParams);
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mViewPager.setCurrentItem(currentItem);
        }
    };
}
