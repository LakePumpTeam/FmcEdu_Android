package com.fmc.edu;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.ListView;

import com.fmc.edu.adapter.KidsSchoolAdapter;
import com.fmc.edu.common.CrashHandler;
import com.fmc.edu.customcontrol.SlideImageControl;
import com.fmc.edu.entity.KidSchoolEntity;
import com.fmc.edu.utils.StringUtils;

import java.util.ArrayList;
import java.util.List;


public class KidSchoolActivity extends Activity {

    private KidsSchoolAdapter mAdapter;
    private ListView list;
    private SlideImageControl slideImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kid_school);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        initViews();
        initPageData();
    }

    private void initViews() {
        list = (ListView) findViewById(R.id.kid_school_list);
        slideImg = (SlideImageControl) findViewById(R.id.kid_school_slide_img);
    }

    private void initPageData() {
        mAdapter = new KidsSchoolAdapter(this, getDynamicList());
        list.setAdapter(mAdapter);
        slideImg.addItem(createImageView(null));
        slideImg.addItem(createImageView(null));
        slideImg.addItem(createImageView(null));
        slideImg.addItem(createImageView(null));


    }

    private ImageView createImageView(Object bitMapObj) {
        ImageView imgView = new ImageView(KidSchoolActivity.this);

        if (StringUtils.isEmptyOrNull(bitMapObj)) {
            imgView.setImageResource(R.mipmap.main_bg);
        } else {
            imgView.setImageBitmap((Bitmap) bitMapObj);
        }
//        imgView.setPadding(0, 10, 0, 10);
        ViewPager.LayoutParams param = new ViewPager.LayoutParams();
        param.width = ViewPager.LayoutParams.MATCH_PARENT;
        param.height = ViewPager.LayoutParams.MATCH_PARENT;
        imgView.setScaleType(ImageView.ScaleType.FIT_XY);
        imgView.setLayoutParams(param);
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
