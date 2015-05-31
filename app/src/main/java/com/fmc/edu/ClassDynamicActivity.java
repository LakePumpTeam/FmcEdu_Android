package com.fmc.edu;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.fmc.edu.adapter.ClassDynamicItemAdapter;
import com.fmc.edu.common.Constant;
import com.fmc.edu.customcontrol.ProgressControl;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.entity.CommentItemEntity;
import com.fmc.edu.entity.DynamicItemEntity;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.enums.DynamicTypeEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.AppConfigUtils;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClassDynamicActivity extends Activity {
    private SlideListView slideListView;
    private RelativeLayout rlComment;
    private EditText editComment;
    private Button btnComment;
    private ProgressControl mProgressControl;
    private String mHostUrl;
    private int mNewsId;
    private int mPositon;
    private ClassDynamicItemAdapter mAdapter;
    private List<DynamicItemEntity> mList;
    private int mPageIndex = 1;
    private boolean mIsLastPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this);
        setContentView(R.layout.activity_class_dynamic);
        mProgressControl = new ProgressControl(this);
        mHostUrl = AppConfigUtils.getServiceHost();
        Bundle bundle = getIntent().getExtras();
        mList = (List<DynamicItemEntity>) bundle.getSerializable("list");
        mIsLastPage = bundle.getBoolean("isLastPage", false);
        initViews();
        initViewEvent();
        initPageData();
    }

    private void initViews() {
        slideListView = (SlideListView) findViewById(R.id.class_dynamic_slide_list);
        rlComment = (RelativeLayout) findViewById(R.id.class_dynamic_rl_comment);
        editComment = (EditText) findViewById(R.id.class_dynamic_edit_comment);
        btnComment = (Button) findViewById(R.id.class_dynamic_btn_comment);
    }

    private void initViewEvent() {
        btnComment.setOnClickListener(btnCommentOnClickListener);
        slideListView.setOnLoadMoreListener(slideLoadedMoreListener);
        slideListView.setOnScrollPrepListener(slideOnScrollPrepListener);
    }

    private void initPageData() {
        mPageIndex = 1;
        mAdapter = new ClassDynamicItemAdapter(this, mList);
        slideListView.setAdapter(mAdapter);
    }

    private View.OnClickListener btnCommentOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doSendComment();
        }
    };

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

    private SlideListView.OnScrollPrepListener slideOnScrollPrepListener = new SlideListView.OnScrollPrepListener() {
        @Override
        public void onScrollPrep() {
            rlComment.setVisibility(View.GONE);
        }
    };

    private void getDynamicData() {
        String url = mHostUrl + "news/requestNewsList";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pageIndex", mPageIndex);
        params.put("pageSize", Constant.PAGE_SIZE);
        params.put("userId", FmcApplication.getLoginUser().userId);
        params.put("type", DynamicTypeEnum.getValue(DynamicTypeEnum.ClassDynamic));
        MyIon.httpPost(ClassDynamicActivity.this, url, params, null, new MyIon.AfterCallBack() {
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

    private void doSendComment() {
        mProgressControl.showWindow(btnComment);
        String url = mHostUrl + "news/postComment";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("newsId", mNewsId);
        params.put("userId", FmcApplication.getLoginUser().userId);
        params.put("content", editComment.getText());
        MyIon.httpPost(this, url, params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                ToastToolUtils.showShort("评论成功");
                CommentItemEntity commentItemEntity = new CommentItemEntity();
                LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
                commentItemEntity.userName = loginUserEntity.userName;
                commentItemEntity.userId = loginUserEntity.userId;
                commentItemEntity.comment = editComment.getText().toString();
                mAdapter.addComment(commentItemEntity, mPositon);
                editComment.setText("");
                rlComment.setVisibility(View.GONE);
            }
        });
    }

    public void setCommentVisible(int newsId, int position) {
        mNewsId = newsId;
        mPositon = position;
        rlComment.setVisibility(View.VISIBLE);
    }
}
