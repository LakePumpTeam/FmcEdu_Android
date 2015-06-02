package com.fmc.edu;

import android.app.Activity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ImageLoaderUtil;

import java.util.ArrayList;


public class CampusDetailActivity extends Activity {
    private Button btnSubmit;
    private LinearLayout llPicture;
    private RadioGroup rgSuggest;
    private TextView txtTitle;
    private TextView txtContent;
    private TextView txtDate;
    private TextView txtPartIn;
    private TopBarControl topBar;
    private Bundle mBundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_campus_detail);
        mBundle = getIntent().getExtras();
        initView();
        initViewEvent();
        initPageData();
    }

    private void initView() {
        btnSubmit = (Button) findViewById(R.id.campus_detail_btn_submit);
        llPicture = (LinearLayout) findViewById(R.id.campus_detail_ll_picture);
        rgSuggest = (RadioGroup) findViewById(R.id.campus_detail_rg_suggest);
        txtTitle = (TextView) findViewById(R.id.campus_detail_txt_title);
        txtContent = (TextView) findViewById(R.id.campus_detail_txt_content);
        txtDate = (TextView) findViewById(R.id.campus_detail_txt_date);
        txtPartIn = (TextView) findViewById(R.id.campus_detail_txt_part_in);
        topBar = (TopBarControl) findViewById(R.id.campus_detail_top_bar);
    }

    private void initViewEvent() {
        btnSubmit.setOnClickListener(btnSubmitOnClickListener);
    }

    private void initPageData() {
        if (null == mBundle) {
            return;
        }
        String subject = mBundle.getString("subject");
        String title = subject.length() > 8 ? subject.substring(0, 8) : subject;
        topBar.setTopBarText(title);
        txtTitle.setText(subject);
        txtContent.setText(mBundle.getString("content"));
        txtDate.setText(mBundle.getString("date"));
        txtPartIn.setText(mBundle.getString("partincount", "0"));
        partInComment(!mBundle.getBoolean("partIned"));
        bindPicture(mBundle.getStringArrayList("imageUrl"));
    }

    private View.OnClickListener btnSubmitOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            partInComment(false);
        }
    };

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

    private void partInComment(boolean isEnable) {
        rgSuggest.setEnabled(isEnable);
        btnSubmit.setEnabled(isEnable);
    }
}
