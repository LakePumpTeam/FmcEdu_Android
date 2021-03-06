package com.fmc.edu;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.fmc.edu.adapter.ClassDynamicItemAdapter;
import com.fmc.edu.common.Constant;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.customcontrol.TopBarControl;
import com.fmc.edu.entity.CommentItemEntity;
import com.fmc.edu.entity.DynamicItemEntity;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.enums.DynamicTypeEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ToastToolUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ClassDynamicActivity extends BaseActivity implements View.OnLayoutChangeListener {
    private Button btnComment;
    private EditText editComment;
    private LinearLayout llCover;
    private RelativeLayout rlComment;
    private SlideListView slideListView;
    private View view;
    private View mParentView;
    private TopBarControl topBar;
    private int mNewsId;
    private int mPosition;
    private ClassDynamicItemAdapter mAdapter;
    private List<DynamicItemEntity> mList;
    private int mPageIndex = 1;
    private boolean mIsLastPage;
    private DisplayMetrics mDisplay;
    private int mMoveDistance;
    private int mSoftWareMinHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = LayoutInflater.from(this).inflate(R.layout.activity_class_dynamic, null);
        FmcApplication.addActivity(this, view);
        Bundle bundle = getIntent().getExtras();
        mList = (List<DynamicItemEntity>) bundle.getSerializable("list");
        mIsLastPage = bundle.getBoolean("isLastPage", false);
        mDisplay = getResources().getDisplayMetrics();
        mSoftWareMinHeight = mDisplay.heightPixels / 3;
        initViews();
        initViewEvent();
        initPageData();
    }

    private void initViews() {
        slideListView = (SlideListView) findViewById(R.id.class_dynamic_slide_list);
        rlComment = (RelativeLayout) findViewById(R.id.class_dynamic_rl_comment);
        btnComment = (Button) findViewById(R.id.class_dynamic_btn_comment);
        editComment = (EditText) findViewById(R.id.class_dynamic_edit_comment);
        llCover = (LinearLayout) findViewById(R.id.class_dynamic_ll_cover);
        topBar = (TopBarControl) findViewById(R.id.class_dynamic_top_bar);
    }

    private void initViewEvent() {
        btnComment.setOnClickListener(btnCommentOnClickListener);
        slideListView.setOnLoadMoreListener(slideLoadedMoreListener);
        llCover.setOnTouchListener(llCoverOnTouchListener);
        topBar.setOnOperateOnClickListener(classDynamicOnOperateListener);
        rlComment.addOnLayoutChangeListener(onLayoutChangeListener);
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

    private View.OnTouchListener llCoverOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            hideEditComment();
            return true;
        }
    };

    private TopBarControl.OnOperateOnClickListener classDynamicOnOperateListener = new TopBarControl.OnOperateOnClickListener() {
        @Override
        public void onBackClick(View view) {
            hideEditComment();
        }

        @Override
        public void onOperateClick(View v) {

        }
    };

    private View.OnLayoutChangeListener onLayoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if (top == oldTop) {
                return;
            }
            if (top < oldTop && oldTop - top > mSoftWareMinHeight) {
                mMoveDistance = 0;
                int[] location = new int[2];
                mParentView.getLocationOnScreen(location);
                final int y = location[1];
                final int height = mParentView.getHeight();
                if ((mPosition == 0 || mPosition == 1) && y + height < bottom) {
                    return;
                }
                int otherY = y < top ? 0 : 120;
                mMoveDistance = y - top + height + otherY;
                slideListView.smoothScrollBy(mMoveDistance, 500);

            }

            if (top > oldTop && top - oldTop > mSoftWareMinHeight) {
                slideListView.smoothScrollBy(-mMoveDistance, 500);
            }
        }
    };

    private void getDynamicData() {
        mProgressControl.showWindow();
        Map<String, Object> params = new HashMap<String, Object>();
        LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
        params.put("pageIndex", mPageIndex);
        params.put("pageSize", Constant.PAGE_SIZE);
        params.put("userId", loginUserEntity.userId);
        params.put("type", DynamicTypeEnum.getValue(DynamicTypeEnum.ClassDynamic));
        params.put("classId", loginUserEntity.classId);
        MyIon.httpPost(ClassDynamicActivity.this, "news/requestNewsList", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                slideListView.setFooterViewVisible(false);
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

    private void doSendComment() {
        hideEditComment();
        mProgressControl.showWindow();
        Map<String, Object> params = new HashMap<>();
        params.put("newsId", mNewsId);
        params.put("userId", FmcApplication.getLoginUser().userId);
        char[] chars = new char[editComment.getText().length()];
        editComment.getText().getChars(0, editComment.getText().length(), chars, 0);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(chars);

        params.put("content", editComment.getText());
        MyIon.httpPost(this, "news/postComment", params, mProgressControl, new MyIon.AfterCallBack() {
            @Override
            public void afterCallBack(Map<String, Object> data) {
                ToastToolUtils.showShort("评论成功");
                CommentItemEntity commentItemEntity = new CommentItemEntity();
                LoginUserEntity loginUserEntity = FmcApplication.getLoginUser();
                commentItemEntity.userName = loginUserEntity.userName;
                commentItemEntity.userId = loginUserEntity.userId;
                commentItemEntity.comment = editComment.getText().toString();
                mAdapter.addComment(commentItemEntity, mPosition);
                editComment.setText("");


            }
        });
    }

    private void hideEditComment() {
        editComment.clearFocus();
        hideSystemSoftInputKeyboard(editComment);
        rlComment.setVisibility(View.GONE);
        llCover.setVisibility(View.GONE);
    }

    private void showEditComment() {
        rlComment.setVisibility(View.VISIBLE);
        llCover.setVisibility(View.VISIBLE);
        editComment.requestFocus();
        showSystemSoftInputKeyboard(editComment);
    }

    /**
     * 显示输入法
     *
     * @param editText
     */
    public void showSystemSoftInputKeyboard(EditText editText) {
        if (editText != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText, InputMethodManager.SHOW_FORCED);
        }
    }


    /**
     * 隐藏输入法
     *
     * @param editText
     */
    public void hideSystemSoftInputKeyboard(EditText editText) {
        if (editText != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0); //强制隐藏键盘
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, final int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {

        if (bottom == oldBottom) {
            return;
        }
        if (bottom < oldBottom && oldBottom - bottom > mSoftWareMinHeight) {
            mMoveDistance = 0;
            int[] location = new int[2];
            mParentView.getLocationOnScreen(location);
            final int y = location[1];
            final int height = mParentView.getHeight();
            if ((mPosition == 0 || mPosition == 1) && y + height < bottom) {
                return;
            }
            int otherIndex = y <= bottom ? -40 : 80;
            mMoveDistance = y - bottom + height + rlComment.getHeight() + otherIndex;
            slideListView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    slideListView.smoothScrollBy(mMoveDistance, 500);
                }
            }, 100);

        }

        if (bottom > oldBottom && bottom - oldBottom > mSoftWareMinHeight) {
            slideListView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    slideListView.smoothScrollBy(-mMoveDistance, 500);
                }
            }, 100);
        }

    }


    public void setCommentVisible(int newsId, int position, View parentView) {
        mPosition = position;
        mNewsId = newsId;
        mParentView = parentView;
        showEditComment();
    }

}