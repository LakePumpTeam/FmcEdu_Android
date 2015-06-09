package com.fmc.edu;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fmc.edu.adapter.DynamicItemGridAdapter;
import com.fmc.edu.adapter.FmcBaseAdapter;
import com.fmc.edu.common.Constant;
import com.fmc.edu.customcontrol.ExpandableTextViewControl;
import com.fmc.edu.customcontrol.ImageShowControl;
import com.fmc.edu.customcontrol.SlideListView;
import com.fmc.edu.entity.CommentItemEntity;
import com.fmc.edu.entity.DynamicItemEntity;
import com.fmc.edu.entity.ImageItemEntity;
import com.fmc.edu.entity.LoginUserEntity;
import com.fmc.edu.enums.DynamicTypeEnum;
import com.fmc.edu.http.MyIon;
import com.fmc.edu.utils.ConvertUtils;
import com.fmc.edu.utils.ImageLoaderUtil;
import com.fmc.edu.utils.ToastToolUtils;

import java.util.ArrayList;
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
    private int distance;
    private Rect r;
    private DisplayMetrics mDisplay;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FmcApplication.addActivity(this, R.layout.activity_class_dynamic);
        Bundle bundle = getIntent().getExtras();
        mList = (List<DynamicItemEntity>) bundle.getSerializable("list");
        mIsLastPage = bundle.getBoolean("isLastPage", false);
        mDisplay = getResources().getDisplayMetrics();
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
                hideSystemSoftInputKeyboard(editComment);
                rlComment.setVisibility(View.GONE);
            }
        });
    }


    /**
     * 显示输入法
     *
     * @param editText
     */
    public void showSystemSoftInputKeyboard(EditText editText) {
        if (editText != null) {
            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
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
            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getApplicationWindowToken(), 0);
        }
    }


    class ItemClick implements View.OnClickListener {

        View mParentView;

        public ItemClick(int position, int newsId, View parentView) {
            mPositon = position;
            mNewsId = newsId;
            mParentView = parentView;
        }

        @Override
        public void onClick(final View v) {
            rlComment.setVisibility(View.VISIBLE);
            showSystemSoftInputKeyboard(editComment);
            rlComment.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    if (r == null) {
                        r = new Rect();
                    }
                    r.setEmpty();
                    rlComment.getGlobalVisibleRect(r);
                    rlComment.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            });

            int[] location = new int[2];
            mParentView.getLocationOnScreen(location);
            final int y = location[1];
            mParentView.getLocationOnScreen(location);

            final int height = mParentView.getHeight();
            slideListView.postDelayed(new Runnable() {

                @Override
                public void run() {
                    if (r != null) {
                        distance = y - r.top;
                        slideListView.smoothScrollBy(distance + height + (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, mDisplay), 500);
                    }
                }
            }, 100);
        }
    }

    class ClassDynamicItemAdapter extends FmcBaseAdapter<DynamicItemEntity> {
        private final SparseBooleanArray mCollapsedStatus;

        public ClassDynamicItemAdapter(Context context, List<DynamicItemEntity> items) {
            super(context, items);
            mCollapsedStatus = new SparseBooleanArray();
        }

        public void addComment(CommentItemEntity commentItemEntity, int positon) {
            if (null == mItems.get(positon).commentList) {
                mItems.get(positon).commentList = new ArrayList<CommentItemEntity>();
            }

            mItems.get(positon).commentList.add(commentItemEntity);
            mItems.get(positon).commentCount++;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (null == mItems) {
                return convertView;
            }
            if (null == convertView) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_class_danamic_list, null);
            }
            TextView txtDate = (TextView) convertView.findViewById(R.id.item_class_dynamic_list_txt_date);
            TextView txtComment = (TextView) convertView.findViewById(R.id.item_class_dynamic_list_txt_comment);
            GridView gridView = (GridView) convertView.findViewById(R.id.item_class_dynamic_list_grid_picture);
            LinearLayout commentView = (LinearLayout) convertView.findViewById(R.id.item_class_dynamic_list_ll_comment);
            ExpandableTextViewControl expand_text_view = (ExpandableTextViewControl) convertView.findViewById(R.id.expand_text_view);

            DynamicItemEntity item = mItems.get(position);
            expand_text_view.setText(item.content, mCollapsedStatus, position);
            txtComment.setText(ConvertUtils.getString(item.commentCount, "0"));
            txtDate.setText(item.createDate);


            List<CommentItemEntity> commentList = item.commentList;
            commentView.removeAllViews();
            for (int i = 0; i < commentList.size(); i++) {
                String userName = commentList.get(i).userName + "：";
                String comment = commentList.get(i).comment;
                TextView textView = createText(userName, comment);
                commentView.addView(textView);
            }
            DynamicItemGridAdapter dynamicItemGridAdapter = new DynamicItemGridAdapter(mContext, item.imageUrls, ImageLoaderUtil.initCacheImageLoader(mContext));
            gridView.setAdapter(dynamicItemGridAdapter);
            gridView.setOnItemClickListener(gridOnItemClickListener);
            txtComment.setOnClickListener(new ItemClick(position, item.newsId, commentView));
            return convertView;
        }

        private TextView createText(String userName, String comment) {
            SpannableStringBuilder builder = new SpannableStringBuilder(userName + comment);
            TextView textView = new TextView(mContext);
            ForegroundColorSpan userNameSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.text_parent_name_font_color));
            ForegroundColorSpan commentSpan = new ForegroundColorSpan(mContext.getResources().getColor(R.color.dynamic_class_dynamic_color));
            builder.setSpan(userNameSpan, 0, userName.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            builder.setSpan(commentSpan, userName.length(), userName.length() + comment.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            textView.setText(builder);
            return textView;
        }


        private AdapterView.OnItemClickListener gridOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                List<ImageItemEntity> imageList = ((DynamicItemGridAdapter) parent.getAdapter()).getItems();
                ImageShowControl imageShowControl = new ImageShowControl(mContext);
                imageShowControl.showWindow(view, getOrigUrl(imageList), position);
            }
        };

        private List<String> getOrigUrl(List<ImageItemEntity> list) {
            List<String> origUrls = new ArrayList<String>();
            for (int i = 0; i < list.size(); i++) {
                origUrls.add(list.get(i).origUrl);
            }
            return origUrls;
        }


    }
}