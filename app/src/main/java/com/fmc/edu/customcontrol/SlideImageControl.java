package com.fmc.edu.customcontrol;

import android.content.Context;
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

/**
 * Created by Candy on 2015/5/10.
 */
public class SlideImageControl extends LinearLayout {
    private List<String> mImageUrls;
    private ViewPager mViewPager;
    private LinearLayout dotsLayout;
    private Context mContext;
    private OnSlideItemClickListener mOnSlideItemClickListener;

    public interface OnSlideItemClickListener {
        void onSlideItemClick();
    }

    public SlideImageControl(Context context, AttributeSet attrs) {
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

    public void setPageData(List<String> imageUrls, int selectIndex) {
        mImageUrls = imageUrls;
        ViewPagerAdapter adapter = new ViewPagerAdapter(mContext, createImageView(imageUrls));
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(selectIndex);
        mViewPager.setOnPageChangeListener(mViewPagerChangeListener);
        refreshDotsLayout(selectIndex);
    }

    public void setOnSlideItemClickListener(OnSlideItemClickListener onSlideItemClickListener) {
        mOnSlideItemClickListener = onSlideItemClickListener;
    }

    private List<ImageView> createImageView(List<String> pictureUrls) {
        List<ImageView> list = new ArrayList<ImageView>();
        for (int i = 0; i < pictureUrls.size(); i++) {
            ImageView imgView = new ImageView(mContext);
            ViewPager.LayoutParams param = new ViewPager.LayoutParams();
            param.width = ViewPager.LayoutParams.MATCH_PARENT;
            param.height = ViewPager.LayoutParams.WRAP_CONTENT;
            imgView.setScaleType(ImageView.ScaleType.FIT_CENTER);
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
            if (null == mOnSlideItemClickListener) {
                return;
            }
            mOnSlideItemClickListener.onSlideItemClick();
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

    // * 刷新标签元素布局，每次currentItemIndex值改变的时候都应该进行刷新。
    private void refreshDotsLayout(int position) {
        dotsLayout.removeAllViews();
        for (int i = 0; i < mImageUrls.size(); i++) {
            LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
            linearParams.weight = 1;
            linearParams.setMargins(3, 0, 3, 0);
            RelativeLayout relativeLayout = new RelativeLayout(getContext());
            ImageView image = new ImageView(getContext());
            if (i == position) {
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

}
