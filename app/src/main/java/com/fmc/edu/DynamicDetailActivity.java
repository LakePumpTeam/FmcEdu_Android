package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.fmc.edu.adapter.DynamicCommentDetailAdapter;
import com.fmc.edu.adapter.SinglePictureAdapter;
import com.fmc.edu.common.CrashHandler;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.CommentItemEntity;
import com.fmc.edu.enums.DynamicTypeEnum;

import java.util.ArrayList;
import java.util.List;


public class DynamicDetailActivity extends Activity {
    private TopBarControl topBar;
    private TextView txtTitle;
    private TextView txtContent;
    private TextView txtDetailType;
    private TextView txtDate;
    private GridView gridPicture;
    private ListView listComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this);
        setContentView(R.layout.activity_dynamic_detail);
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(this);
        initViews();
        initPageData();
    }

    private void initViews() {
        topBar = (TopBarControl) findViewById(R.id.dynamic_detail_top_bar);
        txtDetailType = (TextView) findViewById(R.id.dynamic_detail_txt_detail_type);
        txtTitle = (TextView) findViewById(R.id.dynamic_detail_txt_title);
        txtContent = (TextView) findViewById(R.id.dynamic_detail_txt_content);
        txtDate = (TextView) findViewById(R.id.dynamic_detail_txt_date);
        gridPicture = (GridView) findViewById(R.id.dynamic_detail_grid_picture);
        listComment = (ListView) findViewById(R.id.dynamic_detail_list_comment);
    }

    private void initPageData() {
        Bundle bundle = getIntent().getExtras();
        if (null == bundle) {
            return;
        }
        bindDynamicType(bundle.getInt("type"));
        txtTitle.setText(bundle.getString("subject"));
        txtContent.setText(bundle.getString("content"));
        txtDate.setText(bundle.getString("createDate"));
        bindPicture(bundle.getStringArrayList("imageUrl"));
        List<CommentItemEntity> commentList = bundle.getParcelableArrayList("commentList");
        bindCommentList(commentList);
    }

    private void bindDynamicType(int dynamicType) {
        switch (DynamicTypeEnum.getEnumValue(dynamicType)) {
            case SchoolActivity:
                setTitleAndDetailType("校园活动");
                break;
            case SchoolNews:
                setTitleAndDetailType("校园新闻");
                break;
            case SchoolNotice:
                setTitleAndDetailType("校园通知");
                break;
            case ClassDynamic:
                setTitleAndDetailType("班级动态");
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
        SinglePictureAdapter singlePictureAdapter = new SinglePictureAdapter(this, imageUrls);
        gridPicture.setAdapter(singlePictureAdapter);
    }

    private void bindCommentList(List<CommentItemEntity> list) {
        DynamicCommentDetailAdapter dynamicItemAdapter = new DynamicCommentDetailAdapter(this, list);
        listComment.setAdapter(dynamicItemAdapter);
    }
}
