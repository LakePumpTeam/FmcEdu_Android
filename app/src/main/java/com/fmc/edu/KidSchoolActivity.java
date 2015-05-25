package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.fmc.edu.adapter.KidsSchoolAdapter;
import com.fmc.edu.common.Constant;
import com.fmc.edu.common.CrashHandler;
import com.fmc.edu.customcontrol.AutoSlidePictureControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.entity.CommentItemEntity;
import com.fmc.edu.entity.DynamicItemEntity;
import com.fmc.edu.enums.DynamicTypeEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.io.Serializable;
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
    private List<Map<String, Object>> mSlidePicture;

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
        slideImg.setOnSelectedListener(slideImgOnSelectedListener);
        list.setOnItemClickListener(listOnItemClickListener);
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
                mSlidePicture = (List<Map<String, Object>>) data.get("slideList");
                slideImg.setPageData(getPictureUrls());
            }
        });
    }

    private List<String> getPictureUrls() {
        List<String> pictureUrls = new ArrayList<String>();
        for (int i = 0; i < mSlidePicture.size(); i++) {
            Map<String, Object> item = mSlidePicture.get(i);
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

    private AutoSlidePictureControl.OnSelectedListener slideImgOnSelectedListener = new AutoSlidePictureControl.OnSelectedListener() {
        @Override
        public void onSelected(int position) {
            ToastToolUtils.showLong(position + "");
        }
    };

    private AdapterView.OnItemClickListener listOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            gotoDynamicDetailPage(view, mList.get(position).newsId);
        }
    };


    private void gotoDynamicDetailPage(View view, int newsId) {
        mProgressControl.showWindow(view);
        String url = AppConfigUtils.getServiceHost() + "news/requestNewsDetail";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("newsId", newsId);
        params.put("userId", FmcApplication.getLoginUser().userId);
        MyIon.httpPost(KidSchoolActivity.this, url, params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                Bundle bundle = new Bundle();
                bundle.putInt("newsId", ConvertUtils.getInteger(data.get("newsId")));
                bundle.putInt("like", ConvertUtils.getInteger(data.get("like")));
                bundle.putInt("type", DynamicTypeEnum.getValue(DynamicTypeEnum.KidSchool));
                bundle.putBoolean("liked", ConvertUtils.getBoolean(data.get("liked"), false));
                bundle.putString("subject", ConvertUtils.getString(data.get("subject")));
                bundle.putString("content", ConvertUtils.getString(data.get("content")));
                bundle.putStringArrayList("imageUrl", ConvertUtils.getStringList(data.get("imgs")));
                bundle.putString("createDate", ConvertUtils.getString(data.get("createDate")));
                List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("commentList");
                bundle.putSerializable("commentList", (Serializable) CommentItemEntity.toCommentEntityList(list));

                Intent intent = new Intent(KidSchoolActivity.this, DynamicDetailActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }


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

}
