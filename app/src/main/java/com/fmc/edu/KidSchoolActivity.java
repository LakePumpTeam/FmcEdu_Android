package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.fmc.edu.adapter.KidsSchoolAdapter;
import com.fmc.edu.common.Constant;
import com.fmc.edu.customcontrol.AutoSlidePictureControl;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.entity.CommentItemEntity;
import com.fmc.edu.entity.DynamicItemEntity;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.enums.DynamicTypeEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.RequestCodeUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class KidSchoolActivity extends BaseActivity {

    private KidsSchoolAdapter mAdapter;
    private SlideListView slideListView;
    private AutoSlidePictureControl slideImg;
    private List<DynamicItemEntity> mList;
    private int mPageIndex = 1;
    private boolean mIsLastPage;
    private List<Map<String, Object>> mSlidePicture;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_kid_school);
        Bundle bundle = getIntent().getExtras();
        mList = (List<DynamicItemEntity>) bundle.getSerializable("list");
        mIsLastPage = bundle.getBoolean("isLastPage", false);
        initViews();
        initViewEvent();
        initPageData();
        initSlidePicture();
    }

    private void initViews() {
        slideListView = (SlideListView) findViewById(R.id.kid_school_list);
        slideImg = (AutoSlidePictureControl) findViewById(R.id.kid_school_slide_img);
    }

    private void initViewEvent() {
        slideListView.setOnLoadMoreListener(slideLoadedMoreListener);
        slideImg.setOnSelectedListener(slideImgOnSelectedListener);
        slideListView.setOnItemClickListener(listOnItemClickListener);
    }

    private void initPageData() {
        mPageIndex = 1;
        mAdapter = new KidsSchoolAdapter(this, mList);
        slideListView.setAdapter(mAdapter);
    }

    private void initSlidePicture() {
        MyIon.httpPost(KidSchoolActivity.this, "news/requestSlides", new HashMap<String, Object>(), null, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                if (null == data.get("slideList")) {
                    return;
                }
                mSlidePicture = (List<Map<String, Object>>) data.get("slideList");
                slideImg.setPageData(mSlidePicture);
            }
        });
    }

    private SlideListView.OnLoadMoreListener slideLoadedMoreListener = new SlideListView.OnLoadMoreListener() {
        @Override
        public void onLoadMore(View footerView) {
            if (mIsLastPage) {
                return;
            }
            mPageIndex++;
            slideListView.setFooterViewVisible(true);
            getDynamicData();
        }
    };


    private AutoSlidePictureControl.OnSelectedListener slideImgOnSelectedListener = new AutoSlidePictureControl.OnSelectedListener() {
        @Override
        public void onSelected(int newsId) {
            gotoDynamicDetailPage(slideImg, ConvertUtils.getInteger(newsId, 0));
        }
    };

    private AdapterView.OnItemClickListener listOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            gotoDynamicDetailPage(view, mList.get(position).newsId);
        }
    };


    private void gotoDynamicDetailPage(View view, int newsId) {
        mProgressControl.showWindow();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("newsId", newsId);
        params.put("userId", FmcApplication.getLoginUser().userId);
        MyIon.httpPost(KidSchoolActivity.this, "news/requestNewsDetail", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                Bundle bundle = new Bundle();
                if (!data.containsKey("newsId")) {
                    ToastToolUtils.showLong("该详情不存在");
                    return;
                }
                bundle.putInt("newsId", ConvertUtils.getInteger(data.get("newsId")));
                bundle.putInt("like", ConvertUtils.getInteger(data.get("like")));
                bundle.putInt("type", DynamicTypeEnum.getValue(DynamicTypeEnum.KidSchool));
                bundle.putBoolean("liked", ConvertUtils.getBoolean(data.get("liked"), false));
                bundle.putString("subject", ConvertUtils.getString(data.get("subject")));
                bundle.putString("content", ConvertUtils.getString(data.get("content")));
                bundle.putStringArrayList("imageUrl", ConvertUtils.getStringList(data.get("imageUrls")));
                bundle.putString("createDate", ConvertUtils.getString(data.get("createDate")));
                List<Map<String, Object>> list = (List<Map<String, Object>>) data.get("commentList");
                bundle.putSerializable("commentList", (Serializable) CommentItemEntity.toCommentEntityList(list));

                Intent intent = new Intent(KidSchoolActivity.this, DynamicDetailActivity.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, RequestCodeUtils.KID_SCHOOL_DETAIL);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == RequestCodeUtils.KID_SCHOOL_DETAIL) {
            int newsId = data.getIntExtra("newsId", 0);
            int like = data.getIntExtra("like", 0);
            mAdapter.updateLikeByNewsId(newsId, like);
        }

    }

    private void getDynamicData() {
        mProgressControl.showWindow();
        Map<String, Object> params = new HashMap<String, Object>();
        LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
        params.put("pageIndex", mPageIndex);
        params.put("pageSize", Constant.PAGE_SIZE);
        params.put("userId", loginUserEntity.userId);
        params.put("type", DynamicTypeEnum.getValue(DynamicTypeEnum.ClassDynamic));
        params.put("classId", loginUserEntity.classId);
        MyIon.httpPost(KidSchoolActivity.this,  "news/requestNewsList", params, mProgressControl, new MyIon.AfterCallBack() {
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
        slideListView.setFooterViewVisible(false);
    }

}
