package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;

import com.fmc.edu.adapter.KidsSchoolAdapter;
import com.fmc.edu.common.Constant;
import com.fmc.edu.common.CrashHandler;
import com.fmc.edu.customcontrol.AutoSlidePictureControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.entity.DynamicItemEntity;
import com.fmc.edu.entity.KidSchoolEntity;
import com.fmc.edu.enums.DynamicTypeEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KidSchoolActivity extends Activity {

    private KidsSchoolAdapter mAdapter;
    private SlideListView list;
    private AutoSlidePictureControl slideImg;
    private List<DynamicItemEntity> mList;
    private ProgressControl mProgressControl;
    private String mHostUrl;
    private int mPageIndex = 1;
    private boolean mIsLastPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this);
        setContentView(R.layout.activity_kid_school);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        mProgressControl = new ProgressControl(this);
        mHostUrl = AppConfigUtils.getServiceHost();
        Bundle bundle = getIntent().getExtras();
        mList = (List<DynamicItemEntity>) bundle.getSerializable("list");
        mIsLastPage = bundle.getBoolean("isLastPage", false);
        initViews();
        initViewEvent();
        initPageData();
        initSlidePicture();
    }

    private void initViews() {
        list = (SlideListView) findViewById(R.id.kid_school_list);
        slideImg = (AutoSlidePictureControl) findViewById(R.id.kid_school_slide_img);
    }

    private void initViewEvent() {
        list.setOnLoadMoreListener(slideLoadedMoreListener);
    }

    private void initPageData() {
        mPageIndex = 1;
        mAdapter = new KidsSchoolAdapter(this, mList);
        list.setAdapter(mAdapter);
    }

    private void initSlidePicture() {
        String url = mHostUrl + "news/requestSlides";
        MyIon.httpPost(KidSchoolActivity.this, url, new HashMap<String, Object>(), null, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                if (null == data.get("slideList")) {
                    return;
                }
                List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("slideList");
                slideImg.setPageData(getPictureUrls(list));
            }
        });
    }

    private List<String> getPictureUrls(List<Map<String, Object>> list) {
        List<String> pictureUrls = new ArrayList<String>();
        for (int i = 0; i < list.size(); i++) {
            Map<String, Object> item = list.get(i);
            String imgUrl = mHostUrl + ConvertUtils.getString(item.get("imageUrl"));
            pictureUrls.add(imgUrl);
        }
        return pictureUrls;
    }

    private SlideListView.OnLoadMoreListener slideLoadedMoreListener = new SlideListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore(View footerView) {
            if (mIsLastPage) {
                return;
            }
            mPageIndex++;
            getDynamicData();
        }
    };

    private void getDynamicData() {
        String url = mHostUrl + "news/requestNewsList";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pageIndex", mPageIndex);
        params.put("pageSize", Constant.PAGE_SIZE);
        params.put("userId", FmcApplication.getLoginUser().userId);
        params.put("type", DynamicTypeEnum.getValue(DynamicTypeEnum.ClassDynamic));
        MyIon.httpPost(KidSchoolActivity.this, url, params, null, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                if (null == data.get("newsList")) {
                    return;
                }
                mIsLastPage = ConvertUtils.getBoolean(data.get("isLastPage"));
                afterGetDynamic(DynamicItemEntity.toDynamicItemEntity((List<Map<String, Object>>) data.get("newsList")));
            }
        });
    }

    private void afterGetDynamic(List<DynamicItemEntity> list) {
        if (mPageIndex == 1) {
            mAdapter.addAllItems(list, true);
            return;
        }
        mAdapter.addAllItems(list, false);
    }


    private ImageView createImageView(String url) {
        ImageView imgView = new ImageView(KidSchoolActivity.this);
        ViewPager.LayoutParams param = new ViewPager.LayoutParams();
        param.width = ViewPager.LayoutParams.MATCH_PARENT;
        param.height = ViewPager.LayoutParams.MATCH_PARENT;
        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
        imgView.setLayoutParams(param);
        ImageLoaderUtil.initTitleImageLoader(this).displayImage(url, imgView);
        return imgView;
    }

    private List<KidSchoolEntity> getDynamicList() {
        //TODO 调用接口获取数据
        List<KidSchoolEntity> list = new ArrayList<>();
        KidSchoolEntity item = new KidSchoolEntity();
        item.content = "测试呃逆荣";
        item.title = "测试标题";
        item.date = "2015-05-10";
        list.add(item);
        return list;
    }
}
