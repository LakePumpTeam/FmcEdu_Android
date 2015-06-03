package com.fmc.edu;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.enums.DynamicTypeEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ImageLoaderUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class DynamicDetailActivity extends BaseActivity {
    private TopBarControl topBar;
    private TextView txtTitle;
    private TextView txtContent;
    private TextView txtDetailType;
    private TextView txtDate;
    private LinearLayout llPicture;
    private int mLike = 0;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_dynamic_detail);
        mBundle = getIntent().getExtras();
        initViews();
        initViewEvent();
        initPageData();
    }

    private void initViews() {
        topBar = (TopBarControl) findViewById(R.id.dynamic_detail_top_bar);
        txtDetailType = (TextView) findViewById(R.id.dynamic_detail_txt_detail_type);
        txtTitle = (TextView) findViewById(R.id.dynamic_detail_txt_title);
        txtContent = (TextView) findViewById(R.id.dynamic_detail_txt_content);
        txtDate = (TextView) findViewById(R.id.dynamic_detail_txt_date);
        llPicture = (LinearLayout) findViewById(R.id.dynamic_detail_ll_picture);
    }

    private void initViewEvent() {
        topBar.setOnOperateOnClickListener(topOnOperateOnClickListener);
    }


    private TopBarControl.OnOperateOnClickListener topOnOperateOnClickListener = new TopBarControl.OnOperateOnClickListener() {
        @Override
        public void onBackClick(View view) {
            backKidSchoolList();
        }

        @Override
        public void onOperateClick(View v) {
            Map<String, Object> params = new HashMap<String, Object>();
            params.put("newsId", mBundle.getInt("newsId"));
            params.put("userId", FmcApplication.getLoginUser().userId);
            params.put("isLike", !ConvertUtils.getBoolean(topBar.getTag()));
            MyIon.httpPost(DynamicDetailActivity.this, "news/likeNews", params, null, new MyIon.AfterCallBack() {
                @Override
                public void afterCallBack(Map<String, Object> data) {
                    if (ConvertUtils.getBoolean(topBar.getTag())) {
                        mLike--;
                        topBar.setTag(false);
                        topBar.setTopBarOperateImg(R.mipmap.btn_like_un_select);
                    } else {
                        mLike++;
                        topBar.setTag(true);
                        topBar.setTopBarOperateImg(R.mipmap.btn_like_selected);
                    }
                }
            });
        }
    };

    @Override
    public void onBackPressed() {
        backKidSchoolList();
        this.finish();
    }

    private void backKidSchoolList() {
        if (DynamicTypeEnum.getEnumValue(mBundle.getInt("type", 2)) != DynamicTypeEnum.KidSchool) {
            return;
        }
        Intent intent = new Intent();
        intent.putExtra("newsId", mBundle.getInt("newsId"));
        intent.putExtra("like", mLike);
        setResult(RESULT_OK, intent);

    }

    private void initPageData() {
        if (null == mBundle) {
            return;
        }
        bindDynamicType(mBundle.getInt("type"), mBundle.getBoolean("liked"));
        mLike = mBundle.getInt("like", 0);
        txtTitle.setText(mBundle.getString("subject"));
        txtContent.setText(mBundle.getString("content"));
        txtDate.setText(mBundle.getString("createDate"));
        bindPicture(mBundle.getStringArrayList("imageUrl"));
    }

    private void bindDynamicType(int dynamicType, boolean liked) {
        switch (DynamicTypeEnum.getEnumValue(dynamicType)) {
            case SchoolActivity:
                topBar.setTopOperateImgVisible(false);
                setTitleAndDetailType("校园活动");
                break;
            case SchoolNews:
                topBar.setTopOperateImgVisible(false);
                setTitleAndDetailType("校园新闻");
                break;
            case KidSchool:
                topBar.setTag(liked);
                topBar.setTopBarOperateImg(liked ? R.mipmap.btn_like_selected : R.mipmap.btn_like_un_select);
                topBar.setTopOperateImgVisible(true);
                setTitleAndDetailType("育儿学堂");
                break;
            default:
                setTitleAndDetailType("校园活动");
                break;
        }
    }

    private void setTitleAndDetailType(String title) {
        txtDetailType.setText(title);
        topBar.setTopBarText(title);
    }

    private void bindPicture(ArrayList<String> imageUrls) {
        for (int i = 0; i < imageUrls.size(); i++) {
            ImageView imageView = (ImageView) LayoutInflater.from(this).inflate(R.layout.item_single_picture, null);
            DisplayMetrics displayMetrics = new DisplayMetrics();

            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels - 20;
            imageView.setMaxWidth(screenWidth);
            imageView.setMaxHeight(screenWidth * 5);//这里其实可以根据需求而定，我这里测试为最大宽度的5倍
            ImageLoaderUtil.initCacheImageLoader(this).displayImage(AppConfigUtils.getServiceHost() + imageUrls.get(i), imageView);
            llPicture.addView(imageView);
        }
    }
}
