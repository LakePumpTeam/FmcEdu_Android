package com.fmc.edu.customcontrol;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fmc.edu.R;
import com.fmc.edu.adapter.ViewPagerAdapter;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by Candy on 2015-05-22.
 */
public class AutoSlidePictureControl extends LinearLayout {

    private List<String> mImageUrls;
    private int oldPosition = 0;//记录上一次点的位置
    private int currentItem; //当前页面
    private ScheduledExecutorService scheduledExecutorService;
    private ViewPager mViewPager;
    private LinearLayout dotsLayout;
    private Context mContext;

    public AutoSlidePictureControl(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        initControlViews();
    }

    private void initControlViews() {
        View view = LayoutInflater.from(mContext).inflate(R.layout.control_auto_slide_picture, null);
        mViewPager = (ViewPager) view.findViewById(R.id.auto_slide_picture_view_pager);
        dotsLayout = (LinearLayout) view.findViewById(R.id.auto_slide_picture_dots);
    }

    public void setPageDatas(List<String> imageUrls) {
        mImageUrls = imageUrls;
        ViewPagerAdapter adapter = new ViewPagerAdapter(mContext, mImageUrls);
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(mViewPagerChangeListener);
    }

    private ViewPager.OnPageChangeListener mViewPagerChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public void onStartPlay() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();

        //每隔2秒钟切换一张图片
        scheduledExecutorService.scheduleWithFixedDelay(new ViewPagerTask(), 2, 2, TimeUnit.SECONDS);
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
    private void refreshDotsLayout() {
        dotsLayout.removeAllViews();
        for (int i = 0; i < mImageUrls.size(); i++) {
            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(0, RelativeLayout.LayoutParams.FILL_PARENT);
            linearParams.weight = 1;
            linearParams.setMargins(3, 0, 3, 0);
            RelativeLayout relativeLayout = new RelativeLayout(getContext());
            ImageView image = new ImageView(getContext());
            if (i == currentItem) {
                image.setBackgroundResource(R.mipmap.select);
            } else {
                image.setBackgroundResource(R.mipmap.unselect);
            }
            RelativeLayout.LayoutParams relativeParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            relativeParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            relativeLayout.addView(image, relativeParams);
            dotsLayout.addView(relativeLayout, linearParams);
        }
    }

    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            //设置当前页面
            mViewPager.setCurrentItem(currentItem);
            refreshDotsLayout();
        }
    };
}
