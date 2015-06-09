package com.fmc.edu;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.fmc.edu.adapter.ClassDynamicItemAdapter;
import com.fmc.edu.common.Constant;
import com.fmc.edu.customcontrol.SlideImageControl;
import com.fmc.edu.customcontrol.SlideListView;
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


public class ClassDynamicActivity extends BaseActivity {
    private SlideListView slideListView;
    private RelativeLayout rlComment;

    private EditText editComment;
    private EditText editPopUpComment;
    private Button btnComment;
    private int mNewsId;
    private int mPositon;
    private ClassDynamicItemAdapter mAdapter;
    private List<DynamicItemEntity> mList;
    private int mPageIndex = 1;
    private boolean mIsLastPage;
    private LinearLayout tttt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_class_dynamic);
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
        tttt = (LinearLayout) findViewById(R.id.tttt);
    }

    private void initViewEvent() {
        btnComment.setOnClickListener(btnCommentOnClickListener);
        slideListView.setOnLoadMoreListener(slideLoadedMoreListener);
        slideListView.setOnScrollPrepListener(slideOnScrollPrepListener);
        tttt.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                rlComment.setVisibility(View.GONE);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(editComment.getWindowToken(), 0);
                return false;
            }
        });
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
//            rlComment.setVisibility(View.GONE);
        }
    };

    private void getDynamicData() {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("pageIndex", mPageIndex);
        params.put("pageSize", Constant.PAGE_SIZE);
        params.put("userId", FmcApplication.getLoginUser().userId);
        params.put("type", DynamicTypeEnum.getValue(DynamicTypeEnum.ClassDynamic));
        MyIon.httpPost(ClassDynamicActivity.this, "news/requestNewsList", params, null, new MyIon.AfterCallBack() {
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
        mProgressControl.showWindow();
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("newsId", mNewsId);
        params.put("userId", FmcApplication.getLoginUser().userId);
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
                mAdapter.addComment(commentItemEntity, mPositon);
                editComment.setText("");
                rlComment.setVisibility(View.GONE);
            }
        });
    }

    public void setCommentVisible(int newsId, int position, View view) {
        mNewsId = newsId;
        mPositon = position;
        rlComment.setVisibility(View.VISIBLE);
        if (!(getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_STATE_UNSPECIFIED)) {
            editComment.setFocusableInTouchMode(true);
            editComment.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editComment, 0);
        }
        slideListView.smoothScrollToPositionFromTop(mPositon, slideListView.getHeight() - view.getHeight() - rlComment.getHeight());

//        SendCommentPopWindow sendCommentPopWindow = new SendCommentPopWindow(this);
//        sendCommentPopWindow.showWindow(view);
//        slideListView.smoothScrollToPositionFromTop(mPositon, slideListView.getHeight() - view.getHeight());
    }

    private class SendCommentPopWindow extends PopupWindow {
        private Context mContext;
        private DisplayMetrics mDisplayMetrics;

        public SendCommentPopWindow(Context context) {
            super(context, null);
            this.mContext = context;
            mDisplayMetrics = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(mDisplayMetrics);
            initPopWindow();
            initContentView();
        }

        private void initPopWindow() {
            this.setWidth(mDisplayMetrics.widthPixels);
            this.setHeight(mDisplayMetrics.heightPixels);
            ColorDrawable dw = new ColorDrawable(-000000);
            this.setTouchable(true);
            this.setFocusable(true); // 设置PopupWindow可获得焦点
            this.setBackgroundDrawable(dw);
        }

        private void initContentView() {
            LinearLayout linearLayout = new LinearLayout(mContext);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(mDisplayMetrics.widthPixels, mDisplayMetrics.heightPixels);
            linearLayout.setLayoutParams(params);
            linearLayout.setGravity(Gravity.CENTER);
            linearLayout.setBackgroundColor(Color.parseColor("#00ffffff"));
            View view = LayoutInflater.from(mContext).inflate(R.layout.popup_edit_comment, null);
            editPopUpComment = (EditText) view.findViewById(R.id.popup_edit_comment_edit_comment);
            linearLayout.setOnTouchListener(onTouchListener);
//打开键盘，设置延时时长
            linearLayout.addView(view);
            this.setContentView(linearLayout);
        }

        public void showWindow(View parentView) {
            this.showAtLocation(parentView, Gravity.CENTER, 0, 0);
            openKeyboard(new Handler(), 100);
        }

        private SlideImageControl.OnSlideItemClickListener onSlideItemClickListener = new SlideImageControl.OnSlideItemClickListener() {
            @Override
            public void onSlideItemClick() {
            }
        };

        private void openKeyboard(Handler mHandler, int s) {
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.toggleSoftInput(0, InputMethodManager.RESULT_SHOWN);
                }
            }, s);
        }


        private View.OnTouchListener onTouchListener = new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (SendCommentPopWindow.this.isShowing()) {
                    SendCommentPopWindow.this.dismiss();
                }
                return false;
            }
        };
    }

}
